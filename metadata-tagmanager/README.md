# Datamesh Templates
This repository contains a series of Datamesh templates that automate the metadata management process in the Data as a Product lifecycle for Dataplex managed data assets. This may be accomplished with Dataplex's custom tasks utility, which is based on the Sparkservelss service and can be readily customized by developers.

With this set of automation code, you'll be able to build data-as-a-products and annotate them at scale which is a primary challenge. The tool now supports automated annotations for the following:

- Data product classfication (Table-level only)
- Data product quality(TBD)
- Data product info
- Data product versioning(TBD)
- Data product cost metric(TBD)
- Data product publish info(TBD)
- Data product consumption info(TBD)
- Data product retention policy(TBD)

Currently only supports BigQuery storage only. Tool can be easily extended to support other storages in future

Under-the-hood, this modules makes use of the below underlying GCP technologies:
- Google Cloud Dataplex
- Google Cloud DLP(as a pre-requisite only.)
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
4. Data Quality Results from CloudDQ engine

Each templated will require a certain input for end-to-end automation.

# Getting Started

## Requirements
- Java 8
- Maven 3

1. Clone this repository:

    ```
    git clone git@github.com:mansim07/datamesh-templates.git

    cd datamesh-templates/metadata-tagmanager/
    ```
2.  Set the GOOGLE_APPLICATION_CREDENTIALS environment variable to point to a service account key JSON file path.

    Learn more at [Setting Up Authentication for Server to Server Production Applications][ADC].

    Note: Application Default Credentials is able to implicitly find the credentials as long as the application is running on Compute Engine, Kubernetes Engine, App Engine, or Cloud Functions.

3. Format Code
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

5. Upload the output jar to a gs bucket
    ```
      gs util cp tagmanager-1.0-SNAPSHOT.jar gs://<bucket-name>/<<folder-name>>
    ```

6. Create the tag templates in Data Catalog
    ```
        java -cp target/tagmanager-1.0-SNAPSHOT.jar  com.google.cloud.dataplex.setup.CreateTagTemplates <<project-name>> <<location>>
    ```
7. Executing Templates

    The README files for each of the different templates have detailed instruction on how to use them. Please see the README.md file for the relevant section.

    The code can be executed locally as well as via custom tasks in Dataplex

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
