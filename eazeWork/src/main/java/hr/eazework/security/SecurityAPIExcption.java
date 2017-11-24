package hr.eazework.security;

public class SecurityAPIExcption extends Exception {

    private static final long serialVersionUID = 5671329085823446725L;



    private String message = null;


    /**
     * Custom Exception handler thrown from the EncryptionDecryption class
     *
     */
    public SecurityAPIExcption(String message) {
        super(message);
        this.message = message;
    }




}
