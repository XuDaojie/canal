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
@Table(name = "SCHEMATA", schema = "information_schema")
public class Schemata extends Model {

    public static final Schemata.SchemataFinder find = new Schemata.SchemataFinder();

    public static class SchemataFinder extends Finder<Long, Schemata> {

        /**
         * Construct using the default EbeanServer.
         */
        public SchemataFinder(){
            super(Schemata.class);
        }

    }

    @Column(name = "SCHEMA_NAME")
    private String schemaName;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
}
