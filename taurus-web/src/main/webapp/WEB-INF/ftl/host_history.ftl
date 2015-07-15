<!DOCTYPE html >
<html >
<head>
	
	<title>Taurus</title>
	<#include "segment/html_header.ftl">
	<link rel="stylesheet" href="${rc.contextPath}/css/jquery-ui.min.css"/>
	<script type="text/javascript" src="${rc.contextPath}/lib/ace/js/bootstrap-datepicker.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/lib/ace/js/daterangepicker.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/jquery-ui.min.js"></script>
    <script src="${rc.contextPath}/js/jquery.datetimepicker.js"></script>
	<#include "segment/html_header2.ftl">
	<link rel="stylesheet" href="${rc.contextPath}/css/jquery.datetimepicker.css"/>
    <link href="${rc.contextPath}/css/bwizard.min.css" rel="stylesheet"/>
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
<#include "segment/header.ftl">
<#include "segment/left.ftl">

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
                <a href="${rc.contextPath}/index">HOME</a>
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
                                    <td>一个<span style='color: gainsboro; font-size: 15px'>●</span>代表 20 分钟</td>
                                    <td><span style='color: gainsboro; font-size: 15px'>●  </span>表示该任务未调度执行</td>
                                    <td><span style='color: green; font-size: 15px'>●  </span>表示该任务调度执行成功</td>
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
                                        style="color: white"><#if ip?exists>JOB主机 [${ip!}] 任务执行历史<#else>JOB主机任务执行历史</#if></span></a>
                        </h5>

                        <div class="widget-toolbar">
                            <label id="viewlable">截止时间：</label><input type="text" id="startTime"/>
                            <a id="viewbtn" class="btn btn-primary btn-small" href='#' onClick="time_reflash_view()"><i
                                    class="icon-eye-open">查看</i></a>
                            <span id="split">|</span>
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
                                <#if ip?exists == false>
                                <#list hosts as hostip>
                                	<option>${hostip.ip!}</option>
                                </#list>
                                </#if>
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
    <img src="${rc.contextPath}/img/betop.png" width="66" height="67">
</a>

<div class="feedTool">

    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img
            border="0" src="${rc.contextPath}/img/qq.png" width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img
            border="0" src="${rc.contextPath}/img/x_alt.png" width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>

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
    formatTime: 'H:i',
    format: 'Y-m-d H:i',
    formatDate: 'Y-m-d',
    defaultTime: '10:00',
    timepicker: true,
    timepickerScrollbar: true
});

function GetDateStr(dd, AddDayCount) {
    dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth() + 1;//获取当前月份的日期
    var d = dd.getDate();
    var h = dd.getHours();
    var mm = dd.getMinutes();
    var s = dd.getSeconds();

    if (h < '10') {
        h = '0' + h;
    }
    if (m < '10') {
        m = '0' + m;
    }

    if (d < '10') {
        d = '0' + d;
    }

    if (mm < '10') {
        mm = '0' + mm;
    }
    if (s < '10') {
        s = '0' + s;
    }
    return y + "-" + m + "-" + d + " " + h + ":" + mm + ":" + s;
}

var now_s = "${now_s!}";
var now = new Date(Date.parse(now_s.replace(/-/g, "/")));
var time = GetDateStr(now, 0);


var not_run = "<span style='color: gainsboro; font-size: 12px'>●</span>"
var run_green = "<span style='color: green; font-size: 12px'>●</span>";
var run_red = "<span style='color: red; font-size: 12px'>●</span>";
var run_bule = "<span style='color: #0000ff; font-size: 12px'>●</span>";
var run_yellow = "<span style='color: #ffff00; font-size: 12px'>●</span>";
var ip = "${ip!}";

if (ip != null && ip != "") {
    get_history(ip, time);
}

function get_history(ip, time) {
    $.ajax({
        data: {
            action: "host_history",
            time: time,
            ip: ip
        },
        type: "POST",
        url: "${rc.contextPath}/host_history.do",
        error: function () {
            $("#history").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
            $("#history").addClass("align-center");
        },
        success: function (response, textStatus) {
            var table_body = '<a class="btn btn-primary btn-minier" style="float: right" href="${rc.contextPath}/host_history">返回</a><table class="table table-striped table-bordered table-condensed" >';
            var jsonarray = $.parseJSON(response);

            if (jsonarray.length == 0) {
                $("#history").html("<i class='icon-info-sign icon-large red '>该Job机没有任何任务执行~</i> <a class='btn btn-primary btn-minier' href='${rc.contextPath}/host_history'>返回</a>");
                $("#history").addClass("align-center");
                $('#startTime').addClass("hide");
                $('#viewlable').addClass("hide");
                $('#viewbtn').addClass("hide");
                $('#split').addClass("hide");


            } else {
                var show_time = new Date(Date.parse(time.replace(/-/g, "/")));
                table_body += '<tr><th>任务名</th><th><span style="float:left">时间段 :</span><span class="padding-right-14" style="float:left">' + GetDateStr(show_time, -1) + '</span><span class="padding-left-14" style="float:right">' + GetDateStr(show_time, 1) + '</span></th></tr>';
                $.each(jsonarray, function (i, item) {

                    table_body += " <tr><td valign='left'>"
                            +"<a href='${rc.contextPath}/attempt?taskID="
                            +item.taskId
                            +"' >"
                            +"<span style='color: darkgreen; font-size: 9px'>"
                            + item.taskName
                            + "</span></a></td>"
                            + "<td valign='middle'>"
                            + genrate_body(item.runningMap, show_time)
                            + "</td><tr>";


                });
                table_body += "</table>"
                $("#history").html(table_body);
                $(".atip").tooltip();
            }


        }
    });
}
function reflash_view() {
    var new_time = $('#startTime').val();
    var selected_ip = $("#ip").val();
    if (new_time == null || new_time == "") {

        get_history(selected_ip, time);

    } else {
        new_time += ':00';
        get_history(selected_ip, new_time);
    }


}

function time_reflash_view() {
    var new_time = $('#startTime').val();
    if (ip != null && ip != "null") {

        if (new_time == null || new_time == "") {
            bootbox.confirm("截止时间不能为空！", function (result) {
            });
            return;

        } else {
            new_time += ':00';
        }

        get_history(ip, new_time);
    } else {
        if (new_time == null || new_time == "") {
            bootbox.confirm("截止时间不能为空！", function (result) {
            });
            return;

        } else {
            new_time += ':00';
            var selected_ip = $("#ip").val();
            get_history(selected_ip, new_time);
        }

    }


}

function genrate_body(runningMap, time) {
    var runng_history = runningMap.split(",");
    var ret_body = "";
    var pointCount = 72;
    var runArray = new Array(pointCount);
    for (var i = 0; i < pointCount; i++) {
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

    for (var i = 0; i < pointCount; i++) {

        if (runArray[i] != '-1') {
            ret_body += '<a class="atip tooltip-info" data-toggle="tooltip" data-placement="bottom"  data-original-title="时间区间：' + GetDateStr(new Date(time.getTime() - ( pointCount- i) * 20 * 60 * 1000), 0) + ' ~' + GetDateStr(new Date(time.getTime() - (pointCount - i - 1) * 20 * 60 * 1000), 0) + '"> ' + runArray[i] + '</a>';
        } else {
            ret_body += '<a class="atip tooltip-info" data-toggle="tooltip" data-placement="bottom"  data-original-title="时间区间：' + GetDateStr(new Date(time.getTime() - (pointCount - i) * 20 * 60 * 1000), 0) + ' ~' + GetDateStr(new Date(time.getTime() - (pointCount - i - 1) * 20 * 60 * 1000), 0) + '"> ' + not_run + '</a>';
        }

    }
    return ret_body;
}

</script>
<script src="${rc.contextPath}/js/jquery.validate.min.js" type="text/javascript"></script>
</body>
</html>