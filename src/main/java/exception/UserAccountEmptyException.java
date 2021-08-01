package exception;

public class UserAccountEmptyException extends RuntimeException{

    public UserAccountEmptyException(String message) {
        super(message);
    }
}
