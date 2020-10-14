package com.alibaba.otter.canal.admin.controller;

import com.alibaba.otter.canal.admin.model.BaseModel;
import com.alibaba.otter.canal.admin.model.NodeClient;
import com.alibaba.otter.canal.admin.model.Pager;
import com.alibaba.otter.canal.admin.service.NodeClientService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 节点信息控制层
 *
 * @author XuDaojie
 * @date 2020-10-08
 * @since 1.1.5
 */
@RestController
@RequestMapping("/api/{env}")
public class NodeClientController {

    @Resource
    NodeClientService nodeClientService;

    /**
     * 获取所有节点信息列表
     *
     * @param nodeClient 筛选条件
     * @param env 环境变量
     * @return 节点信息列表
     */
    @GetMapping(value = "/nodeClients")
    public BaseModel<Pager<NodeClient>> nodeServers(NodeClient nodeClient, Pager<NodeClient> pager,
                                                    @PathVariable String env) {
        return BaseModel.getInstance(nodeClientService.findList(nodeClient, pager));
    }

    /**
     * 保存节点信息
     *
     * @param nodeClient 节点信息
     * @param env 环境变量
     * @return 是否成功
     */
    @PostMapping(value = "/nodeClient")
    public BaseModel<String> save(@RequestBody NodeClient nodeClient, @PathVariable String env) {
        nodeClientService.save(nodeClient);
        return BaseModel.getInstance("success");
    }

    /**
     * 获取节点信息详情
     *
     * @param id 节点信息id
     * @param env 环境变量
     * @return 检点信息
     */
    @GetMapping(value = "/nodeClient/{id}")
    public BaseModel<NodeClient> detail(@PathVariable Long id, @PathVariable String env) {
        return BaseModel.getInstance(nodeClientService.detail(id));
    }

    /**
     * 修改节点信息
     *
     * @param nodeClient 节点信息
     * @param env 环境变量
     * @return 是否成功
     */
    @PutMapping(value = "/nodeClient")
    public BaseModel<String> update(@RequestBody NodeClient nodeClient, @PathVariable String env) {
        nodeClientService.update(nodeClient);
        return BaseModel.getInstance("success");
    }

    /**
     * 删除节点信息
     *
     * @param id 节点信息id
     * @param env 环境变量
     * @return 是否成功
     */
    @DeleteMapping(value = "/nodeClient/{id}")
    public BaseModel<String> delete(@PathVariable Long id, @PathVariable String env) {
        nodeClientService.delete(id);
        return BaseModel.getInstance("success");
    }

    /**
     * 获取远程节点运行状态
     *
     * @param ip 节点ip
     * @param port 节点端口
     * @param env 环境变量
     * @return 状态信息
     */
    @GetMapping(value = "/nodeClient/status")
    public BaseModel<Integer> status(@RequestParam String ip, @RequestParam Integer port, @PathVariable String env) {
        return BaseModel.getInstance(nodeClientService.remoteNodeStatus(ip, port));
    }

    /**
     * 启动远程节点
     *
     * @param id 节点id
     * @param env 环境变量
     * @return 是否成功
     */
    @PutMapping(value = "/nodeClient/start/{id}")
    public BaseModel<Boolean> start(@PathVariable Long id, @PathVariable String env) {
        return BaseModel.getInstance(nodeClientService.remoteOperation(id, "start"));
    }

    /**
     * 获取远程节点日志
     *
     * @param id 节点id
     * @param env 环境变量
     * @return 节点日志
     */
    @GetMapping(value = "/nodeClient/log/{id}")
    public BaseModel<String> log(@PathVariable Long id, @PathVariable String env) {
        return BaseModel.getInstance(nodeClientService.remoteCanalLog(id));
    }

    /**
     * 关闭远程节点
     *
     * @param id 节点id
     * @param env 环境变量
     * @return 是否成功
     */
    @PutMapping(value = "/nodeClient/stop/{id}")
    public BaseModel<Boolean> stop(@PathVariable Long id, @PathVariable String env) {
        return BaseModel.getInstance(nodeClientService.remoteOperation(id, "stop"));
    }
}
