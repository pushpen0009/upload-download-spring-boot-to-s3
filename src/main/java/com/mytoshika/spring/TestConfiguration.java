package com.mytoshika.spring;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {
    public static final String ACCESS_KEY = "XXXXXXXXXXXXXXXX"; //""AKIAI6CSE2UG2KNXF2VA";
    public static final String SECRET_KEY = "XXXXXXXXXXXXXXXXXXXX"; //""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";

    @Bean
    public AmazonS3 amazonS3(AWSCredentials awsCredentials) {
        AmazonS3 s3client =
                AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                        .withRegion(Regions.US_WEST_1)
                        .build();

        return s3client;
    }

    @Bean
    public AWSCredentials awsCredentials() {
        AWSCredentials credentials =
                new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        return credentials;
    }
}
