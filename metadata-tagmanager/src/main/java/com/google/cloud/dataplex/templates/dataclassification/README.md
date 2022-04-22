# Data Product Classification Annotations 

The Data product classification annotation modules requires an yaml file matching the Data Catalog Tag Template structure input and Dataplex Entity. There are 2 ways to create a Data Product Annotations:
    
    Option1:  Manually provide the values in a yaml file
    Option2: Leave it empty or say "derived" to fetch the tag values dynamically



# Pre-requisites

### Step1: Tag Templates Creation
 Create the Tag template in Data Catalog. This is a one time job and should be owned by the centralized governance team rather than individual domain teams. 


| Field Display Name  | Field ID | Field Description  | Type |  Is Field required |
| ------------- | ------------- | ------------- | ------------- | ------------- |
| Has PII | has_pii  | Indicator of Personally Identifiable Information(PII) data	  | BOOL  | Content Cell  |
| Sensitivity Score  | sensitivity_score  | Sensitivity score of the data  | STRING | Content Cell  |
| Risk Score | risk_score  | The risk score of the data assert  | STRING  | Content Cell  |
| Info Types  | info_types  | List of info types present in the data  | STRING  | Content Cell  |
| Is Confidential| is_confidential  | Indicates if the data assert is confidential  | BOOL  | Content Cell  |
| Is Restricted | is_restricted  | Indicates if the data assert is restricted  | BOOL  | Content Cell  |
| Is Public | is_public  | Indicates if the data assert is public  | BOOL  | Content Cell  |
| Is Encrypted | is_encrypted | Indicates if the data assert is encrypted at rest  | BOOL  | Content Cell  |
| Encryption Key Type		 | encryption_key_type  | Encryption key type used for encrypting the data asset	  | STRING  | Content Cell  |
| Last Profiling Date  | last_profiling_date  | Last time the data asset was profiled for data classification	  | DATETIME | Content Cell  |
| Last Modified By | last_modfied_by  | 	individual or system id responsible for modifying the tag | STRING  | Content Cell  |
| Last Modified Date  | last_modified_date  | when the tag was last modified  | DATETIME  | Content Cell  |


### Step2: prepare the Input Yaml file
Create a Yaml Input file

For Option#1, Manually provide the inputs in the yaml file. This is a great option for testing before automation  


For Option#2, you can use the default.yaml file. 

### Step3: Enable the DLP Data Profiling
Enable the Data profiling job either at the project level or the organization level and make sure entry is available for your data product in the BQ output table of DLP profiling. This table details is required for automation. 

# Execution
The code can be executed locally as well as via custom tasks in Dataplex 

## Using Custom tasks in Dataplex 

### Inputs: Task Configurations
**Type**: Spark 

**Main Class or jar file**: com.google.cloud.dataplex.templates.tagmanager.dataclassification.DataProductClassification

**File uris(Optional)**: **GCS Path to the Yaml file* e.g. gs://data-catalog-demo/DataClassification.yaml 

**Arguments**:
 
 tag_template_id: name of the tag template id. e.g. projects/mdm-dg/locations/us-central1/tagTemplates/data_classification

 project_id: Project Id of the Dataplex lake e.g. mdm-dg
 
 location: Location of the Dataplex lake e.g. us-central1
 
 lake_id: Id of the Dataplex lake e.g. consumer-banking-credit-cards-domain-prod
 
 zone_id: Zone id of the Dataplex lake e.g. data-product-zone 
 
 entity_id: Entity_id of the Dataplex entity e.g. cc_transactions_data
 
 input_file: Name of the input yaml file. Set it to "default.yaml" for dynamic annotation but make sure the DLP results are available  e.g. DataClassification.yaml. If providing some manual overrides make sure it is avaibable in classpath by specifing the file in File URIS above. 


**Service Account**:
A service account that has access to the DLP results table and access to create tags for the data assert.










