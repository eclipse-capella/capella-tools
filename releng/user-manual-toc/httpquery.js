let https = require("https");

let httpquery = {
	
    getFile: function(file) {
        if (typeof file === 'string') {
            let url = file;
            file = { };
            if (url.startsWith("http://")) {
                url = url.substring("http://".length);
            }
            if (url.startsWith("https://")) {
                url = url.substring("https://".length);
            }
            file.host = url.split("/")[0];
            file.path = url.substring(file.host.length);
            file.type = url.split(".")[url.split(".").length -1];
        }
        let type = file.type ? file.type : "json";
        return httpquery.request(file.host, file.path, type);
    },
    
    get: function(host, path) {
        return httpquery.request(host, path, "json");
    },
    
    request: function(host, path, type, object) {
        
        return new Promise((resolve, reject) => {
            var data = undefined; 
            if (object != undefined) {
                data = JSON.stringify(object); 
            }
            var options = {
                host: host,
                port: 443,
                path: path,
                method: (data == undefined ? 'GET': 'POST'),
                headers: { }
            };
            if (data != undefined) {
                options.headers['Content-Type'] = 'application/x-www-form-urlencoded';
                options.headers['Content-Length'] = Buffer.byteLength(data);
            }
            
            var req = https.request(options, function(res) {
                let body = '';
                res.on('data', function(chunk) {
                    body += chunk;
                });
                res.on('end', function() {
                    if (type == "json") {
                        result = JSON.parse(body);
                    } else {
                        result = body;
                    }
                    if (res.statusCode == 404) {
                        reject(res.statusCode);
                    } else {
                        resolve(result);
                    }
                });
                
            }).on('error', function(e) {
                console.log("Got error: " + e.message);
                reject(e);
            });

            if (data != undefined) {
                req.write(data);
            }
            req.end();
        });
    }
};

module.exports = httpquery;