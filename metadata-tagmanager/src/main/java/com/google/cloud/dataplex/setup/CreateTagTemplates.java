package com.google.cloud.dataplex.setup;


import com.google.api.gax.rpc.PermissionDeniedException;
import com.google.cloud.datacatalog.v1.CreateTagTemplateRequest;
import com.google.cloud.datacatalog.v1.DeleteTagTemplateRequest;
import com.google.cloud.datacatalog.v1.DataCatalogClient;
import com.google.cloud.datacatalog.v1.FieldType;
import com.google.cloud.datacatalog.v1.LocationName;
import com.google.cloud.datacatalog.v1.TagTemplate;
import com.google.cloud.datacatalog.v1.TagTemplateField;
import com.google.cloud.datacatalog.v1.FieldType.EnumType.EnumValue;
import com.google.cloud.datacatalog.v1.FieldType.EnumType.EnumValue.Builder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.cloud.datacatalog.v1.GetTagTemplateRequest;

public class CreateTagTemplates {
        private static final Logger LOGGER = LoggerFactory.getLogger(CreateTagTemplates.class);

        public static void main(String[] args) throws IOException {
                String projectId = args[0];
                String location = args[1];
                String templateName = args[2];
                LocationName locationName = LocationName.of(projectId, location);

                if (templateName.equals(new String("data_product_information"))) {
                        createDataProductInformationTagTemplate(locationName);
                }
                if (templateName.equals(new String("data_product_classification"))) {
                        createDataClassificationTagTemplate(locationName);
                }
                if (templateName.equals(new String("data_product_quality"))) {
                        createDataProductQualityTagTemplate(locationName);
                }
                if (templateName.equals(new String("data_product_exchange"))) {
                        createDataProductExchangeTagTemplate(locationName);
                }



        }
        // Create Data Product Information Tag

        private static void createDataClassificationTagTemplate(LocationName locationName)
                        throws IOException {
                String tagTemplateId = "data_product_classification";

                TagTemplateField is_pii = TagTemplateField.newBuilder().setDisplayName("Is PII")
                                .setIsRequired(true).setOrder(13)
                                .setDescription("Indicator of Personally Identifiable Information(PII) data")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.BOOL)
                                                .build())
                                .build();

                TagTemplateField is_confidential = TagTemplateField.newBuilder()
                                .setDisplayName("Is Confidential").setIsRequired(true).setOrder(12)
                                .setDescription("Indicates if data is confidential")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.BOOL)
                                                .build())
                                .build();

                TagTemplateField is_restricted = TagTemplateField.newBuilder()
                                .setDisplayName("Is Restricted").setIsRequired(true).setOrder(11)
                                .setDescription("Indicates if data is restricted")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.BOOL)
                                                .build())
                                .build();

                TagTemplateField is_public = TagTemplateField.newBuilder()
                                .setDisplayName("Is Public").setIsRequired(true).setOrder(10)
                                .setDescription("Indicates if data is public")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.BOOL)
                                                .build())
                                .build();
                TagTemplateField is_encrypted = TagTemplateField.newBuilder()
                                .setDisplayName("Is Encrypted").setIsRequired(true).setOrder(9)
                                .setDescription("Indicates if data is encrypted")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.BOOL)
                                                .build())
                                .build();
                TagTemplateField encryption_key_type = TagTemplateField.newBuilder()
                                .setDisplayName("Encryption Key types").setIsRequired(true)
                                .setOrder(8).setDescription("Type of encryption key used")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.STRING)
                                                .build())
                                .build();
                TagTemplateField sensitivity_score = TagTemplateField.newBuilder()
                                .setDisplayName("Sensitivity Score").setIsRequired(true).setOrder(7)
                                .setDescription("Sensitivity score is an indication of how sensitive the data in a project, table, or column is. Data is sensitive if it contains detected elements, such as personally identifiable information (PII), financial data, and credentials.")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.STRING)
                                                .build())
                                .build();
                TagTemplateField risk_score = TagTemplateField.newBuilder()
                                .setDisplayName("Risk Score").setIsRequired(true).setOrder(6)
                                .setDescription("Risk score is the risk associated with the data in its current state. It considers the sensitivity level of the data in the resource and the presence of access controls to protect that data.")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.STRING)
                                                .build())
                                .build();
                TagTemplateField info_types = TagTemplateField.newBuilder()
                                .setDisplayName("Info Type").setIsRequired(true).setOrder(5)
                                .setDescription("An infoType is a type of sensitive data, such as a name, email address, telephone number, identification number, or credit card number.")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.STRING)
                                                .build())
                                .build();
                TagTemplateField related_data_products = TagTemplateField.newBuilder()
                                .setDisplayName("Related Data product").setIsRequired(false)
                                .setOrder(4)
                                .setDescription("Any linked data products that can provide additional analytical and reports for the classification")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.RICHTEXT)
                                                .build())
                                .build();

                TagTemplateField last_profiling_date = TagTemplateField.newBuilder()
                                .setDisplayName("Last Profiling Date").setIsRequired(true)
                                .setOrder(3)
                                .setDescription("Date & time when the data was last profilled or scanned for classification")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.TIMESTAMP)
                                                .build())
                                .build();

                TagTemplateField last_modified_by = TagTemplateField.newBuilder()
                                .setDisplayName("Last Modified By").setIsRequired(true).setOrder(2)
                                .setDescription("Individual or service account ldaps reponsible for modifying the tag")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.STRING)
                                                .build())
                                .build();

                TagTemplateField last_modified_date = TagTemplateField.newBuilder()
                                .setDisplayName("Last Modified Date").setIsRequired(true)
                                .setOrder(1).setDescription("when the tag was last modified")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.TIMESTAMP)
                                                .build())
                                .build();
                TagTemplate tagTemplate = TagTemplate.newBuilder()
                                .setDisplayName("Data Product Classification")
                                .setIsPubliclyReadable(true).putFields("is_pii", is_pii)
                                .putFields("is_confidential", is_confidential)
                                .putFields("is_restricted", is_restricted)
                                .putFields("is_public", is_public)
                                .putFields("is_encrypted", is_encrypted)
                                .putFields("encryption_key_type", encryption_key_type)
                                .putFields("sensitivity_score", sensitivity_score)
                                .putFields("risk_score", risk_score)
                                .putFields("info_types", info_types)
                                .putFields("related_data_products", related_data_products)
                                .putFields("last_profiling_date", last_profiling_date)
                                .putFields("last_modified_by", last_modified_by)
                                .putFields("last_modified_date", last_modified_date).build();
                createTagTemplate(locationName, tagTemplateId, tagTemplate);



        }

        public static void createDataProductInformationTagTemplate(LocationName locationName)
                        throws IOException {
                String tagTemplateId = "data_product_information";
                TagTemplateField data_product_id = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product Identifier").setIsRequired(true)
                                .setOrder(14).setDescription("A unique id for the data product")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.STRING)
                                                .build())
                                .build();

                TagTemplateField data_product_name = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product Name").setIsRequired(true)
                                .setOrder(13).setDescription("Name of the data product")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.STRING)
                                                .build())
                                .build();

                TagTemplateField data_product_type = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product System").setIsRequired(true)
                                .setOrder(12).setDescription("Name of the data product System")
                                .setType(FieldType.newBuilder().setEnumType(FieldType.EnumType
                                                .newBuilder()
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("BIGQUERY"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("LOOKER"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("MYSQL"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("POSTGRES"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("APIGEE"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("CLOUD_STORAGE"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("MODEL"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("PUBSUB"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("S3"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("SNOWFLAKE"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("AZURE"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("HIVE"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("TABLEAU"))))
                                .build();

                TagTemplateField data_product_description = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product Description").setIsRequired(true)
                                .setOrder(11)
                                .setDescription("A brief description of the data product")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.STRING)
                                                .build())
                                .build();

                TagTemplateField data_product_icon = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product Icon").setIsRequired(true)
                                .setOrder(10)
                                .setDescription("A URL link to a uniquely image or icon for your Data product")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.RICHTEXT)
                                                .build())
                                .build();

                TagTemplateField data_product_category = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product Category").setIsRequired(true)
                                .setOrder(9)
                                .setDescription("Category of the data product. Allowed Values: 'Master Data', 'Reference Data', 'System Data', 'Application Data'")
                                .setType(FieldType.newBuilder().setEnumType(FieldType.EnumType
                                                .newBuilder()
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("Master Data"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("Reference Data"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("System Data"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("Application Data"))))
                                .build();
                TagTemplateField data_product_geo_region = TagTemplateField.newBuilder()
                                .setDisplayName("Geo Region").setIsRequired(true).setOrder(8)
                                .setDescription("The region the data product is available for consumption")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.STRING)
                                                .build())
                                .build();

                TagTemplateField data_product_owner = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product Owner").setIsRequired(true)
                                .setOrder(7)
                                .setDescription("The Data product owner. This can be the domain owner or another set of individual or group responsible for the product. For Dataplex, this should be set to owner zone/assert")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.RICHTEXT)
                                                .build())
                                .build();

                TagTemplateField data_product_documentation = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product Documentation").setIsRequired(true)
                                .setOrder(6)
                                .setDescription("Product Documentation for reference purposes")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.RICHTEXT)
                                                .build())
                                .build();

                TagTemplateField domain = TagTemplateField.newBuilder()
                                .setDisplayName("Data Domain").setIsRequired(true).setOrder(5)
                                .setDescription("Domain of the data product. An organization should already have a list of data domain. VALUES - Org specific.If using dataplex for Management make sure your Lake's Display Name matches the allowed value list specified here. Predefining this is recommeneded")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.STRING)
                                                .build())
                                .build();

                /*
                 * TagTemplateField domain =
                 * TagTemplateField.newBuilder().setDisplayName("Data Domain")
                 * .setIsRequired(true).setOrder(5) .setDescription(
                 * "Domain of the data product. An organization should already have a list of data domain. VALUES - Org specific.If using dataplex for Management make sure your Lake's Display Name matches the allowed value list specified here. Predefining this is recommeneded"
                 * ) .setType(FieldType.newBuilder().setEnumType(FieldType.EnumType.newBuilder()
                 * .addAllowedValues(EnumValue.newBuilder().setDisplayName("Core - Customers"))
                 * .addAllowedValues(
                 * EnumValue.newBuilder().setDisplayName("Core - Financial Products"))
                 * .addAllowedValues(EnumValue.newBuilder()
                 * .setDisplayName("Consumer Banking - Deposits"))
                 * .addAllowedValues(EnumValue.newBuilder()
                 * .setDisplayName("Consumer Banking - Credit Cards"))
                 * .addAllowedValues(EnumValue.newBuilder()
                 * .setDisplayName("Consumer Banking - Student Loans"))
                 * .addAllowedValues(EnumValue.newBuilder()
                 * .setDisplayName("Consumer Banking - Home Equity Loans"))
                 * .addAllowedValues(EnumValue.newBuilder().setDisplayName("Sales"))
                 * .addAllowedValues(EnumValue.newBuilder()
                 * .setDisplayName("Marketing - Product Development"))
                 * .addAllowedValues(EnumValue.newBuilder()
                 * .setDisplayName("Marketing - Brand Advertising Management"))
                 * .addAllowedValues(EnumValue.newBuilder()
                 * .setDisplayName("Marketing - Rewards Program"))
                 * .addAllowedValues(EnumValue.newBuilder()
                 * .setDisplayName("Marketing - Customer Acquisition"))
                 * .addAllowedValues(EnumValue.newBuilder()
                 * .setDisplayName("Marketing - Portfolio Management"))
                 * .addAllowedValues(EnumValue.newBuilder()
                 * .setDisplayName("risk management - Credit Card Risk"))
                 * .addAllowedValues(EnumValue.newBuilder()
                 * .setDisplayName("fraud management - Credit Cards")) .addAllowedValues(
                 * EnumValue.newBuilder().setDisplayName("wholesale banking - CRE"))
                 * .addAllowedValues(
                 * EnumValue.newBuilder().setDisplayName("risk management - AML")))) .build();
                 */


                TagTemplateField domain_owner = TagTemplateField.newBuilder()
                                .setDisplayName("Domain Owner").setIsRequired(true).setOrder(4)
                                .setDescription("Domain owner of the data product. This can be individual or group ldaps or email. For Dataplex, this should be set to owner of the data lake")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.RICHTEXT)
                                                .build())
                                .build();

                TagTemplateField domain_type = TagTemplateField.newBuilder()
                                .setDisplayName("Domain Type").setIsRequired(true).setOrder(3)
                                .setDescription("Type of the domain. Allowed Values: 'Source', 'Consumer'. A Source domain is close to system of truth and only consumes from sources and other source domains. A consumer domain should only consume from source domains and not real sources")
                                .setType(FieldType.newBuilder().setEnumType(FieldType.EnumType
                                                .newBuilder()
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("Source"))
                                                .addAllowedValues(EnumValue.newBuilder()
                                                                .setDisplayName("Consumer"))))
                                .build();

                TagTemplateField last_modified_by = TagTemplateField.newBuilder()
                                .setDisplayName("Last Modified By").setIsRequired(true).setOrder(2)
                                .setDescription("Individual or service account ldaps reponsible for modifying the tag")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.STRING)
                                                .build())
                                .build();

                TagTemplateField last_modified_date = TagTemplateField.newBuilder()
                                .setDisplayName("last_modified_date").setIsRequired(true)
                                .setOrder(1).setDescription("when the tag was last modified")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.TIMESTAMP)
                                                .build())
                                .build();

                TagTemplate tagTemplate = TagTemplate.newBuilder()
                                .setDisplayName("Data Product Information")
                                .setIsPubliclyReadable(true)
                                .putFields("data_product_id", data_product_id)
                                .putFields("data_product_name", data_product_name)
                                .putFields("data_product_type", data_product_type)
                                .putFields("domain", domain)
                                .putFields("data_product_description", data_product_description)
                                .putFields("data_product_icon", data_product_icon)
                                .putFields("data_product_category", data_product_category)
                                .putFields("data_product_geo_region", data_product_geo_region)
                                .putFields("domain_owner", domain_owner)
                                .putFields("data_product_owner", data_product_owner)
                                .putFields("data_product_documentation", data_product_documentation)
                                .putFields("domain_type", domain_type)
                                .putFields("last_modified_by", last_modified_by)
                                .putFields("last_modified_date", last_modified_date).build();
                createTagTemplate(locationName, tagTemplateId, tagTemplate);
        }

        public static void createDataProductQualityTagTemplate(LocationName locationName)
                        throws IOException {
                String tagTemplateId = "data_product_quality";
                TagTemplateField data_quality_score = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product Quality").setIsRequired(true)
                                .setOrder(13).setDescription("Overall Data Quality Score")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.DOUBLE)
                                                .build())
                                .build();

                TagTemplateField timeliness_score = TagTemplateField.newBuilder()
                                .setDisplayName("Timeliness Score").setIsRequired(true).setOrder(12)
                                .setDescription("An assessment of the timeliness percentage(0-100%)")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.DOUBLE)
                                                .build())
                                .build();

                TagTemplateField correctness_score = TagTemplateField.newBuilder()
                                .setDisplayName("Correctness Score").setIsRequired(true)
                                .setOrder(11)
                                .setDescription("An assessment of the correctness percentage(0-100%)")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.DOUBLE)
                                                .build())
                                .build();

                TagTemplateField integrity_score = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product Integrity Score").setIsRequired(true).setOrder(10)
                                .setDescription("An assessment of the integrity percentage(0-100%)")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.DOUBLE)
                                                .build())
                                .build();

                TagTemplateField conformity_score = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product Conformity Score").setIsRequired(true)
                                .setOrder(9)
                                .setDescription("An assessment of the conformity percentage(0-100%)")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.DOUBLE)
                                                .build())
                                .build();

                TagTemplateField completeness_score = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product Completeness Score").setIsRequired(true)
                                .setOrder(8)
                                .setDescription("An assessment of the completeness percentage(0-100%)")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.DOUBLE)
                                                .build())
                                .build();

                TagTemplateField uniqueness_score = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product Uniqueness Score").setIsRequired(true)
                                .setOrder(7)
                                .setDescription("An assessment of the uniqueness percentage(0-100%)")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.DOUBLE)
                                                .build())
                                .build();

                TagTemplateField accuracy_score = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product Accuracy Score").setIsRequired(true)
                                .setOrder(6)
                                .setDescription("An assessment of the accuracy percentage(0-100%)")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.DOUBLE)
                                                .build())
                                .build();
                TagTemplateField related_data_products = TagTemplateField.newBuilder()
                                .setDisplayName("Detailed Quality Report").setIsRequired(true)
                                .setOrder(5).setDescription("Detailed Quality Report")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.RICHTEXT)
                                                .build())
                                .build();

                TagTemplateField dq_dashboard = TagTemplateField.newBuilder()
                                .setDisplayName("Data Quality Dashboard").setIsRequired(true)
                                .setOrder(4).setDescription("DQ Dashboard")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.RICHTEXT)
                                                .build())
                                .build();

                TagTemplateField last_modified_by = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product Quality").setIsRequired(true)
                                .setOrder(3)
                                .setDescription("User responsible for modifying the tag")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.STRING)
                                                .build())
                                .build();

                TagTemplateField last_modified_date = TagTemplateField.newBuilder()
                                .setDisplayName("last_modified_date").setIsRequired(true)
                                .setOrder(1).setDescription("when the tag was last modified")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.TIMESTAMP)
                                                .build())
                                .build();



                TagTemplateField last_profiling_date = TagTemplateField.newBuilder()
                                .setDisplayName("Last Profiling Date").setIsRequired(true)
                                .setOrder(1)
                                .setDescription("When Data Quality Job was last profiled or ran")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.STRING)
                                                .build())
                                .build();

                TagTemplate tagTemplate = TagTemplate.newBuilder()
                                .setDisplayName("Data Product Quality Score")
                                .setIsPubliclyReadable(true)
                                .putFields("data_quality_score", data_quality_score)
                                .putFields("timeliness_score", timeliness_score)
                                .putFields("correctness_score", correctness_score)
                                .putFields("integrity_score", integrity_score)
                                .putFields("conformity_score", conformity_score)
                                .putFields("completeness_score", completeness_score)
                                .putFields("uniqueness_score", uniqueness_score)
                                .putFields("accuracy_score", accuracy_score)
                                .putFields("last_modified_by", last_modified_by)
                                .putFields("last_modified_date", last_modified_date)
                                .putFields("related_data_products", related_data_products)
                                .putFields("dq_dashboard", dq_dashboard)
                                .putFields("last_profiling_date", last_profiling_date).build();

                createTagTemplate(locationName, tagTemplateId, tagTemplate);

        }


        public static void createDataProductExchangeTagTemplate(LocationName locationName)
                        throws IOException {
                String tagTemplateId = "data_product_exchange";
                TagTemplateField data_exchange_platform = TagTemplateField.newBuilder()
                                .setDisplayName("data_exchange_platform").setIsRequired(true)
                                .setOrder(6).setDescription("Data Exchange Platform")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.STRING)
                                                .build())
                                .build();

                TagTemplateField data_exchange_url = TagTemplateField.newBuilder()
                                .setDisplayName("data_exchange_url").setIsRequired(true).setOrder(5)
                                .setDescription("Data Exchange Link")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.RICHTEXT)
                                                .build())
                                .build();

                TagTemplateField access_instructions = TagTemplateField.newBuilder()
                                .setDisplayName("access_instructions").setIsRequired(true)
                                .setOrder(4).setDescription("Access Instructions")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.RICHTEXT)
                                                .build())
                                .build();

                TagTemplateField publish_date = TagTemplateField.newBuilder()
                                .setDisplayName("publish date").setIsRequired(true).setOrder(3)
                                .setDescription("when the data product was last published for consumption")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.TIMESTAMP)
                                                .build())
                                .build();

                TagTemplateField last_modified_date = TagTemplateField.newBuilder()
                                .setDisplayName("last_modified_date").setIsRequired(true)
                                .setOrder(2).setDescription("when the tag was last modified")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.TIMESTAMP)
                                                .build())
                                .build();

                TagTemplateField last_modified_by = TagTemplateField.newBuilder()
                                .setDisplayName("Data Product Quality").setIsRequired(true)
                                .setOrder(1)
                                .setDescription("User responsible for modifying the tag")
                                .setType(FieldType.newBuilder()
                                                .setPrimitiveType(FieldType.PrimitiveType.STRING)
                                                .build())
                                .build();

                TagTemplate tagTemplate = TagTemplate.newBuilder()
                                .setDisplayName("Data Product Exchange").setIsPubliclyReadable(true)
                                .putFields("data_exchange_platform", data_exchange_platform)
                                .putFields("data_exchange_url", data_exchange_url)
                                .putFields("access_instructions", access_instructions)
                                .putFields("publish_date", publish_date)
                                .putFields("last_modified_date", last_modified_date)
                                .putFields("last_modified_by", last_modified_by).build();

                createTagTemplate(locationName, tagTemplateId, tagTemplate);

        }



        public static void createTagTemplate(LocationName name, String tagTemplateId,
                        TagTemplate template) throws IOException {
                // Initialize client that will be used to send requests. This client only needs to
                // be
                // created
                // once, and can be reused for multiple requests. After completing all of your
                // requests,
                // call
                // the "close" method on the client to safely clean up any remaining background
                // resources.
                try (DataCatalogClient client = DataCatalogClient.create()) {

                        try {
                                /*
                                 * GetTagTemplateRequest getRequest =
                                 * GetTagTemplateRequest.newBuilder().setName("projects/" +
                                 * name.getProject() + "/locations/" + name.getLocation() +
                                 * "/tagTemplates/" + tagTemplateId +"_testing").build();
                                 * 
                                 * TagTemplate tagTemplate = client.getTagTemplate(getRequest);
                                 * System.out.println(tagTemplate);
                                 */

                                DeleteTagTemplateRequest delRequest = DeleteTagTemplateRequest
                                                .newBuilder()
                                                .setName("projects/" + name.getProject()
                                                                + "/locations/" + name.getLocation()
                                                                + "/tagTemplates/" + tagTemplateId)
                                                .setForce(true).build();
                                client.deleteTagTemplate(delRequest);

                        } catch (Exception e) {
                                System.out.println("Tag was not found!");

                        }


                        CreateTagTemplateRequest request = CreateTagTemplateRequest.newBuilder()
                                        .setParent(name.toString()).setTagTemplateId(tagTemplateId)
                                        .setTagTemplate(template).build();
                        client.createTagTemplate(request);
                        System.out.println("Tag template " + template.getDisplayName()
                                        + " created successfully in location " + name);
                }
        }
}
