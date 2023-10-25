package fu.hbs.exceptionHandler;

import java.io.IOException;

public class FileNotFoundExceptionHandler extends IOException {
	private static final long serialVersionUID = 1L;

	public FileNotFoundExceptionHandler(String message) {
		super(message);
	}

}
