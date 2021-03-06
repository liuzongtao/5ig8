/**
 * 
 */
package cn.guba.igu8.db.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.jfinal.plugin.activerecord.Db;

import cn.guba.igu8.db.mysqlModel.Teacher;
import cn.guba.igu8.processor.igupiaoWeb.msg.IgpMsgFactory;
import cn.guba.igu8.web.teacher.beans.EIgpTeacher;
import cn.guba.igu8.web.vip.beans.EVipType;

/**
 * @author zongtao liu
 *
 */
public class TeacherDao {

	private static Log log = Logs.get();

	private static Map<Long, Teacher> teacherMap = new ConcurrentHashMap<Long, Teacher>();

	public static void initInsert() {
		List<Teacher> list = Teacher.dao.find("select * from teacher where vipTypeId=" + EVipType.igupiao.getValue());
		List<Teacher> teacherList = new ArrayList<Teacher>();
		for (EIgpTeacher igpTeacher : EIgpTeacher.values()) {
			boolean has = has(list, igpTeacher.getValue());
			if (!has) {
				Teacher teacher = new Teacher();
				teacher.setPfId(igpTeacher.getValue());
				teacher.setName(igpTeacher.getName());
				teacher.setVipTypeId(EVipType.igupiao.getValue());
				// teacher.setBuyEndTime(Long.MAX_VALUE);
				teacher.setBuyEndTime(igpTeacher.getBuyEndTime());
				teacher.setPfVipUid(igpTeacher.getValue());
				teacherList.add(teacher);
			}
		}
		// 批量插入
		if (teacherList.size() > 0) {
			batchInsert(teacherList);
		}
		// 初始化每个老师的vip会员uid
		list = Teacher.dao.find("select * from teacher where vipTypeId=" + EVipType.igupiao.getValue());
		if (list != null && list.size() > 0) {
			long now = System.currentTimeMillis();
			for (Teacher teacher : list) {
				Integer pfId = teacher.getPfId();
				if (teacher.getPfVipUid() == 0 && teacher.getBuyEndTime() > now) {
					boolean isVipUser = IgpMsgFactory.getInstance().isVipUser(pfId, teacher.getPfVipUid());
					if (!isVipUser) {
						int pfVipUid = IgpMsgFactory.getInstance().getUidFromAll(pfId);
						if (teacher.getPfVipUid() != pfVipUid) {
							teacher.setPfVipUid(pfVipUid);
							teacher.update();
						}
					}
				}
				log.infof(" init teacher , teacherPfId == %d, pfVipUid == %d", pfId, teacher.getPfVipUid());
			}
		}

	}

	/***
	 * 判断是否存在
	 * 
	 * @param list
	 * @param pfId
	 * @return
	 */
	private static boolean has(List<Teacher> list, int pfId) {
		boolean result = false;
		if (list == null || list.size() == 0) {
			return result;
		}
		for (Teacher teacher : list) {
			if (teacher.getPfId() == pfId) {
				result = true;
				break;
			}
		}
		return result;
	}

	private static void batchInsert(List<Teacher> teacherList) {
		Db.batchSave(teacherList, teacherList.size());
	}

	/***
	 * 获取 爱股票 app的老师列表
	 * 
	 * @return
	 */
	public static List<Teacher> getIgpTeachers() {
		return getTeachers(EVipType.igupiao.getValue());
	}

	/**
	 * 根据平台类型获取老师
	 * 
	 * @param typeId
	 * @return
	 */
	public static List<Teacher> getTeachers(int typeId) {
		Map<Long, Teacher> teachers = getTeacherMap();
		List<Teacher> teacherList = new ArrayList<Teacher>();
		for (Teacher teacher : teachers.values()) {
			if (typeId == 0 || teacher.getVipTypeId() == typeId) {
				teacherList.add(teacher);
			}
		}
		return teacherList;
	}

	/**
	 * 获取igp老师
	 * 
	 * @param pfId
	 * @return
	 */
	public static Teacher getIgpTeacher(int pfId) {
		Teacher igpTeacher = null;
		Map<Long, Teacher> teachers = getTeacherMap();
		for (Teacher teacher : teachers.values()) {
			if (teacher.getVipTypeId() == EVipType.igupiao.getValue() && teacher.getPfId() == pfId) {
				igpTeacher = teacher;
				break;
			}
		}
		return igpTeacher;
	}

	/***
	 * 获取所有老师信息
	 * 
	 * @return
	 */
	public static List<Teacher> getAllTeacherList() {
		Map<Long, Teacher> teachers = getTeacherMap();
		List<Teacher> teacherList = new ArrayList<Teacher>();
		teacherList.addAll(teachers.values());
		return teacherList;
	}

	private static Map<Long, Teacher> getTeacherMap() {
		if (teacherMap.size() == 0) {
			synchronized (TeacherDao.class) {
				if (teacherMap.size() == 0) {
					List<Teacher> teacherList = Teacher.dao.find("select * from teacher");
					if (teacherList != null) {
						for (Teacher teacher : teacherList) {
							teacherMap.put(teacher.getId(), teacher);
						}
					}
				}
			}
		}
		return teacherMap;
	}

	/***
	 * 获取老师信息
	 * 
	 * @param teacherId
	 * @return
	 */
	public static Teacher getTeacher(long teacherId) {
		Teacher teacher = null;
		Map<Long, Teacher> teachers = getTeacherMap();
		if (teachers.containsKey(teacherId)) {
			teacher = teachers.get(teacherId);
		}
		return teacher;
	}

	public static void updateTeacher(Teacher teacher) {
		teacherMap.put(teacher.getId(), teacher);
		teacher.update();
	}

}
