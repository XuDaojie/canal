package com.alibaba.otter.canal.admin.controller;

import com.alibaba.otter.canal.admin.common.TemplateConfigLoader;
import com.alibaba.otter.canal.admin.model.BaseModel;
import com.alibaba.otter.canal.admin.model.CanalAdapterConfig;
import com.alibaba.otter.canal.admin.model.Pager;
import com.alibaba.otter.canal.admin.service.CanalAdapterService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Canal Adapter配置管理控制层
 *
 * @author XuDaojie 2020-09-22
 * @version 1.1.5
 */
@RestController
@RequestMapping("/api/{env}/canal")
public class CanalAdapterController {

    @Resource
    CanalAdapterService canalAdapterService;

    /**
     * 实例配置列表
     *
     * @param canalAdapterConfig 查询对象
     * @param env 环境变量
     * @return 列表
     */
    @GetMapping(value = "/adapters")
    public BaseModel<Pager<CanalAdapterConfig>> list(CanalAdapterConfig canalAdapterConfig,
                                                      Pager<CanalAdapterConfig> pager, @PathVariable String env) {
        return BaseModel.getInstance(canalAdapterService.findList(canalAdapterConfig, pager));
    }

    /**
     * 保存实例配置
     *
     * @param canalAdapterConfig 配置对象
     * @param env 环境变量
     * @return 是否成功
     */
    @PostMapping(value = "/adapter")
    public BaseModel<String> save(@RequestBody CanalAdapterConfig canalAdapterConfig, @PathVariable String env) {
        canalAdapterService.save(canalAdapterConfig);
        return BaseModel.getInstance("success");
    }

    /**
     * 实例详情信息
     *
     * @param id 配置id
     * @param env 环境变量
     * @return 信息
     */
    @GetMapping(value = "/adapter")
    public BaseModel<CanalAdapterConfig> config(@PathVariable Long id, @PathVariable String env) {
        return BaseModel.getInstance(canalAdapterService.detail(id));
    }

    /**
     * 实例详情信息
     *
     * @param id 配置id
     * @param env 环境变量
     * @return 实例信息
     */
    @GetMapping(value = "/adapter/{id}")
    public BaseModel<CanalAdapterConfig> detail(@PathVariable Long id, @PathVariable String env) {
        return BaseModel.getInstance(canalAdapterService.detail(id));
    }

    /**
     * 修改实例配置
     *
     * @param canalAdapterConfig 配置信息
     * @param env 环境变量
     * @return 是否成功
     */
    @PutMapping(value = "/adapter")
    public BaseModel<String> update(@RequestBody CanalAdapterConfig canalAdapterConfig, @PathVariable String env) {
        canalAdapterService.updateContent(canalAdapterConfig);
        return BaseModel.getInstance("success");
    }

    /**
     * 删除实例配置
     *
     * @param id 配置id
     * @param env 环境变量
     * @return 是否成功
     */
    @DeleteMapping(value = "/adapter/{id}")
    public BaseModel<String> delete(@PathVariable Long id, @PathVariable String env) {
        canalAdapterService.delete(id);
        return BaseModel.getInstance("success");
    }
//
//    /**
//     * 启动远程实例
//     *
//     * @param id 实例配置id
//     * @param env 环境变量
//     * @return 是否成功
//     */
//    @PutMapping(value = "/instance/start/{id}/{nodeId}")
//    public BaseModel<Boolean> start(@PathVariable Long id, @PathVariable Long nodeId, @PathVariable String env) {
//        return BaseModel.getInstance(canalInstanceConfigService.remoteOperation(id, nodeId, "start"));
//    }
//
//    /**
//     * 关闭远程实例
//     *
//     * @param id 实例配置id
//     * @param nodeId 节点id
//     * @param env 环境变量
//     * @return 是否成功
//     */
//    @PutMapping(value = "/instance/stop/{id}/{nodeId}")
//    public BaseModel<Boolean> stop(@PathVariable Long id, @PathVariable Long nodeId, @PathVariable String env) {
//        return BaseModel.getInstance(canalInstanceConfigService.remoteOperation(id, nodeId, "stop"));
//    }
//
//    /**
//     * 通过操作instance状态启动/停止远程instance
//     *
//     * @param id 实例配置id
//     * @param option 操作类型: start/stop
//     * @param env 环境变量
//     * @return 是否成功
//     */
//    @PutMapping(value = "/instance/status/{id}")
//    public BaseModel<Boolean> instanceStart(@PathVariable Long id, @RequestParam String option, @PathVariable String env) {
//        return BaseModel.getInstance(canalInstanceConfigService.instanceOperation(id, option));
//    }
//
//    /**
//     * 获取远程实例运行日志
//     *
//     * @param id 实例配置id
//     * @param nodeId 节点id
//     * @param env 环境变量
//     * @return 实例日志信息
//     */
//    @GetMapping(value = "/instance/log/{id}/{nodeId}")
//    public BaseModel<Map<String, String>> instanceLog(@PathVariable Long id, @PathVariable Long nodeId,
//                                                      @PathVariable String env) {
//        return BaseModel.getInstance(canalInstanceConfigService.remoteInstanceLog(id, nodeId));
//    }
//
//    /**
//     * 通过Server id获取所有活动的Instance
//     *
//     * @param serverId 节点id
//     * @param env 环境变量
//     * @return 实例列表
//     */
//    @GetMapping(value = "/active/instances/{serverId}")
//    public BaseModel<List<CanalInstanceConfig>> activeInstances(@PathVariable Long serverId, @PathVariable String env) {
//        return BaseModel.getInstance(canalInstanceConfigService.findActiveInstanceByServerId(serverId));
//    }
//
    @GetMapping(value = "/adapter/template")
    public BaseModel<String> template(@PathVariable String env, String category) {
        return BaseModel.getInstance(TemplateConfigLoader.loadAdapterConfig(category));
    }
}
