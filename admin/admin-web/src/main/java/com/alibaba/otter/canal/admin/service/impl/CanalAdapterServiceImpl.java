package com.alibaba.otter.canal.admin.service.impl;

import com.alibaba.otter.canal.admin.common.exception.ServiceException;
import com.alibaba.otter.canal.admin.connector.AdminConnector;
import com.alibaba.otter.canal.admin.connector.SimpleAdminConnectors;
import com.alibaba.otter.canal.admin.model.CanalAdapterConfig;
import com.alibaba.otter.canal.admin.model.CanalInstanceConfig;
import com.alibaba.otter.canal.admin.model.NodeServer;
import com.alibaba.otter.canal.admin.model.Pager;
import com.alibaba.otter.canal.admin.service.CanalAdapterService;
import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import io.ebean.Query;

/**
 * Canal-Adapter配置信息业务层
 *
 * @author XuDaojie 2020-09-21
 * @since  1.1.5
 */
@Service
public class CanalAdapterServiceImpl implements CanalAdapterService {

    @Override
    public Pager<CanalAdapterConfig> findList(CanalAdapterConfig canalAdapterConfig, Pager<CanalAdapterConfig> pager) {
        Query<CanalAdapterConfig> query = CanalAdapterConfig.find.query()
                .select("*");
        List<CanalAdapterConfig> canalAdapterConfigs = query.findList();
        pager.setItems(canalAdapterConfigs);
        pager.setCount((long) pager.getItems().size()); // todo 修改为getcount
        pager.getItems().forEach(new Consumer<CanalAdapterConfig>() {
            @Override
            public void accept(CanalAdapterConfig canalAdapterConfig) {
                canalAdapterConfig.setRunningStatus("1");
            }
        });
        return pager;
    }

    /**
     * 通过Server id获取当前Server下所有运行的Instance
     *
     * @param serverId server id
     */
    @Override
    public List<CanalInstanceConfig> findActiveInstanceByServerId(Long serverId) {
        NodeServer nodeServer = NodeServer.find.byId(serverId);
        if (nodeServer == null) {
            return null;
        }
        String runningInstances = SimpleAdminConnectors.execute(nodeServer.getIp(),
            nodeServer.getAdminPort(),
            AdminConnector::getRunningInstances);
        if (runningInstances == null) {
            return null;
        }

        String[] instances = runningInstances.split(",");
        Object obj[] = Lists.newArrayList(instances).toArray();
        // 单机模式和集群模式区分处理
        if (nodeServer.getClusterId() != null) { // 集群模式
            List<CanalInstanceConfig> list = CanalInstanceConfig.find.query()
                .setDisableLazyLoading(true)
                .select("clusterId, serverId, name, modifiedTime")
                .where()
                // 暂停的实例也显示 .eq("status", "1")
                .in("name", obj)
                .findList();
            list.forEach(config -> config.setRunningStatus("1"));
            return list; // 集群模式直接返回当前运行的Instances
        } else { // 单机模式
            // 当前Server所配置的所有Instance
            List<CanalInstanceConfig> list = CanalInstanceConfig.find.query()
                .setDisableLazyLoading(true)
                .select("clusterId, serverId, name, modifiedTime")
                .where()
                // 暂停的实例也显示 .eq("status", "1")
                .eq("serverId", serverId)
                .findList();
            List<String> instanceList = Arrays.asList(instances);
            list.forEach(config -> {
                if (instanceList.contains(config.getName())) {
                    config.setRunningStatus("1");
                }
            });
            return list;
        }
    }

    @Override
    public void save(CanalAdapterConfig canalAdapterConfig) {
        if (StringUtils.isEmpty(canalAdapterConfig.getCategory())) {
            throw new ServiceException("empty category");
        }

        canalAdapterConfig.insert();
    }

    @Override
    public CanalAdapterConfig detail(Long id) {
        return CanalAdapterConfig.find.byId(id);
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
}
