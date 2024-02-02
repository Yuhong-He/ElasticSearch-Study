package com.example.es;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class _09_ESTest_Async {
    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(HttpHost.create("http://localhost:9200")).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchAsyncClient asyncClient = new ElasticsearchAsyncClient(transport);

        System.out.println("In main thread before");
        CompletableFuture<Boolean> booleanCompletableFuture = asyncClient.indices().create(
                req -> req.index("products3")
        ).thenApply(
                res -> {
                    System.out.println("In thenApply");
                    return res.acknowledged();
                }
        ).whenComplete(
                (res, err) -> {
                    System.out.println("In async callback");
                    if (res != null) {
                        System.out.println(res);
                    } else {
                        System.out.println(err.getMessage());
                    }
                }
        );
        System.out.println("In main thread after");

        booleanCompletableFuture.join();
        System.out.println("After async finish");

        transport.close();
    }
}
