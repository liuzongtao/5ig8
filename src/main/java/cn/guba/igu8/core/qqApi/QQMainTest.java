/**
 * 
 */
package cn.guba.igu8.core.qqApi;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import org.nutz.json.Json;
import org.nutz.lang.Files;

import cn.guba.igu8.core.utils.ExcelUtil;
import cn.guba.igu8.processor.igupiaoWeb.beans.User4AdInfo;

/**
 * @author zongtao liu
 *
 */
public class QQMainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, 1);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String name = String.valueOf(year * 10000 + month * 100 + day);
		System.out.println(cal.getTime());
		System.out.println(name);

		File file = Files.findFile("user4Ad.xlsx");
		List<User4AdInfo> list = ExcelUtil.getExcelObjList(file, name, User4AdInfo.class);
		System.out.println("size == " + list.size());
		System.out.println(Json.toJson(list.get(10)));

		List<String> sheetNames = ExcelUtil.getSheetNames(file);
		for (String sheetName : sheetNames) {
			List<User4AdInfo> tmpList = ExcelUtil.getExcelObjList(file, sheetName, User4AdInfo.class);
			System.out.println(sheetName + " " + tmpList.size());
		}

	}

}
