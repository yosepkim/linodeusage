package com.edgecloud9.linodeusage.controller;

import com.edgecloud9.linodeusage.model.BucketListResponse;
import com.edgecloud9.linodeusage.model.DataTransferResponse;
import com.edgecloud9.linodeusage.model.ObjectStorageUsage;
import com.edgecloud9.linodeusage.service.ObjectStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/object-storage/")
public class ObjectStorageController {
    @Autowired
    ObjectStorageService service;

    @GetMapping("current/usage")
    public ObjectStorageUsage currentUsage(@RequestHeader(name="token") String authToken) {
        return service.getCurrentObjectStorageUsage(authToken);
    }

    @GetMapping("current/transfer")
    public DataTransferResponse currentTransferUsage(@RequestHeader(name="token") String authToken) {
        return service.getCurrentDataTransfer(authToken);
    }

    @GetMapping("current/buckets")
    public BucketListResponse currentBucketUsage(@RequestHeader(name="token") String authToken) {
        return service.getAllCurrentBucketUsage(authToken);
    }
}
