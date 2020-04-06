package cn.guba.igu8.stock.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockRestBean {

    private int id;

    private String code;

    private String title;

    private Date start;
}
