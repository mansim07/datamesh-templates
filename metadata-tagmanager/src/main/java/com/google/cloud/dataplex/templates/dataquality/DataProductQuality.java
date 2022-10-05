package com.google.cloud.dataplex.templates.dataquality;

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
import com.google.cloud.dataplex.templates.dataquality.config.DataProductQualityConfig;
import com.google.cloud.dataplex.utils.InputArgsParse;
import com.google.cloud.dataplex.utils.TagOperations;
import com.google.cloud.dataplex.v1.Entity;
import com.google.cloud.dataplex.v1.MetadataServiceClient;
import com.google.protobuf.util.Timestamps;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataProductQuality {


        private static final Logger LOGGER = LoggerFactory.getLogger(DataProductQuality.class);


        public static void main(String... args) throws IOException, ParseException {

                LOGGER.info("Started the Data quality metadata tagging process...");
                Entry entry;

                // SparkSession spark = SparkSession
                // .builder()
                // .appName("JavaPageRank")
                // .config("spark.master","local")
                // .getOrCreate();

                CommandLine cmd = InputArgsParse.parseArguments(args);

                File file = new File(cmd.getOptionValue(INPUT_FILE_OPT));
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                DataProductQualityConfig config =
                                objectMapper.readValue(file, DataProductQualityConfig.class);


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

                                // if ("BIGQUERY".equals(entity.getSystem().name())) {
                                if (1 == 1) {

                                        
                                           entry = dataCatalogClient.lookupEntry(LookupEntryRequest
                                          .newBuilder() .setLinkedResource(String.format("%s/%s",
                                          API_URI_BQ, entity.getDataPath())) .build());
                                         

                                       /*  entry = dataCatalogClient.lookupEntry(LookupEntryRequest
                                                        .newBuilder()
                                                        .setFullyQualifiedName("dataplex:"
                                                                        + dataplex_entity_name_fqdn)
                                                        .build()); */
                                        if (!config.getDqReportConfig().getProjectId().isEmpty()
                                                        && !config.getDqReportConfig()
                                                                        .getDatasetId().isEmpty()
                                                        && !config.getDqReportConfig().getTableId()
                                                                        .isEmpty()) {
                                                LOGGER.info("Fetching the Results from the DQ table...");
                                                LOGGER.info("Data path for the entity is  {}",
                                                                entity.getDataPath());
                                                String data_path = "";
                                                // projects/bankofmars-retail-customers/datasets/data_products_basetables/tables/encrypted_first_party_data_v1_1

                                                if ("CLOUD_STORAGE".equals(
                                                                entity.getSystem().name())) {
                                                        data_path = String.format(
                                                                        "projects/%s/datasets/%s/tables/%s",
                                                                        cmd.getOptionValue(
                                                                                        PROJECT_NAME_OPT),
                                                                        cmd.getOptionValue(
                                                                                        ZONE_ID_OPT)
                                                                                        .replace('-', '_'),
                                                                        entity.getId());
                                                } else {
                                                        data_path = entity.getDataPath();
                                                }


                                                FetchBqDqResults dqResults = FetchBqDqResults
                                                                .getResults(data_path, config);

                                                config = resetValues(config, dqResults);


                                        }



                                        /* set the Tag fields */

                                        /* Fetch the tag name using the tag template */

                                        Map<String, TagField> values = new HashMap<>();

                                        values.put("data_quality_score",
                                                        TagField.newBuilder().setDoubleValue(
                                                                Double.parseDouble(config.getQualityScore()))
                                                                        .build());

                                        values.put("timeliness_score",
                                                        TagField.newBuilder().setDoubleValue(
                                                                Double.parseDouble(config.getTimelinessScore()))
                                                                        .build());
                                        values.put("correctness_score",
                                                        TagField.newBuilder().setDoubleValue(Double.parseDouble(config
                                                                        .getCorrectnessScore()))
                                                                        .build());
                                        values.put("integrity_score",
                                                        TagField.newBuilder().setDoubleValue(
                                                                Double.parseDouble(config.getIntegrityScore()))
                                                                        .build());
                                        values.put("conformity_score",
                                                        TagField.newBuilder().setDoubleValue(
                                                                Double.parseDouble(config.getConformityScore()))
                                                                        .build());
                                        values.put("completeness_score",
                                                        TagField.newBuilder().setDoubleValue(Double.parseDouble(config
                                                                        .getCompletenessScore()))
                                                                        .build());
                                        values.put("uniqueness_score",
                                                        TagField.newBuilder().setDoubleValue(
                                                                Double.parseDouble(config.getUniquenessScore()))
                                                                        .build());
                                        values.put("accuracy_score",
                                                        TagField.newBuilder().setDoubleValue(
                                                                Double.parseDouble(config.getAccuracyScore()))
                                                                        .build());
                                        values.put("dq_dashboard",
                                                        TagField.newBuilder().setRichtextValue(
                                                                        config.getDqDashboard())
                                                                        .build());

                                        /*
                                         * values.put("last_profiling_date", TagField.newBuilder()
                                         * .setTimestampValue(Timestamps.parse(config
                                         * .getLastProfilingDate())) .build());
                                         */

                                        values.put("last_profiling_date",
                                                        TagField.newBuilder().setStringValue(config
                                                                        .getLastProfilingDate())
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
                                                        values, "Data Product Quality Tag");

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

        public static DataProductQualityConfig resetValues(DataProductQualityConfig config,
                        FetchBqDqResults dqResults) {

                if (config.getTimelinessScore().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getTimelinessScore().isEmpty()) {
                        config.setTimelinessScore(dqResults.getPercentageTimeliness());

                }

                if (config.getQualityScore().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getQualityScore().isEmpty()) {
                        config.setQualityScore(dqResults.getQualityScore());

                }

                if (config.getCorrectnessScore().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getCorrectnessScore().isEmpty()) {
                        config.setCorrectnessScore(dqResults.getPercentageCorrectness());

                }

                if (config.getIntegrityScore().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getIntegrityScore().isEmpty()) {
                        config.setIntegrityScore(dqResults.getPercentageIntegrity());

                }

                if (config.getConformityScore().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getConformityScore().isEmpty()) {
                        config.setConformityScore(dqResults.getPercentageConformity());

                }

                if (config.getCompletenessScore().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getCompletenessScore().isEmpty()) {
                        config.setCompletenessScore(dqResults.getPercentageCompleteness());

                }

                if (config.getUniquenessScore().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getUniquenessScore().isEmpty()) {
                        config.setUniquenessScore(dqResults.getPercentageUniqueness());

                }

                if (config.getAccuracyScore().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getAccuracyScore().isEmpty()) {
                        config.setAccuracyScore(dqResults.getPercentageAccuracy());

                }

                if (config.getLastProfilingDate().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getLastProfilingDate().isEmpty()) {
                        config.setLastProfilingDate(dqResults.getExecTs());

                }
                if (config.getRelatedDataProducts().equalsIgnoreCase(DERIVE_INDICATOR)
                                || config.getRelatedDataProducts().isEmpty()) {
                        config.setRelatedDataProducts(
                                        "<a href=\"https://console.cloud.google.com/bigquery?p="
                                                        + config.getDqReportConfig().getProjectId()
                                                        + "&d="
                                                        + config.getDqReportConfig().getDatasetId()
                                                        + "&page=dataset\">Data product</a>");

                }

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
