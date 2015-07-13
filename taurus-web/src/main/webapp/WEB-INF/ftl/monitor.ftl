<!DOCTYPE html >
<html >
<head>
	
	<title>Taurus</title>
	<#include "segment/html_header.ftl">
	<script type="text/javascript" src="${rc.contextPath}/resource/js/lib/Chart.js"></script>
	<#include "segment/html_header2.ftl">
	<link rel="stylesheet" href="${rc.contextPath}/css/loading.css">
	<style>

        .time_inal {
            float: right
        }
    </style>

</head>
<body>
<#include "segment/header.ftl">
<#include "segment/left.ftl">


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
            <a href="${rc.contextPath}/index">HOME</a>
        </li>
        <li class="active">
            <a href="${rc.contextPath}/monitor">任务监控</a>
        </li>
    </ul>
</div>

<div class="page-content">

<div id="alertContainer" class="container col-sm-10">
</div>

<#-- 右边六个ajax表格 start -->
<div class="row">
<#-- running table start -->
<div class="col-sm-12" >
    <div class="widget-box">
        <div class="widget-header header-color-blue2">
            <h5 class="widget-title">
                <i class="icon-spinner"></i>
                <a class="atip" data-toggle="tooltip" data-placement="right" data-original-title="正在运行的任务"><span
                        style="color: white">RUNNING</span></a>
            </h5>

            <div class="widget-toolbar">
                <a href="#" data-action="collapse">
                    <i class="icon-chevron-up"></i>
                </a>
            </div>
        </div>

        <div class="widget-body">
            <div class="widget-main" id="runningwidget">
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
                        <div class="loadIcon">
                            <div></div>
                            <div></div>
                            <div></div>
                            <div></div>
                        </div>
                    </div>

                    </tbody>
                </table>

            </div>
            <!-- /.widget-main -->
        </div>
        <!-- /.widget-body -->
    </div>
    <!-- /.widget-box -->
</div>

<div class="time_inal ">
    <div>
        <a class="atip" data-toggle="tooltip" data-placement="top"
           data-original-title="当你点击了[-1h]|[-1d]|[-1w]|[-1m]后，在想切换到当前页面时，请点击[当天]，刷新页面无效噢～">[注意] </a>
        &nbsp;&nbsp;|&nbsp;&nbsp;
        <a class="atip"
           href="${rc.contextPath}/monitor?step=-24&op=day&date=${todayD!}"
           data-toggle="tooltip" data-placement="top"
           data-original-title=" 时间区间[${todayDtip!}]">[当天] </a>
        <a class="atip" data-toggle="tooltip" data-placement="top"
           data-original-title="查看历史数据">[历史模式] </a>
        &nbsp;&nbsp;|&nbsp;&nbsp;
        <a class="atip"
           href="${rc.contextPath}/monitor?step=-720&op=day&date=${bf1mD!}"
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[${bf1mDtip!}]">[-1m] </a>
        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip"
           href="${rc.contextPath}/monitor?step=-168&op=day&date=${bf1wD!}"
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[${bf1wDtip!}]">[-1w] </a>
        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip"
           href="${rc.contextPath}/monitor?step=-24&op=day&date=${bf1dD!}"
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[${bf1dDtip!}]">[-1d] </a>

        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip"
           href="${rc.contextPath}/monitor?step=24&op=day&date=${af1dD!}"
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[${af1dDtip!}]">[+1d] </a>
        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip" 
           href="${rc.contextPath}/monitor?step=168&op=day&date=${af1wD!}"
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[${af1wDtip!}]">[+1w] </a>
        &nbsp;&nbsp; |&nbsp;&nbsp;
        <a class="atip" 
           href="${rc.contextPath}/monitor?step=720&op=day&date=${af1mD!}"
           data-toggle="tooltip" data-placement="top"
           data-original-title="时间区间[${af1mDtip!}]">[+1m] </a>
    </div>
</div>

<#-- submitfail table start -->
<div class="col-sm-12" >
    <div class="widget-box">
        <div class="widget-header header-color-red">
            <h5 class="widget-title">
                <i class="icon-remove"></i>
                <a class="atip" data-toggle="tooltip" data-placement="right" data-original-title="提交失败的任务"><span
                        style="color: white">SUBMIT-FAILED</span></a>
            </h5>

            <div class="widget-toolbar">
                <a href="#" data-action="collapse">
                    <i class="icon-chevron-up"></i>
                </a>
            </div>
        </div>

        <div class="widget-body">
            <div class="widget-main" id="submitwidget">
                <table cellpadding="0" cellspacing="0" border="0"
                       class="table table-striped table-format table-hover" id="submitfail" >
                    <thead>
                    <tr>
                        <th>任务ID</th>
                        <th>任务名称</th>
                        <th>实际启动时间</th>
                        <th>实际结束时间</th>
                        <th>预计调度时间</th>
                        <th>IP</th>
                        <th>最后执行状态</th>
                        <th>我要报错</th>
                    </tr>
                    </thead>
                    <tbody id="submit_body">

                    <div id="submit_load">
                        <div class="loadIcon">
                            <div></div>
                            <div></div>
                            <div></div>
                            <div></div>
                        </div>
                    </div>

                    </tbody>
                </table>

            </div>
            <!-- /.widget-main -->
        </div>
        <!-- /.widget-body -->
    </div>
    <!-- /.widget-box -->

</div>

<#-- dependencypass table start -->
<div class="col-sm-12" >
    <div class="widget-box">
        <div class="widget-header header-color-red3">
            <h5 class="widget-title">
                <i class="icon-eye-close"></i>
                <a class="atip" data-toggle="tooltip" data-placement="right" data-original-title="拥堵的任务"><span
                        style="color: #ffffff">DEPENDENCY_PASS</span></a>
            </h5>

            <div class="widget-toolbar">
                <a href="#" data-action="collapse">
                    <i class="icon-chevron-up"></i>
                </a>
            </div>
        </div>

        <div class="widget-body">
            <div class="widget-main" id="dependencywidget">
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
                        <div class="loadIcon">
                            <div></div>
                            <div></div>
                            <div></div>
                            <div></div>
                        </div>
                    </div>
                    </tbody>
                </table>

            </div>
            <!-- /.widget-main -->
        </div>
        <!-- /.widget-body -->
    </div>
    <!-- /.widget-box -->

</div>

<#-- failedtasks table start -->
<div class="col-sm-12" >
    <div class="widget-box">
        <div class="widget-header header-color-red2">
            <h5 class="widget-title">
                <i class="icon-remove-circle"></i>
                <a class="atip" data-toggle="tooltip" data-placement="right" data-original-title="失败的任务"><span
                        style="color: #ffffff">FAILED</span></a>
            </h5>

            <div class="widget-toolbar">
                <a href="#" data-action="collapse">
                    <i class="icon-chevron-up"></i>
                </a>
            </div>
        </div>

        <div class="widget-body">
            <div class="widget-main" id="failwidget">
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
                        <th>最后执行状态</th>
                        <th>查看日志</th>
                        <th>我要报错</th>
                    </tr>
                    </thead>
                    <tbody id="failed_body">
                    <div id="failed_load">
                        <div class="loadIcon">
                            <div></div>
                            <div></div>
                            <div></div>
                            <div></div>
                        </div>
                    </div>
                    </tbody>
                </table>

            </div>
            <!-- /.widget-main -->
        </div>
        <!-- /.widget-body -->
    </div>
    <!-- /.widget-box -->

</div>


<#-- dependencytimeout table start -->
<div class="col-sm-12" >
    <div class="widget-box">
        <div class="widget-header header-color-orange">
            <h5 class="widget-title">
                <i class="icon-info-sign"></i>
                <a class="atip" data-toggle="tooltip" data-placement="right" data-original-title="依赖超时的任务"><span
                        style="color: #ffffff">DEPENDENCY_TIMEOUT</span></a>
            </h5>

            <div class="widget-toolbar">
                <a href="#" data-action="collapse">
                    <i class="icon-chevron-up"></i>
                </a>
            </div>
        </div>

        <div class="widget-body">
            <div class="widget-main" id="dependency-timeoutwidget">
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
                        <div class="loadIcon">
                            <div></div>
                            <div></div>
                            <div></div>
                            <div></div>
                        </div>
                    </div>
                    </tbody>
                </table>
            </div>
            <!-- /.widget-main -->
        </div>
        <!-- /.widget-body -->
    </div>
    <!-- /.widget-box -->

</div>

<#-- timeout table start -->
<div class="col-sm-12" >
    <div class="widget-box">
        <div class="widget-header header-color-orange">
            <h5 class="widget-title">
                <i class="icon-exclamation-sign"></i>
                <a class="atip" data-toggle="tooltip" data-placement="right" data-original-title="超时的任务"><span
                        style="color: #ffffff">TIMEOUT</span></a>
            </h5>

            <div class="widget-toolbar">
                <a href="#" data-action="collapse">
                    <i class="icon-chevron-up"></i>
                </a>
            </div>
        </div>

        <div class="widget-body">
            <div class="widget-main" id="timeoutwidget">
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
                        <th>最后执行状态</th>
                        <th>查看日志</th>
                        <th>我要报错</th>

                    </tr>
                    </thead>
                    <tbody id="timeout_body">
                    <div id="timeout_load">
                        <div class="loadIcon">
                            <div></div>
                            <div></div>
                            <div></div>
                            <div></div>
                        </div>
                    </div>
                    </tbody>
                </table>
            </div>
            <!-- /.widget-main -->
        </div>
        <!-- /.widget-body -->
    </div>
    <!-- /.widget-box -->

</div>

</div>
<#-- 右边六个ajax表格 end -->

</div>
<#-- class:page-content end -->

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
<#-- class:main-content end -->

<div class="modal fade" id="feedModal" role="dialog"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
        </div>
    </div>
</div>

<#-- 回到顶端 -->
<a href="#" class="scrollup" style="display: inline;">
    <img src="${rc.contextPath}/img/betop.png" width="66" height="67">
</a>

<#-- 小企鹅报错挂件 -->
<div class="feedTool" style="display: none">
    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="${rc.contextPath}/img/qq.png"  width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img border="0" src="${rc.contextPath}/img/x_alt.png"  width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>
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
var now_s = "${now_s!}";
var now = new Date(Date.parse(now_s.replace(/-/g, "/")));
var id = "${RequestParameters.id!}";
var step = "${step!}";
var op = "${op_str!}";

var starttime;
var endtime;
if (step == null || step == "") {
    starttime = GetDateStr(now, 0);
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
            "bLengthChange": false

        });
    };
    submitfailStyle = function () {
        $('#submitfail').dataTable({
            "bAutoWidth": true,
            "bPaginate": false,
            "bFilter": true,
            "bInfo": false,
            "bLengthChange": false

        });
    };

    dependencyStyle = function () {
        $('#dependency').dataTable({
            "bAutoWidth": true,
            "bPaginate": false,
            "bFilter": true,
            "bInfo": false,
            "bLengthChange": false

        });
    };
    failedStyle = function () {
        $('#fail').dataTable({
            "bAutoWidth": true,
            "bPaginate": false,
            "bFilter": true,
            "bInfo": false,
            "bLengthChange": false

        });
    };

    dependencyTimeoutStyle = function () {
        $('#dependency-timeout').dataTable({
            "bAutoWidth": true,
            "bPaginate": false,
            "bFilter": true,
            "bInfo": false,
            "bLengthChange": false

        });
    };

    timeoutStyle = function () {
        $('#timeout').dataTable({
            "bAutoWidth": true,
            "bPaginate": false,
            "bFilter": true,
            "bInfo": false,
            "bLengthChange": false

        });
    };

});
$(document).ready(function () {
    var load_count = 5;

    $.ajax({
        async: false,
        data: {
            start: starttime
        },
        type: "POST",
        url: "${rc.contextPath}/monitor/reflash_attempts",
        error: function () {
        },
        success: function (response, textStatus) {

        }


    });

    $.ajax({
        type: "GET",
        url: "${rc.contextPath}/monitor/runningtasks",
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
            id: id
        },
        type: "POST",
        url: "${rc.contextPath}/monitor/submitfail",
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
            id: id
        },
        type: "POST",
        url: "${rc.contextPath}/monitor/dependencypass",
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
            id: id
        },
        type: "POST",
        url: "${rc.contextPath}/monitor/failedtasks",
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
            id: id
        },
        type: "POST",
        url: "${rc.contextPath}/monitor/dependencytimeout",
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
            id: id
        },
        type: "POST",
        url: "${rc.contextPath}/monitor/timeout",
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
<script type="text/javascript" charset="utf-8" language="javascript" src="${rc.contextPath}/js/jquery.dataTables.js"></script>
<script type="text/javascript" charset="utf-8" language="javascript" src="${rc.contextPath}/js/DT_bootstrap.js"></script>
<script type="text/javascript" charset="utf-8" language="javascript" src="${rc.contextPath}/static/js/attempt.js"></script>

</body>
</html>