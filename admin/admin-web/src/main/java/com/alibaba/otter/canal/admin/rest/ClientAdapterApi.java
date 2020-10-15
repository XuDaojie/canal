package com.alibaba.otter.canal.admin.rest;

import com.google.common.base.Function;
import com.google.common.collect.MigrateMap;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * @author XuDaojie
 * @date 2020/10/13
 * @since 1.1.5
 */
public interface ClientAdapterApi {

    /**
     * key: url
     */
    Map<String, ClientAdapterApi> apis = MigrateMap.makeComputingMap(new Function<String, ClientAdapterApi>() {
        @Nullable
        @Override
        public ClientAdapterApi apply(@Nullable String url) {
            return Feign.builder()
                    .encoder(new FormEncoder(new JacksonEncoder()))
                    .decoder(new JacksonDecoder())
                    .target(ClientAdapterApi.class, url);
        }
    });

    /**
     * 查询所有订阅同步的canal destination或MQ topic
     */
    @RequestLine("GET /destinations")
    List<Map<String, String>> destinations();

    /**
     * 数据同步开关
     * @param instance 实例名称
     * @param status 开关状态 on / off
     */
    @RequestLine("PUT /syncSwitch/{instance}/{status}")
    Map<String, String> syncSwitch(@Param("instance") String instance, @Param("status") String status);

    /**
     * 数据同步开关状态
     * @param instance 实例名称
     */
    @RequestLine("GET /syncSwitch/{instance}")
    Map<String, String> syncSwitch(@Param("instance") String instance);

    /**
     * ETL curl http://127.0.0.1:8081/etl/rdb/oracle1/mytest_user.yml -X POST
     *
     * @param type 类型 hbase, es
     * @param key adapter key
     * @param task 任务名对应配置文件名 mytest_user.yml
     * @param params etl where条件参数, 为空全部导入
     */
    @RequestLine("POST /etl/{type}/{key}/{task}")
    Map<String, Object> etl(@Param("type") String type, @Param("key") String key, @Param("task") String task,
                         String params);

    /**
     * ETL curl http://127.0.0.1:8081/etl/hbase/mytest_person2.yml -X POST
     *
     * @param type 类型 hbase, es
     * @param task 任务名对应配置文件名 mytest_person2.yml
     * @param params etl where条件参数, 为空全部导入
     */
    @RequestLine("POST /etl/{type}/{task}")
    Map<String, Object> etl(@Param("type") String type, @Param("task") String task,
                         String params);

    /**
     * 统计总数 curl http://127.0.0.1:8081/count/rdb/oracle1/mytest_user.yml
     *
     * @param type 类型 hbase, es
     * @param key adapter key
     * @param task 任务名对应配置文件名 mytest_person2.yml
     * @return
     */
    @RequestLine("GET /count/{type}/{key}/{task}")
    Map<String, Object> count(@Param("type") String type, @Param("key") String key, @Param("task") String task);

    /**
     * 统计总数 curl http://127.0.0.1:8081/count/hbase/mytest_person2.yml
     *
     * @param type 类型 hbase, es
     * @param task 任务名对应配置文件名 mytest_person2.yml
     * @return
     */
    @RequestLine("GET /count/{type}/{task}")
    Map<String, Object> count(@Param("type") String type, @Param("task") String task);

    // -----

    /**
     * 获取日志
     * @param lines
     */
    @RequestLine("GET /canalClient/log?lines={lines}")
    Map<String, Object> canalLog(@Param("lines") int lines);
}
