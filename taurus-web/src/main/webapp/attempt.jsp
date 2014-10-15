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
                        <th>实际启动时间</th>
                        <th>实际结束时间</th>
                        <th>预计调度时间</th>
                        <!-- <th>IP</th> -->
                        <th>返回值</th>
                        <th>状态</th>
                        <th>-</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
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
                    %>
                    <tr id="<%=dto.getAttemptID()%>">
                        <td><%=dto.getId()%>
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

        $.ajax({
            type: "get",
            url: "jsp/common-header.jsp",
            error: function () {
            },
            success: function (response, textStatus) {
                $("#common-header").html(response);
                $('li[id="schedule"]').addClass("active");
            }


        });
    })
</script>

<script type="text/javascript" charset="utf-8" language="javascript" src="js/attempt.js"></script>

</body>
</html>