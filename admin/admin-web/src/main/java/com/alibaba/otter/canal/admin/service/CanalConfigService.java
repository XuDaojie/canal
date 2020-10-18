package com.alibaba.otter.canal.admin.service;

import com.alibaba.otter.canal.admin.model.CanalConfig;
import com.alibaba.otter.canal.admin.model.Pager;

public interface CanalConfigService {

    void save(CanalConfig canalConfig);

    CanalConfig getCanalConfig(Long clusterId, Long serverId, String name);

    CanalConfig getCanalConfig(Long id);

    Pager<CanalConfig> findClientList(Pager<CanalConfig> pager);

    CanalConfig getCanalConfigSummary();

    CanalConfig getClientConfig();

    void updateContent(CanalConfig canalConfig);

}
