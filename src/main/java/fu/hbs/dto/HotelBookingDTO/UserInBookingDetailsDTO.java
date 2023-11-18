package fu.hbs.dto.HotelBookingDTO;

import fu.hbs.entities.BankList;
import fu.hbs.entities.CustomerCancellation;
import fu.hbs.entities.RefundAccount;
import fu.hbs.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInBookingDetailsDTO {
    private User user;
    private RefundAccount refundAccount;
    private BankList bankList;
    private CustomerCancellation customerCancellation;
}
