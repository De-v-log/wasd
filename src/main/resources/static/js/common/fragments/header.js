$(function () {

    $('.group').on('click', function() {
        var url = $(this).attr('attr-url');

        $.ajax({
            url: url,
            method: 'GET',
            success: function(response) {
                $('.content-main').html(response);
            },
            error: function(error) {
                console.error('Error loading content:', error);
            }
        });
    });
});
