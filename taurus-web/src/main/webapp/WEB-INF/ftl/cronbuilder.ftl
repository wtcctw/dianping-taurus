<!DOCTYPE html>
<html>
<head>
	
	<title>Taurus</title>
	<#include "segment/html_header.ftl">
	<script src="${rc.contextPath}/resource/js/lib/jquery.easyui.min.js"></script>
	<#include "segment/html_header2.ftl">
	<script src="${rc.contextPath}/js/cron.js" type="text/javascript"></script>
    <style type="text/css">
        .line {
            height: 25px;
            line-height: 25px;
            margin: 3px;
        }

        .imp {
            padding-left: 25px;
        }

        .col {
            width: 95px;
        }
    </style>
</head>
<body>
<#include "segment/header.ftl">
<#include "segment/left.ftl">

<div class="main-content  " style="opacity: 1;">
<div class="breadcrumbs" id="breadcrumbs">
    <script type="text/javascript">
        try {
            ace.settings.check('breadcrumbs', 'fixed')
        } catch (e) {
        }
    </script>
    <ul class="breadcrumb">
        <li class="active">
            <i class="icon-home home-icon"></i>
            <a href="${rc.contextPath}/index">HOME</a>
        </li>
    </ul>
</div>

<div class="page-content">
<div class="row">
<div class="col-sm-12">
<ul class="nav nav-tabs">
    <li class="active"><a data-toggle="tab" data-toggle="tab" href="#mintab"><span class="tabs-title">分钟</span><span
            class="tabs-icon"></span></a></li>
    <li class=""><a href="#hourtab" data-toggle="tab"><span class="tabs-title">小时</span><span
            class="tabs-icon"></span></a></li>
    <li class=""><a href="#daytab" data-toggle="tab"><span class="tabs-title">日</span><span
            class="tabs-icon"></span></a></li>
    <li class=""><a href="#monthtab" data-toggle="tab"><span class="tabs-title">月</span><span
            class="tabs-icon"></span></a></li>
    <li class=""><a href="#weektab" data-toggle="tab"><span class="tabs-title">周</span><span
            class="tabs-icon"></span></a></li>
    <li class="tabs-selected"><a href="#yeartab" data-toggle="tab"><span
            class="tabs-title">年</span><span class="tabs-icon"></span></a></li>
</ul>
<div class="tab-content " style="width: 830px; height: 325px;">
<div class="tab-pane active" id="mintab">
    <div title="" class="panel-body panel-body-noheader panel-body-noborder" style="width: 830px; height: 320px;">

        <div class="line">
            <input type="radio" checked="checked" name="min" onclick="everyTime(this)">
            分钟 允许的通配符[, - * /]
        </div>
        <div class="line">
            <input type="radio" name="min" onclick="cycle(this)">
            周期从
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; " data-options="min:1,max:58" value="1"
                    id="minStart_0"><input type="hidden" value="1"><span class="spinner-arrow"
                                                                         style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            -
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; " data-options="min:2,max:59" value="2"
                    id="minEnd_0"><input type="hidden" value="2"><span class="spinner-arrow" style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            分钟
        </div>
        <div class="line">
            <input type="radio" name="min" onclick="startOn(this)">
            从
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; " data-options="min:0,max:59" value="0"
                    id="minStart_1"><input type="hidden" value="0"><span class="spinner-arrow"
                                                                         style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            分钟开始,每
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; " data-options="min:1,max:59" value="1"
                    id="minEnd_1"><input type="hidden" value="1"><span class="spinner-arrow" style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            分钟执行一次
        </div>
        <div class="line">
            <input type="radio" name="min" id="min_appoint">
            指定
        </div>
        <div class="imp minList">
            <input type="checkbox" value="1">01
            <input type="checkbox" value="2">02
            <input type="checkbox" value="3">03
            <input type="checkbox" value="4">04
            <input type="checkbox" value="5">05
            <input type="checkbox" value="6">06
            <input type="checkbox" value="7">07
            <input type="checkbox" value="8">08
            <input type="checkbox" value="9">09
            <input type="checkbox" value="10">10
        </div>
        <div class="imp minList">
            <input type="checkbox" value="11">11
            <input type="checkbox" value="12">12
            <input type="checkbox" value="13">13
            <input type="checkbox" value="14">14
            <input type="checkbox" value="15">15
            <input type="checkbox" value="16">16
            <input type="checkbox" value="17">17
            <input type="checkbox" value="18">18
            <input type="checkbox" value="19">19
            <input type="checkbox" value="20">20
        </div>
        <div class="imp minList">
            <input type="checkbox" value="21">21
            <input type="checkbox" value="22">22
            <input type="checkbox" value="23">23
            <input type="checkbox" value="24">24
            <input type="checkbox" value="25">25
            <input type="checkbox" value="26">26
            <input type="checkbox" value="27">27
            <input type="checkbox" value="28">28
            <input type="checkbox" value="29">29
            <input type="checkbox" value="30">30
        </div>
        <div class="imp minList">
            <input type="checkbox" value="31">31
            <input type="checkbox" value="32">32
            <input type="checkbox" value="33">33
            <input type="checkbox" value="34">34
            <input type="checkbox" value="35">35
            <input type="checkbox" value="36">36
            <input type="checkbox" value="37">37
            <input type="checkbox" value="38">38
            <input type="checkbox" value="39">39
            <input type="checkbox" value="40">40
        </div>
        <div class="imp minList">
            <input type="checkbox" value="41">41
            <input type="checkbox" value="42">42
            <input type="checkbox" value="43">43
            <input type="checkbox" value="44">44
            <input type="checkbox" value="45">45
            <input type="checkbox" value="46">46
            <input type="checkbox" value="47">47
            <input type="checkbox" value="48">48
            <input type="checkbox" value="49">49
            <input type="checkbox" value="50">50
        </div>
        <div class="imp minList">
            <input type="checkbox" value="51">51
            <input type="checkbox" value="52">52
            <input type="checkbox" value="53">53
            <input type="checkbox" value="54">54
            <input type="checkbox" value="55">55
            <input type="checkbox" value="56">56
            <input type="checkbox" value="57">57
            <input type="checkbox" value="58">58
            <input type="checkbox" value="59">59
            <input type="checkbox" value="0">00
        </div>
    </div>
</div>
<div class="tab-pane " id="hourtab">
    <div title="" class="panel-body panel-body-noheader panel-body-noborder" style="width: 830px; height: 320px;">
        <div class="line">
            <input type="radio" checked="checked" name="hour" onclick="everyTime(this)">
            小时 允许的通配符[, - * /]
        </div>
        <div class="line">
            <input type="radio" name="hour" onclick="cycle(this)">
            周期从
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; line-height: 20px;" data-options="min:0,max:23" value="0"
                    id="hourStart_0"><input type="hidden" value="0"><span class="spinner-arrow"
                                                                          style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            -
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; line-height: 20px;" data-options="min:2,max:23" value="2"
                    id="hourEnd_1"><input type="hidden" value="2"><span class="spinner-arrow"
                                                                        style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            小时
        </div>
        <div class="line">
            <input type="radio" name="hour" onclick="startOn(this)">
            从
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; line-height: 20px;" data-options="min:0,max:23" value="0"
                    id="hourStart_1"><input type="hidden" value="0"><span class="spinner-arrow"
                                                                          style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            小时开始,每
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; line-height: 20px;" data-options="min:1,max:23" value="1"
                    id="hourEnd_1"><input type="hidden" value="1"><span class="spinner-arrow"
                                                                        style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            小时执行一次
        </div>
        <div class="line">
            <input type="radio" name="hour" id="hour_appoint">
            指定
        </div>
        <div class="imp hourList">
            AM:
            <input type="checkbox" value="0">00
            <input type="checkbox" value="1">01
            <input type="checkbox" value="2">02
            <input type="checkbox" value="3">03
            <input type="checkbox" value="4">04
            <input type="checkbox" value="5">05
            <input type="checkbox" value="6">06
            <input type="checkbox" value="7">07
            <input type="checkbox" value="8">08
            <input type="checkbox" value="9">09
            <input type="checkbox" value="10">10
            <input type="checkbox" value="11">11
        </div>
        <div class="imp hourList">
            PM:
            <input type="checkbox" value="12">12
            <input type="checkbox" value="13">13
            <input type="checkbox" value="14">14
            <input type="checkbox" value="15">15
            <input type="checkbox" value="16">16
            <input type="checkbox" value="17">17
            <input type="checkbox" value="18">18
            <input type="checkbox" value="19">19
            <input type="checkbox" value="20">20
            <input type="checkbox" value="21">21
            <input type="checkbox" value="22">22
            <input type="checkbox" value="23">23
        </div>
    </div>
</div>
<div class="tab-pane " id="daytab">
    <div title="" class="panel-body panel-body-noheader panel-body-noborder" style="width: 830px; height: 320px;">
        <div class="line">
            <input type="radio" checked="checked" name="day" onclick="everyTime(this)">
            日 允许的通配符[, - * / L W]
        </div>
        <div class="line">
            <input type="radio" name="day" onclick="unAppoint(this)">
            不指定
        </div>
        <div class="line">
            <input type="radio" name="day" onclick="cycle(this)">
            周期从
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; line-height: 20px;" data-options="min:1,max:31" value="1"
                    id="dayStart_0"><input type="hidden" value="1"><span class="spinner-arrow"
                                                                         style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            -
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; line-height: 20px;" data-options="min:2,max:31" value="2"
                    id="dayEnd_0"><input type="hidden" value="2"><span class="spinner-arrow" style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            日
        </div>
        <div class="line">
            <input type="radio" name="day" onclick="startOn(this)">
            从
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; line-height: 20px;" data-options="min:1,max:31" value="1"
                    id="dayStart_1"><input type="hidden" value="1"><span class="spinner-arrow"
                                                                         style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            日开始,每
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; line-height: 20px;" data-options="min:1,max:31" value="1"
                    id="dayEnd_1"><input type="hidden" value="1"><span class="spinner-arrow" style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            天执行一次
        </div>
        <div class="line">
            <input type="radio" name="day" onclick="workDay(this)">
            每月
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; line-height: 20px;" data-options="min:1,max:31" value="1"
                    id="dayStart_2"><input type="hidden" value="1"><span class="spinner-arrow"
                                                                         style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            号最近的那个工作日
        </div>
        <div class="line">
            <input type="radio" name="day" onclick="lastDay(this)">
            本月最后一天
        </div>
        <div class="line">
            <input type="radio" name="day" id="day_appoint">
            指定
        </div>
        <div class="imp dayList">
            <input type="checkbox" value="1">1
            <input type="checkbox" value="2">2
            <input type="checkbox" value="3">3
            <input type="checkbox" value="4">4
            <input type="checkbox" value="5">5
            <input type="checkbox" value="6">6
            <input type="checkbox" value="7">7
            <input type="checkbox" value="8">8
            <input type="checkbox" value="9">9
            <input type="checkbox" value="10">10
            <input type="checkbox" value="11">11
            <input type="checkbox" value="12">12
            <input type="checkbox" value="13">13
            <input type="checkbox" value="14">14
            <input type="checkbox" value="15">15
            <input type="checkbox" value="16">16
        </div>
        <div class="imp dayList">
            <input type="checkbox" value="17">17
            <input type="checkbox" value="18">18
            <input type="checkbox" value="19">19
            <input type="checkbox" value="20">20
            <input type="checkbox" value="21">21
            <input type="checkbox" value="22">22
            <input type="checkbox" value="23">23
            <input type="checkbox" value="24">24
            <input type="checkbox" value="25">25
            <input type="checkbox" value="26">26
            <input type="checkbox" value="27">27
            <input type="checkbox" value="28">28
            <input type="checkbox" value="29">29
            <input type="checkbox" value="30">30
            <input type="checkbox" value="31">31
        </div>
    </div>
</div>
<div class="tab-pane " id="monthtab">
    <div title="" class="panel-body panel-body-noheader panel-body-noborder" style="width: 830px; height: 320px;">
        <div class="line">
            <input type="radio" checked="checked" name="month" onclick="everyTime(this)">
            月 允许的通配符[, - * /]
        </div>
        <div class="line">
            <input type="radio" name="month" onclick="unAppoint(this)">
            不指定
        </div>
        <div class="line">
            <input type="radio" name="month" onclick="cycle(this)">
            周期从
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; line-height: 20px;" data-options="min:1,max:12" value="1"
                    id="monthStart_0"><input type="hidden" value="1"><span class="spinner-arrow"
                                                                           style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            -
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; line-height: 20px;" data-options="min:2,max:12" value="2"
                    id="monthEnd_0"><input type="hidden" value="2"><span class="spinner-arrow"
                                                                         style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            月
        </div>
        <div class="line">
            <input type="radio" name="month" onclick="startOn(this)">
            从
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; line-height: 20px;" data-options="min:1,max:12" value="1"
                    id="monthStart_1"><input type="hidden" value="1"><span class="spinner-arrow"
                                                                           style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            日开始,每
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; line-height: 20px;" data-options="min:1,max:12" value="1"
                    id="monthEnd_1"><input type="hidden" value="1"><span class="spinner-arrow"
                                                                         style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            月执行一次
        </div>
        <div class="line">
            <input type="radio" name="month" id="month_appoint">
            指定
        </div>
        <div class="imp monthList">
            <input type="checkbox" value="1">1
            <input type="checkbox" value="2">2
            <input type="checkbox" value="3">3
            <input type="checkbox" value="4">4
            <input type="checkbox" value="5">5
            <input type="checkbox" value="6">6
            <input type="checkbox" value="7">7
            <input type="checkbox" value="8">8
            <input type="checkbox" value="9">9
            <input type="checkbox" value="10">10
            <input type="checkbox" value="11">11
            <input type="checkbox" value="12">12
        </div>
    </div>
</div>
<div class="tab-pane " id="weektab">
    <div title="" class="panel-body panel-body-noheader panel-body-noborder" style="width: 830px; height: 320px;">
        <div class="line">
            <span><font style="color:red;">注意，此处周日对应1，周六对应数字7</font></span>
        </div>
        <div class="line">
            <input type="radio" checked="checked" name="week" onclick="everyTime(this)">
            周 允许的通配符[, - * / L #]
        </div>
        <div class="line">
            <input type="radio" name="week" onclick="unAppoint(this)">
            不指定
        </div>
        <div class="line">
            <input type="radio" name="week" onclick="startOn(this)">
            周期 从星期<span class="spinner" style="width: 58px; height: 20px;"><input
                class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                style="width: 36px; height: 20px; line-height: 20px;" data-options="min:1,max:7" id="weekStart_0"
                value="1"><input type="hidden" value="1"><span class="spinner-arrow" style="height: 20px;"><span
                class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                            style="height: 10px;"></span></span></span>
            -
            <span class="spinner" style="width: 58px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 36px; height: 20px; line-height: 20px;" data-options="min:2,max:7" value="2"
                    id="weekEnd_0"><input type="hidden" value="2"><span class="spinner-arrow"
                                                                        style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
        </div>
        <div class="line">
            <input type="radio" name="week" onclick="weekOfDay(this)">
            第<span class="spinner" style="width: 58px; height: 20px;"><input
                class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                style="width: 36px; height: 20px; line-height: 20px;" data-options="min:1,max:4" value="1"
                id="weekStart_1"><input type="hidden" value="1"><span class="spinner-arrow" style="height: 20px;"><span
                class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                            style="height: 10px;"></span></span></span>
            周 的星期<span class="spinner" style="width: 58px; height: 20px;"><input
                class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                style="width: 36px; height: 20px; line-height: 20px;" data-options="min:1,max:7" id="weekEnd_1"
                value="1"><input type="hidden" value="1"><span class="spinner-arrow" style="height: 20px;"><span
                class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                            style="height: 10px;"></span></span></span>
        </div>
        <div class="line">
            <input type="radio" name="week" onclick="lastWeek(this)">
            本月最后一个星期<span class="spinner" style="width: 58px; height: 20px;"><input
                class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                style="width: 36px; height: 20px; line-height: 20px;" data-options="min:1,max:7" id="weekStart_2"
                value="1"><input type="hidden" value="1"><span class="spinner-arrow" style="height: 20px;"><span
                class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                            style="height: 10px;"></span></span></span>
        </div>
        <div class="line">
            <input type="radio" name="week" id="week_appoint">
            指定
        </div>
        <div class="imp weekList">
            <input type="checkbox" value="1">周日
            <input type="checkbox" value="2">周一
            <input type="checkbox" value="3">周二
            <input type="checkbox" value="4">周三
            <input type="checkbox" value="5">周四
            <input type="checkbox" value="6">周五
            <input type="checkbox" value="7">周六
        </div>
    </div>
</div>
<div class="tab-pane " id="yeartab">
    <div title="" class="panel-body panel-body-noheader panel-body-noborder" style="width: 830px; height: 320px;">
        <div class="line">
            <input type="radio" checked="checked" name="year" onclick="unAppoint(this)">
            不指定 允许的通配符[, - * /] 非必填
        </div>
        <div class="line">
            <input type="radio" name="year" onclick="everyTime(this)">
            每年
        </div>
        <div class="line">
            <input type="radio" name="year" onclick="cycle(this)">周期 从
            <span class="spinner" style="width: 88px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 66px; height: 20px; line-height: 20px;" data-options="min:${year!},max:3000"
                    id="yearStart_0" value="${year!}"><input type="hidden" value="${year!}"><span class="spinner-arrow"
                                                                                          style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
            -
            <span class="spinner" style="width: 88px; height: 20px;"><input
                    class="numberspinner numberspinner-f spinner-text spinner-f validatebox-text numberbox-f"
                    style="width: 66px; height: 20px; line-height: 20px;" data-options="min:${year+1},max:3000"
                    id="yearEnd_0" value="${year+1}"><input type="hidden" value="${year+1}"><span class="spinner-arrow"
                                                                                        style="height: 20px;"><span
                    class="spinner-arrow-up" style="height: 10px;"></span><span class="spinner-arrow-down"
                                                                                style="height: 10px;"></span></span></span>
        </div>
    </div>
</div>
</div>
<div class="panel layout-panel layout-panel-south" style="left: 0px; top: 310px; width: 830px;">
<div data-options="region:'south',border:false" style="height: 230px; width: 830px;" title=""
     class="panel-body panel-body-noheader panel-body-noborder layout-body">
<fieldset style="border-radius: 3px; height: 116px;">
    <legend>表达式</legend>
    <table style="height: 100px;">
        <tbody>
        <tr>
            <td>
            </td>
            <td align="center">
                分钟
            </td>
            <td align="center">
                小时
            </td>
            <td align="center">
                日
            </td>
            <td align="center">
                月<br>
            </td>
            <td align="center">
                星期
            </td>
            <td align="center">
                年
            </td>
        </tr>
        <tr>
            <td>
                表达式字段:
            </td>
            <td>
                <input type="text" name="v_min" class="col" value="0" readonly="readonly" id="min_input">
            </td>
            <td>
                <input type="text" name="v_hour" class="col" value="*" readonly="readonly" id="hour_input">
            </td>
            <td>
                <input type="text" name="v_day" class="col" value="*" readonly="readonly" id="day_input">
            </td>
            <td>
                <input type="text" name="v_month" class="col" value="*" readonly="readonly" id="month_input">
            </td>
            <td>
                <input type="text" name="v_week" class="col" value="?" readonly="readonly" id="week_input">
            </td>
            <td>
                <input type="text" name="v_year" class="col" readonly="readonly" id="year_input">
            </td>
        </tr>
        <tr>
            <td>UI结果:</td>
            <td colspan="6"><input type="text" class="input" name="showvalue" style="width: 100%;" value=""
                                   readonly="readonly" value="0 * * * ? " id="showvalue" >
            </td>
        </tr>

        <tr>
            <td>Cron 表达式:</td>
            <td colspan="6"><input type="text" name="cronvalue" style="width: 100%;" value="0 * * * ?" id="cronvalue">
            </td>
            <td>
                <button class="btn btn-info" type="button" id="btnFan" onclick="btnFan()"><i
                        class="ace-icon fa fa-check bigger-110"></i>反解析到UI
                </button>
            </td>
        </tr>
        </tbody>
    </table>
</fieldset>
<div class="feedTool">

    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="${rc.contextPath}/img/qq.png"  width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img border="0" src="${rc.contextPath}/img/x_alt.png"  width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>
    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>
<div style="text-align: center; margin-top: 5px;">
    <script type="text/javascript">

        $('li[id="cron"]').addClass("active");
        $('#menu-toggler').on(ace.click_event, function () {
            $('#sidebar').toggleClass('display');
            $(this).toggleClass('display');
            return false;
        });
        $.parser.parse($("body"));
        $("#min_appoint").prop("checked", "checked");
        var item = $("input[name=v_min]");
        item.eq(0).prop("checked", "checked");

        function btnFan() {
            //获取参数中表达式的值
            var txt = $("#cronvalue").val();

            if (txt) {
                var regs = txt.split(' ');
                $("input[name=v_min]").val(regs[0]);
                $("input[name=v_hour]").val(regs[1]);
                $("input[name=v_day]").val(regs[2]);
                $("input[name=v_month]").val(regs[3]);
                $("input[name=v_week]").val(regs[4]);

                //initObj(regs[0], "min");
                //initObj(regs[1], "hour");
                initMin(regs[0]);
                initHour(regs[1]);
                initDay(regs[2]);
                initMonth(regs[3]);
                initWeek(regs[4]);

                if (regs.length > 5) {
                    $("input[name=v_year]").val(regs[5]);
                    initYear(regs[5]);
                }
            }
        }


        function initObj(strVal, strid) {
            var ary = null;
            var objRadio = $("input[name='" + strid + "'");
            if (strVal == "*") {
                objRadio.eq(0).prop("checked", "checked");
            } else if (strVal.split('-').length > 1) {
                ary = strVal.split('-');
                objRadio.eq(1).prop("checked", "checked");
                $("#" + strid + "Start_0").numberspinner('setValue', ary[0]);
                $("#" + strid + "End_0").numberspinner('setValue', ary[1]);
            } else if (strVal.split('/').length > 1) {
                ary = strVal.split('/');
                objRadio.eq(2).prop("checked", "checked");
                $("#" + strid + "Start_1").numberspinner('setValue', ary[0]);
                $("#" + strid + "End_1").numberspinner('setValue', ary[1]);
            } else {
                objRadio.eq(3).prop("checked", "checked");
                if (strVal != "?") {
                    ary = strVal.split(",");
                    $tmpList = $("." + strid + "List input");
                    for(var j = 0;j< $tmpList.length; ++j){
                        $tmpList.eq(j).prop("checked", false);
                    }
                    for (var i = 0; i < ary.length; i++) {
                        $("." + strid + "List input[value='" + ary[i] + "']").prop("checked", true);
                    }
                }
            }
        }

        function initMin(strVal) {
            var ary = null;
            var objRadio = $("input[name='min'");
            if (strVal == "*") {
                objRadio.eq(0).prop("checked", "checked");
            } else if (strVal.split('-').length > 1) {
                ary = strVal.split('-');
                objRadio.eq(1).prop("checked", "checked");
                $("#minStart_0").numberspinner('setValue', ary[0]);
                $("#minEnd_0").numberspinner('setValue', ary[1]);
            } else if (strVal.split('/').length > 1) {
                ary = strVal.split('/');
                objRadio.eq(2).prop("checked", "checked");
                $("#minStart_1").numberspinner('setValue', ary[0]);
                $("#minEnd_1").numberspinner('setValue', ary[1]);
            } else {
                objRadio.eq(3).prop("checked", "checked");
                if (strVal != "?") {
                    ary = strVal.split(",");
                    $tmpList = $(".minList input");
                    for(var j = 0;j< $tmpList.length; ++j){
                        $tmpList.eq(j).prop("checked", false);
                    }
                    for (var i = 0; i < ary.length; i++) {
                        $(".minList input[value='" + ary[i] + "']").prop("checked", true);
                    }
                }
            }
        }

        function initHour(strVal) {
            var ary = null;
            var objRadio = $("input[name='hour'");
            if (strVal == "*") {
                objRadio.eq(0).prop("checked", "checked");
            } else if (strVal.split('-').length > 1) {
                ary = strVal.split('-');
                objRadio.eq(1).prop("checked", "checked");
                $("#hourStart_0").numberspinner('setValue', ary[0]);
                $("#hourEnd_0").numberspinner('setValue', ary[1]);
            } else if (strVal.split('/').length > 1) {
                ary = strVal.split('/');
                objRadio.eq(2).prop("checked", "checked");
                $("#hourStart_1").numberspinner('setValue', ary[0]);
                $("#hourEnd_1").numberspinner('setValue', ary[1]);
            } else {
                objRadio.eq(3).prop("checked", "checked");
                if (strVal != "?") {
                    ary = strVal.split(",");
                    $tmpList = $(".hourList input");
                    for(var j = 0;j< $tmpList.length; ++j){
                        $tmpList.eq(j).prop("checked", false);
                    }
                    for (var i = 0; i < ary.length; i++) {
                        $(".hourList input[value='" + ary[i] + "']").prop("checked", true);
                    }
                }
            }
        }

        function initDay(strVal) {
            var ary = null;
            var objRadio = $("input[name='day']");
            if (strVal == "*") {
                objRadio.eq(0).prop("checked", "checked");
            } else if (strVal == "?") {
                objRadio.eq(1).prop("checked", "checked");
            } else if (strVal.split('-').length > 1) {
                ary = strVal.split('-');
                objRadio.eq(2).prop("checked", "checked");
                $("#dayStart_0").numberspinner('setValue', ary[0]);
                $("#dayEnd_0").numberspinner('setValue', ary[1]);
            } else if (strVal.split('/').length > 1) {
                ary = strVal.split('/');
                objRadio.eq(3).prop("checked", "checked");
                $("#dayStart_1").numberspinner('setValue', ary[0]);
                $("#dayEnd_1").numberspinner('setValue', ary[1]);
            } else if (strVal.split('W').length > 1) {
                ary = strVal.split('W');
                objRadio.eq(4).prop("checked", "checked");
                $("#dayStart_2").numberspinner('setValue', ary[0]);
            } else if (strVal == "L") {
                objRadio.eq(5).prop("checked", "checked");
            } else {
                objRadio.eq(6).prop("checked", "checked");
                ary = strVal.split(",");
                $(".dayList input").each(function() {
                    this.checked=false;
                });
                for (var i = 0; i < ary.length; i++) {
                    $(".dayList input[value='" + ary[i] + "']").prop("checked", "checked");
                }
            }
        }

        function initMonth(strVal) {
            var ary = null;
            var objRadio = $("input[name='month']");
            if (strVal == "*") {
                objRadio.eq(0).prop("checked", "checked");
            } else if (strVal == "?") {
                objRadio.eq(1).prop("checked", "checked");
            } else if (strVal.split('-').length > 1) {
                ary = strVal.split('-');
                objRadio.eq(2).prop("checked", "checked");
                $("#monthStart_0").numberspinner('setValue', ary[0]);
                $("#monthEnd_0").numberspinner('setValue', ary[1]);
            } else if (strVal.split('/').length > 1) {
                ary = strVal.split('/');
                objRadio.eq(3).prop("checked", "checked");
                $("#monthStart_1").numberspinner('setValue', ary[0]);
                $("#monthEnd_1").numberspinner('setValue', ary[1]);

            } else {
                objRadio.eq(4).prop("checked", "checked");

                ary = strVal.split(",");
                $(".monthList input").each(function() {
                    this.checked=false;
                });
                for (var i = 0; i < ary.length; i++) {
                    $(".monthList input[value='" + ary[i] + "']").prop("checked", "checked");
                }
            }
        }

        function initWeek(strVal) {
            var ary = null;
            var objRadio = $("input[name='week']");
            if (strVal == "*") {
                objRadio.eq(0).prop("checked", "checked");
            } else if (strVal == "?") {
                objRadio.eq(1).prop("checked", "checked");
            } else if (strVal.split('/').length > 1) {
                ary = strVal.split('/');
                objRadio.eq(2).prop("checked", "checked");
                $("#weekStart_0").numberspinner('setValue', ary[0]);
                $("#weekEnd_0").numberspinner('setValue', ary[1]);
            } else if (strVal.split('-').length > 1) {
                ary = strVal.split('-');
                objRadio.eq(3).prop("checked", "checked");
                $("#weekStart_1").numberspinner('setValue', ary[0]);
                $("#weekEnd_1").numberspinner('setValue', ary[1]);
            } else if (strVal.split('L').length > 1) {
                ary = strVal.split('L');
                objRadio.eq(4).prop("checked", "checked");
                $("#weekStart_2").numberspinner('setValue', ary[0]);
            } else {
                objRadio.eq(5).prop("checked", "checked");
                ary = strVal.split(",");
                $(".weekList input").each(function() {
                    this.checked=false;
                });
                for (var i = 0; i < ary.length; i++) {
                    $(".weekList input[value='" + ary[i] + "']").prop("checked", "checked");
                }
            }
        }

        function initYear(strVal) {
            var ary = null;
            var objRadio = $("input[name='year']");
            if (strVal == "*") {
                objRadio.eq(1).prop("checked", "checked");
            } else if (strVal.split('-').length > 1) {
                ary = strVal.split('-');
                objRadio.eq(2).prop("checked", "checked");
                $("#yearStart_0").numberspinner('setValue', ary[0]);
                $("#yearEnd_0").numberspinner('setValue', ary[1]);
            }
        }
    </script>
</div>
<div class="layout-split-proxy-h"></div>
<div class="layout-split-proxy-v"></div>
<div>
</div>
</div>
</div>
</div>
</div>
</div>
</div>

</body>
</html>