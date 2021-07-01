package parser;

import annotations.JsonElement;
import annotations.JsonSerializableObject;
import enums.JSONType;
import exceptions.ElementTypeException;
import handlers.DefaultHandlers;
import handlers.ElementHandler;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class JSONParser {
    private List<ElementHandler> handlers = null;

    public JSONParser() {
        handlers = new ArrayList<>();

        handlers.add(new DefaultHandlers.ListHandler());
        handlers.add(new DefaultHandlers.StringHandler());
        handlers.add(new DefaultHandlers.IntegerHandler());
        handlers.add(new DefaultHandlers.DoubleHandler());
        handlers.add(new DefaultHandlers.FloatHandler());
        handlers.add(new DefaultHandlers.BooleanHandler());
        handlers.add(new DefaultHandlers.JSONAnnotatedHandler());
        handlers.add(new DefaultHandlers.EnumerationHandler());
        handlers.add(new DefaultHandlers.EnumerationHandlerOrdinal());
    }

    public JSONParser(List<ElementHandler> handlers) {
        this.handlers = handlers;
    }

    private void checkIfSerializable(Object object) throws JsonSerializationException {
        if (Objects.isNull(object)) throw new JsonSerializationException("The object to serialize is null");

        Class<?> clazz = object.getClass();
        if (!clazz.isAnnotationPresent(JsonSerializableObject.class)) {
            throw new JsonSerializationException("The class " + clazz.getName() + " is not annotated with JsonSerializableObject");
        }
    }

    private String getJsonString(Object object) throws JsonSerializationException, IllegalAccessException, ElementTypeException {
        Class<?> clazz = object.getClass();
        StringBuilder jsonString = new StringBuilder();
        ArrayList<Field> fields = new ArrayList<>();

        Iterator<Field> fieldIterator = Arrays.stream(clazz.getDeclaredFields()).iterator();
        while (fieldIterator.hasNext()) {
            Field field = fieldIterator.next();
            field.setAccessible(true);
            if (field.isAnnotationPresent(JsonElement.class)) {
                fields.add(field);
            }
        }


        Iterator<Field> iter = fields.iterator();
        while (iter.hasNext()) {
            Field f = iter.next();

            jsonString.append("\"").append(f.getName()).append("\":"); // Define Parameter
            Object o = f.get(object);

            JSONType type = f.getAnnotation(JsonElement.class).type();
            if (type != JSONType.CUSTOM) {
                for (ElementHandler eh : handlers) {
                    if (eh.getType().equals(type.name())) {
                        jsonString.append(eh.handle(o));
                        break;
                    }
                }
            }

            if (iter.hasNext()) jsonString.append(",");
        }

        return "{" + jsonString + "}";
    }

    public String objectToJSON(Object object) throws JsonSerializationException {
        try {
            checkIfSerializable(object);
            return getJsonString(object);
        } catch (Exception e) {
            throw new JsonSerializationException(e.getMessage());
        }
    }

    public String listToJSON(List<?> list, Class<?> clazz) throws JsonSerializationException {
        if (!clazz.isAnnotationPresent(JsonSerializableObject.class))
            throw new JsonSerializationException("The Type " + clazz.getName() + " is not annotated with " + JsonSerializableObject.class.getName());
        try {
            StringBuilder sb = new StringBuilder("\"" + clazz.getAnnotation(JsonSerializableObject.class).listName() + "\":[");
            Iterator<?> iter = list.iterator();
            while (iter.hasNext()) {
                Object o = iter.next();
                sb.append(objectToJSON(o));

                if (iter.hasNext()) sb.append(",");
            }
            sb.append("]");

            return sb.toString();
        } catch (Exception e) {
            throw new JsonSerializationException(e.getMessage());
        }
    }

    public static class JsonSerializationException extends Throwable {
        JsonSerializationException(String ex) {
            super(ex);
        }
    }
}
