package com.google.cloud.dataplex.templates.dataquality;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.JobException;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
//import com.google.cloud.dataplex.templates.dataquality.config.DataProductQualityConfig;
import com.google.cloud.dataplex.templates.dataquality.config.DataProductQualityConfig;
import com.google.cloud.dataplex.utils.BigqueryEntity;
import com.google.protobuf.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchBqDqResults {

    private static final Logger LOGGER = LoggerFactory.getLogger(FetchBqDqResults.class);

    /*
     * public static final String QUERY =
     * "SELECT table_profile.sensitivity_score.score as sensitivity_score,table_profile.data_risk_level.score as risk_score, table_profile.encryption_status as encryption_status, "
     * +
     * "CONCAT(CASE WHEN STRING_AGG(info_types.info_type.name) IS NULL THEN '' ELSE STRING_AGG(info_types.info_type.name) END , CASE WHEN STRING_AGG(other_info_types.info_type.name) IS NULL THEN '' ELSE STRING_AGG(other_info_types.info_type.name)  END) as info_types, table_profile.profile_last_generated.seconds as ts_seconds , table_profile.profile_last_generated.nanos as ts_nanos"
     * + " FROM `" + "%s" + "." + "%s" + "." + "%s" +
     * "` LEFT OUTER JOIN UNNEST(table_profile.predicted_info_types) AS info_types LEFT OUTER JOIN UNNEST(table_profile.other_info_types) AS other_info_types "
     * + " WHERE table_profile.dataset_id='" + "%s" + "'" + " and table_profile.table_id='" + "%s" +
     * "'" + " and table_profile.dataset_project_id='" + "%s" +
     * "' group by sensitivity_score, risk_score, encryption_status, ts_seconds, ts_nanos limit 1";
     */

    public static final String QUERY =
            "WITH `latest_dq` AS (SELECT invocation_id,MAX(TIMESTAMP(execution_ts)) AS exec_ts, "
                    + " FROM `" + "%s" + "." + "%s" + "." + "%s" + "` summary" + " WHERE table_id='"
                    + "%s" + "." + "%s" + "." + "%s" + "' "
                    + " GROUP BY invocation_id ORDER BY exec_ts LIMIT 1 ) "
                    + "(SELECT MAX(invocation_id) AS invocation_id,"
                    + "STRING(MAX(execution_ts),'UTC') AS exec_ts,"
                    + "dimension AS dimension,ROUND(AVG(percentage),0) AS percentage "
                    + "FROM (SELECT summary.invocation_id AS invocation_id, "
                    + "execution_ts AS execution_ts,dimension AS dimension,"
                    + "CASE WHEN complex_rule_validation_errors_count IS NOT NULL THEN 100 - (complex_rule_validation_errors_count/rows_validated * 100) "
                    + "ELSE success_percentage * 100 END AS percentage " + " FROM `" + "%s" + "."
                    + "%s" + "." + "%s" + "` summary "
                    + "JOIN `latest_dq` dq ON summary.invocation_id=dq.invocation_id) A GROUP BY dimension "
                    + "UNION ALL SELECT * FROM ( SELECT MAX(summary.invocation_id) AS invocation_id, "
                    + "STRING(MAX(execution_ts),'UTC') AS exec_ts,'QUALITY_SCORE' AS dimension, "
                  + "ROUND(AVG(CASE WHEN complex_rule_validation_errors_count IS NOT NULL THEN 100 - (complex_rule_validation_errors_count/rows_validated * 100) ELSE success_percentage * 100 END),0) AS percentage " + " FROM `" + "%s" + "." + "%s"                    
                    + "." + "%s" + "` summary "
                    + "JOIN `latest_dq` dq ON summary.invocation_id=dq.invocation_id ) B where invocation_id is not null )";
    private String qualityScore;
    private String percentageTimeliness;
    private String percentageCorrectness;
    private String percentageIntegrity;
    private String percentageConformity;
    private String percentageCompleteness;
    private String percentageUniqueness;
    private String percentageAccuracy;
    private String execTs; // To Do: Change it to timestamp in future

    public FetchBqDqResults() {
        this.qualityScore = "-1";
        this.percentageTimeliness = "-1";
        this.percentageCorrectness = "-1";
        this.percentageIntegrity = "-1";
        this.percentageConformity = "-1";
        this.percentageCompleteness = "-1";
        this.percentageUniqueness = "-1";
        this.percentageAccuracy = "-1";
        this.execTs = "9999-12-01T00:00:00.000000Z";

    }

    public String getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(String qualityScore) {
        this.qualityScore = qualityScore;
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

    /**
     * Fetch BQ Results
     *
     * @param args command line arguments
     * @return parsed arguments
     */
    public static FetchBqDqResults getResults(String entityDataPath,
            DataProductQualityConfig config) {

       
        BigqueryEntity bqEntity = BigqueryEntity.getBqAttributes(entityDataPath);
        
    
        FetchBqDqResults result = new FetchBqDqResults();

        String bqQuery;

        bqQuery = String.format(QUERY, config.getDqReportConfig().getProjectId().trim(),
                config.getDqReportConfig().getDatasetId().trim(),
                config.getDqReportConfig().getTableId().trim(), bqEntity.getProjectId(),
                bqEntity.getDatasetId(), bqEntity.getTableId(),
                config.getDqReportConfig().getProjectId().trim(),
                config.getDqReportConfig().getDatasetId().trim(),
                config.getDqReportConfig().getTableId().trim(),
                config.getDqReportConfig().getProjectId().trim(),
                config.getDqReportConfig().getDatasetId().trim(),
                config.getDqReportConfig().getTableId().trim());
        //

        LOGGER.info("Query executed against the DQ Table is {}", bqQuery);

        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(bqQuery).build();

        try {
            TableResult bqResults = bigquery.query(queryConfig);
            LOGGER.info("Query executed against the DQ Table is {}", bqQuery);
            LOGGER.info("Number of rows retrieved {}", bqResults.getTotalRows());

            // result.setNumRows(bqResults.getTotalRows());

            if (bqResults.getTotalRows() >= 1) {

                for (FieldValueList row : bqResults.iterateAll()) {
                    // We can use the `get` method along with the column
                    // name to get the corresponding row entry
                    if (row.get("dimension").getStringValue().toLowerCase()
                            .equals(new String("consistency"))) {
                        result.setPercentageCorrectness(row.get("percentage").getStringValue());

                    }

                    if (row.get("dimension").getStringValue().toLowerCase()
                            .equals(new String("correctness"))) {
                        result.setPercentageCorrectness(row.get("percentage").getStringValue());

                    }

                    if (row.get("dimension").getStringValue().toLowerCase()
                            .equals(new String("duplication"))) {
                        result.setPercentageUniqueness(row.get("percentage").getStringValue());

                    }

                    if (row.get("dimension").getStringValue().toLowerCase()
                            .equals(new String("completeness"))) {
                        result.setPercentageCompleteness(row.get("percentage").getStringValue());

                    }

                    if (row.get("dimension").getStringValue().toLowerCase()
                            .equals(new String("conformance"))) {
                        result.setPercentageConformity(row.get("percentage").getStringValue());

                    }

                    if (row.get("dimension").getStringValue().toLowerCase()
                            .equals(new String("integrity"))) {
                        result.setPercentageIntegrity(row.get("percentage").getStringValue());

                    }

                    if (row.get("dimension").getStringValue().toLowerCase()
                            .equals(new String("quality_score"))) {
                        result.setQualityScore(row.get("percentage").getStringValue());
                        result.setExecTs(row.get("exec_ts").getStringValue());

                    }
                    if (row.get("dimension").getStringValue().toLowerCase()
                            .equals(new String("timeliness"))) {
                        result.setPercentageTimeliness(row.get("percentage").getStringValue());

                    }


                }
            }


        } catch (JobException e) {

            e.printStackTrace();
        } catch (InterruptedException e) {

            LOGGER.warn("Interrupted!", e);
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }

        return result;


    }

}

