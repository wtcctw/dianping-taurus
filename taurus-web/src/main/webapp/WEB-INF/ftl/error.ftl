<!DOCTYPE html >
<head>
    <title>Taurus</title>

    <meta charset="utf-8">
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <!-- basic styles -->
    <script type="text/javascript" src="${rc.contextPath}/resource/js/lib/jquery-1.9.1.min.js"></script>
    <link href="${rc.contextPath}/lib/ace/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="${rc.contextPath}/lib/ace/js/ace-extra.min.js"></script>
    <link rel="stylesheet" href="${rc.contextPath}/lib/ace/css/font-awesome.min.css"/>
    <script src="${rc.contextPath}/lib/ace/js/ace-elements.min.js"></script>
    <script src="${rc.contextPath}/lib/ace/js/ace.min.js"></script>
    <script src="${rc.contextPath}/lib/ace/js/bootbox.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/resource/js/lib/raphael.2.1.0.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/resource/js/lib/justgage.1.0.1.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/login.js"></script>
    <!-- page specific plugin styles -->

    <!-- fonts -->
    <script src="${rc.contextPath}/lib/ace/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${rc.contextPath}/lib/ace/css/ace-fonts.css"/>

    <!-- ace styles -->

    <link rel="stylesheet" href="${rc.contextPath}/lib/ace/css/ace.min.css"/>
    <link rel="stylesheet" href="${rc.contextPath}/lib/ace/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="${rc.contextPath}/lib/ace/css/ace-skins.min.css"/>
    <link href="${rc.contextPath}/css/bwizard.min.css" rel="stylesheet"/>
</head>
<body>
<div class="page-content">


    <!-- /section:settings.box -->
    <div class="row">
        <div class="col-xs-12">
            <!-- PAGE CONTENT BEGINS -->

            <!-- #section:pages/error -->
            <div class="error-container">
                <div class="well">
                    <h1 class="grey lighter smaller">
										<span class="blue bigger-125">
											<i class="ace-icon fa fa-sitemap"></i>
											404
										</span>
                        Page Not Found
                    </h1>

                    <hr />
                    <h3 class="lighter smaller">你要的东西不在这或者目前不存在，到别处看看风景吧</h3>

                    <div>
                        <form class="form-search">
											<span class="input-icon align-middle">
												<i class="ace-icon fa fa-search"></i>

												<input type="text" class="search-query" placeholder="Give it a search..." />
											</span>
                            <button class="btn btn-sm" type="button">Go!</button>
                        </form>

                        <div class="space"></div>
                        <h4 class="smaller">您可以尝试:</h4>

                        <ul class="list-unstyled spaced inline bigger-110 margin-15">
                            <li>
                                <i class="ace-icon fa fa-hand-o-right blue"></i>
                                等待任务完成后，查看日志
                            </li>

                            <li>
                                <i class="ace-icon fa fa-hand-o-right blue"></i>
                                阅读帮助
                            </li>

                            <li>
                                <i class="ace-icon fa fa-hand-o-right blue"></i>
                                告诉我们
                            </li>
                        </ul>
                    </div>

                    <hr />
                    <div class="space"></div>

                    <div class="center">
                        <a href="javascript:history.back()" class="btn btn-grey">
                            <i class="ace-icon fa fa-arrow-left"></i>
                            返回
                        </a>

                        <a href="${rc.contextPath}/mvc/index" class="btn btn-primary">
                            <i class="ace-icon fa fa-tachometer"></i>
                            首页
                        </a>
                    </div>
                </div>
            </div>

            <!-- /section:pages/error -->

            <!-- PAGE CONTENT ENDS -->
        </div><!-- /.col -->
    </div><!-- /.row -->
</div><!-- /.page-content -->
</body>
