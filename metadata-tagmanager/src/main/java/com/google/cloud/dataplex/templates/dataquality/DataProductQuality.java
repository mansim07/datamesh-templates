package com.google.cloud.dataplex.templates.dataquality;

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
import com.google.cloud.dataplex.templates.dataquality.config.DataProductQualityConfig;
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


public class DataProductQuality {


    private static final Logger LOGGER = LoggerFactory.getLogger(DataProductQuality.class);


    public static void main(String... args) throws IOException {

        try (MetadataServiceClient mdplx = MetadataServiceClient.create()) {
            String linkedResource = "";
            String dataplex_entity_name = "";
            String dataplex_lake ="";
            String dataplex_entity_name_fqdn ="";
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Instant time = Instant.now();
            com.google.protobuf.Timestamp timestamp = com.google.protobuf.Timestamp.newBuilder().setSeconds(time.getEpochSecond()).setNanos(time.getNano()).build();
            LOGGER.info("Started the metadata tagging process");

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
            
            
            try (DataplexServiceClient sdplx = DataplexServiceClient.create()) {
                Lake lake = sdplx.getLake(dataplex_lake);
            } 

            Entity entity = mdplx.getEntity(dataplex_entity_name);

            try (DataCatalogClient dataCatalogClient = DataCatalogClient.create()) {
                if (entity.getSystem().name() == "BIGQUERY") {
                    linkedResource = String.format(
                            "//bigquery.googleapis.com/%s",
                            entity.getDataPath());
                }
                LookupEntryRequest lookupEntryRequest = LookupEntryRequest.newBuilder().setLinkedResource(linkedResource)
                        .build();

                      //  LookupEntryRequest lookupEntryRequest = LookupEntryRequest.newBuilder()
                      //  .setFullyQualifiedName("dataplex:" + dataplex_entity_name_fqdn).build();

                Entry tableEntry = dataCatalogClient.lookupEntry(lookupEntryRequest);

                System.out.println("Testing Column Level Tags ---------------------:::" + tableEntry.getSchema().getColumnsList());

                /* Parse the input data */
                //Path path = Paths.get("gs://data-catalog-demo/ProductInfo.yaml"); 
                //byte[] bytes = Files.readAllBytes(path);
                //File file = new File("DataQuality.yaml");
                File file = new File(inputFile);
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                DataProductQualityConfig config = objectMapper.readValue(file, DataProductQualityConfig.class);

                /* set the Tag fields */
                
                /*Fetch the tag name using the tag template*/
                Tag persisted_tag = null;
                for (Tag element : dataCatalogClient.listTags(tableEntry.getName()).iterateAll()) {

                    if (new String(element.getTemplate().trim()).equals(new String(tagTemplateId.trim()))) {
                        persisted_tag = element;
                    }
                }

                Map<String,TagField> values = new HashMap<String,TagField>(); 
                values.put("percentage_timeliness", TagField.newBuilder().setStringValue(config.getTimelinessScore()).build());
                values.put("percentage_correctness", TagField.newBuilder().setStringValue(config.getCorrectnessScore()).build());
                values.put ("percentage_integrity", TagField.newBuilder().setStringValue(config.getIntegrityScore()).build());
                values.put ("percentage_conformity", TagField.newBuilder().setStringValue(config.getConformityScore()).build());
                values.put("percentage_completeness", TagField.newBuilder().setStringValue(config.getCompletenessScore()).build());
                values.put("percentage_uniqueness", TagField.newBuilder().setStringValue(config.getUniquenessScore()).build());
                values.put ("percentage_accuracy",  TagField.newBuilder().setStringValue(config.getAccuracyScore()).build());
                values.put("related_data_products", TagField.newBuilder().setRichtextValue(config.getRelatedDataProducts()).build());
                values.put("last_modified_by", TagField.newBuilder().setStringValue(System.getProperty("user.name")).build()); 
                values.put("last_modified_date", TagField.newBuilder().setTimestampValue(timestamp).build());

                if (persisted_tag == null) {
                    // New tag
                    Tag tag = Tag.newBuilder().setTemplate(tagTemplateId).setName("Data Quality Info").putAllFields(values)
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
        }
        


        // spark.stop();
    }
}
