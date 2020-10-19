package com.alibaba.otter.canal.admin.service.impl;

import com.alibaba.otter.canal.admin.common.exception.ServiceException;
import com.alibaba.otter.canal.admin.model.CanalAdapterConfig;
import com.alibaba.otter.canal.admin.model.CanalCluster;
import com.alibaba.otter.canal.admin.model.CanalInstanceConfig;
import com.alibaba.otter.canal.admin.model.NodeClient;
import com.alibaba.otter.canal.admin.model.NodeServer;
import com.alibaba.otter.canal.admin.service.CanalClusterService;

import org.springframework.stereotype.Service;

import java.util.List;

import io.ebean.Query;

@Service
public class CanalClusterServiceImpl implements CanalClusterService {

    public void save(CanalCluster canalCluster) {
        canalCluster.save();
    }

    public CanalCluster detail(Long id) {
        return CanalCluster.find.byId(id);
    }

    public void update(CanalCluster canalCluster) {
        canalCluster.update("name", "zkHosts");
    }

    public void delete(Long id) {
        // 判断集群下是否存在server信息
        int serverCnt = NodeServer.find.query().where().eq("clusterId", id).findCount();
        if (serverCnt > 0) {
            throw new ServiceException("当前集群下存在Server, 无法删除");
        }

        // 判断集群下是否存在instance信息
        int instanceCnt = CanalInstanceConfig.find.query().where().eq("clusterId", id).findCount();
        if (instanceCnt > 0) {
            throw new ServiceException("当前集群下存在Instance配置，无法删除");
        }

        // 判断集群下是否存在client信息
        int clientCnt = NodeClient.find.query().where().eq("clusterId", id).findCount();
        if (clientCnt > 0) {
            throw new ServiceException("当前集群下存在Client配置，无法删除");
        }

        // 判断集群下是否存在adapter信息
        int adapterCnt = CanalAdapterConfig.find.query().where().eq("clusterId", id).findCount();
        if (adapterCnt > 0) {
            throw new ServiceException("当前集群下存在Adapter配置，无法删除");
        }

        CanalCluster canalCluster = CanalCluster.find.byId(id);
        if (canalCluster != null) {
            canalCluster.delete();
        }
    }

    public List<CanalCluster> findList(CanalCluster canalCluster) {
        Query<CanalCluster> query = CanalCluster.find.query();
        query.order().asc("id");
        return query.findList();
    }
}
