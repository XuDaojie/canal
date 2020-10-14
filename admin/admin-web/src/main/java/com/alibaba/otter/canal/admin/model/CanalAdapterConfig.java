package com.alibaba.otter.canal.admin.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import io.ebean.Finder;
import io.ebean.annotation.WhenModified;

/**
 * Canal-Adapter配置信息实体类
 *
 * @author XuDaojie 2020-09-21
 * @since  1.1.5
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
    private String       category;
    private String       name;
    private String       status;             // 1: 正常 0: 停止
    private String       content;
    @WhenModified
    private Date         modifiedTime;

    @Transient
    private String       runningStatus = "0"; // 1: 运行中 0: 停止

    public void init() {
        status = "1";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
