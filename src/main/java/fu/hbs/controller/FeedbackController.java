package fu.hbs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import fu.hbs.entities.Contact;
import fu.hbs.service.dao.FeedbackService;

//@Controller
//@RequestMapping("/feedback")
//public class FeedbackController {
//
//    @Autowired
//    private FeedbackService feedbackService;
//
//    @GetMapping("/form")
//    public String feedbackForm(Model model) {
//        // Điều này trả về một trang HTML chứa form để người dùng nhập thông tin phản hồi.
//        return "feedback-form"; // Tên trang HTML, bạn cần tạo trang này.
//    }
//
//    @PostMapping("/save")
//    public String saveFeedback(@ModelAttribute("feedback") Contact feedback) {
//        try {
//            feedbackService.saveFeedback(feedback);
//            return "success"; // Tên trang thành công, bạn cần tạo trang này.
//        } catch (Exception e) {
//            return "error"; // Tên trang lỗi, bạn cần tạo trang này.
//        }
//    }
//}

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/save")
    public ResponseEntity<String> saveFeedback(@RequestBody Contact feedback) {
        try {
            // Kiểm tra nếu trường "content" là null hoặc trống
//            if (feedback.getContent() == null || feedback.getContent().trim().isEmpty()) {
//                return ResponseEntity.badRequest().body("Nội dung không được trống.");
//            }

            feedbackService.saveFeedback(feedback);
            return ResponseEntity.ok("Phản hồi đã được lưu thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu phản hồi.");
        }
    }
}


