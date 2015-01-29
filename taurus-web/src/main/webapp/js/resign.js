/**
 * Created by kirinli on 14/11/21.
 */

$(document).ready(function () {

    $(".atip").tooltip();
    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('.scrollup').fadeIn();
        } else {
            $('.scrollup').fadeOut();
        }
    });

    $('.scrollup').click(function () {
        $("html, body").scrollTop(0);
        return false;
    });
    $( "#creatorbtn" ).on('click', function(e) {
        var taskName="";
        var oldcreators="";
        var jobId ="";
        var alertUser = "";
        var creator = $('input[name="creator"]:checked').val();
        $('input[name="taskcheckbox"]:checked').each(function(){
            taskName += $(this).val()+",";
            oldcreators += $(this).attr("id")+",";
            jobId += $(this).attr("alertId")+",";
            alertUser += $(this).attr("alertUser")+",";
        })
        taskName = taskName.substr(0,taskName.length - 1);

        oldcreators = oldcreators.substr(0,oldcreators.length - 1);
        if(taskName != null && creator != null && taskName!="" && creator!=""){
            bootbox.confirm("<i class='icon-info-sign icon-large red '>你确定把job名为:"+taskName+"的job的调度人修改为:"+creator+"？</i>"+"<i class='icon-info-sign icon-large red  '>此操作为危险操作，你所做的操作将被系统记录</i>" , function(result) {
                if(result){
                    $.ajax({
                        async: false,
                        data: {
                            action: "resign",
                            taskName:taskName,
                            creator:creator,
                            currentUser:currentUser,
                            oldcreators:oldcreators,
                            jobId:jobId,
                            userId:userId,
                            alertUser:alertUser
                        },
                        type: "POST",
                        url: "/resign",
                        error: function () {
                            $("#adjustout").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
                            $("#adjustout").addClass("align-center");
                        },
                        success: function (response, textStatus) {


                            bootbox.confirm("<i class='icon-info-sign icon-large red '>"+response+"</i>", function(result) {
                                if(result) {
                                    window.location.reload();
                                }
                            });
                        }


                    });
                }
            });

        }else{
            alert("输入不能为空！")
        }


    });
});
