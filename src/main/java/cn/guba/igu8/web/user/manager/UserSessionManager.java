/**
 * 
 */
package cn.guba.igu8.web.user.manager;

import java.util.HashMap;
import java.util.Map;


/**
 * @author zongtao liu
 *
 */
public class UserSessionManager {

	private static volatile UserSessionManager userSessionManager;

	private UserSessionManager() {
	}

	public static UserSessionManager getInstance() {
		if (userSessionManager == null) {
			synchronized (UserSessionManager.class) {
				if (userSessionManager == null) {
					userSessionManager = new UserSessionManager();
				}
			}
		}
		return userSessionManager;
	}
	
	private static Map<String,Long> map = new HashMap<String,Long>();
	
	private static Map<Long,Map<String,Long>> detailMap = new HashMap<Long,Map<String,Long>>();
	private static int MAX = 4;
	
	public long getUid(String requestIp){
		long uid = 0;
		if(map.containsKey(requestIp)){
			uid = map.get(requestIp);
		}
		return uid;
	}
	
	/**
	 * 添加
	 * @param uid
	 * @param requestIp
	 */
	public void add(long uid,String requestIp){
		Map<String,Long> detailInfo = null;
		if(detailMap.containsKey(uid)){
			detailInfo = detailMap.get(uid);
		}else{
			detailInfo = new HashMap<String,Long>();
			detailMap.put(uid, detailInfo);
		}
		detailInfo.put(requestIp, System.currentTimeMillis());
		//加入ip和uid的对应
		map.put(requestIp, uid);
		
		//如果超过数量上限，则删除一个
		if(detailInfo.size() > MAX){
			String minTimeIp = null;
			long minTime = Long.MAX_VALUE;
			for(String ip : detailInfo.keySet()){
				long time = detailInfo.get(ip);
				if(time < minTime){
					minTime = time;
					minTimeIp = ip;
				}
			}
			//删除最小时间ip
			map.remove(minTimeIp);
			detailInfo.remove(minTimeIp);
		}
	}
	
	/***
	 * 删除
	 * @param uid
	 * @param requestIp
	 */
	public void remove(long uid,String requestIp){
		if(detailMap.containsKey(uid)){
			Map<String, Long> detailInfo = detailMap.get(uid);
			detailInfo.remove(requestIp);
		}
		map.remove(requestIp);
	}
	

}
