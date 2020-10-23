package com.alibaba.otter.canal.admin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import io.ebean.Finder;

/**
 * @author XuDaojie
 * @date 2020/10/21
 * @since v1.1.5
 */
@Entity
@Table(name = "TABLES", schema = "information_schema")
public class Tables extends Model {

    public static final Tables.TablesFinder find = new Tables.TablesFinder();

    public static class TablesFinder extends Finder<Long, Tables> {

        /**
         * Construct using the default EbeanServer.
         */
        public TablesFinder(){
            super(Tables.class);
        }

    }

    @Column(name = "TABLE_SCHEMA")
    private String tableSchema;
    @Column(name = "TABLE_NAME")
    private String tableName;

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
