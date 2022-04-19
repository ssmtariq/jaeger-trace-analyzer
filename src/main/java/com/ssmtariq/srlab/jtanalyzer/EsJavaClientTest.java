//package com.ssmtariq.srlab.jtanalyzer;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.elasticsearch.core.SearchResponse;
//import co.elastic.clients.elasticsearch.core.search.Hit;
//import co.elastic.clients.json.jackson.JacksonJsonpMapper;
//import co.elastic.clients.transport.ElasticsearchTransport;
//import co.elastic.clients.transport.rest_client.RestClientTransport;
//import com.ssmtariq.srlab.jtanalyzer.model.JSpan;
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
////@SpringBootTest
//public class EsJavaClientTest {
//
////    @Ignore // we don't have a running ES
////    @Test
//    public void testCreateClient() throws Exception {
//        //tag::create-client
//        // Create the low-level client
//        RestClient restClient = RestClient.builder(
//            new HttpHost("192.168.5.86", 9200)).build();
//
//        // Create the transport with a Jackson mapper
//        ElasticsearchTransport transport = new RestClientTransport(
//            restClient, new JacksonJsonpMapper());
//
//        // And create the API client
//        ElasticsearchClient client = new ElasticsearchClient(transport);
//        //end::create-client
//
//        //tag::first-request
//        SearchResponse<JSpan> search = client.search(s -> s
//            .index("jaeger-span-2022-04-11")
//            .query(q -> q
//                .term(t -> t
//                    .field("spanID")
//                    .value(v -> v.stringValue("078e03e66266cd22"))
//                )),
//                JSpan.class);
//
//        for (Hit<JSpan> hit: search.hits().hits()) {
//            processProduct(hit.source());
//        }
//        //end::first-request
//    }
//
//    private void processProduct(JSpan jSpan) {
//        System.out.println(jSpan);
//    }
//
//}