// Copyright (c) Bartłomiej Kobeszko 2018, GNU AGPL v. 3.0

var Application = function() {
    var guiManager;
    var form;

    var initVariables = function() {
        guiManager = new GuiManager();
        form = new Form(guiManager);
    };

    var initialize = function() {
        initVariables();
        guiManager.initControls();
        form.initialize();
    };

    return {
        initialize: initialize
    }
};

$(function() {
  new Application().initialize();
});