<!DOCTYPE html >
<html >
<head>
	<meta name="description" content=""/>
	<title>Taurus</title>
	<#include "segment/html_header.ftl">
	<!-- page specific plugin scripts -->
    <script src="${rc.contextPath}/lib/ace/js/jquery.dataTables.min.js"></script>
    <script src="${rc.contextPath}/lib/ace/js/jquery.dataTables.bootstrap.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/static/js/jquery.autocomplete.js"></script>
	<#include "segment/html_header2.ftl">
	<link rel="stylesheet" href="${rc.contextPath}/css/loading.css">
    <style>
        .autocomplete-suggestions { border: 1px solid #DDD; background: #FFF; overflow: auto; }
        .autocomplete-suggestion { padding: 2px 5px; white-space: nowrap; overflow: hidden; }
        .autocomplete-selected { background: rgba(113,182,243,0.75); }
        .autocomplete-suggestions strong { font-weight: normal; color: #DCA43B; }
        .autocomplete-group { padding: 2px 5px; }
        .autocomplete-group strong { display: block; border-bottom: 1px solid #111; }
    </style>
</head>
<body>
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
            <li class="active">
                <a href="${rc.contextPath}/schedule">调度中心</a>
            </li>
        </ul>
    </div>



    <div class="page-content ">
        <div id="alertContainer" class="col-sm-12"></div>
        <div class="row">
            <div class="span3 hide">
                <div class="well well-large">
                    <form class="form-horizontal selector-form">
                        <div class="control-group hide">
                            <label class="control-label">任务组</label>

                            <div class="controls">
                                <select id="selector-task-group-id">
                                    <option value="">--选择全部--</option>
                                    <option value="1">wormhole</option>
                                    <option value="2">mid/dim</option>
                                    <option value="3">dm</option>
                                    <option value="4">rpt</option>
                                    <option value="5">mail</option>
                                    <option value="6">dw</option>
                                </select>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">创建人</label>

                            <div class="controls">
                                <select id="selector-cycle">

                                </select>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">任务名称</label>

                            <div class="controls">
                                <input type="text" id="selector-task-name" placeholder="模糊查询...">
                            </div>
                        </div>
                        <div class="control-group">
                            <div class="controls">
                                <button type="submit" id="search-btn" class="btn btn-large btn-primary pull-right">
                                    开始查询
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="col-sm-12">
            	<#if RequestParameters.path?exists && RequestParameters.path != "">
            		<span style="color:red">提示:已部署的作业文件的路径为${RequestParameters.path!}</span>
            	</#if>

                <div id="schedule_content" class="align-center">
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
        </div>
    </div>

    <div id="confirm" class="hide">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3 id="id_header"></h3>
        </div>
        <div class="modal-body">
            <p id="id_body"></p>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn" data-dismiss="modal" aria-hidden="true">取消</a>
            <a href="#" class="btn btn-danger" onClick="action_ok()">确定</a>
        </div>
    </div>
</div>
<!-- detailModal -->

<div class="modal fade" id="detailModal" role="dialog"
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
        $('#menu-toggler').on(ace.click_event, function () {
            $('#sidebar').toggleClass('display');
            $(this).toggleClass('display');
            return false;
        });

        var name = "${RequestParameters.name!}";
        var path = "${RequestParameters.path!}";
        var appname ="${RequestParameters.appname!}";
        var currentUser = "${currentUser!}";
        var scheduleBody = "";
        var isAdmin = "${isAdmin?c}";
        $.ajax({
            data: {
                action: "schedule",
                name: name,
                path: path,
                appname: appname,
                currentUser: currentUser,
                isAdmin:isAdmin
            },
            type: "POST",
            url: "${rc.contextPath}/schedule.do",
            error: function () {
                $("#schedule_content").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
                $("#schedule_content").addClass("align-center");
            },
            success: function (response, textStatus) {

                if(isAdmin == "false"){
                    var jsonarray = $.parseJSON(response);
                    scheduleBody += " <table cellpadding='0' cellspacing='0' border='0' class='table table-striped table-bordered table-hover' width='100%' id='example'>"
                            + "<thead>"
                            + "<tr>"
                            + "<th class='hide'>ID</th>"
                            + "<th width='15%'>名称</th>"
                            + "<th>IP</th>"
                            + "<th>调度人</th>"
                            + "<th>调度身份</th>"
                            + "<th class='hide'>组</th>"
                            + "<th>创建时间</th>"
                            + "<th>Crontab</th>"
                            + "<th>状态</th>"
                            + "<th>最后执行结果</th>"
                            + "<th class='center'>-</th>"
                            + "<th class='center'>-</th>"
                            + "</tr>"
                            + " </thead>"
                            + "<tbody>";


                    $.each(jsonarray, function (i, item) {
                        var state = item.state;
                        var taskState = item.lastTaskStatus;
                        var taskStatsLabel ="";
                        if(taskState == "SUCCEEDED" ||taskState == "RUNNING"){
                            taskStatsLabel = "<span class='label label-info'>"
                                    + item.lastTaskStatus
                                    + "</span>";
                        }else{
                            taskStatsLabel = "<span class='label label-important'>"
                                    + item.lastTaskStatus
                                    + "</span>";
                        }
                        var isRunning = true;
                        if (state == "SUSPEND") {
                            isRunning = false;
                        }

                        if (isRunning) {
                            scheduleBody += "<tr id='" + item.taskId + "'>"
                                    + "<td class='hide'>" + item.taskId + "</td>"
                                    + "<td class='fixLength-td'>"
                                    + item.taskName
                                    + "</td>"
                                    + "<td>"
                                    + item.hostName
                                    + "</td>"
                                    + "<td>"
                                    + item.creator
                                    + "</td>"
                                    + "<td>"
                                    + item.proxyUser
                                    + "</td>"
                                    + "<td class='hide'>arch(mock)</td>"
                                    + "<td>"
                                    + item.addTime
                                    + "</td>"
                                    + "<td>"
                                    + item.crontab
                                    + "</td>"
                                    + "<td>"
                                    + "<span class='label label-info'>"
                                    + item.state
                                    + "</span>"
                                    + "</td>"
                                    +"<td>"
                                    + taskStatsLabel
                                    +"</td>"
                                    + "<td>"
                                    + "<div class='btn-group'>"
                                    + "<button class='btn btn-success dropdown-toggle' data-toggle='dropdown'>"
                                    + "Action"
                                    + "<span class='icon-angle-down'></span></button>"
                                    + "<ul class='dropdown-menu'>"
                                    + "<li><a href='#confirm' onClick=\"action($(this).parents('tr').find('td')[0].textContent,1)\">删除</a></li>"
                                    + "<li><a href='#confirm' onClick=\"action($(this).parents('tr').find('td')[0].textContent,2)\">暂停</a></li>"
                                    + "<li><a href='#confirm' onClick=\"action($(this).parents('tr').find('td')[0].textContent,3)\">执行</a></li>"
                                    + "<li><a class='detailBtn' href='${rc.contextPath}/task_form?task_id="
                                    + item.taskId
                                    + "' >详细</a></li></ul></div>"
                                    +"</td>"
                                    + "<td><a id='attempts' class='btn btn-primary btn-small' href='${rc.contextPath}/attempt?taskID=" + item.taskId + "' target= 'blank'>运行历史</a></td></tr>";
                        } else {
                            scheduleBody += "<tr id='" + item.taskId + "' class='error'>"
                                    +"<td class='hide'>" + item.taskId + "</td>"
                                    + "<td class='fixLength-td'>"
                                    + item.taskName
                                    + "</td>"
                                    + "<td>"
                                    + item.hostName
                                    + "</td>"
                                    + "<td>"
                                    + item.creator
                                    + "</td>"
                                    + "<td>"
                                    + item.proxyUser
                                    + "</td>"
                                    + "<td class='hide'>arch(mock)</td>"
                                    + "<td>"
                                    + item.addTime
                                    + "</td>"
                                    + "<td>"
                                    + item.crontab
                                    + "</td>"
                                    +"<td>"
                                    +"<span class='label label-important'>"
                                    + item.state
                                    + "</span>"
                                    +"</td>"
                                    +"<td>"
                                    + taskStatsLabel
                                    +"</td>"
                                    +"<td>"
                                    + "<div class='btn-group'>"
                                    + "<button class='btn btn-success dropdown-toggle' data-toggle='dropdown'>"
                                    + "Action"
                                    + "<span class='icon-angle-down'></span></button>"
                                    + "<ul class='dropdown-menu'>"
                                    + "<li><a href='#confirm' onClick=\"action($(this).parents('tr').find('td')[0].textContent,1)\">删除</a></li>"
                                    +"<li><a href='#confirm' onClick=\"action($(this).parents('tr').find('td')[0].textContent,2)\">恢复</a></li>"
                                    +"<li><a href='#confirm' onClick=\"action($(this).parents('tr').find('td')[0].textContent,3)\">执行</a></li>"
                                    + "<li><a class='detailBtn' href='${rc.contextPath}/task_form?task_id="
                                    + item.taskId
                                    + "' >详细</a></li></ul></div>"
                                    +"</td>"
                                    + "<td><a id='attempts' class='btn btn-primary btn-small' href='${rc.contextPath}/attempt?taskID=" + item.taskId + "' target= 'blank'>运行历史</a></td></tr>"

                        }





                    });
                }else{
                    var jsonarray = $.parseJSON(response);
                    scheduleBody += " <table cellpadding='0' cellspacing='0' border='0' class='table table-striped table-bordered table-hover' width='100%' id='example'>"
                            + "<thead>"
                            + "<tr>"
                            + "<th class='hide'>ID</th>"
                            + "<th width='15%'>名称</th>"
                            + "<th>IP</th>"
                            + "<th>调度人</th>"
                            + "<th>调度身份</th>"
                            + "<th class='hide'>组</th>"
                            + "<th>创建时间</th>"
                            + "<th>Crontab</th>"
                            + "<th>状态</th>"
                            + "<th class='center'>-</th>"
                            + "<th class='center'>-</th>"
                            + "</tr>"
                            + " </thead>"
                            + "<tbody>";


                    $.each(jsonarray, function (i, item) {
                        var state = item.state;
                        var isRunning = true;
                        if (state == "SUSPEND") {
                            isRunning = false;
                        }

                        if (isRunning) {
                            scheduleBody += "<tr id='" + item.taskId + "'>"
                                    + "<td class='hide'>" + item.taskId + "</td>"
                                    + "<td class='fixLength-td'>"
                                    + item.taskName
                                    + "</td>"
                                    + "<td>"
                                    + item.hostName
                                    + "</td>"
                                    + "<td>"
                                    + item.creator
                                    + "</td>"
                                    + "<td>"
                                    + item.proxyUser
                                    + "</td>"
                                    + "<td class='hide'>arch(mock)</td>"
                                    + "<td>"
                                    + item.addTime
                                    + "</td>"
                                    + "<td>"
                                    + item.crontab
                                    + "</td>"
                                    + "<td>"
                                    + "<span class='label label-info'>"
                                    + item.state
                                    + "</span>"
                                    + "</td>"
                                    + "<td>"
                                    + "<div class='btn-group'>"
                                    + "<button class='btn btn-success dropdown-toggle' data-toggle='dropdown'>"
                                    + "Action"
                                    + "<span class='icon-angle-down'></span></button>"
                                    + "<ul class='dropdown-menu'>"
                                    + "<li><a href='#confirm' onClick=\"action($(this).parents('tr').find('td')[0].textContent,1)\">删除</a></li>"
                                    + "<li><a href='#confirm' onClick=\"action($(this).parents('tr').find('td')[0].textContent,2)\">暂停</a></li>"
                                    + "<li><a href='#confirm' onClick=\"action($(this).parents('tr').find('td')[0].textContent,3)\">执行</a></li>"
                                    + "<li><a class='detailBtn' href='${rc.contextPath}/task_form?task_id="
                                    + item.taskId
                                    + "' >详细</a></li></ul></div>"
                                    +"</td>"
                                    + "<td><a id='attempts' class='btn btn-primary btn-small' href='${rc.contextPath}/attempt?taskID=" + item.taskId + "' target= 'blank'>运行历史</a></td></tr>";
                        } else {
                            scheduleBody += "<tr id='" + item.taskId + "' class='error'>"
                                    +"<td class='hide'>" + item.taskId + "</td>"
                                    + "<td class='fixLength-td'>"
                                    + item.taskName
                                    + "</td>"
                                    + "<td>"
                                    + item.hostName
                                    + "</td>"
                                    + "<td>"
                                    + item.creator
                                    + "</td>"
                                    + "<td>"
                                    + item.proxyUser
                                    + "</td>"
                                    + "<td class='hide'>arch(mock)</td>"
                                    + "<td>"
                                    + item.addTime
                                    + "</td>"
                                    + "<td>"
                                    + item.crontab
                                    + "</td>"
                                    +"<td>"
                                    +"<span class='label label-important'>"
                                    + item.state
                                    + "</span>"
                                    +"</td>"
                                    +"<td>"
                                    + "<div class='btn-group'>"
                                    + "<button class='btn btn-success dropdown-toggle' data-toggle='dropdown'>"
                                    + "Action"
                                    + "<span class='icon-angle-down'></span></button>"
                                    + "<ul class='dropdown-menu'>"
                                    + "<li><a href='#confirm' onClick=\"action($(this).parents('tr').find('td')[0].textContent,1)\">删除</a></li>"
                                    +"<li><a href='#confirm' onClick=\"action($(this).parents('tr').find('td')[0].textContent,2)\">恢复</a></li>"
                                    +"<li><a href='#confirm' onClick=\"action($(this).parents('tr').find('td')[0].textContent,3)\">执行</a></li>"
                                    + "<li><a class='detailBtn' href='${rc.contextPath}/task_form?task_id="
                                    + item.taskId
                                    + "' >详细</a></li></ul></div>"
                                    +"</td>"
                                    + "<td><a id='attempts' class='btn btn-primary btn-small' href='${rc.contextPath}/attempt?taskID=" + item.taskId + "' target= 'blank'>运行历史</a></td></tr>"

                        }

                    });
                }

                scheduleBody +="</tbody> </table>";

                $("#schedule_load").html("");
                $("#schedule_content").html(scheduleBody);
                $("#schedule_content").removeClass("align-center");
                $('#example').dataTable({
                    "bAutoWidth": true,
                    "bPaginate": true,
                    "bFilter": true,
                    "bInfo": true,
                    "bLengthChange": true
                });
            }

        });

    })
</script>
<script type="text/javascript" charset="utf-8" src="${rc.contextPath}/static/js/schedule.js"></script>
<script src="${rc.contextPath}/lib/ace/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="${rc.contextPath}/static/js/taurus_validate.js" type="text/javascript"></script>
</body>
</html>