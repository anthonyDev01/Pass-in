package anthonydev.com.passin.domain.chekin.exceptions;

public class CheckInAlreadyExistsException extends RuntimeException{
    public CheckInAlreadyExistsException(String message){
        super(message);
    }
}
