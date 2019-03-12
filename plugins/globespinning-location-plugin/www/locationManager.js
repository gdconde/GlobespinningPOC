var exec = cordova.require('cordova/exec');

var LocationManager = {
  getCurrentLocation: function(success, failure) {
    exec(success, failure, 'LocationManager', 'getCurrentLocation', []);
  }
}

module.exports = LocationManager;
