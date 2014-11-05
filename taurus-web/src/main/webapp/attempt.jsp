<!DOCTYPE html>
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
    <script type="text/javascript" src="resource/js/lib/Chart.js"></script>
    <!-- page specific plugin styles -->
    <script src="lib/ace/js/jquery.dataTables.min.js"></script>
    <script src="lib/ace/js/jquery.dataTables.bootstrap.js"></script>
    <script type="text/javascript" src="js/login.js"></script>
    <!-- fonts -->
    <script src="lib/ace/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/ace-fonts.css"/>

    <!-- ace styles -->

    <link rel="stylesheet" href="lib/ace/css/ace.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-skins.min.css"/>
</head>
<body data-spy="scroll">
<%@page import="org.restlet.data.MediaType,
                org.restlet.resource.ClientResource,
                com.dp.bigdata.taurus.restlet.resource.IAttemptsResource,
                com.dp.bigdata.taurus.restlet.shared.AttemptDTO,
                java.text.SimpleDateFormat" %>
<%@ page import="com.dp.bigdata.taurus.web.servlet.AttemptProxyServlet" %>
<%@ page import="com.dp.bigdata.taurus.restlet.resource.IGetTasks" %>
<%@ page import="com.dp.bigdata.taurus.generated.module.Task" %>
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
        <!--    <div class="pull-right" style="margin:10px;color: white;">本周值班: {{duty}} {{tel}}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>-->
        <!-- /.navbar-header -->
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
    if(!isAdmin){
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
            <li>
                <a href="schedule.jsp">调度中心</a>
            </li>
            <li class="active">
                <a href="attempt.jsp">调度历史</a>
            </li>
        </ul>
    </div>

    <div class="page-content">
        <div id="alertContainer" class="container col-sm-12">
        </div>
        <div class="row">
            <div class="col-sm-12">
                <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-hover"
                       width="100%" id="example">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>任务名</th>
                        <th>实际启动时间</th>
                        <th>实际结束时间</th>
                        <th>预计调度时间</th>
                        <th>IP</th>
                        <th>返回值</th>
                        <th>状态</th>
                        <th>-</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        ClientResource crTask = new ClientResource(host + "gettasks");
                        IGetTasks taskResource = crTask.wrap(IGetTasks.class);
                        ArrayList<Task> tasks = taskResource.retrieve();

                        String taskID = request.getParameter("taskID");
                        String url = host + "attempt?task_id=" + taskID;
                        cr = new ClientResource(url);
                        cr.setRequestEntityBuffering(true);
                        IAttemptsResource resource = cr.wrap(IAttemptsResource.class);
                        cr.accept(MediaType.APPLICATION_XML);
                        ArrayList<AttemptDTO> attempts = resource.retrieve();

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        for (AttemptDTO dto : attempts) {
                            String state = dto.getStatus();
                            String taskName = "";
                            for (Task task : tasks) {
                                if (task.getTaskid().equals(dto.getTaskID())) {
                                    taskName = task.getName();
                                    break;
                                }
                            }
                    %>
                    <tr id="<%=dto.getAttemptID()%>">
                        <td><%=dto.getId()%>
                        </td>
                        <%if (taskName != null) {%>
                        <td><%=taskName%>
                        </td>
                        <%} else {%>
                        <td>NULL</td>
                        <%}%>
                        <%if (dto.getStartTime() != null) {%>
                        <td><%=formatter.format(dto.getStartTime())%>
                        </td>
                        <%} else {%>
                        <td>NULL</td>
                        <%}%>
                        <%if (dto.getEndTime() != null) {%>
                        <td><%=formatter.format(dto.getEndTime())%>
                        </td>
                        <%} else {%>
                        <td>NULL</td>
                        <%}%>
                        <%if (dto.getScheduleTime() != null) {%>
                        <td><%=formatter.format(dto.getScheduleTime())%>
                        </td>
                        <%} else {%>
                        <td>NULL</td>
                        <%}%>
                        <%if (dto.getExecHost() != null) {%>
                        <td><%=dto.getExecHost()%>
                        </td>
                        <%} else {%>
                        <td>NULL</td>
                        <%}%>
                        <td><%=dto.getReturnValue()%>
                        </td>
                        <td><%if (state.equals("RUNNING")) {%>
                            <span class="label label-info"><%=state%></span>
                            <%} else if (state.equals("SUCCEEDED")) {%>
                            <span class="label label-success"><%=state%></span>
                            <%} else {%>
                            <span class="label label-important"><%=state%></span>
                            <%}%>
                        </td>

                        <td>
                            <%
                                if (state.equals("RUNNING") || state.equals("TIMEOUT")) {%>

                            <a href="#confirm" onClick="action($(this).parents('tr').attr('id'))">Kill</a>
                            <%  boolean isViewLog = AttemptProxyServlet.isHostOverLoad(dto.getExecHost());
                                if(!isViewLog){%>
                            <a target="_blank"
                               href="viewlog.jsp?id=<%=dto.getAttemptID()%>&status=<%=dto.getStatus()%>">日志</a>

                            <%
                                }
                            } else {%>
                            <a target="_blank"
                               href="viewlog.jsp?id=<%=dto.getAttemptID()%>&status=<%=dto.getStatus()%>">日志</a>
                            <%}%>
                        </td>
                    </tr>
                    <% }%>
                    </tbody>
                </table>
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
</div>

<script type="text/javascript">
    jQuery(function ($) {
        var oTable1 =
                $('#example').dataTable({
                    bAutoWidth: true,
                    "bPaginate": true
                });

        $('li[id="schedule"]').addClass("active");
        $('#menu-toggler').on(ace.click_event, function() {
            $('#sidebar').toggleClass('display');
            $(this).toggleClass('display');
            return false;
        });
    })
</script>

<script type="text/javascript" charset="utf-8" language="javascript" src="js/attempt.js"></script>

</body>
</html>