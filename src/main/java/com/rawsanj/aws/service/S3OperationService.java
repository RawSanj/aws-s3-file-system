package com.rawsanj.aws.service;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.rawsanj.aws.controller.IndexController;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by @author Sanjay Rawat on 7/10/17.
 */

@Component
public class S3OperationService {
	

	Logger logger = LoggerFactory.getLogger(S3OperationService.class);

    @Value("${aws.bucket.name}")
    private String bucket;

    private final ResourcePatternResolver resourcePatternResolver;
    private final ResourceLoader resourceLoader;
    private final AmazonS3 amazonS3;

    @Autowired
    public S3OperationService(ResourcePatternResolver resourcePatternResolver, ResourceLoader resourceLoader, AmazonS3 amazonS3) {
        this.resourcePatternResolver = resourcePatternResolver;
        this.resourceLoader = resourceLoader;
        this.amazonS3 = amazonS3;
    }

    public List<String> getAllFiles() throws IOException {
        String bucketPath = "s3://" + bucket + "/";
        Resource[] allFilesInFolder =  resourcePatternResolver.getResources(bucketPath+"**");
        List<Resource> resources = Arrays.asList(allFilesInFolder);
        List<String> filesInS3Bucket = new ArrayList<>();

        resources.forEach(f-> {
            filesInS3Bucket.add(f.getFilename());
        });
        Collections.sort(filesInS3Bucket);

        return filesInS3Bucket;
    }
    
    public void saveFile(MultipartFile file, String path, String fileName) throws IOException {
    	
    	String bucketPath = "s3://" + bucket + "/";
        Resource resource = this.resourceLoader.getResource(bucketPath+path+ "/"+ fileName);

        System.out.println("Storing File :"+bucketPath+path+ "/"+ fileName);

        WritableResource writableResource = (WritableResource) resource;

        try (OutputStream outputStream = writableResource.getOutputStream()) {
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());

            logger.info("Storing file {} in S3", fileName);
            outputStream.write(bytes);
        }catch (Exception e){
            logger.error("Failed to upload file on S3");
            e.printStackTrace();
        }
            	
    }
    
    public void deleteFile(String file) {
    	amazonS3.deleteObject(bucket, file);

	}

}
