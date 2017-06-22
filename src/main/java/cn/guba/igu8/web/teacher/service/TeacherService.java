/**
 * 
 */
package cn.guba.igu8.web.teacher.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.guba.igu8.db.dao.EovaMenuDao;
import cn.guba.igu8.db.dao.TeacherDao;
import cn.guba.igu8.db.mysqlModel.EovaMenu;
import cn.guba.igu8.db.mysqlModel.Teacher;
import cn.guba.igu8.db.mysqlModel.Uservipinfo;
import cn.guba.igu8.processor.igupiaoWeb.msg.IgpMsgFactory;
import cn.guba.igu8.web.teacher.beans.TeacherViewInfoBean;
import cn.guba.igu8.web.vip.beans.EVipType;
import cn.guba.igu8.web.vip.service.VipService;

/**
 * @author zongtao liu
 *
 */
public class TeacherService {

	private static volatile TeacherService teacherService;

	private TeacherService() {
	}

	public static TeacherService getInstance() {
		if (teacherService == null) {
			synchronized (TeacherService.class) {
				if (teacherService == null) {
					teacherService = new TeacherService();
				}
			}
		}
		return teacherService;
	}

	public void initTeacher() {
		//初始化老师
		TeacherDao.initInsert();
		//初始化老师相关
		List<Teacher> allTeacherList = TeacherDao.getAllTeacherList();
		Collections.sort(allTeacherList, new Comparator<Teacher>() {
			@Override
			public int compare(Teacher o1, Teacher o2) {
				return Long.compare(o1.getId(), o2.getId());
			}
		});
		long now = System.currentTimeMillis();
		int index = 1;
		for (Teacher tmpTeacher : allTeacherList) {
			if (tmpTeacher.getBuyEndTime() > now) {
				long id = tmpTeacher.getId();
				int menuId = Integer.valueOf(String.valueOf(1000 + id));
				EovaMenu eovaMenu = EovaMenuDao.getEovaMenu(menuId);
				if (eovaMenu == null) {
					eovaMenu = new EovaMenu();
					eovaMenu.setId(menuId);
					eovaMenu.setCode("biz_content");
					int vipType = tmpTeacher.getVipTypeId();
					String typeDescr = EVipType.getEVipType(vipType).getDescr();
					eovaMenu.setName(typeDescr + "：" + tmpTeacher.getName());
					eovaMenu.setType("diy");
					eovaMenu.setIcon("icon-bookopen");
					eovaMenu.setOrderNum(index);
					eovaMenu.setParentId(3);
					eovaMenu.setUrl("/cc/list?tid=" + id);
					EovaMenuDao.addEovaMenu(eovaMenu);
				}
				index++;
			}
		}
	}

	/***
	 * 获取老师名字
	 * 
	 * @param teacherId
	 * @return
	 */
	public String getTeacherName(long teacherId) {
		String name = "";
		Teacher teacher = TeacherDao.getTeacher(teacherId);
		if (teacher != null) {
			name = teacher.getName();
		}
		return name;
	}

	/**
	 * 初始化teacher
	 * 
	 * @param teacherId
	 */
	public void initTeacher4VipUser(long teacherId) {
		Teacher teacher = TeacherDao.getTeacher(teacherId);
		Integer pfId = teacher.getPfId();
		int pfVipUid = IgpMsgFactory.getInstance().getUidFromAll(pfId);
		if (teacher.getPfVipUid() != pfVipUid) {
			teacher.setPfVipUid(pfVipUid);
			TeacherDao.updateTeacher(teacher);
		}
	}

	public List<Teacher> getTeachers(int typeId) {
		return TeacherDao.getTeachers(typeId);
	}

	/**
	 * 获取显示的老师信息
	 * 
	 * @param typeId
	 * @return
	 */
	public List<TeacherViewInfoBean> getTeacherList(int typeId) {
		List<TeacherViewInfoBean> list = new ArrayList<TeacherViewInfoBean>();
		List<Teacher> teachers = getTeachers(typeId);
		long now = System.currentTimeMillis();
		for (Teacher teacher : teachers) {
			if (teacher.getBuyEndTime() > now) {
				TeacherViewInfoBean bean = new TeacherViewInfoBean();
				bean.setTeacherId(teacher.getId());
				bean.setTeacherName(teacher.getName());
				list.add(bean);
			}
		}
		return list;
	}

	/**
	 * 获取可以添加的老师信息
	 * 
	 * @param uid
	 * @param typeId
	 * @return
	 */
	public List<TeacherViewInfoBean> getCanAddTeacher(long uid) {
		return getCanAddTeacher(uid, 0);
	}

	/**
	 * 获取可更新的老师信息
	 * 
	 * @param uid
	 * @param curTeacherId
	 * @return
	 */
	public List<TeacherViewInfoBean> getCanUpdateTeacher(long uid, long curTeacherId) {
		List<TeacherViewInfoBean> canAddTeachers = getCanAddTeacher(uid);
		TeacherViewInfoBean curTeacherViewInfoBean = new TeacherViewInfoBean();
		curTeacherViewInfoBean.setTeacherId(curTeacherId);
		curTeacherViewInfoBean.setTeacherName(TeacherService.getInstance().getTeacherName(curTeacherId));
		List<TeacherViewInfoBean> newCanAddTeachers = new ArrayList<TeacherViewInfoBean>();
		newCanAddTeachers.add(curTeacherViewInfoBean);
		newCanAddTeachers.addAll(canAddTeachers);
		return newCanAddTeachers;
	}

	public List<TeacherViewInfoBean> getCanAddTeacher(long uid, int typeId) {
		List<TeacherViewInfoBean> list = getTeacherList(typeId);
		List<Uservipinfo> vipList = VipService.getInstance().getVipList(uid);
		Set<Long> teacherIdSet = new HashSet<Long>();
		for (Uservipinfo vip : vipList) {
			teacherIdSet.add(vip.getConcernedTeacherId());
		}
		List<TeacherViewInfoBean> removeList = getTeacherList(typeId);
		for (TeacherViewInfoBean tmp : list) {
			if (teacherIdSet.contains(tmp.getTeacherId())) {
				removeList.add(tmp);
			}
		}
		list.removeAll(removeList);
		return list;
	}

}
