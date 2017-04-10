var appId = -1;
var envId = -1;
var version = "";
getSession();

// 提交
$("#item_submit").on("click", function (e) {
    $("#error").addClass("hide");

    if (version == '自定义版本') {
        version = $('#selfversion_value').val();
    }

    var key = $("#key").val();
    var value = $("#value").val();

    // 验证
    if (appId < 1 || envId < 1 || version == "" || !value || !key) {
        $("#error").removeClass("hide");
        $("#error").html("表单不能为空或填写格式错误！");
        return;
    }
    layer.msg('加载中', {icon: 16,shade:0.5}); 

    $.ajax({
        type: "POST",
        url: "/api/web/config/item",
        data: {
            "appId": appId,
            "version": version,
            "key": key,
            "envId": envId,
            "value": value
        }
    }).done(function (data) {
        layer.closeAll();
        $("#error").removeClass("hide");
        if (data.success === "true") {
            $("#error").html(data.result);
            layer.confirm("是否继续创建?",{btn:['继续创建','返回首页']},function(){
                $("#key").val('');
                $("#value").val('');
                $('#selfversion_value').val('');
                window.location.reload();
                layer.closeAll();
            },function(){
                window.location.href = "main.html";
            })
        } else {
            Util.input.whiteError($("#error"), data);
        }
    });
});
