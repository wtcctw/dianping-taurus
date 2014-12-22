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
    <link rel="stylesheet" href="css/jquery-ui.min.css"/>

    <!-- ace styles -->

    <link rel="stylesheet" href="lib/ace/css/ace.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-skins.min.css"/>
    <link href="css/bwizard.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="css/common.css">
        <style>
        label.error{margin-left: 10px; color: red;}
        label.success{margin-left: 10px; color: green;}
    </style>
</head>
<body>
<%@page import="com.dp.bigdata.taurus.restlet.resource.IPoolsResource" %>
<%@page import="com.dp.bigdata.taurus.restlet.resource.IAttemptStatusResource" %>
<%@page import="com.dp.bigdata.taurus.restlet.resource.IUserGroupsResource" %>
<%@page import="com.dp.bigdata.taurus.restlet.resource.IHostsResource" %>

<%@page import="com.dp.bigdata.taurus.restlet.shared.PoolDTO" %>
<%@page import="com.dp.bigdata.taurus.restlet.shared.StatusDTO" %>
<%@page import="com.dp.bigdata.taurus.restlet.shared.UserGroupDTO" %>
<%@page import="com.dp.bigdata.taurus.restlet.shared.HostDTO" %>

<%
    cr = new ClientResource(host + "pool");
    IPoolsResource poolResource = cr.wrap(IPoolsResource.class);
    cr.accept(MediaType.APPLICATION_XML);
    ArrayList<PoolDTO> pools = poolResource.retrieve();
    int UNALLOCATED = 1;

    cr = new ClientResource(host + "host");
    IHostsResource hostResource = cr.wrap(IHostsResource.class);
    cr.accept(MediaType.APPLICATION_XML);
    ArrayList<HostDTO> hosts = hostResource.retrieve();

    cr = new ClientResource(host + "status");
    IAttemptStatusResource attemptResource = cr.wrap(IAttemptStatusResource.class);
    cr.accept(MediaType.APPLICATION_XML);
    ArrayList<StatusDTO> statuses = attemptResource.retrieve();

    cr = new ClientResource(host + "group");
    IUserGroupsResource groupResource = cr.wrap(IUserGroupsResource.class);
    cr.accept(MediaType.APPLICATION_XML);
    ArrayList<UserGroupDTO> groups = groupResource.retrieve();
    String name = request.getParameter("appname");
    String path = request.getParameter("path");
    String ip = request.getParameter("ip");
    if (name == null) {
        name = "";
    }
    if (ip == null) {
        ip = "";
    }
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
        <div class="pull-right" style="margin:10px;color: white;">
            <a target="_blank" style="margin:10px;color: white;"  href="http://shang.qq.com/wpa/qunwpa?idkey=6a730c052b1b42ce027179ba1f1568d0e5e598c456ccb6798be582b9a9c931f7"><img border="0" src="img/group.png" width="20" height="20" alt="Taurus后援团" title="Taurus后援团">点我加入Taurus后援团 155326270</a>
        </div>

        <div class="pull-right ng-binding" style="margin:10px;color: white;" ng-bind="monitorMessage"><i class="icon-user-md">开发者：李明  <a target="_blank" style="margin:10px;color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="img/qq.png"  width="20" height="20" color="white" alt="点我报错" title="点我报错"/>点我报错</a></i> <i class="icon-phone">: 13661871541</i></div>

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
            <a href="#" class="dropdown-toggle">
                <i class="icon-dashboard"></i>
                <span class="menu-text" id="userrolechange">监控中心</span>
                <b class="icon-angle-down"></b>
            </a>
            <ul class="submenu">
                <li  id="monitor_center">
                    <a href="index.jsp">
                        <i class="menu-icon icon-caret-right"></i>
                        我的任务
                    </a>

                </li>

                <li id="task_center">
                    <a href="task_center.jsp">
                        <i class="menu-icon icon-caret-right"></i>
                        所有任务
                    </a>


                </li>
                <li id="host_center">
                    <a href="host_center.jsp">
                        <i class="menu-icon icon-caret-right"></i>
                        主机负载
                    </a>

                </li>
            </ul>

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
        <li id="hosts">
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

        <li id="power">
            <a href="#" target="_self">
                <span class="menu-text" style="padding-left: 10px"> ©&nbsp;&nbsp;&nbsp;&nbsp;点评工具组 </span>
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

<div class="main-content" style="opacity: 1;">

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
            <a href="task.jsp">新建任务</a>
        </li>
    </ul>
</div>
<div class="page-content">
<div id="wizard">
<ol>
    <li>作业部署</li>
    <li>基本设置</li>
    <li>其他设置</li>
</ol>
<div id="deploy">
    <form id="deploy-form" class="form-horizontal">
        <fieldset>
            <legend>部署设置</legend>
            <div class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-1" for="taskType">作业类型*</label>

                <div class="controls col-sm-10">
                    <select id="taskType" name="taskType" class="input-big  field" style="width: 300px">
                        <option>default</option>
                        <option>hadoop</option>
                    </select>
                    <a href="about.jsp#config" class="atip" data-toggle="tooltip" data-placement="top"
                       data-original-title="hadoop: 需要访问hadoop的作业。这种类型的作业，taurus会管理作业的hadoop ticket的申请和销毁。
                            default: 上述两种类型以外所有类型。">帮助</a>
                </div>
            </div>
            <br>

            <div id="jarAddress" style="display:none;">
                <div class="control-group">
                    <label class="label label-lg label-info arrowed-right col-sm-1" for="taskUrl">Jar包ftp地址*</label>

                    <div class="controls col-sm-10">
                        <input type="text" class="input-xxlarge field" id="taskUrl" name="taskUrl"
                               placeholder="ftp://10.1.1.81/{project-name}/{date}/{jarName}" style="width: 300px">
                    </div>
                </div>
            </div>
            <br>

            <div id="hadoopName" style="display:none;">
                <div class="control-group">
                    <label class="label label-lg label-info arrowed-right col-sm-2" for="hadoopName">hadoop用户名*</label>

                    <div class="controls col-sm-9">
                        <input type="text" class="input-large field" id="hadoopName" name="hadoopName"
                               placeholder="kerberos principle (wwwcron)">
                        <a href="about.jsp#config" class="atip" data-toggle="tooltip" data-placement="top"
                           data-original-title=" hadoop类型的作业，需要提供一个用于访问hadoop的principle name。
                                为此，taurus需要读取这个principle的keytab文件，一般情况下这个keytab已经放到相应的目录。
                                如果你不确定这一点，请联系我们。">帮助</a>

                    </div>
                    <br>
                    <br>
                </div>
            </div>

            <div id="host" class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-1" for="hostname">部署的机器*</label>

                <div class="controls col-sm-10">
                    <select id="hostname" name="hostname" class="input-big field" style="width: 300px">
                        <%
                            if (ip != null)
                        %>
                        <option selected="selected"><%=ip%>
                        </option>
                        <%
                        %>
                        <%

                            for (HostDTO hostip : hosts) {
                                if (hostip.isConnected()) {
                        %>

                        <option><%=hostip.getIp()%>
                        </option>
                        <% }
                        }
                        %>

                    </select>
                    <a class="atip" data-toggle="tooltip" data-placement="top"
                       data-original-title="如果你要部署的主机ip不在这里，说明agent机器出现了故障或者主机ip上没有部署agent，请联系运维哥哥">提示</a>
                </div>
            </div>
        </fieldset>
    </form>
</div>

<div id="base">
    <form id="basic-form" class="form-horizontal">
        <fieldset>
            <legend>必要设置</legend>
            <div class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-2" for="taskName">名称*</label>

                <div class="controls col-sm-9">
                    <input type="text" class="input-xxlarge field" id="taskName" name="taskName" value="<%=name%>"
                           placeholder="作业的名称，可以作为被依赖的对象，不可修改">
                </div>
                <br>
                <br>
            </div>
            <div class="control-group" style='display:none'>
                <label class="label label-lg label-info arrowed-right col-sm-2" for="taskName">应用名称*</label>

                <div class="controls controls col-sm-9">
                    <input type="text" class="input-xxlarge field" id="appName" name="appName" value="<%=name%>">
                </div>
                <br>
                <br>
            </div>
            <div id="mainClassCG" class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-2" for="mainClass">MainClass*</label>

                <div class="controls controls col-sm-9">
                    <input type="text" class="input-xxlarge field required" id="mainClass" name="mainClass"
                           placeholder="mainClass">
                </div>
                <br>
                <br>
            </div>
            <div class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-2" for="crontab">Crontab*</label>

                <div class="controls col-sm-9">
                    <input type="text" class="input-xxlarge field" id="crontab" name="crontab" value="0 0 * * ?">
                    <a href="about.jsp#crontab">帮助</a>
                </div>
                <br>
                <br>

            </div>

            <div class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-2" for="taskCommand">命令*</label>

                <div class="controls col-sm-9">
                    <input type="text" class="input-xxlarge field" id="taskCommand" name="taskCommand"
                           placeholder="执行作业的命令,命令结尾不要使用'&'或';'">
                    <a class="atip" data-toggle="tooltip" data-placement="top"
                       data-original-title="注意，命令结尾不要使用'&'或';' 否则会失败哦～ 请保持命令结尾没有这些字符">提示</a>
                    <%
                        if (path != null && !path.equals("")) {
                    %>
                    <br/><span>提示:已部署的作业文件的路径为<%=path%></span>
                    <%}%>
                </div>
                <br>
                <br>
            </div>

            <div id="beanCG" class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-2" for="taskCommand"></label>

                <div class="controls col-sm-9">
                    <button id="addNewBeanbtn" class="btn btn-small">增加Bean</button>
                    <button id="rmBeanbtn" class="btn btn-small" disabled>删除Bean</button>
                </div>
                <br>
                <br>
            </div>

            <div class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-2" for="proxyUser">运行身份（不可为root）*</label>

                <div class="controls col-sm-9" id="hadoopUser">
                    <input type="text" class="input-xxlarge field" id="proxyUser" name="proxyUser"
                           placeholder="执行作业的用户身份">
                </div>

                <div class="controls col-sm-9" id="defaultUser">
                    <input type="text" class="input-xxlarge field" id="proxyUser" name="proxyUser"
                           placeholder="执行作业的用户身份" value="nobody" disabled="disabled">
                    <a class="atip" data-toggle="tooltip" data-placement="top"
                       data-original-title="如果你非要以其他身份运行，请联系李明【kirin.li@dianping.com】">帮助</a>
                </div>
                <br>
                <br>
            </div>
            <div class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-2" for="description">描述*</label>

                <div class="controls col-sm-9">
                    <input type="text" class="input-xxlarge field" id="description" name="description"
                           placeholder="请尽可能用中文描述作业的用途">
                </div>
                <br>
                <br>
            </div>
            <input type="text" class="field" style="display:none" id="creator" name="creator" value="<%=currentUser%>">
        </fieldset>
    </form>
</div>
<div id="extention">
    <form id="extended-form" class="form-horizontal">
        <fieldset>
            <legend>可选设置</legend>
            <div class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-2"
                       for="maxExecutionTime">最长执行时间（分钟）*</label>

                <div class="controls">
                    <input type="number" class="input-small field" id="maxExecutionTime" name="maxExecutionTime"
                           style="text-align:right" value=60>
                    <a href="about.jsp#config">帮助</a>
                </div>
            </div>
            <div class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-2" for="dependency">依赖</label>

                <div class="controls">
                    <input type="text" class="input-large field" id="dependency" name="dependency"
                           placeholder="dependency expression" value="">
                    <a href="about.jsp#config">帮助</a>
                </div>
            </div>
            <div class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-2" for="maxWaitTime">最长等待时间（分钟）*</label>

                <div class="controls">
                    <input type="number" class="input-small field" id="maxWaitTime" name="maxWaitTime"
                           style="text-align:right" value=60>
                    <a href="about.jsp#config">帮助</a>
                </div>
            </div>

            <div class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-2" for="retryTimes">重试次数*</label>

                <div class="controls">
                    <input type="number" class="input-small field" id="retryTimes" name="retryTimes"
                           style="text-align:right" value=0>
                    <a href="about.jsp#config">帮助</a>
                </div>
            </div>
            <div class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-2" for="multiInstance">自动kill
                    timeout实例*</label>

                <div class="controls field" id="isAutoKill">
                    <input type="radio" value="1" name="isAutoKill" checked> 是
                    <input type="radio" value="0" name="isAutoKill"> 否
                    <span class="label">不要轻易修改，除非你确定其含义：<a href="about.jsp#config">帮助</a></span>
                </div>
            </div>
            <br/>
            <br/>

            <p><span class="label">注意：使用以下配置项，你需要在用户<a href="user.jsp">用户设置</a>填写您的联系方式</span></p>

            <div class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-2">选择何时收到报警</label>

                <div class="controls">
                    <%
                        for (StatusDTO status : statuses) {
                            if (status.getStatus().equals("FAILED") || status.getStatus().equals("TIMEOUT")) {
                    %>
                    <label><input type="checkbox" class="ace ace-checkbox-2 field alertCondition" id="alertCondition"
                                  name="<%=status.getStatus()%>" checked="checked"><span
                            class="lbl"> <%=status.getCh_status()%></span></label>
                    <%
                    } else {
                    %>
                    <label><input type="checkbox" class="ace ace-checkbox-2 field alertCondition" id="alertCondition"
                                  name="<%=status.getStatus()%>"> <span
                            class="lbl"> <%=status.getCh_status()%></span></label>
                    <%
                            }
                        }
                    %>
                </div>
            </div>

            <div class="control-group col-sm-12 no-padding-left">
                <label class="label label-lg  label-info arrowed-right col-sm-2" for="alertType">选择报警方式</label>

                <div class="controls">
                    <select class="input-small field" id="alertType" name="alertType">
                        <option id="1">邮件</option>
                        <option id="2">短信</option>
                        <option id="3">邮件和短信</option>
                    </select>
                </div>
            </div>

            <div class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-2">选择报警接收人(分号分隔)</label>

                <div class="controls">
                    <input type="text" class="input-large field" id="alertUser" name="alertUser"
                           value="<%=(String)session.getAttribute(com.dp.bigdata.taurus.web.servlet.LoginServlet.USER_NAME)%>;">
                </div>
            </div>

            <div class="control-group">
                <label class="label label-lg label-info arrowed-right col-sm-2">选择报警接收组(分号分隔)</label>

                <div class="controls">
                    <input type="text" class="input-large field" id="alertGroup" name="alertGroup"
                           placeholder="group name split with ;">
                </div>
            </div>
        </fieldset>
    </form>
</div>
</div>
</div>
<div class="feedTool">

    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="img/qq.png"  width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img border="0" src="img/x_alt.png"  width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>
    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>
<div id="confirm" class="modal fade" role="dialog"
     aria-hidden="true">
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
<script type="text/javascript">
    var userList = "", groupList = "", ipList = "";
    <%for(UserDTO user:users) {%>
    userList = userList + ",<%=user.getName()%>";
    <%}%>
    <%for(UserGroupDTO group:groups) {%>
    groupList = groupList + ",<%=group.getName()%>";
    <%}%>
    <% for(HostDTO hostDto:hosts) {%>
    ipList = ipList + ",<%=hostDto.getName()%>";
    <%}%>
    ipList = ipList.substr(1);
    userList = userList.substr(1);
    groupList = groupList.substr(1);
    $(".atip").tooltip();
    options = {
        delay: { show: 500, hide: 100 },
        trigger: 'click'
    };

    $(".optiontip").tooltip(options);
</script>
<script src="js/bwizard.js" type="text/javascript"></script>
<script src="lib/ace/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="js/taurus_validate.js" type="text/javascript"></script>
<script src="js/task.js" type="text/javascript"></script>
</body>
</html>