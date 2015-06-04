var attemptID;
var taskId;
function redo(id){
    taskId = id;
    $("#id_header").html("重跑");
    var info = "确定要重跑任务<strong>" + id + "</strong>";
    bootbox.confirm(info, function(result) {
        if(result) {
            redo_ok();
        }
    });
}
function action(id) {
	attemptID = id;	
	$("#id_header").html("Kill");
	var info = "确定要Kill任务<strong>" + id + "</strong>";
    bootbox.confirm(info, function(result) {
        if(result) {
            action_ok();
        }
    });
}

function redo_ok() {
    $.ajax({
        url : "../tasks.do",
        data : {
            action : "execute",
            id : taskId
        },
        type : 'POST',
        error: function(){
            $("#alertContainer").html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>重跑失败</strong></div>');
            $(".alert").alert();
            $('#confirm').modal("hide");
        },
        success: function(){
           $("#alertContainer").html('<div id="alertContainer" class="alert alert-success"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>开始重跑..</strong></div>');
           location.reload();

        }
    });
}

function action_ok() {
	$.ajax({
		url : "../attempts.do",
		data : {
			id : attemptID,
			action : 'kill'
		},
		type : 'POST',
		error: function(){
				$("#alertContainer").html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> Kill <strong>' + attemptID + '</strong>失败</div>');
				$(".alert").alert();
				$('#confirm').modal("hide");
		},
		success: function(){
				$("#alertContainer").html('<div id="alertContainer" class="alert alert-success"><button type="button" class="close" data-dismiss="alert">&times;</button> Kill <strong>' + attemptID + '</strong>成功</div>');
				$(".alert").alert();
				$('#confirm').modal("hide");
				$('#' + attemptID + ' td .label').addClass("label-important").removeClass('label-info');
				$('#' + attemptID + ' td .label').html('KILLED');
            location.reload();
        }
	});
}