package fu.hbs.exceptionHandler;

public class BookingException extends RuntimeException{
    public BookingException(String message) {
        super(message);
    }
}
