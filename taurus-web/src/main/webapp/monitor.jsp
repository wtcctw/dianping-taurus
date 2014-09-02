<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="en">
<head>
    <%@ include file="jsp/common-header.jsp" %>
    <%@ include file="jsp/common-nav.jsp" %>
    <link rel="stylesheet" type="text/css" href="css/DT_bootstrap.css">
    <style>

        .time_inal {
            float: right
        }
    </style>
</head>
<body data-spy="scroll">
<%@page import="org.restlet.data.MediaType,
                org.restlet.resource.ClientResource,
                com.dp.bigdata.taurus.restlet.shared.AttemptDTO,
                java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.dp.bigdata.taurus.restlet.resource.impl.TaskResource" %>
<%@ page import="com.dp.bigdata.taurus.restlet.shared.TaskDTO" %>
<%@ page import="com.dp.bigdata.taurus.core.InstanceID" %>
<%@ page import="org.restlet.data.Form" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dp.bigdata.taurus.restlet.resource.*" %>
<%@ page import="com.dp.bigdata.taurus.generated.module.Task" %>

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
    <li><a class="atip"  data-toggle="tooltip" data-placement="right" data-original-title="正在运行的任务"><span class="label label-info">RUNNING</span></a></li>
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
            IGetTasks taskResource = crTask.wrap(IGetTasks.class);
            ArrayList<Task> tasks= taskResource.retrieve();

            String id = request.getParameter("id");
            String taskTime = "";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String url = host + "getattemptsbystatus/";

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



            cr = new ClientResource(url+6);
            IGetAttemptsByStatus resource = cr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> attempts = resource.retrieve();

            if (attempts !=null)
            for (AttemptDTO dto : attempts) {
                Date startDate = dto.getStartTime();
                String startTime;
                if (startDate == null){
                    startTime = null;
                }else {
                    startTime = formatter.format(startDate);
                }

                Date endDate = dto.getEndTime();
                String endTime;
                if (endDate == null)
                {
                    endTime=null;
                }else{
                    endTime = formatter.format(endDate);
                }


                String state = dto.getStatus();
                if (startTime!=null &&state.equals("RUNNING")&& (startTime.compareTo(now) <=0 &&(endTime == null|| endTime.compareTo(now) >= 0))) {
                    String taskName ="";
                    for (Task task: tasks){
                        if (task.getTaskid().equals(dto.getTaskID())){
                            taskName = task.getName();
                            break;
                        }
                    }
        %>
        <tr id="<%=dto.getAttemptID()%>">
            <td><%=dto.getTaskID()%>
            </td>
            <td><%=taskName%>
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

                <a target="_blank" href="viewlog.jsp?id=<%=dto.getAttemptID()%>&status=<%=dto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        } %>
        </tbody>
    </table>
</ul>
<div class="time_inal">

    <a class="atip"  data-toggle="tooltip" data-placement="top" data-original-title="当你点击了[-1h]后，在想切换到当前页面时，请点击[当天]，刷新页面无效噢～">[注意] </a>
    &nbsp;&nbsp; |&nbsp;&nbsp;
    <a class="atip" href="monitor.jsp?id=1&taskdate＝<%=df.format(new Date(time.getTime() - (countTotal+1)*hourTime))    %> "  data-toggle="tooltip" data-placement="top" data-original-title="时间区间[<%=formatter.format(new Date(time.getTime() - (countTotal+1)*hourTime))%>~<%=formatter.format(new Date(time.getTime()- countTotal*hourTime))%>]">[-1h] </a>
    &nbsp;&nbsp; |&nbsp;&nbsp;
    <a class="atip" href="monitor.jsp?id=24&taskdate＝<%=df.format(new Date(new Date().getTime() -24*hourTime))    %>"  data-toggle="tooltip" data-placement="top" data-original-title=" 时间区间[<%=formatter.format(new Date(new Date().getTime() -24*hourTime))%>~<%=formatter.format(new Date())%>]">[当天] </a>
</div>

<ul class="submit-fail-tag">
    <li><a class="atip"  data-toggle="tooltip" data-placement="right" data-original-title="提交失败的任务"><span class="label label-important">SUBMIT_FAIL</span></a></li>
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
            ClientResource submitFailCr = new ClientResource(url+5);
            IGetAttemptsByStatus submitFailResource = submitFailCr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> submitFailAttempts = submitFailResource.retrieve();

            if (submitFailAttempts != null)
                for (AttemptDTO dto : submitFailAttempts) {
                    String state = dto.getStatus();


                    if (taskTime != null) {
                        Date startDate = dto.getStartTime();
                        String startTime;
                        if (startDate == null){
                            startTime = null;
                        }else {
                            startTime = formatter.format(startDate);
                        }

                        Date endDate = dto.getEndTime();
                        String endTime;
                        if (endDate == null)
                        {
                            endTime=null;
                        }else{
                            endTime = formatter.format(endDate);
                        }

                        if (startTime!=null && state.equals("SUBMIT_FAIL") && (startTime.compareTo(taskTime) >=0 || endTime.compareTo(taskTime) >= 0)) {
                            String taskName ="";
                            for (Task task: tasks){
                                if (task.getTaskid().equals(dto.getTaskID())){
                                    taskName = task.getName();
                                    break;
                                }
                            }

        %>
        <tr id="<%=dto.getAttemptID()%>">
            <td><%=dto.getTaskID()%>
            </td>
            <td><%=taskName%>
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
                <a target="_blank" href="viewlog.jsp?id=<%=dto.getAttemptID()%>&status=<%=dto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        } else {
            if (state.equals("SUBMIT_FAIL")) {
                String taskName ="";
                for (Task task: tasks){
                    if (task.getTaskid().equals(dto.getTaskID())){
                        taskName = task.getName();
                        break;
                    }
                }

        %>
        <tr id="<%=dto.getAttemptID()%>">
            <td><%=dto.getTaskID()%>
            </td>
            <td><%=taskName%>
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
                <a target="_blank" href="viewlog.jsp?id=<%=dto.getAttemptID()%>&status=<%=dto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        }

        }%>
        </tbody>
    </table>
</ul>

<ul class="fail-tag">
    <li><a class="atip"  data-toggle="tooltip" data-placement="right" data-original-title="失败的任务"><span class="label label-important">FAILED</span></a></li>

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
            ClientResource failCr = new ClientResource(url+8);
            IGetAttemptsByStatus failResource = failCr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> failAttempts = failResource.retrieve();

            if (failAttempts != null)
            for (AttemptDTO dto : failAttempts) {
               String state = dto.getStatus();


                if (taskTime != null) {
                    Date startDate = dto.getStartTime();
                    String startTime;
                    if (startDate == null){
                        startTime = null;
                    }else {
                        startTime = formatter.format(startDate);
                    }

                    Date endDate = dto.getEndTime();
                    String endTime;
                    if (endDate == null)
                    {
                        endTime=null;
                    }else{
                        endTime = formatter.format(endDate);
                    }

                    if (startTime!=null && state.equals("FAILED") && (startTime.compareTo(taskTime) >=0 || endTime.compareTo(taskTime) >= 0)) {
                        String taskName ="";
                        for (Task task: tasks){
                            if (task.getTaskid().equals(dto.getTaskID())){
                                taskName = task.getName();
                                break;
                            }
                        }

        %>
        <tr id="<%=dto.getAttemptID()%>">
            <td><%=dto.getTaskID()%>
            </td>
            <td><%=taskName%>
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
                <a target="_blank" href="viewlog.jsp?id=<%=dto.getAttemptID()%>&status=<%=dto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        } else {
            if (state.equals("FAILED")) {
                String taskName ="";
                for (Task task: tasks){
                    if (task.getTaskid().equals(dto.getTaskID())){
                        taskName = task.getName();
                        break;
                    }
                }

        %>
        <tr id="<%=dto.getAttemptID()%>">
            <td><%=dto.getTaskID()%>
            </td>
            <td><%=taskName%>
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
               <a target="_blank" href="viewlog.jsp?id=<%=dto.getAttemptID()%>&status=<%=dto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        }

        }%>
        </tbody>
    </table>
</ul>

<ul class="dependency-timeout-tag">
    <li><a class="atip"  data-toggle="tooltip" data-placement="right" data-original-title="依赖超时的任务"><span class="label label-important">DEPENDENCY_TIMEOUT</span></a></li>
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
            ClientResource dependencyTimeOutCr = new ClientResource(url+3);
            IGetAttemptsByStatus dependencyTimeOutResource = dependencyTimeOutCr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> dependencyTimeOutAttempts = dependencyTimeOutResource.retrieve();

            if (dependencyTimeOutAttempts !=null)
                for (AttemptDTO dto : dependencyTimeOutAttempts) {
                    String state = dto.getStatus();


                    if (time != null) {
                        Date startDate = dto.getStartTime();
                        String startTime;
                        if (startDate == null){
                            startTime = null;
                        }else {
                            startTime = formatter.format(startDate);
                        }

                        Date endDate = dto.getEndTime();
                        String endTime;
                        if (endDate == null)
                        {
                            endTime=null;
                        }else{
                            endTime = formatter.format(endDate);
                        }
                        if (startTime!=null && state.equals("DEPENDENCY_TIMEOUT") && ( startTime.compareTo(taskTime) >= 0 || endTime.compareTo(taskTime) >= 0)) {
                            String taskName ="";
                            for (Task task: tasks){
                                if (task.getTaskid().equals(dto.getTaskID())){
                                    taskName = task.getName();
                                    break;
                                }
                            }
        %>
        <tr id="<%=dto.getAttemptID()%>">
            <td><%=dto.getTaskID()%>
            </td>
            <td><%=taskName%>
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
                <a target="_blank" href="viewlog.jsp?id=<%=dto.getAttemptID()%>&status=<%=dto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        } else {
            if (state.equals("DEPENDENCY_TIMEOUT")) {
                String taskName ="";
                for (Task task: tasks){
                    if (task.getTaskid().equals(dto.getTaskID())){
                        taskName = task.getName();
                        break;
                    }
                }

        %>
        <tr id="<%=dto.getAttemptID()%>">
            <td><%=dto.getTaskID()%>
            </td>
            <td><%=taskName%>
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
                <a target="_blank" href="viewlog.jsp?id=<%=dto.getAttemptID()%>&status=<%=dto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        }

        }%>
        </tbody>
    </table>
</ul>

<ul class="timeout-tag">
    <li><a class="atip"  data-toggle="tooltip" data-placement="right" data-original-title="超时的任务"><span class="label label-important">TIMEOUT</span></a></li>
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
            ClientResource timeOutCr = new ClientResource(url+9);
            IGetAttemptsByStatus timeOutResource = timeOutCr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> timeOutAttempts = timeOutResource.retrieve();

            if (timeOutAttempts !=null)
            for (AttemptDTO dto : timeOutAttempts) {
                String state = dto.getStatus();


                if (time != null) {
                    Date startDate = dto.getStartTime();
                    String startTime;
                    if (startDate == null){
                        startTime = null;
                    }else {
                        startTime = formatter.format(startDate);
                    }

                    Date endDate = dto.getEndTime();
                    String endTime;
                    if (endDate == null)
                    {
                        endTime=null;
                    }else{
                        endTime = formatter.format(endDate);
                    }
                    if (startTime!=null && state.equals("TIMEOUT") && ( startTime.compareTo(taskTime) >= 0 || endTime.compareTo(taskTime) >= 0)) {
                        String taskName ="";
                        for (Task task: tasks){
                            if (task.getTaskid().equals(dto.getTaskID())){
                                taskName = task.getName();
                                break;
                            }
                        }
        %>
        <tr id="<%=dto.getAttemptID()%>">
            <td><%=dto.getTaskID()%>
            </td>
            <td><%=taskName%>
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
                <a target="_blank" href="viewlog.jsp?id=<%=dto.getAttemptID()%>&status=<%=dto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }
        } else {
            if (state.equals("TIMEOUT")) {
                String taskName ="";
                for (Task task: tasks){
                    if (task.getTaskid().equals(dto.getTaskID())){
                        taskName = task.getName();
                        break;
                    }
                }

        %>
        <tr id="<%=dto.getAttemptID()%>">
            <td><%=dto.getTaskID()%>
            </td>
            <td><%=taskName%>
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
                <a target="_blank" href="viewlog.jsp?id=<%=dto.getAttemptID()%>&status=<%=dto.getStatus()%>">日志</a>
            </td>

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
<script type="text/javascript">
    $(".atip").tooltip();
    options={
        delay: { show: 500, hide: 100 },
        trigger:'click',
    };
    $(".optiontip").tooltip(options);
</script>
<script type="text/javascript" charset="utf-8" language="javascript" src="js/jquery.dataTables.js"></script>
<script type="text/javascript" charset="utf-8" language="javascript" src="js/DT_bootstrap.js"></script>
<script type="text/javascript" charset="utf-8" language="javascript" src="js/attempt.js"></script>
</body>
</html>
