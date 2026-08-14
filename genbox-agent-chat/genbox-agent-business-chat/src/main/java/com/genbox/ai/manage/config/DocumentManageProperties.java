package com.genbox.ai.manage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置属性。
 */
@Data
@ConfigurationProperties(prefix = "app.manage")
public class DocumentManageProperties {

    private Minio minio = new Minio();

    private Kafka kafka = new Kafka();

    private Chunk chunk = new Chunk();

    private StructureParsing structureParsing = new StructureParsing();

    private PgVector pgVector = new PgVector();

    private Elasticsearch elasticsearch = new Elasticsearch();

    private Neo4j neo4j = new Neo4j();

    @Data
    public static class Minio {
        private String endpoint = "http://127.0.0.1:9000";
        private String accessKey = "minioadmin";
        private String secretKey = "minioadmin";
        private String bucketName = "genbox-agent-document";
        private String objectPrefix = "genbox-agent/rag/document";
        private String parsedTextPrefix = "genbox-agent/rag/parsed-text";
    }

    @Data
    public static class Kafka {
        private String parseTopic = "genbox-agent-document-parse-route";
        private String indexTopic = "genbox-agent-document-index-build";
        private String groupId = "genbox-agent-document-manage";
        private Boolean autoCreateTopics = Boolean.TRUE;
    }

    @Data
    public static class Chunk {
        private Integer recursiveMaxChars = 800;
        private Integer recursiveOverlapChars = 120;
        private Integer semanticMaxChars = 700;
        private Integer semanticMinChars = 240;
        private Double semanticSimilarityThreshold = 0.18D;
        private Boolean llmEnabled = Boolean.FALSE;
        private Integer llmMaxChars = 3500;
        private Boolean recommendLlmWhenLowQuality = Boolean.TRUE;
    }

    @Data
    public static class StructureParsing {

        private Boolean llmDisambiguationEnabled = Boolean.TRUE;

        private Integer maxAmbiguousSignalsPerCall = 8;

        private Integer contextWindowLines = 2;

        private Integer maxPlainHeadingChars = 32;

        private Double ambiguityConfidenceFloor = 0.45D;

        private Double ambiguityConfidenceCeil = 0.80D;
    }

    @Data
    public static class PgVector {

        private Boolean enabled = Boolean.TRUE;

        private String host = "127.0.0.1";

        private Integer port = 5432;

        private String database = "genbox_agent_pgvector";

        private String schema = "public";

        private String username = "postgres";

        private String password = "postgres";

        private String poolName = "genbox-agent-manage-pgvector-hikari";

        private Integer maximumPoolSize = 5;

        private Integer minimumIdle = 1;
    }

    @Data
    public static class Elasticsearch {

        private Boolean enabled = Boolean.TRUE;

        private List<String> uris = new ArrayList<>(List.of("http://127.0.0.1:9200"));

        private String username = "elastic";

        private String password = "elastic";

        private String indexName = "genbox-agent-document-keyword";

        private String analyzer = "ik_max_word";

        private String searchAnalyzer = "ik_smart";

        private String navigationIndexName = "genbox-agent-document-navigation";

        private String routeIndexName = "genbox-agent-knowledge-route";

        private Integer connectTimeoutMillis = 3000;

        private Integer socketTimeoutMillis = 5000;
    }

    @Data
    public static class Neo4j {

        private Boolean enabled = Boolean.FALSE;

        private String uri = "bolt://127.0.0.1:7687";

        private String username = "neo4j";

        private String password = "12345678";

        private String database = "neo4j";

        private Integer queryTimeoutSeconds = 5;
    }
}
