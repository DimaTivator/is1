package org.dimativator.is1.services;

import org.dimativator.is1.model.Person;
import org.dimativator.is1.repository.PersonFilter;
import org.springframework.data.jpa.domain.Specification;

public interface PersonSpecificationService {
    Specification<Person> filterBy(PersonFilter personFilter);
}
