$(function(){
//	emp_info_modal.jsp 页面中 
	$(levelBtn).on("click",function(){
		did = $(this).attr("alt") ;
		console.log(did) ;
		$.post("pages/back/admin/dept/editMgr.action",{"did":did},function(data){
			$("#mgr-" + did).empty() ;	// 清空已有的领导信息
			operateAlert(data.trim() == "true","部门领导更新成功！","部门领导更新失败！") ;
			$("#userInfo").modal("toggle") ;
		},"text") ;
	}) ;
	
	$("button[id*='edit-']").each(function(){
		$(this).on("click",function(){
			did = this.id.split("-")[1] ;
			console.log("部门编号：" +  did) ;
			dname = $("#dname-" + did).val() ;
			console.log("部门名称：" +  dname) ;
			
			if (dname == "") { 
				operateAlert(false,"","部门名称不允许为空，请确认后再提交更新！") ;
			} else {
				// 编写Ajax异步更新操作
				$.post("pages/back/admin/dept/edit.action",{"did":did,"dname":dname},function(data){
					operateAlert(data.trim() != "true","栏目信息修改成功！","栏目信息修改失败！") ;
				},"text") ;
//				operateAlert(true,"部门名称更新成功！","") ;
			}
		}) ;
	}) ;
	$("span[id^=eid-]").each(function(){
		$(this).on("click",function(){
//			eid =  this.id.split("-")[1] +this.id.split("-")[2] ;
			eid =this.id.replace("eid-","");
			did = $(this).attr("alt") ;//获取alt 属性中的did
			console.log("雇员编号：" + eid) ;
			
//			// 编写Ajax异步更新操作
			$.post("pages/back/admin/emp/get.action",{"eid":eid},function(data){

				$("#modal-modalphoto").attr("src","upload/member/" + data.modalphoto) ;
				$("#modal-modaleEname").text(data.modaleEname) ;
				$("#modal-modallid").text(data.modallid) ;
				$("#modal-modaldDname").text(data.modaldDname) ;
				$("#modal-modalphone").text(data.modalphone) ;
				$("#modal-modalhiredate").text(data.modalhiredate) ;
				$("#modal-modalenote").html(data.modalenote) ;
				
				$("#levelBtn").attr("alt",did) ;	// 保存部门的编号信息
			},"json") ;
			$("#userInfo").modal("toggle") ;
		}) ;
	}) ;
}) ;