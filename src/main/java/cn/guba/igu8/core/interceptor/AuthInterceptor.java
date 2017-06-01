/**
 * 
 */
package cn.guba.igu8.core.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * @author zongtao liu
 *
 */
public class AuthInterceptor implements Interceptor {

	/* (non-Javadoc)
	 * @see com.jfinal.aop.Interceptor#intercept(com.jfinal.aop.Invocation)
	 */
	@Override
	public void intercept(Invocation inv) {
		inv.getController().getSession();
		inv.invoke();
	}

}
