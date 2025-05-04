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
    private static int emailIdCount = 1;

    public EmailManager() {

        bootstrapEmailList();

        bootstrapReceiverEmailList();
    }

    // Add

    public boolean addEmail(String email){

        ArrayList<Email> emailList = new ArrayList<>();
        ArrayList<Email> emailList2 = new ArrayList<>();


        return add(email, emailList, emailList2);
    }

    private boolean add(String email, ArrayList<Email> emailList, ArrayList<Email> emailList2){
        boolean added = false;
        if(!senderEmails.containsKey(email)) {
            added = true;
            senderEmails.put(email, emailList);
            receiverEmails.put(email, emailList2);
        }
        return added;
    }



    /// senderEmail an email
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

    public boolean checkIfEmailMatchRegex(String email){

        boolean match = false;
        String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (email.matches(pattern)){

            match = true;
        }
        return match;
    }


    // retrieve emails for the logged in user

    public ArrayList<Email> searchForRetrievedEmails(String username){

        ArrayList<Email> emails = receiverEmails.get(username);

        return emails;
    }



    // search for email based on subject


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
