package parser;

import parser.annotations.JsonField;
import parser.annotations.JsonSerializableObject;
import parser.enums.JSONType;
import parser.enums.ParserResponseType;
import exceptions.JsonSerializationException;
import formatter.JSONFormatter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JSONParserTest {

    @org.junit.jupiter.api.Test
    void testObjectToJSON() {
        JSONParser parser = new JSONParser();
        Address address = new Address(new Person("Marck der Nyme", 69), "Kerberstrasse 420");
        String s = null;
        try {
            s = parser.objectToJSON(address);
        } catch (JsonSerializationException e) {
            e.printStackTrace();
        }

        assertEquals("{\"address\":\"Kerberstrasse 420\",\"person\":{\"name\":\"Marck der Nyme\",\"age\":69}}", s);
    }

    @org.junit.jupiter.api.Test
    void testListToJSON() {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("Marc Andri Fuchs", 16));
        persons.add(new Person("Tim Jan Irmler", 18));

        JSONParser parser = new JSONParser();
        String s = null;
        try {
            s = parser.listToJSON(persons, Person.class, ParserResponseType.OBJECT_LIST);
        } catch (JsonSerializationException e) {
            e.printStackTrace();
        }

        System.out.println(JSONFormatter.format(s));

        assertEquals("{\"persons\":[{\"name\":\"Marc Andri Fuchs\",\"age\":16},{\"name\":\"Tim Jan Irmler\",\"age\":18}]}", s);
    }

    @Test
    void format() {
        JSONParser parser = new JSONParser();
        try {
            String json = parser.objectToJSON(new Person("Marc[] Andri, Fuchs{}", 16));
            System.out.println(JSONFormatter.format(json));
        } catch (JsonSerializationException e) {
            e.printStackTrace();
        }
    }

    @JsonSerializableObject(listName = "persons")
    private record Person(@JsonField(type = JSONType.STRING) String name,
                          @JsonField(type = JSONType.INTEGER) long age) {
    }

    @JsonSerializableObject(listName = "addresses")
    private record Address(@JsonField(type = JSONType.JSON_ANNOTATED) Person person,
                           @JsonField(type = JSONType.STRING) String address) {
    }
}