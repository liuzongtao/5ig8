/**
 * 
 */
package cn.guba.igu8.web.vip.service;

import cn.guba.igu8.processor.igupiaoWeb.msg.beans.EIgpKind;

/**
 * @author zongtao liu
 *
 */
public class VipService {

	private static volatile VipService vipService;

	private VipService() {
	}

	public static VipService getInstance() {
		if (vipService == null) {
			synchronized (VipService.class) {
				if (vipService == null) {
					vipService = new VipService();
				}
			}
		}
		return vipService;
	}

	/**
	 * 获取发送邮件信息
	 * 
	 * @return
	 */
	public String getSendVipEmailStr() {
		return EIgpKind.VIP.getValue();
	}

	public String getSendAllEmailStr() {
		StringBuilder sb = new StringBuilder();
		for (EIgpKind kind : EIgpKind.values()) {
			sb.append(kind.getValue()).append(",");
		}
		return sb.substring(0, sb.length() - 1);
	}

}
