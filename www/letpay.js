var exec = require("cordova/exec");

module.exports = {

    ENTER_OPTIONS: {
	partner_order_no: "",
        subject: "LET_TEST_TV",
        price: "0.01", 
        partner_notify_url: ""
    },

    pay: function (options, successCallback, errorCallback) {
        options = this.merge(this.ENTER_OPTIONS, options);
        cordova.exec(successCallback, errorCallback, "letOrder", "Pay", [options]);
    },
	
    iandroid: function (mag, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "letOrder", "Iandroid", [mag]);
    },
    
    packageinfo: function (mag, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "letOrder", "Packageinfo", [mag]);
    },
    
    sign: function (mag, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "letOrder", "Sign", [mag]);
    },
    
    echo: function (mag, duration, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "letOrder", "Echo", [mag,duration]);
    },
    
    merge: function () {
        var obj = {};
        Array.prototype.slice.call(arguments).forEach(function(source) {
            for (var prop in source) {
                obj[prop] = source[prop];
            }
        });
        return obj;
    }
};
