package cn.mldn.travel.service.back.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.mldn.travel.dao.IDeptDAO;
import cn.mldn.travel.dao.IEmpDAO;
import cn.mldn.travel.service.back.IDeptServiceBack;
import cn.mldn.travel.service.back.abs.AbstractService;
import cn.mldn.travel.vo.Dept;
import cn.mldn.travel.vo.Emp;

@Service
public class DeptServiceBackImpl extends AbstractService implements IDeptServiceBack {
	@Resource
	private IDeptDAO deptDAO;
	@Resource
	private IEmpDAO empDAO;

	@Override
	public Map<String, Object> listAllDnameAndEname() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("allEmpManagerName", this.deptDAO.findAllManagerNameForDeptName());

		map.put("allDeptName", this.deptDAO.findAllDeptName());

		return map;
	}

	/**
	 * 1.1 实现更新部门名称的操作
	 */
	@Override
	public boolean updateDeptName(Dept dept) {
		System.out.println(deptDAO.doUpdate(dept));
		return false;
	}

	/**
	 * 1.3 实现 部门和 雇员中的 经理的降级操作 详细业务分析：<br>
	 * 1.一共需要两张表：dept和 emp表<br>
	 * 2.如果要是实现经理的降级操作，首先应当直接更新dept表中，直接将的普通表中的部门经理的字段设置为空<br>
	 * 3.如果步骤二更新成功，那么则继续获取当前的操作用户（修改者）的id，然后进行权限的获取和比较，如果当前的用户有权限
	 * 那么则可以将当前的修改的id，还有要修改的经理的职位信息（员工为staff）还有部门id（did）放入对象，然后mapping
	 * 然后数据库根据did来实现经理的降级操作<br>
	 * 4.如果实现了dept表和emp表的最终更新操作，那么，返回true，如果没有更新成功，那么返回false；
	 */
	@Override
	public boolean updateDeptNameAndManager(Long did, String ineid) {

		// 1. 首先查询当前要修改的部门是否存在
		Dept dept = deptDAO.findByIdForDid(did);
		// 2.修改前取得管理者的信息
		Emp emp = new Emp();
		emp.setEid(dept.getEid()); 	// 取得原始的部门的领导编号

		if (dept != null) {
			// 3.然后判断当前的登陆用户（修改者）具有的权限
			Emp mgr = empDAO.findById(ineid);
			// 4.如果是manager 那么就可以修改
			if ("manager".equals(mgr.getLid())) {
				dept.setEid(null);// 设置dept 中的经理为空
				// 如果部门表修改员工成功
				if (deptDAO.doUpdateManager(dept)) {
					// 更新经理的信息，降级为普通员工
					emp.setIneid(ineid);// 修改者的名字
					emp.setLid("staff");// 普通员工字段
					return empDAO.doUpdateLevel(emp);
					

				}
			}

		}
		return false;
	}

}
