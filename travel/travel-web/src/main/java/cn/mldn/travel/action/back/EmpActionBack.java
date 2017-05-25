package cn.mldn.travel.action.back;

import java.text.SimpleDateFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.mldn.travel.dao.IDeptDAO;
import cn.mldn.travel.exception.DeptManagerExistException;
import cn.mldn.travel.service.back.IEmpServiceBack;
import cn.mldn.travel.vo.Dept;
import cn.mldn.travel.vo.Emp;
import cn.mldn.travel.vo.Level;
import cn.mldn.util.action.abs.AbstractBaseAction;
import cn.mldn.util.enctype.PasswordUtil;
import cn.mldn.util.web.FileUtils;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/pages/back/admin/emp/*")
public class EmpActionBack extends AbstractBaseAction {
	private static final String FLAG = "雇员";
	@Resource
	private IEmpServiceBack iempServiceBack;

	@Resource
	private IDeptDAO ideptDAO;

	// ---------------------------------------------------------------------------------------------------
	/**
	 * 2.1.1 增加雇员前的部门信息获取和职位等级获取模块<br>
	 * 进行页面的数据查询操作，包含的查询内容：<br>
	 * 1.部门名字的查询<br>
	 * 2.职位等级的查询
	 * 
	 * @return
	 */
	@RequestMapping("add_pre")
	@RequiresUser
	@RequiresRoles("emp")
	@RequiresPermissions(value = { "emp:add", "empshow:get", "emp:get" }, logical = Logical.OR)
	public ModelAndView addPre() {
		ModelAndView mav = new ModelAndView(super.getUrl("emp.add.page"));

		/**
		 * 注意！！！此处有坑！！前台页面获取的是下拉列表，因为不是使用的二级级联关联列表，所以
		 * 
		 */
		mav.addAllObjects(this.iempServiceBack.addEmpPre());

		return mav;
	}
	// ---------------------------------------------------------------------------------------------------

	// ---------------------------------------------------------------------------------------------------

	/**
	 * 2.1.2 增加雇员模块<br>
	 * 进行页面的数据查询操作，包含的查询内容：<br>
	 * 1.部门名字的查询<br>
	 * 2.职位等级的查询
	 * 
	 * @return
	 */
	@RequestMapping("add")
	@RequiresUser
	@RequiresRoles("emp")
	@RequiresPermissions("emp:add")
	public ModelAndView add(Emp vo, MultipartFile pic, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("back.forward.page"));
		// 1.设置密码，进行加密操作
		vo.setPassword(PasswordUtil.getPassword(vo.getPassword()));
		// 2.locked=0（活跃）、locked=1（锁定）、locked=2（删除）。
		vo.setLocked(0);
		// 3.取得当前操作者的用户的名字
		vo.setIneid(super.getEid());
		// 4.设置上传的图片
		FileUtils fileUtil = null;
		if (!pic.isEmpty()) { // 如果说现在有文件上传
			fileUtil = new FileUtils(pic);
			// 上传文件的名字
			vo.setPhoto(fileUtil.createFileName());
		}
		try {
			// 如果添加雇员的信息成功
			if (iempServiceBack.addEmp(vo)) {
				// 上传文件到网站的指定目录
				if (fileUtil != null) { // 如果上传的图片不为空
					fileUtil.saveFile(request, "upload/member/", vo.getPhoto());
					System.out.println("增加的图片"+fileUtil.saveFile(request, "upload/member/", vo.getPhoto()));
					
				}
				super.setUrlAndMsg(request, "emp.add.action", "vo.add.success", FLAG);
			} else {
				super.setUrlAndMsg(request, "emp.add.action", "vo.add.failure",FLAG);
			}

		} catch (Exception e) {
//			e.printStackTrace();
			super.setUrlAndMsg(request, "emp.add.action", "emp.add.dept.mgr.failure");
		}

		
		return mav;
	}

	// ---------------------------------------------------------------------------------------------------
	@RequestMapping("edit_pre")
	@RequiresUser
	@RequiresRoles("emp")
	@RequiresPermissions("emp:edit")
	public ModelAndView editPre(String eid) {
		ModelAndView mav = new ModelAndView(super.getUrl("emp.edit.page"));
		return mav;
	}

	@RequestMapping("edit")
	@RequiresUser
	@RequiresRoles("emp")
	@RequiresPermissions("emp:edit")
	public ModelAndView edit(Emp vo, MultipartFile pic, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("back.forward.page"));
		// super.setUrlAndMsg(request, "emp.list.action", "vo.edit.failure",
		// FLAG);
		super.setUrlAndMsg(request, "emp.list.action", "vo.edit.success", FLAG);
		return mav;
	}

	// 根据雇员的id 查询到雇员的信息
	@Resource
	private IEmpServiceBack iEmpServiceBack;

	@RequestMapping("get")
	@RequiresUser
	@RequiresRoles(value = { "emp", "empshow" }, logical = Logical.OR)
	@RequiresPermissions(value = { "emp:get", "empshow:get" }, logical = Logical.OR)
	public ModelAndView get(String eid, HttpServletResponse response) {
		// System.out.println("0000000000"+iEmpServiceBack.getInformationModal(eid));
		System.out.println("!!!!!!!!!!!!!!!!!!!!!" + super.getEid() + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
		Emp emp = (Emp) this.iEmpServiceBack.getInformationModal(eid).get("allemp");
		Dept dept = (Dept) this.iEmpServiceBack.getInformationModal(eid).get("alldept");
		Level level = (Level) this.iEmpServiceBack.getInformationModal(eid).get("alllevel");

		JSONObject json = new JSONObject();
		json.put("modalphoto", emp.getPhoto());
		json.put("modaleEname", emp.getEname());
		json.put("modallid", level.getTitle());
		json.put("modaldDname", dept.getDname());
		json.put("modalphone", emp.getPhone());
		json.put("modalhiredate", simple.format(emp.getHiredate()).toString());
		json.put("modalenote", emp.getNote());
		super.print(response, json);
		return null;

	}

	@RequestMapping("list")
	@RequiresUser
	@RequiresRoles(value = { "emp", "empshow" }, logical = Logical.OR)
	@RequiresPermissions(value = { "emp:list", "empshow:list" }, logical = Logical.OR)
	public ModelAndView list(String ids, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("emp.list.page"));
		return mav;
	}

	@RequestMapping("delete")
	@RequiresUser
	@RequiresRoles("emp")
	@RequiresPermissions("emp:delete")
	public ModelAndView delete(String ids, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getUrl("back.forward.page"));
		// super.setUrlAndMsg(request, "emp.list.action", "vo.delete.failure",
		// FLAG);
		super.setUrlAndMsg(request, "emp.list.action", "vo.delete.success", FLAG);
		return mav;
	}

	// ---------------------------------------------------------------------------------------------------

}
