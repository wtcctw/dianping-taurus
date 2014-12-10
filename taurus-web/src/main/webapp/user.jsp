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
        label.error{margin-left: 10px; color: red;}
        label.success{margin-left: 10px; color: green;}
        .scrollup {
            opacity: 0.3;
            position: fixed;
            bottom: 150px;
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
        <div class="pull-right" style="margin:10px;color: white;"><i class="icon-group"> Taurus后援QQ群：155326270 </i></div>
        <div class="pull-right ng-binding" style="margin:10px;color: white;" ng-bind="monitorMessage"><i class="icon-user-md">开发者：李明 </i> <i class="icon-phone">: 13661871541</i></div>

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
        <li id="feedback">
            <a href="feedback.jsp" target="_self">
                <i class="icon-comments"></i>
                <span class="menu-text"> 我要反馈 </span>
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
                <a href="user.jsp">用户设置</a>
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
                    if (user.getGroup() == null || user.getMail() == null || user.getTel() == null
                            || user.getGroup().equals("") || user.getMail().equals("") || user.getTel().equals("")) {
        %>
        <div id="alertContainer" class="container col-sm-12">
            <div id="alertContainer" class="alert alert-danger">
                <button type="button" class="close" data-dismiss="alert">×</button>
                请完善你的信息！
            </div>
        </div>
        <%} else {%>
        <div id="alertContainer" class="container col-sm-12"></div>
        <%} %>
        <div class="container" style="margin-top: 10px">
            <div class="row">
                <div class="col-sm-5 padding-14">
                    <form class='form-horizontal' id= 'user-form' >
                        <fieldset>
                            <div style='display:none'>
                                <input type="text" class="input-large field" id="id" name="id"
                                       value="<%=user.getId()%>">
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="userName">用户名</label>

                                <div class="controls">
                                    <input type="text" readonly class="input-large field" id="userName" name="userName"
                                           value="<%=currentUser%>">
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="groupName">组名</label>

                                <div class="controls">
                                    <input type="text" class="input-large field" id="groupName" name="groupName"
                                           value="<%=user.getGroup()%>">
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="email">邮件地址</label>

                                <div class="controls">
                                    <input type="text" class="input-large field" id="email" name="email"
                                           value="<%=user.getMail()%>">
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="tel">手机号码</label>

                                <div class="controls">
                                    <% if (user.getTel() == null) {%>
                                    <input type="text" class="input-large field" id="tel" name="tel" value="">
                                    <%} else { %>
                                    <input type="text" class="input-large field" id="tel" name="tel"
                                           value="<%=user.getTel()%>">
                                    <%} %>
                                </div>
                            </div>
                            <br>

                            <div class="control-group">
                                <div class="controls padding-10">
                                    <button type="submit" id="submit" class="btn btn-primary align-center">保存</button>
                                </div>
                            </div>

                        </fieldset>
                    </form>
                    <%
                            }
                        }
                    %>
                </div>
                <div class="col-sm-7" style="opacity: 0.5">


                    分组情况请参考下面的列表：<br/>
                    <ul>
                        <li>如果列表中没有你想要的分组，你可以填写一个组名，这个组名请尽可能地细化。</li>
                        <li>这个选项的重要性在于，你可以在作业的通知选项中，选择通知一个组的人。</li>
                        <li>并且同组的人可以操作彼此的作业。</li>
                    </ul>

                    <table class="table table-striped table-bordered table-condensed">
                        <tr>
                            <th align="left" width="15%">组名</th>
                            <th align="left" width="85%">成员</th>
                        </tr>
                        <%
                            for (String group : map.keySet()) {
                        %>
                        <tr>
                            <td align="left"><%=group%>
                            </td>
                            <td align="left"><%=map.get(group)%>
                            </td>
                        </tr>
                        <% }%>

                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<a href="#" class="scrollup" style="display: inline;">
    <img src="img/betop.png" width="66" height="67">
</a>
<script type="text/javascript">
    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('.scrollup').fadeIn();
        } else {
            $('.scrollup').fadeOut();
        }
    });

    $('.scrollup').click(function () {
        $("html, body").scrollTop(0);
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
<script src="js/user.js" type="text/javascript"></script>


</body>

</html>