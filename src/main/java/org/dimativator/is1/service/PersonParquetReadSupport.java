package org.dimativator.is1.service;

import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.hadoop.api.ReadSupport;
import org.apache.parquet.io.api.RecordMaterializer;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;
import org.dimativator.is1.model.Person;

import java.util.Map;

public class PersonParquetReadSupport extends ReadSupport<Person> {
    private static final String SCHEMA = "message Person {\n" +
        "  optional binary name (UTF8);\n" +
        "  optional double coordinates_x;\n" +
        "  optional double coordinates_y;\n" +
        "  optional binary eyeColor (UTF8);\n" +
        "  optional binary hairColor (UTF8);\n" +
        "  optional double location_x;\n" +
        "  optional double location_y;\n" +
        "  optional double location_z;\n" +
        "  optional double height;\n" +
        "  optional binary birthday (UTF8);\n" +
        "  optional binary nationality (UTF8);\n" +
        "}";

    @Override
    public ReadContext init(Configuration configuration,
                          Map<String, String> keyValueMetaData,
                          MessageType fileSchema) {
        MessageType requestedSchema = MessageTypeParser.parseMessageType(SCHEMA);
        return new ReadContext(requestedSchema);
    }

    @Override
    public RecordMaterializer<Person> prepareForRead(Configuration configuration,
                                                    Map<String, String> keyValueMetaData,
                                                    MessageType messageType,
                                                    ReadContext readContext) {
        return new PersonRecordMaterializer();
    }
} 