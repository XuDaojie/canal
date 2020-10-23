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

import java.util.List;

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
    private CanalAdapterService canalAdapterService;

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
     * 保存实例配置
     *
     * @param canalAdapterConfigs 配置对象
     * @param env 环境变量
     * @return 是否成功
     */
    @PostMapping(value = "/adapters/batch")
    public BaseModel<String> save(@RequestBody List<CanalAdapterConfig> canalAdapterConfigs, @PathVariable String env) {
        canalAdapterService.batchSave(canalAdapterConfigs);
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

    /**
     * 返回的模板为静态配置内容
     */
    @GetMapping(value = "/adapter/template")
    public BaseModel<String> template(@PathVariable String env, String category) {
        return BaseModel.getInstance(TemplateConfigLoader.loadAdapterConfig(category));
    }

    /**
     * 返回的模板为模板引擎内容（Thymeleaf）
     */
    @GetMapping(value = "/adapter/template/engine")
    public BaseModel<String> templateEngine(@PathVariable String env, String category) {
        return BaseModel.getInstance(TemplateConfigLoader.loadAdapterEngineConfig(category));
    }

    /**
     * 预览模板解析后的内容（Thymeleaf）
     * 注意：
     * 前端应以post方式调用，并增加参数 ?_method=get
     * 按规范应为get请求，但考虑到模板内容较长故采用post
     */
    @GetMapping(value = "/adapter/template/engine/preview")
    public BaseModel<String> templateEnginePreview(@RequestBody CanalAdapterConfig canalAdapterConfig,
                                                   @PathVariable String env) {
        return BaseModel.getInstance(canalAdapterService.previewTemplate(canalAdapterConfig));
    }
}
