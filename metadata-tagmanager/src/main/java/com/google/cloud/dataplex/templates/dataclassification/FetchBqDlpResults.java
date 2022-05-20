package com.google.cloud.dataplex.templates.dataclassification;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.JobException;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import com.google.cloud.dataplex.templates.dataclassification.config.DataClassificationConfig;
import com.google.cloud.dataplex.utils.BigqueryEntity;
import com.google.protobuf.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchBqDlpResults {

    private static final Logger LOGGER =
    LoggerFactory.getLogger(FetchBqDlpResults.class);

    public static final String QUERY = "SELECT table_profile.sensitivity_score.score as sensitivity_score,table_profile.data_risk_level.score as risk_score, table_profile.encryption_status as encryption_status, "
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
            + "' group by sensitivity_score, risk_score, encryption_status, ts_seconds, ts_nanos limit 1";

    private String sensitivityScore;
    private String riskScore;
    private String encryptionStatus;
    private String infoTypes;
    private Timestamp profileTs;
    private Long numRows;



    public String getSensitivityScore() {
        return sensitivityScore;
    }

    public void setSensitivityScore(String sensitivityScore) {
        this.sensitivityScore = sensitivityScore;
    }

    public String getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(String riskScore) {
        this.riskScore = riskScore;
    }

    public String getEncryptionStatus() {
        return encryptionStatus;
    }

    public void setEncryptionStatus(String encryptionStatus) {
        this.encryptionStatus = encryptionStatus;
    }

    public String getInfoTypes() {
        return infoTypes;
    }

    public void setInfoTypes(String infoTypes) {
        this.infoTypes = infoTypes;
    }

    public Timestamp getProfileTs() {
        return profileTs;
    }

    public void setProfileTs(Timestamp profileTs) {
        this.profileTs = profileTs;
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
    public static FetchBqDlpResults getResults(String entityDataPath, DataClassificationConfig config) {
            BigqueryEntity bqEntity = BigqueryEntity.getBqAttributes(entityDataPath);

            FetchBqDlpResults result = new FetchBqDlpResults();

                String bqQuery;

            bqQuery = String.format(QUERY, config.getDLPReportConfig().getProjectId().trim(),
                    config.getDLPReportConfig().getDatasetId().trim(),
                    config.getDLPReportConfig().getTableId().trim(),
                    bqEntity.getDatasetId(), bqEntity.getTableId(),
                    bqEntity.getProjectId());

            BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(bqQuery).build();

            try {
                TableResult bqResults = bigquery.query(queryConfig);
                LOGGER.info("Query executed against the DLP Table is {}" , bqQuery);
                LOGGER.info("Number of rows retrieved {}", bqResults.getTotalRows());

                result.setNumRows(bqResults.getTotalRows()); 

                if(bqResults.getTotalRows() >= 1) {
                
                for (FieldValueList row : bqResults.iterateAll()) {
                    // We can use the `get` method along with the column
                    // name to get the corresponding row entry

                    result.setSensitivityScore(row.get("sensitivity_score")
                            .getStringValue());
                    result.setRiskScore(row.get("risk_score").getStringValue());

                    result.setEncryptionStatus(row.get("encryption_status")
                            .getStringValue());
                    result.setInfoTypes(row.get("info_types").getStringValue()); 
                    


                  result.setProfileTs(Timestamp.newBuilder()
                            .setSeconds(row.get("ts_seconds")
                                    .getNumericValue().longValue())
                            .setNanos(row.get("ts_nanos").getNumericValue()
                                    .intValue())
                            .build());
                  
                }
            }
                else
                {
                    result.setSensitivityScore("Not Available!");
                    result.setRiskScore("Not Available!");
                    result.setEncryptionStatus("Not Available!");
                    result.setInfoTypes("Not Available");
                    result.setProfileTs(Timestamp.newBuilder()
                    .setSeconds(0)
                    .setNanos(0).build());
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
