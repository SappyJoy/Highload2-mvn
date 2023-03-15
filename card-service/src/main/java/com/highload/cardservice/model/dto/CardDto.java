package com.highload.cardservice.model.dto;

import com.highload.feign.dto.ContentDto;
import com.highload.feign.dto.PersonDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Getter
@Setter
@RequiredArgsConstructor
public class CardDto {
    @NotNull
    PersonDto person;
    @NotEmpty
    Collection<ContentDto> details;
}
