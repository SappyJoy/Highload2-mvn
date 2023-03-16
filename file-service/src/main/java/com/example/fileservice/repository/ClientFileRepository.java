package com.example.fileservice.repository;

import com.example.fileservice.model.ClientFile;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface ClientFileRepository extends CrudRepository<ClientFile, Long>{

}

