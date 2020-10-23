package com.alibaba.otter.canal.admin.controller;

import com.alibaba.otter.canal.admin.model.BaseModel;
import com.alibaba.otter.canal.admin.model.Schemata;
import com.alibaba.otter.canal.admin.model.Tables;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author XuDaojie
 * @date 2020-10-19
 * @since v1.1.5
 */
@RestController
@RequestMapping("/api/{env}")
public class SrcDataSourceController {

    /**
     * 返回库列表
     */
    @GetMapping("/canal/srcDataSource/schemas")
    public BaseModel<List<Schemata>> schematas() {
        return BaseModel.getInstance(
                Schemata.find.query().findList());
    }

    /**
     * 返回表列表
     */
    @GetMapping("/canal/srcDataSource/schemas/{schema}/tables")
    public BaseModel<List<Tables>> tables(@PathVariable String schema) {
        return BaseModel.getInstance(
                Tables.find.query()
                .where()
                .eq("TABLE_SCHEMA", schema)
                .eq("TABLE_TYPE", "BASE TABLE")
                .findList());
    }

}
