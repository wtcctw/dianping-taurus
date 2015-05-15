function login(url){
    if($('#username').val()==""&&$('#password').val()==""){
        $('#alertContainer').html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>用户名和密码不能为空！</strong></div>');
        return;
    }else if($('#username').val()==""){
        $('#alertContainer').html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>用户名不能为空！</strong></div>');
        return;
    }else if($('#password').val()==""){
        $('#alertContainer').html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>密码不能为空！</strong></div>');
        return;
    }
	$.ajax({
		url: '../login.do',
		data: {
			username : $('#username').val(),
			password:  $('#password').val()
		},
		type:"POST",
		statusCode:{
			401 : function(){
				$('#alertContainer').html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>用户名或密码错误，登陆失败！</strong></div>');
				$(".alert").alert();
			},
			200 : function(){
				if(url=='')
					window.location="../index.jsp";
				else{
					window.location=url;
				}
			},
			201 : function(){
				if(url=='')
					window.location="../index.jsp";
				else{
					window.location=url;
				}
			}
		}
	});
	return false;
}

function logout(){
	$.ajax({
		url: '../login.do',
		type:"get",
		statusCode:{
			200 : function(){
				window.location="../signin.jsp";
			}
			
		}
	});
	return false;
}

function EnterTo(url){
	if (window.event.keyCode == 13){
		login(url);
	}
}

function close_tool(){
    $(".feedTool").hide();
}
