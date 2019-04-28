package com.apl.auction.externalApi;

import java.io.File;
import java.io.InputStream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.apl.auction.helper.Constant;

public class S3ImageUpload {

	public String imageUpload(String uploadImagePath) {
		boolean isImageUploaded = false;

		BasicAWSCredentials creds = new BasicAWSCredentials(Constant.AccessKey, Constant.SecretKey);
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1)
				.withCredentials(new AWSStaticCredentialsProvider(creds)).build();

		String ImageURL = null;

		try {

			File file = new File(uploadImagePath);
			

			PutObjectResult response = s3Client.putObject(new PutObjectRequest(Constant.S3BucketName, file.getName(), file));

			ImageURL = String.valueOf(s3Client.getUrl(Constant.S3BucketName, // The S3 Bucket To Upload To
					file.getName()));

			System.out.println("ImageURL: " + ImageURL + " ,FileName: " + file.getName());

		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which " + "means the client encountered "
					+ "an internal error while trying to " + "communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
		return ImageURL;
	}

	public String imageUpload(InputStream uploadedInputStream, String fileName,long fileSize) {
		boolean isImageUploaded = false;

		BasicAWSCredentials creds = new BasicAWSCredentials(Constant.AccessKey, Constant.SecretKey);
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1)
				.withCredentials(new AWSStaticCredentialsProvider(creds)).build();

		String ImageURL = null;
		  ObjectMetadata metadata = new ObjectMetadata();
		    metadata.setContentLength(fileSize);
		    System.out.println(metadata.getContentLength());
		    
		try {
			PutObjectResult response = s3Client.putObject(new PutObjectRequest(Constant.S3BucketName,fileName,uploadedInputStream, metadata));

			ImageURL = String.valueOf(s3Client.getUrl(Constant.S3BucketName, // The S3 Bucket To Upload To
					fileName));

			System.out.println("ImageURL: " + ImageURL + " ,FileName: " + fileName);

		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which " + "means the client encountered "
					+ "an internal error while trying to " + "communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
		return ImageURL;
	}
}
