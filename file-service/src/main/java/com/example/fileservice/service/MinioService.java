package com.example.fileservice.service;

import com.example.fileservice.dto.FileDto;
import com.example.fileservice.dto.UploadResponse;
import io.minio.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Service
public
class MinioService(
        MinioClient minioClient,

        @Value("${minio.bucket}")
        String defaultBucketName
        ) {

    Logger logger;

    }
}
