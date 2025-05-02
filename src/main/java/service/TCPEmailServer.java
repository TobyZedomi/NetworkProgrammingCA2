package service;

import com.google.gson.JsonObject;
import model.IUserManager;
import model.User;
import model.UserManager;
import network.TCPNetworkLayer;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class TCPEmailServer implements Runnable {

    private Socket clientDataSocket;
    private TCPNetworkLayer networkLayer;
    private UserManager userManager;

    private final Gson gson = new Gson();


    public TCPEmailServer(Socket clientDataSocket, UserManager userManager) throws IOException {
        this.clientDataSocket = clientDataSocket;
        this.networkLayer = new TCPNetworkLayer(clientDataSocket);

        this.userManager = userManager;
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
                            jsonResponse = registerUser(jsonRequest, userManager);
                            break;
                        case UserUtilities.LOGIN:
                            jsonResponse = loginUser(jsonRequest, userManager);
                            if (jsonResponse == createStatusResponse(UserUtilities.LOGIN_SUCCESSFUL)) {
                                loginStatus = true;




                            }
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

    private static JsonObject registerUser(JsonObject jsonRequest, IUserManager userManager) throws InvalidKeySpecException, NoSuchAlgorithmException {
        //String jsonResponse;
        JsonObject jsonResponse = null;
        JsonObject payload = (JsonObject) jsonRequest.get("payload");
        if (payload.size() == 4) {

            String username = payload.get("username").getAsString();
            String password = payload.get("password").getAsString();
            String confirmPassword = payload.get("confirmPassword").getAsString();
            String email = payload.get("email").getAsString();


            boolean checkIfUserExist = userManager.checkIfUserExist(username);

            boolean checkIfEmailExist = userManager.checkIfEmailExist(username, email);

            boolean checkPasswordsMatch = userManager.checkIfPasswordsAreTheSame(password, confirmPassword);

            boolean checkPasswordFormat = userManager.checkIfPasswordsMatchRegex(password, confirmPassword);

            boolean checkEmailFormat = userManager.checkIfEmailMatchRegex(email);

            if (checkIfUserExist == true) {
                if (checkPasswordsMatch == true) {

                    if (checkPasswordFormat == true) {

                        if (checkIfEmailExist == false) {

                            if (checkEmailFormat == true) {

                                userManager.registerUser(username, password, email);
                                jsonResponse = createStatusResponse(UserUtilities.REGISTER_SUCCESSFUL);

                            } else {
                                jsonResponse = createStatusResponse(UserUtilities.INVALID_EMAIL_FORMAT);
                            }

                        } else {
                            jsonResponse = createStatusResponse(UserUtilities.EMAIL_ALREADY_EXIST);
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

    private static JsonObject loginUser(JsonObject jsonRequest, IUserManager userManager) {
        JsonObject jsonResponse = null;
        JsonObject payload = (JsonObject) jsonRequest.get("payload");
        if (payload.size() == 2) {

            String username = payload.get("username").getAsString();
            String password = payload.get("password").getAsString();

            boolean loginUser = userManager.loginUser(username, password);

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
