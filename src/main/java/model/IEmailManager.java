package model;

import java.time.LocalDateTime;

public interface IEmailManager {

    public boolean addEmail(String email);

    public boolean sendAnEmailToUser(String sender, String receiver, String subject, String message, LocalDateTime dateTime);

    public boolean checkIfReceiverExist(String receiver);

    public boolean checkIfEmailMatchRegex(String email);

}
