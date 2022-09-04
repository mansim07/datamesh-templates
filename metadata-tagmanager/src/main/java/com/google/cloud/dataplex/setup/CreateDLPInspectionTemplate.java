package com.google.cloud.dataplex.setup;
import com.google.cloud.dlp.v2.DlpServiceClient;
import com.google.privacy.dlp.v2.CreateInspectTemplateRequest;
import com.google.privacy.dlp.v2.InfoType;
import com.google.privacy.dlp.v2.InspectConfig;
import com.google.privacy.dlp.v2.InspectTemplate;
import com.google.privacy.dlp.v2.LocationName;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateDLPInspectionTemplate {
    public static void main(String[] args) throws Exception {
        // TODO(developer): Replace these variables before running the sample.
        String projectId = args[0];
        String location = args[1];
        String templateId = args[2];
        //String projectId = "your-project-id";
        createInspectTemplate(projectId,location,templateId);
      }

    
      // Creates a template to persist configuration information
      public static void createInspectTemplate(String projectId, String location, String templateId) throws IOException {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (DlpServiceClient dlpServiceClient = DlpServiceClient.create()) {
          // Specify the type of info the inspection will look for.
          // See https://cloud.google.com/dlp/docs/infotypes-reference for complete list of info types
          List<InfoType> infoTypes =
              Stream.of("AGE","AUTH_TOKEN","AWS_CREDENTIALS","AZURE_AUTH_TOKEN","CREDIT_CARD_NUMBER","CREDIT_CARD_TRACK_NUMBER","DATE_OF_BIRTH","DOCUMENT_TYPE/FINANCE/REGULATORY","DOCUMENT_TYPE/FINANCE/SEC_FILING","EMAIL_ADDRESS","ENCRYPTION_KEY","ETHNIC_GROUP","GCP_API_KEY","GCP_CREDENTIALS","GENDER","HTTP_COOKIE","IBAN_CODE","IMEI_HARDWARE_ID","IP_ADDRESS","JSON_WEB_TOKEN","LAST_NAME","MAC_ADDRESS","PASSPORT","PASSWORD","PERSON_NAME","PHONE_NUMBER","STORAGE_SIGNED_URL","STREET_ADDRESS","US_BANK_ROUTING_MICR","US_DEA_NUMBER","US_DRIVERS_LICENSE_NUMBER","US_EMPLOYER_IDENTIFICATION_NUMBER","US_INDIVIDUAL_TAXPAYER_IDENTIFICATION_NUMBER","US_SOCIAL_SECURITY_NUMBER","WEAK_PASSWORD_HASH","VEHICLE_IDENTIFICATION_NUMBER","XSRF_TOKEN","US_STATE","US_TOLLFREE_PHONE_NUMBER","US_VEHICLE_IDENTIFICATION_NUMBER")
                  .map(it -> InfoType.newBuilder().setName(it).build())
                  .collect(Collectors.toList());
    
          // Construct the inspection configuration for the template
          InspectConfig inspectConfig = InspectConfig.newBuilder().addAllInfoTypes(infoTypes).build();
    
          // Optionally set a display name and a description for the template
          String displayName = "Inspection Config Template";
          String description = "Save configuration for future inspection jobs";
    
          // Build the template
          InspectTemplate inspectTemplate =
              InspectTemplate.newBuilder()
                  .setName(templateId)
                  .setInspectConfig(inspectConfig)
                  .setDisplayName(displayName)
                  .setDescription(description)
                  .build();
    
          // Create the request to be sent by the client
          CreateInspectTemplateRequest createInspectTemplateRequest =
              CreateInspectTemplateRequest.newBuilder()
                  //.setParent(LocationName.of(projectId, "global").toString())
                  .setParent(LocationName.of(projectId, location).toString())
                  .setInspectTemplate(inspectTemplate)
                  .setTemplateId(templateId)
                  .build();
    
          // Send the request to the API and process the response
          InspectTemplate response =
              dlpServiceClient.createInspectTemplate(createInspectTemplateRequest);

         
          System.out.printf("Template created: %s", response.getName());
        }
      }
    
}
