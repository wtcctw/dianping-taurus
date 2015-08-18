<!DOCTYPE html >
<html >
<head>
	<title>Taurus</title>
	<#include "segment/html_header.ftl">
	<script src="${rc.contextPath}/lib/ace/js/jquery.dataTables.min.js"></script>
	<script src="${rc.contextPath}/lib/ace/js/jquery.dataTables.bootstrap.js"></script>
	<script type="text/javascript" src="${rc.contextPath}/resource/js/lib/Chart.js"></script>
	<#include "segment/html_header2.ftl">
	<link rel="stylesheet" href="${rc.contextPath}/css/loading.css">
</head>
<body data-spy="scroll">
<#include "segment/header.ftl">
<#include "segment/left.ftl">

<div class="main-content">

    <div class="breadcrumbs" id="breadcrumbs">
        <script type="text/javascript">
            try {
                ace.settings.check('breadcrumbs', 'fixed')
            } catch (e) {
            }
        </script>
        <ul class="breadcrumb">
            <li>
                <i class="icon-home home-icon"></i>
                <a href="${rc.contextPath}/index">HOME</a>
            </li>
            <li>
                <a href="${rc.contextPath}/schedule">调度中心</a>
            </li>
            <li class="active">
                <a href="${rc.contextPath}/attempt">调度历史</a>
            </li>
        </ul>
        <div style="float:right;padding-right: 5%">
            <a class="btn btn-info" href='#' onClick="redo('${RequestParameters.taskID!}')"><i class="icon-repeat">生成新实例并立即执行</i></a>
        </div>
    </div>

    <div class="page-content">
        <div id="alertContainer" class="container col-sm-12">
        </div>
        <div class="row">
            <div class="col-sm-12">

                <div id="attempt_content" class="align-center">
                    <div class="loadIcon">
                        <div></div>
                        <div></div>
                        <div></div>
                        <div></div>
                        <div></div>
                    </div>
                    正在加载中...
                </div>


            </div>
            <div id="confirm" class="modal hide fade">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h3 id="id_header"></h3>
                        </div>
                        <div class="modal-body">
                            <p id="id_body"></p>
                        </div>
                        <div class="modal-footer">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="feedModal" role="dialog"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
        </div>
    </div>
</div>
<div class="feedTool">

    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="${rc.contextPath}/img/qq.png"  width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img border="0" src="${rc.contextPath}/img/x_alt.png"  width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>
    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>
<script type="text/javascript">
    jQuery(function ($) {


        $('li[id="schedule"]').addClass("active");
        $('#menu-toggler').on(ace.click_event, function() {
            $('#sidebar').toggleClass('display');
            $(this).toggleClass('display');
            return false;
        });
        var taskID = "${RequestParameters.taskID!}";
        var attemptBody="";
        $.ajax({
            data: {
                action: "attempt",
                taskID:taskID
            },
            timeout: 2000,
            type: "POST",
            url: "${rc.contextPath}/attempt.do",
            error: function () {
                $("#attempt_content").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
                $("#attempt_content").addClass("align-center");
            },
            success: function (response, textStatus) {

                var jsonarray = $.parseJSON(response);
                attemptBody += " <table cellpadding='0' cellspacing='0' border='0' class='table table-striped table-bordered table-hover' width='100%' id='example'>"
                        + "<thead>"
                        + "<tr>"
                        + "<th>ID</th>"
                        + "<th>任务名</th>"
                        + "<th>实际启动时间</th>"
                        + "<th>实际结束时间</th>"
                        + " <th>预计调度时间</th>"
                        + "<th>IP</th>"
                        + "<th>返回值</th>"
                        + "<th>状态</th>"
                        + "<th class='center'>-</th>"
                        + "<th class='center'>报错</th>"
                        + "</tr>"
                        + " </thead>"
                        + "<tbody>";


                $.each(jsonarray, function (i, item) {
                    var state = item.state;

                        attemptBody += "<tr id='" + item.attemptId + "'>"
                                + "<td >" + item.id + "</td>"
                                + "<td>"
                                + item.taskName
                                + "</td>"
                                + "<td>"
                                + item.startTime
                                + "</td>"
                                + "<td>"
                                + item.endTime
                                + "</td>"
                                + "<td>"
                                + item.scheduleTime
                                + "</td>"
                                + "<td>"
                                + item.exeHost
                                + "</td>"
                                + "<td>"
                                + item.returnValue
                                + "</td>";

                    if(state == "RUNNING"){
                        attemptBody +=  "<td>"
                                + "<span class='label label-info'>"
                                + state
                                + "</span>"
                                + "</td>";
                    } else if(item.state == "SUCCEEDED") {
                        attemptBody +=  "<td>"
                                + "<span class='label label-success'>"
                                + state
                                + "</span>"
                                + "</td>";

                    }else{
                        attemptBody +=  "<td>"
                                + "<span class='label label-important'>"
                                + state
                                + "</span>"
                                + "</td>";
                    }

                    if(state == "RUNNING"||state == "TIMEOUT"){
                        attemptBody += "<td><a href='#confirm' onClick=\"action($(this).parents('tr').attr('id'))\">Kill</a>";
                        if(item.isViewLog){
                            attemptBody += " <a target='_blank'  href='${rc.contextPath}/viewlog?id="
                                    +item.attemptId
                                    +"&status="
                                    +state
                                    +"'>日志</a>";
                        }
                        attemptBody +="</td>"
                        attemptBody += "<td> <a id ='feedBtn' class='feedBtn'  href='${rc.contextPath}/feederror?id="
                                +item.attemptId
                                +"&status="
                                +state
                                +"&taskName="
                                +item.taskName
                                +"&ip="
                                +item.exeHost
                                +"&taskId="
                                +taskID
                                +"&feedtype=mail"
                                +"'><i class='icon-envelope'><img border='0' src='${rc.contextPath}/img/wechat.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></i></a> |";
                        attemptBody += "<a id ='feedQQBtn' class='feedBtn'  href='${rc.contextPath}/feederror?id="
                                +item.attemptId
                                +"&status="
                                +state
                                +"&taskName="
                                +item.taskName
                                +"&ip="
                                +item.exeHost
                                +"&taskId="
                                +taskID
                                +"&feedtype=qq"
                                +"'><img border='0' src='${rc.contextPath}/img/qq.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></a></td>"
                    }else{
                        attemptBody += "<td> <a target='_blank'  href='${rc.contextPath}/viewlog?id="
                        +item.attemptId
                        +"&status="
                        +state
                        +"'>日志</a></td>";

                        attemptBody += "<td> <a id ='feedBtn' class='feedBtn'  href='${rc.contextPath}/feederror?id="
                                +item.attemptId
                                +"&status="
                                +state
                                +"&taskName="
                                +item.taskName
                                +"&ip="
                                +item.exeHost
                                +"&taskId="
                                +taskID
                                +"&feedtype=mail"
                                +"'><i class='icon-envelope'><img border='0' src='${rc.contextPath}/img/wechat.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></i></a> |";
                        attemptBody += "<a id ='feedQQBtn' class='feedBtn'  href='${rc.contextPath}/feederror?id="
                                +item.attemptId
                                +"&status="
                                +state
                                +"&taskName="
                                +item.taskName
                                +"&ip="
                                +item.exeHost
                                +"&taskId="
                                +taskID
                                +"&feedtype=qq"
                                +"'><img border='0' src='${rc.contextPath}/img/qq.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></a></td>"
                    }

                    attemptBody +="</tr>"

                });
                attemptBody +="</tbody> </table>";
                $("#attempt_content").html(attemptBody);
                $("#attempt_content").removeClass("align-center");
                $('#example').dataTable({
                    bAutoWidth: true,
                    "bPaginate": true
                });

                $(".feedBtn").on('click', function(e) {

                    var anchor = this;
                    if (e.ctrlKey || e.metaKey) {
                        return true;
                    } else {
                        e.preventDefault();
                    }
                    $.ajax({
                        type: "get",
                        url: anchor.href,
                        error: function () {
                            $("#alertContainer").html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>报错失败</strong></div>');
                            $(".alert").alert();
                        },
                        success: function (response, textStatus) {
                            $("#feedModal").html(response);
                            $("#feedModal").modal().css({
                                backdrop:false

                            });

                        }

                    });
                });
            }
    });

    });
</script>

<script type="text/javascript" charset="utf-8" language="javascript" src="${rc.contextPath}/static/js/attempt.js"></script>
</body>
</html>