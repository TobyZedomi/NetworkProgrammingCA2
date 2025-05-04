package model;

import com.google.gson.JsonObject;
import service.UserUtilities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EmailManager implements IEmailManager {
    private Map<String, ArrayList<Email>> senderEmails = new ConcurrentHashMap<>();
    private Map<String, ArrayList<Email>> receiverEmails = new ConcurrentHashMap<>();
    private final Object emailCountLock = new Object();
    private static int emailIdCount = 0;

    public EmailManager() {

        bootstrapEmailList();

        bootstrapReceiverEmailList();
    }

    // Add

    /**
     * Adding an username to an arraylist
     * @param username is the username being added
     * @return true if added and false if not added
     */

    public boolean addEmail(String username){

        ArrayList<Email> emailList = new ArrayList<>();

        return add(username, emailList);
    }

    /**
     * Adding an arraylist and username to the receiver and sender hashmap
     * @param username is the key being added to the hashmap
     * @param emailList is the arraylist of emails being added as the value
     * @return true if added and false if not added
     */
    private boolean add(String username, ArrayList<Email> emailList){
        boolean added = false;
        if(!senderEmails.containsKey(username)) {
            added = true;
            senderEmails.put(username, emailList);
            receiverEmails.put(username, emailList);
        }
        return added;
    }



    /// senderEmail an email

    /**
     * Sending an email to a particular user in the system
     * @param sender is the user sending the email
     * @param receiver is the user getting the email
     * @param subject is what the email is about
     * @param content is the message of the email
     * @param dateTime is when the email was sent
     * @return true if email was sent and true if not sent
     */
    public boolean sendAnEmailToUser(String sender, String receiver, String subject, String content, LocalDateTime dateTime) {
        Email emailToBeSent;
        synchronized (emailCountLock) {
            emailToBeSent =  new Email(emailIdCount, sender, receiver, subject, content, dateTime);
            emailIdCount++;
        }
        return sendEmail(sender, receiver, emailToBeSent);
    }

    /// one email in the parameter

    // get it based off the key, if exist i can just add it. Use add for the arraylist to put the email in

    /**
     * When sending an email it putd the email in both senderEmail and receiverEmail hashmap
     * @param sender is the user sending the email
     * @param receiver is the user receiving the email
     * @param email is the email being sent
     * @return true if email was added to hashmap and false if not
     */
    private boolean sendEmail(String sender, String receiver, Email email) {
        synchronized (senderEmails) {
            if (senderEmails.containsKey(sender)) {
                senderEmails.get(sender).add(email);
                receiverEmails.get(receiver).add(email);
                return true;
            }
        }
        return false;
    }


    // check if receiver exist

    public boolean checkIfReceiverExist(String receiver){
        boolean match = false;
        if(receiverEmails.containsKey(receiver)) {
            match = true;
        }
        return match;
    }


    // check if email matches regex

    /**
     * Checking if email matches regex format
     * @param email is the email being searched
     * @return true if match and false if no match
     */

    public boolean checkIfEmailMatchRegex(String email){

        boolean match = false;
        String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (email.matches(pattern)){

            match = true;
        }
        return match;
    }


    // retrieve emails for the logged in user

    /**
     * Search for retrieved emails based on username
     * @param username is the username being searched
     * @return an arraylist of retrieved emails
     */

    public ArrayList<Email> searchForRetrievedEmails(String username){

        ArrayList<Email> retrievedEmails = new ArrayList<>();

        ArrayList<Email> email = receiverEmails.get(username);

                for (int i = 0; i < email.size(); i++) {

                    retrievedEmails.add(email.get(i));
                }
        return retrievedEmails;
    }



    // search for email based on subject

    /**
     * Search for retrieved emails based on te subject and username
     * @param username is the username being searched
     * @param subject is the subject being searched
     * @return an arraylist of retrieved emails based on username and subject
     */

    public ArrayList<Email> searchForRetrievedEmailsBasedOnSubject(String username, String subject){

        ArrayList<Email> retrievedEmails = new ArrayList<>();

        ArrayList<Email> email = receiverEmails.get(username);

        for (int i = 0; i < email.size(); i++) {

            if (email.get(i).getSubject().equalsIgnoreCase(subject)) {

                retrievedEmails.add(email.get(i));

            }
        }
        return retrievedEmails;
    }


    // get the content of a particular email

    /**
     * Get the content of a particular email based on username and email id
     * @param username is the username being searched
     * @param emailId is the email being searched
     * @return email that is found
     */

    public Email getContentOfParticularReceivedEmail(String username, int emailId){

        ArrayList<Email> emails = receiverEmails.get(username);

        Email email = null;

        for (int i = 0; i < emails.size();i++){

            if (emails.get(i).getID() == emailId){

               email = (emails.get(i));

            }
        }

        return email;
    }


    // check if email id exist

    /**
     * Check if the received email exist based on username and id
     * @param username is the username being searched
     * @param id is the id being searched
     * @return true if found and false if not found
     */

    public boolean checkIfReceivedEmailIdExist(String username, int id){

        ArrayList<Email> emails = receiverEmails.get(username);

        for (int i = 0; i < emails.size();i++){

            if (emails.get(i).getID() == id){

                return true;
            }
        }
        return false;
    }


    /// get content of sentEmails

    /**
     * Get email based on username and id
     * @param username is the username that is being searched
     * @param emailId is the email id that s being searched
     * @return email that is found
     */

    public Email getContentOfParticularSentEmail(String username, int emailId){

        ArrayList<Email> emails = senderEmails.get(username);

        Email email = null;

        for (int i = 0; i < emails.size();i++){

            if (emails.get(i).getID() == emailId){

                email = (emails.get(i));

            }
        }

        return email;
    }


    // check if sender email id exist

    /**
     * Check if email sent email id exist based on username and id
     * @param username is the username being searched
     * @param id is the id being searched
     * @return true if found and false if not found
     */

    public boolean checkIfSendEmailIdExist(String username, int id){

        ArrayList<Email> emails = senderEmails.get(username);

        for (int i = 0; i < emails.size();i++){

            if (emails.get(i).getID() == id){

                return true;
            }
        }
        return false;
    }


    private void bootstrapEmailList() {
        ArrayList<Email> email = new ArrayList();

        email.add(new Email(1, "user@gmail.com", "user1@gmail.com", "Hip Hop", "I Love Hip Hop", LocalDateTime.of(2025, 03, 5, 5, 34)));

        ArrayList<Email> email2 = new ArrayList();

        email2.add(new Email(1, "user1@gmail.com", "user2@gmail.com", "Hip Hop", "I Love Hip Hop", LocalDateTime.of(2025, 03, 5, 5, 34)));


        ArrayList<Email> email3 = new ArrayList();

        email3.add(new Email(1, "user2@gmail.com", "user@gmail.com", "Hip Hop", "I Love Hip Hop", LocalDateTime.of(2025, 03, 5, 5, 34)));

        senderEmails.put("user@gmail.com", email);
        senderEmails.put("user1@gmail.com", email2);
        senderEmails.put("user2@gmail.com", email3);
    }

    private void bootstrapReceiverEmailList(){

        ArrayList<Email> email = new ArrayList();
        email.add(new Email(1, "user1@gmail.com", "user@gmail.com", "Rock", "I Love Rock", LocalDateTime.of(2025, 02, 9, 5, 34)));

        ArrayList<Email> email2 = new ArrayList();
        email2.add(new Email(1, "user2@gmail.com", "user1@gmail.com", "Pop", "I Love Pop", LocalDateTime.of(2025, 02, 9, 5, 34)));
        email2.add(new Email(2, "user@gmail.com", "user1@gmail.com", "Rock", "I Love Rock", LocalDateTime.of(2025, 9, 20, 5, 34)));
        email2.add(new Email(3, "user@gmail.com", "user1@gmail.com", "Hip Hop", "I Love Hip Hop", LocalDateTime.of(2025, 12, 19, 5, 34)));


        ArrayList<Email> email3 = new ArrayList();
        email3.add(new Email(1, "user2@gmail.com", "user2@gmail.com", "Pop", "I Love Pop", LocalDateTime.of(2025, 02, 9, 5, 34)));

        receiverEmails.put("user@gmail.com", email);
        receiverEmails.put("user1@gmail.com", email2);
        receiverEmails.put("user2@gmail.com", email3);
    }

}
