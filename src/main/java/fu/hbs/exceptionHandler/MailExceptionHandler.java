package fu.hbs.exceptionHandler;

public class MailExceptionHandler extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MailExceptionHandler(String message) {
		super(message);
	}
}
