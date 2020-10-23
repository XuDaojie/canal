package com.alibaba.otter.canal.admin.service.impl;

import com.alibaba.otter.canal.admin.common.exception.ServiceException;
import com.alibaba.otter.canal.admin.connector.SimpleAdminConnectors;
import com.alibaba.otter.canal.admin.model.CanalAdapterConfig;
import com.alibaba.otter.canal.admin.model.CanalInstanceConfig;
import com.alibaba.otter.canal.admin.model.NodeClient;
import com.alibaba.otter.canal.admin.model.NodeServer;
import com.alibaba.otter.canal.admin.model.Pager;
import com.alibaba.otter.canal.admin.rest.ClientAdapterApi;
import com.alibaba.otter.canal.admin.service.CanalAdapterService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.yaml.snakeyaml.Yaml;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.Resource;

import io.ebean.Query;

/**
 * Canal-Adapter配置信息业务层
 *
 * @author XuDaojie 2020-09-21
 * @since 1.1.5
 */
@Service
public class CanalAdapterServiceImpl implements CanalAdapterService {

    private static final Logger logger = LoggerFactory.getLogger(CanalInstanceServiceImpl.class.getName());

    @Resource(name = "dynamicTemplateEngine")
    private TemplateEngine      dynamicTemplateEngine;

    @Override
    public Pager<CanalAdapterConfig> findList(CanalAdapterConfig canalAdapterConfig, Pager<CanalAdapterConfig> pager) {
        Query<CanalAdapterConfig> query = getBaseQuery(canalAdapterConfig).select("*");
        Query<CanalAdapterConfig> queryCnt = query.copy();

        int cnt = queryCnt.findCount();
        pager.setCount((long) cnt);

        List<CanalAdapterConfig> canalAdapterConfigs = query.order()
            .asc("id")
            .setFirstRow(pager.getOffset().intValue())
            .setMaxRows(pager.getSize())
            .findList();
        pager.setItems(canalAdapterConfigs);
        pager.getItems().forEach(new Consumer<CanalAdapterConfig>() {

            @Override
            public void accept(CanalAdapterConfig canalAdapterConfig) {
                // 默认为断开状态
                canalAdapterConfig.setRunningStatus("-1");

                // todo 并行调用
                Yaml yaml = new Yaml();
                Map<String, Object> ymlMap;
                try {
                    ymlMap = yaml.load(canalAdapterConfig.getContent());
                } catch (Exception e) {
                    logger.warn("配置文件格式错误", e);
                    return;
                }
                if (ymlMap == null || ymlMap.get("destination") == null) return;

                String destination = String.valueOf(ymlMap.get("destination"));
                List<NodeClient> nodeClients;
                if (canalAdapterConfig.getClusterId() != null) {
                    // cluster
                    nodeClients = NodeClient.find.query()
                        .where()
                        .eq("clusterId", canalAdapterConfig.getClusterId())
                        .findList();
                } else {
                    // single
                    nodeClients = Collections.singletonList(canalAdapterConfig.getNodeClient());
                }

                for (NodeClient nodeClient : nodeClients) {
                    String url = MessageFormat.format("http://{0}:{1}", nodeClient.getIp(), nodeClient.getPort());
                    try {
                        ClientAdapterApi api = ClientAdapterApi.apis.get(url);
                        Map<String, String> result = api.syncSwitch(destination);
                        canalAdapterConfig.setRunningStatus("on".equals(result.get("status")) ? "1" : "0");
                        break;
                    } catch (Exception e) {
                        logger.warn("client ip 、port配置错误或无法连接client", e);
                    }
                }
            }
        });

        return pager;
    }

    @Override
    public void save(CanalAdapterConfig canalAdapterConfig) {
        if (StringUtils.isEmpty(canalAdapterConfig.getCategory())) {
            throw new ServiceException("empty category");
        }
        if (canalAdapterConfig.getClusterClientId().startsWith("cluster:")) {
            Long clusterId = Long.parseLong(canalAdapterConfig.getClusterClientId().substring(8));
            canalAdapterConfig.setClusterId(clusterId);
        } else if (canalAdapterConfig.getClusterClientId().startsWith("client:")) {
            Long serverId = Long.parseLong(canalAdapterConfig.getClusterClientId().substring(7));
            canalAdapterConfig.setClientId(serverId);
        }

        canalAdapterConfig.insert();
    }

    @Override
    public void batchSave(List<CanalAdapterConfig> canalAdapterConfigs) {
        for (CanalAdapterConfig canalAdapterConfig : canalAdapterConfigs) {
            String content = processTemplate(canalAdapterConfig);
            canalAdapterConfig.setContent(content);

            // todo 批量保存
            save(canalAdapterConfig);
        }
    }

    @Override
    public CanalAdapterConfig detail(Long id) {
        CanalAdapterConfig canalAdapterConfig = CanalAdapterConfig.find.byId(id);
        if (canalAdapterConfig != null) {
            if (canalAdapterConfig.getClusterId() != null) {
                canalAdapterConfig.setClusterClientId("cluster:" + canalAdapterConfig.getClusterId());
            } else if (canalAdapterConfig.getClientId() != null) {
                canalAdapterConfig.setClusterClientId("server:" + canalAdapterConfig.getClientId());
            }
        }
        return canalAdapterConfig;
    }

    @Override
    public void updateContent(CanalAdapterConfig canalAdapterConfig) {
        if (StringUtils.isEmpty(canalAdapterConfig.getCategory())) {
            throw new ServiceException("empty category");
        }

        canalAdapterConfig.update("category", "content");
    }

    @Override
    public void delete(Long id) {
        CanalAdapterConfig canalInstanceConfig = CanalAdapterConfig.find.byId(id);
        if (canalInstanceConfig != null) {
            canalInstanceConfig.delete();
        }
    }

    public Map<String, String> remoteInstanceLog(Long id, Long nodeId) {
        Map<String, String> result = new HashMap<>();

        NodeServer nodeServer = NodeServer.find.byId(nodeId);
        if (nodeServer == null) {
            return result;
        }
        CanalInstanceConfig canalInstanceConfig = CanalInstanceConfig.find.byId(id);
        if (canalInstanceConfig == null) {
            return result;
        }

        String log = SimpleAdminConnectors.execute(nodeServer.getIp(),
            nodeServer.getAdminPort(),
            adminConnector -> adminConnector.instanceLog(canalInstanceConfig.getName(), null, 100));

        result.put("instance", canalInstanceConfig.getName());
        result.put("log", log);
        return result;
    }

    public boolean remoteOperation(Long id, Long nodeId, String option) {
        NodeServer nodeServer = null;
        if ("start".equals(option)) {
            if (nodeId != null) {
                nodeServer = NodeServer.find.byId(nodeId);
            } else {
                nodeServer = NodeServer.find.query().findOne();
            }
        } else {
            if (nodeId == null) {
                return false;
            }
            nodeServer = NodeServer.find.byId(nodeId);
        }
        if (nodeServer == null) {
            return false;
        }
        CanalInstanceConfig canalInstanceConfig = CanalInstanceConfig.find.byId(id);
        if (canalInstanceConfig == null) {
            return false;
        }
        Boolean result = null;
        if ("start".equals(option)) {
            if (nodeServer.getClusterId() == null) { // 非集群模式
                return instanceOperation(id, "start");
            } else {
                throw new ServiceException("集群模式不允许指定server启动");
            }
        } else if ("stop".equals(option)) {
            if (nodeServer.getClusterId() != null) {
                // 集群模式,通知主动释放
                result = SimpleAdminConnectors.execute(nodeServer.getIp(),
                    nodeServer.getAdminPort(),
                    adminConnector -> adminConnector.releaseInstance(canalInstanceConfig.getName()));
            } else { // 非集群模式下直接将状态置为0
                return instanceOperation(id, "stop");
            }
        } else {
            return false;
        }

        if (result == null) {
            result = false;
        }
        return result;
    }

    public boolean instanceOperation(Long id, String option) {
        CanalInstanceConfig canalInstanceConfig = CanalInstanceConfig.find.byId(id);
        if (canalInstanceConfig == null) {
            return false;
        }
        if ("stop".equals(option)) {
            canalInstanceConfig.setStatus("0");
            canalInstanceConfig.update("status");
        } else if ("start".equals(option)) {
            canalInstanceConfig.setStatus("1");
            canalInstanceConfig.update("status");
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String previewTemplate(CanalAdapterConfig canalAdapterConfig) {
        return processTemplate(canalAdapterConfig);
    }

    private Query<CanalAdapterConfig> getBaseQuery(CanalAdapterConfig canalAdapterConfig) {
        Query<CanalAdapterConfig> query = CanalAdapterConfig.find.query();
        if (StringUtils.isNotBlank(canalAdapterConfig.getName())) {
            query.where().eq("name", canalAdapterConfig.getName());
        }

        return query;
    }

    private String processTemplate(CanalAdapterConfig canalAdapterConfig) {
        String template = canalAdapterConfig.getContent();
        Context context = new Context();
        String[] dbTable = StringUtils.removeEnd(canalAdapterConfig.getName(), ".yml").split("::");
        Map<String, String> dbMapping = new HashMap<>();
        dbMapping.put("database", dbTable[0]);
        dbMapping.put("table", dbTable[1]);
        dbMapping.put("targetTable", dbTable[1]);
        context.setVariable("dbMapping", dbMapping);
        return dynamicTemplateEngine.process(template, context);
    }
}
