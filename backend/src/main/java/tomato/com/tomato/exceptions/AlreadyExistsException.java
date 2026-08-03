package tomato.com.tomato.exceptions;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException (String message){
        super(message);
    }

}
