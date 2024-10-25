package org.dimativator.is1.repository;

import org.dimativator.is1.dto.PersonDto;
import org.dimativator.is1.model.Color;
import org.dimativator.is1.model.Country;
import org.dimativator.is1.model.Person;
import org.dimativator.is1.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("select p from Person p where p.eyeColor = :color")
    List<Person> findAllByEyeColor(@Param("color") Color color);

    @Query("select p from Person p where p.hairColor = :color")
    List<Person> findAllByHairColor(@Param("color") Color color);

    @Query("select p from Person p where p.nationality < :country")
    List<Person> findAllWithNationalityLessThan(@Param("country") Country country);

    @Query("select p from Person p where p.nationality < :country")
    List<Person> findAllWithNationalityGreaterThan(@Param("country") Country country);

    @Transactional
    @Modifying
    @Query("delete from Person p where p.height = :height")
    void deleteByHeight(@Param("height") float height);
}
