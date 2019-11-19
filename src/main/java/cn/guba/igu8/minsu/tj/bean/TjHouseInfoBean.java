package cn.guba.igu8.minsu.tj.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TjHouseInfoBean {

    private String trace;
    private String referTraceId;
    private long errorCode;
    private String errorMessage;
    private TjHouseContentBean content;
}
