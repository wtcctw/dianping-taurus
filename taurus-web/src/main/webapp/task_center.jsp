<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.Time" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>Taurus</title>
    <meta charset="utf-8">
    <meta name="description" content="overview &amp; stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <%@ include file="jsp/common-nav.jsp" %>

    <!-- bootstrap & fontawesome -->
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
    <script type="text/javascript" src="lib/ace/js/jquery.flot.min.js"></script>
    <script type="text/javascript" src="lib/ace/js/jquery.flot.pie.min.js"></script>
    <script type="text/javascript" src="lib/ace/js/bootstrap-datepicker.min.js"></script>
    <script type="text/javascript" src="lib/ace/js/daterangepicker.min.js"></script>
    <script src="lib/ace/js/jquery.dataTables.min.js"></script>
    <script src="lib/ace/js/jquery.dataTables.bootstrap.js"></script>
    <script type="text/javascript" src="js/login.js"></script>
    <script type="text/javascript" src="js/task_center.js"></script>
    <script src="js/jquery.datetimepicker.js"></script>
    <script src="lib/dist/echarts.js"></script>
    <script src="lib/dist/chart/gauge.js"></script>
    <script src="lib/dist/theme/macarons.js"></script>

    <!-- page specific plugin styles -->

    <!-- fonts -->
    <script src="lib/ace/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/ace-fonts.css"/>

    <!-- ace styles -->

    <link rel="stylesheet" href="lib/ace/css/ace.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-skins.min.css"/>
    <link rel="stylesheet" href="css/jquery.datetimepicker.css"/>

    <link rel="stylesheet" href="resource/css/monitor-center.css">
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/loading.css">
    <style>
        .dayreport {
            float: left
        }

        .tip {
            text-align: center;
            margin: 0 auto;

        }

        .historyreport {
            float: right
        }

    </style>

</head>
<body>
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
            <a target="_blank" style="margin:10px;color: white;"
               href="http://shang.qq.com/wpa/qunwpa?idkey=6a730c052b1b42ce027179ba1f1568d0e5e598c456ccb6798be582b9a9c931f7"><img
                    border="0" src="img/group.png" width="20" height="20" alt="Taurus后援团" title="Taurus后援团">点我加入Taurus后援团
                155326270</a>
        </div>

        <div class="pull-right ng-binding" style="margin:10px;color: white;" ng-bind="monitorMessage"><i
                class="icon-user-md">开发者：李明 <a target="_blank" style="margin:10px;color: white;"
                                               href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img
                border="0" src="img/qq.png" width="20" height="20" color="white" alt="点我报错" title="点我报错"/>点我报错</a></i>
            <i class="icon-phone">: 13661871541</i></div>

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
        <li id="power">
            <a href="#" target="_self">
                <span class="menu-text" style="padding-left: 10px"> ©&nbsp;&nbsp;&nbsp;&nbsp;点评工具组 </span>
            </a>
        </li>

    </ul>
    <!-- /.nav-list -->


</div>

<script>
    var isAdmin = <%=isAdmin%>;
    if (!isAdmin) {
        $("#userrolechange").html("我的任务");
    }


</script>


<div class="main-content  " style="opacity: 1;">
<div class="breadcrumbs" id="breadcrumbs">
    <script type="text/javascript">
        try {
            ace.settings.check('breadcrumbs', 'fixed')
        } catch (e) {
        }
    </script>
    <ul class="breadcrumb">
        <li class="active">
            <i class="icon-home home-icon"></i>
            <a href="index.jsp">HOME</a>
        </li>
    </ul>
    <div class="hide" style="float:right;padding-right: 20px">
        <label>开始：</label><input type="text" id="startTime"/>
        <label>结束：</label><input type="text" id="endTime"/>
        <a class="btn btn-primary btn-small" href='#' onClick="reflash_view()"><i class="icon-eye-open">查看</i></a>
    </div>
</div>
<div class="page-content">
<div class="row">
<div class="time_inal ">
    <%
        Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        long hourTime = 60 * 60 * 1000;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String step_str = request.getParameter("step");
        String now = request.getParameter("date");
        System.out.println(step_str + "#" + now);
        int step = -24;
    %>

    <div class="col-sm-5">
        <a class="atip" data-toggle="tooltip" data-placement="top"
           data-original-title="查看一天内的数据">[天] </a>
        &nbsp;&nbsp;|&nbsp;&nbsp;
        <a class="atip"
                <% if (now == null) {
                    now = df.format(time);
                }
                    step = -720;
                %>
           href="task_center.jsp?step=<%=step%>&op=day&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%> "
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[<%=formatter.format(new Date(df.parse(now).getTime() + step*hourTime)) %>~<%=formatter.format(new Date(df.parse(now).getTime()))%>]">[-1m] </a>
        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip"
                <% if (now == null) {
                    now = df.format(time);
                }
                    step = -168;
                %>
           href="task_center.jsp?step=<%=step%>&op=day&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%> "
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[<%=formatter.format(new Date(df.parse(now).getTime() + step*hourTime))%>
    ~<%=formatter.format(new Date(df.parse(now).getTime()))%>]">[-1w] </a>
        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip"
                <% if (now == null) {
                    now = df.format(time);
                }
                    step = -24;
                %>
           href="task_center.jsp?step=<%=step%>&op=day&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%>"
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[<%=formatter.format(new Date(df.parse(now).getTime() + step*hourTime))%>
    ~<%=formatter.format(new Date(df.parse(now).getTime()))%>]">[-1d] </a>

        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip"  <% if (now == null) {
            now = df.format(time);
        }
            step = 24;
            if (df.parse(now).after(time)) {%>
           href="task_center.jsp?step=-24&date＝<%=df.format(time)%> "
                <% } else {%>
           href="task_center.jsp?step=<%=step%>&op=day&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%> "
                <% }
                %>
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[<%=formatter.format(new Date(df.parse(now).getTime()))%>~<%=formatter.format(new Date(df.parse(now).getTime() + step*hourTime))%>]">[+1d] </a>
        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip"  <% if (now == null) {
            now = df.format(time);
        }
            step = 168;
            if (df.parse(now).after(time)) {%>
           href="task_center.jsp?step=-24&op=day&date＝<%=df.format(time)%> "
                <% } else {%>
           href="task_center.jsp?step=<%=step%>&op=day&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%> "
                <% }
                %>
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[<%=formatter.format(new Date(df.parse(now).getTime()))%>~<%=formatter.format(new Date(df.parse(now).getTime() + step*hourTime))%>]">[+1w] </a>
        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip"  <% if (now == null) {
            now = df.format(time);
        }
            step = 720;
            if (df.parse(now).after(time)) {%>
           href="task_center.jsp?step=-24&op=day&date＝<%=df.format(time)%> "
                <% } else {%>
           href="task_center.jsp?step=<%=step%>&op=day&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%> "
                <% }
                %>
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[<%=formatter.format(new Date(df.parse(now).getTime()))%>~<%=formatter.format(new Date(df.parse(now).getTime() + step*hourTime))%>]">[+1m] </a>
    </div>

    <div class="col-sm-2">
        <a class="atip" data-toggle="tooltip" data-placement="top"
           data-original-title="当你点击了[-1h]|[-1d]|[-1w]|[-1m]后，在想切换到当前页面时，请点击[当天]，刷新页面无效噢～">[注意] </a>
        &nbsp;&nbsp;|&nbsp;&nbsp;


        <a class="atip"
           href="task_center.jsp?step=<%=step%>&op=day&date=<%=df.format(new Date())%>"
           data-toggle="tooltip" data-placement="top"
           data-original-title=" 时间区间[<%=formatter.format(new Date(new Date().getTime() -24*hourTime))%>~<%=formatter.format(new Date())%>]">[当天] </a>
    </div>
    <div class="col-sm-5 historyreport">

        <a class="atip" data-toggle="tooltip" data-placement="top"
           data-original-title="查看到今天所有的数据">[历史数据] </a>
        &nbsp;&nbsp;|&nbsp;&nbsp;
        <a class="atip"
                <% if (now == null) {
                    now = df.format(time);
                }
                    step = -720;
                %>
           href="task_center.jsp?step=<%=step%>&op=history&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%> "
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[<%=formatter.format(new Date(df.parse(now).getTime() + step*hourTime)) %>~<%=formatter.format(new Date())%>]">[-1m] </a>
        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip"
                <% if (now == null) {
                    now = df.format(time);
                }
                    step = -168;
                %>
           href="task_center.jsp?step=<%=step%>&op=history&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%> "
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[<%=formatter.format(new Date(df.parse(now).getTime() + step*hourTime))%>
    ~<%=formatter.format(new Date())%>]">[-1w] </a>
        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip"
                <% if (now == null) {
                    now = df.format(time);
                }
                    step = -24;
                %>
           href="task_center.jsp?step=<%=step%>&op=history&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%>"
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[<%=formatter.format(new Date(df.parse(now).getTime() + step*hourTime))%>
    ~<%=formatter.format(new Date())%>]">[-1d] </a>
        &nbsp;&nbsp; |&nbsp;&nbsp;


        <a class="atip"  <% if (now == null) {
            now = df.format(time);
        }
            step = 24;
            if (df.parse(now).after(time)) {%>
           href="task_center.jsp?step=-24&date＝<%=df.format(time)%> "
                <% } else {%>
           href="task_center.jsp?step=<%=step%>&op=history&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%> "
                <% }
                %>
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[<%=formatter.format(new Date(df.parse(now).getTime() + step*hourTime))%>~<%=formatter.format(new Date())%>]">[+1d] </a>
        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip"  <% if (now == null) {
            now = df.format(time);
        }
            step = 168;
            if (df.parse(now).after(time)) {%>
           href="task_center.jsp?step=-24&op=history&date＝<%=df.format(time)%> "
                <% } else {%>
           href="task_center.jsp?step=<%=step%>&op=day&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%> "
                <% }
                %>
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[<%=formatter.format(new Date(df.parse(now).getTime() + step*hourTime))%>~<%=formatter.format(new Date())%>]">[+1w] </a>
        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip"  <% if (now == null) {
            now = df.format(time);
        }
            step = 720;
            if (df.parse(now).after(time)) {%>
           href="task_center.jsp?step=-24&op=history&date＝<%=df.format(time)%> "
                <% } else {%>
           href="task_center.jsp?step=<%=step%>&op=history&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%> "
                <% }
                %>
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[<%=formatter.format(new Date(df.parse(now).getTime() + step*hourTime))%>~<%=formatter.format(new Date())%>]">[+1m] </a>
    </div>
</div>


<div id="total">
    <div class="col-sm-12">
        <div class="col-sm-6">
            <div class="widget-box">
                <div class="widget-header header-color-blue3">
                    <h5 class="widget-title">
                        <i class="icon-dashboard"></i>
                        任务仪表盘
                    </h5>

                    <div class="widget-toolbar">
                        <a href="#" data-action="collapse">
                            <i class="icon-chevron-up"></i>
                        </a>
                    </div>
                </div>

                <div class="widget-body">
                    <div class="widget-main" id="total-chart" style="width: 499px;height: 319px">
                        <div class="loadIcon">
                            <div></div>
                            <div></div>
                            <div></div>
                            <div></div>
                        </div>
                    </div>
                    <!-- /.widget-main -->
                </div>
                <!-- /.widget-body -->
            </div>
            <!-- /.widget-box -->

        </div>
        <div class="col-sm-6">
            <div class="widget-box">
                <div class="widget-header header-color-red3">
                    <h5 class="widget-title">
                        <i class="icon-dashboard"></i>
                        所有的任务成功率
                    </h5>

                    <div class="widget-toolbar">
                        <a href="#" data-action="collapse">
                            <i class="icon-chevron-up"></i>
                        </a>
                    </div>
                </div>

                <div class="widget-body">
                    <div class="widget-main" id="total-widget-main">

                        <div id="totaltasks">
                            <div class="loadIcon">
                                <div></div>
                                <div></div>
                                <div></div>
                                <div></div>
                            </div>
                        </div>

                        <div class="hr hr8 hr-double"></div>

                        <div class="clearfix">
                            <div class="col-sm-4">
														<span class="grey">
															<i class="icon-lightbulb green"></i>
															&nbsp; 成功
														</span>
                                <h4 class="smaller pull-right" id="totalsucctask"></h4>
                            </div>

                            <div class="col-sm-4">
														<span class="grey">
															<i class="icon-lightbulb red"></i>
															&nbsp; 失败
														</span>
                                <h4 class="smaller pull-right " id="totalfailtask"></h4>
                            </div>
                            <div class="col-sm-4">
														<span class="grey">
															<i class="icon-lightbulb red"></i>
															&nbsp; 杀死
														</span>
                                <h4 class="smaller pull-right " id="totalkilltask"></h4>
                            </div>
                            <div class="col-sm-4">
														<span class="grey">
															<i class="icon-lightbulb btn-yellow"></i>
															&nbsp; 超时
														</span>
                                <h4 class="smaller pull-right " id="totaltimouttask"></h4>
                            </div>
                            <div class="col-sm-4">
														<span class="grey">
															<i class="icon-lightbulb"></i>
															&nbsp; 拥堵
														</span>
                                <h4 class="smaller pull-right " id="totalcongesttask"></h4>
                            </div>

                            <!-- /section:custom/extra.grid -->
                        </div>
                    </div>
                    <!-- /.widget-main -->
                </div>
                <!-- /.widget-body -->
            </div>
            <!-- /.widget-box -->

        </div>
        <div class="col-sm-12">
            <div class="widget-box">
                <div class="widget-header header-color-green3">
                    <h5 class="widget-title">
                        <i class=" icon-list-alt"></i>
                        所有任务的执行详情
                    </h5>

                    <div class="widget-toolbar">

                        <a href="#" data-action="collapse">
                            <i class="icon-chevron-up"></i>
                        </a>
                    </div>
                </div>

                <div class="widget-body">

                    <div class="widget-main " style="height: 550px" id="totaltasklist">
                        <div class="loadIcon">
                            <div></div>
                            <div></div>
                            <div></div>
                            <div></div>
                        </div>
                    </div>
                    <!-- /.widget-main -->
                </div>
                <!-- /.widget-body -->
            </div>
            <!-- /.widget-box -->

        </div>
    </div>

</div>
<div class="col-sm-12">
    <div class="col-sm-6">
        <div class="widget-box">
            <div class="widget-header header-color-grey">
                <h5 class="widget-title">
                    <i class="icon-fire"></i>
                    执行任务总数排行
                </h5>

                <div class="widget-toolbar">
                    <a href="#" data-action="collapse">
                        <i class="icon-chevron-up"></i>
                    </a>
                </div>
            </div>

            <div class="widget-body">


                <div class="widget-main align-center" style="height: 560px" id="totalJob">
                    <div class="loadIcon">
                        <div></div>
                        <div></div>
                        <div></div>
                        <div></div>
                    </div>
                </div>
                <!-- /.widget-main -->
            </div>
            <!-- /.widget-body -->
        </div>
        <!-- /.widget-box -->

    </div>
    <div class="col-sm-6">
        <div class="widget-box">
            <div class="widget-header header-color-dark">
                <h5 class="widget-title">
                    <i class=" icon-sort-by-order"></i>
                    执行任务失败总数排行
                </h5>

                <div class="widget-toolbar">
                    <a href="#" data-action="collapse">
                        <i class="icon-chevron-up"></i>
                    </a>
                </div>
            </div>

            <div class="widget-body">

                <div class="widget-main align-center" style="height: 560px" id="failedJob">

                    <div class="loadIcon">
                        <div></div>
                        <div></div>
                        <div></div>
                        <div></div>
                    </div>
                </div>
                <!-- /.widget-main -->
            </div>
            <!-- /.widget-body -->
        </div>
        <!-- /.widget-box -->

    </div>
</div>


</div>
</div>
</div>
<a href="#" class="scrollup" style="display: inline;">
    <img src="img/betop.png" width="66" height="67">
</a>

<div class="feedTool hide">

    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img
            border="0" src="img/qq.png" width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img
            border="0" src="img/x_alt.png" width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>

    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>
<script type="text/javascript">
    $('li[id="index"]').addClass("active open");
    $('li[id="task_center"]').addClass("active");
    $('#menu-toggler').on(ace.click_event, function () {
        $('#sidebar').toggleClass('display');
        $(this).toggleClass('display');
        return false;
    });

    // 基于准备好的dom，初始化echarts图表
    $(".atip").tooltip();
    options = {
        delay: { show: 500, hide: 100 },
        trigger: 'click'
    };
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


    $('#startTime').datetimepicker({
        formatTime:'H:i',
        format:'Y-m-d',
        formatDate:'Y-m-d',
        defaultTime:'10:00',
        timepicker:false,
        timepickerScrollbar:false
    });

    $('#endTime').datetimepicker({
        format:'Y-m-d',
        formatDate:'Y-m-d',
        defaultTime:'10:00',
        timepicker:false,
        timepickerScrollbar:false
    });


    function GetDateStr(dd, AddDayCount) {
        dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
        var y = dd.getFullYear();
        var m = dd.getMonth() + 1;//获取当前月份的日期
        var d = dd.getDate();
        return y + "-" + m + "-" + d;
    }
    <%String now_str = request.getParameter("date");
    if (now_str == null || now_str.isEmpty()){
    now_str= df.format(time);
    }
    %>
    var now_s = "<%=formatter.format( df.parse(now_str))%>";
    var now = new Date(Date.parse(now_s.replace(/-/g, "/")));
    var id = "<%=request.getParameter("step")%>";
    <%String op_str = request.getParameter("op");
    if(op_str==null || op_str.isEmpty()){
    op_str="day";
    }%>
    var op = "<%=op_str%>";



</script>
</body>
</html>