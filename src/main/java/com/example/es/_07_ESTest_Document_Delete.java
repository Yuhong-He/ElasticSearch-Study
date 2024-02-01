package com.example.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class _07_ESTest_Document_Delete {
    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(HttpHost.create("http://localhost:9200")).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        /*
         * Regular method
         * */
        if (esClient.exists(req -> req.index("products").id("1001")).value()) {
            DeleteRequest deleteRequest = new DeleteRequest.Builder()
                    .index("products").id("1001").build();
            DeleteResponse response = esClient.delete(deleteRequest);
            System.out.println(response);
        } else {
            System.out.println("Product not exists");
        }

        /*
         * Lambda method
         * */
        if (esClient.exists(req -> req.index("products").id("1001")).value()) {
            DeleteResponse response = esClient.delete(req -> req.index("products").id("1001"));
            System.out.println(response);
        } else {
            System.out.println("Product not exists");
        }

        transport.close();
    }
}
