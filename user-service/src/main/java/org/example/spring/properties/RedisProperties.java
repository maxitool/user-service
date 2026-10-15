package org.example.spring.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
@Getter
public class RedisProperties {
    @Value("${REDIS_TTL_DURATION_MINUTES:10}")
    private Integer ttlDuration;
}
