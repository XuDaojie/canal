package com.alibaba.otter.canal.admin.model;

/**
 * CanalClient实例配置信息实体类
 *
 * @author XuDaojie
 * @date 2020-10-13
 * @since 1.1.5
 */
public class CanalClientInstanceConfig {

    private NodeClient nodeClient;

    private String       name;
    private String       status;             // 1: 正常 0: 停止
    private String       runningStatus = "0"; // 1: 运行中 0: 停止

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        this.runningStatus = runningStatus;
    }

    public NodeClient getNodeClient() {
        return nodeClient;
    }

    public void setNodeClient(NodeClient nodeClient) {
        this.nodeClient = nodeClient;
    }
}
