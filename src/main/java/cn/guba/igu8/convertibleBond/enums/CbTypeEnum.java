package cn.guba.igu8.convertibleBond.enums;

public enum CbTypeEnum {

    DEFENSIVE(1, "防御型"),
    BALANCED(2, "平衡型"),
    REDICAL(3, "激进型"),
    REDICALANDDEFENSIVE(4, "防御型|激进型"),
    REDICALANDBALANCED(5, "平衡型|激进型"),

    ;




    private Integer code;
    private String desc;

    private CbTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static CbTypeEnum resolve(int code) {
        for (CbTypeEnum businessTypeEnum : CbTypeEnum.values()) {
            if (businessTypeEnum.code.equals(code)) {
                return businessTypeEnum;
            }
        }
        return null;
    }
}
