package com.google.cloud.dataplex.templates.dataquality;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.JobException;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import com.google.cloud.dataplex.templates.dataclassification.config.DataClassificationConfig;
import com.google.cloud.dataplex.templates.dataquality.config.DataProductQualityConfig;
import com.google.cloud.dataplex.utils.BigqueryEntity;
import com.google.protobuf.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchBqDqResults {

    private static final Logger LOGGER =
    LoggerFactory.getLogger(FetchBqDqResults.class);

    /* public static final String QUERY = "SELECT table_profile.sensitivity_score.score as sensitivity_score,table_profile.data_risk_level.score as risk_score, table_profile.encryption_status as encryption_status, "
            + "CONCAT(CASE WHEN STRING_AGG(info_types.info_type.name) IS NULL THEN '' ELSE STRING_AGG(info_types.info_type.name) END , CASE WHEN STRING_AGG(other_info_types.info_type.name) IS NULL THEN '' ELSE STRING_AGG(other_info_types.info_type.name)  END) as info_types, table_profile.profile_last_generated.seconds as ts_seconds , table_profile.profile_last_generated.nanos as ts_nanos"
            + " FROM `"
            + "%s"
            + "."
            + "%s"
            + "."
            + "%s"
            + "` LEFT OUTER JOIN UNNEST(table_profile.predicted_info_types) AS info_types LEFT OUTER JOIN UNNEST(table_profile.other_info_types) AS other_info_types "
            + " WHERE table_profile.dataset_id='" + "%s"
            + "'"
            + " and table_profile.table_id='" + "%s" + "'"
            + " and table_profile.dataset_project_id='"
            + "%s"
            + "' group by sensitivity_score, risk_score, encryption_status, ts_seconds, ts_nanos limit 1"; */

    public static final String QUERY = "WITH `latest_dq` AS (SELECT invocation_id,MAX(TIMESTAMP(execution_ts) AS exec_ts, FROM `mdm-dg.operations_db.dq_summary` WHERE table_id='customers-343423.consolidated_customers_data.customer_data' GROUP BY invocation_id ORDER BY exec_ts LIMIT 1 ) (SELECT MAX(invocation_id) AS invocation_id,MAX(execution_ts) AS execution_ts,dimension AS dimension,AVG(percentage) AS percentage FROM (SELECT summary.invocation_id AS invocation_id,execution_ts AS execution_ts,dimension AS dimension,CASE WHEN complex_rule_validation_errors_count IS NOT NULL THEN 100 - (complex_rule_validation_errors_count/rows_validated * 100) ELSE success_percentage * 100 END AS percentage FROM `mdm-dg.operations_db.dq_summary` summary JOIN `latest_dq` dq ON summary.invocation_id=dq.invocation_id) A GROUP BY dimension)";

    private String percentageTimeliness;
    private String percentageCorrectness;
    private String percentageIntegrity;
    private String percentageConformity;
    private String percentageCompleteness;
    private String percentageUniqueness;
    private String percentageAccuracy;
    private String execTs; //To Do: Change it to timestamp in future
    private Long numRows;

    public FetchBqDqResults(){
        this.percentageTimeliness="NA";
        this.percentageCorrectness="NA";
        this.percentageIntegrity="NA";
        this.percentageConformity="NA";
        this.percentageCompleteness="NA";
        this.percentageUniqueness="NA";
        this.percentageAccuracy="NA";
        this.execTs="NA";
        this.numRows=0L;
    }

    public String getPercentageTimeliness() {
        return percentageTimeliness;
    }

    public void setPercentageTimeliness(String percentageTimeliness) {
        this.percentageTimeliness = percentageTimeliness;
    }

    public String getPercentageCorrectness() {
        return percentageCorrectness;
    }

    public void setPercentageCorrectness(String percentageCorrectness) {
        this.percentageCorrectness = percentageCorrectness;
    }

    public String getPercentageIntegrity() {
        return percentageIntegrity;
    }

    public void setPercentageIntegrity(String percentageIntegrity) {
        this.percentageIntegrity = percentageIntegrity;
    }

    public String getPercentageCompleteness() {
        return percentageCompleteness;
    }

    public void setPercentageCompleteness(String percentageCompleteness) {
        this.percentageCompleteness = percentageCompleteness;
    }


    public String getPercentageUniqueness() {
        return percentageUniqueness;
    }

    public void setPercentageUniqueness(String percentageUniqueness) {
        this.percentageUniqueness = percentageUniqueness;
    }

    public String getPercentageAccuracy() {
        return percentageAccuracy;
    }

    public void setPercentageAccuracy(String percentageAccuracy) {
        this.percentageAccuracy = percentageAccuracy;
    }
    public String getPercentageConformity() {
        return percentageConformity;
    }

    public void setPercentageConformity(String percentageConformity) {
        this.percentageConformity = percentageConformity;
    }

    public String getExecTs() {
        return execTs;
    }

    public void setExecTs(String execTs) {
        this.execTs = execTs;
    }


    public Long getNumRows() {
        return numRows;
    }

    public void setNumRows(long l) {
        this.numRows = l;
    }



    /**
     * Fetch BQ Results
     *
     * @param args command line arguments
     * @return parsed arguments
     */
    public static FetchBqDqResults getResults(String entityDataPath, DataProductQualityConfig config) {
            BigqueryEntity bqEntity = BigqueryEntity.getBqAttributes(entityDataPath);

            FetchBqDqResults result = new FetchBqDqResults();

                String bqQuery;

            bqQuery = String.format(QUERY, config.getDqProject().getProjectId().trim(),
                    config.getDLPReportConfig().getDatasetId().trim(),
                    config.getDLPReportConfig().getTableId().trim(),
                    bqEntity.getDatasetId(), bqEntity.getTableId(),
                    bqEntity.getProjectId());

            BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(bqQuery).build();

            try {
                TableResult bqResults = bigquery.query(queryConfig);
                LOGGER.info("Query executed against the DQ Table is {}" , bqQuery);
                LOGGER.info("Number of rows retrieved {}", bqResults.getTotalRows());

                result.setNumRows(bqResults.getTotalRows()); 

                if(bqResults.getTotalRows() >= 1) {
                
                for (FieldValueList row : bqResults.iterateAll()) {
                    // We can use the `get` method along with the column
                    // name to get the corresponding row entry
                    if(row.get("dimension").getStringValue().equals(new String("CORRECTNESS"))){
                        result.setPercentageCorrectness(row.get("percentage").getStringValue());    

                    }

                    if(row.get("dimension").getStringValue().equals(new String("CORRECTNESS"))){
                        result.setPercentageCorrectness(row.get("percentage").getStringValue());    

                    }

                    if(row.get("dimension").getStringValue().equals(new String("CORRECTNESS"))){
                        result.setPercentageCorrectness(row.get("percentage").getStringValue());    

                    }

                    if(row.get("dimension").getStringValue().equals(new String("CORRECTNESS"))){
                        result.setPercentageCorrectness(row.get("percentage").getStringValue());    

                    }

                    if(row.get("dimension").getStringValue().equals(new String("CORRECTNESS"))){
                        result.setPercentageCorrectness(row.get("percentage").getStringValue());    

                    }
                    

                  
                }
            }
                               

            } catch (JobException e) {

                e.printStackTrace();
            } catch (InterruptedException e) {
     
                LOGGER.warn( "Interrupted!", e);
                // Restore interrupted state...
                Thread.currentThread().interrupt();
            }

            return result; 


    }

}

