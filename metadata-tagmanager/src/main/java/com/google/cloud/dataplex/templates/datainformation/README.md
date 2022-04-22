# Data Product Information Annotations 

The Data product information annotation modules requires an yaml file matching the Data Catalog Tag Template structure input and Dataplex Entity. 


# Tag Templates 
 Create the Tag template in Data Catalog. This is a one time job and should be owned by the centralized governance team rather than individual domain teams. A tag should be created for each region. 
 The field_id and type should exactly match to what is mentioned below. A mismatch can lead to failures. 

 | Field Display Name  | Field ID | Field Description  | Type |  Is Field required |
| ------------- | ------------- | ------------- | ------------- | ------------- |
| Data Product Identifier| data_product_id  | A unique id for the data product	  | STRING  | REQUIRED  |
| Data Product Name  | data_product_name | Name of the data product  | STRING | REQUIRED  |
| Data Product Type | data_product_type  | Type of the data product: LOOKER, BIGQUERY, MYSQL, POSTGRES, APIGEE, GCS, MODEL, PUBSUB, S3, SNOWFLAKE, AZURE, HIVE, HDFS, TABLEAU  | ENUM  | REQUIRED  |
| Domain  | domain  | domain of the data product. An organization should already have a list of data domain. VALUES - Org specific. This **should match** the lake name.   | STRING  | REQUIRED  |
|Data Product Description| data_product_description  | A brief description of the data product  | STRING  | REQUIRED  |
| Data Product Icon | data_product_icon  | An universal image or icon for your product for documentation and UI purposes. Provide an URL.  | RICHTEXT  | REQUIRED  |
| Data Product Category| data_product_category  | Category of the data product - Master Data, Reference Data, System Data, Application Data  | ENUM  | REQUIRED  |
| Geo Region | geo_region | The region the data product is available for consumption  | STRING  | REQUIRED  |
| Domain Owner	 | domain_owner  | Domain owner of the data product. This can be individual or group ldaps or email	  | RICHTEXT  | REQUIRED  |
|Data Product Owner  | data_product_owner  | L	The Data product owner. This can be the domain owner or another set of individual or group responsible for the product | RICHTEXT | REQUIRED  |
| Data Product Documentation | data_product_documentation  | 		Product Documentation for reference purposes. Example - Links to source code | RICHTEXT  | REQUIRED  |
| Domain Type  | domain_type  | type of the domain e.g. Source or Consumer  | ENUM  | REQUIRED  |
| Last Modified Date  | last_modified_by  | individual or system id responsible for modifying the tag | STRING  | REQUIRED  |
| Last Modified Date  | last_modified_date  | when the tag was last modified  | DATETIME  | REQUIRED  |


# Tag Template Structure 
TBD

# Derivation Logic
TBD

# Getting Started

## 1. Create an Input Yaml file
There are 2 ways to create a Data Product Annotations:
    
-   Option1:  Manually provide the values in a yaml file. This is a great option for testing before automation. See [sample](https://github.com/mansim07/datamesh-templates/blob/d6f51a4fc14ab5e8784cf453570187d53f865647/metadata-tagmanager/src/main/java/com/google/cloud/dataplex/samples/DataProductInfo-manual.yaml)

-   Option2: Leave it empty or say "derived" to fetch the tag values dynamically. This option will require setting a few labels in the Dataplex lakes and zone level. See [sample](https://github.com/mansim07/datamesh-templates/blob/d6f51a4fc14ab5e8784cf453570187d53f865647/metadata-tagmanager/src/main/java/com/google/cloud/dataplex/samples/DataProductInfo-auto.yaml)

## 2. Upload the yaml file to a GCS location 
## 3. Set labels  [For Automation]
Make sure the below labels are available on  Dataplex

1. At lake-level
    
    Label Key: domain_type

    Accepted Values: source,consumer 

2. At zone-level 
    
    Label Key: data_product_catagory

    Accepted Values: master_data,system_data, application_data, reference_data

# Execution
The code can be executed locally as well as via custom tasks in Dataplex 

## Using Custom tasks in Dataplex 

Provide the following inputs in Task Configurations

**Type**: Spark 

**Main Class or jar file**: com.google.cloud.dataplex.templates.dataclassification

**File uris(Optional)**: **GCS Path to the Yaml file* e.g. gs://data-catalog-demo/DataClassification.yaml 

**Arguments**:
 
 tag_template_id: Absolute name of the tag template id including project and location. e.g. projects/mdm-dg/locations/us-central1/tagTemplates/data_classification

 project_id: Project Id of the Dataplex lake e.g. mdm-dg
 
 location: Location of the Dataplex lake e.g. us-central1
 
 lake_id: Id of the Dataplex lake e.g. consumer-banking-credit-cards-domain-prod
 
 zone_id: Zone id of the Dataplex lake e.g. data-product-zone 
 
 entity_id: Entity_id of the Dataplex entity e.g. cc_transactions_data
 
 input_file: Name of the input yaml file. Set it to "default.yaml" for dynamic annotation but make sure the DLP results are available  e.g. DataClassification.yaml. If providing some manual overrides make sure it is avaibable in classpath by specifing the file in File URIS above. 

**Service Account**:
A service account that has access to the DLP results table and access to create tags for the data assert.

