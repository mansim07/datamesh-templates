package com.google.cloud.dataplex.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BigqueryEntity {

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

    /**
     * Parse command line arguments
     *
     * @param args command line arguments
     * @return parsed arguments
     */
    public static BigqueryEntity getBqAttributes(String entity) {
        try {
            Pattern pattern = Pattern
            .compile("^projects\\/(\\w+.*)\\/datasets\\/(\\w+.*)\\/tables\\/(\\w+.*)");
            Matcher match = pattern.matcher(entity);
            match.matches();
            BigqueryEntity bq_entity = new BigqueryEntity();
            bq_entity.setProjectId(match.group(1));
            bq_entity.setDatasetId(match.group(2));
            bq_entity.setTableId(match.group(3));
            return bq_entity;

        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

    }


}
