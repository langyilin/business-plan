'use strict'
angular.$controllerProvider.register('visitListCtrl',function($scope, $state, $stateParams,layerService,workflowService){
	$scope.url = 'visit/visit/ajaxList.mvc';
	$scope.visit = {};
	
	//配置分页基本参数
    $scope.initPages({
    	currentPage: 1,
	    itemsPerPage: 15,
	    pagesLength: 11,
	    perPageOptions: [10, 20, 30, 40, 50],
        url: $scope.url + "?r=" + $.now(),
        formData: $scope.visit,
        dataSourceName: "visitList"
    });
    
    
    // 查询
    $scope.search = function() {
    	$scope.initPages({
    		currentPage: 1,
    		url: $scope.url + "?r=" + $.now(),
    		formData: $scope.visit
        })
	};
	
	// 删除
	$scope.del = function(id) {
		layer.confirm('确认删除？', {btn: ['是','否']},
			function(index){
				var postData = {id: id};
				$.post("visit/visit/delete.mvc", postData).then(function(response){
					if("success" == response) {
						$.tips.success("信件删除成功");
						$scope.search();
					}else {
						$.tips.fail("信件删除失败");
					}
				});
				layer.close(index);
			}, 
			function(index){
				layer.close(index);
			}
		);
	};
	
	$scope.viewHistory = function(processInstanceId){
		workflowService.viewDiagram($scope,processInstanceId);
	};
	
	
});