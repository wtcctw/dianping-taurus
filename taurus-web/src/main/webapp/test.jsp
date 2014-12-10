<!DOCTYPE HTML>
<html lang="en-US">
<head>
    <meta charset="UTF-8"/>
    <title>xxx</title>
</head>
<body>
<a href="javascript:;" class="qq">发送信息给好友</a>
</body>
</html>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script>
    function qqShare(){
        var p = {
            url:'http://wwww.baidu.com', /*获取URL，可加上来自分享到QQ标识，方便统计*/
            desc:'发送信息给好友',
            title:'发送信息给好友', /*分享标题(可选)*/
            summary:'', /*分享摘要(可选)*/
            pics:'', /*分享图片(可选)*/
            flash: '', /*视频地址(可选)*/
            site:'http://wwww.baidu.com', /*分享来源(可选) 如：QQ分享*/
            style:'203',
            width:16,
            height:16
        };
        var s = [];
        for(var i in p){
            s.push(i + '=' + encodeURIComponent(p[i]||''));
        }
        var qhref = "http://connect.qq.com/widget/shareqq/index.html?"+s.join('&');
        $(".qq").attr({href:qhref,target:"_blank"});
    };

    $(".qq").click(function(){
        qqShare();
    })
</script>