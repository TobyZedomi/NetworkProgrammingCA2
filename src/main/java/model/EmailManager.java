package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EmailManager {
    private Map<String, ArrayList<Email>> senderEmails = new ConcurrentHashMap<>();
    private Map<String, ArrayList<Email>> receiverEmails = new ConcurrentHashMap<>();
    private final Object emailCountLock = new Object();
    private static int emailIdCount = 0;

    public EmailManager() {

        bootstrapEmailList();
    }

    /// senderEmail an email
    public boolean sendAnEmailToUser(String sender, String receiver, String subject, String message) {
        Email emailToBeSent;
        synchronized (emailCountLock) {
            emailToBeSent =  new Email(emailIdCount, sender, receiver, subject, message, LocalDateTime.now());
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

    private void bootstrapEmailList() {
        ArrayList<Email> email = new ArrayList();

        email.add(new Email(1, "user@gmail.com", "user1@gmail.com", "Hip Hop", "I Love Hip Hop", LocalDateTime.of(2025, 03, 5, 5, 34)));
        email.add(new Email(2, "user1@gmail.com", "user@gmail.com", "Rock", "I Love Rock", LocalDateTime.of(2025, 02, 9, 5, 34)));

        ArrayList<Email> email2 = new ArrayList();

        email2.add(new Email(1, "user1@gmail.com", "user2@gmail.com", "Hip Hop", "I Love Hip Hop", LocalDateTime.of(2025, 03, 5, 5, 34)));
        email2.add(new Email(2, "user2@gmail.com", "user1@gmail.com", "Pop", "I Love Pop", LocalDateTime.of(2025, 02, 9, 5, 34)));


        ArrayList<Email> email3 = new ArrayList();

        email3.add(new Email(1, "user2@gmail.com", "user@gmail.com", "Hip Hop", "I Love Hip Hop", LocalDateTime.of(2025, 03, 5, 5, 34)));
        email3.add(new Email(2, "user2@gmail.com", "user1@gmail.com", "Pop", "I Love Pop", LocalDateTime.of(2025, 02, 9, 5, 34)));

        senderEmails.put("user@gmail.com", email);
        senderEmails.put("user1@gmail.com", email2);
        senderEmails.put("user2@gmail.com", email3);

    }

}
