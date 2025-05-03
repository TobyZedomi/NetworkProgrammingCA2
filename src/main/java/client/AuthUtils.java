package client;

public class AuthUtils {

    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 11000;

    public static final String LOGIN = "LOGIN";

    public static final String REGISTER = "REGISTER";

    public static final String SEND_EMAIL = "SEND_EMAIL";

    public static final String EMAIL_SUCCESSFULLY_SENT = "{\"status\":\"EMAIL_SUCCESSFULLY_SENT\"}";

    public static final String RETRIEVE_EMAILS = "RETRIEVE_EMAILS";

    public static final String SEARCH_RETRIEVED_EMAILS = "SEARCH_RETRIEVED_EMAILS";

    public static final String GET_CONTENT_RETRIEVED_EMAILS = "GET_CONTENT_RETRIEVED_EMAILS";


    public static final String ACK = "GOODBYE";

    public static final String LOGIN_SUCCESSFUL = "{\"status\":\"LOGIN_SUCCESSFUL\"}";

    public static final String NO_EMAILS_WITH_THIS_SUBJECT = "{\"status\":\"NO_EMAILS_WITH_THIS_SUBJECT\"}";

    public static final String REGISTER_SUCCESSFUL =   "{\"status\":\"REGISTER_SUCCESSFUL\"}";

    public static final String NON_NUMERIC_ID = "{\"status\":\"NON_NUMERIC_ID\"}";

    public static final String EMAIL_ID_DOESNT_EXIST =   "{\"status\":\"EMAIL_ID_DOESNT_EXIST\"}";
    public static final String DELIMITER = "%%";
}
