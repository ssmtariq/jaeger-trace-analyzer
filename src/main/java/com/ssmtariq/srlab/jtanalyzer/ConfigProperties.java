package com.ssmtariq.srlab.jtanalyzer;

import org.springframework.beans.factory.annotation.Value;

public class ConfigProperties {
    @Value("${es.schema}")
    public String esSchema;

//    @Value("${es.schema}")
//    public void setEsSchema(String esSchema) {
//        ES_SCHEMA = esSchema;
//    }

    @Value("${es.host}")
    public String esHost;

//    @Value("${es.host}")
//    public void setEsHost(String esHost) {
//        ES_HOST = esHost;
//    }

    @Value("${es.port}")
    public Integer ES_PORT;

//    @Value("${es.port}")
//    public void setEsPort(String esPort) {
//        ES_PORT = Integer.valueOf(esPort);
//    }

    @Value("${es.index}")
    public String ES_INDEX = "*"; // accepts * as wildcard, .e.g log*

//    @Value("${es.index}")
//    public void setEsIndex(String esIndex) {
//        ES_INDEX = esIndex;
//    }
}