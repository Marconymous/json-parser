package handlers;

import enums.JSONType;
import exceptions.ElementTypeException;
import parser.JSONParser;

import java.util.Iterator;
import java.util.List;

public class DefaultHandlers {
    public static class IntegerHandler implements ElementHandler {
        @Override
        public String handle(Object o) throws ElementTypeException {
            if (!(o instanceof Number))
                throw new ElementTypeException("Type (" + o.getClass().getSimpleName() + ") cannot be casted to Integer Value!");
            return String.valueOf(((Number) o).longValue());
        }

        @Override
        public String getType() {
            return JSONType.INTEGER.name();
        }
    }

    public static class StringHandler implements ElementHandler {
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
    }

    public static class FloatHandler implements ElementHandler {
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

    }

    public static class DoubleHandler implements ElementHandler {
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
    }

    public static class BooleanHandler implements ElementHandler {
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
    }

    public static class ListHandler implements ElementHandler {
        @Override
        public String handle(Object o) throws ElementTypeException, JSONParser.JsonSerializationException {
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
    }

    public static class JSONAnnotatedHandler implements ElementHandler {
        @Override
        public String handle(Object o) throws ElementTypeException, JSONParser.JsonSerializationException {
            JSONParser parser = new JSONParser();
            return parser.objectToJSON(o);
        }

        @Override
        public String getType() {
            return String.valueOf(JSONType.JSON_ANNOTATED);
        }
    }

    public static class EnumerationHandler implements ElementHandler {
        @Override
        public String handle(Object o) throws ElementTypeException, JSONParser.JsonSerializationException {
            if (!o.getClass().isEnum())
                throw new ElementTypeException("Type (" + o.getClass().getSimpleName() + ") cannot be casted to Enumerated Value!");
            return ((Enum<?>) o).name();
        }

        @Override
        public String getType() {
            return JSONType.ENUMERATED_STRING.name();
        }
    }

    public static class EnumerationHandlerOrdinal implements ElementHandler {
        @Override
        public String handle(Object o) throws ElementTypeException, JSONParser.JsonSerializationException {
            if (!o.getClass().isEnum())
                throw new ElementTypeException("Type (" + o.getClass().getSimpleName() + ") cannot be casted to Enumerated Value!");
            return String.valueOf(((Enum<?>) o).ordinal());
        }

        @Override
        public String getType() {
            return JSONType.ENUMERATED_ORDINAL.name();
        }
    }
}
