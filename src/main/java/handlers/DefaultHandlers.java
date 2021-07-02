package handlers;

import annotations.JsonSerializableObject;
import enums.JSONType;
import exceptions.ElementTypeException;
import exceptions.JsonSerializationException;
import parser.JSONParser;

import java.util.Iterator;
import java.util.List;

/**
 * Default handlers for datatypes in JSON
 */
public class DefaultHandlers {
    /**
     * Handles Integers
     */
    public static class IntegerHandler implements ElementHandler {
        /**
         * Converts an Integer into a String for a JSON Object
         *
         * @param o the Object to convert
         * @return the Converted JSON String
         * @throws ElementTypeException if the Object cannot be converted
         */
        @Override
        public String handle(Object o) throws ElementTypeException {
            if (!(o instanceof Integer) && !(o instanceof Long) && !(o instanceof Byte) && !(o instanceof Short))
                throw new ElementTypeException("Type (" + o.getClass().getSimpleName() + ") cannot be casted to Integer Value!");
            return String.valueOf(((Number) o).longValue());
        }

        @Override
        public String getType() {
            return JSONType.INTEGER.name();
        }

        /**
         * @param o the object to check
         * @return if the object is instance of a Integer Number
         */
        @Override
        public boolean canHandle(Object o) {
            return (o instanceof Integer || o instanceof Long || o instanceof Byte || o instanceof Short);
        }
    }

    public static class StringHandler implements ElementHandler {

        /**
         * Converts a String into a String for a JSON Object
         *
         * @param o the Object to convert
         * @return the Converted JSON String
         * @throws ElementTypeException if the Object cannot be converted
         */
        @Override
        public String handle(Object o) throws ElementTypeException {
            if (!(o instanceof String))
                throw new ElementTypeException("Type (" + o.getClass().getSimpleName() + ") cannot be casted to String Value!");
            return "\"" + o + "\"";
        }

        @Override
        public String getType() {
            return JSONType.STRING.name();
        }

        /**
         * @param o the object to check
         * @return if the Object is instance of a String
         */
        @Override
        public boolean canHandle(Object o) {
            return o instanceof String;
        }
    }

    public static class FloatHandler implements ElementHandler {

        /**
         * Converts a Float into a String for a JSON Object
         *
         * @param o the Object to convert
         * @return the Converted JSON String
         * @throws ElementTypeException if the Object cannot be converted
         */
        @Override
        public String handle(Object o) throws ElementTypeException {
            if (!(o instanceof Float))
                throw new ElementTypeException("Type (" + o.getClass().getSimpleName() + ") cannot be casted to Float Value!");
            return String.valueOf(((Float) o).floatValue());
        }

        @Override
        public String getType() {
            return JSONType.FLOAT.name();
        }

        /**
         * @param o the object to check
         * @return if the Object is an instance of Float
         */
        @Override
        public boolean canHandle(Object o) {
            return o instanceof Float;
        }

    }

    public static class DoubleHandler implements ElementHandler {

        /**
         * Converts a Double into a String for a JSON Object
         *
         * @param o the Object to convert
         * @return the Converted JSON String
         * @throws ElementTypeException if the Object cannot be converted
         */
        @Override
        public String handle(Object o) throws ElementTypeException {
            if (!(o instanceof Double))
                throw new ElementTypeException("Type (" + o.getClass().getSimpleName() + ") cannot be casted to Double Value!");
            return String.valueOf(((Double) o).doubleValue());
        }

        @Override
        public String getType() {
            return JSONType.DOUBLE.name();
        }

        /**
         * @param o the object to check
         * @return if the Object is an instance of Double
         */
        @Override
        public boolean canHandle(Object o) {
            return o instanceof Double;
        }
    }

    public static class BooleanHandler implements ElementHandler {

        /**
         * Converts a Boolean into a String for a JSON Object
         *
         * @param o the Object to convert
         * @return the Converted JSON String
         * @throws ElementTypeException if the Object cannot be converted
         */
        @Override
        public String handle(Object o) throws ElementTypeException {
            if (!(o instanceof Boolean))
                throw new ElementTypeException("Type (" + o.getClass().getSimpleName() + ") cannot be casted to Boolean Value!");
            return String.valueOf(o);
        }

        @Override
        public String getType() {
            return JSONType.BOOLEAN.name();
        }

        /**
         * @param o the object to check
         * @return if the Object is an instance of Boolean
         */
        @Override
        public boolean canHandle(Object o) {
            return o instanceof Boolean;
        }
    }

    public static class ListHandler implements ElementHandler {

        /**
         * Converts a List into a String for a JSON Object
         *
         * @param o the Object to convert
         * @return the Converted JSON String
         * @throws ElementTypeException if the Object cannot be converted
         */
        @Override
        public String handle(Object o) throws ElementTypeException, JsonSerializationException {
            if (!(o instanceof List))
                throw new ElementTypeException("Type (" + o.getClass().getSimpleName() + ") cannot be casted to List Value!");

            JSONParser parser = new JSONParser();
            StringBuilder out = new StringBuilder("[");
            Iterator<?> iter = ((List<?>) o).iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                out.append(parser.objectToJSON(obj));

                if (iter.hasNext()) out.append(", ");
            }
            out.append("]");

            return out.toString();
        }

        @Override
        public String getType() {
            return JSONType.LIST.name();
        }

        /**
         * @param o the object to check
         * @return if the Object is an instance of List
         */
        @Override
        public boolean canHandle(Object o) {
            return o instanceof List;
        }
    }

    public static class JSONAnnotatedHandler implements ElementHandler {
        /**
         * Converts a JSONAnnotated Object into a String for a JSON Object
         *
         * @param o the Object to convert
         * @return the Converted JSON String
         * @throws ElementTypeException if the Object cannot be converted
         */
        @Override
        public String handle(Object o) throws ElementTypeException, JsonSerializationException {
            JSONParser parser = new JSONParser();
            return parser.objectToJSON(o);
        }

        @Override
        public String getType() {
            return String.valueOf(JSONType.JSON_ANNOTATED);
        }

        /**
         * @param o the object to check
         * @return if the Object can be JSONSerialized
         */
        @Override
        public boolean canHandle(Object o) {
            return o.getClass().isAnnotationPresent(JsonSerializableObject.class);
        }
    }

    public static class EnumerationHandler implements ElementHandler {

        /**
         * Converts an Enumerated Object into a String for a JSON Object
         *
         * @param o the Object to convert
         * @return the Converted JSON String (Enum Name)
         * @throws ElementTypeException if the Object cannot be converted
         */
        @Override
        public String handle(Object o) throws ElementTypeException, JsonSerializationException {
            if (!o.getClass().isEnum())
                throw new ElementTypeException("Type (" + o.getClass().getSimpleName() + ") cannot be casted to Enumerated Value!");
            return ((Enum<?>) o).name();
        }

        @Override
        public String getType() {
            return JSONType.ENUMERATED_STRING.name();
        }

        /**
         * @param o the object to check
         * @return if the Object is an Enum
         */
        @Override
        public boolean canHandle(Object o) {
            return o.getClass().isEnum();
        }
    }

    public static class EnumerationHandlerOrdinal implements ElementHandler {

        /**
         * Converts a JSONAnnotated Object into a String for a JSON Object
         *
         * @param o the Object to convert
         * @return the Converted JSON String
         * @throws ElementTypeException if the Object cannot be converted (ordinal)
         */
        @Override
        public String handle(Object o) throws ElementTypeException, JsonSerializationException {
            if (!o.getClass().isEnum())
                throw new ElementTypeException("Type (" + o.getClass().getSimpleName() + ") cannot be casted to Enumerated Value!");
            return String.valueOf(((Enum<?>) o).ordinal());
        }

        @Override
        public String getType() {
            return JSONType.ENUMERATED_ORDINAL.name();
        }

        /**
         * @param o the object to check
         * @return if the Object is an Enum
         */
        @Override
        public boolean canHandle(Object o) {
            return o.getClass().isEnum();
        }
    }
}