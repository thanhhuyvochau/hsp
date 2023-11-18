package fu.hbs.dto.VnpayDTO;

import fu.hbs.entities.PaymentType;
import fu.hbs.entities.Transactions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ViewPaymentDTO {
    private PaymentType paymentType;
    private Transactions Transactions;
}
