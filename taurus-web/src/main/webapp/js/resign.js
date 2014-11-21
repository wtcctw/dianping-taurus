/**
 * Created by kirinli on 14/11/21.
 */

$(document).ready(function () {


    $( "#creatorbtn" ).on('click', function(e) {
        var taskName="";
        var creator = $('input[name="creator"]:checked').val();
        $('input[name="taskcheckbox"]:checked').each(function(){
            taskName+=$(this).val()+",";
        })
        taskName = taskName.substr(0,taskName.length - 1);

        if(taskName != null && creator != null && taskName!="" && creator!=""){
            bootbox.confirm("危险操作，你确定把job名为:"+taskName+"的job的调度人修改为:"+creator+"？", function(result) {
                if(result){
                    $.ajax({
                        async: false,
                        data: {
                            action: "resign",
                            taskName:taskName,
                            creator:creator
                        },
                        type: "POST",
                        url: "/monitor",
                        error: function () {
                            $("#adjustout").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
                            $("#adjustout").addClass("align-center");
                        },
                        success: function (response, textStatus) {

                            $("#adjustout").html("<i class='icon-info-sign icon-large red '>"+response+"</i>");
                            $("#adjustout").addClass("align-center");
                        }


                    });
                }
            });

        }else{
            alert("输入不能为空！")
        }


    });
});
