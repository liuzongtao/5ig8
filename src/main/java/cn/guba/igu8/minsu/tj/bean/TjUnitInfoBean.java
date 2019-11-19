package cn.guba.igu8.minsu.tj.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TjUnitInfoBean {

    private String unitId;
    private String unitName;
    private String houseTypeName;

    private String longitude;
    private String latitude;


    private int cityId;
    private String cityName;
    private String districtName;
    private String address;
    private String unitInfor;

    private String productPrice;
    private String finalPrice;

    private String rankScore;
    private String rankScore2;

    private Boolean allowBooking;

}
