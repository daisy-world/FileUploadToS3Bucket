package com.app.FileUploadToS3Bucket.service;

import org.springframework.web.multipart.MultipartFile;

import com.app.FileUploadToS3Bucket.entity.DocumentDetails;


public interface DocumentService {


	
	public String uploadFileToS3Bucket(MultipartFile multipartFile);

	public DocumentDetails getDocumentById(String docId);

}
