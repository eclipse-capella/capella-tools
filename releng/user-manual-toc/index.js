let xml2js = require("xml2js");
let httpquery = require("./httpquery");


exports.getFileOrNull = function (url) {
    //Retrieve given url or null if error.
    return new Promise((resolve, reject) => {
        httpquery.getFile(url).then(result => {
            resolve(result);
        }).catch(err => {
            console.log(`${url} not found.`);
            resolve(null);
        });
    });
};

exports.parseString = function (string) {
    //Parse given string as object or {} if null
    return new Promise((resolve, reject) => {
        if (string == null) {
            resolve({});
        } else {
            xml2js.parseString(string, function (err, result) {
                if (err) {
                    reject(err);
                } else {
                    resolve(result);
                }
            }).catch(err => console.error(err));
        }
    });
};

exports.getRawTocs = function(urls) {
    //from a toc.xml url, urls to xmls then from xmls to json
    return new Promise((resolve, reject) => {
        let asTocs = function(js) {
            let result = Array.from(Array(urls.length).keys()).map(x => {
                let plugin = urls[x].split("/").filter(x => x.startsWith("org."))[0];
                let filename = urls[x].split("/")[urls[x].split("/").length-1];
                return {"rooturl":urls[x].substring(0, urls[x].indexOf(plugin)), "plugin": plugin, "filename": filename, "content": js[x] }
            });
            resolve(result);
        };
        
        Promise.all(urls.map(url => exports.getFileOrNull(url))).then(results => {
            Promise.all(results.map(r => exports.parseString(r))).then(js => asTocs(js)).catch(err => reject(err));
        }).catch(err => reject(err));
    });
}

exports.getRawPlugins = function(urls) {
    //from urls to xmls then from xmls to json
    return new Promise((resolve, reject) => {
        let asPlugins = function(js) {
            let plugins = Array.from(Array(urls.length).keys()).map(x => {
                let plugin = urls[x].split("/")[urls[x].split("/").length-2];
                return {"url": urls[x], "plugin": plugin, "content": js[x] }
            });
            resolve(plugins);
        };
        Promise.all(urls.map(url => exports.getFileOrNull(url))).then(results => {
            Promise.all(results.map(r => exports.parseString(r))).then(js => asPlugins(js)).catch(err => reject(err));
        }).catch(err => reject(err));
    });
}

exports.getTocsUrls = function(pluginsUrls) {
    return new Promise((resolve, reject) => {
        exports.getRawPlugins(pluginsUrls).then(plugins => {
            
            plugins = plugins.filter(p => p.content.plugin);

            //Retrieve all extensions points
            let tocs2 = function(plugin) {
                let result = plugin.content.plugin.extension.filter(e => e["$"].point == "org.eclipse.help.toc");
                result.forEach(element => {
                    element.plugin = plugin.plugin;
                    element.rootTocUrl = plugin.url.substring(0, plugin.url.length-"plugin.xml".length);
                });
                return result;
            };
        
            //Retrieve all tocs points
            let plug2 = function(point) {
                let result = point.toc;
                result.forEach(topic => {
                    topic.plugin = point.plugin;
                    topic.rootTocUrl = point.rootTocUrl;
                });
                return result;
            };
        
            if (plugins.length == 0) {
                resolve([]);
                return;
            }
            //Retrieve all extensions points, as a single array
            let result = plugins.map(p => tocs2(p)).reduce(function(pre, cur) {
                return pre.concat(cur);
            //Retrieve all toc points, as a single array
            }).map(point => plug2(point)).reduce(function(pre, cur) {
                return pre.concat(cur);
            //Retrieve all toc, as a single array of readable tocs
            }).map(toc => {
                if (toc["$"]) {
                    let url = toc.rootTocUrl+toc["$"].file;
                    url = url.replace(/ /g,"%20");
                    let file = toc["$"].file;
                    file = file.replace(/ /g,"%20");
                    return { url: url, plugin: toc.plugin, file: file };
                }
                return null;
            });
            result = result.filter(r => (r != null));
            resolve(result);
        });
    });
};

exports.getConsolidatedTocs = function(pluginsUrls) {

    return new Promise((resolve, reject) => {
        exports.getTocsUrls(pluginsUrls).then(tocs => {
            tocs = tocs.map(toc => toc.url);

            exports.getRawTocs(tocs).then(result => {
                    
                    result = result.filter(x => x.content.toc);
            
                    let anchors = [];
                    let topics = [];
                
                    let proceedTopics = function(root, topic, parent) {
                        let currentTopic = { topic:topic["$"].label, plugin:root.plugin, rooturl:root.rooturl, href: topic["$"].href };
                
                        if (topic["$"].link_to) {
                            let plugin = topic["$"].link_to.split("/")[1];
                            let fragment = topic["$"].link_to.split("/")[2];
                            let filename = fragment.split("#")[0];
                            let anchor = fragment.split("#")[1];
                            currentTopic.link_to = {plugin:plugin, filename:filename, anchor:anchor};
                        }
                        if (topic.anchor && topic.anchor.length > 0) {
                            topic.anchor.forEach(a => anchors.push({ plugin:root.plugin, filename:root.filename, anchor:a["$"].id, topic:currentTopic}));
                        }
                        if (parent) {
                            if (!parent.childs) parent.childs = [];
                            parent.childs.push(currentTopic);
                        } else {
                            topics.push(currentTopic);
                        }
                        if (topic.topic && topic.topic.length > 0) {
                            topic.topic.forEach(x => proceedTopics(root, x, currentTopic));
                        }
                    }
                
                    let attachTopic = function(topic) {
                        if (topic.link_to) {
                            let anchor = anchors.find(a => a.plugin == topic.link_to.plugin && a.filename == topic.link_to.filename && a.anchor == topic.link_to.anchor);
                            if (!anchor) {
                                console.error("Missing anchor: ");
                                console.error(topic.link_to);
                            } else {
                                if (anchor.topic) {
                                    if (!anchor.topic.childs) anchor.topic.childs = [];
                                    anchor.topic.childs.push(topic);
                                    topics = topics.filter(item => item !== topic);
                                }
                            }
                        }
                    }
                
                    result.filter(r => r.content).forEach(r => proceedTopics(r, r.content.toc, null));
                    topics.forEach(x => attachTopic(x));
                    resolve({ topics: topics, anchors: anchors });
                    //console.log(JSON.stringify(e, null, " "));
        
            });
        });

    });
};

exports.toMarkdown = function(json) {
    return new Promise((resolve, reject) => {
        let result = "";
        let proceed = function(topic, padding) {
            let href = topic.href ? topic.href.replace(/ /g,"%20") : null;
            let text = href != null ? `[${topic.topic}](${href})` : topic.topic;
            result += `${padding}* ${text}\n`;
            if (topic.childs) {
                topic.childs.forEach(t => proceed(t, padding+"  "));
            }
        };
        json.forEach(t => proceed(t, ""));
        resolve(result);
    });
};
