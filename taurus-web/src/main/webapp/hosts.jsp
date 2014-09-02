<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="en">
<head>
    <%@ include file="jsp/common-header.jsp" %>
    <%@ include file="jsp/common-nav.jsp" %>
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

<div class="row-fluid">

 <div class="span2">
    <%@include file="hostList.jsp" %>
</div>
<div class="span10">
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
    <div id="alertContainerError" class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">×</button>
        <%=opChs %>失败
    </div>
</div>
<% }
    if (dto != null) {
%>
<ul class="nav nav-tabs">

    <li class="active"><a href="#state" data-toggle="tab">运行状态</a></li>
    <li class=""><a href="#monitor" data-toggle="tab">任务监控</a></li>

    <li class=""><a href="#log" data-toggle="tab">日志</a></li>
    <li class=""><a href="#statistics" data-toggle="tab">统计</a></li>

</ul>

<div class="tab-content">
<div class="tab-pane active in" id="state">
    <table class="table" id="host_state">
        <thead>
        <tr>
            <th>#</th>
            <th>属性</th>
            <th>值</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr class="success">
            <td>1</td>
            <td>机器IP</td>
            <td><%=hostName%>
            </td>
            <td></td>
        </tr>
        <tr class="error">
            <td>2</td>
            <td>机器状态</td>
            <%if (dto.isOnline()) {%>
            <td>在线</td>
            <td><a id="down" title="这台agent将不在监控范围内，agent进程是否被kill并不能确定。" class="btn btn-primary btn-small"
                   href="updateHost?hostName=<%=hostName%>&op=down">下线</a></td>
            <%} else {%>
            <td>下线</td>
            <td><a id="up" title="这台agent将被纳入监控范围内，agent需要手动启动。" class="btn btn-primary btn-small"
                   href="updateHost?hostName=<%=hostName%>&op=up">上线</a></td>
            <%} %>
        </tr>
        <tr class="warning">
            <td>3</td>
            <td>心跳状态</td>
            <%if (dto.isConnected()) {%>
            <td>正常</td>
            <td><a id="restart" class="btn btn-primary btn-small"
                   href="updateHost?hostName=<%=hostName%>&op=restart">重启</a></td>
            <%} else {%>
            <td>异常</td>
            <td>无法重启</td>
            <%} %>
        </tr>
        <tr class="info">
            <td>4</td>
            <td>版本</td>
            <td><% if (dto.getInfo() == null) {%>
                异常，无法获得版本号 <%
                } else {
                %><%=dto.getInfo().getAgentVersion() %><%}%></td>
            <%if (dto.isConnected()) {%>
            <td><a id="update" class="btn btn-primary btn-small"
                   href="updateHost?hostName=<%=hostName%>&op=update">升级</a></td>
            <%} else {%>
            <td>无法升级</td>
            <%} %>
        </tr>
        </tbody>
    </table>
</div>
<div class="tab-pane" id="monitor">

<ul class="run-tag">
    <li><a>正在运行的任务<span class="label label-info">RUNNING</span></a></li>
</ul>
<ul class="breadcrumb">
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
                    String exeHost=hostDto.getExecHost();
                    if (hostName!=null && hostName.equals(exeHost)) {
                        if (tasks!=null&&startTime != null && state.equals("RUNNING") && (startTime.compareTo(now) <= 0 && (endTime == null || endTime.compareTo(now) >= 0))) {
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

<ul class="submit-fail-tag">
    <li><a>提交失败的任务 <span class="label label-important">SUBMIT_FAIL</span></a></li>
</ul>

<ul class="breadcrumb">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover" id="submitfail">
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
                    String exeHost=hostDto.getExecHost();
                    if (hostName!=null && hostName.equals(exeHost)) {
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

<ul class="fail-tag">
    <li><a>失败的任务 <span class="label label-important">FAILED</span></a></li>

</ul>

<ul class="breadcrumb">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover" id="fail">
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
                    String exeHost=hostDto.getExecHost();
                    if (hostName!=null && hostName.equals(exeHost)) {
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

<ul class="dependency-timeout-tag">
    <li><a>依赖超时的任务<span class="label label-important">DEPENDENCY_TIMEOUT</span></a></li>
</ul>

<ul class="breadcrumb">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover" id="dependency-timeout">
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
                    String exeHost=hostDto.getExecHost();
                    if (hostName!=null && hostName.equals(exeHost)) {
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

<ul class="timeout-tag">
    <li><a>超时的任务<span class="label label-important">TIMEOUT</span></a></li>
</ul>
<ul class="breadcrumb">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover" id="timeout">
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
                    String exeHost=hostDto.getExecHost();
                    if (hostName!=null && hostName.equals(exeHost)) {
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
                 class="terminal terminal-like " id="strout">


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

</body>
<script type="text/javascript">
    $(".atip").tooltip();
</script>
<script src="js/hosts.js" type="text/javascript">

</script>
</html>