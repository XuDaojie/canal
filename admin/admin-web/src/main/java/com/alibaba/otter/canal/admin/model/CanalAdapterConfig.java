package com.alibaba.otter.canal.admin.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import io.ebean.Finder;
import io.ebean.annotation.WhenModified;

/**
 * Canal-Adapter配置信息实体类
 *
 * @author XuDaojie 2020-09-21
 * @since 1.1.5
 */
@Entity
public class CanalAdapterConfig extends Model {

    public static final CanalAdapterConfigFinder find = new CanalAdapterConfigFinder();

    public static class CanalAdapterConfigFinder extends Finder<Long, CanalAdapterConfig> {

        /**
         * Construct using the default EbeanServer.
         */
        public CanalAdapterConfigFinder(){
            super(CanalAdapterConfig.class);
        }

    }

    @Id
    private Long         id;
    private Long         clusterId;
    private Long         clientId;
    private String       category;
    private String       name;
    private String       status;              // 1: 正常 0: 停止
    private String       content;
    @WhenModified
    private Date         modifiedTime;

    @Transient
    private String       clusterClientId;
    @Transient
    private String       runningStatus = "0"; // 1: 运行中 0: 停止 -1:断开

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cluster_id", updatable = false, insertable = false)
    private CanalCluster canalCluster;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", updatable = false, insertable = false)
    private NodeClient   nodeClient;

    public void init() {
        status = "1";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getClusterClientId() {
        return clusterClientId;
    }

    public void setClusterClientId(String clusterClientId) {
        this.clusterClientId = clusterClientId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        this.runningStatus = runningStatus;
    }

    public CanalCluster getCanalCluster() {
        return canalCluster;
    }

    public void setCanalCluster(CanalCluster canalCluster) {
        this.canalCluster = canalCluster;
    }

    public NodeClient getNodeClient() {
        return nodeClient;
    }

    public void setNodeClient(NodeClient nodeClient) {
        this.nodeClient = nodeClient;
    }
}
