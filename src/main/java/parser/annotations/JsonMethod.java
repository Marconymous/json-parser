package parser.annotations;

import parser.enums.JSONType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to annotate Method in a Class which will be converted to a JSON Element
 * Class has to be annotated with JsonSerializableObject for this to work
 *
 * @see JsonSerializableObject
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JsonMethod {
    /**
     * @return the name of the element in the JSON Object
     */
    String key();

    /**
     * @return the type of the element
     */
    JSONType type();

    /**
     * @return the type of the element if the type() is JSONType.CUSTOM
     * @see JSONType
     */
    String customType() default "";
}
