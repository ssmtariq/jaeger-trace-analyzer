package com.ssmtariq.srlab.jtanalyzer;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class App {

	final private static String[] FETCH_FIELDS = { "startTimeMillis", "spanID" };

	final private static String MATCH_FIELD = "spanID";
	final private static String[] MUST_MATCH = { "1b9f189c328ddbb1" };
	final private static String[] MUST_NOT_MATCH = { "21.211.33.63" };

	final private static String TIME_FIELD = "startTimeMillis";
	final private static String START_TIME = "2022-04-18T16:06:50";
	final private static String END_TIME = "2025-05-06T00:00:00";

	final private static String INDEX = "jaeger-span-2022-04-18"; // accepts * as wildcard, .e.g log*

	public static void client() throws IOException {

		/**
		 * Configure elastic search client
		 */
		RestHighLevelClient esClient = new RestHighLevelClient(
				RestClient.builder(new HttpHost("192.168.5.248", 9200, "http")));

		/**
		 * Prepare ES Query
		 */
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		if (MUST_MATCH.length > 0) {
			for (String match : MUST_MATCH) {
				queryBuilder.must(QueryBuilders.matchQuery(MATCH_FIELD, match));
			}
		}

		if (MUST_NOT_MATCH.length > 0) {
			for (String notMatch : MUST_NOT_MATCH) {
				queryBuilder.mustNot(QueryBuilders.matchQuery(MATCH_FIELD, notMatch));
			}
		}

		queryBuilder.must(QueryBuilders.rangeQuery(TIME_FIELD).gte(START_TIME));
		queryBuilder.must(QueryBuilders.rangeQuery(TIME_FIELD).lte(END_TIME));

		searchSourceBuilder.query(queryBuilder).fetchSource(FETCH_FIELDS, null);
		searchRequest.indices(INDEX);
		searchRequest.source(searchSourceBuilder);

		/**
		 * Query elastic search
		 */
		SearchResponse searchResponse = esClient.search(searchRequest);

		if (searchResponse.getHits().getTotalHits() > 0) {

			System.out.println(searchResponse.getHits().getTotalHits());
			
			for (SearchHit hit : searchResponse.getHits()) {
				System.out.println("Match: ");
				for (String fetchField : FETCH_FIELDS) {
					System.out.println(" - " + fetchField + " " + hit.getSourceAsMap().get(fetchField));
				}
			}
		} else {
			System.out.println("No results matching the criteria.");
		}

		esClient.close();

	}
}