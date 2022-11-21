var assert = require('assert');
var extract = require('../index');

let describe = function(msg) { console.log( `\n\x1b[36m${msg}\x1b[0m`) ; }
let ok = function(msg) { console.log( `\x1b[32m${msg}\x1b[0m`); }
let ko = function(msg) { return `\x1b[31m${msg}\x1b[0m`; }

let shallRaiseException = function(msg) { 
    assert(true, ko("error shall be raised"));
    ok("ok, exception raised");
}
let shallNotRaiseException = function(msg) { 
    assert(false, ko("error shall not be raised"));
    ok("ok, exception not raised");
}

extract.getFileOrNull("https://raw.githubusercontent.com/eclipse/capella/master/doc/plugins/org.polarsys.capella.doc/to2c.xml").then(e => {
    describe("getFileOrNull:invalid");
    assert(e == null, ko("value shall be null"));
    ok("ok");
}).catch(e => {
    describe("getFileOrNull:invalid");
});

extract.getFileOrNull("https://raw.githubusercontent.com/eclipse/capella/master/doc/plugins/org.polarsys.capella.doc/toc.xml").then(e => {
    describe("getFileOrNull:valid");
    assert(e != null, ko("value shall not be null"));
    ok("ok");
}).catch(e => {
    describe("getFileOrNull:valid");
    shallNotRaiseException();
});


extract.parseString(null).then(e => {
    describe("parseString:null case");
    assert(e != null && Object.keys(e).length == 0, ko("value shall an empty object"));
    ok("ok");
}).catch(e => {
    describe("parseString:null case");
    shallNotRaiseException();
});

extract.parseString("eee").then(e => {
    describe("parseString:invalid");
    ok("ok");
}).catch(e => {
    describe("parseString:invalid");
    shallRaiseException();
});

extract.parseString("<toto>tt</toto>").then(e => {
    describe("parseString:valid");
    ok("ok");
}).catch(e => {
    describe("parseString:valid");
    shallNotRaiseException();
});


extract.getRawPlugins(["https://raw.githubusercontent.com/eclipse/capella/master/doc/plugins/org.polarsys.capella.doc/plssugin.xml"]).then(e => {
    describe("getPlugins:invalid");
    console.log(e);
    assert(e.length == 1);
    assert(e[0].plugin == "org.polarsys.capella.doc");
    assert(e[0].content != null && Object.keys(e[0].content).length == 0, ko("content shall an empty object"));
    ok("ok");
}).catch(e => {
    describe("getPlugins:invalid");
    shallNotRaiseException();
});

extract.getRawPlugins(["https://raw.githubusercontent.com/eclipse/capella/master/doc/plugins/org.polarsys.capella.doc/plugin.xml"]).then(e => {
    describe("getPlugins:valid");
    console.log(e);
    assert(e.length == 1);
    assert(e[0].plugin == "org.polarsys.capella.doc");
    assert(e[0].content != null && Object.keys(e[0].content).length > 0, ko("content shall be not empty"));
    ok("ok");
}).catch(e => {
    describe("getPlugins:invalid");
    shallNotRaiseException();
});


extract.getTocsUrls(["https://raw.githubusercontent.com/eclipse/capella/master/doc/plugins/org.polarsys.capella.doc/plugin.xml"]).then(e => {
    describe("getTocsUrls:valid");
    console.log(e);
    assert(e.length == 3);
    assert(e[0].file == "toc.xml");
    assert(e[0].plugin == "org.polarsys.capella.doc");
    ok("ok");
}).catch(e => {
    describe("getTocsUrls:invalid");
    shallNotRaiseException();
});

extract.getTocsUrls(["https://raw.githubusercontent.com/eclipse/capella/master/doc/plugins/org.polarsys.capella.doc/pludddgin.xml"]).then(e => {
    describe("getTocsUrls:invalid");
    console.log(e);
    assert(e.length == 0);
    ok("ok");
}).catch(e => {
    console.log(e);
    describe("getTocsUrls:invalid");
    shallNotRaiseException();
});



extract.getRawTocs(["https://raw.githubusercontent.com/eclipse/capella/master/doc/plugins/org.polarsys.capella.doc/toc.xml"]).then(e => {
    describe("getRawTocs:valid");
    console.log(e);
    assert(e[0].filename == "toc.xml");
    assert(e[0].plugin == "org.polarsys.capella.doc");
    assert(e[0].content != null && Object.keys(e[0].content).length > 0, ko("content shall be not empty"));
    ok("ok");
}).catch(e => {
    console.log(e);
    describe("getTocsUrls:invalid");
    shallNotRaiseException();
});

extract.getRawTocs(["https://raw.githubusercontent.com/eclipse/capella/master/doc/plugins/org.polarsys.capella.doc/toddc.xml"]).then(e => {
    describe("getRawTocs:invalid");
    console.log(e);
    assert(e.length == 1);
    assert(e[0].plugin == "org.polarsys.capella.doc");
    assert(e[0].content != null && Object.keys(e[0].content).length == 0, ko("content shall an empty object"));
    ok("ok");
}).catch(e => {
    console.log(e);
    describe("getTocsUrls:invalid");
    shallNotRaiseException();
});



extract.getConsolidatedTocs(["https://raw.githubusercontent.com/eclipse/capella/master/doc/plugins/org.polarsys.capella.doc/plugin.xml"]).then(e => {
    describe("getConsolidatedTocs:invalid");

    console.log(JSON.stringify(e, null, " "));
});
