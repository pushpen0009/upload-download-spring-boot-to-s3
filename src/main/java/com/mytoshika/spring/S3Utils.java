package com.mytoshika.spring;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class S3Utils {

	@Autowired
	private AmazonS3 s3Client;

	@Value("${aws.s3.bucket.name}")
	private String bucketName;

	public void uploadFileFromS3(String key, InputStream is) throws IOException {
		ObjectMetadata metadata = new ObjectMetadata();
		byte[] bytes = IOUtils.toByteArray(is);
		metadata.setContentLength(bytes.length);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		this.s3Client.putObject(bucketName, key, byteArrayInputStream, metadata);
	}

	public InputStream downloadFileFromS3(String key) throws IOException {
		S3Object s3object = this.s3Client.getObject(bucketName, key);
		S3ObjectInputStream inputStream = s3object.getObjectContent();
		return inputStream;
	}

}
