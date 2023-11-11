package fu.hbs.service.impl;

import fu.hbs.dto.CancellationDTO.ViewCancellationDTO;
import fu.hbs.entities.*;
import fu.hbs.repository.*;
import fu.hbs.service.dao.CustomerCancellationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerCancellationServiceImpl implements CustomerCancellationService {
    @Autowired
    CustomerCancellationRepository customerCancellationRepository;
    @Autowired
    HotelBookingRepository hotelBookingRepository;
    @Autowired
    RoomCategoriesRepository roomCategoriesRepository;
    @Autowired
    BookingRoomDetailsRepository bookingRoomDetailsRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public List<ViewCancellationDTO> getAll() {
        List<ViewCancellationDTO> viewCancellationDTOS = new ArrayList<>();
        HotelBooking hotelBooking = new HotelBooking();
        List<BookingRoomDetails> bookingRoomDetailsList = new ArrayList<>();
        BookingRoomDetails bookingRoomDetails = new BookingRoomDetails();
        List<CustomerCancellation> customerCancellationList = customerCancellationRepository.findAll();
        RoomCategories roomCategories = new RoomCategories();
        List<RoomCategories> roomCategoriesList = new ArrayList<>();
        CustomerCancellation customerCancellation = new CustomerCancellation();
        User user = new User();

        for (int i = 0; i < customerCancellationList.size(); i++) {

            ViewCancellationDTO viewCancellationDTO = new ViewCancellationDTO();
            hotelBooking = hotelBookingRepository.findByHotelBookingId(customerCancellationList.get(i).getHotelBookingId());
            user = userRepository.findByUserId(hotelBooking.getUserId());
            if (user == null) {
                continue;
            }
            customerCancellation = customerCancellationRepository.findByCancellationId(customerCancellationList.get(i).getCancellationId());

            viewCancellationDTO.setHotelBooking(hotelBooking);
            viewCancellationDTO.setCustomerCancellation(customerCancellation);
            viewCancellationDTO.setUser(user);

            viewCancellationDTOS.add(viewCancellationDTO);
        }

        return viewCancellationDTOS;
    }
}
