<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="en">
<head>
    <%@ include file="jsp/common-header.jsp" %>
    <%@ include file="jsp/common-nav.jsp" %>
    <link rel="stylesheet" type="text/css" href="css/DT_bootstrap.css">
    <style>
        .fail-tag {
            font-size: 130%
        }

        .run-tag {
            font-size: 130%
        }

        .timeout-tag {
            font-size: 130%
        }

        .time_inal {
            float: right
        }
    </style>
</head>
<body data-spy="scroll">
<%@page import="org.restlet.data.MediaType,
                org.restlet.resource.ClientResource,
                com.dp.bigdata.taurus.restlet.resource.IAttemptsResource,
                com.dp.bigdata.taurus.restlet.shared.AttemptDTO,
                java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.dp.bigdata.taurus.restlet.resource.impl.TaskResource" %>
<%@ page import="com.dp.bigdata.taurus.restlet.shared.TaskDTO" %>
<%@ page import="com.dp.bigdata.taurus.restlet.resource.ITaskResource" %>
<%@ page import="com.dp.bigdata.taurus.core.InstanceID" %>
<%@ page import="org.restlet.data.Form" %>
<%@ page import="com.dp.bigdata.taurus.restlet.resource.ITasksResource" %>
<%@ page import="java.util.List" %>

<div class="container" style="margin-top: 10px">
<div id="alertContainer" class="container">
</div>
<ul class="breadcrumb">
    <li><a href="./index.jsp">首页</a> <span class="divider">/</span></li>
    <li><a href="#" class="active">任务监控</a> <span class="divider">/</span></li>

    <% Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        long hourTime = 60 * 60 * 1000;
        Integer countTotal = (Integer) request.getSession().getAttribute("count");
        if (countTotal == null)
            countTotal = 0;
    %>

</ul>


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


            String id = request.getParameter("id");
            String taskTime = "";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String url = host + "attempt";

            String now = formatter.format(new Date());
            if (id == null) {

                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));

            } else if (id.equals("1")) {
                Integer count = (Integer) request.getSession().getAttribute("count");
                if (count == null)
                    count = 0;
                Integer changeCount = count + 1;
                request.getSession().setAttribute("count", changeCount);

                taskTime = formatter.format(new Date(new Date().getTime() - (count + 1) * hourTime));
            } else if (id.equals("24")) {
                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));
                request.getSession().setAttribute("count", 0);
            }



            cr = new ClientResource(url);
            cr.setRequestEntityBuffering(true);
            IAttemptsResource resource = cr.wrap(IAttemptsResource.class);
            cr.accept(MediaType.APPLICATION_XML);
            ArrayList<AttemptDTO> attempts = resource.retrieve();


            for (AttemptDTO dto : attempts) {
                String startTime = formatter.format(dto.getStartTime());
                String endTime = formatter.format(dto.getEndTime());
                String state = dto.getStatus();
                if (state.equals("RUNNING")&& (startTime.compareTo(now) <=0 &&(endTime.isEmpty()|| endTime.compareTo(now) >= 0))) {
                    ClientResource crTask = new ClientResource(host + "task" + "/" + dto.getTaskID());
                    ITaskResource taskResource = crTask.wrap(ITaskResource.class);
                    TaskDTO taskDTO = taskResource.retrieve();



        %>
        <tr id="<%=dto.getAttemptID()%>">
            <td><%=dto.getTaskID()%>
            </td>
            <td><%=taskDTO.getName()%>
            </td>
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
            <td>
                    <%if(state.equals("RUNNING") || state.equals("TIMEOUT")){%>
                <a>无日志</a>
                    <%}else {%>
                <a target="_blank" href="attempts.do?id=<%=dto.getAttemptID()%>&action=view-log">日志</a>
                    <%}%>

        </tr>
        <% }
        } %>
        </tbody>
    </table>
</ul>
<div class="time_inal">

    <a href="monitor.jsp?id=1&taskdate＝<%=df.format(new Date(time.getTime() - (countTotal+1)*hourTime))    %>">[-1h]的时间区间[<%=formatter.format(new Date(time.getTime() - (countTotal+1)*hourTime))%>~<%=formatter.format(new Date(time.getTime()- countTotal*hourTime))%>] </a>
    |<a href="monitor.jsp?id=24&taskdate＝<%=df.format(new Date(new Date().getTime() -24*hourTime))    %>">[当天]的时间区间[<%=formatter.format(new Date(new Date().getTime() -24*hourTime))%>~<%=formatter.format(new Date())%>] </a>
</div>
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
            for (AttemptDTO dto : attempts) {
                String state = dto.getStatus();
                ClientResource crTask = new ClientResource(host + "task" + "/" + dto.getTaskID());
                ITaskResource taskResource = crTask.wrap(ITaskResource.class);
                TaskDTO taskDTO = taskResource.retrieve();

                if (taskTime != null) {
                    String startTime = formatter.format(dto.getStartTime());
                    String endTime = formatter.format(dto.getEndTime());

                    if (state.equals("FAILED") && (startTime.compareTo(taskTime) >=0 || endTime.compareTo(taskTime) >= 0)) {


        %>
        <tr id="<%=dto.getAttemptID()%>">
            <td><%=dto.getTaskID()%>
            </td>
            <td><%=taskDTO.getName()%>
            </td>
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
            <td>
                    <%if(state.equals("RUNNING") || state.equals("TIMEOUT")){%>
                <a>无日志</a>
                    <%}else {%>
                <a target="_blank" href="attempts.do?id=<%=dto.getAttemptID()%>&action=view-log">日志</a>
                    <%}%>

        </tr>
        <% }
        } else {
            if (state.equals("FAILED")) {


        %>
        <tr id="<%=dto.getAttemptID()%>">
            <td><%=dto.getTaskID()%>
            </td>
            <td><%=taskDTO.getName()%>
            </td>
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
            <td>
                    <%if(state.equals("RUNNING") || state.equals("TIMEOUT")){%>
                <a>无日志</a>
                    <%}else {%>
                <a target="_blank" href="attempts.do?id=<%=dto.getAttemptID()%>&action=view-log">日志</a>
                    <%}%>

        </tr>
        <% }
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

            for (AttemptDTO dto : attempts) {
                String state = dto.getStatus();
                ClientResource crTask = new ClientResource(host + "task" + "/" + dto.getTaskID());
                ITaskResource taskResource = crTask.wrap(ITaskResource.class);
                TaskDTO taskDTO = taskResource.retrieve();

                if (time != null) {
                    String startTime = formatter.format(dto.getStartTime());
                    String endTime = formatter.format(dto.getEndTime());
                    if (state.equals("TIMEOUT") && ( startTime.compareTo(taskTime) >= 0 || endTime.compareTo(taskTime) >= 0)) {
        %>
        <tr id="<%=dto.getAttemptID()%>">
            <td><%=dto.getTaskID()%>
            </td>
            <td><%=taskDTO.getName()%>
            </td>
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
            <!-- <td><%=dto.getExecHost()%></td> -->
            <td>
                    <%if(state.equals("RUNNING") || state.equals("TIMEOUT")){%>
                <!-- <li> -->
                <a>无日志</a>
                <!-- </li> -->
                    <%}else {%>
                <!-- <li> -->
                <a target="_blank" href="attempts.do?id=<%=dto.getAttemptID()%>&action=view-log">日志</a>
                <!-- </li> -->
                    <%}%>
                <!-- </ul>
            </div> -->

        </tr>
        <% }
        } else {
            if (state.equals("TIMEOUT")) {


        %>
        <tr id="<%=dto.getAttemptID()%>">
            <td><%=dto.getTaskID()%>
            </td>
            <td><%=taskDTO.getName()%>
            </td>
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
            <!-- <td><%=dto.getExecHost()%></td> -->
            <td>
                    <%if(state.equals("RUNNING") || state.equals("TIMEOUT")){%>
                <!-- <li> -->
                <a>无日志</a>
                <!-- </li> -->
                    <%}else {%>
                <!-- <li> -->
                <a target="_blank" href="attempts.do?id=<%=dto.getAttemptID()%>&action=view-log">日志</a>
                <!-- </li> -->
                    <%}%>
                <!-- </ul>
            </div> -->

        </tr>
        <% }
        }

        }%>
        </tbody>
    </table>
</ul>


</div>

<div id="confirm" class="modal hide fade">
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

<script type="text/javascript" charset="utf-8" language="javascript" src="js/jquery.dataTables.js"></script>
<script type="text/javascript" charset="utf-8" language="javascript" src="js/DT_bootstrap.js"></script>
<script type="text/javascript" charset="utf-8" language="javascript" src="js/attempt.js"></script>
</body>
</html>
