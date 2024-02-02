package com.example.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
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

public class _06_ESTest_Document_Search_Pagination {
    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(HttpHost.create("http://localhost:9200")).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        int pageSize = 3; // 设置每页大小
        int pageNumber = 2; // 设置页码，从 1 开始

        // 计算要跳过的结果数量
        int from = (pageNumber - 1) * pageSize;

        MatchQuery matchQuery = new MatchQuery.Builder()
                .field("name").query("Xiaomi")
                .build();
        Query query = new Query.Builder()
                .match(matchQuery)
                .build();
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("products")
                .query(query)
                .from(from)
                .size(pageSize)
                .build();
        HitsMetadata<Product> hits = esClient.search(searchRequest, Product.class).hits();
        for (Hit<Product> hit : hits.hits()) {
            Product product = hit.source();
            System.out.println(product);
        }

        transport.close();
    }
}
