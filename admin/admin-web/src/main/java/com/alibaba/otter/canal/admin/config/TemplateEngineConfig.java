package com.alibaba.otter.canal.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

/**
 * @author XuDaojie
 * @date 2020/10/21
 * @since v1.1.5
 */
@Configuration
public class TemplateEngineConfig {

    /**
     * 由于默认TemplateEngine模板只能解析本地文件，
     * 使用此实例解析通过参数传入的模板文件
     */
    @Bean(name = "dynamicTemplateEngine")
    public TemplateEngine dynamicTemplateEngine() {
        TemplateEngine templateEngine = new SpringTemplateEngine();
        StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
        stringTemplateResolver.setCacheable(false);
        stringTemplateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(stringTemplateResolver);

        return templateEngine;
    }
}
