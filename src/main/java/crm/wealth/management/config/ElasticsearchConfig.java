package crm.wealth.management.config;
import lombok.NonNull;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
@Configuration
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${app.elasticsearch.uri}")
    private String hostAndPort;

    @Value("${app.elasticsearch.username}")
    private String username;

    @Value("${app.elasticsearch.password}")
    private String password;

    @Value("${app.elasticsearch.user.index}")
    private String indexName;

    public String getIndexName() {
        return indexName;
    }

    @Override
    public @NonNull RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(hostAndPort)
                .withBasicAuth(username, password)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchOperations() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
}