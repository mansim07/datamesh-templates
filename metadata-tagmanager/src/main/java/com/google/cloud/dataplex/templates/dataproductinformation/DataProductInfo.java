
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

package com.google.cloud.dataplex.templates.dataproductinformation;

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
import com.google.cloud.dataplex.templates.dataproductinformation.config.DataProductInfoConfig;
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

// Move versioning info to a new tag
// copy restruction move it to data governance tag
public class DataProductInfo {
        private static final Logger LOGGER = LoggerFactory.getLogger(DataProductInfo.class);

        public static void main(String[] args) throws IOException {

                LOGGER.info("Started the Data Product information  metadata tagging process...");
                Entry entry;
                CommandLine cmd = InputArgsParse.parseArguments(args);

                File file = new File(cmd.getOptionValue(INPUT_FILE_OPT));
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                DataProductInfoConfig config =
                                objectMapper.readValue(file, DataProductInfoConfig.class);

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
                                        // extending support to GCS Storage
                                        // if ("BIGQUERY".equals(entity.getSystem().name())) {
                                        if (1 == 1) {

                                                /* 
                                                 * Use this if tagging needs to be created at the
                                                 * actualy data object level */

                                                  entry =
                                                  dataCatalogClient.lookupEntry(
                                                  LookupEntryRequest.newBuilder()
                                                  .setLinkedResource( String.format("%s/%s",
                                                  API_URI_BQ, entity.getDataPath())) .build()); 
                                                 /* 

                                                 entry = dataCatalogClient.lookupEntry(
                                                                LookupEntryRequest.newBuilder()
                                                                                .setFullyQualifiedName(
                                                                                                "dataplex:" + dataplex_entity_name_fqdn)
                                                                                .build()); */

                                                if (config.getDataProductId()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getDataProductId()
                                                                                .isEmpty()) {
                                                        config.setDataProductId("dku-" + Math.abs(
                                                                        entity.getName().hashCode()));

                                                }

                                                if (config.getDataProductName()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getDataProductName()
                                                                                .isEmpty()) {
                                                        config.setDataProductName(
                                                                        "" + entity.getId());

                                                }

                                                if (config.getDataProductType()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getDataProductType()
                                                                                .isEmpty()) {
                                                        config.setDataProductType(
                                                                        entity.getSystem().name());

                                                }

                                                if (config.getDomain()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getDomain().isEmpty()) {
                                                        config.setDomain(lake.getDisplayName());

                                                }

                                                if (config.getDataProductDescription()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getDataProductDescription()
                                                                                .isEmpty()) {
                                                        if (!entity.getDescription().isEmpty()) {
                                                                config.setDataProductDescription(
                                                                                entity.getDescription());
                                                        } else {
                                                                config.setDataProductDescription(
                                                                                "Description for the product is currently not available.");
                                                        }



                                                }

                                                if (config.getDataProductIcon()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getDataProductIcon()
                                                                                .isEmpty()) {
                                                        config.setDataProductIcon("<a href=\""
                                                                        + ICON_URL_PREFIX
                                                                        + entity.getName() + ".jpg"
                                                                        + "\">Icon</a> ");

                                                }
                                                // Default assumes it an application data
                                                // This can be derived from Labels if derived when
                                                // defining lakes or domains
                                                // current can derive from Zone labels
                                                if (config.getDataProductCategory()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getDataProductCategory()
                                                                                .isEmpty()) {
                                                        String dpc = zone.getLabelsOrDefault(
                                                                        "data_product_category",
                                                                        "application_data");
                                                        if (dpc.contains("master")) {
                                                                config.setDataProductCategory(
                                                                                "Master Data");
                                                        } else if (dpc.contains("application")) {
                                                                config.setDataProductCategory(
                                                                                "Application Data");
                                                        } else if (dpc.contains("system")) {
                                                                config.setDataProductCategory(
                                                                                "System Data");
                                                        } else if (dpc.contains("reference")) {
                                                                config.setDataProductCategory(
                                                                                "Reference Data");
                                                        }

                                                }

                                                if (config.getGeoRegion()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getGeoRegion()
                                                                                .isEmpty()) {
                                                        // To Do: Move this to a reusable module
                                                        Pattern p = Pattern.compile(
                                                                        "^projects\\/(\\w+.*)\\/locations\\/(\\w+.*)\\/entryGroups\\/(.*)");
                                                        Matcher m = p.matcher(entry.getName());
                                                        m.matches();
                                                        config.setGeoRegion(m.group(2));

                                                }
                                                // To Do: To be replaced with getIamPolicy results.
                                                // Currently not available
                                                if (config.getDomainOwner()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getDomainOwner()
                                                                                .isEmpty()) {
                                                        config.setDomainOwner((lake.getName()
                                                                        .substring(lake.getName()
                                                                                        .lastIndexOf("/")
                                                                                        + 1))
                                                                        + "_domainowner@"
                                                                        + DOMAIN_ORG);

                                                }

                                                // To Do: To be replaced with getIamPolicy results.
                                                // Currently not available
                                                if (config.getDataProductOwner()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getDataProductOwner()
                                                                                .isEmpty()) {
                                                        config.setDataProductOwner(lake.getName()
                                                                        .substring(lake.getName()
                                                                                        .lastIndexOf("/")
                                                                                        + 1)
                                                                        + entity.getAsset()
                                                                        + "_dataproductowner@"
                                                                        + DOMAIN_ORG);

                                                }

                                                if (config.getDataProductDocumentation()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getDataProductDocumentation()
                                                                                .isEmpty()) {
                                                        config.setDataProductDocumentation(
                                                                        "<a href=\"" + DOC_URL_PREFIX
                                                                                        + lake.getName()
                                                                                        + "/"
                                                                                        + zone.getName()
                                                                                        + "/"
                                                                                        + entity.getName()
                                                                                        + "\">Product Documentation</a>");

                                                }

                                                if (config.getDomainType()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getDomainType()
                                                                                .isEmpty()) {
                                                        String dt = lake.getLabelsOrDefault(
                                                                        "domain_type", "source");
                                                        if (dt.contains("source")) {
                                                                config.setDomainType("Source");
                                                        } else if (dt.contains("consumer")) {
                                                                config.setDomainType("Consumer");

                                                        }

                                                }

                                                if (config.getLastModifiedBy()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getLastModifiedBy()
                                                                                .isEmpty()) {
                                                        config.setLastModifiedBy(System
                                                                        .getProperty("user.name"));

                                                }

                                                if (config.getlastModifiedDate()
                                                                .equalsIgnoreCase(DERIVE_INDICATOR)
                                                                || config.getlastModifiedDate()
                                                                                .isEmpty()) {
                                                        config.setLastModifiedDate(Timestamps
                                                                        .toString(Timestamps
                                                                                        .fromMillis(System
                                                                                                        .currentTimeMillis())));

                                                }

                                                Map<String, TagField> values = new HashMap<>();
                                                values.put("data_product_id", TagField.newBuilder()
                                                                .setStringValue(config
                                                                                .getDataProductId())
                                                                .build());
                                                values.put("data_product_name", TagField
                                                                .newBuilder()
                                                                .setStringValue(config
                                                                                .getDataProductName())
                                                                .build());
                                                values.put("data_product_type", TagField
                                                                .newBuilder()
                                                                .setEnumValue(TagField.EnumValue
                                                                                .newBuilder()
                                                                                .setDisplayName(config
                                                                                                .getDataProductType())
                                                                                .build())
                                                                .build());
                                                values.put("domain", TagField.newBuilder()
                                                                .setStringValue(config.getDomain())
                                                                .build());
                                                /*
                                                 * values.put("domain", TagField.newBuilder()
                                                 * .setEnumValue(TagField.EnumValue .newBuilder()
                                                 * .setDisplayName(config .getDomain()) .build())
                                                 * .build());
                                                 */

                                                values.put("data_product_description", TagField
                                                                .newBuilder()
                                                                .setStringValue(config
                                                                                .getDataProductDescription())
                                                                .build());
                                                values.put("data_product_icon", TagField
                                                                .newBuilder()
                                                                .setRichtextValue(config
                                                                                .getDataProductIcon())
                                                                .build());
                                                values.put("data_product_category", TagField
                                                                .newBuilder()
                                                                .setEnumValue(TagField.EnumValue
                                                                                .newBuilder()
                                                                                .setDisplayName(config
                                                                                                .getDataProductCategory())
                                                                                .build())
                                                                .build());
                                                values.put("data_product_geo_region", TagField
                                                                .newBuilder()
                                                                .setStringValue(config
                                                                                .getGeoRegion())
                                                                .build());
                                                values.put("domain_owner", TagField.newBuilder()
                                                                .setRichtextValue(config
                                                                                .getDomainOwner())
                                                                .build());
                                                values.put("data_product_owner", TagField
                                                                .newBuilder()
                                                                .setRichtextValue(config
                                                                                .getDataProductOwner())
                                                                .build());
                                                values.put("data_product_documentation", TagField
                                                                .newBuilder()
                                                                .setRichtextValue(config
                                                                                .getDataProductDocumentation())
                                                                .build());
                                                values.put("domain_type", TagField.newBuilder()
                                                                .setEnumValue(TagField.EnumValue
                                                                                .newBuilder()
                                                                                .setDisplayName(config
                                                                                                .getDomainType())
                                                                                .build())
                                                                .build());

                                                values.put("last_modified_by", TagField.newBuilder()
                                                                .setStringValue(config
                                                                                .getLastModifiedBy())
                                                                .build());
                                                values.put("last_modified_date", TagField
                                                                .newBuilder()
                                                                .setTimestampValue(Timestamps.parse(
                                                                                config.getlastModifiedDate()))
                                                                .build());

                                                TagOperations.publishTag(entry, dataCatalogClient,
                                                                cmd.getOptionValue(
                                                                                TAG_TEMPLATE_ID_OPT),
                                                                values, "Data Product Info");

                                                LOGGER.info("Tag was successfully created for entry {} using tag template {}",
                                                                entry.getFullyQualifiedName(),
                                                                cmd.getOptionValue(
                                                                                TAG_TEMPLATE_ID_OPT));

                                        }

                                        else {
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
