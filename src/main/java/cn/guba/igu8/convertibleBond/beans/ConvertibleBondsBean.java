package cn.guba.igu8.convertibleBond.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvertibleBondsBean {

    private int page;

    private List<ConvertibleBondBean> rows;

    private int total;
}
