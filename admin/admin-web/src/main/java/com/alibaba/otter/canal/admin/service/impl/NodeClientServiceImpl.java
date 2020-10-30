package com.alibaba.otter.canal.admin.service.impl;

import com.alibaba.otter.canal.admin.common.TemplateConfigLoader;
import com.alibaba.otter.canal.admin.common.exception.ServiceException;
import com.alibaba.otter.canal.admin.model.CanalConfig;
import com.alibaba.otter.canal.admin.model.NodeClient;
import com.alibaba.otter.canal.admin.model.Pager;
import com.alibaba.otter.canal.admin.rest.ClientAdapterApi;
import com.alibaba.otter.canal.admin.service.NodeClientService;
import com.alibaba.otter.canal.protocol.SecurityUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import io.ebean.Query;

/**
 * @author XuDaojie
 * @date 2020/10/8
 * @since 1.1.5
 */
@Service
public class NodeClientServiceImpl implements NodeClientService {

    @Override
    public void save(NodeClient nodeClient) {
        int cnt = NodeClient.find.query()
                .where()
                .eq("ip", nodeClient.getIp())
                .eq("port", nodeClient.getPort())
                .findCount();
        if (cnt > 0) {
            throw new ServiceException("节点信息已存在");
        }

        nodeClient.save();

        if (nodeClient.getClusterId() == null) { // 单机模式
            CanalConfig canalConfig = new CanalConfig();
            canalConfig.setServerId(nodeClient.getId());
            String configTmp = TemplateConfigLoader.loadCanalClientConfig();
            canalConfig.setContent(configTmp);
            try {
                String contentMd5 = SecurityUtil.md5String(canalConfig.getContent());
                canalConfig.setContentMd5(contentMd5);
            } catch (NoSuchAlgorithmException e) {
            }
            canalConfig.setName(CanalConfig.CLIENT);
            canalConfig.save();
        }
    }

    @Override
    public NodeClient detail(Long id) {
        return null;
    }

    @Override
    public void update(NodeClient nodeClient) {
        int cnt = NodeClient.find.query()
                .where()
                .eq("ip", nodeClient.getIp())
                .eq("port", nodeClient.getPort())
                .ne("id", nodeClient.getId())
                .findCount();
        if (cnt > 0) {
            throw new ServiceException("节点信息已存在");
        }

        nodeClient.update("name", "ip", "port", "clusterId");
    }

    @Override
    public void delete(Long id) {
        NodeClient nodeClient = NodeClient.find.byId(id);
        if (nodeClient != null) {
            // todo 判断是否存在adapter
//            int cnt = CanalInstanceConfig.find.query().where().eq("serverId", nodeClient.getId()).findCount();
//            if (cnt > 0) {
//                throw new ServiceException("当前Client下存在Adapter配置, 无法删除");
//            }

            // 同时删除配置
            CanalConfig canalConfig = CanalConfig.find.query().where().eq("serverId", id).findOne();
            if (canalConfig != null) {
                canalConfig.delete();
            }

            nodeClient.delete();
        }
    }

    @Override
    public List<NodeClient> findAll(NodeClient nodeClient) {
        Query<NodeClient> query = getBaseQuery(nodeClient);
        query.order().asc("id");
        return query.findList();
    }

    @Override
    public Pager<NodeClient> findList(NodeClient nodeClient, Pager<NodeClient> pager) {
        Query<NodeClient> query = getBaseQuery(nodeClient);
        Query<NodeClient> queryCnt = query.copy();

        int count = queryCnt.findCount();
        pager.setCount((long) count);

        List<NodeClient> nodeClients = query.order()
                .asc("id")
                .setFirstRow(pager.getOffset().intValue())
                .setMaxRows(pager.getSize())
                .findList();
        pager.setItems(nodeClients);

        return pager;
    }

    @Override
    public int remoteNodeStatus(String ip, Integer port) {
        return 0;
    }

    @Override
    public String remoteCanalLog(Long id) {
        NodeClient nodeClient = NodeClient.find.byId(id);

        String url = MessageFormat.format("http://{0}:{1}", nodeClient.getIp(), nodeClient.getPort());
        ClientAdapterApi api =ClientAdapterApi.apis.get(url);
        Map<String, Object> canalLog = api.canalLog(100);
        return String.valueOf(canalLog.get("data"));
    }

    @Override
    public boolean remoteOperation(Long id, String option) {
        return false;
    }

    private Query<NodeClient> getBaseQuery(NodeClient nodeClient) {
        Query<NodeClient> query = NodeClient.find.query();
        query.fetch("canalCluster", "name").setDisableLazyLoading(true);

        if (nodeClient != null) {
            if (StringUtils.isNotEmpty(nodeClient.getName())) {
                query.where().like("name", "%" + nodeClient.getName() + "%");
            }
            if (StringUtils.isNotEmpty(nodeClient.getIp())) {
                query.where().eq("ip", nodeClient.getIp());
            }
            if (nodeClient.getClusterId() != null) {
                if (nodeClient.getClusterId() == -1) {
                    query.where().isNull("clusterId");
                } else {
                    query.where().eq("clusterId", nodeClient.getClusterId());
                }
            }
        }

        return query;
    }
}
