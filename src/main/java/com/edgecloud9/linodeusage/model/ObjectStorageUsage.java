package com.edgecloud9.linodeusage.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ObjectStorageUsage {
    long dataTransferUsage;
    long totalBucketCount;
    long bucketCountFromAPI;
    long totalItemCount;
    long totalStorageSize;
    Map<String, Long> totalSizePerRegion;
    Map<String, Long> totalItemPerRegion;
    List<Bucket> buckets;
}
