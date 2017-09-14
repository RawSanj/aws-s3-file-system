package com.rawsanj.aws.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.rawsanj.aws.service.S3OperationService;
import java.io.IOException;
import java.util.List;

/**
 * Created by @author Sanjay Rawat on 7/7/17.
 */

@RestController
@RequestMapping(path="/api")
public class S3FIleOpsRestController {

    Logger logger = LoggerFactory.getLogger(S3FIleOpsRestController.class);

    private final ResourceLoader resourceLoader;
    private final S3OperationService s3OperationService;

    @Value("${aws.bucket.name}")
    private String bucket;

    @Autowired
    public S3FIleOpsRestController(ResourceLoader resourceLoader, S3OperationService s3OperationService) {
        this.resourceLoader = resourceLoader;
        this.s3OperationService = s3OperationService;
    }

    @GetMapping(path = "/files")
    public ResponseEntity<List<String>> listS3Files() throws IOException {

        List<String> filesInS3Bucket = s3OperationService.getAllFiles();
        logger.info("Number of Files in S3 bucket: {}", filesInS3Bucket.size());

        return ResponseEntity
                .ok()
                .body(filesInS3Bucket);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> serveFile(@RequestParam String filename) {

        String bucketPath = "s3://" + bucket + "/";
        Resource s3Resource = resourceLoader.getResource(bucketPath + filename);
                
        String s3FileName = filename.substring(filename.lastIndexOf("/"));
        s3FileName = s3FileName.replace("/", "");
                
        logger.info("Downloading File: {} from S3", s3FileName);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+s3FileName+"\"")
                .body(s3Resource);
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam String filename) {
                        
        logger.info("Deleting File: {} from S3", filename);
        s3OperationService.deleteFile(filename);
        
        return ResponseEntity
                .accepted()
                .body("File Deleted Successfully");
    }

        
    @PostMapping("/save")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam String path, @RequestParam String fileName) {
            	
    	try {
			s3OperationService.saveFile(file, path, fileName);
		} catch (IOException e) {
			logger.error("Failed to upload file on S3");
			e.printStackTrace();
		}
    	
        return ResponseEntity
                .accepted()
                .body("File Stored Successfully!");
    }
    
    @GetMapping(path = "/search")
    public ResponseEntity<List<String>> searchS3Files(@RequestParam String pattern) throws IOException {

        List<String> filesInS3Bucket = s3OperationService.searchFile(pattern);
        logger.info("Number of Files in S3 bucket: {}", filesInS3Bucket.size());

        return ResponseEntity
                .ok()
                .body(filesInS3Bucket);
    }

}