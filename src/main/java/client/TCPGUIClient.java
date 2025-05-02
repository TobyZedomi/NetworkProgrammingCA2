package client;


import com.google.gson.JsonObject;
import network.TCPNetworkLayer;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

public class TCPGUIClient {

    // Provide networking functionality

    private TCPNetworkLayer network;
    Gson gson = new Gson();

    // GUI components
    private final HashMap<String, Container> guiContainers = new HashMap<>();
    // Main gui window
    private JFrame mainFrame;
    // Main Font setting
    private Font font = new Font("Arial", Font.PLAIN, 16);

    // Panel for initial view
    private JPanel initialView;
    // Display for initial options - labels, text fields and button
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton loginButton;

    private JButton registerButton;


    // Panel for logged-in view
    private JPanel countCharView;
    private JButton countButton;

    private JButton logOut;

    private JButton goBackToHomePage;

    private JButton sendEmail;


    private JPanel registerView;
    private JButton registerViewButton;

    private JLabel usernameLabel1;

    private JTextField usernameTextField1;

    private JLabel passwordLabel1;


    private JTextField passwordTextField1;


    private JLabel confirmPasswordLabel1;


    private JTextField confirmPasswordTextField1;

    private JLabel emailLabel;

    private JTextField emailTextField;

    // send Email

    private JPanel sendEmailView;
    private JButton sendEmailViewButton;


    private JLabel receiverEmailLabel;

    private JTextField receiverEmailTextField;

    private JLabel subjectLabel;


    private JTextField subjectTextField;


    private JLabel messageLabel;


    private JTextField messageTextField;





    // Use constructor to establish the components (parts) of the GUI
    public TCPGUIClient() {

        // Set up the main window
        configureMainWindow();

        // Set up the initial panel (the initial view on the system)
        // This takes in the username and password of the user
        configureInitialPanel();

        // Set up second panel
        configureCountView();

        // register view

        configureRegisterView();

        // send an email Panel
        configureSendEmailPanel();

    }

    private static GridBagConstraints getGridBagConstraints(int col, int row, int width) {
        // Create a constraints object to manage component placement within a frame/panel
        GridBagConstraints gbc = new GridBagConstraints();
        // Set it to fill horizontally (component will expand to fill width)
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Add padding around the component (Pad by 5 on all sides)
        gbc.insets = new Insets(5, 5, 5, 5);

        // Set the row position to the supplied value
        gbc.gridx = col;
        // Set the column position to the supplied value
        gbc.gridy = row;
        // Set the component's width to the supplied value (in columns)
        gbc.gridwidth = width;
        return gbc;
    }

    private void configureMainWindow() {
        // Create the main frame - this is the main window
        mainFrame = new JFrame("Basic Sample GUI");
        mainFrame.setSize(400, 300);
        // Set what should happen when the X button is clicked on the window
        // This approach will dispose of the main window but not shut down the program
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Set the layout manager used for the main window
        mainFrame.setLayout(new CardLayout());

        // Add a listener to the overall window that reacts when window close action is requested
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    network.disconnect();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("Shutting down...");
                // Shut down the application fully
                System.exit(0);
            }
        });

        // Register the main window as a container in the system
        guiContainers.put("mainFrame", mainFrame);
    }

    // Set up initial panel (initial view)
    private void configureInitialPanel() {
        // Create and configure the config panel
        // This will provide a view to take in the user credentials
        // Use a GridBag layout so we have a grid to work with, but there's some flexibility (button can span columns)
        initialView = new JPanel(new GridBagLayout());
        // Register this panel as a container in the system
        guiContainers.put("initialView", initialView);

        // Create text fields and associated labels to take in username and password
        // Username info
        usernameLabel = new JLabel("Username: ");
        usernameField = new JTextField(15);

        // Password info
        passwordLabel = new JLabel("Password: ");
        passwordField = new JTextField(15);

        // Create a button to log in user
        loginButton = new JButton("Log in");
        // Specify what the button should DO when clicked:

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });


        // Create a button to register user
        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });



        // Add credential components to initial view panel in specific positions within the gridbag
        // Add username label and text field on first row (y = 0)
        initialView.add(usernameLabel, getGridBagConstraints(0, 0, 1));
        initialView.add(usernameField, getGridBagConstraints(1, 0, 1));
        // Add password label and text field on second row (y = 1)
        initialView.add(passwordLabel, getGridBagConstraints(0, 1, 1));
        initialView.add(passwordField, getGridBagConstraints(1, 1, 1));

        // Add button on third row (y = 2) spanning two columns (width = 2)
        initialView.add(loginButton, getGridBagConstraints(0, 2, 2));

        initialView.add(registerButton, getGridBagConstraints(0, 3, 2));


        // Add empty space on fourth row (y = 3) spanning two columns (width = 2)
        initialView.add(new JPanel(), getGridBagConstraints(0, 4, 2));

    }

    private void configureCountView(){


        // Create and configure the config panel
        // This will provide a view to take in the user credentials
        // Use a GridBag layout so we have a grid to work with, but there's some flexibility (button can span columns)
        countCharView = new JPanel(new GridBagLayout());
        // Register this panel as a container in the system
        guiContainers.put("countCharView", countCharView);


        // send email

        sendEmail = new JButton("Send Email");
        // Specify what the button should DO when clicked:
        sendEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmailPage();
            }
        });



        // logout

        logOut = new JButton("Log Out");
        // Specify what the button should DO when clicked:
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logOutUser();
            }
        });


        // Add button on third row (y = 2) spanning two columns (width = 2)
        countCharView.add(sendEmail, getGridBagConstraints(0, 2, 2));

        countCharView.add(logOut, getGridBagConstraints(0, 3, 2));
    }

    private void showInitialView(){
        // Add config panel to the main window and make it visible
        mainFrame.add(initialView);
        mainFrame.setVisible(true);
    }

    private void showCountView(){

        // Add config panel to the main window and make it visible
        // mainFrame.remove(0);

        mainFrame.add(countCharView);
        mainFrame.setVisible(true);
    }




    // register View

    private void configureRegisterView(){
        // Create and configure the config panel
        // This will provide a view to take in the user credentials
        // Use a GridBag layout so we have a grid to work with, but there's some flexibility (button can span columns)
        registerView = new JPanel(new GridBagLayout());
        // Register this panel as a container in the system
        guiContainers.put("registerView", registerView);

        // Create text fields and associated labels to take in username and password
        // Username info
        usernameLabel1 = new JLabel("username: ");
        usernameTextField1 = new JTextField(15);

        passwordLabel1 = new JLabel("password: ");
        passwordTextField1 = new JTextField(15);


        confirmPasswordLabel1 = new JLabel("Confirm Password: ");
        confirmPasswordTextField1 = new JTextField(15);


        // Create a button to log in user
        registerViewButton = new JButton("Register");
        // Specify what the button should DO when clicked:
        registerViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRegisterButton();
            }
        });


        logOut = new JButton("Go Back To Login");
        // Specify what the button should DO when clicked:
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToLogin();
            }
        });

        // Add credential components to count view panel in specific positions within the gridbag
        // Add username label and text field on first row (y = 0)
        registerView.add(usernameLabel1, getGridBagConstraints(0, 0, 1));
        registerView.add(usernameTextField1, getGridBagConstraints(1, 0, 1));
        // Add password label and text field on second row (y = 1)
        registerView.add(passwordLabel1, getGridBagConstraints(0, 1, 1));
        registerView.add(passwordTextField1, getGridBagConstraints(1, 1, 1));

        //confirm password

        registerView.add(confirmPasswordLabel1, getGridBagConstraints(0, 2, 1));
        registerView.add(confirmPasswordTextField1, getGridBagConstraints(1, 2, 1));


        // Add button on third row (y = 2) spanning two columns (width = 2)
        registerView.add(registerViewButton, getGridBagConstraints(0, 3, 2));

        // Add button on third row (y = 2) spanning two columns (width = 2)
        registerView.add(logOut, getGridBagConstraints(0, 4, 2));
    }


    private void showRegisterView(){

        // Add config panel to the main window and make it visible
        // mainFrame.remove(0);

        mainFrame.add(registerView);
        mainFrame.setVisible(true);
    }





    /// send an email Panel


    // register View

    private void configureSendEmailPanel(){
        // Create and configure the config panel
        // This will provide a view to take in the user credentials
        // Use a GridBag layout so we have a grid to work with, but there's some flexibility (button can span columns)
        sendEmailView = new JPanel(new GridBagLayout());
        // Register this panel as a container in the system
        guiContainers.put("sendEmailView", sendEmailView);

        // Create text fields and associated labels to take in username and password
        // Username info
        receiverEmailLabel = new JLabel("To: ");
        receiverEmailTextField = new JTextField(15);

        subjectLabel = new JLabel("Subject: ");
        subjectTextField = new JTextField(15);


        messageLabel = new JLabel("Message: ");
        messageTextField = new JTextField(15);



        // Create a button to log in user
        sendEmailViewButton = new JButton("Send Email");
        // Specify what the button should DO when clicked:
        sendEmailViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmailToUser();
            }
        });


        goBackToHomePage = new JButton("Go Back To Home Page");
        // Specify what the button should DO when clicked:
        goBackToHomePage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToHomePageSendEmail();
            }
        });


        logOut = new JButton("LogOut");
        // Specify what the button should DO when clicked:
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logOutSendEmail();
            }
        });


        sendEmailView.add(receiverEmailLabel, getGridBagConstraints(0, 0, 1));
        sendEmailView.add(receiverEmailTextField, getGridBagConstraints(1, 0, 1));

        sendEmailView.add(subjectLabel, getGridBagConstraints(0, 1, 1));
        sendEmailView.add(subjectTextField, getGridBagConstraints(1, 1, 1));

        sendEmailView.add(messageLabel, getGridBagConstraints(0, 2, 1));
        sendEmailView.add(messageTextField, getGridBagConstraints(1, 2, 1));


        // Add button on third row (y = 2) spanning two columns (width = 2)
        sendEmailView.add(sendEmailViewButton, getGridBagConstraints(0, 3, 2));

        // Add button on third row (y = 2) spanning two columns (width = 2)
        sendEmailView.add(goBackToHomePage, getGridBagConstraints(0, 4, 2));
        sendEmailView.add(logOut, getGridBagConstraints(0, 5, 2));
    }


    private void showSendEmailView(){

        // Add config panel to the main window and make it visible
        // mainFrame.remove(0);

        mainFrame.add(sendEmailView);
        mainFrame.setVisible(true);
    }




    public void start() throws IOException {
        network = new TCPNetworkLayer(AuthUtils.SERVER_HOST, AuthUtils.SERVER_PORT);
        network.connect();
        // Add the initial panel to the main window and display the interface
        showInitialView();
    }

    /*
     * All methods below this point provide application logic
     */
    private void loginUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        JsonObject payload = new JsonObject();
        payload.addProperty("username", username);
        payload.addProperty("password", password);

        // Create the overall request object
        JsonObject requestJson = new JsonObject();
        // Add the request type/action and payload
        requestJson.addProperty("action", AuthUtils.LOGIN);
        requestJson.add("payload", payload);

        String request = gson.toJson(requestJson);
        network.send(request);

        String response = network.receive();

        // If the response matches the expected success message, treat user as authenticated
        if (response.equals(AuthUtils.LOGIN_SUCCESSFUL)) {
            JOptionPane.showMessageDialog(initialView, response, "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            mainFrame.remove(initialView);
            showCountView();

            usernameField.setText("");
            passwordField.setText("");

            return;
        }

        if (response.equals(AuthUtils.ACK)){

            JOptionPane.showMessageDialog(initialView, response, "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            try {
                network.disconnect();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Shutting down...");
            // Shut down the application fully
            System.exit(0);
        }

        JOptionPane.showMessageDialog(initialView, response, "Login Failed",
                JOptionPane.ERROR_MESSAGE);


    }


    private void registerUser(){

        mainFrame.remove(initialView);
        showRegisterView();
    }

    private void logOutUser(){

        mainFrame.remove(countCharView);
        showInitialView();
    }

    private void sendEmailPage(){

        mainFrame.remove(countCharView);
        showSendEmailView();
    }

    private void goBackToLogin(){

        mainFrame.remove(registerView);
        showInitialView();
    }



    private void logOutSendEmail(){

        mainFrame.remove(sendEmailView);
        showInitialView();
    }


    private void goBackToHomePageSendEmail(){

        mainFrame.remove(sendEmailView);
        showCountView();
    }





    private void setRegisterButton(){
        String username = usernameTextField1.getText();
        String password = passwordTextField1.getText();
        String confirmPassword = confirmPasswordTextField1.getText();


        JsonObject payload = new JsonObject();
        payload.addProperty("username", username);
        payload.addProperty("password", password);
        payload.addProperty("confirmPassword", confirmPassword);


        // Create the overall request object
        JsonObject requestJson = new JsonObject();
        // Add the request type/action and payload
        requestJson.addProperty("action", AuthUtils.REGISTER);
        requestJson.add("payload", payload);

        String request = gson.toJson(requestJson);
        network.send(request);

        // Wait to receive a response to the authentication request
        String response = network.receive();


        if (response.equalsIgnoreCase(AuthUtils.REGISTER_SUCCESSFUL)) {

            JOptionPane.showMessageDialog(initialView, "You have successfully registered a user!", "Register Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            mainFrame.remove(registerView);
            showCountView();

            usernameTextField1.setText("");
            passwordTextField1.setText("");
            confirmPasswordTextField1.setText("");

            System.out.println(response);
            return;

        }
        JOptionPane.showMessageDialog(initialView, response, "Register Failed",
                JOptionPane.ERROR_MESSAGE);
    }


    // send Email

    private void sendEmailToUser(){

        String receiverEmail = receiverEmailTextField.getText();
        String subject = subjectTextField.getText();
        String message = messageTextField.getText();


        JsonObject payload = new JsonObject();
        payload.addProperty("receiver", receiverEmail);
        payload.addProperty("subject", subject);
        payload.addProperty("message", message);

        // Create the overall request object
        JsonObject requestJson = new JsonObject();
        // Add the request type/action and payload
        requestJson.addProperty("action", AuthUtils.SEND_EMAIL);
        requestJson.add("payload", payload);

        String request = gson.toJson(requestJson);
        network.send(request);

        // Wait to receive a response to the authentication request
        String response = network.receive();

        if (response.equalsIgnoreCase(AuthUtils.EMAIL_SUCCESSFULLY_SENT)) {

            JOptionPane.showMessageDialog(initialView, "You have successfully registered a user!", "Register Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            mainFrame.remove(registerView);
            showCountView();

            usernameTextField1.setText("");
            passwordTextField1.setText("");
            confirmPasswordTextField1.setText("");
            emailTextField.setText("");

            System.out.println(response);
            return;

        }
        JOptionPane.showMessageDialog(initialView, response, "Register Failed",
                JOptionPane.ERROR_MESSAGE);

    }

    private void setStandardFonts(){
        UIManager.put("Label.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("OptionPane.messageFont", font);
        UIManager.put("OptionPane.buttonFont", font);
    }

    private void updateContainers() {
        for (Container c : guiContainers.values()) {
            for (Component component : c.getComponents()) {
                // Set the font in the component
                component.setFont(font);
            }
            // Revalidate and repaint the container
            c.revalidate();
            c.repaint();
        }
    }
    // GUI runner
    public static void main(String[] args) {
        // Create an instance of the GUI
        TCPGUIClient TCPGUIClient = new TCPGUIClient();
        // Start the GUI - this will trigger the application to be made visible
        try{
            TCPGUIClient.start();
        }catch(UnknownHostException e){
            System.out.println("Hostname could not be found. Please contact system administrator");
        }catch(SocketException e){
            System.out.println("Socket exception occurred. Please try again later.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
