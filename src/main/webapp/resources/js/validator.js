var validator = {
    updateMsg : function(msgText) {
        var msg = $(".validate-msg");
        msg.text(msgText)
            .addClass("ui-state-highlight");
        setTimeout(function () {
            msg.removeClass("ui-state-highlight", 1500);
        }, 500);
    },

    checkLength : function(fieldObject, fieldName, min, max) {
        if (fieldObject.val().length > max || fieldObject.val().length < min) {
            fieldObject.addClass("ui-state-error");
            validator.updateMsg("Length of " + fieldName + " must be between " +
                min + " and " + max + ".");
            return false;
        } else {
            return true;
        }
    },

    checkRegexp : function(fieldObject, regexp, msg) {
        if (!(regexp.test(fieldObject.val()))) {
            fieldObject.addClass("ui-state-error");
            validator.updateMsg(msg);
            return false;
        } else {
            return true;
        }
    },

    checkPasswordsMatch : function(pass1, pass2) {
        if (pass2.val() != pass1.val()) {
            pass2.addClass("ui-state-error");
            validator.updateMsg("Passwords do not match");
            return false;
        }
        else {
            return true;
        }
    },

    checkOldPassword : function(id, pass) {
        var shaObject = new jsSHA("SHA-256", "TEXT");
        shaObject.update(pass.val());
        var hashPass = shaObject.getHash("HEX"),
            data = JSON.stringify({'pass': hashPass}),
            check = false,
            checkUrl = $('#appUrl').val() + '/check/pass/' + id;
        $.ajax({
            type: "POST",
            url: checkUrl,
            async: false,
            headers: {
                'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
            },
            data: data,
            contentType: "application/json; charset=utf-8",
            success: function () {
                check = true;
            },
            error: function () {
                check = false;
            }
        });
        if (!check) {
            pass.addClass("ui-state-error");
            validator.updateMsg('Current password is wrong');
        }
        return check;
    },

    checkUniqueLogin : function(id, login) {
        var check = false,
            data = JSON.stringify({'login': login.val()}),
            checkUrl = $('#appUrl').val() + '/check/login';

        if (id != "") {
            checkUrl = checkUrl + '/' + id;
        }
        $.ajax({
            type: "POST",
            url: checkUrl,
            async: false,
            headers: {
                'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
            },
            data: data,
            contentType: "application/json; charset=utf-8",
            success: function () {
                check = true;
            },
            error: function () {
                check = false;
            }
        });
        if (!check) {
            login.addClass("ui-state-error");
            validator.updateMsg('This username is already taken');
        }
        return check;
    },

    validateUserForm : function() {
        var login = $("#login"),
            oldPassword = $("#oldPassword"),
            password = $("#password"),
            rePassword = $("#rePassword"),
            allFields = $([]).add(login).add(password).add(rePassword);
        allFields.removeClass("ui-state-error");

        if (!validator.checkLength(login, "login", 3, 30)) {
            return false;
        }
        if (!validator.checkRegexp(login, /^[a-z]([0-9a-z_])+$/i, "Login may consist of a-z, 0-9, underscores and must begin with a letter.")) {
            return false;
        }
        if ($('#userId').val() == "") {
            if (!validator.checkLength(password, "password", 5, 64)) {
                return false;
            }
            if (!validator.checkLength(rePassword, "password", 5, 64)) {
                return false;
            }
            if (!validator.checkPasswordsMatch(password, rePassword)) {
                return false;
            }
        }
        else {
            if ((oldPassword.val() != "") || (password.val() != "") || (rePassword.val() != "")) {
                if (!validator.checkLength(oldPassword, "password", 5, 64)) {
                    return false;
                }
                if (!validator.checkLength(password, "password", 5, 64)) {
                    return false;
                }
                if (!validator.checkLength(rePassword, "password", 5, 64)) {
                    return false;
                }
                if (!validator.checkPasswordsMatch(password, rePassword)) {
                    return false;
                }
                if (!validator.checkOldPassword($('#userId').val(), oldPassword)) {
                    return false;
                }
            }
        }
        if (!validator.checkUniqueLogin($('#userId').val(), login)) {
            return false;
        }
        return true;
    },

    checkUniqueIsbn : function(id, isbn) {
        var check = false,
            data = JSON.stringify({'isbn': isbn.val()}),
            checkUrl = $('#appUrl').val() + '/check/isbn';
        if (id != "") {
            checkUrl = checkUrl + '/' + id;
        }

        $.ajax({
            type: "POST",
            url: checkUrl,
            async: false,
            headers: {
                'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
            },
            data: data,
            contentType: "application/json; charset=utf-8",
            success: function () {
                check = true;
            },
            error: function () {
                check = false;
            }
        });
        if (!check) {
            isbn.addClass("ui-state-error");
            validator.updateMsg('This ISBN already exists');
        }
        return check;
    },


    validateBookForm : function() {
        var isbn = $("#form-isbn"),
            author = $("#form-author"),
            title = $("#form-title"),
            allFields = $([]).add(isbn).add(author).add(title);
        allFields.removeClass("ui-state-error");

        if (isbn.val().length != 17) {
            isbn.addClass("ui-state-error");
            validator.updateMsg("ISBN must be contain 17 symbols");
            return false;
        }
        if (!validator.checkRegexp(isbn, /^[0-9]+(-[0-9]+){4}$/, "ISBN may consist of 0-9, hyphens and must begin with a number.")) {
            return false;
        }
        if (!validator.checkRegexp(author, /^[а-яa-z][а-яa-z \.]+$/i, "Field 'Author' can contain only letters, spaces and dots.")) {
            return false;
        }
        if (!validator.checkUniqueIsbn($('#bookId').val(), isbn)) {
            return false;
        }
        if ($('#bookId').val() == "") {
            var quantity = parseInt($('#quantity').val());
            $('#quantity').val(quantity + 1);
        }
        return true;
    }
};