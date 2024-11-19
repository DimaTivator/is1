package org.dimativator.is1.services.impl;

import org.dimativator.is1.model.*;
import org.dimativator.is1.repository.PersonFilter;
import org.dimativator.is1.services.PersonSpecificationService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PersonSpecificationServiceImpl implements PersonSpecificationService {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String COORDINATES = "coordinates";
    private static final String CREATION_DATE = "creationDate";
    private static final String EYE_COLOR = "eyeColor";
    private static final String HAIR_COLOR = "hairColor";
    private static final String LOCATION = "location";
    private static final String HEIGHT = "height";
    private static final String BIRTHDAY = "birthday";
    private static final String NATIONALITY = "nationality";
    private static final String USER = "user";

    @Override
    public Specification<Person> filterBy(PersonFilter personFilter) {
        return Specification
                .where(hasId(personFilter.id()))
                .and(hasName(personFilter.name()))
                .and(hasCoordinates(personFilter.coordinates()))
                .and(hasCreationDate(personFilter.creationDate()))
                .and(hasEyeColor(personFilter.eyeColor()))
                .and(hasHairColor(personFilter.hairColor()))
                .and(hasLocation(personFilter.location()))
                .and(hasHeight(personFilter.height()))
                .and(hasBirthday(personFilter.birthday()))
                .and(hasNationality(personFilter.nationality()))
                .and(hasUser(personFilter.user()));
    }

    private Specification<Person> hasId(Long id) {
        return ((root, query, cb) -> id == null ? cb.conjunction() : cb.equal(root.get(ID), id));
    }

    private Specification<Person> hasName(String name) {
        return ((root, query, cb) -> name == null ? cb.conjunction() : cb.equal(root.get(NAME), name));
    }

    private Specification<Person> hasCoordinates(Coordinates coordinates) {
        return ((root, query, cb) -> coordinates == null ? cb.conjunction() : cb.equal(root.get(COORDINATES), coordinates));
    }

//    private Specification<Person> hasCreationDate(java.time.LocalDateTime creationDate) {
//        return ((root, query, cb) -> creationDate == null ? cb.conjunction() : cb.equal(root.get(CREATION_DATE), creationDate));
//    }

    // compare only date, without time
    private Specification<Person> hasCreationDate(java.time.LocalDateTime creationDate) {
        return ((root, query, cb) ->
                creationDate == null ? cb.conjunction() : cb.equal(cb.function(
                                "DATE",
                                java.sql.Date.class,
                                root.get(CREATION_DATE)
                        ),
                        java.sql.Date.valueOf(creationDate.toLocalDate())));
    }

    private Specification<Person> hasEyeColor(Color eyeColor) {
        return ((root, query, cb) -> eyeColor == null ? cb.conjunction() : cb.equal(root.get(EYE_COLOR), eyeColor));
    }

    private Specification<Person> hasHairColor(Color hairColor) {
        return ((root, query, cb) -> hairColor == null ? cb.conjunction() : cb.equal(root.get(HAIR_COLOR), hairColor));
    }

    private Specification<Person> hasLocation(Location location) {
        return ((root, query, cb) -> location == null ? cb.conjunction() : cb.equal(root.get(LOCATION), location));
    }

    private Specification<Person> hasHeight(Float height) {
        return ((root, query, cb) -> height == null ? cb.conjunction() : cb.equal(root.get(HEIGHT), height));
    }

    private Specification<Person> hasBirthday(java.time.ZonedDateTime birthday) {
        return ((root, query, cb) -> birthday == null ? cb.conjunction() : cb.equal(root.get(BIRTHDAY), birthday));
    }

    private Specification<Person> hasNationality(Country nationality) {
        return ((root, query, cb) -> nationality == null ? cb.conjunction() : cb.equal(root.get(NATIONALITY), nationality));
    }

    private Specification<Person> hasUser(User user) {
        return ((root, query, cb) -> user == null ? cb.conjunction() : cb.equal(root.get(USER), user));
    }
}
