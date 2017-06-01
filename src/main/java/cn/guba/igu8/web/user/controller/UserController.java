/**
 * 
 */
package cn.guba.igu8.web.user.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;

import cn.guba.igu8.web.user.validator.LoginValidator;

/**
 * @author zongtao liu
 *
 */
public class UserController extends Controller {
	
	@Clear
	@Before(LoginValidator.class)
	public void login(){
		
	}

}
