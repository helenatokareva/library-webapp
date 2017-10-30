var modal = {
    show : function () {
        $('.overlay').fadeIn(400,
            function(){
                $('.modal-form')
                    .css('display', 'block')
                    .animate({opacity: 1, top: '50%'}, 200);
            });
    },

    close : function () {
        $('.modal-form')
            .animate({opacity: 0, top: '45%'}, 200,
                function(){ // после анимации
                    $(this).css('display', 'none');
                    $('.overlay').fadeOut(400);
                }
            );
        $('.validate-fields').removeClass( "ui-state-error" );
    },

    get : function (url) {
        url = $('#appUrl').val() + url;
        $.ajax({
            type: "GET",
            url: url,
            async: false,
            success: function (data) {
                $('.modal-form').html(data);
                $('.password').val('');
                modal.show();
            }
        })
    }
};