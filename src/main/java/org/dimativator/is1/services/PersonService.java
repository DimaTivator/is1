package org.dimativator.is1.services;

import org.dimativator.is1.dto.PersonDto;
import org.dimativator.is1.model.Color;
import org.dimativator.is1.model.Country;
import org.dimativator.is1.model.User;
import org.dimativator.is1.repository.PersonFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonService {
    List<PersonDto> getAllPersons(Pageable paging, PersonFilter personFilter);

    Integer countPersonsByEyeColor(Color color);

    Integer countPersonsByHairColor(Color color);

    Integer countPersonsWithNationalityLessThan(Country country);

    Integer countPersonsWithNationalityEqualTo(Country country);

    void deletePersonsByHeight(float height, User user);

    void deletePersonById(Long id);

    PersonDto createPerson(PersonDto personDto, User user);

    PersonDto updatePerson(PersonDto personDto);

    void checkUser(long id, User user);
}
