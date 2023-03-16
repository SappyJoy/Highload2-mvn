package com.example.fileservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.web.multipart.MultipartFile;
import java.io.Serializable;



@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDto implements Serializable{
    String title;
    String description;
    MultipartFile file;
    String url;
    Long size;
    String filename;
}


