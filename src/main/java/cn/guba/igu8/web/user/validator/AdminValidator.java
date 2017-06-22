/**
 * 
 */
package cn.guba.igu8.web.user.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

import cn.guba.igu8.core.constants.ControllerConstant;
import cn.guba.igu8.db.mysqlModel.User;
import cn.guba.igu8.web.user.beans.ERoleType;

/**
 * @author zongtao liu
 *
 */
public class AdminValidator extends Validator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jfinal.validate.Validator#validate(com.jfinal.core.Controller)
	 */
	@Override
	protected void validate(Controller c) {
		User user = c.getSessionAttr(ControllerConstant.SESSION_NAME_USER);
		if (user != null) {
			int roleType = user.getRoleType();
			if (roleType != ERoleType.ADMIN.getValue()) {
				addError("error", "没有访问权限");
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
		c.renderError(404);
	}

}
