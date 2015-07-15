$(function () {
    var sidebar = document.getElementById("sidebar");
    $('li[id="host"]').addClass("active");
    $('#menu-toggler').on(ace.click_event, function() {
        sidebar.style.display="block";
        $('#sidebar').toggleClass('display');
        $(this).toggleClass('display');

        return false;
    });

    $('#ipList').change(function(){
        if($("#ipList").val()!=null && $("#ipList").val()!="")
            window.location = "/hosts?hostName=" + $("#ipList").val();
        else
            window.location = "/hosts";
    });
});



