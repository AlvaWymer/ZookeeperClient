package cn.mldn.travel.service.back.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.mldn.travel.dao.IActionDAO;
import cn.mldn.travel.dao.IDeptDAO;
import cn.mldn.travel.dao.IEmpDAO;
import cn.mldn.travel.dao.ILevelDAO;
import cn.mldn.travel.dao.IRoleDAO;
import cn.mldn.travel.exception.DeptManagerExistException;
import cn.mldn.travel.service.back.IEmpServiceBack;
import cn.mldn.travel.service.back.abs.AbstractService;
import cn.mldn.travel.vo.Dept;
import cn.mldn.travel.vo.Emp;
import cn.mldn.travel.vo.Level;

@Service
public class EmpServiceBackImpl extends AbstractService implements IEmpServiceBack {
	@Resource
	private IDeptDAO deptDAO;
	@Resource
	private IEmpDAO empDAO;
	@Resource
	private IRoleDAO roleDAO;
	@Resource
	private IActionDAO actionDAO;
	@Resource
	private ILevelDAO levelDAO;

	@Override
	public Map<String, Object> get(String eid, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		Emp emp = this.empDAO.findById(eid);
		if (emp != null) {
			if (password.equals(emp.getPassword())) {
				map.put("level", this.levelDAO.findById(emp.getLid()));
			}
		}
		map.put("emp", emp);
		return map;
	}

	@Override
	public Map<String, Set<String>> listRoleAndAction(String eid) {
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		map.put("allRoles", this.roleDAO.findAllIdByEmp(eid));
		map.put("allActions", this.actionDAO.findAllIdByEmp(eid));
		return map;
	}

	// 获得领导的个人信息
	@Override
	public Emp getEmpInfo(String eid) {

		Emp emp = this.empDAO.findById(eid);
		return emp;
	}

	// 获取领导的个人信息
	@Override
	public Map<String, Object> getInformationModal(String eid) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		// 对应的领导的的信息
		Emp emp = empDAO.findById(eid);
		if (map != null) {
			// 对应的领导的部门信息
			// ```````````````````````````````````````````````````````````
			map.put("alldept", deptDAO.findById(eid));
//			 map.put("alldept", deptDAO.findById(emp.getDid().toString()));
			// ```````````````````````````````````````````````````````````
			// 对应的领导的等级信息
			map.put("alllevel", levelDAO.findById(emp.getLid()));

		}
		map.put("allemp", emp);
		System.out.println(emp.getDid());

		return map;
	}

	
	
	
	
	
	

//	2.1.1查询所有的部门和等级
	@Override
	public Map<String, Object> addEmpPre() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("allLevels", this.levelDAO.findAll());
		map.put("allDepts", 	this.deptDAO.findAll());
		return map;
	}
//	2.1.2进行雇员的添加操作
	@Override
	public boolean addEmp(Emp emp) {
		// TODO Auto-generated method stub
		emp.setHiredate(new Date());
		System.out.println(emp);
//		1.首先要插入的雇员的id不重名
		if(empDAO.findById(emp.getEid())==null){
//			2.判断当前操作者是否是人事部门的
			Emp mgr = empDAO.findById(emp.getIneid());
//			3.如果当前 操作者对应的部门的ID是人事部门的
			if(mgr.getDid() ==2l){
//				4.如果当前的操作者是部门经理
				if(mgr.getLid().equals("manager")||mgr.getLid()=="manager"){
//					5.如果操作者是部门经理的话 那么就可以进行员工和经理的添加
//					5.1 判断如果添加的是经理的话，那么要先查询目标部门是否已经有经理，如果有的话，那么直接抛出异常
					if(emp.getLid().equals("manager")){//如果要插入的员工的等级是经理的话
								Dept dept = this.deptDAO.findByIdForDid(emp.getDid());//首先查出来 要插入 的雇员的所对应的部门
								if(dept.getEid()==null){
			//						先在emp中插入员工的详细信息，然后再在dept中插入员工的姓名
									this.empDAO.doCreate(emp);
									System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^"+emp.getEid());
									dept.setDid(emp.getDid());
									dept.setEid(emp.getEid());
									return this.deptDAO.doUpdateManager(dept);
								}else{
									throw new DeptManagerExistException("该部门已经有经理了，无法进行新任经理的添加！") ;
								}
					}else{
//						如果插入的员工不是经理的话
						return empDAO.doCreate(emp);
					}
				}
//				如果操作者是普通员工的话
				if(mgr.getLid().equals("staff")||mgr.getLid()=="staff"){
//					6.如果操作者是普通员工的话，那么只能进行普通员工的添加
					if(emp.getLid().equals("manager")){//如果要插入的员工的等级是经理的话
						throw new DeptManagerExistException("您没有权限！") ;
					}
					return empDAO.doCreate(emp);
				}
			}
		}
		return false;
	}
}
