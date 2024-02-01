package com.example.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class _04_ESTest_Index_Delete {
    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(HttpHost.create("http://localhost:9200")).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient esClient = new ElasticsearchClient(transport);
        ElasticsearchIndicesClient indices = esClient.indices();

        /*
         * Regular method
         * */
        if (!indices.exists(req -> req.index("products1")).value()) {
            System.out.println("products1 not exists!");
        } else {
            DeleteIndexRequest request = new DeleteIndexRequest.Builder().index("products1").build();
            DeleteIndexResponse response = indices.delete(request);
            System.out.println(response);
        }

        /*
         * Lambda method
         * */
        if (!indices.exists(req -> req.index("products1")).value()) {
            System.out.println("products1 not exists!");
        } else {
            DeleteIndexResponse response_lamb = indices.delete(req -> req.index("products"));
            System.out.println(response_lamb);
        }

        transport.close();
    }
}
