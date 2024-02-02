package com.example.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
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
import java.util.ArrayList;
import java.util.List;

public class _06_ESTest_Document_Search_Sort {
    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(HttpHost.create("http://localhost:9200")).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        // 构建排序选项
        SortOptions sort1 = new SortOptions.Builder().field(f -> f.field("price").order(SortOrder.Asc)).build();
        SortOptions sort2 = new SortOptions.Builder().field(f -> f.field("name.keyword").order(SortOrder.Asc)).build();
        List<SortOptions> list = new ArrayList<>();
        list.add(sort1);
        list.add(sort2);

        MatchQuery matchQuery = new MatchQuery.Builder()
                .field("name").query("Xiaomi")
                .build();
        Query query = new Query.Builder()
                .match(matchQuery)
                .build();
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("products")
                .sort(list)
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
