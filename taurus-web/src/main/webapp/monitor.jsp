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
<%@ page import="com.dp.bigdata.taurus.web.servlet.AttemptProxyServlet" %>


<div class="common-header" id="common-header">

</div>
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
            <a href="monitor.jsp">任务监控</a>
        </li>
    </ul>
</div>

<div class="page-content">
<div id="alertContainer" class="container col-sm-10">
</div>


<%
    //        String admin = (String)request.getSession().getAttribute("Admin");
//        if (admin== null || !admin.equals("true")) {
//            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
//            String newLocn = "notadmin.jsp";
//            response.setHeader("Location", newLocn);
//        }


    Date time = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
    long hourTime = 60 * 60 * 1000;
    Integer countTotal = (Integer) request.getSession().getAttribute("count");
    if (countTotal == null)
        countTotal = 0;
%>

<div class="row">
<ul class="run-tag col-sm-12">
    <li><a class="atip" data-toggle="tooltip" data-placement="right" data-original-title="正在运行的任务"><span
            class="label label-info">RUNNING</span></a></li>
</ul>
<ul class="breadcrumb col-sm-12 ">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover" id="running">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
            <th>IP</th>
            <th>查看日志</th>

        </tr>
        </thead>
        <tbody>
        <%

            ClientResource crTask = new ClientResource(host + "gettasks");
            IGetTasks taskResource = crTask.wrap(IGetTasks.class);
            ArrayList<Task> tasks = taskResource.retrieve();

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


            cr = new ClientResource(url + taskTime);
            IGetAttemptsByStatus resource = cr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> attempts = resource.retrieve();

            if (attempts != null)
                for (AttemptDTO dto : attempts) {
                    String state = dto.getStatus();
                    if (state.equals("RUNNING")) {
                        String taskName = "";
                        for (Task task : tasks) {
                            if (task.getTaskid().equals(dto.getTaskID())) {
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
            <%if (dto.getExecHost() != null) {%>
            <td><%=dto.getExecHost()%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}
                boolean isViewLog = AttemptProxyServlet.isHostOverLoad(dto.getExecHost());
                if(isViewLog){

            %>
            <td>

                <a target="_blank" href="viewlog.jsp?id=<%=dto.getAttemptID()%>&status=<%=dto.getStatus()%>">日志</a>
            </td>

        </tr>
        <% }}
        } %>
        </tbody>
    </table>
</ul>
<div class="time_inal ">

    <a class="atip" data-toggle="tooltip" data-placement="top"
       data-original-title="当你点击了[-1h]后，在想切换到当前页面时，请点击[当天]，刷新页面无效噢～">[注意] </a>
    &nbsp;&nbsp; |&nbsp;&nbsp;
    <a class="atip"
       href="monitor.jsp?id=1&taskdate＝<%=df.format(new Date(time.getTime() - (countTotal+1)*hourTime))    %> "
       data-toggle="tooltip" data-placement="top"
       data-original-title="时间区间[<%=formatter.format(new Date(time.getTime() - (countTotal+1)*hourTime))%>~<%=formatter.format(new Date(time.getTime()- countTotal*hourTime))%>]">[-1h] </a>
    &nbsp;&nbsp; |&nbsp;&nbsp;
    <a class="atip" href="monitor.jsp?id=24&taskdate＝<%=df.format(new Date(new Date().getTime() -24*hourTime))    %>"
       data-toggle="tooltip" data-placement="top"
       data-original-title=" 时间区间[<%=formatter.format(new Date(new Date().getTime() -24*hourTime))%>~<%=formatter.format(new Date())%>]">[当天] </a>
</div>

<ul class="submit-fail-tag col-sm-12">
    <li><a class="atip" data-toggle="tooltip" data-placement="right" data-original-title="提交失败的任务"><span
            class="label label-important">SUBMIT_FAIL</span></a></li>
</ul>

<ul class="breadcrumb col-sm-12">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover" id="submitfail" style="width: 100%">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
             <th>IP</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (AttemptDTO dto : attempts) {
                String state = dto.getStatus();

                String taskName = "";
                for (Task task : tasks) {
                    if (task.getTaskid().equals(dto.getTaskID())) {
                        taskName = task.getName();
                        break;
                    }
                }

                if (state.equals("SUBMIT_FAIL")) {


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
            <%if (dto.getExecHost() != null) {%>
            <td><%=dto.getExecHost()%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>

        </tr>
        <% }
        }
        %>


        </tbody>
    </table>
</ul>

<ul class="fail-tag col-sm-12">
    <li><a class="atip" data-toggle="tooltip" data-placement="right" data-original-title="失败的任务"><span
            class="label label-important">FAILED</span></a></li>

</ul>

<ul class="breadcrumb col-sm-12">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover" id="fail" style="width: 100%">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
             <th>IP</th>
            <th>查看日志</th>
        </tr>
        </thead>
        <tbody>
        <%

            for (AttemptDTO dto : attempts) {
                String state = dto.getStatus();
                String taskName = "";
                for (Task task : tasks) {
                    if (task.getTaskid().equals(dto.getTaskID())) {
                        taskName = task.getName();
                        break;
                    }
                }


                if (state.equals("FAILED")) {


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
            <%if (dto.getExecHost() != null) {%>
            <td><%=dto.getExecHost()%>
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
        %>
        </tbody>
    </table>
</ul>

<ul class="dependency-timeout-tag col-sm-12">
    <li><a class="atip" data-toggle="tooltip" data-placement="right" data-original-title="依赖超时的任务"><span
            class="label label-important">DEPENDENCY_TIMEOUT</span></a></li>
</ul>

<ul class="breadcrumb col-sm-12">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover" id="dependency-timeout" style="width: 100%">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
             <th>IP</th>

        </tr>
        </thead>
        <tbody>
        <%

            for (AttemptDTO dto : attempts) {
                String taskName = "";
                for (Task task : tasks) {
                    if (task.getTaskid().equals(dto.getTaskID())) {
                        taskName = task.getName();
                        break;
                    }
                }
                String state = dto.getStatus();
                if (state.equals("DEPENDENCY_TIMEOUT")) {

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
            <%if (dto.getExecHost() != null) {%>
            <td><%=dto.getExecHost()%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>

        </tr>
        <% }
        }
        %>
        </tbody>
    </table>
</ul>

<ul class="timeout-tag col-sm-12">
    <li><a class="atip" data-toggle="tooltip" data-placement="right" data-original-title="超时的任务"><span
            class="label label-important">TIMEOUT</span></a></li>
</ul>
<ul class="breadcrumb col-sm-12">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover " id="timeout" style="width: 100%">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
            <th>IP</th>

        </tr>
        </thead>
        <tbody>
        <%
            for (AttemptDTO dto : attempts) {
                String state = dto.getStatus();
                String taskName = "";
                for (Task task : tasks) {
                    if (task.getTaskid().equals(dto.getTaskID())) {
                        taskName = task.getName();
                        break;
                    }
                }
                if (state.equals("TIMEOUT")) {

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
            <%if (dto.getExecHost() != null) {%>
            <td><%=dto.getExecHost()%>
            </td>
            <%} else {%>
            <td>NULL</td>
            <%}%>

        </tr>
        <% }
        }
        %>
        </tbody>
    </table>
</ul>

</div>
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
</div>
<script type="text/javascript">
    $.ajax({
        type: "get",
        url: "jsp/common-header.jsp",
        error: function () {
        },
        success: function (response, textStatus) {
            $("#common-header").html(response);
            $('li[id="monitor"]').addClass("active");
        }


    });
    $(".atip").tooltip();
    options = {
        delay: { show: 500, hide: 100 },
        trigger: 'click'
    };
    $(".optiontip").tooltip(options);
    $('#running').dataTable({
        bAutoWidth: true,
        "aoColumns": [
            { "sWidth": "15%" } ,
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "10%" }

        ],
        bJQueryUI: true
    });

    $('#submitfail').dataTable({
        bAutoWidth: true,
        "aoColumns": [
            { "sWidth": "15%" } ,
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "10%" }

        ],
        bJQueryUI: true
    });

    $('#fail').dataTable({
        bAutoWidth: true,
        "aoColumns": [
            { "sWidth": "15%" } ,
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "10%" }

        ],
        bJQueryUI: true
    });

    $('#timeout').dataTable({
        bAutoWidth: true,
        "aoColumns": [
            { "sWidth": "15%" } ,
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "10%" }

        ],
        bJQueryUI: true
    });
    $('#dependency-timeout').dataTable({
        bAutoWidth: true,
        "aoColumns": [
            { "sWidth": "15%" } ,
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "15%" },
            { "sWidth": "10%" }

        ],
        bJQueryUI: true
    });

</script>
<script type="text/javascript" charset="utf-8" language="javascript" src="js/jquery.dataTables.js"></script>
<script type="text/javascript" charset="utf-8" language="javascript" src="js/DT_bootstrap.js"></script>
<script type="text/javascript" charset="utf-8" language="javascript" src="js/attempt.js"></script>
</body>
</html>
