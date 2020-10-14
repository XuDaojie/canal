package com.alibaba.otter.canal.admin.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.annotation.WhenModified;

/**
 * 节点信息控制层
 *
 * @author XuDaojie
 * @date 2020-10-08
 * @since 1.1.5
 */
@Entity
@Table(name = "canal_node_client")
public class NodeClient extends Model {

    public static final NodeClientFinder find = new NodeClientFinder();

    public static class NodeClientFinder extends Finder<Long, NodeClient> {

        /**
         * Construct using the default EbeanServer.
         */
        public NodeClientFinder(){
            super(NodeClient.class);
        }

    }

    @Id
    private Long         id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cluster_id", updatable = false, insertable = false)
    private CanalCluster canalCluster;
    @Column(name = "cluster_id")
    private Long         clusterId;
    private String       name;
    private String       ip;
    private String       port;
    private String       status;
    @WhenModified
    private Date         modifiedTime;

    public void init() {
        status = "-1";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CanalCluster getCanalCluster() {
        return canalCluster;
    }

    public void setCanalCluster(CanalCluster canalCluster) {
        this.canalCluster = canalCluster;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
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
}
