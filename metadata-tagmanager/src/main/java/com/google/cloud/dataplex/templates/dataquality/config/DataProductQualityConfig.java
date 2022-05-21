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

package com.google.cloud.dataplex.templates.dataquality.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DataProductQualityConfig {

    private DqReportConfig dq_report_config; 

    private String quality_score;

    private String timeliness_score;

    private String correctness_score;

    private String integrity_score;

    private String conformity_score;

    private String completeness_score;

    private String uniqueness_score;

    private String accuracy_score;

    private String dq_dashboard;

    private String last_profiling_date; 

    private String last_modified_by;

    private String last_modified_date;

    private String related_data_product; // can be dashboard or dataset

    public DqReportConfig getDqReportConfig() {
        return dq_report_config;
    }

    public void setDqReportConfig(DqReportConfig dq_report_config) {
        this.dq_report_config = dq_report_config;
    }

    public String getTimelinessScore() {
        return timeliness_score;
    }

    public void setTimelinessScore(String timeliness_score) {
        this.timeliness_score = timeliness_score;
    }

    public String getQualityScore() {
        return quality_score;
    }

    public void setQualityScore(String quality_score) {
        this.quality_score = quality_score;
    }

    public String getCorrectnessScore() {
        return correctness_score;
    }

    public void setCorrectnessScore(String correctness_score) {
        this.correctness_score = correctness_score;
    }
    public String getIntegrityScore() {
        return integrity_score;
    }

    public void setIntegrityScore(String integrity_score) {
        this.integrity_score = integrity_score;
    }
    public String getConformityScore() {
        return conformity_score;
    }

    public void setConformityScore(String conformity_score) {
        this.conformity_score = conformity_score;
    }
    public String getCompletenessScore() {
        return completeness_score;
    }

    public void setCompletenessScore(String completeness_score) {
        this.completeness_score = completeness_score;
    }
    public String getUniquenessScore() {
        return uniqueness_score;
    }

    public void setUniquenessScore(String uniqueness_score) {
        this.uniqueness_score = uniqueness_score;
    }
    public String getAccuracyScore() {
        return accuracy_score;
    }

    public void setAccuracyScore(String accuracy_score) {
        this.accuracy_score = accuracy_score;
    }


    public String getDqDashboard() {
        return dq_dashboard;
    }

    public void setDqDashboard(String dq_dashboard) {
        this.dq_dashboard = dq_dashboard;
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
        return related_data_product;
    }

    public void setRelatedDataProducts(String related_data_product) {
        this.related_data_product = related_data_product;
    }
  
}
