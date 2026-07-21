package tomato.com.restaurant.exceptions;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException (String message){
        super(message);
    }

}
