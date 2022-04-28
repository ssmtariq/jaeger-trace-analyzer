package com.ssmtariq.srlab.jtanalyzer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class ElasticSearchJClient {
	private static final String ES_SCHEMA = "http";
	private static final String ES_HOST = "192.168.242.184";
	private static final Integer ES_PORT = 9200;

	private static final String[] FETCH_FIELDS = { "startTimeMillis", "spanID", "references", "traceID", "process.serviceName" };

	private static final String MATCH_FIELD = "spanID";
	private static final String[] MUST_MATCH = { "2a6ff9e9e58fa03d" };
	private static final String[] MUST_NOT_MATCH = { "21.211.33.63" };

	private static final String TIME_FIELD = "startTimeMillis";
	private static final String START_TIME = dateToMilliseconds("2022/04/18 16:00:00"); //"1650312000000";
	private static final String END_TIME = dateToMilliseconds("2025/05/06 00:00:00");// "1746561600000";//"2025-05-06T00:00:00";

	private static final String INDEX = "jaeger-span-*"; // accepts * as wildcard, .e.g log*

	/**
	 * Elasticsearch rest client to query from es
	 * @throws IOException
	 */
	public static void client() throws IOException {
		// Configure elastic search client
		RestHighLevelClient esClient = new RestHighLevelClient(
				RestClient.builder(new HttpHost(ES_HOST, ES_PORT, ES_SCHEMA)));

		//Prepare ES Query
		SearchRequest searchRequest = new SearchRequest();

		searchRequest.indices(INDEX);
		searchRequest.source(buildQuery());

		// Query elastic search
		SearchResponse searchResponse = esClient.search(searchRequest);

		if (searchResponse.getHits().getTotalHits() > 0) {
			System.out.println("Total number of records found: "+searchResponse.getHits().getTotalHits());

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

	/**
	 * Convert custom date to milliseconds
	 * @param iDate
	 * @return
	 */
	private static String dateToMilliseconds(String iDate){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(iDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return String.valueOf(date.getTime());
	}

	/**
	 * Build query for elasticsearch
	 * @return
	 */
	private static SearchSourceBuilder buildQuery(){
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		// if (MUST_MATCH.length > 0) {
		// 	for (String mustMatchVal : MUST_MATCH) {
		// 		queryBuilder.must(QueryBuilders.matchQuery(MATCH_FIELD, mustMatchVal));
		// 	}
		// }
		queryBuilder.must(QueryBuilders.existsQuery("flags"));

		// if (MUST_NOT_MATCH.length > 0) {
		// 	for (String notMatchVal : MUST_NOT_MATCH) {
		// 		queryBuilder.mustNot(QueryBuilders.matchQuery(MATCH_FIELD, notMatchVal));
		// 	}
		// }

		queryBuilder.must(QueryBuilders.rangeQuery(TIME_FIELD).gte(START_TIME));
		queryBuilder.must(QueryBuilders.rangeQuery(TIME_FIELD).lte(END_TIME));

		searchSourceBuilder.query(queryBuilder).fetchSource(FETCH_FIELDS, null);
		return searchSourceBuilder;
	}
}