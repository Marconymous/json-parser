package handlers;

import exceptions.ElementTypeException;
import parser.JSONParser;

public interface ElementHandler {
    /**
     * Handles the JSON Object generation
     *
     * @return the value for the JSON Object
     */
    String handle(Object o) throws ElementTypeException, JSONParser.JsonSerializationException, ElementTypeException, JSONParser.JsonSerializationException;

    String getType();
}