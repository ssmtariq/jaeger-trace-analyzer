package com.ssmtariq.srlab.jtanalyzer;

import com.ssmtariq.srlab.jtanalyzer.model.Node;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.*;

import static com.ssmtariq.srlab.jtanalyzer.Constants.*;

public class ElasticSearchJClient {
	private static final Integer SEARCH_CONTEXT_LIFE_TIME = 60000;
	private static Map<String, Node> nodeMap = new HashMap<>();
	private static List<String> roots = new ArrayList<>();

	/**
	 * Elasticsearch rest client to query from es
	 * @throws IOException
	 */
	public void execute() throws IOException {
		// Configure elastic search client
		RestHighLevelClient esClient = new RestHighLevelClient(
				RestClient.builder(new HttpHost(ES_HOST, ES_PORT, ES_SCHEMA)));

		//Prepare search request
		SearchRequest searchRequest = new SearchRequest().scroll(new TimeValue(SEARCH_CONTEXT_LIFE_TIME));
		searchRequest.indices(ES_INDEX);
		/* Set elasticsearch query for the request */
		searchRequest.source(buildQuery().size(10000));

		// Query elastic search
		SearchResponse searchResponse = esClient.search(searchRequest);

		//Scroll until no hits are returned
		System.out.println("Total number of spans found: "+searchResponse.getHits().getTotalHits());
		System.out.println("Calculating the results ./././.");
		do {
			if (searchResponse.getHits().getTotalHits() > 0) {
				for (SearchHit hit : searchResponse.getHits()) {

					/* Create new node for the span */
					Node node = new Node(String.valueOf(hit.getSourceAsMap().get(KEY_SPAN_ID)), ((Number) hit.getSourceAsMap().get(KEY_DURATION)).longValue());
					node.setExclusiveDuration(((Number) hit.getSourceAsMap().get(KEY_DURATION)).longValue());
					node.setOperationName(String.valueOf(hit.getSourceAsMap().get(KEY_OPERATION_NAME)));
					for (String fetchField : FETCH_FIELDS) {
						//Set parentId of the span from references
						if(fetchField.equals(KEY_REFERENCES) && Objects.nonNull(hit.getSourceAsMap().get(fetchField))){
							ArrayList<Map<String, Object>> references = (ArrayList<Map<String, Object>>) hit.getSourceAsMap().get(fetchField);
							if (references.size()>0){
								node.setParentId((String) references.get(0).get(KEY_SPAN_ID));
							}
						}

						//Set serviceName of the span from process
						if(fetchField.equals(KEY_PROCESS) && Objects.nonNull(hit.getSourceAsMap().get(fetchField))){
							Map<String, Object> process = (Map<String, Object>) hit.getSourceAsMap().get(fetchField);
							node.setServiceName((String) process.get(KEY_SERVICE_NAME));
						}
					}
					/* Add each node into a Map where key is the unique jaeger span id */
					nodeMap.put(node.getSpanId(), node);
				}
			} else {
				System.out.println("No results matching the criteria.");
			}

			searchResponse = esClient.searchScroll(new SearchScrollRequest(searchResponse.getScrollId()).scroll(new TimeValue(SEARCH_CONTEXT_LIFE_TIME)));
		} while(searchResponse.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while loop.

		esClient.close();

		System.out.println("Map size: "+nodeMap.size());

		final int[] counter = {1};

		/* Populate child list of each node */
		nodeMap.forEach((k,v)->{
			if(!v.isRoot() && nodeMap.containsKey(v.getParentId())){
				nodeMap.get(v.getParentId()).addChildren(v);
				/* update exclusive latency of each parent node by subtracting their children's inclusive latency */
				nodeMap.get(v.getParentId()).setExclusiveDuration(nodeMap.get(v.getParentId()).getExclusiveDuration()-v.getDuration());
			}
		});

		/* Display roots found */
		nodeMap.forEach((k,v)->{
			if(v.getChildren().size()>0 && v.getServiceName().equals(MICRO_SERVICE_NAME)){
				roots.add(v.getSpanId());
			} else if (NUMBER_OF_CALLING_SERVICE_COUNT ==1 && v.getServiceName().equals(MICRO_SERVICE_NAME) ) {
				roots.add(v.getSpanId());
			}
			counter[0]++;
		});

		MergedTree.generate(nodeMap, roots);
		MergedTree.displayAggregatedResultSortedByExclusiveAvgLatencyDesc();

		System.exit(0);
	}

	/**
	 * Build query for elasticsearch
	 * @return
	 */
	private static SearchSourceBuilder buildQuery(){
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		queryBuilder.must(QueryBuilders.existsQuery(KEY_FLAGS));
		queryBuilder.must(QueryBuilders.rangeQuery(TIME_FIELD).gte(START_TIME).lte(END_TIME));

		searchSourceBuilder.query(queryBuilder).fetchSource(FETCH_FIELDS, null);
		searchSourceBuilder.sort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC);
		return searchSourceBuilder;
	}
}