package tomato.com.tomato.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException (String message){
        super(message);
    }
}
