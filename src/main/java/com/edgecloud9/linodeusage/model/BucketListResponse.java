package com.edgecloud9.linodeusage.model;

import lombok.Data;

import java.util.List;

@Data
public class BucketListResponse {
    List<Bucket> data;
    long page;
    long pages;
    long results;
}
