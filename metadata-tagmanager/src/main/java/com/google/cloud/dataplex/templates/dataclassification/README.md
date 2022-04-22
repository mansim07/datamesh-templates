# Data Product Classification Annotations 

The Data product classification annotation modules requires an yaml file matching the Data Catalog Tag Template structure input and Dataplex Entity. There are 2 ways to create a Data Product Annotations:
    
    Option1:  Manually provide the values in a yaml file
    Option2: Leave it empty or say "derived" to fetch the tag values dynamically

# Tag Template Structure 

TBD

# Getting Started

## 1. Create an Input Yaml file
For Option#1, Manually provide the inputs in the yaml file. This is a great option for testing before automation  
For Option#2, you can use the default.yaml file. 

## 2. Enable the DLP Data Profiling at Organization-level[Optional]
You may always give or override the inputs using your yaml input, but if you want to build the tag dynamically, you'll need to make sure DLP scan results are available in a BQ table. After that, the util can dynamically pull data from the table where the DLP scan results are synchronised. Enable the Data Profiling job either at the project or organization level, and check that your data product is listed in the DLP profiling BQ output table. This information about the table is essential for automation.

# Execution
The code can be executed locally as well as via custom tasks in Dataplex 

## Using Custom tasks in Dataplex 

### Inputs: Task Configurations
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