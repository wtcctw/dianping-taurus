<%@ page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Taurus</title>
    <meta charset="utf-8">
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <%@ include file="jsp/common-nav.jsp" %>
    <!-- basic styles -->
    <script type="text/javascript" src="resource/js/lib/jquery-1.9.1.min.js"></script>
    <link href="lib/ace/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="lib/ace/js/ace-extra.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="css/jquery-ui.min.css"/>
    <script src="lib/ace/js/ace-elements.min.js"></script>
    <script src="lib/ace/js/ace.min.js"></script>
    <script src="lib/ace/js/bootbox.min.js"></script>
    <script type="text/javascript" src="resource/js/lib/raphael.2.1.0.min.js"></script>
    <script type="text/javascript" src="resource/js/lib/justgage.1.0.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="js/login.js"></script>
    <!-- page specific plugin styles -->

    <!-- fonts -->
    <script src="lib/ace/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/ace-fonts.css"/>

    <!-- ace styles -->

    <link rel="stylesheet" href="lib/ace/css/ace.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-skins.min.css"/>
    <link href="css/bwizard.min.css" rel="stylesheet"/>
    <style>
        label.error {
            margin-left: 10px;
            color: red;
        }

        label.success {
            margin-left: 10px;
            color: green;
        }
        .creatorbtn{
            float: left;
        }
        .scrollup {
            opacity: 0.3;
            position: fixed;
            bottom: 50px;
            right: 100px;
            display: none;
        }
    </style>
</head>
<body>
<%@page import="java.util.Map" %>
<%@page import="java.util.HashMap" %>

<%@page import="com.dp.bigdata.taurus.restlet.resource.IUserGroupsResource" %>
<%@page import="com.dp.bigdata.taurus.restlet.resource.IUserGroupMappingsResource" %>

<%@page import="com.dp.bigdata.taurus.restlet.shared.UserGroupDTO" %>
<%@page import="com.dp.bigdata.taurus.restlet.shared.UserGroupMappingDTO" %>
<%@ page import="com.dp.bigdata.taurus.restlet.resource.ITasksResource" %>
<%@ page import="com.dp.bigdata.taurus.restlet.shared.TaskDTO" %>
<%@ page import="java.text.SimpleDateFormat" %>

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
        <li id="resign">
            <a href="resign.jsp" target="_self">
                <i class="icon-retweet"></i>
                <span class="menu-text"> 任务交接 </span>
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

<div class="main-content ">
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
            <a href="resign.jsp">离职交接</a>
        </li>
    </ul>
</div>
<div class="page-content">
<%
    cr = new ClientResource(host + "group");
    IUserGroupsResource groupResource = cr.wrap(IUserGroupsResource.class);
    cr.accept(MediaType.APPLICATION_XML);
    ArrayList<UserGroupDTO> groups = groupResource.retrieve();

    Map<String, String> map = new HashMap<String, String>();
    String userGroup = "";

    for (UserDTO user : users) {
        String group = user.getGroup();
        if (group == null || group.equals("")) {
            group = "未分组";
        }
        if (map.containsKey(group)) {
            map.put(group, map.get(group) + ", " + user.getName());
        } else {
            map.put(group, user.getName());
        }
        if (user.getName().equals(currentUser)) {
            if (user.getGroup() == null || user.getGroup().equals("")) {
                userGroup = "null";
%>
<div id="alertContainer" class="container col-sm-12">
    <div id="alertContainer" class="alert alert-danger">
        <button type="button" class="close" data-dismiss="alert">×</button>
        你未设分组，不能交接任务，请到<a href="user.jsp">用户设置</a>中设置自己的分组~
    </div>
</div>
<%} else {%>
<div id="alertContainer" class="container col-sm-12"></div>
<%} %>


<div class="container" style="margin-top: 10px">
    <div class="row">
        <div class="col-sm-8 padding-14">

            <%
                if (!userGroup.equals("null")) {
            %>
            <div class=" creatorbtn">
                <a class="atip tooltip-info" data-toggle="tooltip" data-placement="bottom"
                   data-original-title="你只能把自己组的任务指派给自己组的成员，如果你需要交接给别的组请联系 李明 【kirin.li@dianping.com】">[提示] </a>
                选择你要交接的任务：
                <button class="btn btn-info" type="button" id="creatorbtn">
                    <i class="ace-icon fa fa-check bigger-110"></i>
                    交接任务
                </button>

            </div>
            <%
                }%>
            <br/>


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
            <% }
                cr = new ClientResource(task_api);
                ITasksResource resource = cr.wrap(ITasksResource.class);
                cr.accept(MediaType.APPLICATION_XML);
                ArrayList<TaskDTO> tasks = resource.retrieve();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                if (tasks == null || tasks.size() == 0) {%>

            <div class="align-center">
                <i class='icon-info-sign icon-large red '>你没有创建任何任务～</i>
            </div>

            <% } else {%>
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
                </tr>
                </thead>
                <tbody>
                <%
                    }
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
                    <td class="fixLength-td"><input type="checkbox" class="field taskcheckbox"
                                                    id="taskcheckbox" name="taskcheckbox" value="<%=dto.getName()%>"
                            ><%=dto.getName()%>
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

                </tr>
                <% } %>
                </tbody>
            </table>
            <%
                        userGroup = user.getGroup();
                    }
                }
            %>
        </div>
        <div class="col-sm-4" style="opacity: 0.5">

            <%
                if (userGroup.equals("null")) {
            %>
            你未设置分组！
            <%
            } else {
            %>


            选择你要指派的人：<br/>
            <%
            if (!userGroup.equals("admin")) { %>
            组名：<%=userGroup%> <br>
            <table class="table table-striped table-bordered table-condensed">
                <tr>
                    <th align="left" width="85%">成员</th>
                </tr>
                <%


                        for (String group : map.keySet()) {
                            if (group.equals(userGroup)) {
                %>


                <%
                    String groupUsers = map.get(group);
                    String[] userList = groupUsers.split(",");

                    for (int i = 0; i < userList.length; i++) {
                        String creator = userList[i];
                %>
                <tr>
                    <td align="left">
                        <input type="radio" value="<%=creator%>" name="creator"><%=creator%>
                    </td>
                </tr>
                <%
                    }

                %>


                <%
                        }
                    }
                } else {
                            %>
                <table class="table table-striped table-bordered table-condensed">
                    <tr>
                        <th align="left" width="85%">成员</th>
                        <th align="left" width="85%">组名</th>
                    </tr>
                <%
                    for (String group : map.keySet()) {
                %>


                <%
                    String groupUsers = map.get(group);
                    String[] userList = groupUsers.split(",");

                    for (int i = 0; i < userList.length; i++) {
                        String creator = userList[i];
                %>
                <tr>
                    <td align="left">
                        <input type="radio" value="<%=creator%>" name="creator"><%=creator%>
                    </td>
                    <td align="left">
                        <%=group%>
                    </td>
                </tr>
                <%
                    }

                %>


                <%
                        }
                    }
                %>

            </table>
            <%
                }
            %>
        </div>
    </div>


    <div class="col-sm-8" id="adjustout"></div>
</div>

</div>

</div>
<a href="#" class="scrollup" style="display: inline;">
    <img src="img/ScrollTopArrow.png" width="50" height="50">
</a>
<script type="text/javascript">
    $('li[id="resign"]').addClass("active");
    $('#menu-toggler').on(ace.click_event, function () {
        $('#sidebar').toggleClass('display');
        $(this).toggleClass('display');
        return false;
    });

    var userList = "", groupList = "", isAdmin;
    <%for(UserDTO user:users) {%>
    userList = userList + ",<%=user.getName()%>";
    <%}%>
    <%for(UserGroupDTO group:groups) {%>
    groupList = groupList + ",<%=group.getName()%>";
    <%}%>
    isAdmin = <%=isAdmin%>;
    userList = userList.substr(1);
    groupList = groupList.substr(1);

</script>
<script src="js/jquery.validate.min.js" type="text/javascript"></script>
<script src="js/resign.js" type="text/javascript"></script>


</body>

</html>