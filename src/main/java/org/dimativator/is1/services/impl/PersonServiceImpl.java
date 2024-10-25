package org.dimativator.is1.services.impl;

import org.dimativator.is1.dto.PersonDto;
import org.dimativator.is1.mappers.PersonMapper;
import org.dimativator.is1.model.Color;
import org.dimativator.is1.model.Country;
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

    @Override
    public void savePerson(PersonDto personDto) {
        personRepository.save(PersonMapper.toEntity(personDto));
    }

    @Override
    public List<PersonDto> getPersonsByEyeColor(Color color) {
        return personRepository.findAllByEyeColor(color)
                .stream()
                .map(PersonMapper::toDto)
                .toList();
    }

    @Override
    public List<PersonDto> getPersonsByHairColor(Color color) {
        return personRepository.findAllByHairColor(color)
                .stream()
                .map(PersonMapper::toDto)
                .toList();
    }

    @Override
    public List<PersonDto> getPersonsWithNationalityLessThan(Country country) {
        return personRepository.findAllWithNationalityLessThan(country)
                .stream()
                .map(PersonMapper::toDto)
                .toList();
    }

    @Override
    public List<PersonDto> getPersonsWithNationalityGreaterThan(Country country) {
        return personRepository.findAllWithNationalityGreaterThan(country)
                .stream()
                .map(PersonMapper::toDto)
                .toList();
    }

    @Override
    public void deletePersonsByHeight(float height) {
        personRepository.deleteByHeight(height);
    }
}
