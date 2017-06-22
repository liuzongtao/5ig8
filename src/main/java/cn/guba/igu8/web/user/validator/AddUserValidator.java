/**
 * 
 */
package cn.guba.igu8.web.user.validator;

import org.nutz.lang.Strings;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

import cn.guba.igu8.core.utils.MatcherUtil;
import cn.guba.igu8.db.mysqlModel.User;
import cn.guba.igu8.web.user.beans.UserViewInfoBean;
import cn.guba.igu8.web.user.service.UserService;

/**
 * @author zongtao liu
 *
 */
public class AddUserValidator extends Validator {

	private static final String nicknamePatternStr = "^[A-Za-z0-9]+$";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jfinal.validate.Validator#validate(com.jfinal.core.Controller)
	 */
	@Override
	protected void validate(Controller c) {
		long uid = c.getParaToLong("userViewInfoBean.id");
		validateRequired("userViewInfoBean.nickname", "nicknameMsg", "请输入昵称");
		if (c.getAttr("nicknameMsg") == null) {
			if (!MatcherUtil.matcher(c.getPara("userViewInfoBean.nickname"), nicknamePatternStr)) {
				addError("nicknameMsg", "昵称仅为大小写字母");
			} else {
				validateString("userViewInfoBean.nickname", 6, 12, "nicknameMsg", "昵称长度为6-12位");
				if (c.getAttr("nicknameMsg") == null) {
					String nickname = c.getPara("userViewInfoBean.nickname");
					User user = UserService.getInstance().getUser(nickname);
					if (user != null && user.getId() != uid) {
						addError("nicknameMsg", "该昵称已经存在");
					}
				}
			}
		}
		validateRequired("userViewInfoBean.email", "emailMsg", "请输入电子邮箱");
		if (c.getAttr("emailMsg") == null) {
			validateEmail("userViewInfoBean.email", "emailMsg", "请输入正确的邮箱格式");
			if (c.getAttr("emailMsg") == null) {
				String email = c.getPara("userViewInfoBean.email");
				User user = UserService.getInstance().getUserByEmail(email);
				if (user != null && user.getId() != uid) {
					addError("emailMsg", "该邮箱已经存在");
				}
			}
		}
		String phoneNumberStr = c.getPara("userViewInfoBean.phoneNumber");
		if (Strings.isNotBlank(phoneNumberStr)) {
			if (!Strings.isMobile(phoneNumberStr)) {
				addError("phoneNumberMsg", "请输入正确的手机号");
			} else {
				long phoneNumber = Long.valueOf(phoneNumberStr);
				User user = UserService.getInstance().getUserByPhone(phoneNumber);
				if (user != null && user.getId() != uid) {
					addError("phoneNumberMsg", "该手机号已经存在");
				}
			}
		}
		String inviter = c.getPara("userViewInfoBean.inviter");
		if (Strings.isNotBlank(inviter)) {
			if (Strings.isEmail(inviter)) {
				validateEmail("userViewInfoBean.inviter", "inviterMsg", "请输入正确的邮箱格式");
				if (c.getAttr("inviterMsg") == null) {
					String email = c.getPara("userViewInfoBean.inviter");
					User user = UserService.getInstance().getUserByEmail(email);
					if (user == null) {
						addError("inviterMsg", "没有该邮箱账户");
					}
				}
			} else if (Strings.isNumber(inviter)) {
				if (!Strings.isMobile(inviter)) {
					addError("inviterMsg", "请输入正确的手机号");
				} else {
					User user = UserService.getInstance().getUserByPhone(Long.valueOf(inviter));
					if (user == null) {
						addError("inviterMsg", "没有该手机号账户");
					}
				}
			} else if (MatcherUtil.matcher(inviter, nicknamePatternStr)) {// 昵称
				User user = UserService.getInstance().getUser(inviter);
				if (user == null) {
					addError("inviterMsg", "没有该昵称的账户");
				}
			} else {// 格式错误
				addError("inviterMsg", "请输入正确的邮箱、昵称或手机号码");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jfinal.validate.Validator#handleError(com.jfinal.core.Controller)
	 */
	@Override
	protected void handleError(Controller c) {
		UserViewInfoBean userViewInfoBean = c.getBean(UserViewInfoBean.class);
		c.setAttr("userViewInfoBean", userViewInfoBean);
		c.render("detail.html");
	}

}
