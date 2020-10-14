package com.alibaba.otter.canal.admin.service;

import com.alibaba.otter.canal.admin.model.CanalClientInstanceConfig;

import java.util.List;

/**
 * Canal Client Instance配置管理
 *
 * @author XuDaojie
 * @date 2020-10-13
 * @since 1.1.5
 */
public interface CanalClientInstanceService {

    boolean remoteOperation(String instance, Long nodeId, String option);

    boolean instanceOperation(Long id, String option);

    List<CanalClientInstanceConfig> findInstanceByClientId(Long clientId);
}
