/**
 * 
 */
package cn.guba.igu8.web.user.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * @author zongtao liu
 *
 */
public class LoginValidator extends Validator{

	/* (non-Javadoc)
	 * @see com.jfinal.validate.Validator#validate(com.jfinal.core.Controller)
	 */
	@Override
	protected void validate(Controller c) {
		validateRequired("name", "nameMsg", "请输入用户名");
		validateRequired("pwd", "pwdMsg", "请输入密码");
	}

	/* (non-Javadoc)
	 * @see com.jfinal.validate.Validator#handleError(com.jfinal.core.Controller)
	 */
	@Override
	protected void handleError(Controller c) {
		c.keepPara("name");
		c.render("login.html");
	}

}
