package org.dimativator.is1.service;

import org.apache.parquet.io.api.Binary;
import org.apache.parquet.io.api.Converter;
import org.apache.parquet.io.api.GroupConverter;
import org.apache.parquet.io.api.PrimitiveConverter;
import org.dimativator.is1.model.Person;
import org.dimativator.is1.model.Coordinates;
import org.dimativator.is1.model.Location;
import org.dimativator.is1.model.Color;
import org.dimativator.is1.model.Country;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PersonConverter extends GroupConverter {
    private Person currentPerson;
    private Coordinates coordinates;
    private Location location;

    @Override
    public Converter getConverter(int fieldIndex) {
        return new PrimitiveConverter() {
            @Override
            public void addBinary(Binary value) {
                if (value == null) return;
                String stringValue = value.toStringUsingUTF8();
                switch(fieldIndex) {
                    case 0: // name
                        currentPerson.setName(stringValue);
                        break;
                    case 3: // eyeColor
                        currentPerson.setEyeColor(Color.valueOf(stringValue));
                        break;
                    case 4: // hairColor
                        currentPerson.setHairColor(Color.valueOf(stringValue));
                        break;
                    case 9: // birthday
                        currentPerson.setBirthday(LocalDateTime.parse(stringValue, 
                            DateTimeFormatter.ISO_DATE_TIME).atZone(ZoneId.systemDefault()));
                        break;
                    case 10: // nationality
                        currentPerson.setNationality(Country.valueOf(stringValue));
                        break;
                }
            }

            @Override
            public void addDouble(double value) {
                switch(fieldIndex) {
                    case 1: // coordinates.x
                        coordinates.setX((long) value);
                        break;
                    case 2: // coordinates.y
                        coordinates.setY(value);
                        break;
                    case 5: // location.x
                        location.setX((long) value);
                        break;
                    case 6: // location.y
                        location.setY((float) value);
                        break;
                    case 7: // location.z
                        location.setZ((float) value);
                        break;
                    case 8: // height
                        currentPerson.setHeight((float) value);
                        break;
                }
            }
        };
    }

    @Override
    public void start() {
        currentPerson = new Person();
        coordinates = new Coordinates();
        location = new Location();
        currentPerson.setCoordinates(coordinates);
        currentPerson.setLocation(location);
    }

    @Override
    public void end() {
        // Nothing to do here
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }
} 