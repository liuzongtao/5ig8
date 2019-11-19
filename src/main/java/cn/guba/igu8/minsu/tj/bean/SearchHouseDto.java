package cn.guba.igu8.minsu.tj.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class SearchHouseDto implements Serializable {

    private SearchHouseParams parameter;
    private ClientBean client;
}
