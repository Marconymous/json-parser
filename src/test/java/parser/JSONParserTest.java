package parser;

import annotations.JsonElement;
import annotations.JsonSerializableObject;
import enums.JSONType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        } catch (JSONParser.JsonSerializationException e) {
            e.printStackTrace();
        }

        assertEquals("{\"person\":{\"name\":\"Marck der Nyme\",\"age\":69},\"address\":\"Kerberstrasse 420\"}", s);
    }

    @org.junit.jupiter.api.Test
    void testListToJSON() {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("Marc Andri Fuchs", 16));
        persons.add(new Person("Tim Jan Irmler", 18));

        JSONParser parser = new JSONParser();
        String s = null;
        try {
            s = parser.listToJSON(persons, Person.class);
        } catch (JSONParser.JsonSerializationException e) {
            e.printStackTrace();
        }

        assertEquals("\"persons\":[{\"name\":\"Marc Andri Fuchs\",\"age\":16},{\"name\":\"Tim Jan Irmler\",\"age\":18}]", s);
    }

    @JsonSerializableObject(listName = "persons")
    private record Person(@JsonElement(type = JSONType.STRING) String name,
                          @JsonElement(type = JSONType.INTEGER) long age) {
    }

    @JsonSerializableObject(listName = "addresses")
    private record Address(@JsonElement(type = JSONType.JSON_ANNOTATED) Person person,
                           @JsonElement(type = JSONType.STRING) String address) {

    }
}