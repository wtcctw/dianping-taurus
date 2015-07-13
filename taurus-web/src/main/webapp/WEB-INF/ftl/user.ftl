<!DOCTYPE html >
<html >
<head>
	
	<title>Taurus</title>
	<#include "segment/html_header.ftl">
	<link rel="stylesheet" href="${rc.contextPath}/css/jquery-ui.min.css"/>
	<script type="text/javascript" src="${rc.contextPath}/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/static/js/jquery.autocomplete.js"></script>
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
        .autocomplete-suggestions { border: 1px solid #FFF; background: #FFF; overflow: auto; }
        .autocomplete-suggestion { padding: 2px 5px; white-space: nowrap; overflow: hidden; }
        .autocomplete-selected { background: rgba(113,182,243,0.75); }
        .autocomplete-suggestions strong { font-weight: normal; color: #DCA43B; }
        .autocomplete-group { padding: 2px 5px; }
        .autocomplete-group strong { display: block; border-bottom: 1px solid #111; }
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
                <a href="${rc.contextPath}/user">用户设置</a>
            </li>
        </ul>
    </div>


    <#-- 页面主内容 start -->
    <div class="page-content">

<#-- 提示完善用户信息 start -->
<#if user?exists>
<#if user.group?exists == false || user.mail?exists == false || user.tel?exists == false || user.qq?exists == false || user.group == "" || user.mail == "" || user.tel == "" || user.qq == "" >
        <div id="alertContainer" class="container col-sm-12">
            <div id="alertContainer" class="alert alert-danger">
                <button type="button" class="close" data-dismiss="alert">×</button>
                请完善你的信息！
            </div>
        </div>
<#else>
        <div id="alertContainer" class="container col-sm-12"></div>
</#if>
</#if>
<#-- 提示完善用户信息 end -->


        <div class="container" style="margin-top: 10px">
            <div class="row">

            	<#-- 设置你的个人信息 start -->
            	<#if user?exists>
                <div class="col-sm-5 padding-14">
                    <form class='form-horizontal' id='user-form'>
                        <fieldset>
                            <div class="widget-box">
                                <div class="widget-header header-color-green">
                                    <h5 class="widget-title">
                                        <i class="icon-cogs"></i>
                                        <span style="color: white">设置你的个人信息</span>
                                    </h5>

                                    <div class="widget-toolbar">
                                        <a href="#" data-action="collapse">
                                            <i class="icon-chevron-up"></i>
                                        </a>
                                    </div>
                                </div>

                                <div class="widget-body">
                                    <div class="widget-main " id="userwidget">
                                        <div style="padding-left: 80px">
                                            <div style='display:none'>
                                                <input type="text" class="input-large field" id="id" name="id"
                                                       value="${user.id?string}">
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label" for="userName">用户名</label>

                                                <div class="controls">
                                                    <input type="text" readonly class="input-large field" id="userName"
                                                           name="userName"
                                                           value="${currentUser!}">
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label" for="groupName">组名(逗号分隔)</label>

                                                <div class="controls">
                                                    <input type="text" class="input-large field" id="groupName"
                                                           name="groupName"
                                                           value="${user.group!}"
                                                           placeholder="Max 3 and split with ,">
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label" for="email">邮件地址</label>

                                                <div class="controls">
                                                    <input type="text" class="input-large field" id="email" name="email"
                                                           value="${user.mail!}">
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label" for="tel">手机号码</label>

                                                <div class="controls">
                                                    <input type="text" class="input-large field" id="tel" name="tel"
                                                           value="${user.tel!""}">
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label" for="tel">QQ</label>

                                                <div class="controls">
                                                    <input type="text" class="input-large field" id="qq" name="qq"
                                                           value="${user.qq!""}">
                                                </div>
                                            </div>
                                            <br>

                                            <div class="control-group">
                                                <div class="controls padding-10">
                                                    <button type="submit" id="submit"
                                                            class="btn btn-primary align-center">保存
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.widget-main -->
                                </div>
                                <!-- /.widget-body -->
                            </div>


                        </fieldset>
                    </form>
                </div>
                </#if>
                <#-- 设置你的个人信息 end -->



        		<#-- 分组信息 start -->
                <div class="col-sm-7" style="opacity: 0.5">
                    <div class="widget-box">
                        <div class="widget-header header-color-red3">
                            <h5 class="widget-title">
                                <i class="icon-group"></i>
                                <span style="color: white">分组信息</span>
                            </h5>

                            <div class="widget-toolbar">
                                <a href="#" data-action="collapse">
                                    <i class="icon-chevron-up"></i>
                                </a>
                            </div>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main" id="groupwidget">
                                分组情况请参考下面的列表：<br/>
                                <ul>
                                    <li>如果列表中没有你想要的分组，你可以填写一个组名，这个组名请尽可能地细化。</li>
                                    <li>这个选项的重要性在于，你可以在作业的通知选项中，选择通知一个组的人。</li>
                                    <li>并且同组的人可以操作彼此的作业。</li>
                                </ul>

                                <table class="table table-striped table-bordered table-condensed">
                                    <tr>
                                        <th align="left" width="15%">组名</th>
                                        <th align="left" width="85%">成员</th>
                                    </tr>
                                <#if map?exists>
					                <#list map?keys as group> 
				                    <tr>
				                        <td align="left">${group!}</td>
				                        <td align="left">${map[group]!}</td>
					                </tr>
					                </#list>
					            </#if>

                                </table>

                            </div>
                            <!-- /.widget-main -->
                        </div>
                        <!-- /.widget-body -->
                    </div>
                </div>
                <#-- 分组信息 end -->

            </div>
        </div>


    </div>
    <#-- 页面主内容 end -->
</div>

<a href="#" class="scrollup" style="display: inline;">
    <img src="${rc.contextPath}/img/betop.png" width="66" height="67">
</a>

<div class="feedTool">
    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img
            border="0" src="${rc.contextPath}/img/qq.png" width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img
            border="0" src="${rc.contextPath}/img/x_alt.png" width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>
    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>
<script type="text/javascript">
    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('.scrollup').fadeIn();
        } else {
            $('.scrollup').fadeOut();
        }
    });

    $('.scrollup').click(function () {
        $("html, body").scrollTop(0);
        return false;
    });
    var userList = "", groupList = "", isAdmin;
    userList = userList
    <#if users??>
    <#list users as user>
    	+",${user.name!}"
    </#list>
    </#if>
    	;
    groupList = groupList
    <#if groups??>
    <#list groups as group>
    	+",${group.name!}"
    </#list>
    </#if>
    	;
    isAdmin = ${isAdmin?c};
    userList = userList.substr(1);
    groupList = groupList.substr(1);

</script>
<script src="${rc.contextPath}/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="${rc.contextPath}/static/js/user.js" type="text/javascript"></script>
</body>
</html>