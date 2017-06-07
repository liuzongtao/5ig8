/**
 * 
 */
package cn.guba.igu8.web.index.beans;

import java.util.List;

/**
 * @author zongtao liu
 *
 */
public class TreeInfoBean {

	private TreeAttrBean attributes;

	private List<TreeInfoBean> children;

	private String code;

	private String filter;

	private String iconCls;

	private int id;

	private boolean is_del;

	private int order_num;

	private int parent_id;

	private String state;

	private String text;

	private String type;

	/**
	 * @return the attributes
	 */
	public TreeAttrBean getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(TreeAttrBean attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the children
	 */
	public List<TreeInfoBean> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(List<TreeInfoBean> children) {
		this.children = children;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @return the iconCls
	 */
	public String getIconCls() {
		return iconCls;
	}

	/**
	 * @param iconCls
	 *            the iconCls to set
	 */
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the is_del
	 */
	public boolean isIs_del() {
		return is_del;
	}

	/**
	 * @param is_del
	 *            the is_del to set
	 */
	public void setIs_del(boolean is_del) {
		this.is_del = is_del;
	}

	/**
	 * @return the order_num
	 */
	public int getOrder_num() {
		return order_num;
	}

	/**
	 * @param order_num
	 *            the order_num to set
	 */
	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}

	/**
	 * @return the parent_id
	 */
	public int getParent_id() {
		return parent_id;
	}

	/**
	 * @param parent_id
	 *            the parent_id to set
	 */
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
