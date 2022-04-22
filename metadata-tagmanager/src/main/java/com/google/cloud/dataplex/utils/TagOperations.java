package com.google.cloud.dataplex.utils;

import java.util.Map;

import com.google.cloud.datacatalog.v1.CreateTagRequest;
import com.google.cloud.datacatalog.v1.DataCatalogClient;
import com.google.cloud.datacatalog.v1.Entry;
import com.google.cloud.datacatalog.v1.Tag;
import com.google.cloud.datacatalog.v1.TagField;
import com.google.cloud.datacatalog.v1.UpdateTagRequest;

public class TagOperations {


    public static void publishTag(Entry entry, DataCatalogClient dataCatalogClient, String tagTemplateId,
            Map<String, TagField> tagValues, String tagName) {

        Tag persisted_tag = null;
        for (Tag element : dataCatalogClient.listTags(entry.getName())
                .iterateAll()) {

            if (new String(element.getTemplate().trim())
                    .equals(new String(tagTemplateId.trim()))) {
                persisted_tag = element;
            }
        }

        if (persisted_tag == null) {
            // New tag
            Tag tag = Tag.newBuilder().setTemplate(tagTemplateId)
                    .setName(tagName)
                    .putAllFields(tagValues)
                    .build();
            CreateTagRequest createTagRequest = CreateTagRequest.newBuilder()
                    .setParent(entry.getName())
                    .setTag(tag).build();

            dataCatalogClient.createTag(createTagRequest);
        } else {
            // update existing tag
            Tag tag = Tag.newBuilder().setTemplate(tagTemplateId)
                    .setName(persisted_tag.getName())
                    .putAllFields(tagValues)
                    .build();

            UpdateTagRequest updateTagRequest = UpdateTagRequest.newBuilder()
                    .setTag(tag).build();

            dataCatalogClient.updateTag(updateTagRequest);
        }

    }

}
