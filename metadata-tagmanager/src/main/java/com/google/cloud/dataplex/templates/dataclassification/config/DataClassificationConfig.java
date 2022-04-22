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

package com.google.cloud.dataplex.templates.dataclassification.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import jakarta.validation.constraints.NotNull;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DataClassificationConfig {
    
    private DlpReportConfig dlp_report_config; 

    private Boolean is_pii;
 
    private String sensitivity_score;

    private String risk_score;

    private String info_types;

    private Boolean is_confidential;

    private Boolean is_restricted;

    private Boolean is_public;

    private Boolean is_encrypted;

    private String encryption_key_type;

    private String last_profiling_date;

    private String last_modified_by;

    private String last_modified_date;

    private String related_data_products; 

 

    public DlpReportConfig getDLPReportConfig() {
        return dlp_report_config;
    }

    public void setDLPReportConfig(DlpReportConfig dlp_report_config) {
        this.dlp_report_config = dlp_report_config;
    }

    public Boolean getHasPii() {
        return is_pii;
    }

    public void setHasPii(Boolean is_pii) {
        this.is_pii = is_pii;
    }

    public String getSensitivityScore() {
        return sensitivity_score;
    }

    public void setSensitivityScore(String sensitivity_score) {
        this.sensitivity_score = sensitivity_score;
    }

    public String getRiskScore() {
        return risk_score;
    }

    public void setRiskScore(String risk_score) {
        this.risk_score = risk_score;
    }

    public String getInfoTypes() {
        return info_types;
    }

    public void setInfoTypes(String info_types) {
        this.info_types = info_types;
    }

    public Boolean getIsConfidential() {
        return is_confidential;
    }

    public void setIsConfidential(Boolean is_confidential) {
        this.is_confidential = is_confidential;
    }

    public Boolean getIsRestricted() {
        return is_restricted;
    }

    public void setIsRestricted(Boolean is_restricted) {
        this.is_restricted = is_restricted;
    }

    public Boolean getIsPublic() {
        return is_public;
    }

    public void setIsPublic(Boolean is_public) {
        this.is_public = is_public;
    }

    public Boolean getIsEncrypted() {
        return is_encrypted;
    }

    public void setIsenrypted(Boolean is_encrypted) {
        this.is_encrypted = is_encrypted;
    }

    public String getEncryptionKeyType() {
        return encryption_key_type;
    }

    public void setEncryptionKeyType(String encryption_key_type) {
        this.encryption_key_type = encryption_key_type;
    }

    public String getLastProfilingDate() {
        return last_profiling_date;
    }

    public void setLastProfilingDate(String last_profiling_date) {
        this.last_profiling_date = last_profiling_date;
    }
    
    public String getLastModifiedBy() {
        return last_modified_by;
    }

    public void setLastModifiedBy(String last_modified_by) {
        this.last_modified_by = last_modified_by;
    }
    public String getLastModifiedDate() {
        return last_modified_date;
    }

    public void setLastModifiedDate(String last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public String getRelatedDataProducts() {
        return related_data_products;
    }

    public void setRelatedDataProducts(String related_data_products) {
        this.related_data_products = related_data_products;
    }


}
