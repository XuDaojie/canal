package com.alibaba.otter.canal.admin.service;

import com.alibaba.otter.canal.admin.model.CanalAdapterConfig;
import com.alibaba.otter.canal.admin.model.CanalInstanceConfig;
import com.alibaba.otter.canal.admin.model.Pager;

import java.util.List;
import java.util.Map;

/**
 * Canal-Adapter配置信息业务层接口
 *
 * @author XuDaojie 2020-09-21
 * @since  1.1.5
 */
public interface CanalAdapterService {

    Pager<CanalAdapterConfig> findList(CanalAdapterConfig canalAdapterConfig, Pager<CanalAdapterConfig> pager);

    void save(CanalAdapterConfig canalAdapterConfig);

    CanalAdapterConfig detail(Long id);

    void updateContent(CanalAdapterConfig canalAdapterConfig);

    void delete(Long id);

    Map<String, String> remoteInstanceLog(Long id, Long nodeId);

    boolean remoteOperation(Long id, Long nodeId, String option);

    boolean instanceOperation(Long id, String option);

    List<CanalInstanceConfig> findActiveInstanceByServerId(Long serverId);
}
