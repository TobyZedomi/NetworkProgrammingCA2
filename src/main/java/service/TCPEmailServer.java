package service;

import com.google.gson.JsonObject;
import model.EmailManager;
import model.IEmailManager;
import model.IUserManager;
import model.UserManager;
import network.TCPNetworkLayer;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

public class TCPEmailServer implements Runnable {

    private Socket clientDataSocket;
    private TCPNetworkLayer networkLayer;
    private UserManager userManager;

    private EmailManager emailManager;

    private String username;

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
                            jsonResponse = sendEmail(loginStatus, action, jsonRequest, emailManager);
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

    private JsonObject sendEmail(boolean loginStatus, String action, JsonObject jsonRequest, IEmailManager emailManager) {

        JsonObject jsonResponse = null;

        if (!loginStatus) {

            if (action.equalsIgnoreCase(UserUtilities.SEND_EMAIL)) {

                JsonObject payload = (JsonObject) jsonRequest.get("payload");
                if (payload.size() == 3) {

                    String sender = username;
                    String receiver = payload.get("receiver").getAsString();
                    String subject = payload.get("subject").getAsString();
                    String message = payload.get("message").getAsString();
                    LocalDateTime date = LocalDateTime.now();


                    boolean checkIfReceiverExist = emailManager.checkIfReceiverExist(receiver);

                    boolean checkEmailIsValidFormat = emailManager.checkIfEmailMatchRegex(receiver);

                    if (!date.isAfter(LocalDateTime.now())) {

                        if (checkEmailIsValidFormat == true) {

                            if (checkIfReceiverExist == true) {
                                emailManager.sendAnEmailToUser(sender, receiver, subject, message, date);

                                jsonResponse = createStatusResponse(UserUtilities.EMAIL_SUCCESSFULLY_SENT);
                            } else {
                                jsonResponse = createStatusResponse(UserUtilities.EMAIL_DONT_EXIST);
                            }
                        } else {
                            jsonResponse = createStatusResponse(UserUtilities.INVALID_EMAIL_FORMAT);
                        }
                    }else{
                        jsonResponse = createStatusResponse(UserUtilities.INVALID_DATE_TIME);
                    }
                }
            }
        }else{

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

            String username = payload.get("username").getAsString();
            email = username;
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


    private static JsonObject createStatusResponse(String status){
        JsonObject invalidResponse = new JsonObject();
        invalidResponse.addProperty("status", status);
        return invalidResponse;
    }

}
