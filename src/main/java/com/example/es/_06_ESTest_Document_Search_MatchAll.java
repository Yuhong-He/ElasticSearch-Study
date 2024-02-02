package com.example.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class _06_ESTest_Document_Search_MatchAll {
    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(HttpHost.create("http://localhost:9200")).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        MatchAllQuery matchAllQuery = new MatchAllQuery.Builder().build(); // Match All不需要指定查询文档，会在所有文档中查询
        Query query = new Query.Builder()
                .matchAll(matchAllQuery)
                .build();
        SearchRequest searchRequest = new SearchRequest.Builder()
                .query(query)
                .size(100)
                .build();
        HitsMetadata<Product> hits = esClient.search(searchRequest, Product.class).hits();
        for (Hit<Product> hit : hits.hits()) {
            Product product = hit.source();
            System.out.println(product);
        }
    }
}
