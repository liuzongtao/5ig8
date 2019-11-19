package cn.guba.igu8.minsu.tj.bean;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class ClientBean {

    private Object abTest;
    private Map<String, ABTestValueBean> abTests;
    private String appId;
    private String appVersion;
    private String appVersionUpdate;
    private String buildTag;
    private String channelCode;
    private String devModel;
    private String devToken;
    private int devType;
    private String locale;
    private String oaId;
    private String osVersion;
    private String screenInfo;
    private String sessionId;
    private String tId;
    private String uID;
}
