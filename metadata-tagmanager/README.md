# Datamesh Templates
This repository contains a series of Datamesh templates that automate the metadata management process in the Data as a Product lifecycle. This may be accomplished with Dataplex's Custom Tasks utility, which is based on the Sparkservelss service and can be readily customized by developers.

With this set of automation code, you'll be able to build data-as-a-products and annotate them at scale which is a primary challenge. The tool now supports automated annotations for the following:

- Data product classfication (Table-level only)
- Data product quality(TBD)
- Data Product info
- Data Product versioning(TBD)
- Data product cost metric(TBD)
- Data product publish info(TBD)
- Data product consumption info(TBD)

Currently only supports BigQuery storage only. Tool can be easily extended to support other storages in future

Under-the-hood, this modules makes use of the below underlying GCP technologies:
- Google Cloud Dataplex
- Google Cloud DLP
- Google Cloud Data Catalog
- Google Cloud Logging
- Google Cloud Billing
- Google Cloud Analytic Hub

A centralized team should be responsible for owning and maintainng this repository and Data Product Owners/Domain team should be able use thus module to automate annotations of their data products.

## Pre-requisites
While the code is very flexbile in terms of implement, a set of pre-requisites are required to automate metadata annotations end-to-end.

1. Dataplex - Logically organize your data products into lakes, zones and assets
2. DLP - Enable automatic DLP for BigQuery
3. Create tag templates in Data Catalog

# Getting Started

## Requirements
- Java 8
- Maven 3

1. Clone this repository:

    ```
    git clone ssh://maharanam@google.com@source.developers.google.com:2022/p/gcp-data-solutions/r/data-mesh-demo

    cd data-mesh-demo/data-product-toolkit/tagmanager
    ```
2. Obtain authentication credentials.

    Create local credentials by running the following command and following the oauth2 flow (read more about the command [here][auth_command]):


    ```
    gcloud auth application-default login
    ```
    Or manually set the GOOGLE_APPLICATION_CREDENTIALS environment variable to point to a service account key JSON file path.

    Learn more at [Setting Up Authentication for Server to Server Production Applications][ADC].

    Note: Application Default Credentials is able to implicitly find the credentials as long as the application is running on Compute Engine, Kubernetes Engine, App Engine, or Cloud Functions.

3. Format Code [Optional]
    ```
        mvn spotless:apply
    ```
    This will format the code and add a license header. To verify that the code is formatted correctly, run:

    ```
        mvn spotless:check
    ```

4. Building the Project
    Build the entire project using the maven compile command.
    ```
        mvn clean install
        ```

5. Executing Templates

    The README files for each of the different templates have detailed instruction on how to use them. Please see the README.md file for the relevant section.

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
A service account that has access to the DLP results table and access to create tags for the data entities.

# Development
Contributing
See the contributing instructions to get started contributing.

# Tag Analytics
Use the Open-Source module(TBD) to push the tag into BigQuery Table for reporting, analytics, monitoring, and auditing purposes.

# License
All solutions within this repository are provided under the Apache 2.0 license. Please see the LICENSE file for more detailed terms and conditions.

# Disclaimer
This repository and its contents are not an official Google Product.

# Contact
Questions, issues, and comments should be directed to
