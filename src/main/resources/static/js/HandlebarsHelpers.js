// Copyright (c) Bartłomiej Kobeszko 2018, GNU AGPL v. 3.0

Handlebars.registerHelper("ifGreaterThan", function(value, comparedValue, options) {
    if (value > comparedValue) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }
});

Handlebars.registerHelper("ifEquals", function(value1, value2, options) {
    if (value1 === value2) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }
});

Handlebars.registerHelper("getMonth", function(value) {
    return Months[value - 1];
});

Handlebars.registerHelper("getLastItem", function(value) {
    return value[value.length - 1];
});

Handlebars.registerHelper("getInfoHeader", function(key) {
    return ImportantInfos.get("header." + key);
});

Handlebars.registerHelper("getInfoMessage", function(key, moneyValue) {
    return ImportantInfos.get("message." + key).replace("%s", moneyValue);
});