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
    <script type="text/javascript" src="lib/ace/js/bootstrap-datepicker.min.js"></script>
    <script type="text/javascript" src="lib/ace/js/daterangepicker.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui.min.js"></script>
    <script src="js/jquery.datetimepicker.js"></script>
    <script type="text/javascript" src="js/login.js"></script>
    <!-- page specific plugin styles -->

    <!-- fonts -->
    <script src="lib/ace/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/ace-fonts.css"/>

    <!-- ace styles -->

    <link rel="stylesheet" href="lib/ace/css/ace.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-skins.min.css"/>
    <link rel="stylesheet" href="css/jquery.datetimepicker.css"/>
    <link href="css/bwizard.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="css/common.css">
    <style>
        label.error {
            margin-left: 10px;
            color: red;
        }

        label.success {
            margin-left: 10px;
            color: green;
        }

    </style>
</head>
<body>

<%@page import="com.dp.bigdata.taurus.restlet.resource.IUserGroupsResource" %>
<%@page import="com.dp.bigdata.taurus.restlet.resource.IUserGroupMappingsResource" %>

<%@page import="com.dp.bigdata.taurus.restlet.shared.UserGroupDTO" %>
<%@page import="com.dp.bigdata.taurus.restlet.shared.UserGroupMappingDTO" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.dp.bigdata.taurus.restlet.shared.HostDTO" %>
<%@ page import="com.dp.bigdata.taurus.restlet.resource.IHostsResource" %>

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
                <li id="monitor_center">
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

<div class="main-content ">

    <%
        String ip = request.getParameter("ip");
        java.util.Date time = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now_str = request.getParameter("date");
        if (now_str == null || now_str.isEmpty()) {
            now_str = formatter.format(time);
        }

        Date startDate = new Date(time.getTime() - 12 * 60 * 60 * 1000);
        String startTime = formatter.format(startDate);

        String endTime = formatter.format(formatter.parse(now_str));
    %>
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

        </ul>
    </div>
    <div class="page-content">
        <div class="row">
            <div class="col-sm-12">


                <div class="widget-box">
                    <div class="widget-header header-color-green">
                        <h5 class="widget-title">
                            <i class="icon-bell"></i>
                            说明
                        </h5>

                        <div class="widget-toolbar">
                            <a href="#" data-action="collapse">
                                <i class="icon-chevron-up"></i>
                            </a>
                        </div>
                    </div>

                    <div class="widget-body">

                        <div class="widget-main ">





                                <table class="table table-bordered">
                                    <tr>
                                        <td>一个<span style='color: gainsboro; font-size: 15px'>●</span>代表 6 分钟</td>
                                        <td><span style='color: gainsboro; font-size: 15px'>●  </span>表示该任务未调度执行</td>
                                        <td> <span style='color: green; font-size: 15px'>●  </span>表示该任务调度执行成功</td>
                                        <td>
                                            <span style='color: red; font-size: 15px'>●  </span>表示该任务调度执行失败
                                        </td>
                                        <td><span style='color: #ffff00; font-size: 15px'>●  </span>表示该任务调度执行超时</td>



                                    </tr>
                                 </table>

                        </div>
                        <!-- /.widget-main -->
                    </div>
                    <!-- /.widget-body -->
                </div>


                <div class="widget-box">
                    <div class="widget-header header-color-red3">
                        <h5 class="widget-title">
                            <i class="icon-anchor"></i>
                                <span
                                        style="color: white"><%if (StringUtils.isNotBlank(ip)) {%>JOB主机 [<%=ip%>] 任务执行历史<%} else {%>JOB主机任务执行历史<%}%></span></a>
                        </h5>

                        <div class="widget-toolbar">
                            <label>开始：</label><input type="text" id="startTime"/>
                            <a class="btn btn-primary btn-small" href='#' onClick="time_reflash_view()"><i class="icon-eye-open">查看</i></a>
                            |
                            <a href="#" data-action="collapse">
                                <i class="icon-chevron-up"></i>
                            </a>

                        </div>
                    </div>

                    <div class="widget-body">
                        <div class="widget-main align-center" id="history">

                            <label class="label label-lg label-info arrowed-right "
                                   for="ip">选择查看的Job主机</label>

                                <select id="ip" name="ip" class="input-big field" style="width: 300px">
                                    <%
                                        if (StringUtils.isBlank(ip)) {
                                            cr = new ClientResource(host + "host");
                                            IHostsResource hostResource = cr.wrap(IHostsResource.class);
                                            cr.accept(MediaType.APPLICATION_XML);
                                            ArrayList<HostDTO> hosts = hostResource.retrieve();

                                            for (HostDTO hostip : hosts) {
                                    %>

                                    <option><%=hostip.getIp()%>
                                    </option>
                                    <% }
                                    %>


                                    <%

                                        }
                                    %>

                                </select>
                                <a id="btn" class="btn btn-primary btn-minier"
                                   href="#" onClick="reflash_view()">查看</a>


                        </div>
                        <!-- /.widget-main -->
                    </div>
                    <!-- /.widget-body -->
                </div>


            </div>
        </div>
    </div>
</div>
<a href="#" class="scrollup" style="display: inline;">
    <img src="img/betop.png" width="66" height="67">
</a>

<div class="feedTool">

    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img
            border="0" src="img/qq.png" width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img
            border="0" src="img/x_alt.png" width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>

    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>
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

    $(".atip").tooltip();
    $('#startTime').datetimepicker({
        formatTime:'H:i',
        format:'Y-m-d H:i',
        formatDate:'Y-m-d',
        defaultTime:'10:00',
        timepicker:true,
        timepickerScrollbar:true
    });

    function GetDateStr(dd, AddDayCount) {
        dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
        var y = dd.getFullYear();
        var m = dd.getMonth() + 1;//获取当前月份的日期
        var d = dd.getDate();
        var h = dd.getHours();
        var mm = dd.getMinutes();
        var s = dd.getSeconds();

        if( h < '10')
        {
            h = '0' + h;
        }
        if(m < '10')
        {
            m = '0' + m;
        }

        if(d < '10')
        {
            d = '0' + d;
        }

        if(mm < '10')
        {
            mm = '0' + mm;
        }
        if(s < '10')
        {
            s = '0' + s;
        }
        return y + "-" + m + "-" + d + " " + h + ":" + mm + ":" + s;
    }

    var now_s = "<%=formatter.format( formatter.parse(now_str))%>";
    var now = new Date(Date.parse(now_s.replace(/-/g, "/")));
    var time = GetDateStr(now, -1);
    var table_body = '<a class="btn btn-primary btn-minier" style="float: right" href="host_history.jsp">返回</a><table class="table table-striped table-bordered table-condensed" >';


    var not_run = "<span style='color: gainsboro; font-size: 12px'>●</span>"
    var run_green = "<span style='color: green; font-size: 12px'>●</span>";
    var run_red = "<span style='color: red; font-size: 12px'>●</span>";
    var run_bule = "<span style='color: #0000ff; font-size: 12px'>●</span>";
    var run_yellow = "<span style='color: #ffff00; font-size: 12px'>●</span>";
    var ip = "<%=ip%>";

    if(ip != null && ip !="null"){
        get_history(ip, time);
    }

    function get_history(ip, time){
        $.ajax({
            data: {
                action: "host_history",
                time: time,
                ip: ip
            },
            type: "POST",
            url: "/host_history",
            error: function () {
                $("#history").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
                $("#history").addClass("align-center");
            },
            success: function (response, textStatus) {

                var jsonarray = $.parseJSON(response);

                if(jsonarray.length == 0){
                    $("#history").html("<i class='icon-info-sign icon-large red '>该Job机没有任何任务执行~</i> <a class='btn btn-primary btn-minier' href='host_history.jsp'>返回</a>");
                    $("#history").addClass("align-center");
                }else{
                    var show_time =  new Date(Date.parse(time.replace(/-/g, "/")));
                    table_body += '<tr><th>任务名</th><th><span style="float:left">时间段 :</span><span class="padding-right-14" style="float:left">'+  GetDateStr(show_time, -1) + '</span><span class="padding-left-14" style="float:right">'+GetDateStr(show_time, 1)+'</span></th></tr>';
                    $.each(jsonarray, function (i, item) {

                        table_body += " <tr><td valign='left'><span style='color: darkgreen; font-size: 9px'>"
                                + item.taskName
                                + "</span></td>"
                                + "<td valign='middle'>"
                                + genrate_body(item.runningMap)
                                + "</td><tr>";


                    });
                    table_body += "</table>"
                    $("#history").html(table_body);
                }


            }
        });
    }
    function reflash_view(){
        var selected_ip = $("#ip").val();
        get_history(selected_ip, time);
    }

    function time_reflash_view(){
        if(ip != null && ip !="null"){
           var new_time = $('#startTime').val();
            new_time +=':00';
            get_history(ip, new_time);
        }else{
            new_time = $('#startTime').val();
            new_time +=':00';
            var selected_ip = $("#ip").val();
            get_history(selected_ip, new_time);
        }


    }

    function genrate_body(runningMap) {
        var runng_history = runningMap.split(",");
        var ret_body = "";
        var runArray = new Array(120);
        for (var i = 0; i < 120; i++) {
            runArray[i] = '-1';
        }

        for (var len = 0; len < runng_history.length; len++) {
            var tmp = runng_history[len].split("#");
            var pos_s = tmp[0];
            var pos = parseInt(pos_s);
            var status;

            if (tmp[1] != null) {
                status = tmp[1];
            } else {
                status = "7";
            }

            var span_td;
            if (status == "7" || status == "1" || status == "4") {
                span_td = run_green;
            } else if (status == "2" || status == "5" || status == "8" || status == "10" || status == "11") {
                span_td = run_red;
            } else if (status == "3" || status == "9") {
                span_td = run_yellow;
            } else {
                span_td = run_bule;
            }
            runArray[pos] = span_td
        }

        for (var i = 0; i < 120; i++) {

            if (runArray[i] != '-1') {
                ret_body += runArray[i];
            } else {
                ret_body += not_run;
            }

        }
        return ret_body;
    }
</script>
<script src="js/jquery.validate.min.js" type="text/javascript"></script>


</body>

</html>