var attemptID;
var status;
var error_log_rtn;
var log_rtn;
var result;
var is_flash;
var old_status;
var timeout;
$(document).ready(function () {
    var sidebar = document.getElementById("sidebar");
    sidebar.style.display = "none";
    $('li[id="schedule"]').addClass("active");
    $('#menu-toggler').on(ace.click_event, function () {
        sidebar.style.display = "block";
        $('#sidebar').toggleClass('display');
        $(this).toggleClass('display');
        return false;
    });

    $(".atip").tooltip();
    options = {
        delay: { show: 500, hide: 100 },
        trigger: 'click'
    };
    $(".optiontip").tooltip(options);
    $(function () {
        $('#id-button-borders').attr('checked', 'checked').on('click', function () {
            $('#default-buttons .btn').toggleClass('no-border');

        });


    });
    attemptID = GetQueryString("id"); //通过表达式获得传递参数
    old_status = GetQueryString("status");
    status = get_task_status();
    is_flash = $('#id-button-borders')[0].checked;
    var flash_btn = document.getElementById("flash_btn");
    var reflash_tip = document.getElementById("reflashtip");
    reflash_tip.style.display = "none";
    if (status != "RUNNING") {
        flash_btn.style.display = "none";
        timeout = 1500;
    } else {
        timeout = 1500;
    }

    do_relash_task();
});

function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}


function fetch_errorLog() {
    var $logContainer = $("#errolog");
    if (status == "RUNNING") {
        var is_end = is_log_end();
        if (is_end == "true") {

            clearInterval(error_log_rtn);
        }
    }

    is_flash = $('#id-button-borders')[0].checked;
    if (is_flash == false) {
        clearInterval(error_log_rtn);
    }

    var loading = document.getElementById("errloading");

    $.ajax({
        url: "attempts.do",
        data: {
            id: attemptID,
            action: 'runlog',
            querytype: 'errorlog'
        },
        timeout: timeout,
        type: 'POST',
        error: function () {
            loading.style.display = "none";
            $logContainer.html("<i class='icon-info red'>没有找到日志数据,请到该job主机的/data/app/taurus-agent/logs/日期/"+attemptID+".err 查看");
        },
        success: function (response) {
            loading.style.display = "none";
            result = response;//.replace(/[\n]/g, "<br>")

            $logContainer.append("<div>" + result + "</div>");
            $logContainer.scrollTop($logContainer.get(0).scrollHeight);

        }

    });


}

function fetch_Log() {
    var $logContainer = $("#stdout");
    var loading = document.getElementById("logloading");
    if (status == "RUNNING") {
        var is_end = is_log_end();
        if (is_end == "true") {
            clearInterval(log_rtn);
        }
    }
    is_flash = $('#id-button-borders')[0].checked;
    if (is_flash == false) {
        clearInterval(log_rtn);
    }
    $.ajax({
        url: "attempts.do",
        data: {
            id: attemptID,
            action: 'runlog',
            querytype: 'log'
        },
        timeout: timeout,
        type: 'POST',
        error: function () {
            loading.style.display = "none";
            $logContainer.html("<i class='icon-info red'>没有找到日志数据,请到该job主机的/data/app/taurus-agent/logs/日期/"+attemptID+".log 查看");
        },
        success: function (response) {
            loading.style.display = "none";
            result = response;

            $logContainer.append("<div>" + result + "</div>");
            $logContainer.scrollTop($logContainer.get(0).scrollHeight);

        },
        beforeSend: function () {//正在加载，显示“正在加载......”

        }

    });

}

function do_relash_task() {

    if (status == "RUNNING") {
        var is_end = is_log_end();
        if (is_end == "false") {
            error_log_rtn = setInterval("fetch_errorLog()", 15000);
            log_rtn = setInterval("fetch_Log()", 15000);
            setTimeout("close_reflash()", 10 * 60 * 1000)
        } else {
            fetch_errorLog();
            fetch_Log();
        }

    } else {
        fetch_errorLog();
        fetch_Log();
    }
}
function close_reflash() {
    clearInterval(log_rtn);
    clearInterval(error_log_rtn);
}

function is_log_end() {
    var ret = "";
    $.ajax({
        url: "attempts.do",
        data: {
            id: attemptID,
            action: 'isend'
        },
        timeout: 1500,
        type: 'POST',
        async: false,
        error: function () {
            ret = "null";
        },
        success: function (response) {
            ret = response
        }

    });
    return ret.trim();
}


function get_task_status() {
    var ret = "";
    $.ajax({
        url: "attempts.do",
        data: {
            id: attemptID,
            action: 'status'
        },
        timeout: 100,
        type: 'POST',
        async: false,
        error: function () {
            ret = "null"
        },
        success: function (response) {
            ret = response;
        }

    });
    return ret.trim();
}


var unloadPageTip = function () {
    if (old_status == "RUNNING") {
        opener.document.location.reload();
    }
    is_flash = $('#id-button-borders')[0].checked;
    if (is_new == "true" && is_flash == true && status == "RUNNING") {
        return "本页面默认实时刷新，不用手动刷新哦，如果你不想页面自动刷新，请点【OFF】按钮";
    }


};

window.onbeforeunload = unloadPageTip;
