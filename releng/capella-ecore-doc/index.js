const https = require('https')
var fs = require("fs");
var path = require("path");
const { getRandomValues } = require('crypto');

var fsh = {
	
    write: function (filename, data) {
        return new Promise(function(resolve, reject) {
            if (!(typeof data === 'string' || data instanceof String)) {
                data = JSON.stringify(data, null, " ");
            }
            if (!fs.existsSync(path.dirname(filename))){
                fs.mkdirSync(path.dirname(filename), { recursive: true });
            }
            fs.writeFile(filename, data, 'UTF-8', function(err) {
                if (err) {
                    reject(err);
                } else {
                    resolve(data);
                }
            });
        });
    },
    
    writeIfChange: function (filename, data) {
        if (!(typeof data === 'string' || data instanceof String)) {
            data = JSON.stringify(data, null, " ");
        }
        if (!fs.existsSync(path.dirname(filename))){
            fs.mkdirSync(path.dirname(filename), { recursive: true });
        }
        if (fsh.fileExists(filename)) {
            return fsh.read(filename).then(e => {
                if (e != data) {
                    return fsh.write(filename, data);
                } else {
                    return Promise.resolve(filename);
                }
            })
        } else {
            return fsh.write(filename, data);
        }
    },
	
	read: function(filename) {
		return new Promise(function(resolve, reject) {
			fs.readFile(filename, 'UTF-8', function(err, data){
				if (err) reject(err); 
				else resolve(data);
			});
		});
	},
	
    fileExists: function(filename) {
        try {
            return fs.statSync(filename).isFile();

        } catch (err) {
            if (err.code == 'ENOENT') {
                console.log("File does not exist.");
                return false;
            }
            return false;
        }
    }
};

var httph = {
	
	downloadFile : function(host, path, outputFile) {
		return new Promise((resolve, reject) => {
			httph.get(host, path).then(e => {
				fsh.writeIfChange(outputFile, e);
				resolve(e);
			});
		});
	},

	request: function(host, path, object, method, user, password) {
		return new Promise((resolve, reject) => {
			
			var data = undefined; 
			if (object != undefined) {
				data = JSON.stringify(object);
			}
			var options = {
				host: host,
				port: 443,
				path: path,
				method: method,
				headers: { }
			};
			if (data != undefined) {
				options.headers['Content-Type'] = 'application/json';
				options.headers['Content-Length'] = Buffer.byteLength(data);
			}
			if (user) {
				options.headers["User-Agent"] = user;
			}
			if (password) {
				options.headers["Authorization"] = "Basic "+Buffer.from(user+":"+password).toString("base64");
			}
			
			var req = https.request(options, function(res) {
			    let body = '';
			    res.on('data', function(chunk) {
			    	body += chunk;
			    });
			    res.on('end', function() {
					result = body;
					if (result != undefined && result.message != undefined && result.message.includes("Bad credentials")) {
						reject(result);
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
	},
	
	get: function(host, path) {
		return httph.request(host, path, undefined, "GET");
	}
};

var cacheh = {
  
    fetchFile: function (host, url, cache) {
        if (!fsh.fileExists(cache)) {
            return httph.downloadFile(host, url, cache);
        } else {
			return fsh.read(cache);
		}
    }
}

var promiseh = {
  
    //From an array of values and a function returning a promise from a value
    //Execute promises sequentially (Promise.all doesn't run sequentially)
    consecutive: function(values, fPromise) {
        return values.reduce((p, value) => {
            return p.then(() => {
                return fPromise(value);
            }).catch(error => {
                console.log(error);
            });
        }, Promise.resolve());
    }

  };
var capellah = {
	nsUris: {
		"http://www.polarsys.org/capella/core/core/6.0.0": 		"/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.gen/model/CapellaCore.ecore",
		"http://www.polarsys.org/capella/core/common/6.0.0": 	"/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.gen/model/CapellaCommon.ecore",
		"http://www.polarsys.org/capella/core/modeller/6.0.0":  "/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.gen/model/CapellaModeller.ecore",	
		"http://www.polarsys.org/capella/core/cs/6.0.0":  		"/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.gen/model/CompositeStructure.ecore",	
		"http://www.polarsys.org/capella/core/ctx/6.0.0": 		"/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.gen/model/ContextArchitecture.ecore",	
		"http://www.polarsys.org/capella/core/epbs/6.0.0": 		"/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.gen/model/EPBSArchitecture.ecore",	
		"http://www.polarsys.org/capella/core/fa/6.0.0": 		"/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.gen/model/FunctionalAnalysis.ecore",	
		"http://www.polarsys.org/capella/core/information/6.0.0": 		"/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.gen/model/Information.ecore",	
		"http://www.polarsys.org/capella/core/interaction/6.0.0": 		"/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.gen/model/Interaction.ecore",	
		"http://www.polarsys.org/capella/core/la/6.0.0": 		"/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.gen/model/LogicalArchitecture.ecore",	
		"http://www.polarsys.org/capella/core/oa/6.0.0": 		"/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.gen/model/OperationalAnalysis.ecore",	
		"http://www.polarsys.org/capella/core/pa/6.0.0": 		"/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.gen/model/PhysicalArchitecture.ecore",	
		"http://www.polarsys.org/capella/core/requirement/6.0.0": 		"/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.gen/model/Requirement.ecore",	
		"http://www.polarsys.org/capella/core/sharedmodel/6.0.0": 		"/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.gen/model/SharedModel.ecore",	
		"http://www.polarsys.org/capella/common/re/6.0.0": 		"/eclipse/capella/master/common/plugins/org.polarsys.capella.common.re.gen/model/re.ecore",	
		"http://www.polarsys.org/capella/common/libraries/6.0.0": 		"/eclipse/capella/master/common/plugins/org.polarsys.capella.common.libraries.gen/model/libraries.ecore",	
		"http://www.polarsys.org/capella/common/activity/6.0.0": 		"/eclipse/capella/master/common/plugins/org.polarsys.capella.common.data.activity.gen/model/Activity.ecore",	
		"http://www.polarsys.org/capella/common/behavior/6.0.0": 		"/eclipse/capella/master/common/plugins/org.polarsys.capella.common.data.behavior.gen/model/Behavior.ecore",	
		"http://www.polarsys.org/capella/common/core/6.0.0": 		"/eclipse/capella/master/common/plugins/org.polarsys.capella.common.data.core.gen/model/ModellingCore.ecore",	
		"ecore": "/kchobantonov/org.eclipse.emf/blob/master/plugins/org.eclipse.emf.ecore/model/Ecore.ecore"

	}

}

function getAllPackages() {
	return Promise.all(Object.keys(capellah.nsUris).map(k => { 
		return { k:k, host: "raw.githubusercontent.com", url: capellah.nsUris[k], file: capellah.nsUris[k].substring(capellah.nsUris[k].lastIndexOf("/")+1)}; 
	}).map(f => {
		return cacheh.fetchFile(f.host, f.url, ".cache/"+f.file).then(ee => {
			f.value = ee;
			return Promise.resolve(f);
		});
	})).then(e => {
		var convert = require('xml-js');
		let jsons = e.map(p => JSON.parse(convert.xml2json(p.value, {compact: true, ignoreComment: true, spaces: 0, arrayNotation: true})));
		return Promise.resolve(jsons);

	}).then(jsons => {
		let pkgs = jsons.map(j => j["ecore:EPackage"]);
		let subpkgs = jsons.map(j => j["ecore:EPackage"].eSubpackages).filter(p => p != null);
		let allpkgs = [...pkgs, ...subpkgs.flat()];
		return allpkgs;
	});
}


function findAnnotation(element, type) {
	if (element && Array.isArray(element.eAnnotations)) {
		let result = element.eAnnotations.filter(a => a._attributes["source"] == type);
		if (result && result[0]) {
			return result[0];
		}
	}
	return null;
}

function findDetails(element, type) {
	if (element && Array.isArray(element.details)) {
		let result = element.details.filter(a => a._attributes["key"] == type);
		if (result && result[0]) {
			return result[0]._attributes.value;
		}
	}
	return null;
}

function getClasses(pkgs) {
	let result = {};
	pkgs.forEach(p => {
		p.eClassifiers.forEach(c => {
			c.packageName = p._attributes.name;
			if (c.eStructuralFeatures && !Array.isArray(c.eStructuralFeatures)) {
				c.eStructuralFeatures = [c.eStructuralFeatures];
			}
			result[c._attributes.name] = c;
		});
	});
	
	Object.keys(result).forEach(c => {
		result[c].eAllSuperTypes = getSuperTypes(result[c], result);
	});
	return result;
}

function getSubLeafClasses(cs, classes) {
	return Object.keys(classes).filter(c => {
		return classes[c].eAllSuperTypes.map(e => e._attributes.name).includes(cs._attributes.name);
	}).map(c => classes[c]).filter(a => !a._attributes.abstract);
}


function getIconFromClass(c) {
	if (c._attributes['xsi:type'] == 'ecore:EEnum') {
		return "![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EEnum.gif)";
		
	} else if (c._attributes['xsi:type'] == 'ecore:EDataType') {
		return "![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EDataType.gif)";
		
	} else if (c._attributes.abstract) {
		return "![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif)";
	
	} else {
		return "![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/"+c._attributes.name+".gif)";
	}
}

function getTypeName(s) {

	if (s._attributes.eType) {
		let type = s._attributes.eType.substring(s._attributes.eType.lastIndexOf("/")+1);
		return type;	
	}

	return null;
}

function getTypeFromClass(c, classes, icon=true, link=false) {
	let typeResult = "";
	if (c._attributes.name) {
		let type = c._attributes.name;
		
		if ( classes[type] != null) {
			let ccc = classes[type];
			if (ccc._attributes.name != null && icon) {
				typeResult += ""+getIconFromClass(ccc) + " ";
			}
			if (link) {
				typeResult += "["+ccc._attributes.name+"](https://github.com/eclipse/capella/wiki/Metamodel-"+ccc.packageName+"#-"+ccc._attributes.name.toLowerCase()+")";
			} else {
				typeResult += ""+ccc._attributes.name;
			}
		}
	}
	return typeResult;
}

function getType(feature, classes, icon=true, link=false) {
	let typeResult = "";
	if (feature._attributes.eType) {
		let type = getTypeName(feature);
		
		if ( classes[type] != null) {
			let ccc = classes[type];
			if (ccc._attributes.name != null && icon) {
				typeResult += ""+getIconFromClass(ccc) + " ";
			}
			if (link) {
				typeResult += "["+ccc._attributes.name+"](https://github.com/eclipse/capella/wiki/Metamodel-"+ccc.packageName+"#-"+ccc._attributes.name.toLowerCase()+")";
			} else {
				typeResult += ""+ccc._attributes.name;
			}
		}
	}
	return typeResult;
}

function isDerived(s) {
	let annotation = findAnnotation(s, "http://www.polarsys.org/capella/derived");
	return annotation != null;
}
function getValue(s, classes) {
	let annotation = findAnnotation(s, "http://www.polarsys.org/capella/derived");
	if (annotation) {
		let details = findDetails(annotation, 'viatra.variant');
		let expression = findDetails(annotation, 'viatra.expression');
		let expr = "";
		if (details == "opposite") {
			expr = "opposite("+getType(s, classes, false)+"."+expression+")";
		} else if (details == "alias") {
			expr = expression+".filter("+getType(s, classes, false)+")";
		} else if (details == "patternbody") {
			expr = expression.replace(/\r?\n/g, " ");
		} else if (details == "freeform") {
			expr = expression.replace(/\r?\n/g, " ");
		}
		return "[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.debug.ui/icons/full/obj16/read_obj.png)](#"+s._attributes.name.toLowerCase()+" \""+expr+"\")";
		
	}
	return "";
}
function getSuperTypes(c, classes) {
	let result = [];
	if (c._attributes.eSuperTypes) {
		let types = c._attributes.eSuperTypes.split(" ").map(t => t.substring(t.lastIndexOf("/")+1));
		result = types.map(t => classes[t] != null ? [classes[t], ...getSuperTypes(classes[t], classes)]: []);
	}
	return [...new Set(result.flat())];
}

function getCard(s) {
	let result = "";
	if (s._attributes.lowerBound) {
		result += s._attributes.lowerBound;
	} else {
		result += "0";
	}
	result += "..";
	if (s._attributes.upperBound == "-1")  {
		result += "*";
	} else if (s._attributes.upperBound)  {
		result += s._attributes.upperBound;
	} else {
		result += "1";
	}
	return result;
}

function getStyledNameFeature(s) {
	if (isDerived(s)) {
		return "*"+s._attributes.name+"*";
	}
	return s._attributes.name;
}

function trimDoc(s) {
	return s.replace(/\[source: ?[a-zA-Z ]+\]/, "").trim();
}

function isDeprecatedClass(c) {
	let dp = ["ExchangeLink", "FunctionSpecification", "ExchangeSpecificationRealization", "ExchangeSpecification", 
	"NamingRule", "ExchangeContainment", "FunctionalExchangeSpecification", "BlockArchitecturePkg",
	 "ProvidedInterfaceLink", "RequiredInterfaceLink", "LogicalArchitecturePkg", "SystemAnalysisPkg", 
	"ReuseLink", "SystemEngineeringPkg", "Folder", "ModellingArchitecturePkg", "Gate", "DeploymentAspect", "DeploymentConfiguration", 
	"PhysicalArchitecturePkg", "PhysicalNode", "CapabilityConfiguration", "Location", "ItemInConcept", "Concept", "AbstractConceptItem", 
	"ConceptPkg", "ConceptCompliance", "CommunityOfInterest", "CommunityOfInterestComposition", "OrganisationalUnit", "OrganisationalUnitComposition", 
	"InstanceDeploymentLink", "PortInstance", "ConnectionInstance", "ComponentInstance", "AbstractPhysicalInstance", "Swimlane", "RoleAssemblyUsage"];
	return c != null && c._attributes && dp.includes(c._attributes.name);
}  

function getDoc(p) {
	let doc = findAnnotation(p, 'http://www.polarsys.org/kitalpha/ecore/documentation', "source");
	if (doc) {
		let doc2 = findDetails(doc, 'description', "key");
		if (doc2) {
			return trimDoc(doc2);
		}
	} 
	return "";
}

function getLineDoc(p) {
	let doc = trimDoc(getDoc(p)).replace(/\r?\n/g, "").trim();
	if (doc.length > 0) {
		return "[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#"+p._attributes.name.toLowerCase()+" \""+doc+"\")";
	}
	return "";
}

getAllPackages().then(pkgs => {
	let classes = getClasses(pkgs);

	pkgs.forEach(p => {
		let file = "../../../capella.wiki/apidocs/m2/Metamodel-"+p._attributes.name + ".md";
		let fileContent = "";
		fileContent += "# ![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.pde.runtime/icons/obj16/package_obj@2x.png) "+p._attributes.name;
		fileContent += "\n";
		fileContent += getDoc(p);
		fileContent += "\n";
		fileContent += "\n";
		
		
		p.eClassifiers.filter(c => !isDeprecatedClass(c)).forEach(c => {
			
			fileContent += "## "+getIconFromClass(c);
			if (c._attributes.abstract) {
				fileContent += " *"+c._attributes.name+"*";
			} else {
				fileContent += " "+c._attributes.name;
			}
			fileContent += "\n";
			fileContent += "\n";
			let doc3 = findAnnotation(c, 'http://www.polarsys.org/kitalpha/ecore/documentation', "source");
			if (doc3) {
				let doc4 = findDetails(doc3, 'description', "key");
				if (doc4) {
					fileContent += trimDoc(doc4);
					fileContent += "\n";
				}
			}
			fileContent += "\n";
			

			let dp = ["CapellaElement", "ModelElement", "AbstractNamedElement", "NamedElement", "PublishableElement", "Namespace", "TraceableElement"];
			let commonTypes = [...getSuperTypes(c, classes)].filter(c => dp.includes(c._attributes.name));
			let types = [...getSuperTypes(c, classes)].filter(c => !dp.includes(c._attributes.name));
			fileContent += "Common Types: "+commonTypes.map(e => getTypeFromClass(e, classes, true, true)) ;
			fileContent += "\n";
			fileContent += "\n";
		
			if (!dp.includes(c._attributes.name)) {
				let subTypes = getSubLeafClasses(c, classes).filter(a => !isDeprecatedClass(a)).map(e => getTypeFromClass(e, classes, true, true));
				if (subTypes.length > 0) {
					fileContent += "Known Sub Types: "+subTypes.join(", ");
				}
			} else {
				fileContent += "Known Sub Types: *Too many elements to be displayed here*";
			}

			fileContent += "\n";

			let literals = [c].map(e => Array.isArray(e.eLiterals) ? e.eLiterals: []).flat().sort((e, e2) => e._attributes.name.localeCompare(e2._attributes.name));
			if (literals.length > 0) {
				fileContent += "|Attribute|Description|\n";
				fileContent += "|--|--|\n";
				literals.forEach(s => {
					fileContent += "|"+getStyledNameFeature(s)+"|"+getLineDoc(s)+"\n";
				});
				fileContent += "\n";
			}

			let features = [c].map(e => Array.isArray(e.eStructuralFeatures) ? e.eStructuralFeatures: []).flat().sort((e, e2) => e._attributes.name.localeCompare(e2._attributes.name));

			let attributes = features.filter(a => a._attributes["xsi:type"] == 'ecore:EAttribute').filter(a => !isDeprecatedClass(classes[getTypeName(a)]));
			let references = features.filter(a => a._attributes["xsi:type"] == 'ecore:EReference').filter(a => !isDeprecatedClass(classes[getTypeName(a)]));
			if (features.length > 0) {
				fileContent += "\n";
				fileContent += "**Specific fields**\n";
			}
			if (attributes.length > 0) {
				fileContent += "|Attribute|Type|Range|Description|\n";
				fileContent += "|--|--|--|--|\n";
				attributes.forEach(s => {
					fileContent += "|"+getStyledNameFeature(s)+"|"+getType(s, classes, true, true)+"|"+getCard(s)+"|"+getLineDoc(s)+"\n";
				});
				fileContent += "\n";
			}
			fileContent += "\n";
			if (references.length > 0) {
				fileContent += "|Reference|Type|Range|Description|\n";
				fileContent += "|--|--|--|--|\n";
				references.forEach(s => {
					fileContent += "|"+getStyledNameFeature(s)+"|"+getType(s, classes, true, true)+"|"+getCard(s)+"|"+[getLineDoc(s), getValue(s, classes)].join(" ")+"|"+"\n";
				});
				fileContent += "\n";
			}


			features = types.map(e => Array.isArray(e.eStructuralFeatures) ? e.eStructuralFeatures: []).flat().sort((e, e2) => e._attributes.name.localeCompare(e2._attributes.name));
			attributes = features.filter(a => a._attributes["xsi:type"] == 'ecore:EAttribute').filter(a => !isDeprecatedClass(classes[getTypeName(a)]));
			references = features.filter(a => a._attributes["xsi:type"] == 'ecore:EReference').filter(a => !isDeprecatedClass(classes[getTypeName(a)]));
			if (features.length > 0) {
				fileContent += "\n";
				fileContent += "**Inherited fields** *(from "+types.map(e => getTypeFromClass(e, classes, true, true))+")*\n" ;
			}

			if (attributes.length > 0) {
				fileContent += "|Attribute|Type|Range|Description|\n";
				fileContent += "|--|--|--|--|\n";
				attributes.forEach(s => {
					fileContent += "|"+getStyledNameFeature(s)+"|"+getType(s, classes, true, true)+"|"+getCard(s)+"|"+getLineDoc(s)+"|"+"\n";
				});
				fileContent += "\n";
			}
			fileContent += "\n";
			if (references.length > 0) {
				fileContent += "|Reference|Type|Range|Description|\n";
				fileContent += "|--|--|--|--|\n";
				references.forEach(s => {
					fileContent += "|"+getStyledNameFeature(s)+"|"+getType(s, classes, true, true)+"|"+getCard(s)+"|"+[getLineDoc(s), getValue(s, classes)].join(" ")+"|"+"\n";
				});
				fileContent += "\n";
			}

			fileContent += "---";
			fileContent += "\n";

			});
				
		fileContent += "\n";

		fsh.writeIfChange(file, fileContent);
	});
});
