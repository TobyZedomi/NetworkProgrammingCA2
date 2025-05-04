package client;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.Email;
import network.TCPNetworkLayer;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TCPGUIClient {

    // Provide networking functionality

    static JFrame f;

    //lists
    static JList b;

    private JLabel emailListLabel;


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


    private JButton retrieveEmails;

    private JButton searchEmailsBasedOnSubject;

    private JButton getContentOfRetreivedEmails;

    private JButton getContentOfSentEmails;

    private JButton getRetrievedEmailById;

    private JButton getSentEmailById;


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


    private JLabel contentLabel;


    private JTextField contentTextField;


    // search for emails based on subject

    private JPanel searchEmailSubjectView;

    private JButton searchEmailButton;

    private JLabel subjectSearchLabel;

    private JTextField subjectSearchTextField;

    // get content of retrieved emails based on email Id


    private JPanel contentRetreivedEmailsView;

    private JButton getContentRetreivedEmailsButton;

    private JLabel idLabel;

    private JTextField idTextField;


    // get content of sent emails

    private JPanel contentSentEmailsView;

    private JButton getContentSentEmailsButton;

    private JLabel id2Label;

    private JTextField id2TextField;

    // get retrieved email by id

    private JPanel getRetreivedEmailByIdView;

    private JButton getRetreivedEmailByIdButton;

    private JLabel emailIdLabel;

    private JTextField emailIdTextField;

    // get sent email by id

    private JPanel getSentEmailByIdView;

    private JButton getSentEmailByIdButton;

    private JLabel emailSentIdLabel;

    private JTextField emailSentIdTextField;


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

        // search for email based on subject panel

        configureSearchForSubjectPanel();

        // getContentOfRetrievedEmails

        configureGetContentRetreivedEmails();

        // get content of sent emails

        configureGetContentSentEmail();

        // get retrieved email by id

        configureGetRetrievedEmailByID();

        // get sent email by id

        configureGetSentEmailByID();

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
        mainFrame.setSize(500, 400);
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

        // send email

        retrieveEmails = new JButton("Retrieve Emails");
        // Specify what the button should DO when clicked:
        retrieveEmails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retrieveEmailsForUser();
            }
        });


        // search emails

        searchEmailsBasedOnSubject = new JButton("Search Retrieve Emails Based OnSubject");
        // Specify what the button should DO when clicked:
        searchEmailsBasedOnSubject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToSearchEmailsPage();
            }
        });


        // get content of retreived emails

        getContentOfRetreivedEmails = new JButton("Get Content Of Particular retrieved Email ");
        // Specify what the button should DO when clicked:
        getContentOfRetreivedEmails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToGetContentRetreivedEmailPage();
            }
        });

        // get content of sent email

        getContentOfSentEmails = new JButton("Get Content Of Particular sent Email ");
        // Specify what the button should DO when clicked:
        getContentOfSentEmails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToGetContentSentEmailPage();
            }
        });


        getRetrievedEmailById = new JButton("Get Retrieved Email By ID ");
        // Specify what the button should DO when clicked:
        getRetrievedEmailById.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToGetRetrievedEmilByIdPage();
            }
        });


        getSentEmailById = new JButton("Get Sent Email By ID ");
        // Specify what the button should DO when clicked:
        getSentEmailById.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToGetSentEmilByIdPage();
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

        countCharView.add(retrieveEmails, getGridBagConstraints(0, 3, 2));

        countCharView.add(searchEmailsBasedOnSubject, getGridBagConstraints(0, 4, 2));

        countCharView.add(getContentOfRetreivedEmails, getGridBagConstraints(0, 5, 2));

        countCharView.add(getContentOfSentEmails, getGridBagConstraints(0, 6, 2));

        countCharView.add( getRetrievedEmailById, getGridBagConstraints(0, 7, 2));

        countCharView.add(getSentEmailById, getGridBagConstraints(0, 8, 2));

        countCharView.add(logOut, getGridBagConstraints(0, 9, 2));
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


        contentLabel = new JLabel("Content: ");
        contentTextField = new JTextField(15);



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

        sendEmailView.add(contentLabel, getGridBagConstraints(0, 2, 1));
        sendEmailView.add(contentTextField, getGridBagConstraints(1, 2, 1));


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


    // search for email based on subject


    private void configureSearchForSubjectPanel(){
        // Create and configure the config panel
        // This will provide a view to take in the user credentials
        // Use a GridBag layout so we have a grid to work with, but there's some flexibility (button can span columns)
        searchEmailSubjectView = new JPanel(new GridBagLayout());
        // Register this panel as a container in the system
        guiContainers.put("searchEmailSubjectView", searchEmailSubjectView);


        subjectSearchLabel = new JLabel("Subject: ");
        subjectSearchTextField = new JTextField(15);


        // Create a button to log in user
        searchEmailButton = new JButton("Search Email");
        // Specify what the button should DO when clicked:
        searchEmailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchForEmailBasedOnSubject();
            }
        });


        goBackToHomePage = new JButton("Go Back To Home Page");
        // Specify what the button should DO when clicked:
        goBackToHomePage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToHomePageSendEmail2();
            }
        });


        logOut = new JButton("LogOut");
        // Specify what the button should DO when clicked:
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logOutSearchEmail();
            }
        });



        searchEmailSubjectView.add(subjectSearchLabel, getGridBagConstraints(0, 1, 1));
        searchEmailSubjectView.add(subjectSearchTextField, getGridBagConstraints(1, 1, 1));


        // Add button on third row (y = 2) spanning two columns (width = 2)
        searchEmailSubjectView.add(searchEmailButton, getGridBagConstraints(0, 2, 2));

        // Add button on third row (y = 2) spanning two columns (width = 2)
        searchEmailSubjectView.add(goBackToHomePage, getGridBagConstraints(0, 3, 2));
        searchEmailSubjectView.add(logOut, getGridBagConstraints(0, 4, 2));
    }


    private void showSearchEmailSubjectView(){

        // Add config panel to the main window and make it visible
        // mainFrame.remove(0);

        mainFrame.add(searchEmailSubjectView);
        mainFrame.setVisible(true);
    }


    // get content of retrived emails


    private void configureGetContentRetreivedEmails(){
        // Create and configure the config panel
        // This will provide a view to take in the user credentials
        // Use a GridBag layout so we have a grid to work with, but there's some flexibility (button can span columns)
        contentRetreivedEmailsView = new JPanel(new GridBagLayout());
        // Register this panel as a container in the system
        guiContainers.put("contentRetreivedEmailsView", contentRetreivedEmailsView);


        idLabel = new JLabel("Email ID: ");
        idTextField = new JTextField(15);


        // Create a button to log in user
        getContentRetreivedEmailsButton = new JButton("Get Content");
        // Specify what the button should DO when clicked:
        getContentRetreivedEmailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentRetreivedEmails();
            }
        });


        goBackToHomePage = new JButton("Go Back To Home Page");
        // Specify what the button should DO when clicked:
        goBackToHomePage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToHomePageGetContent();
            }
        });


        logOut = new JButton("LogOut");
        // Specify what the button should DO when clicked:
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logOutGetContent();
            }
        });



        contentRetreivedEmailsView.add(idLabel, getGridBagConstraints(0, 1, 1));
        contentRetreivedEmailsView.add(idTextField, getGridBagConstraints(1, 1, 1));


        // Add button on third row (y = 2) spanning two columns (width = 2)
        contentRetreivedEmailsView.add(getContentRetreivedEmailsButton, getGridBagConstraints(0, 2, 2));

        // Add button on third row (y = 2) spanning two columns (width = 2)
        contentRetreivedEmailsView.add(goBackToHomePage, getGridBagConstraints(0, 3, 2));
        contentRetreivedEmailsView.add(logOut, getGridBagConstraints(0, 4, 2));
    }


    private void showGetContentRetreivedView(){

        // Add config panel to the main window and make it visible
        // mainFrame.remove(0);

        mainFrame.add(contentRetreivedEmailsView);
        mainFrame.setVisible(true);
    }


    // get content of sent emails



    private void configureGetContentSentEmail(){
        // Create and configure the config panel
        // This will provide a view to take in the user credentials
        // Use a GridBag layout so we have a grid to work with, but there's some flexibility (button can span columns)
        contentSentEmailsView = new JPanel(new GridBagLayout());
        // Register this panel as a container in the system
        guiContainers.put("contentSentEmailsView", contentSentEmailsView);


        id2Label = new JLabel("Email ID: ");
        id2TextField = new JTextField(15);


        // Create a button to log in user
        getContentSentEmailsButton = new JButton("Get Content");
        // Specify what the button should DO when clicked:
        getContentSentEmailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentSentEmail();
            }
        });


        goBackToHomePage = new JButton("Go Back To Home Page");
        // Specify what the button should DO when clicked:
        goBackToHomePage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToHomePageGetContentSent();
            }
        });


        logOut = new JButton("LogOut");
        // Specify what the button should DO when clicked:
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logOutGetContentSent();
            }
        });



        contentSentEmailsView.add(id2Label, getGridBagConstraints(0, 1, 1));
        contentSentEmailsView.add(id2TextField, getGridBagConstraints(1, 1, 1));


        // Add button on third row (y = 2) spanning two columns (width = 2)
        contentSentEmailsView.add(getContentSentEmailsButton, getGridBagConstraints(0, 2, 2));

        // Add button on third row (y = 2) spanning two columns (width = 2)
        contentSentEmailsView.add(goBackToHomePage, getGridBagConstraints(0, 3, 2));
        contentSentEmailsView.add(logOut, getGridBagConstraints(0, 4, 2));
    }


    private void showGetContentSentView(){

        // Add config panel to the main window and make it visible
        // mainFrame.remove(0);

        mainFrame.add(contentSentEmailsView);
        mainFrame.setVisible(true);
    }


    // get retrieved email by id

    private void configureGetRetrievedEmailByID(){
        // Create and configure the config panel
        // This will provide a view to take in the user credentials
        // Use a GridBag layout so we have a grid to work with, but there's some flexibility (button can span columns)
        getRetreivedEmailByIdView = new JPanel(new GridBagLayout());
        // Register this panel as a container in the system
        guiContainers.put("getRetreivedEmailByIdView", getRetreivedEmailByIdView);


        emailIdLabel = new JLabel("Email ID: ");
        emailIdTextField = new JTextField(15);


        // Create a button to log in user
        getRetreivedEmailByIdButton = new JButton("Get Email");
        // Specify what the button should DO when clicked:
        getRetreivedEmailByIdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getRetrivedEmailById();
            }
        });


        goBackToHomePage = new JButton("Go Back To Home Page");
        // Specify what the button should DO when clicked:
        goBackToHomePage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToHomePageGetEmailByIdRetrieved();
            }
        });


        logOut = new JButton("LogOut");
        // Specify what the button should DO when clicked:
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logOutGetRetrievedEmailById();
            }
        });



        getRetreivedEmailByIdView.add(emailIdLabel, getGridBagConstraints(0, 1, 1));
        getRetreivedEmailByIdView.add(emailIdTextField, getGridBagConstraints(1, 1, 1));


        // Add button on third row (y = 2) spanning two columns (width = 2)
        getRetreivedEmailByIdView.add(getRetreivedEmailByIdButton, getGridBagConstraints(0, 2, 2));

        // Add button on third row (y = 2) spanning two columns (width = 2)
        getRetreivedEmailByIdView.add(goBackToHomePage, getGridBagConstraints(0, 3, 2));
        getRetreivedEmailByIdView.add(logOut, getGridBagConstraints(0, 4, 2));
    }


    private void showGetRetreivedEmailByIdView(){

        // Add config panel to the main window and make it visible
        // mainFrame.remove(0);

        mainFrame.add( getRetreivedEmailByIdView);
        mainFrame.setVisible(true);
    }



    // get sent email by id




    private void  configureGetSentEmailByID(){
        // Create and configure the config panel
        // This will provide a view to take in the user credentials
        // Use a GridBag layout so we have a grid to work with, but there's some flexibility (button can span columns)
        getSentEmailByIdView = new JPanel(new GridBagLayout());
        // Register this panel as a container in the system
        guiContainers.put("getSentEmailByIdView",  getSentEmailByIdView);


        emailSentIdLabel = new JLabel("Email ID: ");
        emailSentIdTextField = new JTextField(15);


        // Create a button to log in user
        getSentEmailByIdButton = new JButton("Get Email");
        // Specify what the button should DO when clicked:
        getSentEmailByIdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSentEmailById();
            }
        });


        goBackToHomePage = new JButton("Go Back To Home Page");
        // Specify what the button should DO when clicked:
        goBackToHomePage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToHomePageGetEmailByIdSent();
            }
        });


        logOut = new JButton("LogOut");
        // Specify what the button should DO when clicked:
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logOutGetSentEmailById();
            }
        });



        getSentEmailByIdView.add(emailSentIdLabel, getGridBagConstraints(0, 1, 1));
        getSentEmailByIdView.add(emailSentIdTextField, getGridBagConstraints(1, 1, 1));


        // Add button on third row (y = 2) spanning two columns (width = 2)
        getSentEmailByIdView.add( getSentEmailByIdButton, getGridBagConstraints(0, 2, 2));

        // Add button on third row (y = 2) spanning two columns (width = 2)
        getSentEmailByIdView.add(goBackToHomePage, getGridBagConstraints(0, 3, 2));
        getSentEmailByIdView.add(logOut, getGridBagConstraints(0, 4, 2));
    }


    private void showGetSentEmailByIdView(){

        // Add config panel to the main window and make it visible
        // mainFrame.remove(0);

        mainFrame.add(getSentEmailByIdView);
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

        System.out.println(response);

        JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
        String result = jsonResponse.get("message").getAsString();

        // If the response matches the expected success message, treat user as authenticated
        if (response.equals(AuthUtils.LOGIN_SUCCESSFUL)) {
            JOptionPane.showMessageDialog(initialView, result, "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            mainFrame.remove(initialView);
            showCountView();

            log.info("User {} logged in", username);

            usernameField.setText("");
            passwordField.setText("");

            return;
        }

        if (response.equals(AuthUtils.GOODBYE)){

            JOptionPane.showMessageDialog(initialView, result, "GoodBye",
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

        JOptionPane.showMessageDialog(initialView, result, "Login Failed",
                JOptionPane.ERROR_MESSAGE);

        log.info("User {} failed logged in", username);



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

    private void goToSearchEmailsPage(){

        mainFrame.remove(countCharView);
        showSearchEmailSubjectView();
    }

    private void goToGetContentRetreivedEmailPage(){

        mainFrame.remove(countCharView);
       showGetContentRetreivedView();
    }

    private void goToGetContentSentEmailPage(){

        mainFrame.remove(countCharView);
        showGetContentSentView();
    }


    private void goToGetRetrievedEmilByIdPage(){

        mainFrame.remove(countCharView);
        showGetRetreivedEmailByIdView();
    }


    private void goToGetSentEmilByIdPage(){

        mainFrame.remove(countCharView);
        showGetSentEmailByIdView();
    }

    private void goBackToLogin(){

        mainFrame.remove(registerView);
        showInitialView();
    }



    private void logOutSendEmail(){

        mainFrame.remove(sendEmailView);
        showInitialView();
    }


    private void logOutSearchEmail(){

        mainFrame.remove(searchEmailSubjectView);
        showInitialView();
    }

    private void logOutGetContent(){

        mainFrame.remove(contentRetreivedEmailsView);
        showInitialView();
    }

    private void logOutGetContentSent(){

        mainFrame.remove(contentSentEmailsView);
        showInitialView();
    }

    private void logOutGetRetrievedEmailById(){

        mainFrame.remove(getRetreivedEmailByIdView);
        showInitialView();
    }

    private void logOutGetSentEmailById(){

        mainFrame.remove(getSentEmailByIdView);
        showInitialView();
    }


    private void goBackToHomePageSendEmail(){

        mainFrame.remove(sendEmailView);
        showCountView();
    }


    private void goBackToHomePageSendEmail2(){

        mainFrame.remove(searchEmailSubjectView);
        showCountView();
    }


    private void goBackToHomePageGetContent(){

        mainFrame.remove(contentRetreivedEmailsView);
        showCountView();
    }


    private void goBackToHomePageGetContentSent(){

        mainFrame.remove(contentSentEmailsView);
        showCountView();
    }

    private void goBackToHomePageGetEmailByIdRetrieved(){

        mainFrame.remove(getRetreivedEmailByIdView);
        showCountView();
    }

    private void goBackToHomePageGetEmailByIdSent(){

        mainFrame.remove(getSentEmailByIdView);
        showCountView();
    }

    private void goBackToHomePageEmailList(){

        f.dispose();
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

        // formatting it nice for the user
        JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
        String result = jsonResponse.get("message").getAsString();


        if (response.equalsIgnoreCase(AuthUtils.REGISTER_SUCCESSFUL)) {

            JOptionPane.showMessageDialog(initialView, result, "Register Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            mainFrame.remove(registerView);
            showCountView();

            log.info("User {} registered successful", username);

            usernameTextField1.setText("");
            passwordTextField1.setText("");
            confirmPasswordTextField1.setText("");

            System.out.println(response);
            return;

        }
        JOptionPane.showMessageDialog(initialView, result, "Register Failed",
                JOptionPane.ERROR_MESSAGE);
        log.info("User {} failed registration", username);

    }


    // send Email

    private void sendEmailToUser(){

        String receiverEmail = receiverEmailTextField.getText();
        String subject = subjectTextField.getText();
        String content = contentTextField.getText();


        JsonObject payload = new JsonObject();
        payload.addProperty("receiver", receiverEmail);
        payload.addProperty("subject", subject);
        payload.addProperty("content", content);

        // Create the overall request object
        JsonObject requestJson = new JsonObject();
        // Add the request type/action and payload
        requestJson.addProperty("action", AuthUtils.SEND_EMAIL);
        requestJson.add("payload", payload);

        String request = gson.toJson(requestJson);
        network.send(request);

        // Wait to receive a response to the authentication request
        String response = network.receive();

        // formatting it nice for the user
        JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
        String result = jsonResponse.get("message").getAsString();

        if (response.equalsIgnoreCase(AuthUtils.EMAIL_SUCCESSFULLY_SENT)) {

            JOptionPane.showMessageDialog(initialView, result, "Sent email Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            mainFrame.remove(sendEmailView);
            showSendEmailView();

            log.info("Email was sent to user {}", receiverEmail);


            receiverEmailTextField.setText("");
            subjectTextField.setText("");
            contentTextField.setText("");

            System.out.println(response);
            return;

        }
        JOptionPane.showMessageDialog(initialView, result, "Sent email failed",
                JOptionPane.ERROR_MESSAGE);
        log.info("Email was not sent to user {}", receiverEmail);

    }

    public static String [] grow(String [] data, int numExtraSlots){
        String [] larger = new String[data.length + numExtraSlots];
        for(int i = 0; i < data.length; i++){
            larger[i] = data[i];
        }
        return larger;
    }

    private void retrieveEmailsForUser(){

        // Create the overall request object
        JsonObject requestJson = new JsonObject();
        // Add the request type/action and payload
        requestJson.addProperty("action", AuthUtils.RETRIEVE_EMAILS);

        String request = gson.toJson(requestJson);
        network.send(request);

        // Wait to receive a response to the authentication request
        String response = network.receive();


        if (response.equalsIgnoreCase(AuthUtils.INVALID) || response.equalsIgnoreCase(AuthUtils.YOU_HAVE_NO_EMAILS) || response.equalsIgnoreCase(AuthUtils.NOT_LOGGED_IN)){

            JsonObject jsonResponse1 = gson.fromJson(response, JsonObject.class);
            String result1 = jsonResponse1.get("message").getAsString();

            JOptionPane.showMessageDialog(initialView, result1, "Retrieve Emails failed",
                    JOptionPane.INFORMATION_MESSAGE);

        }else {
            JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
            String result = jsonResponse.get("emails").getAsString();

            String[] emails = result.split("##");

            //create a new frame
            f = new JFrame("frame");

            //create a panel
            JPanel p =new JPanel();

            emailListLabel = new JLabel("List of all your received emails");
            p.add(emailListLabel, getGridBagConstraints(0, 0, 1));

           String [] emailArray = grow(emails, emails.length);
            b = new JList(emailArray);
            b.setSelectedIndex(0);
            p.add(b);
            f.add(p);
            f.setSize(500,400);

            goBackToHomePage = new JButton("Go Back To Home Page");
            // Specify what the button should DO when clicked:
            goBackToHomePage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    goBackToHomePageEmailList();
                }
            });
            p.add(goBackToHomePage, getGridBagConstraints(0, 2, 2));
            f.show();

        }

    }

    // search for email based on subject


    private void searchForEmailBasedOnSubject(){

        String subject = subjectSearchTextField.getText();

        JsonObject payload = new JsonObject();
        payload.addProperty("subject", subject);

        // Create the overall request object
        JsonObject requestJson = new JsonObject();
        // Add the request type/action and payload
        requestJson.addProperty("action", AuthUtils.SEARCH_RETRIEVED_EMAILS);
        requestJson.add("payload", payload);

        String request = gson.toJson(requestJson);
        network.send(request);

        // Wait to receive a response to the authentication request
        String response = network.receive();
        if (response.equals(AuthUtils.NO_EMAILS_WITH_THIS_SUBJECT) || response.equals(AuthUtils.INVALID) || response.equals(AuthUtils.YOU_HAVE_NO_EMAILS) || response.equals(AuthUtils.NOT_LOGGED_IN) || response.equals(AuthUtils.EMPTY_SUBJECT)) {

            JsonObject jsonResponse1 = gson.fromJson(response, JsonObject.class);
            String result1 = jsonResponse1.get("message").getAsString();

            JOptionPane.showMessageDialog(initialView, result1, "No Emails with this subject",
                    JOptionPane.ERROR_MESSAGE);

            log.info("No emails with subject {}", subject);
        }else {
            // formatting it nice for the user
            JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
            String result = jsonResponse.get("emails").getAsString();

            String[] emails = result.split("##");

            //create a new frame
            f = new JFrame("frame");

            //create a panel
            JPanel p =new JPanel();

            emailListLabel = new JLabel("Retrieved email list for the subject: "+subject);
            p.add(emailListLabel, getGridBagConstraints(0, 0, 1));

            String [] emailArray = grow(emails, emails.length);
            b = new JList(emailArray);
            b.setSelectedIndex(0);
            p.add(b);
            f.add(p);
            f.setSize(500,400);

            goBackToHomePage = new JButton("Go Back To Home Page");
            // Specify what the button should DO when clicked:
            goBackToHomePage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    goBackToHomePageEmailList();
                }
            });
            p.add(goBackToHomePage, getGridBagConstraints(0, 2, 2));
            f.show();

            subjectSearchTextField.setText("");

        }

    }


    // get content of retreived emails


    private void getContentRetreivedEmails(){

        String Id = idTextField.getText();

        JsonObject payload = new JsonObject();
        payload.addProperty("Id", Id);

        // Create the overall request object
        JsonObject requestJson = new JsonObject();
        // Add the request type/action and payload
        requestJson.addProperty("action", AuthUtils.GET_CONTENT_RETRIEVED_EMAILS);
        requestJson.add("payload", payload);

        String request = gson.toJson(requestJson);
        network.send(request);

        // Wait to receive a response to the authentication request
        String response = network.receive();

        if (response.equals(AuthUtils.NON_NUMERIC_ID) || response.equals(AuthUtils.EMAIL_ID_DOESNT_EXIST) || response.equals(AuthUtils.INVALID) || response.equals(AuthUtils.NOT_LOGGED_IN) || response.equals(AuthUtils.EMAIL_ID_LESS_THAN_1)) {

            JsonObject jsonResponse1 = gson.fromJson(response, JsonObject.class);
            String result1 = jsonResponse1.get("message").getAsString();

            JOptionPane.showMessageDialog(initialView, result1, "Error",
                    JOptionPane.ERROR_MESSAGE);
        }else {

            JsonObject jsonResponse1 = gson.fromJson(response, JsonObject.class);
            String result = jsonResponse1.get("content").getAsString();

            JOptionPane.showMessageDialog(initialView, result, "Get content of retrieved emails",
                    JOptionPane.INFORMATION_MESSAGE);
            mainFrame.remove(searchEmailSubjectView);
            showGetContentRetreivedView();

            idTextField.setText("");

            System.out.println(response);

        }

    }


    private void getContentSentEmail(){

        String Id = id2TextField.getText();

        JsonObject payload = new JsonObject();
        payload.addProperty("Id", Id);

        // Create the overall request object
        JsonObject requestJson = new JsonObject();
        // Add the request type/action and payload
        requestJson.addProperty("action", AuthUtils.GET_CONTENT_SENT_EMAIL);
        requestJson.add("payload", payload);

        String request = gson.toJson(requestJson);
        network.send(request);

        // Wait to receive a response to the authentication request
        String response = network.receive();

        if (response.equals(AuthUtils.NON_NUMERIC_ID) || response.equals(AuthUtils.EMAIL_ID_DOESNT_EXIST) || response.equals(AuthUtils.INVALID) || response.equals(AuthUtils.NOT_LOGGED_IN) || response.equals(AuthUtils.EMAIL_ID_LESS_THAN_1)) {

            JsonObject jsonResponse1 = gson.fromJson(response, JsonObject.class);
            String result1 = jsonResponse1.get("message").getAsString();

            JOptionPane.showMessageDialog(initialView, result1, "Error",
                    JOptionPane.ERROR_MESSAGE);
        }else {

            JsonObject jsonResponse1 = gson.fromJson(response, JsonObject.class);
            String result = jsonResponse1.get("content").getAsString();

            JOptionPane.showMessageDialog(initialView, result, "Get content of retrieved emails",
                    JOptionPane.INFORMATION_MESSAGE);
            mainFrame.remove(contentSentEmailsView);
            showGetContentSentView();

            id2TextField.setText("");

            System.out.println(response);

        }

    }


    private void getRetrivedEmailById(){

        String Id = emailIdTextField.getText();

        JsonObject payload = new JsonObject();
        payload.addProperty("Id", Id);

        // Create the overall request object
        JsonObject requestJson = new JsonObject();
        // Add the request type/action and payload
        requestJson.addProperty("action", AuthUtils.GET_RETRIEVED_EMAIL_BY_ID);
        requestJson.add("payload", payload);

        String request = gson.toJson(requestJson);
        network.send(request);

        // Wait to receive a response to the authentication request
        String response = network.receive();

        if (response.equals(AuthUtils.NON_NUMERIC_ID) || response.equals(AuthUtils.EMAIL_ID_DOESNT_EXIST) || response.equals(AuthUtils.INVALID) || response.equals(AuthUtils.NOT_LOGGED_IN) || response.equals(AuthUtils.EMAIL_ID_LESS_THAN_1)) {

            JsonObject jsonResponse1 = gson.fromJson(response, JsonObject.class);
            String result1 = jsonResponse1.get("message").getAsString();

            JOptionPane.showMessageDialog(initialView, result1, "Error",
                    JOptionPane.ERROR_MESSAGE);
        }else {

            JsonObject jsonResponse1 = gson.fromJson(response, JsonObject.class);
            String result = jsonResponse1.get("emails").getAsString();

            //create a new frame
            f = new JFrame("frame");
            //create a panel
            JPanel p =new JPanel();

            emailListLabel = new JLabel("Email for ID: "+Id);
            p.add(emailListLabel, getGridBagConstraints(0, 0, 1));

            String [] emailArray = new String[]{result};
            b = new JList(emailArray);
            b.setSelectedIndex(0);
            p.add(b);
            f.add(p);
            f.setSize(500,400);

            goBackToHomePage = new JButton("Go Back To Home Page");
            // Specify what the button should DO when clicked:
            goBackToHomePage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    goBackToHomePageEmailList();
                }
            });
            p.add(goBackToHomePage, getGridBagConstraints(0, 2, 2));
            f.show();

            emailIdTextField.setText("");

        }
    }



    private void getSentEmailById(){

        String Id = emailSentIdTextField.getText();

        JsonObject payload = new JsonObject();
        payload.addProperty("Id", Id);

        // Create the overall request object
        JsonObject requestJson = new JsonObject();
        // Add the request type/action and payload
        requestJson.addProperty("action", AuthUtils.GET_SENT_EMAIL_BY_ID);
        requestJson.add("payload", payload);

        String request = gson.toJson(requestJson);
        network.send(request);

        // Wait to receive a response to the authentication request
        String response = network.receive();

        if (response.equals(AuthUtils.NON_NUMERIC_ID) || response.equals(AuthUtils.EMAIL_ID_DOESNT_EXIST) || response.equals(AuthUtils.INVALID) || response.equals(AuthUtils.NOT_LOGGED_IN) || response.equals(AuthUtils.EMAIL_ID_LESS_THAN_1)) {

            JsonObject jsonResponse1 = gson.fromJson(response, JsonObject.class);
            String result1 = jsonResponse1.get("message").getAsString();

            JOptionPane.showMessageDialog(initialView, result1, "Error",
                    JOptionPane.ERROR_MESSAGE);
        }else {

            JsonObject jsonResponse1 = gson.fromJson(response, JsonObject.class);
            String result = jsonResponse1.get("emails").getAsString();

            //create a new frame
            f = new JFrame("frame");
            //create a panel
            JPanel p =new JPanel();

            emailListLabel = new JLabel("Sent Email for ID: "+Id);
            p.add(emailListLabel, getGridBagConstraints(0, 0, 1));

            String [] emailArray = new String[]{result};
            b = new JList(emailArray);
            b.setSelectedIndex(0);
            p.add(b);
            f.add(p);
            f.setSize(500,400);

            goBackToHomePage = new JButton("Go Back To Home Page");
            // Specify what the button should DO when clicked:
            goBackToHomePage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    goBackToHomePageEmailList();
                }
            });
            p.add(goBackToHomePage, getGridBagConstraints(0, 2, 2));
            f.show();

            emailSentIdTextField.setText("");

        }

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
