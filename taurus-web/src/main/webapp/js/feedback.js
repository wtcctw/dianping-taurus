/**
 * Created by kirinli on 14/12/9.
 */
$(function () {


    $('#feedback-content').bind('keyup paste', function () {
        checkTextareaNum();
    });

    $('#feedback-content').bind('blur', function () {
        checkTextareaLen();
    });

    $("#submit-btn").click(submitFeedBack);
});

function submitFeedBack() {
    var $me = $(this);
    $me.button('loading');

    var $feedback = $("#feedback-content").val();
    if (checkFeedBackForm()) {

        $.ajax({
            type: 'POST',
            url: "/feedback",
            data: {
                user: user,
                action: "feedback",
                feedback: $feedback
            },
            error: function () {
                $("#alertContainer").html('<div id="alertContainer" class="alert alert-danger" style="margin-top: 20px; "><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>提交失败!</strong><hr> <i class="icon-info red"> 非常抱歉，您的意见未能提交成功，请您再重新提交一次!</i></div>');
                $(".alert").alert();

            },
            success: function (response) {
                if(response == "error"){
                    $("#alertContainer").html('<div id="alertContainer" class="alert alert-danger" style="margin-top: 20px; "><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>提交失败!</strong><hr> <i class="icon-info red"> 非常抱歉，您的意见未能提交成功，请您再重新提交一次!</i></div>');
                    $(".alert").alert();
                }else{
                    $("#alertContainer").html('<div id="alertContainer" class="alert alert-success" style="margin-top: 20px; "><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>提交成功</strong><hr> <i class="icon-info green"> 感谢您对Taurus的支持，我们会尽快对您提出的与Taurus相关的反馈进行处理，并第一时间回复您的邮箱，请注意查看，谢谢。</i></div>');
                    $(".alert").alert();
                    $("#feedback-content").val("反馈成功！反馈内容为："+$feedback)

                }

            }
        });
    }

    $me.button('reset');
}

function checkFeedBackForm() {
    if (!checkTextareaLen()) {
        return false;
    }
    return true;
}

function checkTextareaLen() {
    var content = $("#feedback-content").val();
    var contentlen = 0;
    for (var i = 0; i < content.length; i++) {
        if (content.charCodeAt(i) < 0 || content.charCodeAt(i) > 255) {
            contentlen += 1;
        }
        contentlen += 1;
    }
    if (contentlen < 10) {
        $('div.limit').css('color', 'red').html("反馈内容不得少于5个汉字或10个英文字符");
        return false;
    }
    return true;
}

function checkTextareaNum() {
    var content = $("#feedback-content").val();
    $('div .limit').css('color', 'black').html('您还可以输入<em id="contentnum"></em>个字');
    if (content.length < 3200) {
        $("#contentnum").html(3200 - content.length);
    } else {
        $("#contentnum").html(0);
        $("#feedback-content").val(content.substring(0, 3200));
        document.getElementById('feedback-content').scrollTop = document.getElementById('feedback-content').scrollHeight;
    }
}