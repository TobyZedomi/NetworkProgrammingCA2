package service;

import com.google.gson.JsonObject;
import model.*;
import network.TCPNetworkLayer;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.StringJoiner;

public class TCPEmailServer implements Runnable {

    private Socket clientDataSocket;
    private TCPNetworkLayer networkLayer;
    private UserManager userManager;

    private EmailManager emailManager;

    private static String username;

    private final Gson gson = new Gson();


    public TCPEmailServer(Socket clientDataSocket, UserManager userManager, EmailManager emailManager, String username) throws IOException {
        this.clientDataSocket = clientDataSocket;
        this.networkLayer = new TCPNetworkLayer(clientDataSocket);

        this.userManager = userManager;
        this.emailManager = emailManager;
        this.username = username;
    }


    public void run() {


        try {
            boolean validClientSession = true;
            boolean loginStatus = false;


            while (validClientSession) {

                String request = networkLayer.receive();
                System.out.println("Request: " + request);

                JsonObject jsonResponse = null;

                JsonObject jsonRequest = gson.fromJson(request, JsonObject.class);


                if (jsonRequest.has("action")) {
                    String action = jsonRequest.get("action").getAsString();
                    switch (action) {
                        case UserUtilities.REGISTER:
                            jsonResponse = registerUser(jsonRequest, userManager, emailManager);
                            break;
                        case UserUtilities.LOGIN:
                            jsonResponse = loginUser(jsonRequest, userManager, username);
                            if (jsonResponse == createStatusResponse(UserUtilities.LOGIN_SUCCESSFUL)) {
                                loginStatus = true;
                            }
                            break;
                        case UserUtilities.SEND_EMAIL:
                            jsonResponse = sendEmail(loginStatus, jsonRequest, emailManager);
                            break;
                        case UserUtilities.RETRIEVE_EMAILS:
                            jsonResponse = retreiveEmails(loginStatus, emailManager);
                            break;
                        case UserUtilities.SEARCH_RETRIEVED_EMAILS:
                            jsonResponse = searchForretrivedEmailsBasedOnUsernameAndSubject(loginStatus, jsonRequest, emailManager);
                            break;
                        case UserUtilities.GET_CONTENT_RETRIEVED_EMAILS:
                            jsonResponse = getContentOfRetreivedEmail(loginStatus, jsonRequest, emailManager);
                            break;
                        case UserUtilities.GET_CONTENT_SENT_EMAIL:
                            jsonResponse = getContentOfParticularSentEmail(loginStatus, jsonRequest, emailManager);
                            break;
                        case UserUtilities.EXIT:
                            jsonResponse = createStatusResponse(UserUtilities.ACK);
                            validClientSession = false;
                            break;
                    }

                }

                if (jsonResponse == null) {
                    jsonResponse = createStatusResponse(UserUtilities.INVALID);
                }

                String response = gson.toJson(jsonResponse);
                // Send response
                networkLayer.send(response);

            }

            networkLayer.disconnect();
        } catch (IOException e) {
            System.out.println("ERROR");
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonObject getContentOfParticularSentEmail(boolean loginStatus, JsonObject jsonRequest, IEmailManager emailManager) {
        JsonObject jsonResponse;
        if (!loginStatus){

            JsonObject payload = (JsonObject) jsonRequest.get("payload");

            if (payload.size() == 1) {

                try {
                    int Id = Integer.parseInt(payload.get("Id").getAsString());

                    boolean checkIfEmailIdExist = emailManager.checkIfSendEmailIdExist(username, Id);

                    Email email =  emailManager.getContentOfParticularSentEmail(username, Id);

                    if (checkIfEmailIdExist){

                        jsonResponse = createStatusResponseForContent(serializeEmailContentOnly(email));

                    }else{
                        jsonResponse = createStatusResponse(UserUtilities.EMAIL_ID_DOESNT_EXIST);
                    }
                }catch (NumberFormatException ex){
                    jsonResponse = createStatusResponse(UserUtilities.NON_NUMERIC_ID);
                }

            }else{
                jsonResponse = createStatusResponse(UserUtilities.INVALID);
            }

        }else{
            jsonResponse = createStatusResponse(UserUtilities.NOT_LOGGED_IN);

        }
        return jsonResponse;
    }

    private JsonObject getContentOfRetreivedEmail(boolean loginStatus, JsonObject jsonRequest, IEmailManager emailManager) {
        JsonObject jsonResponse;
        if (!loginStatus) {

            JsonObject payload = (JsonObject) jsonRequest.get("payload");

            if (payload.size() == 1) {

                try {
                    int Id = Integer.parseInt(payload.get("Id").getAsString());

                    boolean checkIfEmailIdExist = emailManager.checkIfReceivedEmailIdExist(username, Id);

                  Email email =  emailManager.getContentOfParticularReceivedEmail(username, Id);

                    if (checkIfEmailIdExist){

                        jsonResponse = createStatusResponseForContent(serializeEmailContentOnly(email));

                    }else{
                        jsonResponse = createStatusResponse(UserUtilities.EMAIL_ID_DOESNT_EXIST);
                    }
                }catch (NumberFormatException ex){
                    jsonResponse = createStatusResponse(UserUtilities.NON_NUMERIC_ID);
                }

            }else{
                jsonResponse = createStatusResponse(UserUtilities.INVALID);
            }

        }else {
            jsonResponse = createStatusResponse(UserUtilities.NOT_LOGGED_IN);
        }
        return jsonResponse;
    }

    private JsonObject searchForretrivedEmailsBasedOnUsernameAndSubject(boolean loginStatus, JsonObject jsonRequest, IEmailManager emailManager) {
        JsonObject jsonResponse;
        if (!loginStatus) {

            JsonObject payload = (JsonObject) jsonRequest.get("payload");
            if (payload.size() == 1) {
                String subject = payload.get("subject").getAsString();

                ArrayList<Email> emailsForUserBasedOnSubject = emailManager.searchForRetrievedEmailsBasedOnSubject(username, subject);

                if (!emailsForUserBasedOnSubject.isEmpty()) {
                    if (emailsForUserBasedOnSubject != null) {
                            jsonResponse = serializeEmails(emailsForUserBasedOnSubject);

                    } else {
                        jsonResponse = createStatusResponse(UserUtilities.INVALID);
                    }
                } else {
                    jsonResponse = createStatusResponse(UserUtilities.NO_EMAILS_WITH_THIS_SUBJECT);
                }

            } else {
                jsonResponse = createStatusResponse(UserUtilities.INVALID);
            }
        } else {
            jsonResponse = createStatusResponse(UserUtilities.NOT_LOGGED_IN);
        }
        return jsonResponse;
    }

    private JsonObject retreiveEmails(boolean loginStatus, IEmailManager emailManager) {
        JsonObject jsonResponse;
        if (!loginStatus) {
            ArrayList<Email> emailsForUser = emailManager.searchForRetrievedEmails(username);

            if (!emailsForUser.isEmpty()) {
                if (emailsForUser != null) {
                    jsonResponse = serializeEmails(emailsForUser);
                } else {
                    jsonResponse = createStatusResponse(UserUtilities.INVALID);
                }
            } else {
                jsonResponse = createStatusResponse(UserUtilities.YOU_HAVE_NO_EMAILS);
            }
        } else {
            jsonResponse = createStatusResponse(UserUtilities.NOT_LOGGED_IN);
        }
        return jsonResponse;
    }

    private JsonObject sendEmail(boolean loginStatus, JsonObject jsonRequest, IEmailManager emailManager) {

        JsonObject jsonResponse = null;

        if (!loginStatus) {

                JsonObject payload = (JsonObject) jsonRequest.get("payload");
                if (payload.size() == 3) {

                    String sender = username;
                    String receiver = payload.get("receiver").getAsString();
                    String subject = payload.get("subject").getAsString();
                    String content = payload.get("content").getAsString();
                    LocalDateTime date = LocalDateTime.now();


                    boolean checkIfReceiverExist = emailManager.checkIfReceiverExist(receiver);

                    boolean checkEmailIsValidFormat = emailManager.checkIfEmailMatchRegex(receiver);

                    if (!date.isAfter(LocalDateTime.now())) {

                        if (checkEmailIsValidFormat == true) {

                            if (checkIfReceiverExist == true) {
                                emailManager.sendAnEmailToUser(sender, receiver, subject, content, date);

                                jsonResponse = createStatusResponse(UserUtilities.EMAIL_SUCCESSFULLY_SENT);
                            } else {
                                jsonResponse = createStatusResponse(UserUtilities.EMAIL_DONT_EXIST);
                            }
                        } else {
                            jsonResponse = createStatusResponse(UserUtilities.INVALID_EMAIL_FORMAT);
                        }
                    } else {
                        jsonResponse = createStatusResponse(UserUtilities.INVALID_DATE_TIME);
                    }
                } else {
                    jsonResponse = createStatusResponse(UserUtilities.INVALID);
                }
        } else {

            jsonResponse = createStatusResponse(UserUtilities.NOT_LOGGED_IN);
        }
        return jsonResponse;
    }

    private static JsonObject registerUser(JsonObject jsonRequest, IUserManager userManager, IEmailManager emailManager) throws InvalidKeySpecException, NoSuchAlgorithmException {
        //String jsonResponse;
        JsonObject jsonResponse = null;
        JsonObject payload = (JsonObject) jsonRequest.get("payload");
        if (payload.size() == 3) {

            String username = payload.get("username").getAsString();
            String password = payload.get("password").getAsString();
            String confirmPassword = payload.get("confirmPassword").getAsString();


            boolean checkIfUserExist = userManager.checkIfUserExist(username);

            boolean checkPasswordsMatch = userManager.checkIfPasswordsAreTheSame(password, confirmPassword);

            boolean checkPasswordFormat = userManager.checkIfPasswordsMatchRegex(password, confirmPassword);

            boolean checkEmailFormat = userManager.checkIfEmailMatchRegex(username);

            if (checkIfUserExist == true) {
                if (checkPasswordsMatch == true) {

                    if (checkPasswordFormat == true) {
                        if (checkEmailFormat == true) {

                            userManager.registerUser(username, password);
                            emailManager.addEmail(username);
                            jsonResponse = createStatusResponse(UserUtilities.REGISTER_SUCCESSFUL);
                        } else {
                            jsonResponse = createStatusResponse(UserUtilities.INVALID_EMAIL_FORMAT);
                        }
                    } else {
                        jsonResponse = createStatusResponse(UserUtilities.INVALID_PASSWORD_FORMAT);
                    }

                } else {
                    jsonResponse = createStatusResponse(UserUtilities.PASSWORDS_DONT_MATCH);
                }
            } else {
                jsonResponse = createStatusResponse(UserUtilities.USER_ALREADY_EXIST);
            }
        } else {
            jsonResponse = createStatusResponse(UserUtilities.INVALID);
        }
        return jsonResponse;
    }

    private static JsonObject loginUser(JsonObject jsonRequest, IUserManager userManager, String email) {
        JsonObject jsonResponse = null;
        JsonObject payload = (JsonObject) jsonRequest.get("payload");
        if (payload.size() == 2) {

            String usernameLoggedIn = payload.get("username").getAsString();
            email = usernameLoggedIn;
            username = usernameLoggedIn;
            String password = payload.get("password").getAsString();

            boolean loginUser = userManager.loginUser(email, password);

            if (loginUser == true) {
                jsonResponse = createStatusResponse(UserUtilities.LOGIN_SUCCESSFUL);
            } else {
                jsonResponse = createStatusResponse(UserUtilities.LOGIN_FAILED);
            }
        } else {
            jsonResponse = createStatusResponse(UserUtilities.INVALID);
        }
        return jsonResponse;
    }


    public JsonObject serializeEmails(ArrayList<Email> emails) {

        JsonObject jsonResponse = null;

        StringJoiner joiner = new StringJoiner(UserUtilities.EMAIL_DELIMITER2);

        for (Email e : emails) {
            joiner.add(serializeEmail(e));
            jsonResponse = createStatusResponse2(joiner.toString());
        }
        return jsonResponse;
    }

    public String serializeEmail(Email m) {
        if (m == null) {
            throw new IllegalArgumentException("Cannot serialise null Movie");
        }
        return "ID: " + m.getID() + UserUtilities.EMAIL_DELIMITER + "Sender: " + m.getSender() + UserUtilities.EMAIL_DELIMITER + "Subject: " + m.getSubject() + UserUtilities.EMAIL_DELIMITER + "Date: " + m.getTimeStamp().toLocalDate();
    }

    public String serializeEmailContentOnly(Email email) {

        return email.getContent();
    }


    private static JsonObject createStatusResponse(String status) {
        JsonObject invalidResponse = new JsonObject();
        invalidResponse.addProperty("status", status);
        return invalidResponse;
    }

    private JsonObject createStatusResponse2(String emails) {
        JsonObject invalidResponse = new JsonObject();
        invalidResponse.addProperty("emails", emails);
        return invalidResponse;
    }

    private JsonObject createStatusResponseForContent(String content) {
        JsonObject invalidResponse = new JsonObject();
        invalidResponse.addProperty("content", content);
        return invalidResponse;
    }
}
