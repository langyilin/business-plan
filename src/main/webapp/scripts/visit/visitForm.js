'use strict'
angular.$controllerProvider.register('visitFormCtrl',function($scope, $state, $stateParams,workflowService){
	$scope.id = $stateParams.id; // 获取主键值
	$scope.taskId = $stateParams.taskId; // 获取主键值
	
	// 初始化表单
	$scope.initForm = function() {
		var postData = {id: $scope.id,taskId:$scope.taskId};
		$.post("visit/visit/initForm.mvc", postData).success(function(response){
			$scope.visit = response.visit;
			$scope.visit.taskId = $scope.taskId;
			$scope.moduleList = response.moduleList;
			if($scope.visit.processInstanceId){
				$scope.diagramUrl = "workflow/application/task/viewHistory.mvc?processInstanceId="+$scope.visit.processInstanceId;
				$scope.diagramUrl2 = "workflow/application/task/viewHistory.mvc?processInstanceId="+$scope.visit.processInstanceId+"&type=2";
			}
			
		});
	};
	window.fixFrameHeight = function(frameHeight){
		$("#diagram").height(frameHeight+10);
	}
	// 编辑查看时初始化
	$scope.initForm(); 
	//	保存、修改提交
	$scope.save = function(){

		// 表单验证成功后提交
		if($scope.visitForm.$valid) {
//			var postData = $("#visitForm").serialize();
//			postData=postData.replace(/&/g,"\",\"");  
//			postData=postData.replace(/=/g,"\":\"");  
//			postData="{\""+postData+"\"}"; 
//			postData = angular.fromJson(postData);
//			angular.extend($scope.visit, postData);
			var jbrbls = document.getElementsByName('jbrbl');
			var jbrbl = "1";
			for (var i=0; i<jbrbls.length; i++) {  
		        if (jbrbls[i].checked) {  
		        	jbrbl = jbrbls[i].value;
		        	break;
		        }  
		    }
			var cldwbls = document.getElementsByName('cldwbl');
			var cldwbl = "1";
			for (var i=0; i<cldwbls.length; i++) {  
		        if (cldwbls[i].checked) {  
		        	cldwbl = cldwbls[i].value;
		        	break;
		        }  
		    }
			var flags = document.getElementsByName('flag');
			var flag = "1";
			for (var i=0; i<flags.length; i++) {  
		        if (flags[i].checked) {  
		        	flag = flags[i].value;
		        	break;
		        }  
		    }
			$scope.visit.jbrbl=jbrbl;
			$scope.visit.cldwbl = cldwbl;
			$scope.visit.flag=flag;
			$scope.visit.taskId = $scope.taskId;
			$.ajax({
	  			url: "visit/visit/save.mvc",
			  	type: "post",
			  	data: JSON.stringify($scope.visit),
			  	dataType: "json",
			  	contentType: "application/json",
			  	success:function(data){
			  		if("success" == data) {
						$.tips.success("信件保存成功");
				  		setTimeout(function () {
					  		$state.go("app.visit.listDb");
				  		});
			  		}else if("failed" == data){
						$.tips.fail("信件保存失败");
					}else {
						$.tips.fail(data);
					}
			   	},
			   	error:function(data){
			   		$.tips.fail("信件保存失败");
			  	}
			});
		}
	};
	
	
	// 回调设置所属机构属性
	$scope.setParent = function(data) {
		if(data.id) {
			console.log(data);
			console.log($scope.visit);
			$scope.visit.cldwid = data.id;
			$scope.visit.cldwname = data.name;
		}
	};
	
	
	$scope.processDefinitionId = $stateParams.processDefinitionId;

	$scope.startVisitFlow = function(){
		var postData = $("#visitForm").serialize();
		$.post("visit/visit/startFlow.mvc", postData).then(function(response){
			if("success" == response) {
				$.tips.success("流程启动成功");
				$state.go("app.workflow.application.process.list");
			}else{
				$.tips.fail("流程启动失败");
			}
		});
	}
	
	$scope.backProcess = function(){
		workflowService.backProcess($scope,$scope.visit.taskId,function(index, response){
			$.tips.success("退回成功！");
			layer.close(index);
		});
	}
	
});