/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 27/10/2023    1.0        HieuLBM          First Deploy
 */
package fu.hbs.exceptionHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import fu.hbs.entities.User;
import fu.hbs.service.dao.UserService;

import java.text.ParseException;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @Autowired
    UserService userService;

    @ExceptionHandler(MailExceptionHandler.class)
    public ModelAndView handleMessagingException(MailExceptionHandler ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", "Lỗi gửi email: " + ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public String handlerContact(UserNotFoundException exception, final Model model) {
        System.err.println(exception.getMessage());
        model.addAttribute("errorMessage", exception.getMessage());

        return "error";
    }

    @ExceptionHandler(value = {UserIvalidException.class})
    public String handlerContact(UserIvalidException exception, final Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserbyEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("errorMessage", exception.getMessage());

        return "userProfile";
    }

    @ExceptionHandler(ParseException.class)
    public String handleParseException(ParseException ex, Model model) {
        String errorMessage = "Ngày sinh không đúng định dạng hoặc không hợp lệ";
        model.addAttribute("customError", errorMessage);
        return "profile/updateProfileCustomer";
    }
}
