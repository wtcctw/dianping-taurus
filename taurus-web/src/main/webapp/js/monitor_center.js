var tip;
var body = "";

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


    userTaskStyle = function () {
        $('#userTaskTable').dataTable({
            "bAutoWidth": true,
            "bPaginate": true,
            "bFilter": true,
            "bInfo": true,
            "bLengthChange": true,
            "aaSorting": [[ 2, "desc" ]]


        });
    };

    groupTaskStyle = function () {
        $('#groupTaskTable').dataTable({
            "bAutoWidth": true,
            "bPaginate": true,
            "bFilter": true,
            "bInfo": true,
            "bLengthChange": true,
            "aaSorting": [[ 2, "desc" ]]


        });
    };


})
$(document).ready(function () {

    var starttime;
    var endtime;
    if (id == null || id == "null") {
        starttime = GetDateStr(now, 0);
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

    reflash_data(starttime, endtime);



});


function reflash_view(){
    var start = $("#startTime").val();
    var end = $("#endTime").val();
    if (start == null|| end == null || start ==""||end==""){
        bootbox.confirm("开始或结束不能为空！", function(result) {

        });
    }else if (start >= end){
        bootbox.confirm("结束时间应该大于开始时间！", function(result) {

        });
    }else{
        reflash_data(start,end);
    }
}


function reflash_data(starttime, endtime){
    var loading = '<div class="loadIcon"><div></div><div></div><div></div><div></div> </div>';

    $("#mytasklist").html(loading);
    $("#grouptasklist").html(loading);
    $("#mytasks").html(loading);
    $("#grouptasks").html(loading);

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
        url: "/monitor_center",
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
                        "<td><a id='attempts' class='btn btn-primary btn-small' href='attempt.jsp?taskID=" + item.taskId + "' target= 'blank'>查看详情</a></td>"+
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
                            "<td><a id='attempts' class='btn btn-primary btn-small' href='attempt.jsp?taskID=" + item.taskId + "' target= 'blank'>查看详情</a></td>"+
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
                            "<td><a id='attempts' class='btn btn-primary btn-small' href='attempt.jsp?taskID=" + item.taskId + "' target= 'blank'>查看详情</a></td>"+
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
                            "<td><a id='attempts' class='btn btn-primary btn-small' href='attempt.jsp?taskID=" + item.taskId + "' target= 'blank'>查看详情</a></td>"+
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
                            "<td><a id='attempts' class='btn btn-primary btn-small' href='attempt.jsp?taskID=" + item.taskId + "' target= 'blank'>查看详情</a></td>"+
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
                + '<thead><tr><th>任务名</th>  <th>状态</th> <th>次数</th> <th>操作</th></tr> </thead>'
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
        url: "/monitor_center",
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
                    if(item.creator != username){
                        groupTaskListBody += "<tr>" +
                            "<td>" + item.taskName + "</td>" +
                            "<td>成功</td>" +
                            "<td>" + item.nums + "</td>" +
                            "<td>" + item.creator + "</td>" +
                            "<td><a id='attempts' class='btn btn-primary btn-small' href='attempt.jsp?taskID=" + item.taskId + "' target= 'blank'>查看详情</a></td>"+
                            "</tr>";
                    }


                } else if (item.status == "failed"){
                    if (item.nums != 0) {
                        groupFailedNums += item.nums;
                        groupFailedLists[groupFailedNums] = item.taskName;
                        groupFailedTaskNums++;
                        groupFailedBody += "<i class='icon-tasks'></i>" + item.taskName + ": " + item.nums + usericon + item.creator + "<br>"
                        if(item.creator != username) {
                            groupTaskListBody += "<tr>" +
                                "<td>" + item.taskName + "</td>" +
                                "<td>失败</td>" +
                                "<td>" + item.nums + "</td>" +
                                "<td>" + item.creator + "</td>" +
                                "<td><a id='attempts' class='btn btn-primary btn-small' href='attempt.jsp?taskID=" + item.taskId + "' target= 'blank'>查看详情</a></td>" +
                                "</tr>";
                        }
                    }

                }else if (item.status == "killed"){
                    if (item.nums != 0) {
                        groupKillNums += item.nums;
                        groupKillLists[groupKillNums] = item.taskName;
                        groupKillTaskNums++;
                        groupKillBody += "<i class='icon-tasks'></i>" + item.taskName + ": " + item.nums + usericon + item.creator + "<br>"
                        if(item.creator != username) {
                            groupTaskListBody += "<tr>" +
                                "<td>" + item.taskName + "</td>" +
                                "<td>杀死</td>" +
                                "<td>" + item.nums + "</td>" +
                                "<td>" + item.creator + "</td>" +
                                "<td><a id='attempts' class='btn btn-primary btn-small' href='attempt.jsp?taskID=" + item.taskId + "' target= 'blank'>查看详情</a></td>" +
                                "</tr>";
                        }
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
                            "<td><a id='attempts' class='btn btn-primary btn-small' href='attempt.jsp?taskID=" + item.taskId + "' target= 'blank'>查看详情</a></td>"+
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
                            "<td><a id='attempts' class='btn btn-primary btn-small' href='attempt.jsp?taskID=" + item.taskId + "' target= 'blank'>查看详情</a></td>"+
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
                + '<thead><tr><th>任务名</th>  <th>状态</th> <th>次数</th> <th>创建者</th> <th>操作</th></tr> </thead>'
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
}
