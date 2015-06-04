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


    totalTaskStyle = function () {
        $('#totalTaskTable').dataTable({
            "bAutoWidth": true,
            "bPaginate": true,
            "bFilter": true,
            "bInfo": true,
            "bLengthChange": true,
            "aaSorting": [[ 3, "desc" ]]



        });
    };


    failedJobStyle = function () {
        $('#failedJobTable').dataTable({
            "bAutoWidth": true,
            "bPaginate": true,
            "bFilter": true,
            "bInfo": true,
            "bLengthChange": true,
            "aaSorting": [[ 1, "desc" ]]


        });
    };

    totalJobStyle = function () {
        $("#totalJobTable").dataTable({
            "bAutoWidth": true,
            "bPaginate": true,
            "bFilter": true,
            "bInfo": true,
            "bLengthChange": true,
            "aaSorting": [[ 1, "desc" ]]

        });
    };

    jobStateStyle = function () {
        $("#jobStateTable").dataTable({
            "bAutoWidth": true,
            "bPaginate": true,
            "bFilter": true,
            "bInfo": true,
            "bLengthChange": true,
            "aaSorting": [[ 1, "desc" ]]

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

    reflash_data(starttime,endtime);


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

function reflash_data(starttime,endtime){
    var totalBody = "";
    var loading = '<div class="loadIcon"><div></div><div></div><div></div><div></div> </div>';

    $("#total-chart").html(loading);
    $("#failedJob").html(loading);
    $("#totaltasklist").html(loading);
    $("#totalJob").html(loading);
    $("#totaltasks").html(loading);

    $.ajax({
        data: {
            action: "totaltaskload",
            start: starttime,
            end: endtime
        },
        type: "POST",
        url: "../task_center",
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
        url: "../task_center",
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
        url: "../task_center",
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
                        totalKillLists[totalKillNums] = item.taskName;
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
                        totalTimeoutLists[totalTimeoutNums] = item.taskName;
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
                        totalCongestLists[totalCongestNums] = item.taskName;
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
                    var succ =  ((totalSuccNums * 1.0)/(1.0*(totalSuccNums+totalFailedNums+totalKillNums+totalTimeoutNums+totalCongestNums)))*100
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

            if (totalSuccNums != 0 || totalFailedNums != 0 ||  totalKillNums != 0 || totalTimeoutNums != 0 || totalCongestNums != 0) {
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
