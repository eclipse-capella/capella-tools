
exports.adaptGithub = function(tocs) {
    return new Promise((resolve, reject) => {
        let proceed = function(topic) {
            if (topic.href) {
                let href = `${topic.rooturl}${topic.plugin}/${topic.href}`;
                href = href.replace(".html", ".mediawiki");
                href = href.replace(/raw.githubusercontent.com\/([^\/]+)\/([^\/]+)\/([^\/]+)\//, "github.com/$1/$2/blob/$3/");
                href = href.replace(/ /g,"%20");
                topic.href = href;
            }
            if (topic.childs) {
                topic.childs.forEach(t => proceed(t));
            }
        };
        tocs.forEach(t => proceed(t, ""));
        resolve(tocs);
    });
};

exports.adaptCapella = function(tocs) {
    return new Promise((resolve, reject) => {
        let proceed = function(topic) {
            if (topic.topic) {
                //Remove numerotation on titles, N.(M.)? Title
                topic.topic = topic.topic.replace(/^(\d+\.([\d+]\.)?)(.*)/,"$3").trim();
            }
            //Merge topic and its only one child if they have same name
            if (topic.childs && topic.childs.length == 1) {
                if (topic.topic.toLowerCase() == topic.childs[0].topic.toLowerCase()) {
                    topic.href = topic.childs[0].href;
                    topic.plugin = topic.childs[0].plugin;
                    topic.childs = topic.childs[0].childs;
                    proceed(topic);
                    return;
                }
            }
            //Remove Raw API childs
            if (topic.topic.startsWith("Raw API")) {
                topic.childs = [];
            }
            //Remove sub childs on childs of Glossary and Diagrams
            if (topic.topic.startsWith("Glossary") || topic.topic.startsWith("Diagrams")) {
                if (topic.childs) {
                    topic.childs.forEach(t => {
                        t.childs = [];
                    });
                }   
            }
            //Put User Interface section at the top in User Manual
            if (topic.topic == "User Manual") {
                topic.childs.sort((a, b) => {
                    if (a.topic == "User Interface") {
                        return -10;
                    }
                    if (b.topic == "User Interface") {
                        return 10;
                    }
                    return a.topic.localeCompare(b.topic);
                });
            }
            if (topic.childs) {
                topic.childs.forEach(t => proceed(t));
            }
        };
        tocs.forEach(t => proceed(t, ""));
        resolve(tocs);
    });
};

exports.adaptKitalpha = function(tocs) {
    return new Promise((resolve, reject) => {
        let proceed = function(topic) {
            
            //Remove underconstruction topics
            if (topic.childs) {
                topic.childs = topic.childs.filter(t => t.href == undefined || t.href.indexOf("underconstruction")==-1);
            }
            if (topic.childs) {
                topic.childs.forEach(t => proceed(t));
            }
        };
        tocs.forEach(t => proceed(t, ""));
        resolve(tocs);
    });
};
