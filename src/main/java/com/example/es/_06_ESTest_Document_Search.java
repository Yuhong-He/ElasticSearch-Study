package com.example.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class _06_ESTest_Document_Search {
    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(HttpHost.create("http://localhost:9200")).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        /*
         * Regular method
         * */
        MatchQuery matchQuery = new MatchQuery.Builder()
                .field("name").query("Xiaomi")
                .build();
        Query query = new Query.Builder()
                .match(matchQuery)
                .build();
        SearchRequest searchRequest = new SearchRequest.Builder()
                .query(query)
                .build();
        System.out.println(esClient.search(searchRequest, Product.class));

        /*
         * Lambda method
         * */
        HitsMetadata<Product> hits = esClient.search(req -> {
            req.query(q -> q.match(
                    m -> m.field("name").query("Xiaomi")
            ));
            return req;
        }, Product.class).hits();
        System.out.println(hits);

        transport.close();
    }
}
