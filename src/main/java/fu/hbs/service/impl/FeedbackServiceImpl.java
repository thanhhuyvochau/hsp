package fu.hbs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fu.hbs.entities.Contact;
import fu.hbs.repository.ContactRepository;
import fu.hbs.service.dao.FeedbackService;

@Service
public class FeedbackServiceImpl implements FeedbackService {
	private final ContactRepository contactRepository;

    @Autowired
    public FeedbackServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

//    @Override
//    public void saveFeedback(Contact feedback) {
//        // Đảm bảo rằng các trường cần thiết của feedback đã được thiết lập trước
//        feedback.setIsRead(false);
//        feedback.setStatus(true);
//        contactRepository.save(feedback);
//    }
//}
    @Override
    public void saveFeedback(Contact feedback) {
        // Tìm kiếm liên hệ bằng địa chỉ email
        Contact existingContact = contactRepository.findByEmail(feedback.getEmail());

        if (existingContact != null) {
            // Nếu email đã tồn tại, cập nhật trường "user_id"
            //existingContact.setUserId(feedback.getUserId());
            contactRepository.save(existingContact);
        } else {
            // Nếu không tìm thấy email, thêm liên hệ mới
            feedback.setIsRead(false);
            feedback.setStatus(true);
//            feedback.setUserId(6L); // Đặt user_id là 6
            contactRepository.save(feedback);
        }
    }
}
