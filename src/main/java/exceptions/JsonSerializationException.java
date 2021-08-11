package exceptions;

import parser.handlers.DefaultHandlers;
import parser.handlers.ElementHandler;

import java.util.Map;

public class JsonSerializationException extends Exception {
    public JsonSerializationException(String ex) {
        super(ex);
    }

    public static String getHandlerRecommendations(Object o, Map<String, ElementHandler> handlers) {
        ElementHandler handler = null;

        for (Map.Entry<String, ElementHandler> h : handlers.entrySet()) {
            if (h.getValue().canHandle(o)) {
                for (Class<?> preferredHandler : DefaultHandlers.class.getClasses()) {
                    if (h.getValue().getClass() == preferredHandler)
                        return "Try the type JSONType." + h.getValue().getType() + " instead of JSONType.CUSTOM";
                }
                handler = h.getValue();
            }
        }
        if (handler != null) {
            return "Try changing to customType to \"" + handler.getType() + "\"";
        }

        return "No fitting Handler was found!";
    }
}
