package com.example.cart.common.metrics;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseQueryMetricsConfig {
    public DatabaseQueryMetricsConfig(EntityManagerFactory entityManagerFactory, MeterRegistry meterRegistry) {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics hibernateStatistics = sessionFactory.getStatistics();

        FunctionCounter.builder("db_query_total", hibernateStatistics, stats -> (double) stats.getQueryExecutionCount())
            .description("누적 DB 쿼리 실행 수(Hibernate 기준)")
            .register(meterRegistry);
    }
}
