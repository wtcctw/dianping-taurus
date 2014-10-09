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
            bAutoWidth: true,
            "bPaginate": false,
            "bFilter": false,
            "bLengthChange": true,
            "bInfo": false,
            "aoColumns": [
                null,
                { "sType": "html-percent", "aTargets": [2] }
            ]

        });
    };

    memTableStyle = function () {
        $('#memtable').dataTable({
            bAutoWidth: true,
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
$(function () {

    $.ajax({

        type: "get",
        url: "jsp/common-header.jsp",
        error: function () {
        },
        success: function (response, textStatus) {
            $("#common-header").html(response);
            $('li[id="index"]').addClass("active");
        }


    });


    var onlineNums = 0;
    var exceptionNums = 0;
    var onlineLists = null;
    var exceptionLists = null;
    $.ajax({
        async: false,
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

            if (ips[0].trim == "NULL") {
                onlineNums = 0;
                onLineBody = "";
            } else {
                onlineLists = ips[0].split(",");
                onLineBody = ips[0].replace(/[,]/g, "\n");
                ;
                onlineNums = onlineLists.length;
            }
            if (ips[1].trim == "NULL") {
                exceptionNums = 0;
                exceptionBody = "";
            } else {
                exceptionLists = ips[1].split(",");
                exceptionNums = exceptionLists.length;
                exceptionBody = ips[1].replace(/[,]/g, "\n")
            }
            $("#onlineNums").html(onlineNums.toString());
            $("#exceptionNums").html(exceptionNums.toString());

        }


    });
    for (var i = 0; i < exceptionNums; i++) {
        body += "<tr>" +
            "<td>" + exceptionLists[i] + "</td>" +
            "<td><a id='down' title='查看job机详情' class='btn  btn-primary btn-minier' href='hosts.jsp?hostName=" + exceptionLists[i] + "'>详情</a></td>"
        "</tr>"
    }
    var htmlContent = ' <table  class="table table-striped ">'
        + '        <tbody>'
        + body
        + '        </tbody>'
        + '    </table>'
        + '    <div class="controller">'
        + '    </div>';

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
    var $tooltip = $("<div class='tooltip top in'><div class='tooltip-inner'></div></div>").hide().appendTo('body');
    var previousPoint = null;

    placeholder.on('plothover', function (event, pos, item) {
        if (item) {
            if (previousPoint != item.seriesIndex) {
                previousPoint = item.seriesIndex;

                if (item.series['label'] == "正常") {
                    tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%\n' + onLineBody;
                } else if (item.series['label'] == "失联") {
                    tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%\n' + exceptionBody;
                }
                else {
                    tip = item.series['label'] + ':' + item.series['percent'].toFixed(2) + '%'

                }

                $tooltip.show().children(0).text(tip);
            }
            $tooltip.css({top: pos.pageY + 10, left: pos.pageX + 10});
        } else {
            $tooltip.hide();
            previousPoint = null;
        }

    });

    var starttime = GetDateStr(-1);
    var endtime = GetDateStr(1);

    var totalBody = "";

    $.ajax({
        async: false,
        data: {
            action: "totaltaskload",
            start: starttime,
            end: endtime
        },
        type: "POST",
        url: "/monitor",
        error: function () {
        },
        success: function (response, textStatus) {
            var jsonarray = $.parseJSON(response);
            $.each(jsonarray, function (i, item) {
                totalBody += "<tr>" +
                    "<td>" + item.execHost + "</td>" +
                    "<td>" + item.totaltask + "</td>"
                "</tr>"
            });

        }


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
    var failBody = "";

    $.ajax({
        async: false,
        data: {
            action: "failedtaskload",
            start: starttime,
            end: endtime
        },
        type: "POST",
        url: "/monitor",
        error: function () {
        },
        success: function (response, textStatus) {
            var jsonarray = $.parseJSON(response);
            $.each(jsonarray, function (i, item) {
                failBody += "<tr>" +
                    "<td>" + item.execHost + "</td>" +
                    "<td>" + item.totaltask + "</td>"
                "</tr>"
            });

        }


    });
    topLists = ' <table  class="table table-striped table-bordered table-hover ">'
        + '<thead><tr><th>IP</th>  <th>执行任务数</th> </tr> </thead>'
        + '        <tbody>'
        + failBody
        + '        </tbody>'
        + '    </table>'
        + '    <div class="controller">'
        + '    </div>';
    $("#failedJob").append(topLists);

})


var cpuLoadBody = "";
var memLoadBody = "";
$.ajax({
    async: true,
    data: {
        action: "hostload"
    },
    type: "POST",
    url: "/monitor",
    error: function () {
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
                "<td>" +item.memLoad+ "</td>"
            "</tr>";
        });
        var topCpuLoadLists = ' <table  class="table table-striped table-bordered table-hover " id="cputable">'
            + '<thead><tr><th>主机名</th>  <th>CPU负载(load average)</th> </tr> </thead>'
            + '        <tbody>'
            + cpuLoadBody
            + '        </tbody>'
            + '    </table>'
            + '    <div class="controller">'
            + '    </div>';
        $("#cpuload").html(topCpuLoadLists);
        $("#cpuload").removeClass("align-center");
        cpuTableStyle();
        var topMemLoadLists = ' <table  class="table table-striped table-bordered table-hover " id="memtable">'
            + '<thead><tr><th>主机名</th>  <th>内存剩余(free)</th> </tr> </thead>'
            + '        <tbody>'
            + memLoadBody
            + '        </tbody>'
            + '    </table>'
            + '    <div class="controller">'
            + '    </div>';
        $("#memload").html(topMemLoadLists);
        $("#memload").removeClass("align-center");

        memTableStyle();
    }


});


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

