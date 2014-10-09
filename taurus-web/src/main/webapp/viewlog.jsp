<%@ page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Taurus</title>

    <meta charset="utf-8">
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <!-- basic styles -->
    <script type="text/javascript" src="resource/js/lib/jquery-1.9.1.min.js"></script>
    <link href="lib/ace/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="lib/ace/js/ace-extra.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/font-awesome.min.css"/>
    <script src="lib/ace/js/ace-elements.min.js"></script>
    <script src="lib/ace/js/ace.min.js"></script>
    <script src="lib/ace/js/bootbox.min.js"></script>
    <script type="text/javascript" src="resource/js/lib/raphael.2.1.0.min.js"></script>
    <script type="text/javascript" src="resource/js/lib/justgage.1.0.1.min.js"></script>
    <script type="text/javascript" src="js/login.js"></script>
    <script type="text/javascript" src="js/viewlog.js"></script>
    <!-- page specific plugin styles -->

    <!-- fonts -->
    <script src="lib/ace/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/ace-fonts.css"/>

    <!-- ace styles -->

    <link rel="stylesheet" href="lib/ace/css/ace.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-skins.min.css"/>
    <link href="css/bwizard.min.css" rel="stylesheet"/>
</head>
<body>
<div class="common-header" id="common-header">

</div>


    <div class="page-content">
        <div class="row">
            <div class="col-sm-6">
            <div id="error-panel" >
                <div class="spanm col-sm-12">
                    <ul class="error-tag" id="error-tag">
                        <li><a>错误信息<span class="label label-important">STDERR</span></a></li>
                    </ul>
                    <div data-spy="scroll" data-offset="0"  style="cursor: text; margin-top: 5px; background-color: #3A1042; color: #e6e6e6; font-size: 12px;  height: 650px; overflow-x: auto;"   id="errolog">


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

                    <div data-spy="scroll" class="col-sm-12" data-offset="0" style="cursor: text; margin-top: 5px; background-color: #3A1042; color: #e6e6e6; font-size: 12px;  height: 650px; overflow-x: auto;" id="strout">


                    </div>
                </div>
            </div>
        </div>
        </div>
    </div>
</body>

</html>