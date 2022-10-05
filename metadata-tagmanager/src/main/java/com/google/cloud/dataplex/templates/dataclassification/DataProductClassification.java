
/*
 * Copyright (C) 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.dataplex.templates.dataclassification;

import static com.google.cloud.dataplex.utils.Constants.API_URI_BQ;
import static com.google.cloud.dataplex.utils.Constants.ENTITY_ID_OPT;
import static com.google.cloud.dataplex.utils.Constants.INPUT_FILE_OPT;
import static com.google.cloud.dataplex.utils.Constants.LAKE_ID_OPT;
import static com.google.cloud.dataplex.utils.Constants.LOCATION_OPT;
import static com.google.cloud.dataplex.utils.Constants.PROJECT_NAME_OPT;
import static com.google.cloud.dataplex.utils.Constants.TAG_TEMPLATE_ID_OPT;
import static com.google.cloud.dataplex.utils.Constants.ZONE_ID_OPT;
import static com.google.cloud.dataplex.utils.Constants.DERIVE_INDICATOR;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.cloud.datacatalog.v1.DataCatalogClient;
import com.google.cloud.datacatalog.v1.Entry;
import com.google.cloud.datacatalog.v1.LookupEntryRequest;
import com.google.cloud.datacatalog.v1.TagField;
import com.google.cloud.dataplex.templates.dataclassification.config.DataClassificationConfig;
import com.google.cloud.dataplex.utils.InputArgsParse;
import com.google.cloud.dataplex.utils.TagOperations;
import com.google.cloud.dataplex.v1.Entity;
import com.google.cloud.dataplex.v1.MetadataServiceClient;
import com.google.protobuf.util.Timestamps;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataProductClassification {
        
        private static final Logger LOGGER =
                        LoggerFactory.getLogger(DataProductClassification.class);
        

        public static void main(String... args) throws IOException, ParseException {
                
                
                LOGGER.info("Started the Data Classification metadata tagging process...");
                Entry entry;

                // SparkSession spark = SparkSession
                // .builder()
                // .appName("JavaPageRank")
                // .config("spark.master","local")
                // .getOrCreate();

                CommandLine cmd = InputArgsParse.parseArguments(args);

                File file = new File(cmd.getOptionValue(INPUT_FILE_OPT));
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                DataClassificationConfig config =
                                objectMapper.readValue(file, DataClassificationConfig.class);

                try (MetadataServiceClient mdplx = MetadataServiceClient.create()) {

                        Entity entity = mdplx.getEntity(String.format(
                                        "projects/%s/locations/%s/lakes/%s/zones/%s/entities/%s",
                                        cmd.getOptionValue(PROJECT_NAME_OPT),
                                        cmd.getOptionValue(LOCATION_OPT),
                                        cmd.getOptionValue(LAKE_ID_OPT),
                                        cmd.getOptionValue(ZONE_ID_OPT),
                                        cmd.getOptionValue(ENTITY_ID_OPT)));
                        String dataplex_entity_name_fqdn = String.format("%s.%s.%s.%s.%s",
                                        cmd.getOptionValue(PROJECT_NAME_OPT),
                                        cmd.getOptionValue(LOCATION_OPT),
                                        cmd.getOptionValue(LAKE_ID_OPT),
                                        cmd.getOptionValue(ZONE_ID_OPT),
                                        cmd.getOptionValue(ENTITY_ID_OPT));

                        try (DataCatalogClient dataCatalogClient = DataCatalogClient.create()) {

                                if ("BIGQUERY".equals(entity.getSystem().name())) {

                                        
                                         entry = dataCatalogClient.lookupEntry(LookupEntryRequest
                                          .newBuilder() .setLinkedResource(String.format("%s/%s",
                                          API_URI_BQ, entity.getDataPath())) .build()); 
                                         
                                        /* 
                                        entry = dataCatalogClient.lookupEntry(LookupEntryRequest
                                                        .newBuilder()
                                                        .setFullyQualifiedName("dataplex:"
                                                                        + dataplex_entity_name_fqdn)
                                                        .build()); */
                                        if (!config.getDLPReportConfig().getProjectId().isEmpty()
                                                        && !config.getDLPReportConfig()
                                                                        .getDatasetId().isEmpty()
                                                        && !config.getDLPReportConfig().getTableId()
                                                                        .isEmpty()) {
                                                LOGGER.info("Fetching the Results from the DLP table...");


                                                FetchBqDlpResults dlpResults = FetchBqDlpResults
                                                                .getResults(entity.getDataPath(),
                                                                                config);

                                                config = resetValues(config, dlpResults);


                                        }

                                        /* set the Tag fields */

                                        /* Fetch the tag name using the tag template */

                                        Map<String, TagField> values = new HashMap<>();

                                        values.put("is_pii", TagField.newBuilder()
                                                        .setBoolValue(config.getHasPii()).build());
                                        values.put("sensitivity_score",
                                                        TagField.newBuilder().setStringValue(config
                                                                        .getSensitivityScore())
                                                                        .build());
                                        values.put("risk_score",
                                                        TagField.newBuilder().setStringValue(
                                                                        config.getRiskScore())
                                                                        .build());
                                        values.put("info_types",
                                                        TagField.newBuilder().setStringValue(
                                                                        config.getInfoTypes())
                                                                        .build());
                                        values.put("is_confidential",
                                                        TagField.newBuilder().setBoolValue(
                                                                        config.getIsConfidential())
                                                                        .build());
                                        values.put("is_restricted",
                                                        TagField.newBuilder().setBoolValue(
                                                                        config.getIsRestricted())
                                                                        .build());
                                        values.put("is_public",
                                                        TagField.newBuilder().setBoolValue(
                                                                        config.getIsPublic())
                                                                        .build());
                                        values.put("is_encrypted",
                                                        TagField.newBuilder().setBoolValue(
                                                                        config.getIsEncrypted())
                                                                        .build());
                                        values.put("encryption_key_type",
                                                        TagField.newBuilder().setStringValue(config
                                                                        .getEncryptionKeyType())
                                                                        .build());

                                        values.put("last_profiling_date", TagField.newBuilder()
                                                        .setTimestampValue(Timestamps.parse(config
                                                                        .getLastProfilingDate()))
                                                        .build());

                                        values.put("last_modified_by",
                                                        TagField.newBuilder().setStringValue(
                                                                        config.getLastModifiedBy())
                                                                        .build());
                                        values.put("last_modified_date", TagField.newBuilder()
                                                        .setTimestampValue(Timestamps.parse(config
                                                                        .getLastModifiedDate()))
                                                        .build());
                                        values.put("related_data_products", TagField.newBuilder()
                                                        .setRichtextValue(config
                                                                        .getRelatedDataProducts())
                                                        .build());

                                        TagOperations.publishTag(entry, dataCatalogClient,
                                                        cmd.getOptionValue(TAG_TEMPLATE_ID_OPT),
                                                        values, "Data Classification Info");

                                        LOGGER.info("Tag was successfully created for entry {} using tag template {}",
                                                        entry.getFullyQualifiedName(),
                                                        cmd.getOptionValue(TAG_TEMPLATE_ID_OPT));

                                }

                                else {
                                        LOGGER.error("Currently Data Classification tagging is only supoprted for BigQuery Tables");
                                        throw new UnsupportedOperationException(
                                                        "Currently Data Classification tagging is only supoprted for BigQuery Tables");
                                }
                        } catch (UnsupportedOperationException e) {
                                e.printStackTrace();
                        }


                }

                // spark.stop();
        }

        public static DataClassificationConfig resetValues(DataClassificationConfig config,
                        FetchBqDlpResults dlpResults) {

                if (config.getSensitivityScore().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getSensitivityScore().isEmpty()) {
                        config.setSensitivityScore(dlpResults.getSensitivityScore());

                }

                if (config.getRiskScore().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getRiskScore().isEmpty()) {
                        config.setRiskScore(dlpResults.getRiskScore());

                }

                if (config.getInfoTypes().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getInfoTypes().isEmpty()) {
                        config.setInfoTypes(dlpResults.getInfoTypes());

                }

                if (config.getEncryptionKeyType().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getEncryptionKeyType().isEmpty()) {
                        config.setEncryptionKeyType(dlpResults.getEncryptionStatus());

                }

                if (config.getLastProfilingDate().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getLastProfilingDate().isEmpty()) {
                        config.setLastProfilingDate(Timestamps.toString(dlpResults.getProfileTs()));

                }

                if (config.getRelatedDataProducts().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getRelatedDataProducts().isEmpty()) {
                        config.setRelatedDataProducts(
                                        "<a href=\"https://console.cloud.google.com/bigquery?p="
                                                        + config.getDLPReportConfig().getProjectId()
                                                        + "&d="
                                                        + config.getDLPReportConfig().getDatasetId()
                                                        + "&page=dataset\">Data product</a>");

                }

                // https://console.cloud.google.com/bigquery?p=mdm-dg&d=operations_db&page=dataset


                if (config.getSensitivityScore().equalsIgnoreCase("SENSITIVITY_HIGH")
                                || config.getSensitivityScore()
                                                .equalsIgnoreCase("SENSITIVITY_MODERATE")
                                || config.getSensitivityScore().isEmpty()) {
                        config.setHasPii(true);
                } else
                        config.setHasPii(false);

                if (config.getLastModifiedBy().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getLastModifiedBy().isEmpty()) {
                        config.setLastModifiedBy(System.getProperty("user.name"));

                }

                if (config.getLastModifiedDate().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getLastModifiedDate().isEmpty()) {
                        config.setLastModifiedDate(Timestamps.toString(
                                        Timestamps.fromMillis(System.currentTimeMillis())));

                }

                return config;
        }
}
