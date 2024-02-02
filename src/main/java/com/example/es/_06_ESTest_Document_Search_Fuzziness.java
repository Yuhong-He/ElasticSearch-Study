package com.example.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class _06_ESTest_Document_Search_Fuzziness {
    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(HttpHost.create("http://localhost:9200")).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        MatchQuery matchQuery = new MatchQuery.Builder()
                .field("name").fuzziness("AUTO").query("Xiami") // 在 name 字段上执行模糊查询
                .build();
        Query query = new Query.Builder()
                .match(matchQuery)
                .build();
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("products")
                .query(query)
                .build();
        HitsMetadata<Product> hits = esClient.search(searchRequest, Product.class).hits();
        for (Hit<Product> hit : hits.hits()) {
            Product product = hit.source();
            System.out.println(product);
        }

        transport.close();
    }
}
