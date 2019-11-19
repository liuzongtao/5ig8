package cn.guba.igu8.minsu.tj.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TjHouseContentBean {

    private int totalCount;

    private List<TjUnitInfoBean> items;
}
