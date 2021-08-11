package parser.enums;

public enum ParserResponseType {
    /**
     * Json Object containing a key value pair with the list.
     * like: {"numbers":[1,2,3,4,5]}
     */
    OBJECT_LIST,

    /**
     * Key Value Pair to insert into a JSON Object.
     * like: "numbers": [1,2,3,4,5]
     */
    KEY_VALUE_PAIR,

    /**
     * JSON Array
     * like: [1,2,3,4,5]
     */
    LIST
}