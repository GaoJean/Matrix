var envId = -1;
var appId = -1;
var version = -1;
var userId = '1' ;
var totalAppCount = 0;
var totalVersionCount = 0;

var isEnvInit = 0 ;
var isVersionInit = 0;

getSession();
var appList = [];

    /*
    *   获取环境和微服务
    */
    angular.module('myApp', []).controller('namesCtrl', function($scope) {
        //
        // 获取APP信息
        //
        $scope.apps = [{id:'1',name:"正在初始化数据"}];
        $.ajax({
            type: "GET",
            url: "/api/app/list"
        }).done(
            function (data) {
            if (data.success === "true") {
                var html = "";
                var result = data.page.result;
                $scope.apps = result;
                $scope.$apply();
            }
        });
            //
            // 获取Env信息
            //
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
                    $scope.$apply();
                }
            }); 
        // 获取操作记录
        $scope.logs = [{id:'1',name:"正在初始化数据"}];
         $.ajax({
            type: "GET",
            url: "/api/web/config/getLogsTop5"
        }).done(
            function (data) {
                if (data.success === "true") {
                    var html = "";
                    var result = data.page.result;
                    $scope.logs = result;
                    $scope.$apply();
                }
            }); 
        
    });
    /*
    *   获取微服务列表
    */
    $("#envChoice").on('click', 'li', function () {
        envId = $(this).attr('id');
        $("#env_info").html($(this).text());
        $("#envChoice li").removeClass("active");
        $(this).addClass("active");
        $("#appDropdownMenuTitle").text($(this).text());

        $('.versionsManage').html(''); //清除版本号原来的数据
        var getAppData = {
            "envId":envId, 
            "userId":"1" ,
            "currentPage":"1",
            "pageCount":"10"
        }
        $.ajax({
            type: "post",
            data: getAppData,
            // contentType: "application/json;charset=utf-8",
            url: '/api/app/getApp',
            beforeSend:function(){
                layer.msg('加载中', {icon: 16,shade:0.3}); 
            },
            success: function (data) {
                 // var data = JSON.parse(data); 
                 console.log(data);  
                 if (data.success != 'true') {
                    alert(data.message);
                    return false;
                 }

                $('.appManage ').html('');
                $('.appManage').append('<li class="th"><div class="app" ><span>微服务</span></div><div class="handle"><span >操作</span></div></li>')
                 appList =  data.page.result;
                 if (appList.length <= 0) {
                    return ;
                 };
                 for(app in appList){
                    $('.appManage').append('<li ><div class="app" ><span id='+appList[app].id+' onclick="evnClick(id)">' + appList[app].name + '</span></div><div id='+appList[app].id+'&'+appList[app].name+'  onclick="deleteEnv(id)"    class="handle"><span >删除</span></div></li>')
                 }
                $("#appPagination").pagination(data.page.totalCount/10, {
                    num_edge_entries: 1, //边缘页数
                    num_display_entries: 10, //主体页数

                    callback: envPageselectCallback,
                    items_per_page: 1 //每页显示1项
                })
            },
            complete:function(){
                layer.closeAll();
            },
            error: function (data) {
                console.info("error: " + data.responseText);
            }
        });
    });


    /*
    *   获取版本号列表
    */
    var evnClick = function(app){
        var appArray = app.split('&');
        appId = appArray[0];
        var reqDate = {
                "appId":appId,
                "envId":envId,                            //环境id
                "userId":userId,
                "currentPage":"1",
                "pageCount":"10"
            }
        $.ajax({
            type: "post",
            data : reqDate,
            url: "/api/config/getVersion",
            beforeSend:function(){
                layer.msg('加载中', {icon: 16,shade:0.5}); 
            },
            success: function (data) {
                console.log(data); 
                layer.closeAll()
                $('.versionsManage').html(''); //清除原来的数据
                $('.versionsManage').append('<li class="th"><div class="app" ><span>版本号</span></div><div class="handle"><span >操作</span></div></li>')
                var appList =  data.page.result;
                for(app in appList){
                    $('.versionsManage').append('<li><div class="app" ><span>' + appList[app].version + '</span></div>'+
                        '<div class="handle"  ><span id=copy&'+appList[app].version+' onclick="copyApp(id)" style="color:#445670">复制</span>'+
                        '<span id='+appList[app].version+' onclick="deleteApp(id)" style="margin-left:20px;color:gray">删除</span></div></li>')
                }
                totalVersionCount = data.totalVersionCount;
                $("#versionsPagination").pagination(data.totalVersionCount/10, {
                    num_edge_entries: 1, //边缘页数
                    num_display_entries: 10, //主体页数
                    callback: versionPageselectCallback,
                    items_per_page: 1 //每页显示1项
                })
            },
            complete:function(){
                layer.closeAll();
            },
            error: function (data) {
                console.info("error: " + data.responseText);
            }
        }); 
    }
    //删除微服务  －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
    var deleteEnv = function(app){
        var appArray = app.split('&');
        var appId = appArray[0];
        var appName = appArray[1];
        layer.confirm('确定要删除微服务环境："'+appName+'" 吗？',function(){
            console.log('删除'); 

            var reqDate = {
                'envId' : envId,
                'appId' : appId,
                'userId' : userId
            }

            $.ajax({
                type: "post",
                data : reqDate,
                url: "/api/web/config/deleteApp",
                success: function (data) {
                    console.log(data); 
                    if(data.success == "true")
                    {
                        layer.alert('删除成功',{icon:1});
                        envPageselectCallback(0);
                    }else{
                        layer.alert('删除失败',{icon:5});
                    }
                },
                complete:function(){
                    layer.closeAll('loading');
                },
                error: function (data) {
                    console.info("error: " + data.responseText);
                }
            });  
        },function(){
            console.log('取消');
        })
    }

    // 复制 －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
    var copyApp = function(id){
            // 弹窗
            layer.open({
              type: 1,
              skin: 'layui-layer-demo', //样式类名
              btn :['复制','取消'],
              closeBtn: 0, //不显示关闭按钮
              shift: 2,
              area : ['400px'],
              shadeClose: true, //开启遮罩关闭
              content: $('.copy'),
              yes:function(){
                if ( $('#version_copy').val() == '' ) {
                    $('#copy_error_info').css({'display':'block'});
                    return false;
                }
                var versionArray = id.split('&');
                var version = versionArray[1];
                var reqData = {
                    "userId"                : userId,
                    "envIdCopySource"       : envId,                         //复制环境源id 
                    "appIdCopySource"       : appId,                          //复制微服务源Id
                    "versionNameCopySource" : version,                   //复制版本源name
                    
                    "envIdCopyTarget"       : $('#evn_copy').val(),                          //复制环境目标Id
                    "appIdCopyTarget"       : $('#app_copy').val(),                           //复制微服务目标Id
                    "versionNameTarget"     : $('#version_copy').val()                     //复制版本目标name
                }
                console.log(reqData);
                $.ajax({
                    type: "post",
                    data : reqData,
                    url: "/api/web/config/copyProperties",
                    success: function (data) {
                        if (data.success == 'true') {
                            layer.closeAll();
                            layer.msg(data.result);
                            window.location.reload();
                            return true;
                        }else{
                            layer.alert('复制失败',{icon:5});
                            return false;
                        }
                        console.log(data); 
                    },
                    error: function (data) {
                        console.info("error: " + data.responseText);
                    }
                }); 
              },
              btn2 : function(){
                 $('#copy_error_info').css({'display':'none'})
              }

            });
    }

    // 删除版本号
    var deleteApp = function(version){


        console.log(version);
        layer.confirm('确定要删除版本号："'+version+'" 吗？',function(){
            console.log('删除');  
            var reqDate = {
                'envId' : envId,
                'appId' : appId,
                'userId' : userId,
                'version' : version,
            }

            $.ajax({
                type: "post",
                data : reqDate,
                url: "/api/web/config/deleteVersion",
                success: function (data) {
                    console.log(data); 
                    if(data.success == "true")
                    {
                        layer.alert('删除成功',{icon:1});
                        versionPageselectCallback(0);
                    }else{
                        layer.alert('删除失败',{icon:5});
                    }
                },
                error: function (data) {
                    console.info("error: " + data.responseText);
                }
            });  
        },function(){
            console.log('取消');
        })
    }

    // 翻页回调函数--------------------------------------------------------------------------------
    //   获得微服务
    function envPageselectCallback(page_index, jq){
        if (isEnvInit == 0) {
            console.log('isEnvInit');
            isEnvInit ++;
            return false;
        };
        page_index ++;
        console.log(page_index);
                var getAppData = {
                    "envId":envId, 
                    "userId":"1" ,
                    "currentPage":page_index,
                    "pageCount":"10"
                }

                $.ajax({
                    type: "post",
                    data: getAppData,
                    // contentType: "application/json;charset=utf-8",
                    url: '/api/app/getApp',
                    beforeSend:function(){
                        layer.msg('加载中', {icon: 16,shade:0.3}); 
                    },
                    success: function (data) {
                         // var data = JSON.parse(data); 
                         console.log(data);  
                         if (data.success != 'true') {
                            alert(data.message);
                            return false;
                         }

                        $('.appManage ').html('');
                        $('.appManage').append('<li class="th"><div class="app" ><span>微服务</span></div><div class="handle"><span >操作</span></div></li>')
                         appList =  data.page.result;
                         if (appList.length <= 0) {
                            return ;
                         };
                         for(app in appList){
                            $('.appManage').append('<li ><div class="app" ><span id='+appList[app].id+'&envId'+' onclick="evnClick(id)">' + appList[app].name + '</span></div><div id='+appList[app].id+'&'+appList[app].name+'  onclick="deleteEnv(id)"    class="handle"><span >删除</span></div></li>')
                         }
                    },
                    error: function (data) {
                        console.info("error: " + data.responseText);
                    }
                });
        return false;
    }
    // 获得版本号
    function versionPageselectCallback(page_index,jq){
        if (isVersionInit == 0 ) {
            console.log('isVersionInit');
            isVersionInit++;
            return false;
        };
        page_index ++;
        var reqDate = {
                "appId":appId,
                "envId":envId,                            //环境id
                "userId":userId,
                "currentPage":page_index,
                "pageCount":"10"
            }

        $.ajax({
            type: "post",
            data : reqDate,
            url: "/api/config/getVersion",
            success: function (data) {
                console.log(data); 
                $('.versionsManage').html(''); //清除原来的数据
                $('.versionsManage').append('<li class="th"><div class="app" ><span>版本号</span></div><div class="handle"><span >操作</span></div></li>')
                var appList =  data.page.result;
                for(app in appList){
                 $('.versionsManage').append('<li><div class="app" ><span>' + appList[app].version + '</span></div>'+
                        '<div class="handle"  ><span id=copy&'+appList[app].version+' onclick="copyApp(id)" style="color:#445670">复制</span>'+
                        '<span id='+appList[app].version+' onclick="deleteApp(id)" style="margin-left:20px;color:gray">删除</span></div></li>')
                 }
            },
            error: function (data) {
                console.info("error: " + data.responseText);
            }
        }); 
    }
