<!DOCTYPE html >
<html >
<head>
    <meta name="description" content="overview &amp; stats"/>
	<title>Taurus</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
	<#include "segment/html_header.ftl">
    <#include "segment/html_header2.ftl">
    <#include "segment/monitor-center_header.ftl">
    <link rel="stylesheet" href="${rc.contextPath}/css/loading.css">
    <script type="text/javascript" src="${rc.contextPath}/static/js/host_center.js"></script>
    
</head>

<body>
<#include "segment/header.ftl">
<#include "segment/left.ftl">

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
            <a href="${rc.contextPath}/mvc/index">HOME</a>
        </li>
    </ul>
</div>

<div class="page-content">
<div class="row">
<#-- 这个div可能没用了，有待观察 -->
<div class="time_inal "></div>

<div id="admin">
<div class="col-sm-12">
    <div class="col-sm-6">
        <div class="widget-box">
            <div class="widget-header header-color-green2">
                <h5 class="widget-title">
                    <i class="icon-eye-open"></i>
                    Job机器状态
                </h5>

                <div class="widget-toolbar">
                    <a class="btn btn-xs btn-yellow" href="${rc.contextPath}/mvc/host_history">
                        <i class="icon-search bigger-110"></i>
                        查看图谱
                    </a>
                    <a class="atip tooltip-info" data-toggle="tooltip" data-placement="bottom"
                       data-original-title="此按钮可以查看主机的任务执行历史"><span style="color: white">[提示]</span> </a>
                    <a href="#" data-action="collapse">
                        <i class="icon-chevron-up"></i>
                    </a>
                </div>
            </div>

            <div class="widget-body">
                <div class="widget-main">
                    <div id="piechart-placeholder">
                        <div class="loadIcon">
                            <div></div>
                            <div></div>
                            <div></div>
                            <div></div>
                        </div>
                    </div>

                    <div class="hr hr8 hr-double"></div>

                    <div class="clearfix">
                        <div class="col-sm-6">
                                                        <span class="grey">
                                                            <i class="icon-lightbulb green"></i>
                                                            &nbsp; 正常
                                                        </span>
                            <h4 class="bigger pull-right" id="onlineNums"></h4>
                        </div>

                        <div class="col-sm-6">
                                                        <span class="grey">
                                                            <i class="icon-lightbulb red"></i>
                                                            &nbsp; 失联
                                                        </span>
                            <h4 class="bigger pull-right " id="exceptionNums"></h4>
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
    <div class="col-sm-6" >
        <div class="widget-box">
            <div class="widget-header header-color-red3">
                <h5 class="widget-title">
                    <i class="icon-eye-close"></i>
                    Job异常机器列表
                </h5>

                <div class="widget-toolbar">
                    <a href="#" data-action="collapse">
                        <i class="icon-chevron-up"></i>
                    </a>
                </div>
            </div>

            <div class="widget-body">
                <div class="widget-main" id="exceptionJob" style="height: 283px">
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
<div class="col-sm-12">
    <div class="col-sm-6" id="cpuwidget">
        <div class="widget-box">
            <div class="widget-header header-color-blue3">
                <h5 class="widget-title">
                    <i class=" icon-time"></i>
                    主机CPU负载列表
                </h5>


                <div class="widget-toolbar">
                    <a href="#" data-action="collapse">
                        <i class="icon-chevron-up"></i>
                    </a>
                </div>
                <div class="widget-toolbar">
                    <a id='cpureeflash' title='更新数据,刷新时间间隔1分钟' onclick="reflash('reflash');" href="#cpuwidget"><i
                            class="icon-refresh"></i></a>
                </div>
            </div>

            <div class="widget-body">

                <div class="widget-main align-center" style="height: 560px" id="cpuload">
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
    <div class="col-sm-6" id="memwidget">
        <div class="widget-box">
            <div class="widget-header header-color-pink">
                <h5 class="widget-title">
                    <i class=" icon-tint"></i>
                    主机内存使用率列表
                </h5>


                <div class="widget-toolbar">
                    <a href="#" data-action="collapse">
                        <i class="icon-chevron-up"></i>
                    </a>
                </div>
                <div class="widget-toolbar">
                    <a id='reflash' title='更新数据,刷新时间间隔1分钟' onclick="reflash('reflash');" href="#memwidget"> <i
                            class="icon-refresh"></i></a>
                </div>
            </div>

            <div class="widget-body">

                <div class="widget-main align-center"  style="height: 560px" id="memload">
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
    <img src="${rc.contextPath}/img/betop.png" width="66" height="67">
</a>

<div class="feedTool hide">
    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img
            border="0" src="${rc.contextPath}/img/qq.png" width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img
            border="0" src="${rc.contextPath}/img/x_alt.png" width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>
    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>

</div>

<script type="text/javascript">
    $('li[id="index"]').addClass("active open");
    $('li[id="host_center"]').addClass("active");
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


    function GetDateStr(dd, AddDayCount) {
        dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
        var y = dd.getFullYear();
        var m = dd.getMonth() + 1;//获取当前月份的日期
        var d = dd.getDate();
        return y + "-" + m + "-" + d;
    }

    var now_s = "${now_s!}";
    var now = new Date(Date.parse(now_s.replace(/-/g, "/")));
    var id = "${step!}";
    var op = "${op_str!}";


    function reflash(queryType) {
        var cpuLoadBody = "";
        var memLoadBody = "";
        $("#cpuload").html(" <i class='icon-spinner icon-spin icon-large'></i>");
        $("#cpuload").addClass("align-center");
        $("#memload").html("<i class='icon-spinner icon-spin icon-large'></i>");
        $("#memload").addClass("align-center");


        $.ajax({
            async: true,
            data: {
                action: "hostload",
                queryType: queryType

            },
            type: "POST",
            url: "../host_center",
            error: function () {
                $("#cpuload").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
                $("#cpuload").addClass("align-center");
                $("#memload").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
                $("#memload").addClass("align-center");
            },
            success: function (response, textStatus) {
                var jsonarray = $.parseJSON(response);
                $.each(jsonarray, function (i, item) {
                    cpuLoadBody += "<tr>" +
                            "<td>" + item.hostName + "</td>" +
                            "<td>" + item.cpuLoad + "</td>"
                    "</tr>";

                    memLoadBody += "<tr>" +
                            "<td>" + item.hostName + "</td>" +
                            "<td>" + item.memLoad + "</td>"
                    "</tr>";
                });
                var topCpuLoadLists = ' <table  class="table table-striped table-bordered table-hover " id="cputable">'
                        + '<thead><tr><th>主机名</th>  <th>CPU负载(load average)</th> </tr> </thead>'
                        + '        <tbody>'
                        + cpuLoadBody
                        + '        </tbody>'
                        + '    </table>';
                $("#cpuload").html(topCpuLoadLists);
                $("#cpuload").removeClass("align-center");

                var topMemLoadLists = ' <table  class="table table-striped table-bordered table-hover " id="memtable">'
                        + '<thead><tr><th>主机名</th>  <th>内存剩余(free)</th> </tr> </thead>'
                        + '        <tbody>'
                        + memLoadBody
                        + '        </tbody>'
                        + '    </table>';
                $("#memload").html(topMemLoadLists);
                $("#memload").removeClass("align-center");
                cpuTableStyle();
                memTableStyle();
            }


        });

    }
</script>

</body>
</html>