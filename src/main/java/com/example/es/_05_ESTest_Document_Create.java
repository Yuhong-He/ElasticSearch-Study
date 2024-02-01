package com.example.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.CreateRequest;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.CreateOperation;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class _05_ESTest_Document_Create {
    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(HttpHost.create("http://localhost:9200")).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        /*
        * Regular method
        * */
        if (!esClient.exists(req -> req.index("products").id("1001")).value()) {
            CreateRequest<Product> createRequest = new CreateRequest.Builder<Product>()
                    .index("products")
                    .id("1001")
                    .document(new Product(1, "iPhone", 9999))
                    .build();
            CreateResponse response = esClient.create(createRequest);
            System.out.println(response);
        } else {
            System.out.println("Product 1001 exists");
        }

        // bulk
        List<BulkOperation> opts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            if (!esClient.exists(req -> req.index("products").id("10000" + finalI)).value()) {
                CreateOperation<Product> optObj = new CreateOperation.Builder<Product>()
                        .index("products")
                        .id("10000" + i)
                        .document(new Product(10 + i, "iPhone" + i, 5999 + i))
                        .build();
                BulkOperation opt = new BulkOperation.Builder().create(optObj).build();
                opts.add(opt);
            } else {
                System.out.println("ID " + 10000 + i + " is exists");
            }
        }
        if (!opts.isEmpty()) {
            BulkRequest bulkRequest = new BulkRequest.Builder()
                    .operations(opts)
                    .build();
            BulkResponse bulkResponse = esClient.bulk(bulkRequest);
            System.out.println(bulkResponse);
        } else {
            System.out.println("No data insert");
        }

        /*
         * Lambda method
         * */
        if (!esClient.exists(req -> req.index("products").id("1002")).value()) {
            CreateResponse response_lamb = esClient.create(req -> req.index("products").id("1002")
                    .document(new Product(2, "Xiaomi", 3999)));
            System.out.println(response_lamb);
        } else {
            System.out.println("Product 1002 exists");
        }

        // bulk
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            products.add(new Product(2000 + i, "Xiaomi", 3999 + i));
        }
        BulkResponse bulkResponse = esClient.bulk(
                req -> {
                    products.forEach(
                            product -> {
                                req.operations(
                                        builder -> builder.create(
                                                d -> d.index("products").id(product.getId().toString()).document(product)
                                        )
                                );
                            }
                    );
                    return req;
                }
        );
        System.out.println(bulkResponse);

        transport.close();
    }
}
