package com.highload.personservice.service;


import com.highload.cardservice.service.CardService;
import com.highload.contentservice.model.Content;
import com.highload.contentservice.service.ContentService;
import com.highload.feign.dto.PersonDto;
import com.highload.feign.exceptions.NoSuchEntityException;
import com.highload.feign.model.Card;
import com.highload.marketservice.service.MarketService;
import com.highload.personservice.model.Person;
import com.highload.personservice.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Repository
@Component
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final ContentService contentService;
    private final CardService cardService;
    private final MarketService marketService;

    public List<PersonDto> getAllPersons(Integer pageSize, Integer pageNum) {
        Pageable page = PageRequest.of(pageNum, pageSize);
        return personRepository
                .findAll(page)
                .stream()
                .map(v -> modelMapper.map(v, PersonDto.class))
                .toList();
    }

    public PersonDto getPerson(UUID personId) {
        return modelMapper.map(getPersonById(personId), PersonDto.class);
    }

    @SneakyThrows
    public com.highload.feign.model.Person getPersonById(UUID personId) {
        return personRepository
                .findPersonByPersonId(personId)
                .orElseThrow(() -> new NoSuchEntityException("No such person"));
    }


    @Transactional
    public void deletePerson(UUID personId) {
        personRepository.deletePersonByPersonId(personId);
    }

    @Transactional
    public void buyContent(UUID contentId, UUID personId) {
        Content content = contentService.getContentById(contentId);
        com.highload.feign.model.Person person = getPersonById(personId);
        person.setBalance(person.getBalance() - content.getCost());
        personRepository.save(person);
        Card card = person.getCard();
        card.getDetails().add(content);
        cardService.insertCard(card);
    }

    public PersonDto updatePerson(PersonDto personDto, UUID personId) {
        Person oldPerson = getPersonById(personId);
        modelMapper.map(modelMapper.map(personDto, Person.class), oldPerson);
        return modelMapper.map(personRepository.save(oldPerson), PersonDto.class);
    }
}