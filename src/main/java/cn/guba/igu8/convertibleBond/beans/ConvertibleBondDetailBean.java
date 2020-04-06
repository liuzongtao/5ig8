package cn.guba.igu8.convertibleBond.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nutz.json.JsonField;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvertibleBondDetailBean implements Comparable<ConvertibleBondDetailBean> {

    /**
     * 转债代码
     */
    @JsonField("bond_id")
    private String bondId;

    /**
     * 转债名称
     */
    @JsonField("bond_nm")
    private String bondNm;

    /**
     * 现价
     */
    @JsonField("price")
    private float price;

    /**
     * 可转债涨跌幅("0.52%")
     */
    @JsonField("increase_rt")
    private String increaseRt;

    /**
     * 正股代码
     */
    @JsonField("stock_id")
    private String stockId;

    /**
     * 正股名称
     */
    @JsonField("stock_nm")
    private String stockNm;

    /**
     * 正股价
     */
    @JsonField("sprice")
    private float sprice;

    /**
     * 正股涨跌幅("4.05%")
     */
    @JsonField("sincrease_rt")
    private String sincreaseRt;

    /**
     * 转股价（不变）
     */
    @JsonField("convert_price")
    private float convertPrice;

    /**
     * 到期时间
     */
    @JsonField("maturity_dt")
    private Date maturityDt;

    /**
     * 溢价率("-5.63%")
     */
    @JsonField("premium_rt")
    private String premiumRt;

    /**
     * 评级(AA-)
     */
    @JsonField("rating_cd")
    private String ratingCd;

    /**
     * 到期税后收益率("-8.68%")
     */
    @JsonField("ytm_rt_tax")
    private String ytmRtTax;

    /**
     * 剩余年限
     */
    @JsonField("year_left")
    private float yearLeft;

    /**
     * 强赎触发价
     */
    @JsonField("force_redeem_price")
    private float forceRedeemPrice;

    /**
     * 赎回日期（"2020-04-13"，名称后有感叹号）
     */
    @JsonField("redeem_dt")
    private Date redeemDt;

    /**
     * 回购触发价
     */
    @JsonField("put_convert_price")
    private float putConvertPrice;

    /**
     * 初始规模（亿元）
     */
    @JsonField("orig_iss_amt")
    private float origIssAmt;

    /**
     * 剩余规模（亿元）
     */
    @JsonField("curr_iss_amt")
    private float currIssAmt;

    /**
     * 成交额（万元）
     */
    @JsonField("volume")
    private float volume;

    /**
     * 转股时间（"2019-06-26"）
     */
    @JsonField("convert_dt")
    private Date convertDt;

    /**
     * 类型
     */
    private int cbType;

    @Override
    public int compareTo(ConvertibleBondDetailBean o) {
        float thisYtmRtTax = Float.valueOf(this.getYtmRtTax().replace("%", ""));
        float oYtmRtTax = Float.valueOf(o.getYtmRtTax().replace("%", ""));
        return Float.compare(oYtmRtTax, thisYtmRtTax);
    }
}
