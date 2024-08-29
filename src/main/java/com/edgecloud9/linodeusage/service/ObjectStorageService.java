package com.edgecloud9.linodeusage.service;

import com.edgecloud9.linodeusage.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ObjectStorageService {
    @Autowired
    private RestTemplate restTemplate;

    public ObjectStorageUsage getCurrentObjectStorageUsage(String authToken) {
        DataTransferResponse currentDataTransfer = getCurrentDataTransfer(authToken);
        BucketListResponse allCurrentBucketUsage = getAllCurrentBucketUsage(authToken);

        long totalSize = 0;
        long totalItemCount = 0;
        long bucketCount = 0;

        Map<String, Long> sizePerRegion = new HashMap<>();
        Map<String, Long> countPerRegion = new HashMap<>();
        for(Bucket bucket : allCurrentBucketUsage.getData()) {
            totalSize += bucket.getSize();
            totalItemCount += bucket.getObjects();
            bucketCount += 1;

            Long currentRegionSize = 0L;
            if (sizePerRegion.containsKey(bucket.getRegion())) {
                currentRegionSize = sizePerRegion.get(bucket.getRegion());
            }
            sizePerRegion.put(bucket.getRegion(), currentRegionSize + bucket.getSize());

            Long currentRegionCount = 0L;
            if (countPerRegion.containsKey(bucket.getRegion())) {
                currentRegionCount = countPerRegion.get(bucket.getRegion());
            }
            countPerRegion.put(bucket.getRegion(), currentRegionCount + bucket.getObjects());
        }
        return ObjectStorageUsage.builder()
                .totalBucketCount(allCurrentBucketUsage.getData().size())
                .bucketCountFromAPI(bucketCount)
                .totalStorageSize(totalSize)
                .totalItemCount(totalItemCount)
                .buckets(allCurrentBucketUsage.getData())
                .totalSizePerRegion(sizePerRegion)
                .totalItemPerRegion(countPerRegion)
                .dataTransferUsage(currentDataTransfer.getUsage())
                .build();
    }

    public DataTransferResponse getCurrentDataTransfer(String authToken) {
        return callObjectStorageEndpoint(authToken, "transfer", DataTransferResponse.class);
    }

    public BucketListResponse getAllCurrentBucketUsage(String authToken) {
        return callObjectStorageEndpoint(authToken, "buckets", BucketListResponse.class);
    }

    private <T> T callObjectStorageEndpoint(String authToken, String path, Class<T> clazz) {
        String OBJECT_STORAGE_BASE_URL = "https://api.linode.com/v4/object-storage/";

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("accept", "application/json");
        headers.add("authorization", "Bearer " + authToken);
        ResponseEntity<T> response = restTemplate.exchange(OBJECT_STORAGE_BASE_URL + path,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                clazz);

        return clazz.cast(response.getBody());
    }
}
