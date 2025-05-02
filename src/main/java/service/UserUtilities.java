package service;

public class UserUtilities {

    public static final String HOSTNAME = "localhost";
    public static final int PORT = 11000;

    // REQUESTS
    public static final String LOGIN = "LOGIN";

    public static final String REGISTER = "REGISTER";

    public static final String EXIT = "EXIT";


    // Responses

    public static final String LOGIN_SUCCESSFUL = "LOGIN_SUCCESSFUL";
    public static final String LOGIN_FAILED = "LOGIN_FAILED";

    public static final String PASSWORDS_DONT_MATCH = "PASSWORDS_DONT_MATCH";

    public static final String INVALID_PASSWORD_FORMAT = "INVALID_PASSWORD_FORMAT";

    public static final String INVALID_EMAIL_FORMAT = "INVALID_EMAIL_FORMAT";


    public static final String REGISTER_SUCCESSFUL = "REGISTER_SUCCESSFUL";

    public static final String USER_ALREADY_EXIST = "USER_ALREADY_EXIST";

    public static final String EMAIL_ALREADY_EXIST = "EMAIL_ALREADY_EXIST";


    public static final String ACK = "GOODBYE";


    // DELIMITERS
    public static final String DELIMITER = "%%";
    public static final String EMAIL_DELIMITER = "##";



    // GENERAL MALFORMED RESPONSE:
    public static final String INVALID = "INVALID";

}
