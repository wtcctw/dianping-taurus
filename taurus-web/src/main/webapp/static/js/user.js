$.validator.addMethod("notAdmin",function(value,element,params){
	 if(value.toLowerCase( )=="admin")
		 return false;
	 else 
		 return true;
},"嘿嘿，你懂的");	   

$(document).ready(function() {
    $('li[id="user"]').addClass("active");
    $('#menu-toggler').on(ace.click_event, function() {
        $('#sidebar').toggleClass('display');
        $(this).toggleClass('display');
        return false;
    });

	$("#submit").click(function(e){
		if(!($("#user-form").validate().form())){
			return false;
		}
		$.ajax({
			url: '/rest/saveUser',
	        type: 'POST',
			error: function(){
				$("#alertContainer").html('<div id="alertContainer" class="alert alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>保存失败</strong></div>');
				$(".alert").alert();
			},
			success: function(response, textStatus) {
				$("#alertContainer").html('<div id="alertContainer" class="alert alert-success"><button type="button" class="close" data-dismiss="alert">&times;</button> <strong>保存成功</strong></div>');
				$(".alert").alert();
			},
			data: $("#user-form").serialize()
		});
		return false;
	});
	// $('#groupName').autocomplete({
	// 	source: groupList.split(',')
	// });
	$('#groupName',$('#user-form')).autocomplete({
        width: 210,
        delimiter: /(,)\s*/,
        zIndex: 9999,
        lookup: groupList.split(',')
    });
	
	  $('#user-form').validate({
		    rules: {
		    	groupName:{
		    		required:true,
		    		notAdmin:!isAdmin
		    	}
		    },
		    highlight: function(label) {
		    	$(label).closest('.control-group').removeClass('success').addClass('error');
		    },
		    success: function(label) {
                label.closest('.control-group').removeClass('error');
                label.closest('.control-group').addClass('success');
		    },
	  });
	
});