package service;

public class UserUtilities {

    public static final String HOSTNAME = "localhost";
    public static final int PORT = 11000;

    // REQUESTS
    public static final String LOGIN = "LOGIN";

    public static final String REGISTER = "REGISTER";

    public static final String EXIT = "EXIT";

    public static final String SEND_EMAIL = "SEND_EMAIL";

    public static final String RETRIEVE_EMAILS = "RETRIEVE_EMAILS";

    public static final String SEARCH_RECEIVED_EMAILS = "SEARCH_RECEIVED_EMAILS";

    public static final String GET_CONTENT_RECEIVED_EMAILS = "GET_CONTENT_RECEIVED_EMAILS";

    public static final String GET_CONTENT_SENT_EMAIL = "GET_CONTENT_SENT_EMAIL";
    public static final String GET_RECEIVED_EMAIL_BY_ID = "GET_RECEIVED_EMAIL_BY_ID";
    public static final String GET_SENT_EMAIL_BY_ID = "GET_SENT_EMAIL_BY_ID";

    public static final String LOGOUT = "LOGOUT";



    // Responses

    public static final String LOGIN_SUCCESSFUL = "LOGIN_SUCCESSFUL";
    public static final String LOGIN_FAILED = "LOGIN_FAILED";

    public static final String PASSWORDS_DONT_MATCH = "PASSWORDS_DONT_MATCH";

    public static final String INVALID_PASSWORD_FORMAT = "INVALID_PASSWORD_FORMAT";

    public static final String INVALID_EMAIL_FORMAT = "INVALID_EMAIL_FORMAT";


    public static final String REGISTER_SUCCESSFUL = "REGISTER_SUCCESSFUL";

    public static final String USER_ALREADY_EXIST = "USER_ALREADY_EXIST";

    public static final String EMAIL_ALREADY_EXIST = "EMAIL_ALREADY_EXIST";

    public static final String INVALID_DATE_TIME = "INVALID_DATE_TIME";

    public static final String EMAIL_DONT_EXIST = "EMAIL_DONT_EXIST";

    public static final String EMAIL_SUCCESSFULLY_SENT = "EMAIL_SUCCESSFULLY_SENT";

    public static final String NOT_LOGGED_IN = "NOT_LOGGED_IN";

    public static final String YOU_HAVE_NO_EMAILS = "YOU_HAVE_NO_EMAILS";

    public static final String EMAILS_RETRIEVED_SUCCESSFULLY = "EMAILS_RETRIEVED_SUCCESSFULLY";

    public static final String EMAIL_CONTENT_RETRIEVED_SUCCESSFULLY = "EMAIL_CONTENT_RETRIEVED_SUCCESSFULLY";


    public static final String NO_EMAILS_WITH_THIS_SUBJECT = "NO_EMAILS_WITH_THIS_SUBJECT";

    public static final String NON_NUMERIC_ID= "NON_NUMERIC_ID";
    public static final String EMAIL_ID_DOESNT_EXIST = "EMAIL_ID_DOESNT_EXIST";

    public static final String EMPTY_SUBJECT = "EMPTY_SUBJECT";

    public static final String EMAIL_ID_LESS_THAN_1 = "EMAIL_ID_LESS_THAN_1";
    public static final String EMAIL_RETRIEVED_SUCCESSFULLY = "EMAIL_RETRIEVED_SUCCESSFULLY";
    public static final String GOODBYE = "GOODBYE";


    // DELIMITERS
    public static final String DELIMITER = "%%";
    public static final String EMAIL_DELIMITER = ", ";

    public static final String EMAIL_DELIMITER2 = "##";



    // GENERAL MALFORMED RESPONSE:
    public static final String INVALID = "INVALID";

}
