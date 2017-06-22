/**
 * 
 */
package cn.guba.igu8.web.teacher.beans;

/**
 * @author zongtao liu
 *
 */
public class TeacherViewInfoBean {
	
	public long teacherId;
	
	public String teacherName;

	/**
	 * @return the teacherId
	 */
	public long getTeacherId() {
		return teacherId;
	}

	/**
	 * @param teacherId the teacherId to set
	 */
	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
	}

	/**
	 * @return the teacherName
	 */
	public String getTeacherName() {
		return teacherName;
	}

	/**
	 * @param teacherName the teacherName to set
	 */
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

}
