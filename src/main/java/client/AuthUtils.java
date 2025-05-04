package client;

public class AuthUtils {

    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 11000;


    // REQUEST
    public static final String LOGIN = "LOGIN";

    public static final String REGISTER = "REGISTER";

    public static final String SEND_EMAIL = "SEND_EMAIL";

    public static final String RETRIEVE_EMAILS = "RETRIEVE_EMAILS";

    public static final String SEARCH_RETRIEVED_EMAILS = "SEARCH_RETRIEVED_EMAILS";

    public static final String GET_CONTENT_RETRIEVED_EMAILS = "GET_CONTENT_RETRIEVED_EMAILS";
    public static final String GET_CONTENT_SENT_EMAIL = "GET_CONTENT_SENT_EMAIL";

    public static final String GET_RETRIEVED_EMAIL_BY_ID = "GET_RETRIEVED_EMAIL_BY_ID";



    // RESPONSES

    public static final String EMAIL_SUCCESSFULLY_SENT = "{\"status\":\"EMAIL_SUCCESSFULLY_SENT\",\"message\":\"Email Successfully sent\"}";
    public static final String GOODBYE = "{\"status\":\"GOODBYE\",\"message\":\"Goodbye\"}";
    public static final String LOGIN_SUCCESSFUL = "{\"status\":\"LOGIN_SUCCESSFUL\",\"message\":\"Login Successful\"}";
    public static final String NO_EMAILS_WITH_THIS_SUBJECT = "{\"status\":\"NO_EMAILS_WITH_THIS_SUBJECT\",\"message\":\"No emails with this subject\"}";
    public static final String REGISTER_SUCCESSFUL =   "{\"status\":\"REGISTER_SUCCESSFUL\",\"message\":\"Registration Successful\"}";
    public static final String NON_NUMERIC_ID = "{\"status\":\"NON_NUMERIC_ID\",\"message\":\"Id must be a number\"}";
    public static final String EMAIL_ID_DOESNT_EXIST =   "{\"status\":\"EMAIL_ID_DOESNT_EXIST\",\"message\":\"Email with this id doesnt exist\"}";
    public static final String INVALID =   "{\"status\":\"INVALID\",\"message\":\"Invalid\"}";
    public static final String YOU_HAVE_NO_EMAILS =   "{\"status\":\"YOU_HAVE_NO_EMAILS\",\"message\":\"You have no emails\"}";
    public static final String NOT_LOGGED_IN =   "{\"status\":\"NOT_LOGGED_IN\",\"message\":\"Not logged in\"}";
    public static final String EMPTY_SUBJECT =   "{\"status\":\"EMPTY_SUBJECT\",\"message\":\"Subject was left empty\"}";
    public static final String EMAIL_ID_LESS_THAN_1 =   "{\"status\":\"EMAIL_ID_LESS_THAN_1\",\"message\":\"Email id cant be less than 1\"}";

    /// Delimiter
    public static final String DELIMITER = "%%";
}
