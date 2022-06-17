package crm.wealth.management.service.requestsearch.service;

import crm.wealth.management.config.ElasticsearchConfig;
import crm.wealth.management.model.Request;
import crm.wealth.management.service.requestsearch.model.ElasticRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class InitIndexService {

    @Autowired
    private ElasticsearchConfig esConfig;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public void createIndex(@NotNull Request request) {

        ElasticRequest elasticRequest = ElasticRequest.builder()
                .id(request.getId())
                .name(request.getName())
                .comment(request.getComment())
                .type(request.getType().name())
                .status(request.getStatus().name())
                .priority(request.getPriority().name())
                .assignee(request.getAssignee())
                .createdBy(request.getCreatedBy())
                .checkedBy(request.getCheckedBy())
                .approvedBy(request.getApprovedBy())
                .lastModifiedBy(request.getLastModifiedBy())
                .createdDate(request.getCreatedDate())
                .checkedDate(request.getCheckedDate())
                .approvedDate(request.getApprovedDate())
                .lastModifiedDate(request.getLastModifiedDate())
                .build();

        this.indexRequest(elasticRequest);

    }
    private void indexRequest(@NotNull ElasticRequest elasticRequest) {

        String documentId = elasticRequest.getId().toString();

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(documentId)
                .withObject(elasticRequest)
                .build();

        elasticsearchOperations.index(indexQuery, IndexCoordinates.of(esConfig.getIndexName()));

    }

}
