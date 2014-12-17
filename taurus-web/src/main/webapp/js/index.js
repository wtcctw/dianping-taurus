var tip;
var onLineBody;
var exceptionBody;
var body = "";
var memTableStyle;
var cpuTableStyle;

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

    cpuTableStyle = function () {
        $('#cputable').dataTable({
            "bAutoWidth": true,
            "bPaginate": true,
            "bFilter": false,
            "bInfo": true,
            "bLengthChange": true,
            "aoColumns": [
                null,
                { "sType": "html-percent", "aTargets": [2] }
            ]
        });
    };

    memTableStyle = function () {
        $('#memtable').dataTable({
            "bAutoWidth": true,
            "bPaginate": true,
            "bFilter": false,
            "bInfo": true,
            "bLengthChange": true,
            "aoColumns": [
                null,
                { "sType": "html-percent", "aTargets": [2] }
            ]


        });
    };
    totalTaskStyle = function () {
        $('#totalTaskTable').dataTable({
            "bAutoWidth": true,
            "bPaginate": true,
            "bFilter": false,
            "bInfo": true,
            "bLengthChange": true,
            "aoColumns": [
                { "sType": "html-percent", "aTargets": [1] },
                { "sType": "html-percent", "aTargets": [2] },
                { "sType": "html-percent", "aTargets": [3] },
                { "sType": "html-percent", "aTargets": [4] },
                { "sType": "html-percent", "aTargets": [5] }
            ]


        });
    };
    userTaskStyle = function () {
        $('#userTaskTable').dataTable({
            "bAutoWidth": true,
            "bPaginate": true,
            "bFilter": false,
            "bInfo": true,
            "bLengthChange": true,
            "aoColumns": [
                null,
                { "sType": "html-percent", "aTargets": [1] },
                { "sType": "html-percent", "aTargets": [2] }
            ]


        });
    };

    groupTaskStyle = function () {
        $('#groupTaskTable').dataTable({
            "bAutoWidth": true,
            "bPaginate": true,
            "bFilter": false,
            "bInfo": true,
            "bLengthChange": true,
            "aoColumns": [
                null,
                { "sType": "html-percent", "aTargets": [1] },
                { "sType": "html-percent", "aTargets": [2] },
                { "sType": "html-percent", "aTargets": [3] }
            ]


        });
    };

    failedJobStyle = function () {
        $('#failedJobTable').dataTable({
            "bAutoWidth": true,
            "bPaginate": true,
            "bFilter": false,
            "bInfo": true,
            "bLengthChange": true,
            "aoColumns": [
                null,
                null
            ]


        });
    };

    totalJobStyle = function () {
        $("#totalJobTable").dataTable({
            "bAutoWidth": true,
            "bPaginate": true,
            "bFilter": false,
            "bInfo": true,
            "bLengthChange": true,
            "aoColumns": [
                null,
                null
            ]


        });
    };

    jobStateStyle = function () {
        $("#jobStateTable").dataTable({
            "bAutoWidth": true,
            "bPaginate": true,
            "bFilter": false,
            "bInfo": true,
            "bLengthChange": true,
            "aoColumns": [
                null,
                null
            ]


        });
    };

})
$(document).ready(function () {

    var starttime;
    var endtime;
    if (id == null || id == "null") {
        starttime = GetDateStr(now, -1);
        if (op == "day") {
            endtime = GetDateStr(now, 1);
        } else {
            endtime = GetDateStr(new Date(), 1);
        }

    } else if (id == "-24") {
        starttime = GetDateStr(now, -1);
        if (op == "day") {
            endtime = GetDateStr(now, 1);
        } else {
            endtime = GetDateStr(new Date(), 1);
        }
    } else if (id == "-168") {
        starttime = GetDateStr(now, -7);
        if (op == "day") {
            endtime = GetDateStr(now, 1);
        } else {
            endtime = GetDateStr(new Date(), 1);
        }
    } else if (id == "-720") {
        starttime = GetDateStr(now, -30);
        if (op == "day") {
            endtime = GetDateStr(now, 1);
        } else {
            endtime = GetDateStr(new Date(), 1);
        }
    } else if (id == "24") {
        if (op == "day") {
            starttime = GetDateStr(now, 0);
            endtime = GetDateStr(now, 1);
        } else {
            starttime = GetDateStr(now, 0);
            endtime = GetDateStr(new Date(), 1);
        }


    } else if (id == "168") {
        if (op == "day") {
            starttime = GetDateStr(now, 0);
            endtime = GetDateStr(now, 7);
        } else {
            starttime = GetDateStr(now, 7);
            endtime = GetDateStr(new Date(), 1);
        }

    } else if (id == "720") {
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


    //if (isAdmin) {
    var onlineNums = 0;
    var exceptionNums = 0;
    var onlineLists = null;
    var exceptionLists = null;
    $.ajax({
        data: {
            action: "host",
            gettype: "all"
        },
        type: "POST",
        url: "/monitor",
        error: function () {

        },
        success: function (response, textStatus) {
            var ips = response.split("#");

            if (ips[0].trim() == "NULL") {
                onlineNums = 0;
                onLineBody = " <i class='icon-info-sign icon-large red '>没有正常机器</i>";
            } else {
                onlineLists = ips[0].split(",");
                onLineBody = "<i class='icon-lightbulb green'></i>" + ips[0].replace(/[,]/g, "<br><i class='icon-lightbulb green'></i>");
                ;
                onlineNums = onlineLists.length;
            }
            if (ips[1].trim() == "NULL") {
                exceptionNums = 0;
                exceptionBody = " <i class='icon-info-sign icon-large green '>当前状态很好，没有异常JOB机器</i>";
            } else {
                exceptionLists = ips[1].split(",");
                exceptionNums = exceptionLists.length;
                exceptionBody = "<i class='icon-lightbulb red'></i>" + ips[1].replace(/[,]/g, "<br><i class='icon-lightbulb red'></i>")
            }
            $("#onlineNums").html(onlineNums.toString());
            $("#exceptionNums").html(exceptionNums.toString());
            var htmlContent = "";
            if (exceptionNums == 0) {
                htmlContent = "<i class='icon-info-sign icon-large green '>当前状态很好，没有异常JOB机器</i> ";
            } else {
                for (var i = 0; i < exceptionNums; i++) {
                    body += "<tr>" +
                        "<td>" + exceptionLists[i] + "</td>" +
                        "<td><a id='down' title='查看job机详情' class='btn  btn-primary btn-minier' href='hosts.jsp?hostName=" + exceptionLists[i] + "'>详情</a></td>"
                    "</tr>"
                }
                htmlContent = ' <table  class="table table-striped " id = "jobStateTable">'
                    +'<thead><td>IP</td><td>操作</td></thead>'
                    + '        <tbody>'
                    + body
                    + '        </tbody>'
                    + '    </table>'
                    + '    <div class="controller">'
                    + '    </div>';


            }

            $("#exceptionJob").html(htmlContent);
            jobStateStyle();
            var placeholder = $('#piechart-placeholder').css({'width': '90%', 'min-height': '200px'});
            var data = [
                { label: "正常", data: onlineNums, color: "#68BC31"},
                { label: "失联", data: exceptionNums, color: "#AF4E96"}
            ]
            Number.prototype.toFixed = function (fractionDigits) {
                return  (parseInt(this * Math.pow(10, fractionDigits) + 0.5) / Math.pow(10, fractionDigits)).toString();
            }

            function drawPieChart(placeholder, data, position) {
                $.plot(placeholder, data, {
                    series: {
                        pie: {
                            show: true,
                            tilt: 1,
                            highlight: {
                                opacity: 0.25
                            },
                            stroke: {
                                color: '#fff',
                                width: 2
                            },
                            startAngle: 2
                        }
                    },
                    legend: {
                        show: true,
                        position: position || "ne",
                        labelBoxBorderColor: null,
                        margin: [-30, 15]
                    },
                    grid: {
                        hoverable: true,
                        clickable: true
                    }
                })
            }

            drawPieChart(placeholder, data);

            /**
             we saved the drawing function and the data to redraw with different position later when switching to RTL mode dynamically
             so that's not needed actually.
             */
            placeholder.data('chart', data);
            placeholder.data('draw', drawPieChart);


            //pie chart tooltip example
            var tooltip = $("<div class='tooltip top in align-left'><div class='tooltip-inner align-left'></div></div>").hide().appendTo('body');
            var previousPoint = null;

            placeholder.on('plothover', function (event, pos, item) {
                if (item) {
                    if (previousPoint != item.seriesIndex) {
                        previousPoint = item.seriesIndex;

                        if (item.series['label'] == "正常") {
                            tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<br>' + onLineBody;
                        } else if (item.series['label'] == "失联") {
                            tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<br>' + exceptionBody;
                        }
                        else {
                            tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%'

                        }

                        tooltip.show().children(0).html(tip);
                    }
                    tooltip.css({top: pos.pageY + 10, left: pos.pageX + 10});
                } else {
                    tooltip.hide();
                    previousPoint = null;
                }

            });

        }


    });


    var totalBody = "";

    $.ajax({
        data: {
            action: "totaltaskload",
            start: starttime,
            end: endtime
        },
        type: "POST",
        url: "/monitor",
        error: function () {
            $("#totalJob").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
            $("#totalJob").addClass("align-center");
        },
        success: function (response, textStatus) {
            var jsonarray = $.parseJSON(response);
            $.each(jsonarray, function (i, item) {
                totalBody += "<tr>" +
                    "<td>" + item.execHost + "</td>" +
                    "<td>" + item.totaltask + "</td>"+"</tr>"
            });
            var topLists = ' <table  class="table table-striped table-bordered table-hover " id="totalJobTable">'
                + '<thead><tr><th>IP</th>  <th>执行任务数</th> </tr> </thead>'
                + '        <tbody>'
                + totalBody
                + '        </tbody>'
                + '    </table>';

            $("#totalJob").html(topLists);

            totalJobStyle();
        }


    });

    var failBody = "";

    $.ajax({
        data: {
            action: "failedtaskload",
            start: starttime,
            end: endtime
        },
        type: "POST",
        url: "/monitor",
        error: function () {
            $("#failedJob").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
            $("#failedJob").addClass("align-center");
        },
        success: function (response, textStatus) {
            var jsonarray = $.parseJSON(response);
            var topLists = ""
            if(jsonarray != null&& jsonarray.length > 0){
                $.each(jsonarray, function (i, item) {
                    failBody += "<tr>" +
                        "<td>" + item.execHost + "</td>" +
                        "<td>" + item.totaltask + "</td>"+"</tr>";
                });
                topLists = ' <table  class="table table-striped table-bordered table-hover " id = "failedJobTable">'
                    + '<thead><tr><th>IP</th>  <th>执行任务数</th> </tr> </thead>'
                    + '        <tbody>'
                    + failBody
                    + '        </tbody>'
                    + '    </table>';
            }else{
                topLists="<i class='icon-info-sign icon-large green '>没有执行失败的任务！</i> ";
            }



            $("#failedJob").html(topLists);

            failedJobStyle();
        }


    });

    reflash(null);


    //  } else {
    var succNums = 0;
    var failedNums = 0;
    var killNums = 0;
    var timeoutNums = 0;
    var congestNUms = 0;
    var succTaskNums = 0;
    var congestTaskNums = 0;
    var failedTaskNums = 0;
    var killTaskNums = 0;
    var timeoutTaskNums = 0;
    var congestTaskNums = 0;

    var succLists = new Array();
    var failedLists = new Array();
    var killLists = new Array();
    var timeoutLists = new Array();
    var congestLists = new Array();
    var succBody = "";
    var failedBody = "";
    var killBody = "";
    var timeoutBody = "";
    var congestBody = "";
    $.ajax({
        data: {
            action: "usertask",
            username: username,
            start: starttime,
            end: endtime
        },
        type: "POST",
        url: "/monitor",
        error: function () {
            $("#user-widget-main").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
            $("#user-widget-main").addClass("align-center");
        },
        success: function (response, textStatus) {
            var userTaskListBody = "";
            var jsonarray = $.parseJSON(response);
            $.each(jsonarray, function (i, item) {

                if (item.status == "success" && item.nums != 0) {
                    succNums += item.nums;

                    succLists[succTaskNums] = item.taskName;
                    succTaskNums++;
                    succBody += "<i class='icon-tasks'></i>" + item.taskName + ": " + item.nums + "<br>"
                    userTaskListBody += "<tr>" +
                        "<td>" + item.taskName + "</td>" +
                        "<td>成功</td>" +
                        "<td>" + item.nums + "</td>" +
                        "</tr>";

                } else if (item.status == "failed") {
                    if (item.nums != 0) {
                        failedNums += item.nums;
                        failedLists[failedNums] = item.taskName;
                        failedTaskNums++;
                        failedBody += "<i class='icon-tasks'></i>" + item.taskName + ": " + item.nums + "<br>"
                        userTaskListBody += "<tr>" +
                            "<td>" + item.taskName + "</td>" +
                            "<td>失败</td>" +
                            "<td>" + item.nums + "</td>" +
                            "</tr>";
                    }

                } else if (item.status == "killed") {
                    if (item.nums != 0) {
                        killNums += item.nums;
                        killLists[killNums] = item.taskName;
                        killTaskNums++;
                        killBody += "<i class='icon-tasks'></i>" + item.taskName + ": " + item.nums + "<br>"
                        userTaskListBody += "<tr>" +
                            "<td>" + item.taskName + "</td>" +
                            "<td>杀死</td>" +
                            "<td>" + item.nums + "</td>" +
                            "</tr>";
                    }

                } else if (item.status == "timeout") {
                    if (item.nums != 0) {
                        timeoutNums += item.nums;
                        timeoutLists[timeoutNums] = item.taskName;
                        timeoutTaskNums++;
                        timeoutBody += "<i class='icon-tasks'></i>" + item.taskName + ": " + item.nums + "<br>"
                        userTaskListBody += "<tr>" +
                            "<td>" + item.taskName + "</td>" +
                            "<td>超时</td>" +
                            "<td>" + item.nums + "</td>" +
                            "</tr>";
                    }

                }
                else if (item.status == "congest") {
                    if (item.nums != 0) {
                        congestNUms += item.nums;
                        congestLists[congestNUms] = item.taskName;
                        congestTaskNums++;
                        congestBody += "<i class='icon-tasks'></i>" + item.taskName + ": " + item.nums + "<br>"
                        userTaskListBody += "<tr>" +
                            "<td>" + item.taskName + "</td>" +
                            "<td>拥堵</td>" +
                            "<td>" + item.nums + "</td>" +
                            "</tr>";
                    }

                }
            });

            if (succNums != 0 || failedNums != 0 || killNums != 0 || timeoutNums != 0 || congestNUms!= 0) {
                $("#succtask").html(succNums.toString());
                $("#failtask").html(failedNums.toString());
                $("#killtask").html(killNums.toString());
                $("#timeouttask").html(timeoutNums.toString());
                $("#congesttask").html(congestNUms.toString());

            } else {
                $("#user-widget-main").html("<i class='icon-info-sign icon-large red '>今天没有任务调度～</i>");
                $("#user-widget-main").addClass("align-center");
            }
            var topUserTaskLists = ' <table  class="table table-striped table-bordered table-hover " id="userTaskTable">'
                + '<thead><tr><th>任务名</th>  <th>状态</th> <th>次数</th> </tr> </thead>'
                + '        <tbody>'
                + userTaskListBody
                + '        </tbody>'
                + '    </table>'
            $("#mytasklist").html(topUserTaskLists);
            userTaskStyle();

            if (succNums == 0 && failedNums == 0) {
                $("#user-widget-main").html("<i class='icon-info-sign icon-large red'>今天没有任务调度～</i>");
                $("#user-widget-main").addClass("align-center");
            } else {

                var placeholder = $('#mytasks').css({'width': '90%', 'height': '200px'});
                var data = [
                    { label: "正常", data: succNums, color: "#68BC31"},
                    { label: "失败", data: failedNums, color: "#FF0000"},
                    { label: "杀死", data: killNums, color: "#AF4E96"},
                    { label: "超时", data: timeoutNums, color: "#FFD306"},
                    { label: "拥堵", data: congestNUms, color: "#8E8E8E"}
                ]
                Number.prototype.toFixed = function (fractionDigits) {
                    return  (parseInt(this * Math.pow(10, fractionDigits) + 0.5) / Math.pow(10, fractionDigits)).toString();
                }

                function drawPieChart(placeholder, data, position) {
                    $.plot(placeholder, data, {
                        series: {
                            pie: {
                                show: true,
                                tilt: 1,
                                highlight: {
                                    opacity: 0.25
                                },
                                stroke: {
                                    color: '#fff',
                                    width: 2
                                },
                                startAngle: 2
                            }
                        },
                        legend: {
                            show: true,
                            position: position || "ne",
                            labelBoxBorderColor: null,
                            margin: [-30, 15]
                        },
                        grid: {
                            hoverable: true,
                            clickable: true
                        }
                    })
                }

                drawPieChart(placeholder, data);

                /**
                 we saved the drawing function and the data to redraw with different position later when switching to RTL mode dynamically
                 so that's not needed actually.
                 */
                placeholder.data('chart', data);
                placeholder.data('draw', drawPieChart);


                //pie chart tooltip example
                var tooltip = $("<div class='tooltip top in align-left'><div class='tooltip-inner align-left'></div></div>").hide().appendTo('body');
                var previousPoint = null;

                placeholder.on('plothover', function (event, pos, item) {
                    if (item) {
                        if (previousPoint != item.seriesIndex) {
                            previousPoint = item.seriesIndex;

                            if (item.series['label'] == "正常") {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<br>' + succBody;
                            } else if (item.series['label'] == "失败") {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<br>' + failedBody;
                            }
                            else if (item.series['label'] == "杀死") {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<br>' + killBody;
                            } else if (item.series['label'] == "超时") {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<br>' + timeoutBody;
                            }
                            else if (item.series['label'] == "拥堵") {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<br>' + congestBody;
                            }
                            else {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%'

                            }

                            tooltip.show().children(0).html(tip);
                        }
                        tooltip.css({top: pos.pageY + 10, left: pos.pageX + 10});
                    } else {
                        tooltip.hide();
                        previousPoint = null;
                    }

                });

            }

        }


    });


    var groupSuccNums = 0;
    var groupFailedNums = 0;
    var groupKillNums = 0;
    var groupTimeoutNums = 0;
    var groupCongestNums = 0;
    var groupSuccTaskNums = 0;
    var groupFailedTaskNums = 0;
    var groupKillTaskNums = 0;
    var groupTimeoutTaskNums = 0;
    var groupCongestTaskNums = 0;
    var groupSuccLists = new Array();

    var groupFailedLists = new Array();
    var groupKillLists = new Array();
    var groupTimeoutLists = new Array();
    var groupCongestLists = new Array();

    var groupSuccBody = "";
    var groupFailedBody = "";
    var groupKillBody = "";
    var groupTimeoutBody = "";
    var groupCongestBody = "";

    $.ajax({
        data: {
            action: "grouptask",
            username: username,
            start: starttime,
            end: endtime
        },
        type: "POST",
        url: "/monitor",
        error: function () {
            $("#group-widget-main").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
            $("#group-widget-main").addClass("align-center");
        },
        success: function (response, textStatus) {
            var groupTaskListBody = "";
            var jsonarray = $.parseJSON(response);
            $.each(jsonarray, function (i, item) {
                var usericon = "";
                if (username == item.creator) {
                    usericon = "&nbsp;&nbsp;<i class='icon-user green'></i>";
                } else {
                    usericon = "&nbsp;&nbsp;<i class='icon-user'></i>";
                }
                if (item.status == "success" && item.nums != 0) {
                    groupSuccNums += item.nums;

                    groupSuccLists[groupSuccTaskNums] = item.taskName;
                    groupSuccTaskNums++;

                    groupSuccBody += "<i class='icon-tasks'></i>" + item.taskName + ": " + item.nums + usericon + item.creator + "<br>"
                    groupTaskListBody += "<tr>" +
                        "<td>" + item.taskName + "</td>" +
                        "<td>成功</td>" +
                        "<td>" + item.nums + "</td>" +
                        "<td>" + item.creator + "</td>" +
                        "</tr>";
                } else if (item.status == "failed"){
                    if (item.nums != 0) {
                        groupFailedNums += item.nums;
                        groupFailedLists[groupFailedNums] = item.taskName;
                        groupFailedTaskNums++;
                        groupFailedBody += "<i class='icon-tasks'></i>" + item.taskName + ": " + item.nums + usericon + item.creator + "<br>"
                        groupTaskListBody += "<tr>" +
                            "<td>" + item.taskName + "</td>" +
                            "<td>失败</td>" +
                            "<td>" + item.nums + "</td>" +
                            "<td>" + item.creator + "</td>" +
                            "</tr>";
                    }

                }else if (item.status == "killed"){
                    if (item.nums != 0) {
                        groupKillNums += item.nums;
                        groupKillLists[groupKillNums] = item.taskName;
                        groupKillTaskNums++;
                        groupKillBody += "<i class='icon-tasks'></i>" + item.taskName + ": " + item.nums + usericon + item.creator + "<br>"
                        groupTaskListBody += "<tr>" +
                            "<td>" + item.taskName + "</td>" +
                            "<td>杀死</td>" +
                            "<td>" + item.nums + "</td>" +
                            "<td>" + item.creator + "</td>" +
                            "</tr>";
                    }

                }else if (item.status == "timeout"){
                    if (item.nums != 0) {
                        groupTimeoutNums += item.nums;
                        groupTimeoutLists[groupTimeoutNums] = item.taskName;
                        groupTimeoutTaskNums++;
                        groupTimeoutBody += "<i class='icon-tasks'></i>" + item.taskName + ": " + item.nums + usericon + item.creator + "<br>"
                        groupTaskListBody += "<tr>" +
                            "<td>" + item.taskName + "</td>" +
                            "<td>超时</td>" +
                            "<td>" + item.nums + "</td>" +
                            "<td>" + item.creator + "</td>" +
                            "</tr>";
                    }

                }else if (item.status == "congest"){
                    if (item.nums != 0) {
                        groupCongestNums += item.nums;
                        groupCongestLists[groupCongestNums] = item.taskName;
                        groupCongestTaskNums++;
                        groupCongestBody += "<i class='icon-tasks'></i>" + item.taskName + ": " + item.nums + usericon + item.creator + "<br>"
                        groupTaskListBody += "<tr>" +
                            "<td>" + item.taskName + "</td>" +
                            "<td>拥堵</td>" +
                            "<td>" + item.nums + "</td>" +
                            "<td>" + item.creator + "</td>" +
                            "</tr>";
                    }

                }

            });

            if (groupSuccNums != 0 || groupFailedNums != 0 ||  groupKillNums != 0 || groupTimeoutNums != 0 || groupCongestNums != 0) {
                $("#groupsucctask").html(groupSuccNums.toString());
                $("#groupfailtask").html(groupFailedNums.toString());
                $("#groupkilltask").html(groupKillNums.toString());
                $("#grouptimouttask").html(groupTimeoutNums.toString());
                $("#groupcongesttask").html(groupCongestTaskNums.toString());
            } else {
                $("#group-widget-main").html("<i class='icon-info-sign icon-large red '>今天没有任务调度～</i>");
                $("#group-widget-main").addClass("align-center");
            }
            var topGroupTaskLists = ' <table  class="table table-striped table-bordered table-hover " id="groupTaskTable">'
                + '<thead><tr><th>任务名</th>  <th>状态</th> <th>次数</th> <th>创建者</th> </tr> </thead>'
                + '        <tbody>'
                + groupTaskListBody
                + '        </tbody>'
                + '    </table>'
            $("#grouptasklist").html(topGroupTaskLists);
            groupTaskStyle();
            if (groupSuccNums != 0 || groupFailedNums != 0) {
                var placeholder = $('#grouptasks').css({'width': '90%', 'min-height': '200px'});
                var data = [
                    { label: "正常", data: groupSuccNums, color: "#68BC31"},
                    { label: "失败", data: groupFailedNums, color: "#FF0000"},
                    { label: "杀死", data: groupKillNums, color: "#AF4E96"},
                    { label: "超时", data: groupTimeoutNums, color: "#FFD306"},
                    { label: "拥堵", data: groupCongestNums, color: "#8E8E8E"}
                ]
                Number.prototype.toFixed = function (fractionDigits) {
                    return  (parseInt(this * Math.pow(10, fractionDigits) + 0.5) / Math.pow(10, fractionDigits)).toString();
                }

                function drawPieChart(placeholder, data, position) {
                    $.plot(placeholder, data, {
                        series: {
                            pie: {
                                show: true,
                                tilt: 1,
                                highlight: {
                                    opacity: 0.25
                                },
                                stroke: {
                                    color: '#fff',
                                    width: 2
                                },
                                startAngle: 2
                            }
                        },
                        legend: {
                            show: true,
                            position: position || "ne",
                            labelBoxBorderColor: null,
                            margin: [-30, 15]
                        },
                        grid: {
                            hoverable: true,
                            clickable: true
                        }
                    })
                }

                drawPieChart(placeholder, data);

                /**
                 we saved the drawing function and the data to redraw with different position later when switching to RTL mode dynamically
                 so that's not needed actually.
                 */
                placeholder.data('chart', data);
                placeholder.data('draw', drawPieChart);


                //pie chart tooltip example
                var tooltip = $("<div class='tooltip top in align-left'><div class='tooltip-inner align-left'></div></div>").hide().appendTo('body');
                var previousPoint = null;

                placeholder.on('plothover', function (event, pos, item) {
                    if (item) {
                        if (previousPoint != item.seriesIndex) {
                            previousPoint = item.seriesIndex;

                            if (item.series['label'] == "正常") {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<br>' + groupSuccBody;
                            } else if (item.series['label'] == "失败") {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<br>' + groupFailedBody;
                            }else if (item.series['label'] == "杀死") {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<br>' + groupKillBody;
                            }else if (item.series['label'] == "超时") {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<br>' + groupTimeoutBody;
                            }else if (item.series['label'] == "拥堵") {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<br>' + groupCongestBody;
                            }

                            else {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%'

                            }

                            tooltip.show().children(0).html(tip);
                        }
                        tooltip.css({top: pos.pageY + 10, left: pos.pageX + 10});
                    } else {
                        tooltip.hide();
                        previousPoint = null;
                    }

                });
            } else {
                $("#group-widget-main").html("<i class='icon-info-sign icon-large red'>今天没有任务调度～</i>");
                $("#group-widget-main").addClass("align-center");

            }
        }


    });


    var totalSuccNums = 0;
    var totalFailedNums = 0;
    var totalKillNums = 0;
    var totalTimeoutNums = 0;
    var totalCongestNums = 0;
    var totalSuccTaskNums = 0;
    var totalFailedTaskNums = 0;
    var totalKillTaskNums = 0;
    var totalTimeoutTaskNums = 0;
    var totalCongestTaskNums = 0;
    var totalSuccLists = new Array();

    var totalFailedLists = new Array();
    var totalKillLists = new Array();
    var totalTimeoutLists = new Array();
    var totalCongestLists = new Array();

    var totalSuccBody = "";
    var totalFailedBody = "";
    var totalKillBody = "";
    var totalTimeoutBody = "";
    var totalCongestBody = "";

    $.ajax({
        data: {
            action: "totaltask",
            start: starttime,
            end: endtime
        },
        type: "POST",
        url: "/monitor",
        error: function () {
            $("#total-widget-main").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
            $("#total-widget-main").addClass("align-center");
        },
        success: function (response, textStatus) {
            var totalTaskListBody = "";
            var jsonarray = $.parseJSON(response);
            $.each(jsonarray, function (i, item) {
                var usericon = "";
                var groupicon ="";
                var taskicon ="";

                if (username == item.creator) {
                    usericon = "&nbsp;&nbsp;<i class='icon-user green'></i>";
                    groupicon = "&nbsp;&nbsp;<i class='icon-group green'></i>";
                    taskicon = "<i class='icon-tasks green'></i>"
                } else {
                    usericon = "&nbsp;&nbsp;<i class='icon-user'></i>";
                    groupicon = "&nbsp;&nbsp;<i class='icon-group'></i>";
                    taskicon = "<i class='icon-tasks'></i>"
                }

                if (item.status == "success" && item.nums != 0) {
                    totalSuccNums += item.nums;

                    totalSuccLists[totalSuccTaskNums] = item.taskName;
                    totalSuccTaskNums++;

                    var group = item.group;

                    if(group == null || group == ""){
                        group = "未分组";
                    }

                    totalSuccBody += taskicon + item.taskName + ": " + item.nums + usericon + item.creator + groupicon + group +"<br>"
                    totalTaskListBody += "<tr>" +
                        "<td>" + group + "</td>" +
                        "<td>" + item.taskName + "</td>" +
                        "<td>成功</td>" +
                        "<td>" + item.nums + "</td>" +
                        "<td>" + item.creator + "</td>" +
                        "</tr>";
                } else if (item.status == "failed"){
                    if (item.nums != 0) {
                        totalFailedNums += item.nums;
                        totalFailedLists[totalFailedNums] = item.taskName;
                        totalFailedTaskNums++;
                        var group = item.group;

                        if(group == null || group == ""){
                            group = "未分组";
                        }

                        totalFailedBody += taskicon + item.taskName + ": " + item.nums + usericon + item.creator + groupicon + group + "<br>"
                        totalTaskListBody += "<tr>" +
                            "<td>" + group + "</td>" +
                            "<td>" + item.taskName + "</td>" +
                            "<td>失败</td>" +
                            "<td>" + item.nums + "</td>" +
                            "<td>" + item.creator + "</td>" +
                            "</tr>";
                    }

                }else if (item.status == "killed"){
                    if (item.nums != 0) {
                        totalKillNums += item.nums;
                        totalKillLists[groupKillNums] = item.taskName;
                        totalKillTaskNums++;

                        var group = item.group;

                        if(group == null || group == ""){
                            group = "未分组";
                        }

                        totalKillBody += taskicon+ item.taskName + ": " + item.nums + usericon + item.creator + groupicon + group + "<br>"
                        totalTaskListBody += "<tr>" +
                            "<td>" + group + "</td>" +
                            "<td>" + item.taskName + "</td>" +
                            "<td>杀死</td>" +
                            "<td>" + item.nums + "</td>" +
                            "<td>" + item.creator + "</td>" +
                            "</tr>";
                    }

                }else if (item.status == "timeout"){
                    if (item.nums != 0) {
                        totalTimeoutNums += item.nums;
                        totalTimeoutLists[groupTimeoutNums] = item.taskName;
                        totalTimeoutTaskNums++;

                        var group = item.group;

                        if(group == null || group == ""){
                            group = "未分组";
                        }

                        totalTimeoutBody += taskicon + item.taskName + ": " + item.nums + usericon + item.creator + groupicon + group + "<br>"
                        totalTaskListBody += "<tr>" +
                            "<td>" + group + "</td>" +
                            "<td>" + item.taskName + "</td>" +
                            "<td>超时</td>" +
                            "<td>" + item.nums + "</td>" +
                            "<td>" + item.creator + "</td>" +
                            "</tr>";
                    }

                }else if (item.status == "congest"){
                    if (item.nums != 0) {
                        totalCongestNums += item.nums;
                        totalCongestLists[groupCongestNums] = item.taskName;
                        totalCongestTaskNums++;

                        var group = item.group;

                        if(group == null || group == ""){
                            group = "未分组";
                        }

                        totalCongestBody += taskicon + item.taskName + ": " + item.nums + usericon + item.creator + groupicon + group + "<br>"
                        totalTaskListBody += "<tr>" +
                            "<td>" + group + "</td>" +
                            "<td>" + item.taskName + "</td>" +
                            "<td>拥堵</td>" +
                            "<td>" + item.nums + "</td>" +
                            "<td>" + item.creator + "</td>" +
                            "</tr>";
                    }

                }

            });

            // 路径配置
            require.config({
                paths: {
                    echarts: '../lib/dist'
                }
            });
            // 使用
            require(
                [
                    'echarts',
                    'echarts/chart/gauge' // 使用柱状图就加载bar模块，按需加载
                ],
                function (ec) {
                    // 基于准备好的dom，初始化echarts图表
                    var myChart = ec.init(document.getElementById('total-chart'));
                    var succ =  ((totalSuccNums * 1.0)/(1.0*(totalSuccNums+totalFailedNums+totalKillNums+totalFailedNums+totalTimeoutNums+totalCongestNums)))*100
                    var option = {
                        tooltip : {
                            formatter: "{a} <br/>{b} : {c}%"
                        },
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                restore : {show: true},
                                saveAsImage : {show: true}
                            }
                        },
                        series : [
                            {
                                name:'',
                                type:'gauge',
                                startAngle: 180,
                                endAngle: 0,
                                center : ['50%', '90%'],    // 默认全局居中
                                radius : 220,
                                axisLine: {            // 坐标轴线
                                    lineStyle: {       // 属性lineStyle控制线条样式
                                        width: 120
                                    }
                                },
                                axisTick: {            // 坐标轴小标记
                                    splitNumber: 10,   // 每份split细分多少段
                                    length :12        // 属性length控制线长
                                },
                                axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
                                    formatter: function(v){
                                        switch (v+''){
                                            case '10': return '低';
                                            case '50': return '中';
                                            case '90': return '高';
                                            default: return '';
                                        }
                                    },
                                    textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                        color: '#fff',
                                        fontSize: 15,
                                        fontWeight: 'bolder'
                                    }
                                },
                                pointer: {
                                    width:50,
                                    length: '90%',
                                    color: 'rgba(255, 255, 255, 0.8)'
                                },
                                title : {
                                    show : true,
                                    offsetCenter: [0, '-60%'],       // x, y，单位px
                                    textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                        color: '#fff',
                                        fontSize: 30
                                    }
                                },
                                detail : {
                                    show : true,
                                    backgroundColor: 'rgba(0,0,0,0)',
                                    borderWidth: 0,
                                    borderColor: '#ccc',
                                    width: 100,
                                    height: 40,
                                    offsetCenter: [0, -40],       // x, y，单位px
                                    formatter:'{value}%',
                                    textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                        fontSize : 50
                                    }
                                },
                                data:[{value:succ.toFixed(2), name: '任务成功率'}]
                            }
                        ]
                    };


                    var macarons = getTheme();
                    myChart.setTheme(macarons);
                    // 为echarts对象加载数据
                    myChart.setOption(option);
                }
            );

            if (totalSuccNums != 0 || totalFailedNums != 0 ||  totalKillNums != 0 || totalTimeoutNums != 0 || groupCongestNums != 0) {
                $("#totalsucctask").html(totalSuccNums.toString());
                $("#totalfailtask").html(totalFailedNums.toString());
                $("#totalkilltask").html(totalKillNums.toString());
                $("#totaltimouttask").html(totalTimeoutNums.toString());
                $("#totalcongesttask").html(totalCongestTaskNums.toString());
            } else {
                $("#total-widget-main").html("<i class='icon-info-sign icon-large red '>今天没有任务调度～</i>");
                $("#total-widget-main").addClass("align-center");
            }
            var topTotalTaskLists = ' <table  class="table table-striped table-bordered table-hover align-center" id="totalTaskTable">'
                + '<thead><tr><th>组名</th><th>任务名</th>  <th>状态</th> <th>次数</th> <th>创建者</th> </tr> </thead>'
                + '        <tbody>'
                + totalTaskListBody
                + '        </tbody>'
                + '    </table>'
            $("#totaltasklist").html(topTotalTaskLists);
            totalTaskStyle();
            if (totalSuccNums != 0 || totalFailedNums != 0) {
                var placeholder = $('#totaltasks').css({'width': '90%', 'min-height': '200px'});
                var data = [
                    { label: "正常", data: totalSuccNums, color: "#68BC31"},
                    { label: "失败", data: totalFailedNums, color: "#FF0000"},
                    { label: "杀死", data: totalKillNums, color: "#AF4E96"},
                    { label: "超时", data: totalTimeoutNums, color: "#FFD306"},
                    { label: "拥堵", data: totalCongestNums, color: "#8E8E8E"}
                ]
                Number.prototype.toFixed = function (fractionDigits) {
                    return  (parseInt(this * Math.pow(10, fractionDigits) + 0.5) / Math.pow(10, fractionDigits)).toString();
                }

                function drawPieChart(placeholder, data, position) {
                    $.plot(placeholder, data, {
                        series: {
                            pie: {
                                show: true,
                                tilt: 1,
                                highlight: {
                                    opacity: 0.25
                                },
                                stroke: {
                                    color: '#fff',
                                    width: 2
                                },
                                startAngle: 2
                            }
                        },
                        legend: {
                            show: true,
                            position: position || "ne",
                            labelBoxBorderColor: null,
                            margin: [-30, 15]
                        },
                        grid: {
                            hoverable: true,
                            clickable: true
                        }
                    })
                }






                drawPieChart(placeholder, data);

                /**
                 we saved the drawing function and the data to redraw with different position later when switching to RTL mode dynamically
                 so that's not needed actually.
                 */
                placeholder.data('chart', data);
                placeholder.data('draw', drawPieChart);


                //pie chart tooltip example
                var tooltip = $("<div class='tooltip top in align-left'><div class='tooltip-inner align-left'></div></div>").hide().appendTo('body');
                var previousPoint = null;

                placeholder.on('plothover', function (event, pos, item) {
                    if (item) {
                        if (previousPoint != item.seriesIndex) {
                            previousPoint = item.seriesIndex;

                            if (item.series['label'] == "正常") {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<hr>' + totalSuccBody;
                            } else if (item.series['label'] == "失败") {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<hr>' + totalFailedBody;
                            }else if (item.series['label'] == "杀死") {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<hr>' + totalKillBody;
                            }else if (item.series['label'] == "超时") {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<hr>' + totalTimeoutBody;
                            }else if (item.series['label'] == "拥堵") {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%<hr>' + totalCongestBody;
                            }

                            else {
                                tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%'

                            }

                            tooltip.show().children(0).html(tip);
                        }
                        tooltip.css({top: pos.pageY + 10, left: pos.pageX + 10});
                    } else {
                        tooltip.hide();
                        previousPoint = null;
                    }

                });
            } else {
                $("#total-widget-main").html("<i class='icon-info-sign icon-large red'>今天没有任务调度～</i>");
                $("#total-widget-main").addClass("align-center");

            }
        }


    });


    // }


    function reflash(queryType) {
        var cpuLoadBody = "";
        var memLoadBody = "";
        $.ajax({
            async: true,
            data: {
                action: "hostload",
                queryType: queryType

            },
            type: "POST",
            url: "/monitor",
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


    jQuery(function ($) {
        $('#totalstarttime').datepicker({autoclose: true}).next().on(ace.click_event, function () {
            $(this).prev().focus();
        });
        $('#totalendtime').datepicker({autoclose: true}).next().on(ace.click_event, function () {
            $(this).prev().focus();
        });

        $('#failstarttime').datepicker({autoclose: true}).next().on(ace.click_event, function () {
            $(this).prev().focus();
        });
        $('#failendtime').datepicker({autoclose: true}).next().on(ace.click_event, function () {
            $(this).prev().focus();
        });
    });
});

