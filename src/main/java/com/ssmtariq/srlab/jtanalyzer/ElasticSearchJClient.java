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
	public void client() throws IOException {
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
		do {
			if (searchResponse.getHits().getTotalHits() > 0) {
				System.out.println("Total number of spans found: "+searchResponse.getHits().getTotalHits());
				for (SearchHit hit : searchResponse.getHits()) {

					/* Create new node for the span */
					Node node = new Node(String.valueOf(hit.getSourceAsMap().get(KEY_SPAN_ID)), (Integer) hit.getSourceAsMap().get(KEY_DURATION));
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
					/* Add each node into a Map */
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
			}
		});

		/* Display roots found */
//		nodeMap.forEach((k,v)->{
//			if(v.getChildren().size()>0 && v.getServiceName().equals(SERVICE_PRESERVE_OTHER)){
//				System.out.println("Node Data - "+ counter[0]);
//				System.out.println("\t SpanId: "+v.getSpanId() +";"+" ServiceName: "+Utility.getServiceNameShort(v.getServiceName())+";"+" ParentId: "+v.getParentId()+";"+" Duration: "+v.getDuration()+";"+" Is Root: "+v.isRoot());
//				System.out.println("\t Childrens: "+v.getChildren());
//				roots.add(v.getSpanId());
//			}
//			counter[0]++;
//		});

		/* Display the tree info */
		roots.forEach(s->{
			GenericTree.displaySpanSummary(nodeMap.get(s));
		});

		GenericTree.displayAggregatedResult();

		System.exit(0);
	}

	/**
	 * Build query for elasticsearch
	 * @return
	 */
	private static SearchSourceBuilder buildQuery(){
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		queryBuilder.must(QueryBuilders.existsQuery("flags"));

		queryBuilder.must(QueryBuilders.rangeQuery(TIME_FIELD).gte(START_TIME));
		queryBuilder.must(QueryBuilders.rangeQuery(TIME_FIELD).lte(END_TIME));

		searchSourceBuilder.query(queryBuilder).fetchSource(FETCH_FIELDS, null);
		searchSourceBuilder.sort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC);
		return searchSourceBuilder;
	}
}