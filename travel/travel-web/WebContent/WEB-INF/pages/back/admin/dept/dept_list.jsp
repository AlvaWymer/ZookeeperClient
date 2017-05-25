<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/pages/plugins/back/back_header.jsp" />
<script type="text/javascript"
	src="js/pages/back/admin/dept/dept_list.js"></script>
<body class="hold-transition skin-blue sidebar-mini">
	<div class="wrapper">
		<!-- 导入头部标题栏内容 -->
		<jsp:include page="/WEB-INF/pages/plugins/back/include_title_head.jsp" />
		<!-- 导入左边菜单项 -->
		<jsp:include page="/WEB-INF/pages/plugins/back/include_menu_item.jsp">
			<jsp:param name="mi" value="1" />
			<jsp:param name="msi" value="11" />
		</jsp:include>
		<div class="content-wrapper text-left">
			<div class="panel panel-success">
				<div class="panel-heading">
					<strong><span class="glyphicon glyphicon-list"></span>&nbsp;部门列表</strong>
				</div>
				<div class="panel-body">
					<table class="table table-condensed">
						<thead>
							<tr>
								<th class="text-center">部门名称</th>
								<th class="text-center">领导姓名</th>
								<!-------------------------------------->
								<!-- 总监不能操作修改，并且必须要具有部门修改的权限 -->
								<c:if test="${level != 1}">
									<shiro:hasPermission name="dept:edit">
										<th class="text-center">操作</th>
									</shiro:hasPermission>
								</c:if>
								<!-------------------------------------->
							</tr>
						</thead>
						<tbody>



							<c:forEach items="${allDeptName}" var="dept">

								<tr>
									<td class="text-center">
									<!-------------------------------------->
									<!-- 如果具有部门的修改权限，可以修改部门的名字，如果没有权限，那么只能查看 -->
									<shiro:hasPermission name="dept:edit">
									<input type="text" id="dname-${dept.did}" class="form-control" value="${dept.dname }"></td>
									</shiro:hasPermission>
									<shiro:lacksPermission name="dept:edit"> ${dept.dname} </shiro:lacksPermission>
									<!-------------------------------------->
									<!-- 对应的部门领导 -->
									<td class="text-center" id="mgr-${dept.did}">
									<!--  !!注意：【alt】  是自定义属性，这是为了当点击 部门所对应的经理的姓名的时候，
									往模态窗口 里面传递did（部门id）详见：dept_list.js-->
									<span id="eid-${dept.eid}" style="cursor: pointer;" alt="${dept.did}">${allEmpManagerName[dept.eid]}</span></td>
									<!---------权限控制--------------->
										<!-- 对应的编辑按钮  -->
									<shiro:hasPermission name="dept:edit">
										<td class="text-center">
											<button id="edit-${dept.did}" class="btn btn-warning">
												<span class="glyphicon glyphicon-edit"></span>&nbsp;编辑
											</button>
										</td>
									</shiro:hasPermission>
									<!------------------------>
								</tr>
							</c:forEach>


						</tbody>
					</table>
				</div>
				<div class="panel-footer">
					<jsp:include page="/WEB-INF/pages/plugins/include_alert.jsp" />
				</div>
			</div>
		</div>
		<!-- 导入公司尾部认证信息 -->
		<jsp:include page="/WEB-INF/pages/plugins/back/include_title_foot.jsp" />
		<!-- 导入右边工具设置栏 -->
		<jsp:include
			page="/WEB-INF/pages/plugins/back/include_menu_sidebar.jsp" />
		<div class="control-sidebar-bg"></div>
	</div>
	<jsp:include page="/WEB-INF/pages/plugins/back/info/emp_info_modal.jsp" />
	<jsp:include
		page="/WEB-INF/pages/plugins/back/include_javascript_foot.jsp" />
	<jsp:include page="/WEB-INF/pages/plugins/back/back_footer.jsp" />