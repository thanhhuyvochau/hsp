package fu.hbs.exceptionHandler;

public class MailExceptionHandler extends RuntimeException {
    public MailExceptionHandler(String message) {
        super(message);
    }
}
