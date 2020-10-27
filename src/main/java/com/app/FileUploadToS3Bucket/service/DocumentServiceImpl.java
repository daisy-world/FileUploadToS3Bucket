package com.app.FileUploadToS3Bucket.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.app.FileUploadToS3Bucket.entity.DocumentDetails;
import com.app.FileUploadToS3Bucket.repository.DocumentDetailsRepository;


@Service
public class DocumentServiceImpl implements DocumentService {
	
	@Autowired
	DocumentDetailsRepository documentDetailsRepository;
	
	
	
	@Autowired
    private AmazonS3 amazonS3;
	
	
	@Value("${aws.s3.bucket}")
	private String s3bucket;	
	
	


	

	@Override
	public String uploadFileToS3Bucket(MultipartFile multipartFile) {
		
		
		DocumentDetails documentDetails =null;
		
		 try {
	            File file = convertMultiPartFileToFile(multipartFile);
	          
	        	String orgId  ="ORG00002";
	    		String userId ="USER00002";
	        	String docCatagory ="education";	
	    		String fileName = file.getName();
 
	        	String path = orgId+"/"+userId+"/"+docCatagory+"/"+fileName;
	            uploadFile(s3bucket,path, file);

				System.out.println("Data :"+path);
				
				//insert to document table //
				DocumentDetails document = new DocumentDetails();
				document.setDocument_name(multipartFile.getOriginalFilename());
				document.setPath(path);
				document.setType(multipartFile.getContentType());
				documentDetails=	documentDetailsRepository.save(document);
	            file.delete();  // To remove the file locally created in the project folder.
	        } catch (final AmazonServiceException ex) {
	           System.out.println("error message ....... "  +  ex.getErrorMessage());
	        }		
		 
		 
		 return documentDetails.getDocument_id();
	}





	private void uploadFile(String bucketName,String filePath,File file) {
		
    	
		amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file)
		            .withCannedAcl(CannedAccessControlList.PublicRead));
	}





	private File convertMultiPartFileToFile(MultipartFile multipartFile) {
		final File file = new File(multipartFile.getOriginalFilename());
        try {
            FileOutputStream outputStream = new FileOutputStream(file) ;
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
        	System.out.println("Error converting the multi-part file to file= "+ex.getMessage());
        }
        return file;
	}





	@Override
	public DocumentDetails getDocumentById(String docId) {
		DocumentDetails docs = null;
		Optional<DocumentDetails> documentDetails = documentDetailsRepository.findById(docId);
		
		if(documentDetails.isPresent()) {
			
			 docs = documentDetails.get();
		}
		
		return docs;
	}
	
	
	
	

}
