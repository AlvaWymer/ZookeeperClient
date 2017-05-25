package cn.mldn.travel.action.back;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mldn.travel.service.back.IDeptServiceBack;
import cn.mldn.travel.service.back.IEmpServiceBack;
import cn.mldn.travel.vo.Dept;
import cn.mldn.travel.vo.Emp;
import cn.mldn.util.action.abs.AbstractBaseAction;

@Controller
@RequestMapping("/pages/back/admin/dept/*")
public class DeptActionBack extends AbstractBaseAction {
	@Resource
	private IEmpServiceBack iEmpServiceBack;
	@Resource
	private IDeptServiceBack ideptServiceBack;
	//---------------------------------------------------------------------------------------------------
	@RequestMapping("list")
	@RequiresUser
	@RequiresRoles(value = { "emp", "empshow" }, logical = Logical.OR)
	@RequiresPermissions(value = { "dept:list", "deptshow:list" }, logical = Logical.OR)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView(super.getUrl("dept.list.page"));
		// 方案二： 直接在此处通过map集合 进行一系列的处理，然后返回给前台页面，这样性能会好一点儿，所以选择第二种方案：

		Map<String, Object> map = ideptServiceBack.listAllDnameAndEname();
		
		mav.addObject("allDeptName", map.get("allDeptName")); // 保存所有的部门信息
		
//		将所有的雇员信息 塞到map集合里面  List 转为Map
		List<Emp> allEmps = (List<Emp>) map.get("allEmpManagerName"); // 取得所有的雇员信息
		Map<String, String> empMap = new HashMap<String, String>();
		Iterator<Emp> iter = allEmps.iterator();
		while (iter.hasNext()) {
			Emp emp = iter.next();
			empMap.put(emp.getEid(), emp.getEname());
		}
		mav.addObject("allEmpManagerName", empMap);

		// 方案一：直接通过modeland View 返回 给前台页面调用，但是会涉及到数据库的1+N 次查询的问题
		// mav.addAllObjects(ideptServiceBack.listAllDnameAndEname());

		return mav;
	}

	//---------------------------------------------------------------------------------------------------
	@RequestMapping("edit")
	@RequiresUser
	@RequiresRoles("emp")
	@RequiresPermissions("dept:edit")
	// 注意：前台js传入的iid 的值
	public ModelAndView edit(HttpServletResponse response, Dept dept) {
//		if (ideptServiceBack.updateDeptName(dept)) {
//			super.print(response, "true");
//		}
		super.print(response, ideptServiceBack.updateDeptName(dept));
		return null;
	}
	
	//---------------------------------------------------------------------------------------------------
	/**
	 * 1.4部门经理 降级的操作
	 * @param response HttpServletResponse
	 * @param did 前台页面传入的 要修改的部门的ID
	 * @return 不返回
	 */
	@RequestMapping("editMgr")
	@RequiresUser
	@RequiresRoles(value = { "emp", "empshow" }, logical = Logical.OR)
	@RequiresPermissions(value = { "dept:edit", "emp:edit" }, logical = Logical.AND)
	public ModelAndView editMgr(HttpServletResponse response, Long did){
		super.print(response, this.ideptServiceBack.updateDeptNameAndManager(did, super.getEid()));
		return null;
	}
	
	
}
