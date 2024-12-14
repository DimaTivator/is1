package org.dimativator.is1.service;

import org.apache.parquet.io.api.GroupConverter;
import org.apache.parquet.io.api.RecordMaterializer;
import org.dimativator.is1.model.Person;

public class PersonRecordMaterializer extends RecordMaterializer<Person> {
    private final PersonConverter root;

    public PersonRecordMaterializer() {
        this.root = new PersonConverter();
    }

    @Override
    public Person getCurrentRecord() {
        return root.getCurrentPerson();
    }

    @Override
    public GroupConverter getRootConverter() {
        return root;
    }
} 