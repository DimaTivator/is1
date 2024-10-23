package org.dimativator.is1.services.impl;

import org.dimativator.is1.dto.PersonDto;
import org.dimativator.is1.mappers.PersonMapper;
import org.dimativator.is1.repository.PersonRepository;
import org.dimativator.is1.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<PersonDto> getAllPersons() {
        return personRepository.findAll()
                .stream()
                .map(PersonMapper::toDto)
                .toList();
    }
}
