/* jQuery Tags Input Revisited Plugin
 *
 * Copyright (c) Krzysztof Rusnarczyk
 * Licensed under the MIT license */

(function ($) {
    var delimiter = [];
    var inputSettings = [];
    var callbacks = [];

    $.fn.addTag = function (value, options) {
        options = jQuery.extend({
            focus: false,
            callback: true
        }, options);

        this.each(function () {
            let $this = $(this);
            var id = $(this).attr('id');

            var tagslist = $(this).val().split(_getDelimiter(delimiter[id]));
            if (tagslist[0] === '') tagslist = [];
            let tagId;
            if (typeof value === "object") {
                tagId = value.id;
                value = value.value;
            } else {
                tagId = value;
            }
            value = jQuery.trim(value);

            if ((inputSettings[id].unique && $(this).tagExist(tagId)) || !_validateTag(tagId, inputSettings[id], tagslist, delimiter[id])) {
                $('#' + id + '_tag').addClass('error');
                return false;
            }

            let tagSpan = $('<span>', {class: 'tag'}).append(
                $('<span>', {class: 'tag-text'}).text(value),
                $('<button>', {class: 'tag-remove'}).click(function () {
                    return $('#' + id).removeTag(encodeURI(tagId));
                })
            ).data("id", tagId).insertBefore('#' + id + '_addTag');
            let tagColor = $(this).data("setting").tagColor;
            if ($.isFunction(tagColor)) {
                tagColor = tagColor.apply(this, [{
                    id: tagId,
                    value: value
                }]);
            }
            if (options.color) {
                tagSpan.css("background-color", options.color);
            } if(tagColor){
                tagSpan.css("background-color", tagColor);
            }

            tagslist.push(tagId);
            let values = $(this).data("values") || [];
            values.push({
                id: tagId,
                value: value
            });
            $(this).data("values", values);

            $('#' + id + '_tag').val('');
            if (options.focus) {
                $('#' + id + '_tag').focus();
            } else {
                $('#' + id + '_tag').blur();
            }

            $.fn.tagsInput.updateTagsField(this, tagslist);

            if (options.callback && callbacks[id] && callbacks[id]['onAddTag']) {
                var f = callbacks[id]['onAddTag'];
                f.call(this, this, value);
            }

            if (callbacks[id] && callbacks[id]['onChange']) {
                var i = tagslist.length;
                var f = callbacks[id]['onChange'];
                f.call(this, this, value);
            }
        });

        return false;
    };

    $.fn.removeTag = function (value) {
        value = decodeURI(value);

        this.each(function () {
            var id = $(this).attr('id');

            var old = $(this).val().split(_getDelimiter(delimiter[id]));

            $('#' + id + '_tagsinput .tag').remove();

            let values = $(this).data("values") || [];
            let temp=[];
            var str = '';
            for (i = 0; i < values.length; ++i) {
                if (values[i].id != value) {
                    str = str + _getDelimiter(delimiter[id]) + values[i].value;
                    temp.push(values[i]);
                }
            }
            $(this).data("values",temp);
            $.fn.tagsInput.importTags(this, temp);

            if (callbacks[id] && callbacks[id]['onRemoveTag']) {
                var f = callbacks[id]['onRemoveTag'];
                f.call(this, this, value);
            }

            let setting = $(this).data("setting");
            let autocompleteContainer = $(this).data("autocompleteContainer");
            if (autocompleteContainer) {
                autocompleteContainer.find("li").each(function () {
                    if ($(this).data("rowData")[setting.autocomplete.idField] == value) {
                        $(this).removeClass("disabled");
                    }
                });
            }
        });

        return false;
    };

    $.fn.clearTag = function () {
        this.each(function () {
            let id = $(this).attr('id');
            $(this).data("values",[]);
            $('#' + id + '_tagsinput .tag').remove();
        });
        return false;
    };

    $.fn.tagExist = function (val) {
        var id = $(this).attr('id');
        var tagslist = $(this).val().split(_getDelimiter(delimiter[id]));
        return (jQuery.inArray(val, tagslist) >= 0);
    };

    $.fn.importTags = function (str) {
        var id = $(this).attr('id');
        $('#' + id + '_tagsinput .tag').remove();
        $.fn.tagsInput.importTags(this, str);
    };

    $.fn.tagsInput = function (options) {
        var settings = jQuery.extend({
            interactive: true,
            placeholder: 'Add a tag',
            minChars: 0,
            maxChars: null,
            limit: null,
            validationPattern: null,
            width: 'auto',
            height: 'auto',
            autocomplete: null,
            hide: true,
            delimiter: ',',
            unique: true,
            removeWithBackspace: true
        }, options);

        var uniqueIdCounter = 0;

        this.each(function () {
            if (typeof $(this).data('tagsinput-init') !== 'undefined') return;

            $(this).data('tagsinput-init', true);

            if (settings.hide) $(this).hide();

            var id = $(this).attr('id');
            if (!id || _getDelimiter(delimiter[$(this).attr('id')])) {
                id = $(this).attr('id', 'tags' + new Date().getTime() + (++uniqueIdCounter)).attr('id');
            }

            var data = jQuery.extend({
                pid: id,
                real_input: '#' + id,
                holder: '#' + id + '_tagsinput',
                input_wrapper: '#' + id + '_addTag',
                fake_input: '#' + id + '_tag'
            }, settings);

            delimiter[id] = data.delimiter;
            inputSettings[id] = {
                minChars: settings.minChars,
                maxChars: settings.maxChars,
                limit: settings.limit,
                validationPattern: settings.validationPattern,
                unique: settings.unique
            };

            if (settings.onAddTag || settings.onRemoveTag || settings.onChange) {
                callbacks[id] = [];
                callbacks[id]['onAddTag'] = settings.onAddTag;
                callbacks[id]['onRemoveTag'] = settings.onRemoveTag;
                callbacks[id]['onChange'] = settings.onChange;
            }

            $(data.real_input).data("setting", settings);
            let autocompleteContainer = $('<div class="tagsInput-autocomplete-container">' +
                '<ul></ul>' +
                '</div>')
                .appendTo('body')
                .delegate(">ul>li", "click", function () {
                    let li = $(this);
                    if (li.hasClass("disabled")) {
                        return;
                    }
                    li.addClass("disabled");
                    let rowData = li.data("rowData");
                    let id = rowData[settings.autocomplete.idField];
                    let text = rowData[settings.autocomplete.textField];
                    $(data.real_input).addTag({
                        id: id + "",
                        value: text
                    }, {
                        focus: true,
                        unique: settings.unique
                    });
                });

            var markup = $('<div>', {id: id + '_tagsinput', class: 'tagsinput'}).append(
                $('<div>', {id: id + '_addTag'}).append(
                    settings.interactive ? $('<input>', {
                        id: id + '_tag',
                        class: 'tag-input',
                        value: '',
                        placeholder: settings.placeholder
                    }) : null
                )
            );

            $(markup).insertAfter(this);

            $(data.holder).css('width', settings.width);
            $(data.holder).css('min-height', settings.minHeight);
            $(data.holder).css('height', settings.height);

            if ($(data.real_input).val() !== '') {
                $.fn.tagsInput.importTags($(data.real_input), $(data.real_input).val());
            }

            // Stop here if interactive option is not chosen
            if (!settings.interactive) return;

            $(data.fake_input).val('');
            $(data.fake_input).data('pasted', false);

            $(data.fake_input).on('focus', data, function (event) {
                $(data.holder).addClass('focus');

                if ($(this).val() === '') {
                    $(this).removeClass('error');
                }
            });

            $(data.fake_input).on('blur', data, function (event) {
                $(data.holder).removeClass('focus');
            });

            if (settings.autocomplete) {
                $(data.real_input).data("autocompleteContainer", autocompleteContainer);
                let XHR;
                $(data.fake_input).on("input", function () {
                    if (XHR) {
                        XHR.abort();
                        XHR = undefined;
                    }
                    let searchKey = $(this).val().trim();
                    XHR = $.ajax({
                        type: "get",
                        url: settings.autocomplete.source,
                        data: {searchKey: searchKey},
                        dataType: "json",
                        success: function (msg) {
                            XHR = undefined;
                            let rows = $.isArray(msg) ? msg : msg.rows;
                            showAutocomplete($(data.fake_input), autocompleteContainer, rows, settings.autocomplete, $(data.real_input).val().split(","));
                        }
                    });
                }).on("click", function () {
                    if (settings.autocomplete.minLength <= 0) {
                        $(this).triggerHandler("input");
                    }
                });
                $(window).on("click", function (event) {
                    let target = $(event.target);
                    if (!target.hasClass("tag-input") && !target.hasClass("tagsInput-autocomplete-container") && target.parents(".tagsInput-autocomplete-container").length === 0) {
                        autocompleteContainer.hide();
                    }
                })
            } else {
                $(data.fake_input).on('blur', data, function (event) {
                    $(event.data.real_input).addTag($(event.data.fake_input).val(), {
                        focus: true,
                        unique: settings.unique
                    });

                    return false;
                });
                // If a user types a delimiter create a new tag
                $(data.fake_input).on('keypress', data, function (event) {
                    if (_checkDelimiter(event)) {
                        event.preventDefault();

                        $(event.data.real_input).addTag($(event.data.fake_input).val(), {
                            focus: true,
                            unique: settings.unique
                        });

                        return false;
                    }
                });
            }

            $(data.fake_input).on('paste', function () {
                $(this).data('pasted', true);
            });

            // If a user pastes the text check if it shouldn't be splitted into tags
            $(data.fake_input).on('input', data, function (event) {
                if (!$(this).data('pasted')) return;

                $(this).data('pasted', false);

                var value = $(event.data.fake_input).val();

                value = value.replace(/\n/g, '');
                value = value.replace(/\s/g, '');

                var tags = _splitIntoTags(event.data.delimiter, value);

                if (tags.length > 1) {
                    for (var i = 0; i < tags.length; ++i) {
                        $(event.data.real_input).addTag(tags[i], {
                            focus: true,
                            unique: settings.unique
                        });
                    }

                    return false;
                }
            });

            // Deletes last tag on backspace
            data.removeWithBackspace && $(data.fake_input).on('keydown', function (event) {
                if (event.keyCode == 8 && $(this).val() === '') {
                    event.preventDefault();
                    var lastTag = $(this).closest('.tagsinput').find('.tag:last > span').text();
                    var id = $(this).attr('id').replace(/_tag$/, '');
                    $('#' + id).removeTag(encodeURI(lastTag));
                    $(this).trigger('focus');
                }
            });

            // Removes the error class when user changes the value of the fake input
            $(data.fake_input).keydown(function (event) {
                // enter, alt, shift, esc, ctrl and arrows keys are ignored
                if (jQuery.inArray(event.keyCode, [13, 37, 38, 39, 40, 27, 16, 17, 18, 225]) === -1) {
                    $(this).removeClass('error');
                }
            });
        });

        return this;
    };

    $.fn.tagsInput.updateTagsField = function (obj, tagslist) {
        var id = $(obj).attr('id');
        $(obj).val(tagslist.join(_getDelimiter(delimiter[id])));
    };

    $.fn.tagsInput.importTags = function (obj, val) {
        $(obj).val('');

        var id = $(obj).attr('id');
        var tags;
        if($.isArray(val)){
            tags=val;
        }else{
            tags = _splitIntoTags(delimiter[id], val);
        }

        for (i = 0; i < tags.length; ++i) {
            $(obj).addTag(tags[i], {
                focus: false,
                callback: false
            });
        }

        if (callbacks[id] && callbacks[id]['onChange']) {
            var f = callbacks[id]['onChange'];
            f.call(obj, obj, tags);
        }
    };

    var _getDelimiter = function (delimiter) {
        if (typeof delimiter === 'undefined') {
            return delimiter;
        } else if (typeof delimiter === 'string') {
            return delimiter;
        } else {
            return delimiter[0];
        }
    };

    var _validateTag = function (value, inputSettings, tagslist, delimiter) {
        var result = true;

        if (value === '') result = false;
        if (value.length < inputSettings.minChars) result = false;
        if (inputSettings.maxChars !== null && value.length > inputSettings.maxChars) result = false;
        if (inputSettings.limit !== null && tagslist.length >= inputSettings.limit) result = false;
        if (inputSettings.validationPattern !== null && !inputSettings.validationPattern.test(value)) result = false;

        if (typeof delimiter === 'string') {
            if (value.indexOf(delimiter) > -1) result = false;
        } else {
            $.each(delimiter, function (index, _delimiter) {
                if (value.indexOf(_delimiter) > -1) result = false;
                return false;
            });
        }

        return result;
    };

    var _checkDelimiter = function (event) {
        var found = false;

        if (event.which === 13) {
            return true;
        }

        if (typeof event.data.delimiter === 'string') {
            if (event.which === event.data.delimiter.charCodeAt(0)) {
                found = true;
            }
        } else {
            $.each(event.data.delimiter, function (index, delimiter) {
                if (event.which === delimiter.charCodeAt(0)) {
                    found = true;
                }
            });
        }

        return found;
    };

    var _splitIntoTags = function (delimiter, value) {
        if (value === '') return [];

        if (typeof delimiter === 'string') {
            return value.split(delimiter);
        } else {
            var tmpDelimiter = '∞';
            var text = value;

            $.each(delimiter, function (index, _delimiter) {
                text = text.split(_delimiter).join(tmpDelimiter);
            });

            return text.split(tmpDelimiter);
        }

        return [];
    };

    var showAutocomplete = function (input, container, rows, option, existsIds) {
        if (rows) {
            let ul = container.find("ul").empty();
            for (let i = 0; i < rows.length; i++) {
                let rowData = rows[i];
                let id = rowData[option.idField];
                let disable = false;
                for (let i = 0; i < existsIds.length; i++) {
                    if (id == existsIds[i]) {
                        disable = true;
                        break;
                    }
                }
                $('<li' + (disable ? ' class="disabled"' : '') + '><span class="text">' + (rowData[option.textField] || "") + '</span><span class="unit">' + (rowData[option.unitField] || "") + '</span></li>')
                    .data("rowData", rowData)
                    .appendTo(ul);
            }
        }
        let offset = input.offset();
        container.css({
            width: input.outerWidth() + "px",
            top: offset.top + input.outerHeight() + "px",
            left: offset.left + "px",
        }).show();
    };
})(jQuery);
