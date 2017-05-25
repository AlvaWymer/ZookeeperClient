package cn.mldn.travel.dao;

import java.util.Set;

import cn.mldn.travel.vo.Dept;
import cn.mldn.travel.vo.Emp;
import cn.mldn.util.dao.IBaseDAO;

public interface IEmpDAO extends IBaseDAO<String, Emp> {

	/**
	 * 1.1根据用户的id查询员工的详细信息
	 * @param eid 员工的id
	 * @return 员工的详细信息
	 */
	public Emp findById(String eid) ;
	
	/**
	 * 	1.3根据传入的emp对象，来更新员工信息
	 * @param emp vo对象
	 * @return 如果更新成功，返回true ，如果更新失败，返回false
	 */
	public boolean doUpdateLevel(Emp emp);
	
	

}
