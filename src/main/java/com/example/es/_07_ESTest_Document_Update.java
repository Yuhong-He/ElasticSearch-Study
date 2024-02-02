package com.example.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class _07_ESTest_Document_Update {
    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(HttpHost.create("http://localhost:9200")).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        /*
         * Regular method
         * */
        Product product = Product.builder()
                .name("Xiaomi").build();
        UpdateRequest<Product, Object> request = new UpdateRequest.Builder<Product, Object>()
                .index("products")
                .id("1001")
                .doc(product)
                .build();
        System.out.println(esClient.update(request, Product.class));

        /*
         * Lambda method
         * */
        UpdateResponse<Product> response = esClient.update(req ->
                        req.index("products")
                                .id("1001")
                                .doc(product)
                , Product.class);
        System.out.println(response);

        transport.close();
    }
}
