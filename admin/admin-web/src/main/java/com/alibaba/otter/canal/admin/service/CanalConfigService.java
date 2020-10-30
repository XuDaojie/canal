package com.alibaba.otter.canal.admin.service;

import com.alibaba.otter.canal.admin.model.CanalConfig;

public interface CanalConfigService {

    void save(CanalConfig canalConfig);

    CanalConfig getCanalConfig(Long clusterId, Long serverId, String name);

    CanalConfig getCanalConfigSummary();

    void updateContent(CanalConfig canalConfig);

}
