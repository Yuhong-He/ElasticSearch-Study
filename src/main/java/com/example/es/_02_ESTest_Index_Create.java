package com.example.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class _02_ESTest_Index_Create {
    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(HttpHost.create("http://localhost:9200")).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient esClient = new ElasticsearchClient(transport);
        ElasticsearchIndicesClient indices = esClient.indices();

        /*
        * Regular method
        * */
        ExistsRequest existsRequest = new ExistsRequest.Builder().index("products1").build();
        if (indices.exists(existsRequest).value()) {
            System.out.println("products1 is exists!");
        } else {
            CreateIndexRequest request = new CreateIndexRequest.Builder().index("products1").build();
            CreateIndexResponse response = indices.create(request);
            System.out.println(response);
            System.out.println("products1 created");
        }

        /*
         * Lambda method
         * */
        if (indices.exists(req -> req.index("products1")).value()) {
            System.out.println("products1 is exists!");
        } else {
            CreateIndexResponse response = esClient.indices().create(req -> req.index("products2"));
            System.out.println(response);
            System.out.println("products1 created");
        }

        transport.close();
    }
}
