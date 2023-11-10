package fu.hbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SaleManagerController {

    @GetMapping("/customer/listRefund")
    public String getAllRefund() {
        return "salemanager/listRefund";
    }

    @GetMapping("/customer/ConfirmRefund")
    public String getAllConfirmRefund() {
        return "salemanager/confirmRefund";
    }
}
