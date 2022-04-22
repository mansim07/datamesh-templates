# Data Product Information Annotations 

The Data product information annotation modules requires an yaml file matching the Data Catalog Tag Template structure input and Dataplex Entity. There are 2 ways to create a Data Product Annotations:
    
    Option1:  Manually provide the values in a yaml file
    Option2: Leave it empty or say "derived" to fetch the tag values dynamically. This option will require setting a few labels in the Dataplex lakes and zone level 



# Pre-requisites

### Step1: Tag Templates Creation
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