package com.google.cloud.dataplex.templates.datapublication;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.cloud.datacatalog.v1.CreateTagRequest;
import com.google.cloud.datacatalog.v1.DataCatalogClient;
import com.google.cloud.datacatalog.v1.DataCatalogClient.ListTagsPagedResponse;
import com.google.cloud.datacatalog.v1.Entry;
import com.google.cloud.datacatalog.v1.LookupEntryRequest;
import com.google.cloud.datacatalog.v1.Tag;
import com.google.cloud.datacatalog.v1.TagField;
import com.google.cloud.datacatalog.v1.UpdateTagRequest;
import com.google.cloud.dataplex.templates.datapublication.config.DataProductPublicationConfig;
import com.google.cloud.dataplex.v1.DataplexServiceClient;
import com.google.cloud.dataplex.v1.Entity;
import com.google.cloud.dataplex.v1.Lake;
import com.google.cloud.dataplex.v1.MetadataServiceClient;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.cloud.dataplex.utils.InputArgsParse;
import static com.google.cloud.dataplex.utils.Constants.TAG_TEMPLATE_ID_OPT; 
import static com.google.cloud.dataplex.utils.Constants.PROJECT_NAME_OPT; 
import static com.google.cloud.dataplex.utils.Constants.LOCATION_OPT; 
import static com.google.cloud.dataplex.utils.Constants.LAKE_ID_OPT; 
import static com.google.cloud.dataplex.utils.Constants.ZONE_ID_OPT; 
import static com.google.cloud.dataplex.utils.Constants.ENTITY_ID_OPT; 
import static com.google.cloud.dataplex.utils.Constants.INPUT_FILE_OPT; 


public class DataProductPublicationInfo {
    private static ListTagsPagedResponse listTags;

    // private static Properties properties;
    private static final Logger LOGGER = LoggerFactory.getLogger(DataProductPublicationInfo.class);


    public static void main(String... args) throws IOException {

        MetadataServiceClient mdplx = MetadataServiceClient.create();
        String linkedResource = "";
        String dataplex_entity_name = "";
        String dataplex_lake ="";
        String dataplex_entity_name_fqdn ="";
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Instant time = Instant.now();
        Instant hosttime
            = Instant.parse("2022-01-30T19:34:50.63Z");
        com.google.protobuf.Timestamp timestamp = com.google.protobuf.Timestamp.newBuilder().setSeconds(time.getEpochSecond()).setNanos(time.getNano()).build();
        com.google.protobuf.Timestamp hosttime_ts = com.google.protobuf.Timestamp.newBuilder().setSeconds(hosttime.getEpochSecond()).setNanos(hosttime.getNano()).build();
       

        LOGGER.info("Started the metadata tagging process");
        System.out.println("******************************Environment Vars*****************************");
        Map<String, String> enviorntmentVars  = System.getenv();
        enviorntmentVars.entrySet().forEach(System.out::println);
        // SparkSession spark = SparkSession
        // .builder()
        // .appName("JavaPageRank")
        // .config("spark.master","local")
        // .getOrCreate();
        CommandLine cmd = InputArgsParse.parseArguments(args);

        String tagTemplateId = cmd.getOptionValue(TAG_TEMPLATE_ID_OPT);
        String projectId = cmd.getOptionValue(PROJECT_NAME_OPT);
        String location = cmd.getOptionValue(LOCATION_OPT);
        String lakeId = cmd.getOptionValue(LAKE_ID_OPT);
        String zoneId = cmd.getOptionValue(ZONE_ID_OPT);
        String entityId = cmd.getOptionValue(ENTITY_ID_OPT);
        String inputFile = cmd.getOptionValue(INPUT_FILE_OPT);

        dataplex_entity_name = String.format(
                "projects/%s/locations/%s/lakes/%s/zones/%s/entities/%s",
                projectId, location, lakeId, zoneId, entityId);
        
                dataplex_entity_name_fqdn = String.format(
                        "%s.%s.%s.%s.%s",
                        projectId, location, lakeId, zoneId, entityId);

        dataplex_lake = String.format(
                        "projects/%s/locations/%s/lakes/%s",
                        projectId, location, lakeId);
        
        
        DataplexServiceClient sdplx = DataplexServiceClient.create(); 

        Lake lake = sdplx.getLake(dataplex_lake);


        Entity entity = mdplx.getEntity(dataplex_entity_name);
        try (DataCatalogClient dataCatalogClient = DataCatalogClient.create()) {
            if (entity.getSystem().name() == "BIGQUERY") {
                linkedResource = String.format(
                        "//bigquery.googleapis.com/%s",
                        entity.getDataPath());
            }
            LookupEntryRequest lookupEntryRequest = LookupEntryRequest.newBuilder().setLinkedResource(linkedResource)
                    .build();

            //LookupEntryRequest lookupEntryRequest = LookupEntryRequest.newBuilder()
            //                            .setFullyQualifiedName("dataplex:" + dataplex_entity_name_fqdn).build();

            Entry tableEntry = dataCatalogClient.lookupEntry(lookupEntryRequest);

            /* Parse the input data */
            //Path path = Paths.get("gs://data-catalog-demo/ProductInfo.yaml"); 
            //byte[] bytes = Files.readAllBytes(path);
            //File file = new File("DataPublicationInfo.yaml");
            File file = new File(inputFile);
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            DataProductPublicationConfig config = objectMapper.readValue(file, DataProductPublicationConfig.class);

            System.out.println("------value is ----" + config.getDataExchangePlatform());

            /* set the Tag fields */
            
            /*Fetch the tag name using the tag template*/
            Tag persisted_tag = null;
            for (Tag element : dataCatalogClient.listTags(tableEntry.getName()).iterateAll()) {

                if (new String(element.getTemplate().trim()).equals(new String(tagTemplateId.trim()))) {
                    persisted_tag = element;
                }
            }

            Map<String,TagField> values = new HashMap<String,TagField>(); 
            values.put("data_exchange_platform", TagField.newBuilder().setStringValue(config.getDataExchangePlatform()).build());
            values.put("data_exchange_url", TagField.newBuilder().setRichtextValue(config.getDataExchangeUrl()).build());
            values.put ("access_instructions", TagField.newBuilder().setRichtextValue(config.getAccessInstruction()).build());
            values.put ("host_date", TagField.newBuilder().setTimestampValue(hosttime_ts).build());
            values.put("last_modified_by", TagField.newBuilder().setStringValue(System.getProperty("user.name")).build()); 
            values.put("last_modified_date", TagField.newBuilder().setTimestampValue(timestamp).build());

            if (persisted_tag == null) {
                // New tag
                Tag tag = Tag.newBuilder().setTemplate(tagTemplateId).setName("Data Publication Info").putAllFields(values)
                        .build();
                CreateTagRequest createTagRequest = CreateTagRequest.newBuilder().setParent(tableEntry.getName())
                        .setTag(tag).build();

                dataCatalogClient.createTag(createTagRequest);
            } else {
                //update existing tag
                Tag tag = Tag.newBuilder().setTemplate(tagTemplateId).setName(persisted_tag.getName()).putAllFields(values)
                        .build();

                UpdateTagRequest updateTagRequest = UpdateTagRequest.newBuilder().setTag(tag).build();

                dataCatalogClient.updateTag(updateTagRequest);
            }

        }
        ;


        // spark.stop();
    }
}
