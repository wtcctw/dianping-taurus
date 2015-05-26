<!DOCTYPE html >
<html >
<head>
	
	<title>Taurus</title>
	<#include "segment/html_header.ftl">
	<script type="text/javascript" src="${rc.contextPath}/resource/js/lib/Chart.js"></script>
	<#include "segment/html_header2.ftl">
	<style>
        .spann {
            width: 95%;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/DT_bootstrap.css">
    <link href="${rc.contextPath}/css/viewlog.css" rel="stylesheet" type="text/css">
</head>
<body data-spy="scroll">
<#include "segment/header.ftl">
<#include "segment/left.ftl">

<div class="mid-div col-sm-12">
<div class="page-content col-sm-12">
<div class="sidebar col-sm-2 no-padding-left">
<#include "hostList.ftl">
</div>

<div class="main-content">
<div class="page-content col-sm-12">
<#if statusCode == "200">
<div id="alertContainer" class="container">
    <div id="alertContainerSuccess" class="alert alert-success">
        <button type="button" class="close" data-dismiss="alert">×</button>
        ${opChs!}成功
    </div>
</div>
<#elseif statusCode == "500">
<div id="alertContainer" class="container">
    <div id="alertContainerError" class="alert alert-danger">
        <button type="button" class="close" data-dismiss="alert">×</button>
        ${opChs!}失败
    </div>
</div>
</#if>
<%
    if (dto != null) {
%>

<ul class="nav nav-tabs">
    <li class="active"><a href="#state" data-toggle="tab">运行状态</a></li>
    <li class=""><a href="#taskmonitor" data-toggle="tab">任务监控</a></li>
    <li class=""><a href="#log" data-toggle="tab">日志</a></li>
    <li class=""><a href="#statistics" data-toggle="tab">统计</a></li>
</ul>

<#-- agent机器详情 start -->
<div class="tab-content col-sm-12">

<#-- 运行状态标签 start -->
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

        <tr class="info">
            <td>5</td>
            <td>主机任务执行历史</td>
            <td><a id="history" class="btn btn-primary btn-minier"
                   href="host_history.jsp?ip=<%=hostName%>">查看</a></td>
        </tr>
        </tbody>
    </table>
</div>
<#-- 运行状态标签 end -->

<#-- 任务监控标签 start -->
<div class="tab-pane" id="taskmonitor">

<#-- 正在运行的任务 start -->
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
<#-- 正在运行的任务 end -->

<#-- 提交失败的任务 start -->
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
<#-- 提交失败的任务 end -->

<#-- 失败的任务 start -->
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
<#-- 失败的任务 end -->

<#-- 依赖超时的任务 start -->
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
<#-- 依赖超时的任务 end -->

<#-- 超时的任务 start -->
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
<#-- 超时的任务 end -->

</div>
<#-- 任务监控标签 end -->

<#-- 日志标签 start -->
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
<#-- 日志标签 end -->

<#-- 统计标签 start -->
<div class="tab-pane" id="statistics">
</div>
<#-- 统计标签 end -->

</div>
<#-- agent机器详情 end -->
<% }%>
</div>


</div>
</div>
</div>

<div class="feedTool">
    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="${rc.contextPath}/img/qq.png"  width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img border="0" src="${rc.contextPath}/img/x_alt.png"  width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>
    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>

<script type="text/javascript">
    $(".atip").tooltip();
</script>
<script src="${rc.contextPath}/static/js/hosts.js" type="text/javascript"></script>

</body>
</html>