package com.code.deepreader.common.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 消息中间件和任务调度的核心配置，供各个微服务共享。
 */
@Component
@ConfigurationProperties(prefix = "platform.messaging")
public class MessagingProperties {

    /**
     * Kafka/RabbitMQ 等消息总线的主题前缀。
     */
    private String topicPrefix = "cdr";

    /**
     * 调度器触发的默认并发度限制。
     */
    private int maxConcurrency = 4;

    public String getTopicPrefix() {
        return topicPrefix;
    }

    public void setTopicPrefix(String topicPrefix) {
        this.topicPrefix = topicPrefix;
    }

    public int getMaxConcurrency() {
        return maxConcurrency;
    }

    public void setMaxConcurrency(int maxConcurrency) {
        this.maxConcurrency = maxConcurrency;
    }
}
