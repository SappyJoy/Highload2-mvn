package com.example.fileservice.config;

import io.minio.MinioClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.util.concurrent.TimeUnit;


@Configuration
class MinioConfig {
    @Value("${minio.access.name}")
    String accessKey;

    @Value("${minio.access.secret}")
    String accessSecret;

    @Value("${minio.url}")
    String minioUrl;

    @Bean
    public void generateMinioClient(){
    try{
        OkHttpClient okhttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.MINUTES)
                .build();
        MinioClient.builder()
                .endpoint(minioUrl)
                .httpClient(okhttpClient)
                .credentials(accessKey, accessSecret)
                .build();

    }catch(RuntimeException e) {throw new RuntimeException(e.getMessage());}
    }

}