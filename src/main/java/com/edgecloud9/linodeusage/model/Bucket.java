package com.edgecloud9.linodeusage.model;

import lombok.Data;

@Data
public class Bucket {
    String label;
    long objects;
    long size;
    String region;
}
