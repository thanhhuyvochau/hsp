package fu.hbs.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import fu.hbs.entities.RoomService;
import fu.hbs.service.dao.ServiceService;

@Controller
public class ServiceDetailController {

    private final ServiceService serviceService;

    public ServiceDetailController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/service-details")
    public String getServiceDetail(@RequestParam("serviceId") Long serviceId, Model model) {
        // Truy vấn dịch vụ dựa trên serviceId
    	RoomService service = serviceService.findById(serviceId);

        // Đưa dịch vụ vào model để hiển thị trong trang service-detail.html
        model.addAttribute("service", service);

        return "serviceDetail";
    }

}

