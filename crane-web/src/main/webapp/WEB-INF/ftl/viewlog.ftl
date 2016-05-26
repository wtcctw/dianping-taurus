<!DOCTYPE html >
<html >
<head>
	<title>Taurus</title>
	<#include "segment/html_header.ftl">
	<script type="text/javascript" src="${rc.contextPath}/static/js/viewlog.js"></script>
	<#include "segment/html_header2.ftl">
	<link rel="stylesheet" href="${rc.contextPath}/css/loading.css">
</head>
<body>
<#include "segment/header.ftl">
<#include "segment/left.ftl">

	<div class="page-content">
        <div class="row">
            <div class="col-sm-6">
            <div id="error-panel" >
                <div class="spanm col-sm-12">
                    <ul class="error-tag" id="error-tag">
                        <li><a>错误信息<span class="label label-important">STDERR</span></a></li>
                    </ul>
                    <div data-spy="scroll" data-offset="0"  style="cursor: text; margin-top: 5px; background-color: #3A1042; color: #e6e6e6; font-size: 12px;  height: 650px; overflow-x: auto;"   id="errolog">

                            <div class="loadIcon" id = "errloading">
                                <div></div>
                                <div></div>
                                <div></div>
                                <div></div>
                            </div>

                    </div>
                </div>
            </div>
            </div>
            <div class="col-sm-6">

            <div id="log-panel" >
                <div class="spann col-sm-12" id="spann">
                    <div class="col-sm-4">
                    <ul class="run-tag ">
                        <li><a>日志信息<span class="label label-info">STDOUT</span></a>
                        </li>
                    </ul>
                    </div>
                    <div class="flash_btn col-sm-6" id="force_btn">
                        <div class="col-sm-8">
                            <label class="label ">强制查看</label>
                            <input id="force-button-borders" type="checkbox" class="ace ace-switch ace-switch-5">
                            <span class="lbl"></span>
                        </div>
                        <div class="col-sm-4">
                            <a class="atip tooltip-info" data-toggle="tooltip" data-placement="left"
                               data-original-title="你的日志显示异常，是由于job主机负载过高导致，如果强制显示日志，可能对该主机的job造成影响">[提示] </a>
                        </div>
                    </div>
                    <div class="flash_btn col-sm-6" id="flash_btn">

                       <div class="col-sm-8">
                        <label class="label " >实时刷新:</label>
                            <input id="id-button-borders" checked="checked" type="checkbox" class="ace ace-switch ace-switch-5">
                            <span class="lbl middle"></span>
                        </div>
                        <div class="col-sm-4">
                           <a class="atip tooltip-info" data-toggle="tooltip" data-placement="left"
                           data-original-title="当你开启实时刷新后页面会自动刷新，不需要自己动手刷新页面喽～">[提示] </a>
                        </div>
                     </div>

                    <div class="reflashtip col-sm-2" id="reflashtip">

                        <a class="atip tooltip-info" data-toggle="tooltip" data-placement="left"
                           data-original-title="您的任务运行在老版本的agent上，此版本不支持实时查看日志，请等待任务完成后查看日志">[注意] </a>
                    </div>

                    <div data-spy="scroll" class="col-sm-12" data-offset="0" style="cursor: text; margin-top: 5px; background-color: #3A1042; color: #e6e6e6; font-size: 12px;  height: 650px; overflow-x: auto;" id="stdout">
                        <div class="loadIcon" id = "logloading">

                            <div></div>
                            <div></div>
                            <div></div>
                            <div></div>

                        </div>


                    </div>
                </div>
            </div>
        </div>
        </div>
    </div>
<div class="feedTool hide">

    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="${rc.contextPath}/img/qq.png"  width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img border="0" src="${rc.contextPath}/img/x_alt.png"  width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>
    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>
</body>
</html>