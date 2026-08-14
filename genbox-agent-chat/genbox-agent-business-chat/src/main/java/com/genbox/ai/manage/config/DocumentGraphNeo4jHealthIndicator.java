package com.genbox.ai.manage.config;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * 配置类。
 */
@Component
@ConditionalOnBean(name = "documentManageNeo4jDriver")
public class DocumentGraphNeo4jHealthIndicator implements HealthIndicator {

    private final Driver driver;
    private final DocumentManageProperties properties;

    public DocumentGraphNeo4jHealthIndicator(Driver driver, DocumentManageProperties properties) {
        this.driver = driver;
        this.properties = properties;
    }

    @Override
    public Health health() {
        try (Session session = driver.session(SessionConfig.forDatabase(properties.getNeo4j().getDatabase()))) {
            session.run("RETURN 1 AS ok").consume();
            return Health.up()
                .withDetail("database", properties.getNeo4j().getDatabase())
                .build();
        }
        catch (Exception exception) {
            return Health.down(exception)
                .withDetail("database", properties.getNeo4j().getDatabase())
                .build();
        }
    }
}
