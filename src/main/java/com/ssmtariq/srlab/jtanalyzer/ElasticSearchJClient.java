package com.ssmtariq.srlab.jtanalyzer;

import com.ssmtariq.srlab.jtanalyzer.model.TreeNode;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.ssmtariq.srlab.jtanalyzer.Constants.*;

public class ElasticSearchJClient {
	private static Map<String, TreeNode> nodeMap = new HashMap<>();

	/**
	 * Elasticsearch rest client to query from es
	 * @throws IOException
	 */
	public void client() throws IOException {
		// Configure elastic search client
		RestHighLevelClient esClient = new RestHighLevelClient(
				RestClient.builder(new HttpHost(ES_HOST, ES_PORT, ES_SCHEMA)));

		//Prepare ES Query
		SearchRequest searchRequest = new SearchRequest();

		searchRequest.indices(ES_INDEX);
		searchRequest.source(buildQuery().size(10000));

		// Query elastic search
		SearchResponse searchResponse = esClient.search(searchRequest);

		if (searchResponse.getHits().getTotalHits() > 0) {
			System.out.println("Total number of records found: "+searchResponse.getHits().getTotalHits());
			for (SearchHit hit : searchResponse.getHits()) {
//				System.out.println("Match: ");
				TreeNode treeNode = new TreeNode(String.valueOf(hit.getSourceAsMap().get(KEY_SPAN_ID)), (Integer) hit.getSourceAsMap().get(KEY_DURATION));
				for (String fetchField : FETCH_FIELDS) {
//					System.out.println(" - " + fetchField + " " + hit.getSourceAsMap().get(fetchField));

					//Retrieve parent spanId from references
					if(fetchField.equals(KEY_REFERENCES) && Objects.nonNull(hit.getSourceAsMap().get(fetchField))){
						ArrayList<Map<String, Object>> references = (ArrayList<Map<String, Object>>) hit.getSourceAsMap().get(fetchField);
						if (references.size()>0){
							treeNode.setParentId((String) references.get(0).get(KEY_SPAN_ID));
						}
					}

					//Retrieve parent spanId from references
					if(fetchField.equals(KEY_PROCESS) && Objects.nonNull(hit.getSourceAsMap().get(fetchField))){
						Map<String, Object> process = (Map<String, Object>) hit.getSourceAsMap().get(fetchField);
						treeNode.setServiceName((String) process.get(KEY_SERVICE_NAME));
					}
				}
				nodeMap.put(treeNode.getSpanId(), treeNode);
				if(!treeNode.isRoot() && nodeMap.containsKey(treeNode.getParentId())){
					nodeMap.get(treeNode.getParentId()).addChildren(treeNode);
				}
			}
		} else {
			System.out.println("No results matching the criteria.");
		}
		esClient.close();

		final int[] counter = {1};
		nodeMap.forEach((k,v)->{
			if(v.getChildren().size()>1){
				System.out.println("Node Data - "+ counter[0]);
				System.out.println("\t SpanId: "+v.getSpanId() +";"+" ServiceName: "+v.getServiceName()+";"+" ParentId: "+v.getParentId()+";"+" Duration: "+v.getDuration()+";"+" Is Root: "+v.isRoot());
				System.out.println("\t Childrens: "+v.getChildren());
			}
			counter[0]++;
		});

		System.exit(0);
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