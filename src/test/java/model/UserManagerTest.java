package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {

    /**
     * Test for if user exist
     */
    @Test
    void checkIfUserExist() {
        System.out.println("Test for if user exist");

        IUserManager userManager = new UserManager();
        String username = "user@gmail.com";
        boolean result = userManager.checkIfUserExist(username);

        assertEquals(result, false);
    }

    /**
     * Test for if user doesnt exist
     */
    @Test
    void checkIfUserExistButUsernameDoesntExist() {
        System.out.println("Test for if user doesnt exist");

        IUserManager userManager = new UserManager();
        String username = "user456@gmail.com";
        boolean result = userManager.checkIfUserExist(username);

        assertEquals(result, true);
    }

    /**
     * Test for if user exist but user is null
     */
    @Test
    void checkIfUserExistButItsNull() {
        System.out.println("Test for if user doesnt exist but its null");

        IUserManager userManager = new UserManager();
        String username = null;

        assertThrows(NullPointerException.class, () -> {

            userManager.checkIfUserExist(username);
        });
    }

    /**
     * Test to register user
     */

    @Test
    void registerUser() {
        System.out.println("Test to register user");
        IUserManager userManager = new UserManager();

        String username = "user456@gmail.com";
        String password = "Admin123@";

        boolean result = userManager.registerUser(username, password);

        assertEquals(result, true);
    }

    /**
     * Test to register user but username already exist
     */

    @Test
    void registerUserButUsernameAlreadyExist() {
        System.out.println("Test to register user but username already exist");
        IUserManager userManager = new UserManager();

        String username = "user@gmail.com";
        String password = "Admin123@";

        boolean result = userManager.registerUser(username, password);

        assertEquals(result, false);
    }


    /**
     * Test to register user but username and password is null
     */

    @Test
    void registerUserButUsernameAndPasswordIsNull() {
        System.out.println("Test to register user but username and password is null");
        IUserManager userManager = new UserManager();

        String username = null;
        String password = null;


        assertThrows(NullPointerException.class, () -> {

            userManager.registerUser(username, password);
        });
    }

    /**
     * Test for if passwords are the same
     */
    @Test
    void checkIfPasswordsAreTheSame() {

        System.out.println("Test to check if passwords are the same");
        IUserManager userManager = new UserManager();

        String password = "Admin123@";
        String confirmPassword = "Admin123@";

        boolean result = userManager.checkIfPasswordsAreTheSame(password, confirmPassword);

        assertEquals(result, true);
    }

    /**
     * Test for if passwords are not the same
     */
    @Test
    void checkIfPasswordsAreNotTheSame() {

        System.out.println("Test to check if passwords are not the same");
        IUserManager userManager = new UserManager();

        String password = "Admin123@";
        String confirmPassword = "A";

        boolean result = userManager.checkIfPasswordsAreTheSame(password, confirmPassword);

        assertEquals(result, false);
    }


    /**
     * Test for if passwords are null
     */
    @Test
    void checkIfPasswordsAreNull() {

        System.out.println("Test to check if passwords are not the same");
        IUserManager userManager = new UserManager();

        String password = null;
        String confirmPassword = null;

        assertThrows(NullPointerException.class, () -> {

            userManager.checkIfPasswordsAreTheSame(password, confirmPassword);
        });
    }

    /**
     * Test for if password matches format
     */
    @Test
    void checkIfPasswordsMatchRegex() {

        System.out.println("Test for if password matches format");

        IUserManager userManager = new UserManager();

        String password = "Admin123@";
        String confirmPassword = "Admin123@";

        boolean result = userManager.checkIfPasswordsMatchRegex(password,confirmPassword);

        assertEquals(result, true);
    }


    /**
     * Test for if passwords dont match format
     */
    @Test
    void checkIfPasswordsMatchRegexButItDoesntMatch() {

        System.out.println("Test for if password doesnt match format");

        IUserManager userManager = new UserManager();

        String password = "Admin123";
        String confirmPassword = "Admin123@";

        boolean result = userManager.checkIfPasswordsMatchRegex(password,confirmPassword);

        assertEquals(result, false);
    }

    /**
     * Test for if passwords are null for if password matches regex method
     */
    @Test
    void checkIfPasswordsMatchRegexButItsNull() {

        System.out.println("Test for if passwords are null for if password matches regex method");

        IUserManager userManager = new UserManager();

        String password = null;
        String confirmPassword = "Admin123@";


        assertThrows(NullPointerException.class, () -> {

            userManager.checkIfPasswordsMatchRegex(password,confirmPassword);
        });
    }

    /**
     * Test for if username matches format
     */

    @Test
    void checkIfUsernameMatchRegex() {
        System.out.println("Test for if username matches format");

        IUserManager userManager = new UserManager();

        String username = "user@gmail.com";

        boolean result = userManager.checkIfEmailMatchRegex(username);

        assertEquals(result, true);

    }

    /**
     * Test for if username doenst match format
     */

    @Test
    void checkIfUsernameMatchRegexButItDoesntMatch() {
        System.out.println("Test for if username doesntMatchFormat");

        IUserManager userManager = new UserManager();

        String username = "user";

        boolean result = userManager.checkIfEmailMatchRegex(username);

        assertEquals(result, false);
    }


    /**
     * Test for if username doenst match format because its null
     */

    @Test
    void checkIfUsernameMatchRegexButItsNull() {
        System.out.println("Test for if username doesnt Match Format because its null");

        IUserManager userManager = new UserManager();

        String username = null;

        assertThrows(NullPointerException.class, () -> {

            userManager.checkIfEmailMatchRegex(username);
        });

    }

    /**
     * Test to login user
     */

    @Test
    void loginUser() {
        System.out.println("Test to login user");

        IUserManager userManager = new UserManager();

        String username = "user@gmail.com";
        String password = "Admin123@";

        boolean result = userManager.loginUser(username, password);

        assertEquals(result, true);
    }

    /**
     * Test to login user but details are wrong
     */

    @Test
    void loginUserButDetailsAreWrong() {
        System.out.println("Test to login user but details are wrong");

        IUserManager userManager = new UserManager();

        String username = "use342r@gmail.com";
        String password = "Admin123@";

        boolean result = userManager.loginUser(username, password);

        assertEquals(result, false);
    }


    /**
     * Test to login user but null
     */

    @Test
    void loginUserButNull() {
        System.out.println("Test to login user but null");


        IUserManager userManager = new UserManager();

        String username = null;
        String password = "Admin123@";


        assertThrows(NullPointerException.class, () -> {

            userManager.loginUser(username, password);
        });
    }
}