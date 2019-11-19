package cn.guba.igu8.minsu.tj.bean;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SearchHouseParams {

    private Map<String, String> abTest;
    private String checkInDate;
    private String checkOutDate;
    private boolean favorite;
    private List<Integer> houseIdList;
    private boolean needUnactive;
    private boolean noNeedPrice;
}
