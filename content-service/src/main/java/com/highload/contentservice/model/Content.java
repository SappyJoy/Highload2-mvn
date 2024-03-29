package com.highload.contentservice.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "`CONTENT`")
public class Content extends com.highload.feign.model.Content implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "content_id")
    private UUID contentId;

    @NotBlank(message = "name of part cannot be empty")
    @Column(name = "name")
    private String name;

    @Min(value = 1)
    @Column(name = "cost")
    private Long cost;
}