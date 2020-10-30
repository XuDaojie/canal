package com.alibaba.otter.canal.admin.controller;

import com.alibaba.otter.canal.admin.common.TemplateConfigLoader;
import com.alibaba.otter.canal.admin.model.BaseModel;
import com.alibaba.otter.canal.admin.model.CanalConfig;
import com.alibaba.otter.canal.admin.service.CanalConfigService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Canal主配置管理控制层
 *
 * @author rewerma 2019-07-13 下午05:12:16
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/{env}/canal")
public class CanalConfigController {

    @Autowired
    CanalConfigService canalConfigService;

    /**
     * 获取配置信息
     *
     * @param env 环境变量
     * @return 配置信息
     */
    @GetMapping(value = "/config/{clusterId}/{serverId}")
    public BaseModel<CanalConfig> canalConfig(@PathVariable Long clusterId, @PathVariable Long serverId,
                                              @PathVariable String env,
                                              @RequestParam(value = "name",defaultValue = "canal.properties") String name) {
        return BaseModel.getInstance(canalConfigService.getCanalConfig(clusterId, serverId, name));
    }

    /**
     * 新增配置信息
     *
     * @param env 环境变量
     * @return 配置信息
     */
    @PostMapping(value = "/config")
    public BaseModel<String> save(@RequestBody CanalConfig canalConfig,
                                              @PathVariable String env) {
        canalConfigService.save(canalConfig);
        return BaseModel.getInstance("success");
    }

    /**
     * 修改配置
     *
     * @param canalConfig 配置信息对象
     * @param env 环境变量
     * @return 是否成功
     */
    @PutMapping(value = "/config")
    public BaseModel<String> updateConfig(@RequestBody CanalConfig canalConfig, @PathVariable String env) {
        canalConfigService.updateContent(canalConfig);
        return BaseModel.getInstance("success");
    }

    @GetMapping(value = "/config/template")
    public BaseModel<String> template(@PathVariable String env) {
        return BaseModel.getInstance(TemplateConfigLoader.loadCanalConfig());
    }

    @GetMapping(value = "/config/client/template")
    public BaseModel<String> clientTemplate(@PathVariable String env) {
        return BaseModel.getInstance(TemplateConfigLoader.loadCanalClientConfig());
    }
}
