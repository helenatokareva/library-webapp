$(document).ready(function () {
    var table = {
        ascSortByAuthor : true,
        ascSortByTitle : false,
        sortColumn : 1,
        sort : function () {
            var data = table.getRenderedData(0);
            if (data != "") {
                $('#booksTable > tbody > tr:not(:first)').remove();
                $('#booksTable > tbody').append(data);
                if (($('#booksTable tr').length - 1) == $('#quantity').val()) {
                    $('#showMore').hide();
                }
            }
        },

        getRenderedData : function (quantity) {
            var url = $('#appUrl').val() + '/books/table',
                column,
                asc,
                responseData = "";
            if(table.sortColumn == 2) {
                column = 'title';
                asc = table.ascSortByTitle;
            }
            else {
                column = 'author';
                asc = table.ascSortByAuthor;
            }
            $.ajax({
                type: "GET",
                url: url,
                async: false,
                data: {'quantity': quantity, 'column': column, 'asc': asc},
                success: function(data){
                    responseData = data;
                }
            });
            return responseData;
        }
    };

    $.get($('#appUrl').val() + '/books/quantity', function (data) {
        $('#quantity').val(data);
    });

    $('.sorted').click(function(){
        $('.sorted').removeClass('header-sort');
        $(this).addClass('header-sort');
        if ($(this).attr('id') == 'header-author') {
            if(table.sortColumn == 1) {
                table.ascSortByAuthor = !table.ascSortByAuthor;
            }
            else {
                table.ascSortByAuthor = true;
                table.ascSortByTitle = false;
                table.sortColumn = 1;
            }
            table.sort();
            if(table.ascSortByAuthor) {
                $('#header-author').html('Author &uarr;');
            }
            else {
                $('#header-author').html('Author &darr;');
            }
            $('#header-title').html('Title');
        }
        else {
            if(table.sortColumn == 2) {
                table.ascSortByTitle = !table.ascSortByTitle;
            }
            else {
                table.ascSortByTitle = true;
                table.ascSortByAuthor = false;
                table.sortColumn = 2;
            }
            table.sort();
            if(table.ascSortByTitle) {
                $('#header-title').html('Title &uarr;');
            }
            else {
                $('#header-title').html('Title &darr;');
            }
            $('#header-author').html('Author');
        }
        $('#showMore').show();
    });


    $('.a-render').click(function () {
        var data = table.getRenderedData($('#booksTable tr').length - 1);
        if (data != "") {
            $("#booksTable tr:last").after(data);
            if (($('#booksTable tr').length - 1) == $('#quantity').val()) {
                $('#showMore').hide();
            }
        }
    });

});
