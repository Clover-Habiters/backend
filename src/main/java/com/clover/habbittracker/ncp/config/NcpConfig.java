package com.clover.habbittracker.ncp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class NcpConfig {
	private final String endPoint;
	private final String regionName;
	private final String accessKey;
	private final String secretKey;

	public NcpConfig(@Value("${NCP_API_ACCESS_KEY}") String accessKey,
		@Value("${NCP_SECRET_KEY}") String secretKey) {
		this.endPoint = "https://kr.object.ncloudstorage.com";
		this.regionName = "kr-standard";
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	@Bean
	public AmazonS3 NcpStorageServer() {
		return AmazonS3ClientBuilder.standard()
			.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
			.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
			.build();
	}
}
