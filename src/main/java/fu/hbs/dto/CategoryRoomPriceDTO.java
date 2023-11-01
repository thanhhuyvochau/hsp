package fu.hbs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryRoomPriceDTO {
    private Long roomCategoryId;
    private Date changeDate;
    private BigDecimal price;
    private Date createdDate;
    private Date updatedDate;
}
