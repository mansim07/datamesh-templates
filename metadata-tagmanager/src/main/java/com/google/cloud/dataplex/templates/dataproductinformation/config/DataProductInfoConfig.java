/*
 * Copyright (C) 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.dataplex.templates.dataproductinformation.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DataProductInfoConfig {

    
    private String data_product_id;
    
    private String data_product_name;
    
    private String data_product_type;
    
    private String data_product_description;
    
    private String data_product_icon;
    
    private String data_product_category;

    private String data_product_geo_region;

    private String data_product_owner;

    private String data_product_documentation;

    private String domain;


    private String domain_owner;

    private String domain_type;


    private String last_modified_by;

    private String last_modify_date;

    public String getDataProductId() {
        return data_product_id;
    }

    public void setDataProductId(String data_product_id) {
        this.data_product_id = data_product_id;
    }

    public String getDataProductName() {
        return data_product_name;
    }

    public void setDataProductName(String data_product_name) {
        this.data_product_name = data_product_name;
    }

    public String getDataProductType() {
        return data_product_type;
    }

    public void setDataProductType(String data_product_type) {
        this.data_product_type = data_product_type;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomainType() {
        return domain_type;
    }

    public void setDomainType(String domain_type) {
        this.domain_type = domain_type;
    }

    public String getDataProductDescription() {
        return data_product_description;
    }

    public void setDataProductDescription(String data_product_description) {
        this.data_product_description = data_product_description;
    }

    public String getDataProductIcon() {
        return data_product_icon;
    }

    public void setDataProductIcon(String data_product_icon) {
        this.data_product_icon = data_product_icon;
    }

    public String getDataProductCategory() {
        return data_product_category;
    }

    public void setDataProductCategory(String data_product_category) {
        this.data_product_category = data_product_category;
    }
    
    public String getGeoRegion() {
        return data_product_geo_region;
    }

    public void setGeoRegion(String data_product_geo_region) {
        this.data_product_geo_region = data_product_geo_region;
    }
    

    public String getDomainOwner() {
        return domain_owner;
    }

    public void setDomainOwner(String domain_owner) {
        this.domain_owner = domain_owner;
    }
    public String getDataProductOwner() {
        return data_product_owner;
    }

    public void setDataProductOwner(String data_product_owner) {
        this.data_product_owner = data_product_owner;
    }

    public String getDataProductDocumentation() {
        return data_product_documentation;
    }

    public void setDataProductDocumentation(String data_product_documentation) {
        this.data_product_documentation = data_product_documentation;
    }

    public String getlastModifiedDate() {
        return last_modify_date;
    }

    public void setLastModifiedDate(String last_modify_date) {
        this.last_modify_date = last_modify_date;
    }

    public String getLastModifiedBy() {
        return last_modified_by;
    }

    public void setLastModifiedBy(String last_modified_by) {
        this.last_modified_by = last_modified_by;
    }


}