/**
 * 
 */
package cn.guba.igu8.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.nutz.http.Response;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.guba.igu8.core.utils.ExcelUtil;
import cn.guba.igu8.core.utils.HttpUtil;
import cn.guba.igu8.tools.igp.account.beans.IgpAccountBean;
import cn.guba.igu8.tools.igp.account.beans.IgpAccountRes;

/**
 * @author zongtao liu
 *
 */
public class IgpAccountTool {
	
	private static Log log = Logs.get();
	
	private static final String URL_IGP_RECOMMMEN_TALENTS = "https://www.5igupiao.com/api/index/recommend_talents?id=%d";
	
	public static void main(String[] args) {
		test();
	}
	
	public static List<IgpAccountBean> getAccounts(long id,Set<Long> idSet){
		List<IgpAccountBean> list = new ArrayList<IgpAccountBean>();
		String url = String.format(URL_IGP_RECOMMMEN_TALENTS, 180000);//180000- 189268 - 190000
		Response response = HttpUtil.get(url);
		if(response != null && response.isOK()){
			String content = response.getContent();
			if(Strings.isNotBlank(content) && (content.startsWith("{") || content.startsWith("["))){
				IgpAccountRes accountRes = Json.fromJson(IgpAccountRes.class, content);
				if(accountRes.isSucc()){
					List<IgpAccountBean> dataList = accountRes.getData();
					for(IgpAccountBean data : dataList){
						long curId = data.getId();
						if(!idSet.contains(curId)){
							list.add(data);
							idSet.add(curId);
						}
					}
				}
			}
		}
		return list;
	}
	
	public static void test(){
		String filepath = "E:\\igpAccount_20170829.xlsx";
		String sheetName = "account";
		Set<Long> idSet = new HashSet<Long>();
		List<IgpAccountBean> list = ExcelUtil.getExcelObjList(filepath, sheetName, IgpAccountBean.class);
		for(IgpAccountBean bean : list){
			idSet.add(bean.getId());
		}
		System.out.println("begin num == " + idSet.size());
		for(int i = 0 ; i < 100 ; i++){
			System.out.println("=============" + i + "=============");
			List<IgpAccountBean> curList = new ArrayList<IgpAccountBean>();
			for(int j = 0 ; j < 10 ;j++){
				List<IgpAccountBean> accounts = getAccounts(i,idSet);
				curList.addAll(accounts);
			}
			System.out.println("add num == " + curList.size());
			if(curList.size() > 0){
				ExcelUtil.writeExcel(filepath, sheetName, curList);
				list.addAll(curList);
			}
		}
		System.out.println("begin num == " + list.size());
	}

}
