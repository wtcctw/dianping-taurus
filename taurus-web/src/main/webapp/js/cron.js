
function btnShow() {
    var show_v;
    var min_v = $("#min_input").val();
    show_v = min_v + " ";

    var hour_v = $("#hour_input").val();
    show_v += hour_v + " ";
    var day_v = $("#day_input").val();
    show_v += day_v + " ";
    var month_v = $("#month_input").val();
    show_v += month_v + " ";
    var week_v = $("#week_input").val();
    show_v += week_v + " ";
    var year_v =$("#year_input").val();
    show_v += year_v;

    $("#showvalue").val(show_v);

}
/**
 * 每周期
 */
function everyTime(dom) {
    var item = $("input[name=v_" + dom.name + "]");
    item.val("*");
    item.change();
    btnShow();
}

/**
 * 不指定
 */
function unAppoint(dom) {
    var name = dom.name;
    var val = "?";
    if (name == "year")
        val = "";
    var item = $("input[name=v_" + name + "]");
    item.val(val);
    item.change();
    btnShow();
}

function appoint(dom) {

}

/**
 * 周期
 */
function cycle(dom) {
    var name = dom.name;
    var ns = $(dom).parent().find(".numberspinner");
    var start = ns.eq(0).numberspinner("getValue");
    var end = ns.eq(1).numberspinner("getValue");
    var item = $("input[name=v_" + name + "]");
    item.val(start + "-" + end);
    item.change();
    btnShow();
}

/**
 * 从开始
 */
function startOn(dom) {
    var name = dom.name;
    var ns = $(dom).parent().find(".numberspinner");
    var start = ns.eq(0).numberspinner("getValue");
    var end = ns.eq(1).numberspinner("getValue");
    var item = $("input[name=v_" + name + "]");
    item.val(start + "/" + end);
    item.change();
    btnShow();
}

function lastDay(dom){
    var item = $("input[name=v_" + dom.name + "]");
    item.val("L");
    item.change();
    btnShow();
}

function weekOfDay(dom){
    var name = dom.name;
    var ns = $(dom).parent().find(".numberspinner");
    var start = ns.eq(0).numberspinner("getValue");
    var end = ns.eq(1).numberspinner("getValue");
    var item = $("input[name=v_" + name + "]");
    item.val(start + "#" + end);
    item.change();
    btnShow();
}

function lastWeek(dom){
    var item = $("input[name=v_" + dom.name + "]");
    var ns = $(dom).parent().find(".numberspinner");
    var start = ns.eq(0).numberspinner("getValue");
    item.val(start+"L");
    item.change();
    btnShow();
}

function workDay(dom) {
    var name = dom.name;
    var ns = $(dom).parent().find(".numberspinner");
    var start = ns.eq(0).numberspinner("getValue");
    var item = $("input[name=v_" + name + "]");
    item.val(start + "W");
    item.change();
    btnShow();
}

$(function() {

    $(".numberspinner").numberspinner({
        onChange:function(){
            $(this).closest("div.line").children().eq(0).click();
        }
    });

    var vals = $("input[name^='v_']");
    var cron = $("#cron");
    vals.change(function() {
        var item = [];
        vals.each(function() {
            item.push(this.value);
        });
        cron.val(item.join(" "));
    });

    var secondList = $(".secondList").children();
    $("#sencond_appoint").click(function(){
        if(this.checked){
            secondList.eq(0).change();
        }
    });

    secondList.change(function() {
        var sencond_appoint = $("#sencond_appoint").prop("checked");
        if (sencond_appoint) {
            var vals = [];
            secondList.each(function() {
                if (this.checked) {
                    vals.push(this.value);
                }
            });
            var val = "?";
            if (vals.length > 0 && vals.length < 59) {
                val = vals.join(",");
            }else if(vals.length == 59){
                val = "*";
            }
            var item = $("input[name=v_second]");
            item.val(val);
            item.change();
        }
    });

    var minList = $(".minList").children();
    $("#min_appoint").click(function(){
        if(this.checked){
            minList.eq(0).change();
        }
    });

    minList.change(function() {
        var min_appoint = $("#min_appoint").prop("checked");
        if (min_appoint) {
            var vals = [];
            minList.each(function() {
                if (this.checked) {
                    vals.push(this.value);
                }
            });
            var val = "?";
            if (vals.length > 0 && vals.length < 59) {
                val = vals.join(",");
            }else if(vals.length == 59){
                val = "*";
            }
            var item = $("input[name=v_min]");
            item.val(val);
            item.change();
            btnShow();
        }
    });




    var hourList = $(".hourList").children();
    $("#hour_appoint").click(function(){
        if(this.checked){
            hourList.eq(0).change();
        }
    });

    hourList.change(function() {
        var hour_appoint = $("#hour_appoint").prop("checked");
        if (hour_appoint) {
            var vals = [];
            hourList.each(function() {
                if (this.checked) {
                    vals.push(this.value);
                }
            });
            var val = "?";
            if (vals.length > 0 && vals.length < 24) {
                val = vals.join(",");
            }else if(vals.length == 24){
                val = "*";
            }
            var item = $("input[name=v_hour]");
            item.val(val);
            item.change();
            btnShow();
        }
    });

    var dayList = $(".dayList").children();
    $("#day_appoint").click(function(){
        if(this.checked){
            dayList.eq(0).change();
        }
    });

    dayList.change(function() {
        var day_appoint = $("#day_appoint").prop("checked");
        if (day_appoint) {
            var vals = [];
            dayList.each(function() {
                if (this.checked) {
                    vals.push(this.value);
                }
            });
            var val = "?";
            if (vals.length > 0 && vals.length < 31) {
                val = vals.join(",");
            }else if(vals.length == 31){
                val = "*";
            }
            var item = $("input[name=v_day]");
            item.val(val);
            item.change();
            btnShow();
        }
    });

    var monthList = $(".monthList").children();
    $("#month_appoint").click(function(){
        if(this.checked){
            monthList.eq(0).change();
        }
    });

    monthList.change(function() {
        var month_appoint = $("#month_appoint").prop("checked");
        if (month_appoint) {
            var vals = [];
            monthList.each(function() {
                if (this.checked) {
                    vals.push(this.value);
                }
            });
            var val = "?";
            if (vals.length > 0 && vals.length < 12) {
                val = vals.join(",");
            }else if(vals.length == 12){
                val = "*";
            }
            var item = $("input[name=v_month]");
            item.val(val);
            item.change();
            btnShow();
        }
    });

    var weekList = $(".weekList").children();
    $("#week_appoint").click(function(){
        if(this.checked){
            weekList.eq(0).change();
        }
    });

    weekList.change(function() {
        var week_appoint = $("#week_appoint").prop("checked");
        if (week_appoint) {
            var vals = [];
            weekList.each(function() {
                if (this.checked) {
                    vals.push(this.value);
                }
            });
            var val = "?";
            if (vals.length > 0 && vals.length < 7) {
                val = vals.join(",");
            }else if(vals.length == 7){
                val = "*";
            }
            var item = $("input[name=v_week]");
            item.val(val);
            item.change();
            btnShow();
        }
    });
});