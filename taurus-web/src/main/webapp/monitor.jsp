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
    <link rel="stylesheet" href="css/common.css">
    <style>

        .time_inal {
            float: right
        }
    </style>
</head>
<body data-spy="scroll">
<%@page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

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
        <li id="power">
            <a href="#" target="_self">
                <i class="icon-copyright-mark"></i>
                <span class="menu-text"> © 点评工具组 </span>
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
        <tbody id="running_body">
        <div id="running_load">
            <i class="icon-spinner icon-spin icon-large"></i>
        </div>

        </tbody>
    </table>
</ul>
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

    <div>
        <a class="atip" data-toggle="tooltip" data-placement="top"
           data-original-title="当你点击了[-1h]|[-1d]|[-1w]|[-1m]后，在想切换到当前页面时，请点击[当天]，刷新页面无效噢～">[注意] </a>
        &nbsp;&nbsp;|&nbsp;&nbsp;


        <a class="atip"
           href="monitor.jsp?step=<%=step%>&op=day&date=<%=df.format(new Date())%>"
           data-toggle="tooltip" data-placement="top"
           data-original-title=" 时间区间[<%=formatter.format(new Date(new Date().getTime() -24*hourTime))%>~<%=formatter.format(new Date())%>]">[当天] </a>
        <a class="atip" data-toggle="tooltip" data-placement="top"
           data-original-title="查看历史数据">[历史模式] </a>
        &nbsp;&nbsp;|&nbsp;&nbsp;
        <a class="atip"
                <% if (now == null) {
                    now = df.format(time);
                }
                    step = -720;
                %>
           href="monitor.jsp?step=<%=step%>&op=day&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%> "
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[<%=formatter.format(new Date(df.parse(now).getTime() + step*hourTime)) %>~<%=formatter.format(new Date(df.parse(now).getTime()))%>]">[-1m] </a>
        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip"
                <% if (now == null) {
                    now = df.format(time);
                }
                    step = -168;
                %>
           href="monitor.jsp?step=<%=step%>&op=day&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%> "
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
           href="monitor.jsp?step=<%=step%>&op=day&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%>"
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[<%=formatter.format(new Date(df.parse(now).getTime() + step*hourTime))%>
    ~<%=formatter.format(new Date(df.parse(now).getTime()))%>]">[-1d] </a>

        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip"  <% if (now == null) {
            now = df.format(time);
        }
            step = 24;
            if (df.parse(now).after(time)) {%>
           href="monitor.jsp?step=-24&date＝<%=df.format(time)%> "
                <% } else {%>
           href="monitor.jsp?step=<%=step%>&op=day&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%> "
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
           href="monitor.jsp?step=-24&op=day&date＝<%=df.format(time)%> "
                <% } else {%>
           href="monitor.jsp?step=<%=step%>&op=day&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%> "
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
           href="monitor.jsp?step=-24&op=day&date＝<%=df.format(time)%> "
                <% } else {%>
           href="monitor.jsp?step=<%=step%>&op=day&date=<%=df.format(new Date(df.parse(now).getTime() + step*hourTime))%> "
                <% }
                %>
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[<%=formatter.format(new Date(df.parse(now).getTime()))%>~<%=formatter.format(new Date(df.parse(now).getTime() + step*hourTime))%>]">[+1m] </a>
    </div>


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
            <th>我要报错</th>
        </tr>
        </thead>
        <tbody id="submit_body">

        <div id="submit_load">
            <i class="icon-spinner icon-spin icon-large"></i>
        </div>

        </tbody>
    </table>
</ul>

<ul class="dependency-tag col-sm-12">
    <li><a class="atip" data-toggle="tooltip" data-placement="right" data-original-title="拥堵的任务"><span
            class="label label-important">DEPENDENCY_PASS</span></a></li>

</ul>

<ul class="breadcrumb col-sm-12">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover" id="dependency" style="width: 100%">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
            <th>IP</th>
            <th>我要报错</th>
        </tr>
        </thead>
        <tbody id="dependency_body">
        <div id="dependency_load">
            <i class="icon-spinner icon-spin icon-large"></i>
        </div>
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
            <th>我要报错</th>
        </tr>
        </thead>
        <tbody id="failed_body">
        <div id="failed_load">
            <i class="icon-spinner icon-spin icon-large"></i>
        </div>
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
            <th>查看日志</th>
            <th>我要报错</th>

        </tr>
        </thead>
        <tbody id="dependency_timeout_body">
        <div id="dependency_timeout_load">
            <i class="icon-spinner icon-spin icon-large"></i>
        </div>
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
            <th>查看日志</th>
            <th>我要报错</th>

        </tr>
        </thead>
        <tbody id="timeout_body">
        <div id="timeout_load">
            <i class="icon-spinner icon-spin icon-large"></i>
        </div>
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
<div class="modal fade" id="feedModal" role="dialog"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
        </div>
    </div>
</div>
<a href="#" class="scrollup" style="display: inline;">
    <img src="img/betop.png" width="66" height="67">
</a>
<div class="feedTool">

    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="img/qq.png"  width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img border="0" src="img/x_alt.png"  width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>
    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>
<script type="text/javascript">
$('li[id="monitor"]').addClass("active");
$('#menu-toggler').on(ace.click_event, function () {
    $('#sidebar').toggleClass('display');
    $(this).toggleClass('display');
    return false;
});
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
$(".atip").tooltip();
options = {
    delay: { show: 500, hide: 100 },
    trigger: 'click'
};
$(".optiontip").tooltip(options);
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
var id = "<%= request.getParameter("id")%>";
var step = "<%=request.getParameter("step")%>";
<%String op_str = request.getParameter("op");
if(op_str==null || op_str.isEmpty()){
op_str="day";
}%>
var op = "<%=op_str%>";
var starttime;
var endtime;
if (step == null || step == "null") {
    starttime = GetDateStr(now, -1);
    if (op == "day") {
        endtime = GetDateStr(now, 1);
    } else {
        endtime = GetDateStr(new Date(), 1);
    }

} else if (step == "-24") {
    starttime = GetDateStr(now, -1);
    if (op == "day") {
        endtime = GetDateStr(now, 1);
    } else {
        endtime = GetDateStr(new Date(), 1);
    }
} else if (step == "-168") {
    starttime = GetDateStr(now, -7);
    if (op == "day") {
        endtime = GetDateStr(now, 1);
    } else {
        endtime = GetDateStr(new Date(), 1);
    }
} else if (step == "-720") {
    starttime = GetDateStr(now, -30);
    if (op == "day") {
        endtime = GetDateStr(now, 1);
    } else {
        endtime = GetDateStr(new Date(), 1);
    }
} else if (step == "24") {
    if (op == "day") {
        starttime = GetDateStr(now, 0);
        endtime = GetDateStr(now, 1);
    } else {
        starttime = GetDateStr(now, 0);
        endtime = GetDateStr(new Date(), 1);
    }


} else if (step == "168") {
    if (op == "day") {
        starttime = GetDateStr(now, 0);
        endtime = GetDateStr(now, 7);
    } else {
        starttime = GetDateStr(now, 7);
        endtime = GetDateStr(new Date(), 1);
    }

} else if (step == "720") {
    if (op == "day") {
        starttime = GetDateStr(now, 0);
        endtime = GetDateStr(now, 30);
    } else {
        starttime = GetDateStr(now, 30);
        endtime = GetDateStr(new Date(), 1);
    }

} else {
    starttime = GetDateStr(now, -1);
    endtime = GetDateStr(now, 1);
}

jQuery(function ($) {

    jQuery.extend(jQuery.fn.dataTableExt.oSort, {
        "html-percent-pre": function (a) {
            var x = String(a).replace(/<[\s\S]*?>/g, "");    //去除html标记
            x = x.replace(/&nbsp;/ig, "");                   //去除空格
            x = x.replace(/MB/, "");                          //去除MB
            x = x.replace(/异常数据/, "0");                          //去除异常数据
            return parseFloat(x);
        },
        "html-percent-asc": function (a, b) {                //正序排序引用方法
            return ((a < b) ? -1 : ((a > b) ? 1 : 0));
        },
        "html-percent-desc": function (a, b) {                //倒序排序引用方法
            return ((a < b) ? 1 : ((a > b) ? -1 : 0));
        }
    });

    runningStyle = function () {
        $('#running').dataTable({
            "bAutoWidth": true,
            "bPaginate": false,
            "bFilter": true,
            "bInfo": false,
            "bLengthChange": false,
            "aoColumns": [
                { "sType": "html-percent", "aTargets": [1] },
                { "sType": "html-percent", "aTargets": [2] },
                { "sType": "html-percent", "aTargets": [3] },
                { "sType": "html-percent", "aTargets": [4] },
                { "sType": "html-percent", "aTargets": [5] },
                { "sType": "html-percent", "aTargets": [6] },
                { "sType": "html-percent", "aTargets": [7] }
            ]
        });
    };
    submitfailStyle = function () {
        $('#submitfail').dataTable({
            "bAutoWidth": true,
            "bPaginate": false,
            "bFilter": true,
            "bInfo": false,
            "bLengthChange": false,
            "aoColumns": [
                { "sType": "html-percent", "aTargets": [1] },
                { "sType": "html-percent", "aTargets": [2] },
                { "sType": "html-percent", "aTargets": [3] },
                { "sType": "html-percent", "aTargets": [4] },
                { "sType": "html-percent", "aTargets": [5] },
                { "sType": "html-percent", "aTargets": [6] },
                { "sType": "html-percent", "aTargets": [7] }
            ]
        });
    };

    dependencyStyle = function () {
        $('#dependency').dataTable({
            "bAutoWidth": true,
            "bPaginate": false,
            "bFilter": true,
            "bInfo": false,
            "bLengthChange": false,
            "aoColumns": [
                { "sType": "html-percent", "aTargets": [1] },
                { "sType": "html-percent", "aTargets": [2] },
                { "sType": "html-percent", "aTargets": [3] },
                { "sType": "html-percent", "aTargets": [4] },
                { "sType": "html-percent", "aTargets": [5] },
                { "sType": "html-percent", "aTargets": [6] },
                { "sType": "html-percent", "aTargets": [7] }
            ]
        });
    };
    failedStyle = function () {
        $('#fail').dataTable({
            "bAutoWidth": true,
            "bPaginate": false,
            "bFilter": true,
            "bInfo": false,
            "bLengthChange": false,
            "aoColumns": [
                { "sType": "html-percent", "aTargets": [1] },
                { "sType": "html-percent", "aTargets": [2] },
                { "sType": "html-percent", "aTargets": [3] },
                { "sType": "html-percent", "aTargets": [4] },
                { "sType": "html-percent", "aTargets": [5] },
                { "sType": "html-percent", "aTargets": [6] },
                { "sType": "html-percent", "aTargets": [7] },
                { "sType": "html-percent", "aTargets": [8] }
            ]
        });
    };

    dependencyTimeoutStyle = function () {
        $('#dependency-timeout').dataTable({
            "bAutoWidth": true,
            "bPaginate": false,
            "bFilter": true,
            "bInfo": false,
            "bLengthChange": false,
            "aoColumns": [
                { "sType": "html-percent", "aTargets": [1] },
                { "sType": "html-percent", "aTargets": [2] },
                { "sType": "html-percent", "aTargets": [3] },
                { "sType": "html-percent", "aTargets": [4] },
                { "sType": "html-percent", "aTargets": [5] },
                { "sType": "html-percent", "aTargets": [6] },
                { "sType": "html-percent", "aTargets": [7] },
                { "sType": "html-percent", "aTargets": [8] }
            ]
        });
    };

    timeoutStyle = function () {
        $('#timeout').dataTable({
            "bAutoWidth": true,
            "bPaginate": false,
            "bFilter": true,
            "bInfo": false,
            "bLengthChange": false,
            "aoColumns": [
                { "sType": "html-percent", "aTargets": [1] },
                { "sType": "html-percent", "aTargets": [2] },
                { "sType": "html-percent", "aTargets": [3] },
                { "sType": "html-percent", "aTargets": [4] },
                { "sType": "html-percent", "aTargets": [5] },
                { "sType": "html-percent", "aTargets": [6] },
                { "sType": "html-percent", "aTargets": [7] },
                { "sType": "html-percent", "aTargets": [8] }
            ]
        });
    };

});
$(document).ready(function () {
    var load_count = 5;
    $.ajax({
        async: false,
        data: {
            action: "reflash_attempts",
            start: starttime
        },
        type: "POST",
        url: "/monitor",
        error: function () {
        },
        success: function (response, textStatus) {

        }


    });
    $.ajax({
        data: {
            action: "runningtasks"

        },
        type: "POST",
        url: "/monitor",
        error: function () {
            $("#running_body").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
            $("#running_body").addClass("align-center");
        },
        success: function (response, textStatus) {
            $("#running_load").html("");
            $("#running_body").html(response);
            runningStyle();
        }


    });
    $.ajax({
        data: {
            action: "submitfail",
            id: id
        },
        type: "POST",
        url: "/monitor",
        error: function () {
            $("#submit_body").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
            $("#submit_body").addClass("align-center");
        },
        success: function (response, textStatus) {
            $("#submit_load").html("");
            $("#submit_body").html(response);
            submitfailStyle();

            load_count = load_count - 1;
            if(load_count == 0){
                $(".feedBtn").on('click', function (e) {

                    var anchor = this;
                    if (e.ctrlKey || e.metaKey) {
                        return true;
                    } else {
                        e.preventDefault();
                    }
                    $.ajax({
                        type: "get",
                        url: anchor.href,
                        error: function () {
                            $("#alertContainer").html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>报错失败</strong></div>');
                            $(".alert").alert();
                        },
                        success: function (response, textStatus) {
                            $("#feedModal").html(response);
                            $("#feedModal").modal().css({
                                backdrop: false


                            });


                        }


                    });
                });
            }


        }


    });

    $.ajax({
        data: {
            action: "dependencypass",
            id: id
        },
        type: "POST",
        url: "/monitor",
        error: function () {
            $("#dependency_body").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
            $("#dependency_body").addClass("align-center");
        },
        success: function (response, textStatus) {
            $("#dependency_load").html("");
            $("#dependency_body").html(response);
            dependencyStyle();

            load_count = load_count - 1;
            if(load_count == 0){
                $(".feedBtn").on('click', function (e) {

                    var anchor = this;
                    if (e.ctrlKey || e.metaKey) {
                        return true;
                    } else {
                        e.preventDefault();
                    }
                    $.ajax({
                        type: "get",
                        url: anchor.href,
                        error: function () {
                            $("#alertContainer").html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>报错失败</strong></div>');
                            $(".alert").alert();
                        },
                        success: function (response, textStatus) {
                            $("#feedModal").html(response);
                            $("#feedModal").modal().css({
                                backdrop: false


                            });



                        }


                    });
                });
            }
        }


    });

    $.ajax({
        data: {
            action: "failedtasks",
            id: id
        },
        type: "POST",
        url: "/monitor",
        error: function () {
            $("#failed_body").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
            $("#failed_body").addClass("align-center");
        },
        success: function (response, textStatus) {
            $("#failed_load").html("");
            $("#failed_body").html(response);

            failedStyle();

            load_count = load_count - 1;
            if(load_count == 0){
                $(".feedBtn").on('click', function (e) {

                    var anchor = this;
                    if (e.ctrlKey || e.metaKey) {
                        return true;
                    } else {
                        e.preventDefault();
                    }
                    $.ajax({
                        type: "get",
                        url: anchor.href,
                        error: function () {
                            $("#alertContainer").html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>报错失败</strong></div>');
                            $(".alert").alert();
                        },
                        success: function (response, textStatus) {
                            $("#feedModal").html(response);
                            $("#feedModal").modal().css({
                                backdrop: false


                            });


                        }


                    });
                });
            }


        }


    });
    $.ajax({
        data: {
            action: "dependencytimeout",
            id: id
        },
        type: "POST",
        url: "/monitor",
        error: function () {
            $("#dependency_timeout_body").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
            $("#dependency_timeout_body").addClass("align-center");
        },
        success: function (response, textStatus) {
            $("#dependency_timeout_load").html("");
            $("#dependency_timeout_body").html(response);

            dependencyTimeoutStyle();

            load_count = load_count - 1;
            if(load_count == 0){
                $(".feedBtn").on('click', function (e) {

                    var anchor = this;
                    if (e.ctrlKey || e.metaKey) {
                        return true;
                    } else {
                        e.preventDefault();
                    }
                    $.ajax({
                        type: "get",
                        url: anchor.href,
                        error: function () {
                            $("#alertContainer").html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>报错失败</strong></div>');
                            $(".alert").alert();
                        },
                        success: function (response, textStatus) {
                            $("#feedModal").html(response);
                            $("#feedModal").modal().css({
                                backdrop: false


                            });


                        }


                    });
                });
            }


        }


    });
    $.ajax({
        data: {
            action: "timeout",
            id: id
        },
        type: "POST",
        url: "/monitor",
        error: function () {
            $("#timeout_body").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
            $("#timeout_body").addClass("align-center");
        },
        success: function (response, textStatus) {
            $("#timeout_load").html("");
            $("#timeout_body").html(response);

            timeoutStyle();

            load_count = load_count - 1;
            if(load_count == 0){
                $(".feedBtn").on('click', function (e) {

                    var anchor = this;
                    if (e.ctrlKey || e.metaKey) {
                        return true;
                    } else {
                        e.preventDefault();
                    }
                    $.ajax({
                        type: "get",
                        url: anchor.href,
                        error: function () {
                            $("#alertContainer").html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>报错失败</strong></div>');
                            $(".alert").alert();
                        },
                        success: function (response, textStatus) {
                            $("#feedModal").html(response);
                            $("#feedModal").modal().css({
                                backdrop: false


                            });


                        }


                    });
                });
            }

        }


    });
});

</script>
<script type="text/javascript" charset="utf-8" language="javascript" src="js/jquery.dataTables.js"></script>
<script type="text/javascript" charset="utf-8" language="javascript" src="js/DT_bootstrap.js"></script>
<script type="text/javascript" charset="utf-8" language="javascript" src="js/attempt.js"></script>
</body>
</html>
