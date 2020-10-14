package com.alibaba.otter.canal.adapter.launcher.rest.admin;

import com.alibaba.otter.canal.client.adapter.support.Result;
import com.alibaba.otter.canal.common.utils.FileUtils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author XuDaojie
 * @since 2020/10/8
 */
@RestController
public class AdminController {

    // todo
    @GetMapping("/canal/log")
    public Result canalLog(int lines) {
        String log = FileUtils.readFileFromOffset("../logs/adapter/adapter.log", lines, "UTF-8");
        Result result = Result.createSuccess("success");
        result.setData(log);
        return result;
    }
}
