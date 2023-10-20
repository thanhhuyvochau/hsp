package fu.hbs.exceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MailExceptionHandler.class)
    public ModelAndView handleMessagingException(MailExceptionHandler ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", "Lỗi gửi email: " + ex.getMessage());
        return modelAndView;
    }
}
