package com.example.fileservice.controller;

import com.example.fileservice.dto.FileDto;
import com.example.fileservice.dto.UploadResponse;
import com.example.fileservice.repository.ClientFileRepository;
import com.example.fileservice.service.MinioService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.*;

@RestController
@RequestMapping("api/file")
public class FileController {

}