var attemptID;
var status;
var error_log_rtn;
var log_rtn;
var result;
var is_flash;
var is_new;
var old_status;
var timeout;
$(document).ready(function () {
    $(".atip").tooltip();
    options = {
        delay: { show: 500, hide: 100 },
        trigger: 'click'
    };
    $(".optiontip").tooltip(options);
    $(function () {

        $('.btn-toggle').click(function () {
            var $this = $(this); //找到当前btn-toggle定义的按钮组

            if ($this.find('.btn-danger').length > 0) {
                $this.find('.btn').toggleClass('btn-default');
                is_flash = false;
            } else {
                is_flash = true;
            }

            /*
             *  这里我们可以修改btn定义不同的切换按钮样式：danger,info,success,primary
             */

            $this.find('.btn').toggleClass('btn-danger').toggleClass('active');

        });

    });
    attemptID = GetQueryString("id"); //通过表达式获得传递参数
    old_status = GetQueryString("status");
    status = get_task_status();
    is_flash = true;
    is_new = is_new_agent();
    var error_panel = document.getElementById("error-panel");
    var log_panel = document.getElementById("spann");
    var flash_btn = document.getElementById("flash_btn");
    var reflash_tip = document.getElementById("reflashtip");
    reflash_tip.style.display = "none";
    if(status !="RUNNING"){
        flash_btn.style.display = "none";
        timeout = 25000;
    }else{
        timeout = 1000;
    }


    if (is_new == "true") {

    } else {
        error_panel.style.display = "none";
        log_panel.style.width = "95%";
        flash_btn.style.display = "none";
        if(status !="RUNNING"){
            reflash_tip.style.display = "none";
        }else {
            reflash_tip.style.display = "block";
        }

        clearInterval(log_rtn);
        clearInterval(error_log_rtn);

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
    if (is_new == "true") {
        var is_end = is_log_end();
        if (is_end == "true") {

            clearInterval(error_log_rtn);
        }
        if(is_flash == false){
            clearInterval(error_log_rtn);
        }
    } else {
        return;
    }

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
            $logContainer.text("无数据");
        },
        success: function (response) {
            result = response.replace("\n", "<br>")

            $logContainer.append("<div class=\"terminal-like\">" + response + "</div>");
            $logContainer.scrollTop($logContainer.get(0).scrollHeight);
            $(".loading").hide();
        },
        beforeSend: function () {//正在加载，显示“正在加载......”
            $(".loading").show();
        }

    });


}

function fetch_Log() {
    var $logContainer = $("#strout")
    if (is_new == "true") {
        var is_end = is_log_end();
        if (is_end == "true") {
            clearInterval(log_rtn);
        }
        if(is_flash == false){
            clearInterval(log_rtn);
        }
    } else {
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
            $logContainer.text("没有找到日志数据");
        },
        success: function (response) {

            result = response.replace("\n", "<br>");

            $logContainer.append("<div class=\"terminal-like\">" + response + "</div>");
            $logContainer.scrollTop($logContainer.get(0).scrollHeight);
            $(".loading").hide();
        },
        beforeSend: function () {//正在加载，显示“正在加载......”
            $(".loading").show();
        }

    });

}

function do_relash_task() {

    if (status == "RUNNING") {
        var is_end = is_log_end();
        if (is_end == "false") {
            error_log_rtn = setInterval("fetch_errorLog()", 2500);
            log_rtn = setInterval("fetch_Log()", 2500);
        } else {
            fetch_errorLog();
            fetch_Log();
        }

    } else {
        fetch_errorLog();
        fetch_Log();
    }
}

function is_log_end() {
    var ret = "";
    $.ajax({
        url: "attempts.do",
        data: {
            id: attemptID,
            action: 'isend'
        },
        timeout: 1000,
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

function is_new_agent() {
    var ret = "";
    $.ajax({
        url: "attempts.do",
        data: {
            id: attemptID,
            action: 'isnew'
        },
        timeout: 1500,
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

    if (is_new == "true" &&is_flash == true && status == "RUNNING") {
            return "本页面默认实时刷新，不用手动刷新哦，如果你不想页面自动刷新，请点【OFF】按钮";
    }


};

window.onbeforeunload = unloadPageTip;
