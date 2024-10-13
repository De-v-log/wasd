$(document).ready(function() {

    loadContentMain();
    $(document).on('click', '.group', function() {
        $('.group').removeClass('active');
        $(this).addClass('active');
        loadContentMain();
    });
});


function loadContentMain(){

    var url = $('.group.active').attr('attr-url');
    $.ajax({
        url: url,
        method: 'GET',
        success: function(response) {
            $('.content-main').html(response);
        }
    });

}