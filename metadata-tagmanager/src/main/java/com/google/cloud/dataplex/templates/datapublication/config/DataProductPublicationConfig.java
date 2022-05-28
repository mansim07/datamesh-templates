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

package com.google.cloud.dataplex.templates.datapublication.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DataProductPublicationConfig {

    @NotNull
    private String data_exchange_platform;
    @NotNull
    private String data_exchange_url;
    @NotNull
    private String access_instructions;
    @NotNull
    private String publish_date;
    @NotNull
    private String last_modified_by;
    @NotNull
    private String last_modified_date; 

    public String getDataExchangePlatform() {
        return data_exchange_platform;
    }

    public void setDataExchangePlatform(String data_exchange_platform) {
        this.data_exchange_platform = data_exchange_platform;
    }

    public String getDataExchangeUrl() {
        return data_exchange_url;
    }

    public void setDataExchangeUrl(String data_exchange_url) {
        this.data_exchange_url = data_exchange_url;
    }
    public String getAccessInstruction() {
        return access_instructions;
    }

    public void setAccessInstruction(String access_instructions) {
        this.access_instructions = access_instructions;
    }
    public String getPublishDate() {
        return publish_date;
    }

    public void setPublishDate(String host_date) {
        this.publish_date = host_date;
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
  
}
