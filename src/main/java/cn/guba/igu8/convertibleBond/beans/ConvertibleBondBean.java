package cn.guba.igu8.convertibleBond.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvertibleBondBean {

    private String id;

    private ConvertibleBondDetailBean cell;

}
