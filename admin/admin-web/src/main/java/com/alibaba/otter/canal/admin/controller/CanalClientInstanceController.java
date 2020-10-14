package com.alibaba.otter.canal.admin.controller;

import com.alibaba.otter.canal.admin.model.BaseModel;
import com.alibaba.otter.canal.admin.model.CanalClientInstanceConfig;
import com.alibaba.otter.canal.admin.service.CanalClientInstanceService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

/**
 * Canal Client Instance配置管理控制层
 *
 * @author XuDaojie
 * @date 2020-10-13
 * @since 1.1.5
 */
@RestController
@RequestMapping("/api/{env}/canalClient")
public class CanalClientInstanceController {

    @Resource
    CanalClientInstanceService canalClientInstanceService;

//    /**
//     * 实例配置列表
//     *
//     * @param canalInstanceConfig 查询对象
//     * @param env 环境变量
//     * @return 实例列表
//     */
//    @GetMapping(value = "/instances")
//    public BaseModel<Pager<CanalInstanceConfig>> list(CanalInstanceConfig canalInstanceConfig,
//                                                      Pager<CanalInstanceConfig> pager, @PathVariable String env) {
//        return BaseModel.getInstance(canalInstanceConfigService.findList(canalInstanceConfig, pager));
//    }

    /**
     * 启动远程实例
     *
     * @param instance instance 名称
     * @param nodeId client id
     * @param env 环境变量
     * @return 是否成功
     */
    @PutMapping(value = "/instance/{instance}/start")
    public BaseModel<Boolean> start(@PathVariable String instance, @PathVariable String env, Long nodeId) {
        return BaseModel.getInstance(canalClientInstanceService.remoteOperation(instance, nodeId, "start"));
    }

    /**
     * 关闭远程实例
     *
     * @param instance 实例配置id
     * @param nodeId 节点id
     * @param env 环境变量
     * @return 是否成功
     */
    @PutMapping(value = "/instance/{instance}/stop")
    public BaseModel<Boolean> stop(@PathVariable String instance, @PathVariable String env, Long nodeId) {
        return BaseModel.getInstance(canalClientInstanceService.remoteOperation(instance, nodeId, "stop"));
    }

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

    /**
     * 通过Client id所有活动的Instance
     *
     * @param clientId 节点id
     * @param env 环境变量
     * @return 实例列表
     */
    @GetMapping(value = "/instances/{clientId}")
    public BaseModel<List<CanalClientInstanceConfig>> activeInstances(@PathVariable Long clientId, @PathVariable String env) {
        return BaseModel.getInstance(canalClientInstanceService.findInstanceByClientId(clientId));
    }

//    @GetMapping(value = "/instance/template")
//    public BaseModel<String> template(@PathVariable String env) {
//        return BaseModel.getInstance(TemplateConfigLoader.loadInstanceConfig());
//    }
}
