
angular.module("myApp",[]).controller("SerchController",["$scope",function($scope){
	$scope.envs = [];
	$scope.items = [{id:'1',name:"正在初始化数据"}];
    $.ajax({
       type: "GET",
       url: "/api/env/list"
   }).done(
       function (data) {
           if (data.success === "true") {
               var html = "";
               var result = data.page.result;
               $scope.items = result;
               $scope.envs = result.name;
               $scope.$apply();
           }
       }); 
      }]);

angular.module('myApp', []).controller('SerchController', ['$scope', function($scope) {
    $scope.version = [];
    $scope.myFunc = function(envId) {
    	var base_url = "/api/web/config/versionlist?appId=12&envId=" + envId;
        $.ajax({
            type: "GET",
            url: base_url
        }).done(function (data) {
            if (data.success === "true") {
                var result = data.page.result;
                $scope.version=result;
            }
    });
    }}]);

angular.module('myApp', []).controller('SerchController', ['$scope', function($scope) {
    $scope.tableData = [];
    $scope.getData = function(envId,versin) {
    	var getAppData = {
                "envId":envId, 
                "userId":"1" ,
                "currentPage":"1",
                "pageCount":"10"
            }
    		$.ajax({
                type: "post",
                data: getAppData,
                url: '/api/app/getApp',
                success: function (data) {
                     appList =  data.page.result;
                     for (var i=0 ;i<appList.lenth;i++){
                    	 	var base_url="/api/web/config/list?appId="+appList[i].id+"&envId="+envId+"&version="+versin+"&"
                    	    $.ajax({
                                type: "GET",
                                url: base_url
                            }).done(function (data) {
                                if (data.success === "true") {
                                    var result = data.page.result;
                                    for (var j=0;j<result.lenth;j++){
                                    	if (result[j].machineSize<1){
                                    		$scope.tableData=[result[j].appName,result[j].key,result[j].machineSize];
                                    	}
                                    }
                                }
                        });
                     }
                     
                }  
                    
    });
    }}]);



       
   