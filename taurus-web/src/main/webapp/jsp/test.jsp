<%--
  Created by IntelliJ IDEA.
  User: kirinli
  Date: 15/3/3
  Time: 下午12:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<script type="text/javascript">
    /*
     * 请修改下面js对象的内容以完成定制
     * 内容：请定制为需要提醒用户的内容,
     * 时间：请定制为事件发生的时间，格式为：年-月-日 时:分
     * advance：请定制为需要提前提醒用户的时间，0为准时提醒
     * url： 请定制为提醒用户访问的url链接
     * icon： 生成的icon的大小，可选值为1，2，3，尺寸分别为90*24，63*24，50*16
     * 注意：如果不使用标准接口，而是按需要自己生成链接地址
     * 请用encodeURIComponent对内容和时间进行编码。
     */
    var __qqClockShare = {
        content: "你的任务执行失败",
        time: "2015-3-3 12:23",
        advance: 0,
        url: "http://taurus.dp",
        icon: "1_1"
    };
    document.write('<a href="http://qzs.qq.com/snsapp/app/bee/widget/open.htm#content=' + encodeURIComponent(__qqClockShare.content) +'&time=' + encodeURIComponent(__qqClockShare.time) +'&advance=' + __qqClockShare.advance +'&url=' + encodeURIComponent(__qqClockShare.url) + '" target="_blank"><img src="http://i.gtimg.cn/snsapp/app/bee/widget/img/' + __qqClockShare.icon + '.png" style="border:0px;"/></a>');

</script>
</body>
</html>
