package com.example.fileservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "clientfile")
public class ClientFile{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;

        @Column(name = "useremail")
        String userEmail;
        @Column(name = "filename")
        String filename;
}
