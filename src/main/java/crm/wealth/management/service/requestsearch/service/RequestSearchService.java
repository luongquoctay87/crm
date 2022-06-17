package crm.wealth.management.service.requestsearch.service;

import crm.wealth.management.config.ElasticsearchConfig;
import crm.wealth.management.service.requestsearch.model.ElasticRequest;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j

public class RequestSearchService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ElasticsearchConfig esConfig;

    public Map<String, Object> search(Optional<String> keyword, String[] status, Optional<String> priority, Integer pageNo, Integer pageSize) {

        List<ElasticRequest> data = new ArrayList<>();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

//        Case request have param: keyword
        if (keyword.isPresent()) {
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("name", "*" + keyword + "*"));
        }

//       Case request have param: status
        if (status != null && status.length > 0) {

            for (int i = 0; i < status.length; i++) {

                String requestStatus = status[i];

                boolQueryBuilder.must(QueryBuilders.termQuery("status", requestStatus.toUpperCase()));
            }
        }

//       Case request have param: priority
        if (priority.isPresent()) {

            String requestPriority = priority.get().toUpperCase();
            boolQueryBuilder.must(QueryBuilders.termQuery("priority", requestPriority.toUpperCase()));
        }

        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withFilter(boolQueryBuilder)
                .withSearchType(SearchType.DEFAULT)
                .withPageable(PageRequest.of(pageNo, pageSize))
                .withSorts(SortBuilders.fieldSort("createdDate").order(SortOrder.DESC))
                .build();

        SearchHits<ElasticRequest> searchHits = elasticsearchOperations.search(searchQuery, ElasticRequest.class, IndexCoordinates.of(esConfig.getIndexName()));

        if (searchHits.isEmpty()) {
            return Collections.EMPTY_MAP;
        }

        try {
            data = searchHits.getSearchHits().stream()
                    .map(SearchHit::getContent)
                    .collect(Collectors.toList());

            if(data == null) {
                log.error("Data from elasticsearch query is null");
                throw new Exception("Data from elasticsearch query is null");
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("data", data);
        map.put("totalElements", searchHits.getTotalHits());

        return map;

    }

}
