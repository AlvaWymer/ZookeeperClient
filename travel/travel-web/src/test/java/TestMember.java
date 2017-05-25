
import javax.annotation.Resource;

import org.apache.taglibs.standard.lang.jstl.EmptyOperator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.mldn.travel.service.back.IDeptServiceBack;
import cn.mldn.travel.service.back.IEmpServiceBack;
import cn.mldn.travel.service.back.impl.DeptServiceBackImpl;
import cn.mldn.travel.service.back.impl.EmpServiceBackImpl;
import cn.mldn.travel.vo.Dept;
import cn.mldn.travel.vo.Emp;

@ContextConfiguration(locations = {"classpath:spring/spring-common.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestMember {
	
	@Resource
	private IDeptServiceBack ideptServiceBack;
	
	@Resource
	private IEmpServiceBack iempServiceBack;
	/**
	 * 验证用户名
	 */
	@Test
	public void testGet() {
		System.out.println("+++++++"+ideptServiceBack.listAllDnameAndEname());
	}
//	/**
//	 * 验证根据用户名 是否能够得到 用户对应的权限和角色
//	 *
//	 */
//	@Test
//	public void testRoleAndAction() {
//		System.out.println(memberService.getRoleAndAction("mldn"));
//	}
	
	/**
	 * 验证获取用户的个人信息
	 */
	@Resource
	private IEmpServiceBack iEmpServiceBack;
	@Test
	public void testGet1() {
		System.out.println("testGet1()+++++++-----"+iEmpServiceBack.getEmpInfo("mldn1"));
	}
	

	/**
	 * 根据部门id 更新部门的名称
	 */
	
	@Test
	public void testGet2() {
		Dept dept= new Dept();
		dept.setDid(7L);
		dept.setDname("shichangbu");
		dept.setEid("mldn-dev3");
		System.out.println(ideptServiceBack.updateDeptName(dept));
	}
	
//	@Resource
//	private IDeptServiceBack ideptServiceBack;
	
	@Test
	public void testGet3() {
		System.out.println(iEmpServiceBack.getInformationModal("mldn-dev1"));
	}
	
	@Test
	public void testGet4() {
		System.out.println(ideptServiceBack.updateDeptNameAndManager(7l,"mldn-human"));
	}
	
	@Test
	public void testGet5() {
		System.out.println(this.iempServiceBack.addEmpPre());
	}
	
//	2.1.1测试雇员添加
//	唯一的eid，普通员工，5部门，mldn-human
	@Test
	public void testGet6() {
		Emp emp = new Emp();
		emp.setEid("111");
		emp.setLid("staff");
		emp.setDid(6L);
		emp.setIneid("mldn-human");
		System.out.println("!!!!!!!!!"+iempServiceBack.addEmp(emp));
	}
//	2.1.1测试雇员添加
//	唯一的eid，插入的员工为部门经理，5部门，mldn-human
	@Test
	public void testGet7() {
		Emp emp = new Emp();
		emp.setEid("222");
		emp.setLid("manager");
		emp.setDid(5L);
		emp.setIneid("mldn-human");
		System.out.println("!!!!!!!!!"+iempServiceBack.addEmp(emp));
	}
	
//	2.1.2测试雇员添加
//	唯一的eid，插入的员工为部门经理，5部门，mldn-human
	@Test
	public void testGet8() {
		Emp emp = new Emp();
		emp.setEid("4323");
		emp.setLid("manager");
		emp.setDid(5L);
		emp.setIneid("mldn1");
		System.out.println("!!!!!!!!!"+iempServiceBack.addEmp(emp));
	}
	
	
}
