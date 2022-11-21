
var fs = require("fs");
let fsquery = {
    write: function (filename, data) {
        return new Promise(function(resolve, reject) {
            fs.writeFile(filename, data, 'UTF-8', function(err) {
                if (err) reject(err);
                else resolve(data);
            });
        });
    }
};

module.exports = fsquery;
