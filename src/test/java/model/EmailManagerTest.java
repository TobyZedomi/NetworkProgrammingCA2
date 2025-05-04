package model;

import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

class EmailManagerTest {

    /**
     * Test to add a user to an email
     */

    @Test
    void addEmail() {
        System.out.println("Testing adding Email for User");

        IEmailManager emailManager = new EmailManager();

        String username = "user56@gmail.com";

        boolean result = emailManager.addEmail(username);

        assertEquals(result, true);
    }

    /**
     * Test to add User to an email but they already exist and has an email
     */

    @Test
    void addEmailButUserAlreadyExist() {
        System.out.println("Testing adding Email for User but they already exist and has an email");

        IEmailManager emailManager = new EmailManager();

        String username = "user@gmail.com";

        boolean result = emailManager.addEmail(username);

        assertEquals(result, false);
    }

    /**
     * Send an email to a user in the system
     */
    @Test
    void sendAnEmailToUser() {

        System.out.println("Send email to a user in the system");

        IEmailManager emailManager = new EmailManager();

        String sender = "user@gmail.com";
        String receiver = "user1@gmail.com";
        String subject = "Hip Hop";
        String content = "Kendrick Lamar";
        LocalDateTime date = LocalDateTime.now();

       boolean result = emailManager.sendAnEmailToUser(sender, receiver, subject, content, date);

       assertEquals(result, true);
    }

    /**
     * Send email to user but sender doesnt exist
     */
    @Test
    void sendAnEmailToUserButSenderDoesntExist() {

        System.out.println("Send email to a user in the system but sender doesnt exist");

        IEmailManager emailManager = new EmailManager();

        String sender = "user45@gmail.com";
        String receiver = "user1@gmail.com";
        String subject = "Hip Hop";
        String content = "Kendrick Lamar";
        LocalDateTime date = LocalDateTime.now();

        boolean result = emailManager.sendAnEmailToUser(sender, receiver, subject, content, date);

        assertEquals(result, false);
    }

    /**
     * Send email to user but sender is null
     */
    @Test
    void sendAnEmailToUserButSenderIsNull() {

        System.out.println("Send email to a user in the system but sender is null");

        IEmailManager emailManager = new EmailManager();

        String sender = null;
        String receiver = "user1@gmail.com";
        String subject = "Hip Hop";
        String content = "Kendrick Lamar";
        LocalDateTime date = LocalDateTime.now();

        assertThrows(NullPointerException.class, () -> {

            emailManager.sendAnEmailToUser(sender, receiver, subject, content, date);
        });
    }

    /**
     * Send email to user but receiver doesnt exist and is null
     */
    @Test
    void sendAnEmailToUserButReceiverDoesntExist() {

        System.out.println("Send email to a user in the system but receiver doesnt exist and is null");

        IEmailManager emailManager = new EmailManager();

        String sender = "user@gmail.com";
        String receiver = "user100@gmail.com";
        String subject = "Hip Hop";
        String content = "Kendrick Lamar";
        LocalDateTime date = LocalDateTime.now();

        assertThrows(NullPointerException.class, () -> {

            emailManager.sendAnEmailToUser(sender, receiver, subject, content, date);
        });
    }

    /**
     * Test for when receiver exist
     */

    @Test
    void checkIfReceiverExist() {

        System.out.println("Test for when receiver exist");

        IEmailManager emailManager = new EmailManager();

        String receiver = "user@gmail.com";

        boolean result = emailManager.checkIfReceiverExist(receiver);

        assertEquals(result, true);
    }

    /**
     * Test for when receiver doesnt exist
     */

    @Test
    void checkIfReceiverDoesntExist() {

        System.out.println("Test for when receiver doesnt exist");

        IEmailManager emailManager = new EmailManager();

        String receiver = "user505@gmail.com";

        boolean result = emailManager.checkIfReceiverExist(receiver);

        assertEquals(result, false);
    }

    /**
     * Test for when receiver is null
     */

    @Test
    void checkIfReceiverIsNull() {

        System.out.println("Test for when receiver is null");

        IEmailManager emailManager = new EmailManager();

        String receiver = null;

        assertThrows(NullPointerException.class, () -> {

            emailManager.checkIfReceiverExist(receiver);
        });

    }

    /**
     * test for if email matches format
     */

    @Test
    void checkIfEmailMatchRegex() {

        System.out.println("Test for when email matches format");

        IEmailManager emailManager = new EmailManager();

        String email = "user@gmail.com";

        boolean result = emailManager.checkIfEmailMatchRegex(email);

        assertEquals(result, true);
    }

    /**
     * test for if email doesn't match format
     */

    @Test
    void checkIfEmailDoesntMatchRegex() {

        System.out.println("Test for when email doesnt match format");

        IEmailManager emailManager = new EmailManager();

        String email = "user";

        boolean result = emailManager.checkIfEmailMatchRegex(email);

        assertEquals(result, false);
    }

    /**
     * test for if email is null
     */

    @Test
    void checkIfEmailDoesntMatchRegexCauseItsNull() {

        System.out.println("Test for when email doesnt match format cause its null");

        IEmailManager emailManager = new EmailManager();

        String email = null;

        assertThrows(NullPointerException.class, () -> {

            emailManager.checkIfEmailMatchRegex(email);
        });
    }

    /**
     * Test to search for retrieved emails by username
     */

    @Test
    void searchForRetrievedEmails() {

        System.out.println("Test to search for retrieved emails by username");

        IEmailManager emailManager = new EmailManager();

        String username = "user@gmail.com";

        ArrayList<Email> result = emailManager.searchForRetrievedEmails(username);

        ArrayList<Email> expectedResults = new ArrayList();
        expectedResults.add(new Email(1, "user1@gmail.com", "user@gmail.com", "Rock", "I Love Rock", LocalDateTime.of(2025, 02, 9, 5, 34)));


        assertEquals(result, expectedResults);
    }

    /**
     * Test to search for retrieved emails but username doesnt exist
     */

    @Test
    void searchForRetrievedEmailsButUsernameDoesntExist() {

        System.out.println("Test to search for retrieved emails");

        IEmailManager emailManager = new EmailManager();

        String username = "user405@gmail.com";

        ArrayList<Email> result =  emailManager.searchForRetrievedEmails(username);
         assertNull(result);
    }

    /**
     * Test to search for retrieved emails based on subject and username
     */

    @Test
    void searchForRetrievedEmailsBasedOnSubjectAndUsername() {

        System.out.println("Test to search for retrieved emails based on subject and username");

        IEmailManager emailManager = new EmailManager();
        String username = "user@gmail.com";
        String subject = "Rock";

        ArrayList<Email> result = emailManager.searchForRetrievedEmailsBasedOnSubject(username, subject);

        ArrayList<Email> expectedResults = new ArrayList();
        expectedResults.add(new Email(1, "user1@gmail.com", "user@gmail.com", "Rock", "I Love Rock", LocalDateTime.of(2025, 02, 9, 5, 34)));

        assertEquals(result, expectedResults);
    }


    /**
     * Test to search for retrieved emails based on subject and username taht doesnt exist
     */

    @Test
    void searchForRetrievedEmailsBasedOnSubjectAndUsernameThatDoesntExist() {

        System.out.println("Test to search for retrieved emails based on subject and username doesnt exist");

        IEmailManager emailManager = new EmailManager();
        String username = "user405@gmail.com";
        String subject = "Rock";

        assertThrows(NullPointerException.class, () -> {

            emailManager.searchForRetrievedEmailsBasedOnSubject(username, subject);
        });
    }

    /**
     * Test to search for retrieved emails based on subject that doesnt exist and username
     */

    @Test
    void searchForRetrievedEmailsBasedOnSubjectThatDoesntExistAndUsername() {

        System.out.println("Test to search for retrieved emails based on subject that doesnt exist and username ");

        IEmailManager emailManager = new EmailManager();
        String username = "user@gmail.com";
        String subject = "Hello";
        ArrayList<Email> result = emailManager.searchForRetrievedEmailsBasedOnSubject(username, subject);

        ArrayList<Email> expectedResults = new ArrayList();
        expectedResults.add(new Email(1, "user1@gmail.com", "user@gmail.com", "Rock", "I Love Rock", LocalDateTime.of(2025, 02, 9, 5, 34)));

        assertNotEquals(result, expectedResults);
    }

    /**
     * Test to get received email based on username and id
     */

    @Test
    void getContentOfParticularReceivedEmail() {

        System.out.println("Test to get received email based on username and id");

        IEmailManager emailManager = new EmailManager();

        String username = "user@gmail.com";
        int id = 1;

        Email result = emailManager.getContentOfParticularReceivedEmail(username, id);
        Email expectedResults =  new Email(1, "user1@gmail.com", "user@gmail.com", "Rock", "I Love Rock", LocalDateTime.of(2025, 02, 9, 5, 34));
        assertEquals(result, expectedResults);
    }

    /**
     * Test to get received email based on username that doesnt exist and id
     */

    @Test
    void getContentOfParticularReceivedEmailBasedOnUsernameThatDoesntExist() {

        System.out.println("Test to get received email based on username and id");

        IEmailManager emailManager = new EmailManager();

        String username = "user432@gmail.com";
        int id = 1;

        assertThrows(NullPointerException.class, () -> {

            emailManager.getContentOfParticularReceivedEmail(username, id);
        });
    }

    /**
     * Test to get received email based on username and id that doesnt exist
     */

    @Test
    void getContentOfParticularReceivedEmailBasedOnEmailIdThatDoesntExist() {

        System.out.println("Test to get received email based on username and id that doesnt exist");

        IEmailManager emailManager = new EmailManager();

        String username = "user@gmail.com";
        int id = 11;

        Email result = emailManager.getContentOfParticularReceivedEmail(username, id);
        Email expectedResults =  new Email(1, "user1@gmail.com", "user@gmail.com", "Rock", "I Love Rock", LocalDateTime.of(2025, 02, 9, 5, 34));
        assertNotEquals(result, expectedResults);
    }

    /**
     * Test to see received email exist
     */

    @Test
    void checkIfReceivedEmailIdExist() {

        System.out.println("Test to see received email exist");
        IEmailManager emailManager = new EmailManager();

        String username = "user@gmail.com";
        int id = 1;

        boolean result = emailManager.checkIfReceivedEmailIdExist(username, id);
        assertEquals(result, true);
    }

    /**
     * Test to see received email exist but id doesnt exist
     */

    @Test
    void checkIfReceivedEmailIdExistButIdDoesntExist() {

        System.out.println("Test to see received email exist but id doesntExist");
        IEmailManager emailManager = new EmailManager();

        String username = "user@gmail.com";
        int id = 11;

        boolean result = emailManager.checkIfReceivedEmailIdExist(username, id);
        assertEquals(result, false);
    }

    /**
     * Test to see received email exist but username doesnt exist
     */

    @Test
    void checkIfReceivedEmailIdExistButUsernameDoesntExist() {

        System.out.println("Test to see received email exist but username doesnt exist");
        IEmailManager emailManager = new EmailManager();

        String username = "user432@gmail.com";
        int id = 1;

        assertThrows(NullPointerException.class, () -> {

            emailManager.checkIfReceivedEmailIdExist(username, id);
        });
    }

    /**
     * Get sent email based on username and id
     */

    @Test
    void getContentOfParticularSentEmail() {

        System.out.println("Get sent email based on username and id");
        IEmailManager emailManager = new EmailManager();

        String username = "user@gmail.com";
        int id = 1;

        Email result = emailManager.getContentOfParticularSentEmail(username, id);
        Email expectedResults =  new Email(1, "user@gmail.com", "user1@gmail.com", "Hip Hop", "I Love Hip Hop", LocalDateTime.of(2025, 03, 5, 5, 34));
        assertEquals(result, expectedResults);
    }

    /**
     * Get sent email based on username and id doesnt exist
     */

    @Test
    void getContentOfParticularSentEmailButIdDoesntExist() {

        System.out.println("Get sent email based on username and id doesnt exist");
        IEmailManager emailManager = new EmailManager();

        String username = "user@gmail.com";
        int id = 11;

        Email result = emailManager.getContentOfParticularSentEmail(username, id);
        Email expectedResults =  new Email(1, "user@gmail.com", "user1@gmail.com", "Hip Hop", "I Love Hip Hop", LocalDateTime.of(2025, 03, 5, 5, 34));
        assertNotEquals(result, expectedResults);
    }


    /**
     * Test to see received email exist but username doesnt exist
     */

    @Test
    void  getContentOfParticularSentEmailButUsernameDoesntExist() {

        System.out.println("Test to see received email exist but username doesnt exist");
        IEmailManager emailManager = new EmailManager();

        String username = "user432@gmail.com";
        int id = 1;

        assertThrows(NullPointerException.class, () -> {

            emailManager.getContentOfParticularSentEmail(username, id);
        });
    }


    /**
     * Test to check if sent email exist
     */
    @Test
    void checkIfSendEmailIdExist() {

        System.out.println("Test to see sent email exist");
        IEmailManager emailManager = new EmailManager();

        String username = "user@gmail.com";
        int id = 1;

        boolean result = emailManager.checkIfSendEmailIdExist(username, id);
        assertEquals(result, true);

    }


    /**
     * Test to check if sent email exist when id doesnt exist
     */
    @Test
    void checkIfSendEmailIdExistWhenIdDoesntExist() {

        System.out.println("Test to see sent email exist when id doesnt exist");
        IEmailManager emailManager = new EmailManager();

        String username = "user@gmail.com";
        int id = 11;

        boolean result = emailManager.checkIfSendEmailIdExist(username, id);
        assertEquals(result, false);

    }


    /**
     * Test to see sent email exist but username doesnt exist
     */

    @Test
    void checkIfSendEmailIdExistWhenUseranmeDoesntExist() {

        System.out.println("Test to see sent email exist when username doesnt exist");
        IEmailManager emailManager = new EmailManager();

        String username = "user432@gmail.com";
        int id = 1;

        assertThrows(NullPointerException.class, () -> {

            emailManager.checkIfSendEmailIdExist(username, id);
        });
    }

}