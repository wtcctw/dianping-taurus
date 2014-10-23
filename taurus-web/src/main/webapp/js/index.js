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
            "bPaginate": false,
            "bFilter": false,
            "bInfo": false,
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
            "bPaginate": false,
            "bFilter": false,
            "bInfo": false,
            "bLengthChange": true,
            "aoColumns": [
                null,
                { "sType": "html-percent", "aTargets": [2] }
            ]


        });
    };

})
$(document).ready(function () {




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
                    onLineBody = "<i class='icon-lightbulb green'></i>" +ips[0].replace(/[,]/g, "<br><i class='icon-lightbulb green'></i>");
                    ;
                    onlineNums = onlineLists.length;
                }
                if (ips[1].trim() == "NULL") {
                    exceptionNums = 0;
                    exceptionBody = " <i class='icon-info-sign icon-large green '>当前状态很好，没有异常JOB机器</i>";
                } else {
                    exceptionLists = ips[1].split(",");
                    exceptionNums = exceptionLists.length;
                    exceptionBody = "<i class='icon-lightbulb red'></i>" +ips[1].replace(/[,]/g, "<br><i class='icon-lightbulb red'></i>")
                }
                $("#onlineNums").html(onlineNums.toString());
                $("#exceptionNums").html(exceptionNums.toString());
                var htmlContent ="";
                if(exceptionNums == 0){
                    htmlContent = "<i class='icon-info-sign icon-large green '>当前状态很好，没有异常JOB机器</i> " ;
                }else{
                    for (var i = 0; i < exceptionNums; i++) {
                        body += "<tr>" +
                            "<td>" + exceptionLists[i] + "</td>" +
                            "<td><a id='down' title='查看job机详情' class='btn  btn-primary btn-minier' href='hosts.jsp?hostName=" + exceptionLists[i] + "'>详情</a></td>"
                        "</tr>"
                    }
                    htmlContent= ' <table  class="table table-striped ">'
                        + '        <tbody>'
                        + body
                        + '        </tbody>'
                        + '    </table>'
                        + '    <div class="controller">'
                        + '    </div>';


                }

                $("#exceptionJob").html(htmlContent);
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


        var starttime = GetDateStr(-1);
        var endtime = GetDateStr(1);

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
                        "<td>" + item.totaltask + "</td>"
                    "</tr>"
                });
                var topLists = ' <table  class="table table-striped table-bordered table-hover ">'
                    + '<thead><tr><th>IP</th>  <th>执行任务数</th> </tr> </thead>'
                    + '        <tbody>'
                    + totalBody
                    + '        </tbody>'
                    + '    </table>'
                    + '    <div class="controller">'
                    + '    </div>';
                $("#totalJob").html(topLists);
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
                $.each(jsonarray, function (i, item) {
                    failBody += "<tr>" +
                        "<td>" + item.execHost + "</td>" +
                        "<td>" + item.totaltask + "</td>"
                    "</tr>"
                });
              var  topLists = ' <table  class="table table-striped table-bordered table-hover ">'
                    + '<thead><tr><th>IP</th>  <th>执行任务数</th> </tr> </thead>'
                    + '        <tbody>'
                    + failBody
                    + '        </tbody>'
                    + '    </table>'
                    + '    <div class="controller">'
                    + '    </div>';
                $("#failedJob").append(topLists);
            }


        });

    reflash(null);


  //  } else {
        var succNums = 0;
        var failedNums = 0;
        var succTaskNums = 0;
        var failedTaskNums = 0;
        var succLists = new Array();;
        var failedLists = new Array();;
        var succBody = "";
        var failedBody = "";
        var starttime = GetDateStr(-1);
        var endtime = GetDateStr(1);
        $.ajax({
            data: {
                action: "usertask",
                username:username,
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
                var userTaskListBody="";
                var jsonarray = $.parseJSON(response);
                $.each(jsonarray, function (i, item) {

                    if (item.status == "success" && item.nums != 0) {
                        succNums += item.nums;

                        succLists[succTaskNums] = item.taskName;
                        succTaskNums++;
                        succBody += "<i class='icon-tasks'></i>"+item.taskName  + ": " +item.nums + "<br>"
                        userTaskListBody += "<tr>" +
                            "<td>" + item.taskName + "</td>" +
                            "<td>成功</td>" +
                            "<td>"+item.nums +"</td>"+
                            "</tr>";

                    } else {
                        if (item.nums != 0) {
                            failedNums += item.nums;
                            failedLists[failedNums] = item.taskName;
                            failedTaskNums++;
                            failedBody += "<i class='icon-tasks'></i>"+item.taskName  + ": " +item.nums + "<br>"
                            userTaskListBody += "<tr>" +
                                "<td>" + item.taskName + "</td>" +
                                "<td>失败</td>" +
                                "<td>"+item.nums +"</td>"+
                                "</tr>";
                        }

                    }
                });

                if (succNums != 0 || failedNums != 0) {
                    $("#succtask").html(succNums.toString());
                    $("#failtask").html(failedNums.toString());
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

                if (succNums == 0 && failedNums == 0) {
                    $("#user-widget-main").html("<i class='icon-info-sign icon-large red'>今天没有任务调度～</i>");
                    $("#user-widget-main").addClass("align-center");
                } else
                {

                    var placeholder = $('#mytasks').css({'width': '90%', 'height': '200px'});
                    var data = [
                        { label: "正常", data: succNums, color: "#68BC31"},
                        { label: "失败", data: failedNums, color: "#AF4E96"}
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
        var groupSuccTaskNums = 0;
        var groupFailedTaskNums = 0;
        var groupSuccLists = new Array();;
        var groupFailedLists = new Array();;
        var groupSuccBody = "";
        var groupFailedBody = "";
        var starttime = GetDateStr(-1);
        var endtime = GetDateStr(1);
        $.ajax({
            async: false,
            data: {
                action: "grouptask",
                username:username,
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
                    if(username == item.creator){
                        usericon = "&nbsp;&nbsp;<i class='icon-user green'></i>";
                    }else{
                        usericon = "&nbsp;&nbsp;<i class='icon-user'></i>";
                    }
                    if (item.status == "success" && item.nums != 0) {
                        groupSuccNums += item.nums;

                        groupSuccLists[groupSuccTaskNums] = item.taskName;
                        groupSuccTaskNums++;

                        groupSuccBody += "<i class='icon-tasks'></i>"+item.taskName  + ": " +item.nums + usericon + item.creator + "<br>"
                        groupTaskListBody += "<tr>" +
                            "<td>" + item.taskName + "</td>" +
                            "<td>成功</td>" +
                            "<td>"+item.nums +"</td>"+
                            "<td>"+item.creator +"</td>"+
                            "</tr>";
                    } else {
                        if (item.nums != 0) {
                            groupFailedNums += item.nums;
                            groupFailedLists[failedNums] = item.taskName;
                            groupFailedTaskNums++;
                            groupFailedBody += "<i class='icon-tasks'></i>"+ item.taskName  + ": " +item.nums +usericon + item.creator +"<br>"
                            groupTaskListBody += "<tr>" +
                                "<td>" + item.taskName + "</td>" +
                                "<td>失败</td>" +
                                "<td>"+item.nums +"</td>"+
                                "<td>"+item.creator +"</td>"+
                                "</tr>";
                        }

                    }

                });
                if (groupSuccNums != 0 || groupFailedNums != 0) {
                    $("#groupsucctask").html(groupSuccNums.toString());
                    $("#groupfailtask").html(groupFailedNums.toString());
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
                if (groupSuccNums != 0 || groupFailedNums != 0) {
                    var placeholder = $('#grouptasks').css({'width': '90%', 'min-height': '200px'});
                    var data = [
                        { label: "正常", data: groupSuccNums, color: "#68BC31"},
                        { label: "失败", data: groupFailedNums, color: "#AF4E96"}
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


   // }



    function reflash(queryType) {
    var cpuLoadBody = "";
    var memLoadBody = "";
    $.ajax({
        async: true,
        data: {
            action: "hostload",
            queryType:queryType

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

function GetDateStr(AddDayCount) {
    var dd = new Date();
    dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth() + 1;//获取当前月份的日期
    var d = dd.getDate();
    return y + "-" + m + "-" + d;
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

