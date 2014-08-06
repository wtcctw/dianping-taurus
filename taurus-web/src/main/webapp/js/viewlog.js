var attemptID;
var status;
$(document).ready(function () {
    attemptID = GetQueryString("id"); //通过表达式获得传递参数
    status = GetQueryString("status");
    if(status == "RUNNING"){
        setInterval("fetch_errorLog()", 1500);
        setInterval("fetch_Log()", 1500);
    }else
    {
        fetch_errorLog();
        fetch_Log();
    }

});
function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}


function fetch_errorLog() {
    var $logContainer = $("#errolog");

    $.ajax({
        url : "attempts.do",
        data : {
            id : attemptID,
            action : 'runerrorlog'
        },
        type : 'POST',
        error: function(){
            $logContainer.text("无数据");
        },
        success: function (response) {
            if(response == "")
            {
                $logContainer.text("没有找到日志数据");

            }else{
                $logContainer.html(response);
            }
        }

    });
}

function fetch_Log() {
    var $logContainer = $("#strout");

    $.ajax({
        url : "attempts.do",
        data : {
            id : attemptID,
            action : 'runlog'
        },
        type : 'POST',
        error: function(){
            $logContainer.text("没有找到日志数据");
        },
        success: function (response) {
            if(response == "")
            {
                $logContainer.text("没有找到日志数据");

            }else{
                $logContainer.html(response);
            }

        }

    });
}





