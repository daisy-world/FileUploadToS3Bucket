package com.app.FileUploadToS3Bucket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.FileUploadToS3Bucket.entity.DocumentDetails;



@Repository
public interface DocumentDetailsRepository extends JpaRepository<DocumentDetails, String> {
	
	
	

}
