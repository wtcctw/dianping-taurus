var taskID;
var action_chinese;
jQuery(function($) {

	$(document).delegate('.detailBtn', 'click', function(e) {

        var anchor = this;
        if (e.ctrlKey || e.metaKey) {
            return true;
        } else {
            e.preventDefault();
        }
        $.ajax({
            type: "get",
            url: anchor.href,
            error: function () {
                $("#alertContainer").html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>获取详情失败</strong></div>');
                $(".alert").alert();
            },
            success: function (response, textStatus) {
                $("#detailModal").html(response);
                $("#detailModal").modal().css({
                    backdrop:false


                });


            }


        });
    });
});


function action(id, index) {
	action_chinese = $("#" + id + " .dropdown-menu li:nth-child(" + index + ") a").html();
    action_chinese = action_chinese.trim();
	taskID = id;
	var info;
		if (action_chinese == '删除') {
            info = "确定要删除任务<strong>" + id + "</strong>";
		} else if (action_chinese == '暂停') {
            info = "确定要暂停任务<strong>" + id + "</strong>";
		} else if (action_chinese == '执行') {
            info = "确定要执行任务<strong>" + id + "</strong>";
		} else if (action_chinese == '恢复') {
            info = "确定要恢复任务<strong>" + id + "</strong>";
		} else if (action_chinese == '清理拥塞') {
			info = "确定要清理任务拥塞<strong>" + id + "</strong>吗？清理拥塞任务将会暂停当前任务调度，清理完成后请手动恢复任务调度。";
		}
    bootbox.confirm(info, function(result) {
        if(result) {
            action_ok();
        }
    });

}


function action_update(id) {
	var btn = $('#updateBtn',$('#detailModal'));
	var form =  $("#form_"+id);
	if(btn.text().trim() == "修改"){
		btn.html("保存");
		$('.field',form).removeAttr("disabled", "disabled");
		$('#alertUser',form).autocomplete({
	        width: 210,
	        delimiter: /(;)\s*/,
	        zIndex: 9999,
	        lookup: userList.split(',')});
		$('#alertGroup',form).autocomplete({
	        width: 210,
	        delimiter: /(;)\s*/,
	        zIndex: 9999,
	        lookup: groupList.split(',')});
	} else {
		if(!(form.validate().form())){
			return false;
		}
		btn.button('loading');
		var params={};
		var file = $('#uploadFile',form).get(0);
		var newForm = document.createElement('form');
		var len=$(".field",form).length;
		for(var i = 0; i < len; i++)
 		{
      		var element = $(".field",form).get(i);
			if(element.id=="uploadFile" || element.id=="alertCondition"  || element.id=="alertType"){
				//do nothing
			}else if(element.id=="alertUser" || element.id=="alertGroup") {
				var result = element.value;
				if(result[result.length-1]==';')
					params[element.id] = result.substr(0,result.length-1);
				else
					params[element.id] = element.value;
			} else if(element.id == "isAutoKill"){
				var checked = $('input[type=radio]:checked','#isAutoKill').val();
				
				params[element.id] = checked;
			} else if(element.id == "iskillcongexp") {
				var checked = $('input[type=radio]:checked','#iskillcongexp').val();
				params[element.id] = checked;
			} else if(element.id == "isnotconcurrency") {
				var checked = $('input[type=radio]:checked','#isnotconcurrency').val();
				params[element.id] = checked;
			}else {
				params[element.id] = element.value;
			}
		}
		params["taskName"]=$("#taskName",form).get(0).value;
		var condition = $('.alertCondition',form).map(function() {
			if($(this).prop("checked"))
				return this.name;
		    }).get().join(";");
		var type = $('.alertType',form).map(function() {
			if($(this).prop("checked"))
				return this.name;
		    }).get().join(";");

		params["alertCondition"] = condition;
		params["alertType"] = type;
		params["poolId"] = $('#poolIdReal',form).val();
		if(params["dependency"]!=null && params["dependency"]!=''){
			params["alertCondition"] = params["alertCondition"] + ";DEPENDENCY_TIMEOUT";
		}
		for(var key in params) {
    		if(params.hasOwnProperty(key)) {
        		var hiddenField = document.createElement("input");
        		hiddenField.setAttribute("type", "hidden");
    			hiddenField.setAttribute("name", key);
        		hiddenField.setAttribute("value", params[key]);
				newForm.appendChild(hiddenField);
     		}
		}		
		if($('#uploadFile',form).val() == null || $('#uploadFile',form).val() == '') {
			newForm.setAttribute("enctype","application/x-www-form-urlencoded");
			$.ajax({
				type: "POST",
	            url: '/create_task?update='+id, 
	            data: $(newForm).serialize(), // serializes the form's elements.
	            enctype: 'application/x-www-form-urlencoded',
	            error: function(data)
	            {
	            	$("#alertContainer").html('<div id="alertContainer" class="alert alert-info"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>'
	    					+ '修改失败</strong></div>');
	    			$(".alert").alert();
	    			$("#detail_"+id).modal("hide");
	    			btn.button('reset');
	            },
	            success: function(data)
	            {
	            	$("#detail_"+id).modal("hide");
                    bootbox.confirm("您的修改已经生效！", function(result) {
                        window.location.reload();
                    });
	            },
	            cache: false,
		        contentType: 'application/xml',
		        processData: false
		    });
		} else {	
			function progressHandlingFunction(e){
			    if(e.lengthComputable){
			        $('progress').attr({value:e.loaded,max:e.total});
			    }
			}
			newForm.setAttribute('enctype','multipart/form-data');
			newForm.appendChild(file);
			$.ajax({
				type: "POST",
	           	url: '/create_task?update='+id, 
	           	data: new FormData(newForm),
	           	enctype: 'multipart/form-data',
	           	xhr: function() {  // custom xhr
		            myXhr = $.ajaxSettings.xhr();
		            if(myXhr.upload){ // check if upload property exists
		                myXhr.upload.addEventListener('progress',progressHandlingFunction, false); // for handling the progress of the upload
		            }
		            return myXhr;
		        },
	           	error: function(data)
	            {
	            	$("#alertContainer").html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>'
	    					+ '修改失败</strong></div>');
	    			$(".alert").alert();
	    			$("#detail_"+id).modal("hide");
	    			btn.button('reset');
	            },
	            success: function(data)
	            {
	            	$("#detail_"+id).modal("hide");
                    bootbox.confirm("您的修改已经生效！", function(result) {
                        if(result) {
                            window.location.reload();
                        }
                    });
	            },
	            cache: false,
		        contentType: false,
		        processData: false
		    });
			
		}
		$('#fileDiv',form).append(file);
	    return false; // avoid to execute the actual submit of the form.

	}
}

function action_ok() {
    if(action_chinese == '执行') {
        var isExistRunningTask =""
        $.ajax({
            url: "/attempts.do",
            data: {
                action: "runningtask",
                taskId: taskID
            }
            ,
            timeout: 1000,
            type: 'POST',
            async: false,
            error: function () {
                isExistRunningTask = "null";
        },
        success: function (response) {
            isExistRunningTask = response
        }
        });

        if(isExistRunningTask == "true"){
            bootbox.confirm("<i class='icon-info red'>该任务正在运行中，是否再一次执行？</i>", function(result) {
                if(result) {
                    do_action();
                }
            });
        }else{
            do_action();
        }

    }else{
        do_action();
    }


}

function do_action(){
    $.ajax({
        url : "/tasks.do",
        data : {
            action : toAction(action_chinese),
            id : taskID
        },
        type : 'POST',
        error: function(){
            $("#alertContainer").html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>'
                + action_chinese + '失败</strong></div>');
            $(".alert").alert();
            $('#confirm').modal("hide");
        },
        success: function(){
            if(action_chinese == '执行') {
                $("#alertContainer").html('<div id="alertContainer" class="alert alert-success"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>开始执行..</strong></div>');
            } else {
                $("#alertContainer").html('<div id="alertContainer" class="alert alert-success"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>'
                    + action_chinese + '成功</strong></div>');
            }
            $(".alert").alert();
            $('#confirm').modal("hide");
            if(action_chinese == '删除'){
                $('#' + taskID).remove();
            }else if(action_chinese == '暂停'){
                $('#' + taskID).addClass("error");
                $('#' + taskID + ' td .label').addClass("label-important").removeClass('label-info');
                $('#' + taskID + ' td .label').html('SUSPEND');
                $('#' + taskID + ' .dropdown-menu li:nth-child(2) a').html("恢复");
            }else if(action_chinese == '恢复'){
                $('#' + taskID).removeClass("error");
                $('#' + taskID + ' td .label').addClass("label-info").removeClass('label-important');
                $('#' + taskID + ' td .label').html('RUNNING');
                $('#' + taskID + ' .dropdown-menu li:nth-child(2) a').html("暂停");
            }
        }
    });
}

function toAction(chinese){
	var action = null;
	if(chinese == "删除"){
		action = "delete";
	}else if(chinese == "暂停"){
		action = "suspend";
	}else if(chinese == "恢复"){
		action = "resume";
	}else if(chinese == "执行"){
		action = "execute";
	}
	return action;
}




