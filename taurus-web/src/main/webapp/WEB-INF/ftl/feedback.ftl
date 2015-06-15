<!DOCTYPE html >
<html >
<head>
	
	<title>Taurus</title>
	<#include "segment/html_header.ftl">
	<link rel="stylesheet" href="${rc.contextPath}/css/jquery-ui.min.css"/>
	<script type="text/javascript" src="${rc.contextPath}/js/jquery-ui.min.js"></script>
	<#include "segment/html_header2.ftl">
	<link href="${rc.contextPath}/css/bwizard.min.css" rel="stylesheet"/>
	<style>
        label.error {
            margin-left: 10px;
            color: red;
        }

        label.success {
            margin-left: 10px;
            color: green;
        }

        .tips {
            background: #ebf4f8;
            color: #888;
            border-top: 1px #f6f1dc solid;
            padding: 11px 26px 11px 5px;
        }

        .tip-title {
            padding-top: 20px;
            color: #888;
            font-weight: normal;
            font-size: 13px;
        }

        #contentnum {
            color: red;
        }
    </style>
</head>
<body>
<#include "segment/header.ftl">
<#include "segment/left.ftl">

<div class="main-content ">
    <div class="breadcrumbs" id="breadcrumbs">
        <script type="text/javascript">
            try {
                ace.settings.check('breadcrumbs', 'fixed')
            } catch (e) {
            }
        </script>
        <ul class="breadcrumb">
            <li>
                <i class="icon-home home-icon"></i>
                <a href="${rc.contextPath}/index">HOME</a>
            </li>
            <li class="active">
                <a href="${rc.contextPath}/feedback">我要反馈</a>
            </li>
        </ul>
    </div>
    <div class="page-content">
        <div class="row">
            <div class="col-sm-12">
                <div class="content-wrap">
                    <div class="col-sm-2"></div>
                    <div class="col-sm-7">

                        <div class="content-body">

                            <div class="tips col-sm-12">
                                <i class="icon-lightbulb bigger-210 blue"></i>
                                感谢你使用Taurus !请告诉我们你的意见和建议，我会及时响应你的反馈，并不断完善Taurus。<br>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                联系方式：

                                <div class="col-sm-12">
                                    <div class="col-sm-1"></div>
                                    <div class="col-sm-4"><i class="icon-user-md">&nbsp;&nbsp; 李明 </i></div>
                                </div>
                                <div class="col-sm-12">
                                    <div class="col-sm-1"></div>
                                    <div class="col-sm-4"><i class="icon-phone">&nbsp;&nbsp; 13661871541</i></div>
                                </div>
                                <div class="col-sm-12">
                                    <div class="col-sm-1"></div>
                                    <div class="col-sm-4"><a target="_blank" style="color: orangered;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="${rc.contextPath}/img/qq.png"  width="18" height="18"  alt="点我QQ在线反馈" title="点我QQ在线反馈"/>&nbsp;&nbsp;点我QQ在线反馈</a></div>
                                </div>
                                <div class="col-sm-12">
                                    <div class="col-sm-1"></div>
                                    <div class="col-sm-4"><i class="icon-envelope">&nbsp;&nbsp;&nbsp;kirin.li@dianping.com</i>
                                    </div>
                                </div>
                                <div class="col-sm-12">
                                    <div class="col-sm-1"></div>
                                    <div class="col-sm-4"><i class="icon-group"><strong>
                                        &nbsp;&nbsp;155326270</strong></i></div>
                                </div>

                            </div>

                            <div id="alertContainer" class="container"></div>
                            <h5 class="tip-title">请详细描述你遇到的问题、意见及建议:</h5>

                            <textarea id="feedback-content" class="feedback-content"
                                      style="width:100%; height:240px; resize: none; border-radius: 4px;"></textarea>
                            <br>

                            <div>
                                <div class="limit"
                                     style="float: left; margin-right: 20px; line-height: 34px; color: black;"></div>

                                <div class="btn-wrap" style="float: right">
                                    <a id="submit-btn" class="btn btn-sm btn-primary" data-loading-text="正在反馈...">反馈
                                        <i class="icon-ok bigger-110"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-3"></div>
                </div>
            </div>
        </div>

    </div>
</div>
<div class="feedTool">

    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="${rc.contextPath}/img/qq.png"  width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img border="0" src="${rc.contextPath}/img/x_alt.png"  width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>
    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>
<script type="text/javascript">

    $('li[id="feedback"]').addClass("active");
    $('#menu-toggler').on(ace.click_event, function () {
        $('#sidebar').toggleClass('display');
        $(this).toggleClass('display');
        return false;
    });
    var user = "${currentUser!}";

</script>
<script src="${rc.contextPath}/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="${rc.contextPath}/static/js/feedback.js" type="text/javascript"></script>

</body>
</html>