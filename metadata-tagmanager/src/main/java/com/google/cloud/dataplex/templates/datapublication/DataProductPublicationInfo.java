package com.google.cloud.dataplex.templates.datapublication;


import static com.google.cloud.dataplex.utils.Constants.API_URI_BQ;
import static com.google.cloud.dataplex.utils.Constants.DOC_URL_PREFIX;
import static com.google.cloud.dataplex.utils.Constants.DOMAIN_ORG;
import static com.google.cloud.dataplex.utils.Constants.ENTITY_ID_OPT;
import static com.google.cloud.dataplex.utils.Constants.ICON_URL_PREFIX;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.cloud.datacatalog.v1.DataCatalogClient;
import com.google.cloud.datacatalog.v1.Entry;
import com.google.cloud.datacatalog.v1.LookupEntryRequest;
import com.google.cloud.datacatalog.v1.TagField;
import com.google.cloud.dataplex.templates.datapublication.config.DataProductPublicationConfig;
import com.google.cloud.dataplex.utils.InputArgsParse;
import com.google.cloud.dataplex.utils.TagOperations;
import com.google.cloud.dataplex.v1.DataplexServiceClient;
import com.google.cloud.dataplex.v1.Entity;
import com.google.cloud.dataplex.v1.Lake;
import com.google.cloud.dataplex.v1.MetadataServiceClient;
import com.google.cloud.dataplex.v1.Zone;
import com.google.protobuf.util.Timestamps;

import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataProductPublicationInfo {
        private static final Logger LOGGER =
                        LoggerFactory.getLogger(DataProductPublicationInfo.class);


        public static void main(String... args) throws IOException {

                LOGGER.info("Started the Data Product Exchange metadata tagging process...");
                Entry entry;
                CommandLine cmd = InputArgsParse.parseArguments(args);

                File file = new File(cmd.getOptionValue(INPUT_FILE_OPT));
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                DataProductPublicationConfig config =
                                objectMapper.readValue(file, DataProductPublicationConfig.class);

                try (MetadataServiceClient mdplx = MetadataServiceClient.create()) {
                        try (DataplexServiceClient sdplx = DataplexServiceClient.create()) {
                                Entity entity = mdplx.getEntity(String.format(
                                                "projects/%s/locations/%s/lakes/%s/zones/%s/entities/%s",
                                                cmd.getOptionValue(PROJECT_NAME_OPT),
                                                cmd.getOptionValue(LOCATION_OPT),
                                                cmd.getOptionValue(LAKE_ID_OPT),
                                                cmd.getOptionValue(ZONE_ID_OPT),
                                                cmd.getOptionValue(ENTITY_ID_OPT)));
                                Lake lake = sdplx.getLake(
                                                String.format("projects/%s/locations/%s/lakes/%s",
                                                                cmd.getOptionValue(
                                                                                PROJECT_NAME_OPT),
                                                                cmd.getOptionValue(LOCATION_OPT),
                                                                cmd.getOptionValue(LAKE_ID_OPT)));
                                Zone zone = sdplx.getZone(String.format(
                                                "projects/%s/locations/%s/lakes/%s/zones/%s",
                                                cmd.getOptionValue(PROJECT_NAME_OPT),
                                                cmd.getOptionValue(LOCATION_OPT),
                                                cmd.getOptionValue(LAKE_ID_OPT),
                                                cmd.getOptionValue(ZONE_ID_OPT)));

                                String dataplex_entity_name_fqdn = String.format("%s.%s.%s.%s.%s",
                                                cmd.getOptionValue(PROJECT_NAME_OPT),
                                                cmd.getOptionValue(LOCATION_OPT),
                                                cmd.getOptionValue(LAKE_ID_OPT),
                                                cmd.getOptionValue(ZONE_ID_OPT),
                                                cmd.getOptionValue(ENTITY_ID_OPT));


                                try (DataCatalogClient dataCatalogClient =
                                                DataCatalogClient.create()) {
                                        if (1 == 1) {

                                              entry = dataCatalogClient.lookupEntry(LookupEntryRequest
                                          .newBuilder() .setLinkedResource(String.format("%s/%s",
                                          API_URI_BQ, entity.getDataPath())) .build()); 
                                                /* 
                                                entry = dataCatalogClient.lookupEntry(
                                                                LookupEntryRequest.newBuilder()
                                                                                .setFullyQualifiedName(
                                                                                                "dataplex:" + dataplex_entity_name_fqdn)
                                                                                .build()); */

                                                if (config.getPublishDate()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getPublishDate()
                                                                                .isEmpty()) {
                                                        config.setPublishDate(Timestamps.toString(
                                                                        Timestamps.fromMillis(System
                                                                                        .currentTimeMillis())));

                                                }

                                                if (config.getLastModifiedDate()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getLastModifiedDate()
                                                                                .isEmpty()) {
                                                        config.setLastModifiedDate(Timestamps
                                                                        .toString(Timestamps
                                                                                        .fromMillis(System
                                                                                                        .currentTimeMillis())));

                                                }
                                                if (config.getLastModifiedBy()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getLastModifiedBy()
                                                                                .isEmpty()) {
                                                        config.setLastModifiedBy(System
                                                                        .getProperty("user.name"));

                                                }

                                                if (config.getDataExchangePlatform()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getLastModifiedBy()
                                                                                .isEmpty()) {
                                                        config.setDataExchangePlatform(
                                                                        "Analytics Hub");;

                                                }

                                                if (config.getDataExchangeUrl()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getLastModifiedBy()
                                                                                .isEmpty()) {
                                                        config.setDataExchangePlatform(
                                                                        "Oops! Exchange URL is not available");

                                                }
                                                if (config.getAccessInstruction()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getLastModifiedBy()
                                                                                .isEmpty()) {
                                                        config.setAccessInstruction(
                                                                        "Oops! Access instruction is not available");

                                                }


                                                Map<String, TagField> values =
                                                                new HashMap<String, TagField>();
                                                values.put("data_exchange_platform", TagField
                                                                .newBuilder()
                                                                .setStringValue(config
                                                                                .getDataExchangePlatform())
                                                                .build());
                                                values.put("data_exchange_url", TagField
                                                                .newBuilder()
                                                                .setRichtextValue(config
                                                                                .getDataExchangeUrl())
                                                                .build());
                                                values.put("access_instructions", TagField
                                                                .newBuilder()
                                                                .setRichtextValue(config
                                                                                .getAccessInstruction())
                                                                .build());
                                                values.put("publish_date", TagField.newBuilder()
                                                                .setTimestampValue(Timestamps.parse(
                                                                                config.getPublishDate()))
                                                                .build());
                                                values.put("last_modified_by", TagField.newBuilder()
                                                                .setStringValue(config
                                                                                .getLastModifiedBy())
                                                                .build());
                                                values.put("last_modified_date", TagField
                                                                .newBuilder()
                                                                .setTimestampValue(Timestamps.parse(
                                                                                config.getLastModifiedDate()))
                                                                .build());

                                                TagOperations.publishTag(entry, dataCatalogClient,
                                                                cmd.getOptionValue(
                                                                                TAG_TEMPLATE_ID_OPT),
                                                                values,
                                                                "Data Product Exchange Tag");

                                                LOGGER.info("Tag was successfully created for entry {} using tag template {}",
                                                                entry.getFullyQualifiedName(),
                                                                cmd.getOptionValue(
                                                                                TAG_TEMPLATE_ID_OPT));

                                        } else {
                                                LOGGER.error("Currently Data Product Info  tagging is only supoprted for BigQuery Tables");
                                                throw new UnsupportedOperationException(
                                                                "Currently Data Product Info tagging is only supoprted for BigQuery Tables");
                                        }

                                } catch (ParseException e) {
                                        e.printStackTrace();
                                }
                        }
                }

                // spark.stop();

        }
}
