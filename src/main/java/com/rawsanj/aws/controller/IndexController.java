package com.rawsanj.aws.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.rawsanj.aws.service.S3OperationService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by @author Sanjay Rawat on 7/7/17.
 */

@Controller
public class IndexController {

	Logger logger = LoggerFactory.getLogger(IndexController.class);
	
    private final S3OperationService s3OperationService;
    private final ResourceLoader resourceLoader;

    @Value("${aws.bucket.name}")
    private String bucket;
    
    @Autowired
    public IndexController(S3OperationService s3OperationService, ResourceLoader resourceLoader) {
        this.s3OperationService = s3OperationService;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) {

        try {
        	logger.info("Fetching all Files from S3");
			model.addAttribute("files", s3OperationService.getAllFiles()
			        .stream()
			        .collect(Collectors.toMap(f-> f, f -> {
			        	List<String> filesNFolders = new ArrayList<>();
			        	String[] folders = f.split("/");
			        	Arrays.asList(folders).forEach(filesNFolders::add);
			        	return filesNFolders;
			        })));
		} catch(IOException e) {
			logger.error("Failed to Connect to AWS S3 - Please check your AWS Keys");
			e.printStackTrace();
		}

        return "index";
    }
    
    @PostMapping("/save")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, 
    									@RequestParam String path, @RequestParam String fileName,
    										RedirectAttributes redirectAttributes) {
    	
    	try {
			s3OperationService.saveFile(file, path, fileName);
		} catch (IOException e) {
			logger.error("Failed to upload file on S3");
			e.printStackTrace();
		}

        return "redirect:/";
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
    
    @GetMapping("/delete")
    public String deleteFile(@RequestParam String filename) {
                        
        logger.info("Deleting File: {} from S3", filename);
        s3OperationService.deleteFile(filename);

        return "redirect:/";
    }
    
    @PostMapping("/search")
    public String searchFiles(@RequestParam String pattern, Model model) {
    	
    	try {
        	logger.info("Fetching Files from S3 for Pattern: {}", pattern);
			model.addAttribute("files", s3OperationService.searchFile(pattern)
			        .stream()
			        .collect(Collectors.toMap(f-> f, f -> {
			        	List<String> filesNFolders = new ArrayList<>();
			        	String[] folders = f.split("/");
			        	Arrays.asList(folders).forEach(filesNFolders::add);
			        	return filesNFolders;
			        })));
		} catch(IOException e) {
			logger.error("Failed to Connect to AWS S3 - Please check your AWS Keys");
			e.printStackTrace();
		}

        return "index";
    }

}
