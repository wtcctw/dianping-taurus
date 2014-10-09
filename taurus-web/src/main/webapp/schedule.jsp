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
            <a href="index.jsp">监控中心</a>
        </li>
        <li class="active">
            <a href="schedule.jsp">调度中心</a>
        </li>
    </ul>
</div>


<%@page import="com.dp.bigdata.taurus.restlet.resource.ITasksResource" %>
<%@page import="com.dp.bigdata.taurus.restlet.shared.TaskDTO" %>
<%@page import="java.text.SimpleDateFormat" %>

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
                <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-hover"
                       width="100%" id="example">
                    <thead>
                    <tr>
                        <th class="hide">ID</th>
                        <th width="15%">名称</th>
                        <th>IP</th>
                        <th>调度人</th>
                        <th>调度身份</th>
                        <th class="hide">组</th>
                        <th>创建时间</th>
                        <th>Crontab</th>
                        <th>状态</th>
                        <th class="center">-</th>
                        <th class="center">-</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% String task_api = host + "task";
                        String name = request.getParameter("name");
                        String path = request.getParameter("path");
                        String appname = request.getParameter("appname");
                        if (name != null && !name.isEmpty()) {
                            task_api = task_api + "?name=" + name;
                        } else if (appname != null) {
                            task_api = task_api + "?appname=" + appname;
                        } else if (currentUser != null) {
                            task_api = task_api + "?user=" + currentUser;
                        }
                        if (path != null && !path.equals("")) {
                    %>
                    <span style="color:red">提示:已部署的作业文件的路径为<%=path%></span>
                    <% }
                        cr = new ClientResource(task_api);
                        ITasksResource resource = cr.wrap(ITasksResource.class);
                        cr.accept(MediaType.APPLICATION_XML);
                        ArrayList<TaskDTO> tasks = resource.retrieve();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        for (TaskDTO dto : tasks) {
                            String state = dto.getStatus();
                            boolean isRunning = true;
                            if (state.equals("SUSPEND")) {
                                isRunning = false;
                            }
                            if (isRunning) {
                    %>
                    <tr id="<%=dto.getTaskid()%>">
                            <% } else { %>
                    <tr id="<%=dto.getTaskid()%>" class="error">
                        <%}%>
                        <td class="hide"><%=dto.getTaskid()%>
                        </td>
                        <td class="fixLength-td"><%=dto.getName()%>
                        </td>
                        <td><%=dto.getHostname()%>
                        </td>
                        <td><%=dto.getCreator()%>
                        </td>
                        <td><%=dto.getProxyuser()%>
                        </td>
                        <td class="hide">arch(mock)</td>
                        <td><%=formatter.format(dto.getAddtime())%>
                        </td>
                        <td><%=dto.getCrontab()%>
                        </td>
                        <td><%if (isRunning) {%>
                            <span class="label label-info"><%=state%></span>
                            <%} else {%>
                            <span class="label label-important"><%=state%></span>
                            <%}%>
                        </td>
                        <td>
                            <div class="btn-group">
                                <button class="btn btn-success dropdown-toggle" data-toggle="dropdown">
                                    Action <span class="icon-angle-down"></span></button>
                                <ul class="dropdown-menu">
                                    <li><a href="#confirm"
                                           onClick="action($(this).parents('tr').find('td')[0].textContent,1)">删除</a>
                                    </li>
                                    <% if (isRunning) {%>
                                    <li><a href="#confirm"
                                           onClick="action($(this).parents('tr').find('td')[0].textContent,2)">暂停</a>
                                    </li>
                                    <%} else { %>
                                    <li><a href="#confirm"
                                           onClick="action($(this).parents('tr').find('td')[0].textContent,2)">恢复</a>
                                    </li>
                                    <%}%>
                                    <li><a href="#confirm"
                                           onClick="action($(this).parents('tr').find('td')[0].textContent,3)">执行</a>
                                    </li>
                                    <li><a class="detailBtn" href="task_form.jsp?task_id=<%=dto.getTaskid()%>">详细</a>
                                    </li>
                                </ul>
                            </div>
                        </td>
                        <td><a id="attempts" class="btn btn-primary btn-small"
                               href="attempt.jsp?taskID=<%=dto.getTaskid()%>">运行历史</a></td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>

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
     aria-hidden="true" >
    <div class="modal-dialog">
        <div class="modal-content">
        </div>
    </div>
</div>


<script type="text/javascript">
    jQuery(function ($) {
        var oTable1 =
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


    })
</script>
<script type="text/javascript" charset="utf-8" src="js/schedule.js"></script>
<script src="js/jquery.validate.min.js" type="text/javascript"></script>
<script src="js/taurus_validate.js" type="text/javascript"></script>
</body>
</html>