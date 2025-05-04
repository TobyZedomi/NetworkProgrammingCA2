package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface IEmailManager {

     boolean addEmail(String username);

     boolean sendAnEmailToUser(String sender, String receiver, String subject, String content, LocalDateTime dateTime);

     boolean checkIfReceiverExist(String receiver);

     boolean checkIfEmailMatchRegex(String email);

     ArrayList<Email> searchForRetrievedEmails(String username);

     ArrayList<Email> searchForRetrievedEmailsBasedOnSubject(String username, String subject);
     Email getRecievedEmailBasedOnUsernameAndEmailId(String username, int emailId);
     boolean checkIfReceivedEmailIdExist(String username, int id);

      Email getSenderEmailBasedOnUsernameAndEmailId(String username, int emailId);
      boolean checkIfSendEmailIdExist(String username, int id);
      boolean checkIfIdIsLessThan1(int id);

}
