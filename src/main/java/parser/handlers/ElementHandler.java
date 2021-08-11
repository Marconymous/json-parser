package parser.handlers;

import exceptions.ElementTypeException;
import exceptions.JsonSerializationException;

public interface ElementHandler {
    /**
     * Handles the JSON Object generation
     *
     * @return the value for the JSON Object
     */
    String handle(Object o) throws ElementTypeException, JsonSerializationException;

    String getType();

    boolean canHandle(Object o);
}