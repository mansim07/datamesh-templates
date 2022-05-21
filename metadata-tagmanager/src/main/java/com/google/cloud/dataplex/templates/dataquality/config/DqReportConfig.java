package com.google.cloud.dataplex.templates.dataquality.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DqReportConfig {


    private String project_id; 


    private String dataset_id; 


    private String table_id; 

    public String getProjectId() {
        return project_id;
    }

    public void setProjectId(String project_id) {
        this.project_id = project_id;
    }

    public String getDatasetId() {
        return dataset_id;
    }

    public void setDatasetId(String dataset_id) {
        this.dataset_id = dataset_id;
    }

    public String getTableId() {
        return table_id;
    }

    public void setTableId(String table_id) {
        this.table_id = table_id;
    }

}