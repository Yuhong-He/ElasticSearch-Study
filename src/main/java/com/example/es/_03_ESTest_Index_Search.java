package com.example.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class _03_ESTest_Index_Search {
    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(HttpHost.create("http://localhost:9200")).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient esClient = new ElasticsearchClient(transport);
        ElasticsearchIndicesClient indices = esClient.indices();

        /*
         * Regular method
         * */
        GetIndexRequest request = new GetIndexRequest.Builder().index("products1").build();
        GetIndexResponse response = indices.get(request);
        IndexState products1 = response.get("products1");
        System.out.println(products1);

        /*
         * Lambda method
         * */
        IndexState products1_lamb = indices.get(req -> req.index("products1")).get("products1");
        System.out.println(products1_lamb);

        transport.close();
    }
}
