// Copyright (c) Bartłomiej Kobeszko 2018, GNU AGPL v. 3.0

var Form = function(guiManager) {
    var NOT_VALID_CLASS = "not_valid";
    var MONEY_VALIDATION_REGEXP = /^[+]?([1-9][0-9]*(?:([\.]|[\,])[0-9]*)?|0*\.0*[1-9][0-9]*)(?:[eE][+-][0-9]+)?$/; // numbers only, allowed one dot or semicolon

    var form;
    var accordionTitle;
    var calculationResultTemplate;

    var checkAdditionalOptionsFormValid = function() {
        var costsValid = form.form("is valid", "monthly_net_costs");

        if (costsValid) {
            accordionTitle.removeClass(NOT_VALID_CLASS);
        } else {
            accordionTitle.addClass(NOT_VALID_CLASS);
        }
    };

    var validateMoney = function(inputId, optional) {
        var result = false;

        var inputObject = $("#" + inputId);
        var inputElement = inputObject[0];
        var validity = inputElement.validity;

        if (validity && (validity.valid || (validity.stepMismatch && !validity.rangeUnderflow))) {
           var value = inputObject.val();

           if (value && value > 0) {
                result = MONEY_VALIDATION_REGEXP.exec(value);
           } else if (optional) {
                result = true;
           }
        }

        return result;
    };

    var afterCalculationResponse = function(calculationResult) {
        var filledTemplate = calculationResultTemplate(calculationResult);
        $("#calculationResults").empty().append(filledTemplate);

        guiManager.initControls();
        guiManager.formatFooter();

        if (Accordions.UniqueProfits.Opened) {
            guiManager.openAccordion(Accordions.UniqueProfits);
        }

        if (Accordions.Details.Opened) {
            guiManager.openAccordion(Accordions.Details);
        }
    };

    var formSubmit = function() {
        form.removeClass("warning");

        var done = false;

        // if response is not returned within 300 ms, show spinner
        setTimeout(function() {
            if (!done) {
                form.addClass("loading");
            }
        }, 300);

        Accordions.Options.Opened = guiManager.isAccordionOpened(Accordions.Options);
        Accordions.UniqueProfits.Opened = guiManager.isAccordionOpened(Accordions.UniqueProfits);
        Accordions.Details.Opened = guiManager.isAccordionOpened(Accordions.Details);

        $.post("calculate", {
            monthlyNetIncome: $("#monthlyNetIncome").val(),
            zusTaxType: guiManager.getRadioValue("#zusTaxType"),
            taxType: guiManager.getRadioValue("#taxType"),
            monthlyCosts: $("#monthlyNetCosts").val(),
            year: $("#year").val(),
            payZUSHealthInsurance: $("#payZUSHealthInsurance").prop("checked"),
            payVAT: $("#payVAT").prop("checked")
            })
            .done(afterCalculationResponse)
            .fail(function() {
                form.addClass("warning");
            })
            .always(function() {
                form.removeClass("loading");
                done = true;
            })
        ;
    };

    var initControls = function() {
        $("form .button").click(function() {
            form.form('validate form');
        });
    };

    var initVariables = function() {
        form = $(".ui.form");
        accordionTitle = $("#additionalOptionsAccordion .title");
        calculationResultTemplate = Handlebars.compile($("#calculation-result-template").html());
    };

    var initialize = function() {
        initVariables();
        initControls();

        $.fn.form.settings.rules.validateNetIncome = function(value, money) {
            return validateMoney("monthlyNetIncome", false);
        };

        $.fn.form.settings.rules.validateNetCosts = function(value, money) {
            return validateMoney("monthlyNetCosts", true);
        };

        form.form({
            onSuccess: function() {
                formSubmit();
                return false; // avoid page reload
            },
            onInvalid: function() {
                checkAdditionalOptionsFormValid();
            },
            onValid: function() {
                checkAdditionalOptionsFormValid();
            },
            on: 'change',
            delay: 300,
            inline: true,
            fields: {
                monthly_net_income: {
                    identifier  : 'monthly_net_income',
                    rules: [{
                        type   : 'validateNetIncome',
                        prompt : Messages.validation.validIncome
                    }]
                },
                monthly_net_costs: {
                    identifier  : 'monthly_net_costs',
                    rules: [{
                        type   : 'validateNetCosts',
                        prompt : Messages.validation.validCosts
                    }]
                }
            }
        });
    };

    return {
        initialize: initialize
    }
};