package com.genbox.ai;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationConfigurationTest {

    private static final Path APPLICATION_YAML = Path.of("src/main/resources/application.yaml");

    @Test
    void externalConnectionsAndCredentialsUseRequiredEnvironmentVariables() throws IOException {
        String yaml = Files.readString(APPLICATION_YAML);
        List<String> variables = List.of(
            "GENBOX_REDIS_HOST", "GENBOX_REDIS_PORT",
            "GENBOX_MYSQL_URL", "GENBOX_MYSQL_USERNAME", "GENBOX_MYSQL_PASSWORD",
            "GENBOX_KAFKA_BOOTSTRAP_SERVERS",
            "GENBOX_OPENAI_BASE_URL", "GENBOX_OPENAI_API_KEY",
            "GENBOX_OPENAI_EMBEDDING_BASE_URL", "GENBOX_OPENAI_EMBEDDING_API_KEY",
            "GENBOX_RERANK_URL", "GENBOX_RERANK_API_KEY",
            "GENBOX_TAVILY_BASE_URL", "GENBOX_TAVILY_API_KEY",
            "GENBOX_MINIO_ENDPOINT", "GENBOX_MINIO_ACCESS_KEY", "GENBOX_MINIO_SECRET_KEY",
            "GENBOX_PGVECTOR_HOST", "GENBOX_PGVECTOR_PORT", "GENBOX_PGVECTOR_DATABASE",
            "GENBOX_PGVECTOR_SCHEMA", "GENBOX_PGVECTOR_USERNAME", "GENBOX_PGVECTOR_PASSWORD",
            "GENBOX_ELASTICSEARCH_URIS", "GENBOX_ELASTICSEARCH_USERNAME", "GENBOX_ELASTICSEARCH_PASSWORD",
            "GENBOX_NEO4J_URI", "GENBOX_NEO4J_USERNAME", "GENBOX_NEO4J_PASSWORD", "GENBOX_NEO4J_DATABASE",
            "GENBOX_AGENT_ADMIN_USERNAME", "GENBOX_AGENT_ADMIN_PASSWORD", "GENBOX_AGENT_ADMIN_TOKEN_SECRET"
        );

        variables.forEach(variable -> {
            assertThat(yaml).contains("${" + variable + "}");
            assertThat(yaml).doesNotContain("${" + variable + ":");
        });
    }

    @Test
    void environmentTemplateListsEveryRequiredVariableWithoutValues() throws IOException {
        List<String> lines = Files.readAllLines(Path.of(".env.example"));
        List<String> variables = List.of(
            "GENBOX_REDIS_HOST", "GENBOX_REDIS_PORT",
            "GENBOX_MYSQL_URL", "GENBOX_MYSQL_USERNAME", "GENBOX_MYSQL_PASSWORD",
            "GENBOX_KAFKA_BOOTSTRAP_SERVERS",
            "GENBOX_OPENAI_BASE_URL", "GENBOX_OPENAI_API_KEY",
            "GENBOX_OPENAI_EMBEDDING_BASE_URL", "GENBOX_OPENAI_EMBEDDING_API_KEY",
            "GENBOX_RERANK_URL", "GENBOX_RERANK_API_KEY",
            "GENBOX_TAVILY_BASE_URL", "GENBOX_TAVILY_API_KEY",
            "GENBOX_MINIO_ENDPOINT", "GENBOX_MINIO_ACCESS_KEY", "GENBOX_MINIO_SECRET_KEY",
            "GENBOX_PGVECTOR_HOST", "GENBOX_PGVECTOR_PORT", "GENBOX_PGVECTOR_DATABASE",
            "GENBOX_PGVECTOR_SCHEMA", "GENBOX_PGVECTOR_USERNAME", "GENBOX_PGVECTOR_PASSWORD",
            "GENBOX_ELASTICSEARCH_URIS", "GENBOX_ELASTICSEARCH_USERNAME", "GENBOX_ELASTICSEARCH_PASSWORD",
            "GENBOX_NEO4J_URI", "GENBOX_NEO4J_USERNAME", "GENBOX_NEO4J_PASSWORD", "GENBOX_NEO4J_DATABASE",
            "GENBOX_AGENT_ADMIN_USERNAME", "GENBOX_AGENT_ADMIN_PASSWORD", "GENBOX_AGENT_ADMIN_TOKEN_SECRET"
        );

        variables.forEach(variable -> assertThat(lines).contains(variable + "="));
    }
}
