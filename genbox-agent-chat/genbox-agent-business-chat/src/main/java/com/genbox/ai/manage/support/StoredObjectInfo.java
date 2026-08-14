package com.genbox.ai.manage.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支撑组件。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoredObjectInfo {

    private String bucketName;

    private String objectName;

    private String objectUrl;
}
