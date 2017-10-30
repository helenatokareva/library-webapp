var user = {
    deleteUser : function(userId) {
        if (confirm("Are you sure you want to delete this object?")) {
            var url = $('#appUrl').val() + '/user/delete';
            $.ajax({
                type: "POST",
                url: url,
                async: false,
                headers: {
                    'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
                },
                data: {'userId': userId},
                success: function () {
                    var id = '#' + userId;
                    $(id).remove();
                },
                error: function (jqXHR) {
                    alert(jqXHR.responseText);
                }
            })
        }
    }
};

var book = {
    deleteBook : function(bookId) {
        if (confirm("Are you sure you want to delete this object?")) {
            var url = $('#appUrl').val() + '/book/delete';
            $.ajax({
                type: "POST",
                url: url,
                async: false,
                headers: {
                    'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
                },
                data: {'bookId': bookId},
                success: function () {
                    var id = '#' + bookId;
                    $(id).remove();

                    var quantity = parseInt($('#quantity').val());
                    $('#quantity').val(quantity - 1);
                },
                error: function (jqXHR) {
                    alert(jqXHR.responseText);
                }
            })
        }
    },

    takeBook : function(bookId, login) {
        var url = $('#appUrl').val() + '/book/take';
        $.ajax({
            type: "POST",
            url: url,
            async: false,
            headers: {
                'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
            },
            data: {'bookId': bookId, 'login': login},
            success: function (data) {
                var id = '#' + bookId;
                $(id).replaceWith(data);
            }
        });
    },

    giveBook : function(bookId) {
        var url = $('#appUrl').val() + '/book/give';
        $.ajax({
            type: "POST",
            url: url,
            async: false,
            headers: {
                'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
            },
            data: {'bookId': bookId},
            success: function (data) {
                var id = '#' + bookId;
                $(id).replaceWith(data);
            }
        })
    }
};