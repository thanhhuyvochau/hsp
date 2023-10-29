package fu.hbs.service.dao;

import org.springframework.stereotype.Service;

import fu.hbs.entities.Contact;

@Service
public interface FeedbackService {
    void saveFeedback(Contact feedback);
}

