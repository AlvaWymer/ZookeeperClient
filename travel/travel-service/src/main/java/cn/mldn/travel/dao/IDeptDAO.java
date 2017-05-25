package cn.mldn.travel.dao;

import java.util.List;
import java.util.Set;

import cn.mldn.travel.vo.Dept;
import cn.mldn.travel.vo.Emp;
import cn.mldn.util.dao.IBaseDAO;

public interface IDeptDAO extends IBaseDAO<Long, Dept> {
	
	/**
	 * 1.1查询所有的部门名称
	 * @return 返回全部的部门名称集合
	 */
	public Set<Dept> findAllDeptName() ;
	
	/**
	 * 1.1根据部门表中查到的eid 查询到 雇员表中 部门所对应的部门经理 
	 * @param dept 部门
	 * @return 返回的部门管理者的名字
	 */
	public List<Emp> findAllManagerNameForDeptName() ;
	
	//```````````````````````````````````````````````````````````
	/**
	 *  根据雇员的id，查询到雇员对应的部门
	 * @param eid 用户的id（String类型）
	 * @return
	 */
	public Dept findById(String eid);
	
	/**
	 *  1.4根据部门的id，查询到雇员对应的部门
	 * @param did 部门的id（long类型）
	 * @return
	 */
	public Dept findByIdForDid(Long did);
	
	//```````````````````````````````````````````````````````````
	/**
	 * 1.3更新部门表中的经理
	 * @param dept  dept 对象
	 * @return 如果更新部门的经理成功，返回true，否则返回false
	 */
	public boolean doUpdateManager(Dept dept);
}
