<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<html lang="en">
<head>
    <title>Taurus</title>
    <meta charset="utf-8">
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <%@ include file="jsp/common-nav.jsp"%>
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
    <script type="text/javascript" src="js/login.js"></script>
    <!-- page specific plugin styles -->

    <!-- fonts -->
    <script src="lib/ace/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/ace-fonts.css"/>

    <!-- ace styles -->

    <link rel="stylesheet" href="lib/ace/css/ace.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-skins.min.css"/>

	<link href="css/index.css" rel="stylesheet" type="text/css">
</head>
<body >

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
                <a href="update.jsp">更新日志</a>
            </li>
        </ul>
    </div>

	<div class="page-content ">

			<div class="row ">
                <div class="col-sm-12 padding-14">
                <div class="page-header">
                    <h1>服务器更新日志</h1>
                </div>
					<table class="table table-striped table-bordered table-condensed ">
						<tbody>

							<tr class="text-success">
								<th width="90%">最新发布功能描述</th>
								<th width="10%">发布时间</th>
							</tr>
                            <tr>
                                <td>优化界面访问速度</td>
                                <td>2014-11-06</td>
                            </tr>
                            <tr>
                                <td>新增查看历史数据</td>
                                <td>2014-11-06</td>
                            </tr>
                            <tr>
                                <td>新增定期和手工清理zookeeper节点功能</td>
                                <td>2014-11-05</td>
                            </tr>
                            <tr>
                                <td>新增 <a href="cronbuilder.jsp">cron 生成器</a></td>
                                <td>2014-10-28</td>
                            </tr>
                            <tr>
                                <td>新增job主机负载内存监控</td>
                                <td>2014-10-20</td>
                            </tr>
                            <tr>
                                <td>新增组任务情况监控</td>
                                <td>2014-10-15</td>
                            </tr>
                            <tr>
                                <td>新版Taurus界面，新增我的任务</td>
                                <td>2014-10-09</td>
                            </tr>
                            <tr>
                                <td>job host监控细化，<a href="hosts.jsp">主机监控</a></td>
                                <td>2014-09-05</td>
                            </tr>
                            <tr>
                                <td>agent war包化，可以通过button部署</td>
                                <td>2014-09-01</td>
                            </tr>
                            <tr>
                                <td>新增实时查看正在运行的任务日志</td>
                                <td>2014-08-05</td>
                            </tr>
                            <tr>
                                <td>新增任务监控<a href="monitor.jsp">任务监控</a>页面</td>
                                <td>2014-08-03</td>
                            </tr>
							<tr>
								<td>重新组织了<a href="about.jsp">帮助</a>页面</td>
								<td>2013-09-27</td>
							</tr>
							<tr>
								<td>增加了<a href="user.jsp">用户设置</a>页面</td>
								<td>2013-09-27</td>
							</tr>
							<tr>
								<td>用户可以看到同组的所有人的作业</td>
								<td>2013-09-27</td>
							</tr>
							<tr>
								<td>修复了description显示为null和修改告警人的bug</td>
								<td>2013-09-27</td>
							</tr>
							<tr>
								<td>支持自动杀死Timeout作业。具体细节请看<a href="about.jsp#config">帮助</a></td>
								<td>2013-08-20</td>
							</tr>
							<tr>
								<td>修正Bugs。对杀死的作业设置返回值为-1。</td>
								<td>2013-08-20</td>
							</tr>
							<tr>
								<td>增加Cat打点</td>
								<td>2013-08-20</td>
							</tr>
						</tbody>
					</table>
                <div class="page-header padding-14">
                    <h1>开发者</h1>
                </div>
					<table class="table table-striped table-bordered table-condensed ">
						<tbody>
                            <tr>
                                <td>李明</td>
                                <td>kirin.li@dianping.com</td>
                                <td>13661871541</td>
                            </tr>
						</tbody>
					</table>

			</div>
	</div>
    </div>
    </div>
<script>
    $(document).ready(function() {
        $('li[id="update"]').addClass("active");
        $('#menu-toggler').on(ace.click_event, function() {
            $('#sidebar2').toggleClass('display');
            $(this).toggleClass('display');
            return false;
        });

    });
</script>
</body>
</html>
