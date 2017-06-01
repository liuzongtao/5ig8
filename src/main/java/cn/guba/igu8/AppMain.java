/**
 * 
 */
package cn.guba.igu8;

import com.jfinal.core.JFinal;

/**
 * @author zongtao liu
 *
 */
public class AppMain {

	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 80, "/", 5);
//		System.out.println(System.currentTimeMillis() + 1000l* 60*60*24*365*100);//4649279698951
//		System.out.println("http://47.92.157.16/cc/158262".length());
	}

}
