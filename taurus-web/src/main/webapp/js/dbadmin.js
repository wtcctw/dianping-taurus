
$(document).ready(function () {
$( "#querybtn" ).on('click', function(e) {
var taskId = $("#sqlinput").val();
    var status = $("#status").val();
    alert(status);
     if(taskId != null && taskId!=""){
         bootbox.confirm("危险操作，请确定清理taskId:"+taskId+" 的DEPENDENCY_PASS状态？", function(result) {
             if(result){
                 $.ajax({
                     async: false,
                     data: {
                         action: "sqlquery",
                         taskId:taskId,
                         status:status
                     },
                     type: "POST",
                     url: "/monitor",
                     error: function () {
                         $("#sqloutput").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
                         $("#sqloutput").addClass("align-center");
                     },
                     success: function (response, textStatus) {

                         $("#sqloutput").html("<i class='icon-info-sign icon-large red '>"+response+"</i>");
                         $("#sqloutput").addClass("align-center");
                     }


                 });
             }
         });

     }else{
         alert("输入不能为空！")
     }


});
    $( "#clearbtn" ).on('click', function(e) {
        var start = $("#start").val();
        var end = $("#end").val();

        if(start != null && end != null && start!="" && end!=""){
            bootbox.confirm("危险操作，请确定start:"+start+" end :"+end+"Zookeeper节点？", function(result) {
                if(result){
                    $.ajax({
                        async: false,
                        data: {
                            action: "clearzknodes",
                            start:start,
                            end:end
                        },
                        type: "POST",
                        url: "/monitor",
                        error: function () {
                            $("#clearoutput").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
                            $("#clearoutput").addClass("align-center");
                        },
                        success: function (response, textStatus) {

                            $("#clearoutput").html("<i class='icon-info-sign icon-large red '>"+response+"</i>");
                            $("#clearoutput").addClass("align-center");
                        }


                    });
                }
            });

        }else{
            alert("输入不能为空！")
        }


    });
});
