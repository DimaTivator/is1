package org.dimativator.is1.mappers;

import org.dimativator.is1.dto.PersonDto;
import org.dimativator.is1.model.Person;

public class PersonMapper {
    public static Person toEntity(PersonDto personDto) {
        return Person.builder()
                .id(personDto.getId())
                .name(personDto.getName())
                .coordinates(CoordinatesMapper.toEntity(personDto.getCoordinates()))
                .creationDate(personDto.getCreationDate())
                .eyeColor(personDto.getEyeColor())
                .hairColor(personDto.getHairColor())
                .location(LocationMapper.toEntity(personDto.getLocation()))
                .height(personDto.getHeight())
                .birthday(personDto.getBirthday())
                .nationality(personDto.getNationality())
                .user(UserMapper.toEntity(personDto.getUser()))
                .build();
    }

    public static PersonDto toDto(Person person) {
        return PersonDto.builder()
                .id(person.getId())
                .name(person.getName())
                .coordinates(CoordinatesMapper.toDto(person.getCoordinates()))
                .creationDate(person.getCreationDate())
                .eyeColor(person.getEyeColor())
                .hairColor(person.getHairColor())
                .location(LocationMapper.toDto(person.getLocation()))
                .height(person.getHeight())
                .birthday(person.getBirthday())
                .nationality(person.getNationality())
                .user(UserMapper.toDto(person.getUser()))
                .build();
    }
}
