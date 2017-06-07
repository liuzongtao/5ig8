/**
 * 
 */
package cn.guba.igu8.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private static Map<Long, Teacher> teacherMap = new HashMap<Long, Teacher>();

	public static void initInsert() {
		List<Teacher> list = Teacher.dao.find("select * from teacher where vipTypeId=" + EVipType.igupiao.getValue());
		if (list == null || list.size() == 0) {
			List<Teacher> teacherList = new ArrayList<Teacher>();
			for (EIgpTeacher igpTeacher : EIgpTeacher.values()) {
				Teacher teacher = new Teacher();
				teacher.setPfId(igpTeacher.getValue());
				teacher.setName(igpTeacher.getName());
				teacher.setVipTypeId(Long.valueOf(EVipType.igupiao.getValue()));
				// teacher.setBuyEndTime(Long.MAX_VALUE);
				teacher.setBuyEndTime(igpTeacher.getBuyEndTime());
				teacherList.add(teacher);
			}
			// 批量插入
			batchInsert(teacherList);
		}
		// 初始化每个老师的vip会员uid
		list = Teacher.dao.find("select * from teacher where vipTypeId=" + EVipType.igupiao.getValue());
		if (list != null && list.size() > 0) {
			long now = System.currentTimeMillis();
			for (Teacher teacher : list) {
				Integer pfId = teacher.getPfId();
				if (teacher.getPfVipUid() == 0 && teacher.getBuyEndTime() > now) {
					int pfVipUid = IgpMsgFactory.getInstance().getUidFromAll(pfId);
					if (teacher.getPfVipUid() != pfVipUid) {
						teacher.setPfVipUid(pfVipUid);
						teacher.update();
					}
				}
				log.infof(" init teacher , teacherPfId == %d, pfVipUid == %d", pfId, teacher.getPfVipUid());
			}
		}

	}

	private static void batchInsert(List<Teacher> teacherList) {
		Db.batchSave(teacherList, teacherList.size());
	}

	/***
	 * 获取 爱股票 app的老师列表
	 * 
	 * @return
	 */
	public static List<Teacher> getIgpTeacherList() {
		Map<Long, Teacher> teachers = getTeacherMap();
		List<Teacher> teacherList = new ArrayList<Teacher>();
		for (Teacher teacher : teachers.values()) {
			if (teacher.getVipTypeId() == EVipType.igupiao.getValue()) {
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
			List<Teacher> teacherList = Teacher.dao.find("select * from teacher");
			if (teacherList != null) {
				for (Teacher teacher : teacherList) {
					teacherMap.put(teacher.getId(), teacher);
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

	public static void saveTeacher(Teacher teacher) {
		teacherMap.put(teacher.getId(), teacher);
		teacher.update();
	}

}
