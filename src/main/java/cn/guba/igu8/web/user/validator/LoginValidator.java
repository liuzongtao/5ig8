/**
 * 
 */
package cn.guba.igu8.web.user.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

import cn.guba.igu8.db.mysqlModel.User;
import cn.guba.igu8.web.user.service.UserService;

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
		//验证用户名密码是否正确
		if(!invalid){
			String nickName = c.getPara("name");
			String pwd = c.getPara("pwd");
			User user = UserService.getInstance().getUser(nickName, pwd);
			if(user == null){
				addError("errorMsg", "用户名或密码错误");
			}
		}
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
