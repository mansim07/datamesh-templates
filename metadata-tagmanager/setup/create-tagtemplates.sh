#!/bin/bash

GCP_PROJECT_ID=${1}
TAG_LOCATION=${2}

gcloud data-catalog tag-templates create data_product_information --field=id=ID1,display-name=DISPLAY1,type=string --field=id=ID2,display-name=DISPLAY2,type='enum(A|B)',required=TRUE


gcloud data-catalog tag-templates create data_product_classification --project=${GCP_PROJECT_ID} --location=${TAG_LOCATION} --field=id=is_pii,display-name="Is PII",type=bool --field=id=ID2,display-name=DISPLAY2,type='enum(A|B)',required=TRUE