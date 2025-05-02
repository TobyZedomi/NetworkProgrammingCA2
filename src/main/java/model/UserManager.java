package model;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.mindrot.jbcrypt.BCrypt;

public class UserManager implements IUserManager {


    // private HashMap<String, User> users = new HashMap<>();

    private Map<String, User> users = new ConcurrentHashMap<>();


    public UserManager(){

        bootstrapUserList();
    }

    // check if user already exist

    public boolean checkIfUserExist(String username){
        boolean match = false;
        if(!users.containsKey(username)) {
            match = true;
        }
        return match;
    }

    // register User

    public boolean registerUser(String username, String password){

        User userToBeRegistered = new User(username,hashPassword(password));

        return register(userToBeRegistered);
    }

    private boolean register(User u){
        boolean added = false;
        if(!users.containsKey(u.getUsername())) {
            added = true;
            users.put(u.getUsername(), u);
        }
        return added;
    }


    // check if passwords are the same

    public boolean checkIfPasswordsAreTheSame(String password, String confirmPassword){

        boolean match = false;

        if (password.equals(confirmPassword)){

            match = true;
        }

        return match;
    }


    // check if password match regex

    public boolean checkIfPasswordsMatchRegex(String password, String confirmPassword){

        boolean match = false;

        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

        if (password.matches(pattern) && confirmPassword.matches(pattern)){

            match = true;
        }


        return match;
    }


    // check if email has correct regex



    public boolean checkIfEmailMatchRegex(String email){

        boolean match = false;

        String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (email.matches(pattern)){

            match = true;
        }


        return match;
    }


    /// check if user is logged in

    public boolean loginUser(String username, String password){

        boolean match = false;

        User u = users.get(username);

        if (u == null){

            match = false;
        }

        if (u != null){
            if (checkPassword(password, u.getPassword())){

                match = true;
            }
        }

        return match;
    }


    // Method to fill the list of quotations with a set of initial quotes
    private void bootstrapUserList()
    {


        users.put("user@gmail.com", new User("user@gmail.com", "$2a$12$x4EwpUD5VU.vJW1.xICz1OnEJqEMfdYx/ttl/Gi/JxljZAsguzqbi"));
        users.put("user1@gmail.com", new User("user1@gmail.com", "$2a$12$x4EwpUD5VU.vJW1.xICz1OnEJqEMfdYx/ttl/Gi/JxljZAsguzqbi"));
        users.put("user2@gmail.com", new User("user2@gmail.com", "$2a$12$x4EwpUD5VU.vJW1.xICz1OnEJqEMfdYx/ttl/Gi/JxljZAsguzqbi"));

    }

    private static int workload = 12;

    public static String hashPassword(String password_plaintext) {
        String salt = BCrypt.gensalt(workload);
        String hashed_password = BCrypt.hashpw(password_plaintext, salt);


        return(hashed_password);
    }


    private static boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified = false;

        if(null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);

        return(password_verified);
    }

}
