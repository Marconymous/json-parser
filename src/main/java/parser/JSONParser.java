package parser;

import annotations.JsonField;
import annotations.JsonMethod;
import annotations.JsonSerializableObject;
import enums.JSONType;
import exceptions.ElementTypeException;
import exceptions.JsonSerializationException;
import handlers.DefaultHandlers;
import handlers.ElementHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Class which is used to parse JsonSerializableObject into a JSONObject
 * - passed Object has to be of a Class which is annotated with JsonSerializableObject
 * - it should contain at least one Field annotated with JsonElement or JsonFunction or JsonMethod
 *
 * @see JsonSerializableObject
 * @see JsonField
 * @see JsonMethod
 */
public class JSONParser {
    /**
     * The Map of all Handlers for Objects.
     * - The Key is the type of the Handler which it will handle
     * - The Value is an Instance of the handler
     */
    private final Map<String, ElementHandler> handlers;

    /**
     * no-args constructor
     */
    public JSONParser() {
        handlers = new HashMap<>();

        for (Class<?> clazz : DefaultHandlers.class.getClasses()) {
            ElementHandler handler = null;
            try {
                handler = (ElementHandler) clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            assert handler != null;
            handlers.put(handler.getType(), handler);
        }
    }

    /**
     * Constructor to set the list of handlers to your own
     *
     * @param handlers the list of handlers
     */
    public JSONParser(Map<String, ElementHandler> handlers) {
        this.handlers = handlers;
    }

    /**
     * Adds one or multiple Handlers to a list
     *
     * @param handlers the list of handlers
     */
    public void addHandler(ElementHandler... handlers) {
        for (ElementHandler handler : handlers)
            this.handlers.put(handler.getType(), handler);
    }

    /**
     * Checks if an object is Serializable
     *
     * @param object the object to check
     * @throws JsonSerializationException if the Object cannot be serialized
     */
    private void checkIfSerializable(Object object) throws JsonSerializationException {
        if (Objects.isNull(object)) throw new JsonSerializationException("The object to serialize is null");

        Class<?> clazz = object.getClass();
        if (!clazz.isAnnotationPresent(JsonSerializableObject.class)) {
            throw new JsonSerializationException("The class " + clazz.getName() + " is not annotated with JsonSerializableObject");
        }
    }
    
    private String getJsonString(Object object) throws JsonSerializationException, IllegalAccessException, ElementTypeException, InvocationTargetException, exceptions.JsonSerializationException {
        // Get class of object
        Class<?> clazz = object.getClass();
        StringBuilder jsonString = new StringBuilder();
        ArrayList<Field> fields = new ArrayList<>();
        ArrayList<Method> methods = new ArrayList<>();
        Map<String, String> jsonElements = new HashMap<>();

        // add all annotated fields to an array
        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(JsonField.class)) {
                fields.add(f);
            }
        }

        // add all annotated methods to an array
        for (Method m : clazz.getDeclaredMethods()) {
            m.setAccessible(true);
            if (m.isAnnotationPresent(JsonMethod.class)) {
                methods.add(m);
            }
        }

        // Loop over all Fields in the List
        for (Field f : fields) {
            String annotationKey = f.getAnnotation(JsonField.class).key();
            String key = "\"" + ((annotationKey.equals("")) ? f.getName() : annotationKey) + "\":";
            Object o = f.get(object);

            JSONType type = f.getAnnotation(JsonField.class).type();
            String customType = f.getAnnotation(JsonField.class).customType();

            if (customType.equals("") && type == JSONType.CUSTOM)
                throw new JsonSerializationException("The field(" + object.getClass() + " --> " + f.getName() + ") is annotated with JSONType.CUSTOM but not with customType");

            handleKeyObjectPair(jsonElements, key, o, (type == JSONType.CUSTOM) ? customType : type.name());
        }

        // Loop over all Methods in the List
        for (Method m : methods) {
            String annotationKey = m.getAnnotation(JsonMethod.class).key();
            String key = "\"" + ((annotationKey.equals("")) ? m.getName() : annotationKey) + "\":";
            Object o = m.invoke(object);

            JSONType type = m.getAnnotation(JsonMethod.class).type();
            String customType = m.getAnnotation(JsonMethod.class).customType();
            handleKeyObjectPair(jsonElements, key, o, (type == JSONType.CUSTOM) ? customType : type.name());
        }

        Iterator<Map.Entry<String, String>> iter = jsonElements.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> e = iter.next();
            jsonString.append(e.getKey())
                    .append(e.getValue());

            if (iter.hasNext())
                jsonString.append(",");
        }


        return "{" + jsonString + "}";
    }

    private void handleKeyObjectPair(Map<String, String> jsonElements, String key, Object o, String type) throws ElementTypeException, JsonSerializationException, exceptions.JsonSerializationException {
        if (o == null) {
            jsonElements.put(key, "null");
        } else {
            ElementHandler handler = handlers.get(type);
            if (handler == null)
                throw new JsonSerializationException("No handler with the Type( \"" + type + "\" ) was found. " + JsonSerializationException.getHandlerRecommendations(o, handlers));
            jsonElements.put(key, handler.handle(o));
        }
    }


    public String objectToJSON(Object object) throws JsonSerializationException {
        try {
            checkIfSerializable(object);
            return getJsonString(object);
        } catch (Exception e) {
            throw new JsonSerializationException(e.getMessage());
        }
    }

    public String listToJSON(List<?> list, Class<?> clazz, boolean includeTitle) throws JsonSerializationException {
        if (!clazz.isAnnotationPresent(JsonSerializableObject.class))
            throw new JsonSerializationException("The Type " + clazz.getName() + " is not annotated with " + JsonSerializableObject.class.getName());
        try {
            StringBuilder sb = new StringBuilder(((includeTitle) ? "{\"" + clazz.getAnnotation(JsonSerializableObject.class).listName() + "\":" : "") + "[");
            Iterator<?> iter = list.iterator();
            while (iter.hasNext()) {
                Object o = iter.next();
                sb.append(objectToJSON(o));

                if (iter.hasNext()) sb.append(",");
            }
            sb.append("]").append((includeTitle) ? "}" : "");

            return sb.toString();
        } catch (Exception e) {
            throw new JsonSerializationException(e.getMessage());
        }
    }
}
