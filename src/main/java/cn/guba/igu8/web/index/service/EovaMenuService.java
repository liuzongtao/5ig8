/**
 * 
 */
package cn.guba.igu8.web.index.service;

import java.util.ArrayList;
import java.util.List;

import cn.guba.igu8.db.dao.EovaMenuDao;
import cn.guba.igu8.db.mysqlModel.EovaMenu;
import cn.guba.igu8.web.index.beans.TreeAttrBean;
import cn.guba.igu8.web.index.beans.TreeInfoBean;

/**
 * @author zongtao liu
 *
 */
public class EovaMenuService {

	private static volatile EovaMenuService eovaMenuService;

	private EovaMenuService() {
	}

	public static EovaMenuService getInstance() {
		if (eovaMenuService == null) {
			synchronized (EovaMenuService.class) {
				if (eovaMenuService == null) {
					eovaMenuService = new EovaMenuService();
				}
			}
		}
		return eovaMenuService;
	}

	public List<EovaMenu> getAllEovaMenu() {
		return EovaMenuDao.getAllEovaMenu();
	}

	public List<EovaMenu> getEovaMenuRoot() {
		return EovaMenuDao.getEovaMenuRoot();
	}

	public List<TreeInfoBean> getTreeInfoList(int parentId) {
		List<TreeInfoBean> list = new ArrayList<TreeInfoBean>();
		List<EovaMenu> eovaMenus = EovaMenuDao.getEovaMenusByParentId(parentId);
		if(eovaMenus != null){
			for(EovaMenu eovaMenu : eovaMenus){
				TreeInfoBean treeInfoBean = genTreeInfoBean(eovaMenu);
				list.add(treeInfoBean);
			}
		}
		return list;
	}
	
	private TreeInfoBean genTreeInfoBean(EovaMenu eovaMenu){
		TreeInfoBean treeInfoBean = new TreeInfoBean();
		if(!eovaMenu.getType().equals("dir")){
			TreeAttrBean attr = new TreeAttrBean();
			if(eovaMenu.getType().equals("diy")){
				attr.setUrl(eovaMenu.getUrl());
			}else{
				attr.setUrl("/" + eovaMenu.getType() + "/list/" + eovaMenu.getCode());
			}
			treeInfoBean.setAttributes(attr);
		}
		treeInfoBean.setChildren(getTreeInfoList(eovaMenu.getId()));
		treeInfoBean.setCode(eovaMenu.getCode());
		treeInfoBean.setFilter(eovaMenu.getFilter());
		treeInfoBean.setIconCls(eovaMenu.getIcon());
		treeInfoBean.setId(eovaMenu.getId());
		treeInfoBean.setIs_del(eovaMenu.getIsDel());
		treeInfoBean.setOrder_num(eovaMenu.getOrderNum());
		treeInfoBean.setParent_id(eovaMenu.getParentId());
		treeInfoBean.setState("open");
		treeInfoBean.setText(eovaMenu.getName());
		treeInfoBean.setType(eovaMenu.getType());
		return treeInfoBean;
	}

}
