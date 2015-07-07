<div class="navbar navbar-default" id="navbar">
    <script type="text/javascript">
        try{ace.settings.check('navbar', 'fixed')} catch(e){}
    </script>

    <div class="navbar-container" id="navbar-container" style="height: 30px">
        <div class="navbar-header pull-left">
            <a href="${rc.contextPath}/index" class="navbar-brand">
                <i class="icon-tasks"></i>
                Taurus
            </a>
            <!-- /.brand -->
        </div>
        <!-- /.navbar-header -->
        <div class="navbar-header">
            <span style="margin:10px;font-size: 16px" class="label label-transparent">任务调度系统</span>
        </div>
        <!-- /.navbar-header -->
        <button type="button" class="navbar-toggle pull-left" id="menu-toggler">
            <span class="sr-only">Toggle sidebar</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <div class="navbar-header pull-right" role="navigation">
            <ul class="nav ace-nav">
                <li class="light-blue">
                    <a data-toggle="dropdown" href="#" target="_self" class="dropdown-toggle">
                        <img class="nav-user-photo" src="${rc.contextPath}/lib/ace/avatars/user.jpg" alt="Jason's Photo"/>
            <span class="user-info">
                                    <small>欢迎,</small>
                                    <div id="username">${currentUser!}
                                    </div>
                                </span>

                        <i class="icon-caret-down"></i>
                    </a>
                    <ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                        <li>
                            <a href="${rc.contextPath}/user">
                                <i class="icon-cogs"></i>
                                设置
                            </a>
                        </li>
                        <li>
                            <a href="javascript:logout('${currentUser!}')">
                                <i class="icon-off"></i>
                                退出
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
            <!-- /.ace-nav -->
        </div>
        <div class="pull-right" style="margin:10px;color: white;">
        <#if switchUrls?exists>
        <#list switchUrls as switchUrl>
            <a target="_blank" style="margin:10px;color: white;"
               href="${switchUrl!}"><i class="icon-bolt">
                <#if switchUrl?contains("alpha")>
                切换alpha
                <#elseif switchUrl?contains("beta")>
                切换beta
                <#elseif switchUrl?contains("ppe")>
                切换ppe
                <#else>
                切换线上
                </#if>
               </i></a>
        </#list>
        </#if>
        </div>
        <div class="pull-right" style="margin:10px;color: white;">
            <a rel="popoverpic" style="color:white;"><i class="icon-group">加入后援团: 155326270</i></a>
        </div>
<script>
    $(function(){
        $("a[rel=popoverpic]").popover({
            trigger:'manual',
            placement : 'bottom', //placement of the popover. also can use top, bottom, left or right
            //title : '<div style="text-align:center; color:gray; font-size:20px;">Taurus后援团:155326270</div>', //this is the top title bar of the popover. add some basic css
            html: 'true', //needed to show html of course
            content : '<div id="popOverBox"><img src="${rc.contextPath}/static/img/taurus.png" width="246px" height="246px" alt="暂无" /></div>', //this is the content of the html box. add the image here or anything you want really.
            animation: false
        }).on("mouseenter", function () {
                    var _this = this;
                    $(this).popover("show");
                    $(this).siblings(".popover").on("mouseleave", function () {
                        $(_this).popover('hide');
                    });
                }).on("mouseleave", function () {
                    var _this = this;
                    setTimeout(function () {
                        if (!$(".popover:hover").length) {
                            $(_this).popover("hide")
                        }
                    }, 100);
                });
    });
</script>
        <div class="pull-right ng-binding" style="margin:10px;color: white;" ng-bind="monitorMessage"><i
                class="icon-user-md">开发者：李明 <a target="_blank" style="margin:10px;color: white;"
                                               href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img
                border="0" src="${rc.contextPath}/img/qq.png" width="20" height="20" color="white" alt="点我报错" title="点我报错"/>点我报错</a></i>
            <i class="icon-phone">: 13661871541</i>
        </div>

    </div>
    <!-- /.container -->
</div>