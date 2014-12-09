<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="en">
<head>
    <%@ include file="jsp/common-nav.jsp" %>
    <title>Taurus</title>
    <meta charset="utf-8">
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <!-- basic styles -->
    <script type="text/javascript" src="resource/js/lib/jquery-1.9.1.min.js"></script>
    <link href="lib/ace/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="lib/ace/js/ace-extra.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/font-awesome.min.css"/>
    <script src="lib/ace/js/ace-elements.min.js"></script>
    <script src="lib/ace/js/ace.min.js"></script>
    <script src="lib/ace/js/bootbox.min.js"></script>
    <script type="text/javascript" src="resource/js/lib/raphael.2.1.0.min.js"></script>
    <script type="text/javascript" src="resource/js/lib/justgage.1.0.1.min.js"></script>
    <!-- page specific plugin scripts -->
    <script src="lib/ace/js/jquery.dataTables.min.js"></script>
    <script src="lib/ace/js/jquery.dataTables.bootstrap.js"></script>
    <script type="text/javascript" src="js/login.js"></script>
    <!-- page specific plugin styles -->

    <!-- fonts -->
    <script src="lib/ace/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/ace-fonts.css"/>

    <!-- ace styles -->

    <link rel="stylesheet" href="lib/ace/css/ace.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-skins.min.css"/>
</head>
<body>


<div class="navbar navbar-default" id="navbar">
    <script type="text/javascript">
        try {
            ace.settings.check('navbar', 'fixed')
        } catch (e) {
        }
    </script>

    <div class="navbar-container" id="navbar-container" style="height: 30px">
        <div class="navbar-header pull-left">

            <a href="index.jsp" class="navbar-brand">
                <i class="icon-tasks"></i>
                Taurus
            </a>
            <!-- /.brand -->
        </div>
        <!-- /.navbar-header -->
        <div class="navbar-header">
            <span style="margin:10px;font-size: 16px" class="label label-transparent">任务调度系统</span>
        </div>

        <!-- /.navbar-header -->
        <button type="button" class="navbar-toggle pull-left" id="menu-toggler">
            <span class="sr-only">Toggle sidebar</span>

            <span class="icon-bar"></span>

            <span class="icon-bar"></span>

            <span class="icon-bar"></span>
        </button>
        <div class="navbar-header pull-right" role="navigation">
            <ul class="nav ace-nav">
                <li class="light-blue">
                    <a data-toggle="dropdown" href="#" target="_self" class="dropdown-toggle">
                        <img class="nav-user-photo" src="lib/ace/avatars/user.jpg" alt="Jason's Photo"/>
            <span class="user-info">
                                    <small>欢迎,</small>
                                    <div id="username"><%=currentUser%>
                                    </div>
                                </span>

                        <i class="icon-caret-down"></i>
                    </a>

                    <ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                        <li>
                            <a href="user.jsp">
                                <i class="icon-cogs"></i>
                                设置
                            </a>
                        </li>
                        <li>
                            <a href="javascript:logout('<%=currentUser%>')">
                                <i class="icon-off"></i>
                                退出
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
            <!-- /.ace-nav -->
        </div>
        <div class="pull-right" style="margin:10px;color: white;"><i class="icon-group"> Taurus后援QQ群：155326270 </i></div>
        <div class="pull-right ng-binding" style="margin:10px;color: white;" ng-bind="monitorMessage"><i class="icon-user-md">开发者：李明 </i> <i class="icon-phone">: 13661871541</i></div>

    </div>
    <!-- /.container -->
</div>
<div class="sidebar " id="sidebar">
    <script type="text/javascript">
        try {
            ace.settings.check('sidebar', 'fixed')
        } catch (e) {
        }
    </script>

    <ul class="nav nav-list">

        <li id="index">
            <a href="index.jsp">
                <i class="icon-dashboard"></i>
                <span class="menu-text" id="userrolechange">监控中心</span>
            </a>
        </li>

        <li id="task">
            <a href="task.jsp" target="_self">
                <i class="icon-edit"></i>
                <span class="menu-text">新建任务 </span>
            </a>
        </li>
        <li id="schedule">
            <a href="schedule.jsp" target="_self">
                <i class="icon-tasks"></i>
                <span class="menu-text"> 调度中心 </span>
            </a>
        </li>
        <li id="monitor">
            <a href="monitor.jsp" target="_self">
                <i class="icon-trello"></i>
                <span class="menu-text"> 任务监控 </span>
            </a>
        </li>
        <li id="host">
            <a href="hosts.jsp" target="_self">
                <i class="icon-desktop"></i>
                <span class="menu-text"> 主机监控 </span>
            </a>
        </li>
        <li id="cron">
            <a href="cronbuilder.jsp" target="_self">
                <i class="icon-indent-right"></i>
                <span class="menu-text"> Cron 生成器</span>
            </a>
        </li>
        <li id="user">
            <a href="user.jsp" target="_self">
                <i class="icon-user"></i>
                <span class="menu-text"> 用户设置 </span>
            </a>
        </li>
        <li id="resign">
            <a href="resign.jsp" target="_self">
                <i class="icon-retweet"></i>
                <span class="menu-text"> 任务交接 </span>
            </a>
        </li>
        <li id="feedback">
            <a href="feedback.jsp" target="_self">
                <i class="icon-comments"></i>
                <span class="menu-text"> 我要反馈 </span>
            </a>
        </li>
        <li id="update">
            <a href="update.jsp" target="_self">
                <i class="icon-tag"></i>
                <span class="menu-text"> 更新日志 </span>
            </a>
        </li>
        <li id="about">
            <a href="about.jsp" target="_self">
                <i class="icon-question"></i>
                <span class="menu-text"> 使用帮助 </span>
            </a>
        </li>


    </ul>
    <!-- /.nav-list -->

    <div class="sidebar-collapse" id="sidebar-collapse">
        <i class="icon-double-angle-left" data-icon1="icon-double-angle-left"
           data-icon2="icon-double-angle-right"></i>
    </div>
    <script type="text/javascript">
        try {
            ace.settings.check('sidebar', 'collapsed')
        } catch (e) {
        }
    </script>

</div>

<script>
    var isAdmin = <%=isAdmin%>;
    if (!isAdmin) {
        $("#userrolechange").html("我的任务");
    }


</script>


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
                <a href="index.jsp">HOME</a>
            </li>
            <li class="active">
                <a href="schedule.jsp">调度中心</a>
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
                <%
                    String path = request.getParameter("path");
                    if (path != null && !path.equals("")) {
                %>
                <span style="color:red">提示:已部署的作业文件的路径为<%=path%></span>
                <% }%>

                <div id="schedule_content" class="align-center">
                    <i class="icon-spinner icon-spin icon-large"></i>
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


<script type="text/javascript">
    jQuery(function ($) {

        $('li[id="schedule"]').addClass("active");
        $('#menu-toggler').on(ace.click_event, function () {
            $('#sidebar').toggleClass('display');
            $(this).toggleClass('display');
            return false;
        });

        var name = "<%=request.getParameter("name")%>";
        var path = "<%=request.getParameter("path")%>";
        var appname ="<%=request.getParameter("appname")%>";
        var currentUser = "<%= currentUser%>";
        var scheduleBody = "";
        $.ajax({
            data: {
                action: "schedule",
                name: name,
                path: path,
                appname: appname,
                currentUser: currentUser
            },
            type: "POST",
            url: "/monitor",
            error: function () {
                $("#schedule_content").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
                $("#schedule_content").addClass("align-center");
            },
            success: function (response, textStatus) {


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
                                + "<li><a class='detailBtn' href='task_form.jsp?task_id="
                                + item.taskId
                                + "'>详细</a></li></ul></div>"
                        +"</td>"
                        + "<td><a id='attempts' class='btn btn-primary btn-small' href='attempt.jsp?taskID=" + item.taskId + "'>运行历史</a></td></tr>";
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
                                + "<li><a class='detailBtn' href='task_form.jsp?task_id="
                                + item.taskId
                                + "'>详细</a></li></ul></div>"
                        +"</td>"
                        + "<td><a id='attempts' class='btn btn-primary btn-small' href='attempt.jsp?taskID=" + item.taskId + "'>运行历史</a></td></tr>"

                    }





                });
                scheduleBody +="</tbody> </table>";
                console.log(scheduleBody);
                $("#schedule_load").html("");
                $("#schedule_content").html(scheduleBody);
                $("#schedule_content").removeClass("align-center");
                $('#example').dataTable({
                    bAutoWidth: true,
                    "bPaginate": true,
                    "aoColumns": [
                        { "bSortable": false },
                        null,
                        null,
                        null,
                        null,
                        null,
                        { "bSortable": false },
                        null,
                        null,
                        null,
                        null
                    ]


                });
            }


        });




    })
</script>
<script type="text/javascript" charset="utf-8" src="js/schedule.js"></script>
<script src="js/jquery.validate.min.js" type="text/javascript"></script>
<script src="js/taurus_validate.js" type="text/javascript"></script>
</body>
</html>