package cn.mldn.travel.service.back;

import java.util.Map;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;

import cn.mldn.travel.vo.Dept;

public interface IDeptServiceBack {

	/**1.1
	 * 列出所有的部门姓名，部门的管理人员的姓名
	 * 1.调用IDeptDAO.findAllDeptName() 查询出所有的部门的名称 <br>
	 * 2.调用IDeptDAO.findAllManagerNameForDeptName()
	 * 根据部门信息里面的部门负责人员的id查询出该部门的 负责人的姓名<br>
	 * @return 返回Map集合数据包含有如下内容：<br>
	 * 1. key = allEmpManagerName  返回的所有的管理者的名字<br>
	 * 2. key=  allDeptName 返回的部门的名字
	 * 
	 * 
	 */
// 	具有:雇员信息管理  或者 雇员信息浏览 这两个权限 的都可以
//	具有：部门列表  或者 部门列表  的权限  才能够实现
	@RequiresRoles(value = {"emp", "empshow"}, logical = Logical.OR)
	@RequiresPermissions(value = {"dept:list","deptshow:list"}, logical = Logical.OR)
	public Map<String, Object> listAllDnameAndEname();
	
	/**1.2
	 * 根据传入的部门id，实现，更新部门信息的操作
	 * @param dept vo对象
	 * @return 如果成功返回true，如果失败，返回false
	 */
	@RequiresRoles(value = {"emp", "empshow"}, logical = Logical.OR)
	@RequiresPermissions(value = {"dept:edit"}, logical = Logical.OR)
	public boolean updateDeptName(Dept dept);
	
	/**1.4
	 * 实现部门领导的降级操作，具体的操作如下：<br>
	 * 1.首先调用IDeptDAO.doUpdateManager()，然后实现Dept表中的 管理者的字段设置为空<br>
	 * 2.调用ImpDAO.doUpdateLevel(),然后实现Emp表中的 员工信息更新 降级操作
	 * @param did    传入的要修改的部门的ID
	 * @param ineid   当前操作者的ID
	 * @return  
	 */
	@RequiresRoles(value = {"emp"}, logical = Logical.OR)
	@RequiresPermissions(value = {"dept:edit","emp:edit"}, logical = Logical.AND)
	public boolean updateDeptNameAndManager(Long did,String ineid);
	
	
	
	
}
