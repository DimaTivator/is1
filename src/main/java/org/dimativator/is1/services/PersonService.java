package org.dimativator.is1.services;

import org.dimativator.is1.dto.PersonDto;
import org.dimativator.is1.model.Color;
import org.dimativator.is1.model.Country;

import java.util.List;

public interface PersonService {
    List<PersonDto> getAllPersons();
    List<PersonDto> getPersonsByEyeColor(Color color);
    List<PersonDto> getPersonsByHairColor(Color color);
    List<PersonDto> getPersonsWithNationalityLessThan(Country country);
    List<PersonDto> getPersonsWithNationalityGreaterThan(Country country);
    void deletePersonsByHeight(float height);
    void savePerson(PersonDto personDto);
}
