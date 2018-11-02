// Copyright (c) Bartłomiej Kobeszko 2018, GNU AGPL v. 3.0

var GuiManager = function() {
    var FOOTER_TOP_MARGIN_LARGE_CLASS = "footer_top_margin_large";

    var areResultsVisible = function() {
        return $("#tableResults").length > 0;
    };

    var isAccordionOpened = function(accordion) {
        return $("#" + accordion.Id).children(".content").first().hasClass("active");
    };

    var openAccordion = function(accordion) {
        $("#" + accordion.Id).children(".title").first().click();
    };

    var formatFooter = function() {
        var resultsVisible = areResultsVisible();
        var optionsOpened = isAccordionOpened(Accordions.Options);
        var footer = $("#footer");

        if (resultsVisible || optionsOpened) {
            footer.removeClass(FOOTER_TOP_MARGIN_LARGE_CLASS);
        } else if (!resultsVisible) {
            footer.addClass(FOOTER_TOP_MARGIN_LARGE_CLASS);
        }
    };

    var getRadioValue = function(radioContainerSelector) {
        return $(radioContainerSelector + " .checkbox.checked label").attr("val");
    };

    var initControls = function() {
        $('.ui.accordion').accordion({
            onChange:  function() {
                formatFooter()
            }
        });

        $('.ui.checkbox').checkbox();
        $('.ui.radio.checkbox').checkbox();
        $('select.dropdown').dropdown();

        $('.cookie.nag').nag({
            key: 'accepts-cookies',
            value: true
        });
    };

    return {
        isAccordionOpened: isAccordionOpened,
        openAccordion: openAccordion,
        formatFooter: formatFooter,
        getRadioValue: getRadioValue,
        initControls: initControls
    }
};