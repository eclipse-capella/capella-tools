let extract = require("./index");
let fsquery = require("./fsquery");
let adapter = require("./adapter");

let plugins = [
    "org.polarsys.capella.commandline.doc", 
    "org.polarsys.capella.common.ui.massactions.doc", 
    "org.polarsys.capella.core.re.updateconnections.doc", 
    "org.polarsys.capella.core.ui.intro", 
    "org.polarsys.capella.developer.doc", 
    "org.polarsys.capella.diagrams.doc", 
    "org.polarsys.capella.diffmerge.doc", 
    "org.polarsys.capella.doc", 
    "org.polarsys.capella.git.doc", 
    "org.polarsys.capella.glossary.doc", 
    "org.polarsys.capella.mylyn.doc", 
    "org.polarsys.capella.preferences.doc", 
    "org.polarsys.capella.properties.doc", 
    "org.polarsys.capella.re.doc", 
    "org.polarsys.capella.th.doc", 
    "org.polarsys.capella.tipsandtricks.doc", 
    "org.polarsys.capella.transitions.doc", 
    "org.polarsys.capella.ui.doc", 
    "org.polarsys.capella.validation.doc", 
    "org.polarsys.capella.viewpoint.doc"
];

let pluginsUrls = plugins.map(p => `https://raw.githubusercontent.com/eclipse/capella/master/doc/plugins/${p}/plugin.xml`);

extract.getConsolidatedTocs(pluginsUrls).then(e => {
    
    adapter.adaptGithub(e.topics).then(e => {

        adapter.adaptCapella(e).then(e2 => {
            fsquery.write("capella.json", JSON.stringify(e2, null, " "));

            extract.toMarkdown(e2).then(e3 => {
                fsquery.write("capella.md", e3);
            })
        });

    });

});

let kit = [
    "https://raw.githubusercontent.com/eclipse/kitalpha/master/architecture%20description/doc/plugins/org.polarsys.kitalpha.ad.doc/plugin.xml",
    "https://raw.githubusercontent.com/eclipse/kitalpha/master/doc/plugins/org.polarsys.kitalpha.doc/plugin.xml",
    "https://raw.githubusercontent.com/eclipse/kitalpha/master/emde/doc/plugins/org.polarsys.kitalpha.emde.doc/plugin.xml",
    "https://raw.githubusercontent.com/eclipse/kitalpha/master/massactions/plugins/org.polarsys.kitalpha.massactions.doc/plugin.xml",
    "https://raw.githubusercontent.com/eclipse/kitalpha/master/Docgen/business/plugins/org.polarsys.kitalpha.doc.gen.business.core.doc/plugin.xml",
    "https://raw.githubusercontent.com/eclipse/kitalpha/master/richtext/doc/plugins/org.polarsys.kitalpha.richtext.widget.doc/plugin.xml",
]

extract.getConsolidatedTocs(kit).then(e => {
    
    adapter.adaptGithub(e.topics).then(e => {

       adapter.adaptKitalpha(e).then(e2 => {
            fsquery.write("kitalpha.json", JSON.stringify(e2, null, " "));

            extract.toMarkdown(e2).then(e3 => {
                fsquery.write("kitalpha.md", e3);
            })
       });

    });

});
