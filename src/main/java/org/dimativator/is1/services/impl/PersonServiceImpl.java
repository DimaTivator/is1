package org.dimativator.is1.services.impl;

import org.dimativator.is1.dto.PersonDto;
import org.dimativator.is1.mappers.PersonMapper;
import org.dimativator.is1.model.*;
import org.dimativator.is1.repository.CoordinatesRepository;
import org.dimativator.is1.repository.LocationRepository;
import org.dimativator.is1.repository.PersonFilter;
import org.dimativator.is1.repository.PersonRepository;
import org.dimativator.is1.services.PersonService;
import org.dimativator.is1.services.PersonSpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final LocationRepository locationRepository;
    private final PersonSpecificationService personSpecificationService;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository,
                             CoordinatesRepository coordinatesRepository,
                             LocationRepository locationRepository,
                             PersonSpecificationService personSpecificationService) {
        this.personRepository = personRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.locationRepository = locationRepository;
        this.personSpecificationService = personSpecificationService;
    }

    @Override
    public List<PersonDto> getAllPersons(Pageable paging, PersonFilter personFilter) {
        final Specification<Person> spec = personSpecificationService.filterBy(personFilter);
        final Page<Person> personPage = personRepository.findAll(spec, paging);
        return personPage
                .stream()
                .map(PersonMapper::toDto)
                .peek(personDto -> personDto.setTotalPages(personPage.getTotalPages()))
                .toList();
    }

    private void setLocation(Person person) {
        final Location location;
        if (person.getLocation().getId() != null) {
            location = locationRepository.findById(person.getLocation().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));
        } else {
            location = locationRepository.findByXAndYAndZ(person.getLocation().getX(),
                            person.getLocation().getY(), person.getLocation().getZ())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));
        }
        person.setLocation(location);
    }

    private void setCoordinates(Person person) {
        final Coordinates coordinates;
        if (person.getCoordinates().getId() != null) {
            coordinates = coordinatesRepository.findById(person.getCoordinates().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coordinates not found"));
        } else {
            coordinates = coordinatesRepository.findByXAndY(person.getCoordinates().getX(), person.getCoordinates().getY())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coordinates not found"));
        }
        person.setCoordinates(coordinates);
    }

    @Override
    @Transactional
    public PersonDto createPerson(PersonDto personDto, User user) {
        checkUniqueCombination(personDto);
        final Person person = PersonMapper.toEntity(personDto);
        person.setUser(user);
        try {
            setLocation(person);
            setCoordinates(person);
        } catch (ResponseStatusException ignored) {
        }

        try {
            saveEmbedded(person);
        } catch (DataIntegrityViolationException ignored) {
        }

        return PersonMapper.toDto(personRepository.save(person));
    }

    @Override
    @Transactional
    public PersonDto updatePerson(PersonDto personDto) {
        checkUniqueCombination(personDto);
        final Person person = PersonMapper.toEntity(personDto);
        try {
            setLocation(person);
            setCoordinates(person);
        } catch (ResponseStatusException ignored) {
        }
        person.setCreationDate(personRepository.findById(personDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Object not found"))
                .getCreationDate());

        try {
            saveEmbedded(person);
        } catch (DataIntegrityViolationException ignored) {
        }

        return PersonMapper.toDto(personRepository.save(person));
    }

    private void saveEmbedded(Person person) {
        final Location location = person.getLocation();
        if (location != null) {
            locationRepository.save(location);
        }
        final Coordinates coordinates = person.getCoordinates();
        if (coordinates != null) {
            coordinatesRepository.save(coordinates);
        }
    }

    @Override
    public Integer countPersonsByEyeColor(Color color) {
        return personRepository.findAllByEyeColor(color).size();
    }

    @Override
    public Integer countPersonsByHairColor(Color color) {
        return personRepository.findAllByHairColor(color).size();
    }

    @Override
    public Integer countPersonsWithNationalityLessThan(Country country) {
        return personRepository.findAllWithNationalityLessThan(country).size();
    }

    @Override
    public Integer countPersonsWithNationalityEqualTo(Country country) {
        return personRepository.findAllWithNationalityEqualTo(country).size();
    }

    @Override
    public void deletePersonsByHeight(float height, User user) {
        if (user.getRole().equals(Role.ADMIN)) {
            personRepository.deleteByHeight(height);
        } else {
            personRepository.deleteByHeightAndUser(height, user.getId());
        }
    }

    @Override
    public void deletePersonById(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public void checkUser(long id, User user) {
        final Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Object not found"));
        if (person.getUser().equals(user) || user.getRole().equals(Role.ADMIN)) {
            return;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not owner of this object");
    }

    @Override
    public void checkUniqueCombination(PersonDto personDto) throws ResponseStatusException {
        List<Person> persons  = personRepository.findByNameAndBirthdayAndNationality(
            personDto.getName(), personDto.getBirthday(), personDto.getNationality()
        );

        if (persons.size() > 1 && personDto.getId() != null || !persons.isEmpty() && personDto.getId() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Person with this combination of Name, Birthday and Nationality already exists"
            );
        }
    }
}
