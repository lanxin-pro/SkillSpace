package cn.iocoder.educate.framework.web.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 16:23
 */
@Data
@Validated
@ConfigurationProperties(prefix = "lanxin.web")
public class WebProperties {

    @NotNull(message = "pre前缀不能为空")
    private String prePath = "server";

    @NotNull(message = "APP API 不能为空")
    private Api appApi = new Api("/app-api", "**.controller.app.**");

    @NotNull(message = "Admin API 不能为空")
    private Api adminApi = new Api("/admin-api", "**.controller.admin.**");

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Valid
    public static class Api {

        /**
         * API 前缀，实现所有 Controller 提供的 RESTFul API 的统一前缀
         *
         * 意义：通过该前缀，避免 Swagger、Actuator 意外通过 Nginx 暴露出来给外部，带来安全性问题
         *      这样，Nginx 只需要配置转发到 /api/* 的所有接口即可。
         *
         * @see EducateWebAutoConfiguration#configurePathMatch(PathMatchConfigurer)
         */
        @NotEmpty(message = "API 前缀不能为空")
        private String prefix;

        /**
         * Controller 所在包的 Ant 路径规则
         *
         * 主要目的是，给该 Controller 设置指定的 {@link #prefix}
         */
        @NotEmpty(message = "Controller 所在包不能为空")
        private String controller;

    }
}
