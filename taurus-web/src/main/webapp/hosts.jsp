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
    <script type="text/javascript" src="resource/js/lib/Chart.js"></script>
    <script type="text/javascript" src="js/login.js"></script>

    <!-- page specific plugin styles -->

    <!-- fonts -->
    <script src="lib/ace/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/ace-fonts.css"/>

    <!-- ace styles -->

    <link rel="stylesheet" href="lib/ace/css/ace.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-skins.min.css"/>
    <style>

        .spann {

            width: 95%;
        }

    </style>
    <link rel="stylesheet" type="text/css" href="css/DT_bootstrap.css">
    <link href="css/viewlog.css" rel="stylesheet" type="text/css">
</head>
<body data-spy="scroll">

<%@page import="org.restlet.resource.ClientResource" %>
<%@page import="com.dp.bigdata.taurus.restlet.shared.HostDTO" %>
<%@page import="org.restlet.data.MediaType" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page import="com.dp.bigdata.taurus.restlet.resource.*" %>
<%@ page import="com.dp.bigdata.taurus.generated.module.Task" %>
<%@ page import="java.util.*" %>
<%@ page import="com.dp.bigdata.taurus.restlet.shared.AttemptDTO" %>
<%
    cr = new ClientResource(host + "host");
    IHostsResource hostsResource = cr.wrap(IHostsResource.class);
    cr.accept(MediaType.APPLICATION_XML);
    ArrayList<HostDTO> hosts = hostsResource.retrieve();
%>
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

<div class="mid-div col-sm-12">
<div class="page-content col-sm-12">
<div class="sidebar col-sm-2 no-padding-left">
    <%@include file="hostList.jsp" %>
</div>
<div class="main-content">
<div class="page-content col-sm-12">
<%
    String statusCode = (String) (request.getAttribute("statusCode"));
    String hostName = request.getParameter("hostName");
    String op = request.getParameter("op");
    cr = new ClientResource(host + "host/" + hostName);
    IHostResource hostResource = cr.wrap(IHostResource.class);
    cr.accept(MediaType.APPLICATION_XML);
    HostDTO dto = hostResource.retrieve();
    Map<String, String> maps = new HashMap<String, String>();
    maps.put("up", "上线");
    maps.put("dowan", "下线");
    maps.put("restart", "重启");
    maps.put("update", "升级");
    String opChs = maps.get(op);
    if (opChs == null) {
        opChs = "操作";
    }
    if ("200".equals(statusCode)) {
%>
<div id="alertContainer" class="container">
    <div id="alertContainerSuccess" class="alert alert-success">
        <button type="button" class="close" data-dismiss="alert">×</button>
        <%=opChs %>成功
    </div>
</div>
<%
} else if ("500".equals(statusCode)) {
%>
<div id="alertContainer" class="container">
    <div id="alertContainerError" class="alert alert-danger">
        <button type="button" class="close" data-dismiss="alert">×</button>
        <%=opChs %>失败
    </div>
</div>
<% }
    if (dto != null) {
%>
<ul class="nav nav-tabs">

    <li class="active"><a href="#state" data-toggle="tab">运行状态</a></li>
    <li class=""><a href="#taskmonitor" data-toggle="tab">任务监控</a></li>

    <li class=""><a href="#log" data-toggle="tab">日志</a></li>
    <li class=""><a href="#statistics" data-toggle="tab">统计</a></li>

</ul>

<div class="tab-content col-sm-12">
<div class="tab-pane active in" id="state">
    <table class="table table-striped table-bordered table-hover " id="host_state">
        <thead>
        <tr>
            <th>#</th>
            <th>属性</th>
            <th>值</th>
            <% if (isAdmin){%>
            <th>操作</th>
            <%}%>
        </tr>
        </thead>
        <tbody>
        <tr class="success">
            <td>1</td>
            <td>机器IP</td>
            <td><%=hostName%>
            </td>
            <% if (isAdmin){%>
            <td></td>
            <%}%>
        </tr>
        <tr class="error">
            <td>2</td>
            <td>机器状态</td>
            <%if (dto.isOnline()) {%>
            <td>在线</td>
            <% if (isAdmin){%>

            <td><a id="down" title="这台agent将不在监控范围内，agent进程是否被kill并不能确定。" class="btn  btn-primary btn-minier"
                   href="updateHost?hostName=<%=hostName%>&op=down">下线</a></td>
            <%}%>
            <%} else {%>
            <td>下线</td>
            <% if (isAdmin){%>
            <td><a id="up" title="这台agent将被纳入监控范围内，agent需要手动启动。" class="btn btn-primary btn-minier"
                   href="updateHost?hostName=<%=hostName%>&op=up">上线</a></td>
            <%}%>
            <%} %>
        </tr>
        <tr class="warning">
            <td>3</td>
            <td>心跳状态</td>
            <%if (dto.isConnected()) {%>
            <td>正常</td>
            <% if (isAdmin){%>
            <td><a id="restart" class="btn  btn-primary btn-minier"
                   href="updateHost?hostName=<%=hostName%>&op=restart">重启</a></td>
            <%}%>
            <%} else {%>
            <td>异常</td>
            <% if (isAdmin){%>
            <td>无法重启</td>

            <%} }%>
        </tr>
        <tr class="info">
            <td>4</td>
            <td>版本</td>
            <td><% if (dto.getInfo() == null) {%>
                异常，无法获得版本号 <%
                } else {
                %><%=dto.getInfo().getAgentVersion() %><%}%></td>
            <%if (dto.isConnected()) {%>
            <% if (isAdmin){%>
            <td><a id="update" class="btn btn-primary btn-minier"
                   href="http://code.dianpingoa.com/arch/taurus/rollout_branches">升级</a></td>
            <%} %>
            <%} else {%>
            <% if (isAdmin){%>
            <td><a id="update" class="btn btn-primary btn-minier"
                   href="http://code.dianpingoa.com/arch/taurus/rollout_branches">升级</a></td>
            <%}} %>
        </tr>
        </tbody>
    </table>
</div>
<div class="tab-pane" id="taskmonitor">

<ul class="run-tag col-sm-12">
    <li><a>正在运行的任务<span class="label label-info">RUNNING</span></a></li>
</ul>
<ul class="breadcrumb col-sm-12">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover" id="running">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
            <!-- <th>IP</th> -->
            <th>查看日志</th>

        </tr>
        </thead>
        <tbody>
        <%

            ClientResource crTask = new ClientResource(host + "gettasks");
            com.dp.bigdata.taurus.restlet.resource.IGetTasks taskResource = crTask.wrap(IGetTasks.class);
            ArrayList<Task> tasks = taskResource.retrieve();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String url = host + "getattemptsbystatus/";

            String now = formatter.format(new Date());
            long hourTime = 60 * 60 * 1000;
            String taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));
            cr = new ClientResource(url + 6);
            IGetAttemptsByStatus resource = cr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> attempts = resource.retrieve();

            if (attempts != null)
                for (AttemptDTO hostDto : attempts) {
                    Date startDate = hostDto.getStartTime();
                    String startTime;
                    if (startDate == null) {
                        startTime = null;
                    } else {
                        startTime = formatter.format(startDate);
                    }

                    Date endDate = hostDto.getEndTime();
                    String endTime;
                    if (endDate == null) {
                        endTime = null;
                    } else {
                        endTime = formatter.format(endDate);
                    }


                    String state = hostDto.getStatus();
                    String exeHost = hostDto.getExecHost();
                    if (hostName != null && hostName.equals(exeHost)) {
                        if (tasks != null && startTime != null && state.equals("RUNNING") && (startTime.compareTo(now) <= 0 && (endTime == null || endTime.compareTo(now) >= 0))) {
                            String taskName = "";

                            for (Task task : tasks) {
                                if (task.getTaskid().equals(hostDto.getTaskID())) {
                                    taskName = task.getName();
                                    break;
                                }
                            }
        %>
        <tr id="<%=hostDto.getAttemptID()%>">
            <td><%=hostDto.getTaskID()%>
            </td>
            <td><%=taskName%>
            </td>
            <%if (hostDto.getStartTime() != null) {%>
            <td><%=formatter.format(hostDto.getStartTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getEndTime() != null) {%>
            <td><%=formatter.format(hostDto.getEndTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getScheduleTime() != null) {%>
            <td><%=formatter.format(hostDto.getScheduleTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <td>

                <a target="_blank"
                   href="viewlog.jsp?id=<%=hostDto.getAttemptID()%>&status=<%=hostDto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        }
        }%>
        </tbody>
    </table>
</ul>

<ul class="submit-fail-tag col-sm-12">
    <li><a>提交失败的任务 <span class="label label-important">SUBMIT_FAIL</span></a></li>
</ul>

<ul class="breadcrumb col-sm-12">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover " id="submitfail">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
            <!-- <th>IP</th> -->
            <th>查看日志</th>
        </tr>
        </thead>
        <tbody>
        <%
            ClientResource submitFailCr = new ClientResource(url + 5);
            IGetAttemptsByStatus submitFailResource = submitFailCr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> submitFailAttempts = submitFailResource.retrieve();

            if (submitFailAttempts != null)
                for (AttemptDTO hostDto : submitFailAttempts) {
                    String exeHost = hostDto.getExecHost();
                    if (hostName != null && hostName.equals(exeHost)) {
                        String state = hostDto.getStatus();

                        if (taskTime != null) {
                            Date startDate = hostDto.getStartTime();
                            String startTime;
                            if (startDate == null) {
                                startTime = null;
                            } else {
                                startTime = formatter.format(startDate);
                            }

                            Date endDate = hostDto.getEndTime();
                            String endTime;
                            if (endDate == null) {
                                endTime = null;
                            } else {
                                endTime = formatter.format(endDate);
                            }

                            if (startTime != null && state.equals("SUBMIT_FAIL") && (startTime.compareTo(taskTime) >= 0 || endTime.compareTo(taskTime) >= 0)) {
                                String taskName = "";
                                for (Task task : tasks) {
                                    if (task.getTaskid().equals(hostDto.getTaskID())) {
                                        taskName = task.getName();
                                        break;
                                    }
                                }

        %>
        <tr id="<%=hostDto.getAttemptID()%>">
            <td><%=hostDto.getTaskID()%>
            </td>
            <td><%=taskName%>
            </td>
            <%if (hostDto.getStartTime() != null) {%>
            <td><%=formatter.format(hostDto.getStartTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getEndTime() != null) {%>
            <td><%=formatter.format(hostDto.getEndTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getScheduleTime() != null) {%>
            <td><%=formatter.format(hostDto.getScheduleTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>

            <td>
                <a target="_blank"
                   href="viewlog.jsp?id=<%=hostDto.getAttemptID()%>&status=<%=hostDto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        } else {
            if (state.equals("SUBMIT_FAIL")) {
                String taskName = "";
                for (Task task : tasks) {
                    if (task.getTaskid().equals(hostDto.getTaskID())) {
                        taskName = task.getName();
                        break;
                    }
                }

        %>
        <tr id="<%=hostDto.getAttemptID()%>">
            <td><%=hostDto.getTaskID()%>
            </td>
            <td><%=taskName%>
            </td>
            <%if (hostDto.getStartTime() != null) {%>
            <td><%=formatter.format(hostDto.getStartTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getEndTime() != null) {%>
            <td><%=formatter.format(hostDto.getEndTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getScheduleTime() != null) {%>
            <td><%=formatter.format(hostDto.getScheduleTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <td>
                <a target="_blank"
                   href="viewlog.jsp?id=<%=hostDto.getAttemptID()%>&status=<%=hostDto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        }

        }
        }%>
        </tbody>
    </table>
</ul>

<ul class="fail-tag col-sm-12">
    <li><a>失败的任务 <span class="label label-important">FAILED</span></a></li>

</ul>

<ul class="breadcrumb col-sm-12">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover " id="fail">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
            <!-- <th>IP</th> -->
            <th>查看日志</th>
        </tr>
        </thead>
        <tbody>
        <%
            ClientResource failCr = new ClientResource(url + 8);
            IGetAttemptsByStatus failResource = failCr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> failAttempts = failResource.retrieve();

            if (failAttempts != null)
                for (AttemptDTO hostDto : failAttempts) {
                    String exeHost = hostDto.getExecHost();
                    if (hostName != null && hostName.equals(exeHost)) {
                        String state = hostDto.getStatus();


                        if (taskTime != null) {
                            Date startDate = hostDto.getStartTime();
                            String startTime;
                            if (startDate == null) {
                                startTime = null;
                            } else {
                                startTime = formatter.format(startDate);
                            }

                            Date endDate = hostDto.getEndTime();
                            String endTime;
                            if (endDate == null) {
                                endTime = null;
                            } else {
                                endTime = formatter.format(endDate);
                            }

                            if (startTime != null && state.equals("FAILED") && (startTime.compareTo(taskTime) >= 0 || endTime.compareTo(taskTime) >= 0)) {
                                String taskName = "";
                                for (Task task : tasks) {
                                    if (task.getTaskid().equals(hostDto.getTaskID())) {
                                        taskName = task.getName();
                                        break;
                                    }
                                }

        %>
        <tr id="<%=hostDto.getAttemptID()%>">
            <td><%=hostDto.getTaskID()%>
            </td>
            <td><%=taskName%>
            </td>
            <%if (hostDto.getStartTime() != null) {%>
            <td><%=formatter.format(hostDto.getStartTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getEndTime() != null) {%>
            <td><%=formatter.format(hostDto.getEndTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getScheduleTime() != null) {%>
            <td><%=formatter.format(hostDto.getScheduleTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>

            <td>
                <a target="_blank"
                   href="viewlog.jsp?id=<%=hostDto.getAttemptID()%>&status=<%=hostDto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        } else {
            if (state.equals("FAILED")) {
                String taskName = "";
                for (Task task : tasks) {
                    if (task.getTaskid().equals(hostDto.getTaskID())) {
                        taskName = task.getName();
                        break;
                    }
                }

        %>
        <tr id="<%=hostDto.getAttemptID()%>">
            <td><%=hostDto.getTaskID()%>
            </td>
            <td><%=taskName%>
            </td>
            <%if (hostDto.getStartTime() != null) {%>
            <td><%=formatter.format(hostDto.getStartTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getEndTime() != null) {%>
            <td><%=formatter.format(hostDto.getEndTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getScheduleTime() != null) {%>
            <td><%=formatter.format(hostDto.getScheduleTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <td>
                <a target="_blank"
                   href="viewlog.jsp?id=<%=hostDto.getAttemptID()%>&status=<%=hostDto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        }

        }
        }%>
        </tbody>
    </table>
</ul>

<ul class="dependency-timeout-tag col-sm-12">
    <li><a>依赖超时的任务<span class="label label-important">DEPENDENCY_TIMEOUT</span></a></li>
</ul>

<ul class="breadcrumb col-sm-12">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover " id="dependency-timeout">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
            <!-- <th>IP</th> -->
            <th>查看日志</th>

        </tr>
        </thead>
        <tbody>
        <%
            ClientResource dependencyTimeOutCr = new ClientResource(url + 3);
            IGetAttemptsByStatus dependencyTimeOutResource = dependencyTimeOutCr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> dependencyTimeOutAttempts = dependencyTimeOutResource.retrieve();

            if (dependencyTimeOutAttempts != null)
                for (AttemptDTO hostDto : dependencyTimeOutAttempts) {
                    String exeHost = hostDto.getExecHost();
                    if (hostName != null && hostName.equals(exeHost)) {
                        String state = hostDto.getStatus();


                        if (taskTime != null) {
                            Date startDate = hostDto.getStartTime();
                            String startTime;
                            if (startDate == null) {
                                startTime = null;
                            } else {
                                startTime = formatter.format(startDate);
                            }

                            Date endDate = hostDto.getEndTime();
                            String endTime;
                            if (endDate == null) {
                                endTime = null;
                            } else {
                                endTime = formatter.format(endDate);
                            }
                            if (startTime != null && state.equals("DEPENDENCY_TIMEOUT") && (startTime.compareTo(taskTime) >= 0 || endTime.compareTo(taskTime) >= 0)) {
                                String taskName = "";
                                for (Task task : tasks) {
                                    if (task.getTaskid().equals(hostDto.getTaskID())) {
                                        taskName = task.getName();
                                        break;
                                    }
                                }
        %>
        <tr id="<%=hostDto.getAttemptID()%>">
            <td><%=hostDto.getTaskID()%>
            </td>
            <td><%=taskName%>
            </td>
            <%if (hostDto.getStartTime() != null) {%>
            <td><%=formatter.format(hostDto.getStartTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getEndTime() != null) {%>
            <td><%=formatter.format(hostDto.getEndTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getScheduleTime() != null) {%>
            <td><%=formatter.format(hostDto.getScheduleTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <td>
                <a target="_blank"
                   href="viewlog.jsp?id=<%=hostDto.getAttemptID()%>&status=<%=hostDto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        } else {
            if (state.equals("DEPENDENCY_TIMEOUT")) {
                String taskName = "";
                for (Task task : tasks) {
                    if (task.getTaskid().equals(hostDto.getTaskID())) {
                        taskName = task.getName();
                        break;
                    }
                }

        %>
        <tr id="<%=hostDto.getAttemptID()%>">
            <td><%=hostDto.getTaskID()%>
            </td>
            <td><%=taskName%>
            </td>
            <%if (hostDto.getStartTime() != null) {%>
            <td><%=formatter.format(hostDto.getStartTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getEndTime() != null) {%>
            <td><%=formatter.format(hostDto.getEndTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getScheduleTime() != null) {%>
            <td><%=formatter.format(hostDto.getScheduleTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <!-- <td><%=hostDto.getExecHost()%></td> -->

            <td>
                <a target="_blank"
                   href="viewlog.jsp?id=<%=hostDto.getAttemptID()%>&status=<%=hostDto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        }

        }
        }%>
        </tbody>
    </table>
</ul>

<ul class="timeout-tag col-sm-12">
    <li><a>超时的任务<span class="label label-important">TIMEOUT</span></a></li>
</ul>
<ul class="breadcrumb col-sm-12">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover " id="timeout">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
            <!-- <th>IP</th> -->
            <th>查看日志</th>

        </tr>
        </thead>
        <tbody>
        <%
            ClientResource timeOutCr = new ClientResource(url + 9);
            IGetAttemptsByStatus timeOutResource = timeOutCr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> timeOutAttempts = timeOutResource.retrieve();

            if (timeOutAttempts != null)
                for (AttemptDTO hostDto : timeOutAttempts) {
                    String exeHost = hostDto.getExecHost();
                    if (hostName != null && hostName.equals(exeHost)) {
                        String state = hostDto.getStatus();

                        if (taskTime != null) {
                            Date startDate = hostDto.getStartTime();
                            String startTime;
                            if (startDate == null) {
                                startTime = null;
                            } else {
                                startTime = formatter.format(startDate);
                            }

                            Date endDate = hostDto.getEndTime();
                            String endTime;
                            if (endDate == null) {
                                endTime = null;
                            } else {
                                endTime = formatter.format(endDate);
                            }
                            if (startTime != null && state.equals("TIMEOUT") && (startTime.compareTo(taskTime) >= 0 || endTime.compareTo(taskTime) >= 0)) {
                                String taskName = "";
                                for (Task task : tasks) {
                                    if (task.getTaskid().equals(hostDto.getTaskID())) {
                                        taskName = task.getName();
                                        break;
                                    }
                                }
        %>
        <tr id="<%=hostDto.getAttemptID()%>">
            <td><%=hostDto.getTaskID()%>
            </td>
            <td><%=taskName%>
            </td>
            <%if (hostDto.getStartTime() != null) {%>
            <td><%=formatter.format(hostDto.getStartTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getEndTime() != null) {%>
            <td><%=formatter.format(hostDto.getEndTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getScheduleTime() != null) {%>
            <td><%=formatter.format(hostDto.getScheduleTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <td>
                <a target="_blank"
                   href="viewlog.jsp?id=<%=hostDto.getAttemptID()%>&status=<%=hostDto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        } else {
            if (state.equals("TIMEOUT")) {
                String taskName = "";
                for (Task task : tasks) {
                    if (task.getTaskid().equals(hostDto.getTaskID())) {
                        taskName = task.getName();
                        break;
                    }
                }

        %>
        <tr id="<%=hostDto.getAttemptID()%>">
            <td><%=hostDto.getTaskID()%>
            </td>
            <td><%=taskName%>
            </td>
            <%if (hostDto.getStartTime() != null) {%>
            <td><%=formatter.format(hostDto.getStartTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getEndTime() != null) {%>
            <td><%=formatter.format(hostDto.getEndTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <%if (hostDto.getScheduleTime() != null) {%>
            <td><%=formatter.format(hostDto.getScheduleTime())%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>
            <!-- <td><%=hostDto.getExecHost()%></td> -->

            <td>
                <a target="_blank"
                   href="viewlog.jsp?id=<%=hostDto.getAttemptID()%>&status=<%=hostDto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        }
        }
        }%>
        </tbody>
    </table>
</ul>


</div>
<div class="tab-pane" id="log">
    <div id="log-panel">
        <div class="spann" id="spann">
            <ul class="run-tag">
                <li><a>日志信息<span class="label label-info">STDOUT</span></a></li>
            </ul>
            <div data-spy="scroll" data-offset="0" style="height: 510px; line-height: 20px; overflow: auto;"
                 class="terminal terminal-like col-sm-12" id="strout">


            </div>
        </div>
    </div>
</div>
<div class="tab-pane" id="statistics">

</div>

</div>
</div>
<% }%>

</div>
</div>
</div>

</body>
<script type="text/javascript">
    $(".atip").tooltip();


</script>
<script src="js/hosts.js" type="text/javascript">

</script>
</html>