/*******************************************************************************
 * Copyright (c) 2017, 2025 THALES GLOBAL SERVICES.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/

var parse = require('parse-diff');
var http = require('http');
var fs = require('fs');

exports.loadFile = function(file, cb) {
  var data = '';

  var gitDiffChunks = [];
  const CHUNK_LENGTH = 10**7;

  var readStream = fs.createReadStream(file);
  readStream.setEncoding('UTF8');
  readStream.on('data', function (chunk) {
    if (data.length > CHUNK_LENGTH) {
      var lastIndx = data.lastIndexOf('diff --git ');
      gitDiffChunks.push(data.substring(0, lastIndx));
      data = data.substring(lastIndx);
    }
    data += chunk;
  });

  readStream.on('end', function () {
    gitDiffChunks.push(data);
    cb(gitDiffChunks);
  });

  readStream.on('error', function (err) {
    console.log(err);
    throw err;
  });
};


exports.loadUrl = function(url, cb) {
	var request = http.get(url, function(response) {
		var body = '';
		response.on('data', function(chunk) {
			body+=chunk;
		});
		response.on('end', function(chunk) {
			body+=chunk;
			cb(body);
		});
	});
	return;
};

//Add a regex indexOf on strings
String.prototype.regexIndexOf = function(regex, startpos) {
    var indexOf = this.substring(startpos || 0).search(regex);
    return (indexOf >= 0) ? (indexOf + (startpos || 0)) : indexOf;
}


exports.plugins = { };

//A method to trim string
exports.trim = function(res) {

      res = res.replace(/^-/, ""); //trim +/-
      res = res.replace(/^\+/, ""); //trim +/-
      res = res.replace(/^ */, ""); //trim left spaces
      res = res.replace(/\t/g, ""); //Remove \t
      res = res.replace(/\r/g, ""); //Remove \r
      res = res.replace(/\n/g, ""); //Remove \n
      
      res = res.replace(" {", ""); //remove {
      res = res.replace("{", ""); //remove {
      res = res.replace(";", ""); //remove ;
      //console.log(res);
      return res;
      
};

//From a given change, extract a method signature
exports.getMethodFromChange = function(changes, i) {
  var ki = parseInt(i);
  
  while (changes[ki] != undefined && changes[ki].content.indexOf("(")<0) {
      ki = ki+1;
  }
  if (changes[ki] != undefined) {
    var res = exports.trim(changes[ki].content);
    while (res.indexOf(")")<0 && changes[ki] != undefined) {
      res = res + exports.trim(changes[ki].content);
      ki = ki+1;
    }
    
    if (res.indexOf("(") >= 0 && res.indexOf(")")>=0) {
      return exports.trim(res);
    }
  }
  return null;
};

exports.proceedDeprecated = function(file, changes, i) {
   var ki = parseInt(i)+1;
   
   var signature = exports.getMethodFromChange(changes, i);
   
   if (signature != null) {
      var method = exports.getMethod(file, signature);
      method.deprecated = true;
   }
};

exports.proceedOverride = function(file, changes, i) {
   var ki = parseInt(i)+1;
   
   var signature = exports.getMethodFromChange(changes, i);
   if (signature != null) {
      var method = exports.getMethod(file, signature);
      method.override = true;
   }
};

exports.proceedAddedClass = function(file, changes, i) {
   var ki = parseInt(i);
   
   var clazz = exports.getFile(file, changes[ki].content);
   clazz.added = true;
};

exports.proceedRemovedClass = function(file, changes, i) {
   var ki = parseInt(i);
   var clazz = exports.getFile(file, changes[ki].content);
   clazz.removed = true;
};

exports.proceedAddedInterface = function(file, changes, i) {
   var ki = parseInt(i);
   var clazz = exports.getFile(file, changes[ki].content);
   clazz.added = true;
   clazz.interface = true;
};

exports.proceedRemovedInterface = function(file, changes, i) {
   var ki = parseInt(i);

   var clazz = exports.getFile(file, changes[ki].content);
   clazz.removed = true;
   clazz.interface = true;
};

exports.proceedAddedMethod = function(file, changes, i) {
   var ki = parseInt(i);
   var signature = exports.getMethodFromChange(changes, i);
   if (signature != null) {
     var method = exports.getMethod(file, signature);
     method.added = true;
   }
};

exports.proceedRemovedMethod = function(file, changes, i) {
   var ki = parseInt(i);
   var signature = exports.getMethodFromChange(changes, i);
   if (signature != null) {
      var method = exports.getMethod(file, signature);
      method.removed = true;
   }
};



//Process a file
exports.proceedFile = function(file) {
	if (file.from === undefined || file.to === undefined) {
		return;
	}
    if (file.from.endsWith("java") || file.to.endsWith("java")) {
      if (file.from.indexOf("capella.test")>0 || file.to.indexOf("capella.test")>0) {
        return;
      }
      if (file.from.indexOf("releng")>0 || file.to.indexOf("releng")>0) {
        return;
      }
      if (file.from.indexOf("egf")>0 || file.to.indexOf("egf")>0) {
        return;
      }
      if (file.from.indexOf("viatra")>0 || file.to.indexOf("viatra")>0) {
	    return;
      }
         
      if (file.chunks.length>0) {
        
        for (ch in file.chunks) {
          var interest = 0;
          
          for (i in file.chunks[ch].changes) {
           
		    //Classes
            if (file.chunks[ch].changes[i].type == "add" && file.chunks[ch].changes[i].content.regexIndexOf("public .*class")>=0) {
              interest = 1;
			  exports.proceedAddedClass(file, file.chunks[ch].changes, i);
            }
            if (file.chunks[ch].changes[i].type == "del" && file.chunks[ch].changes[i].content.regexIndexOf("public .*class")>=0) {
              interest = 1;
              exports.proceedRemovedClass(file, file.chunks[ch].changes, i);
            }
            
            //Interfaces
            if (file.chunks[ch].changes[i].type == "add" && file.chunks[ch].changes[i].content.regexIndexOf("public .*interface")>=0) {
              interest = 1;
              exports.proceedAddedInterface(file, file.chunks[ch].changes, i);
            }
            if (file.chunks[ch].changes[i].type == "del" && file.chunks[ch].changes[i].content.regexIndexOf("public .*interface")>=0) {
              interest = 1;
              exports.proceedRemovedInterface(file, file.chunks[ch].changes, i);
            }
            
            //Deprecated comment
            if (file.chunks[ch].changes[i].type == "add" && file.chunks[ch].changes[i].content.indexOf("Deprecated")>=0) {
              interest = 1;
              exports.proceedDeprecated(file, file.chunks[ch].changes, i);
            }
            
			//Override comment
            if (file.chunks[ch].changes[i].content.indexOf("Override")>=0) {
              interest = 1;
              exports.proceedOverride(file, file.chunks[ch].changes, i);
            }
            
			//Added or removed methods
            if (file.chunks[ch].changes[i].type == "add" && file.chunks[ch].changes[i].content.indexOf("public")>=0 && file.chunks[ch].changes[i].content.indexOf("(")>=0 && file.chunks[ch].changes[i].content.indexOf("=")==-1) {
              interest = 1;
              exports.proceedAddedMethod(file, file.chunks[ch].changes, i);
            }
            
            if (file.chunks[ch].changes[i].type == "del" && file.chunks[ch].changes[i].content.indexOf("public")>=0 && file.chunks[ch].changes[i].content.indexOf("(")>=0 && file.chunks[ch].changes[i].content.indexOf("=")==-1) {
              interest = 1;
              exports.proceedRemovedMethod(file, file.chunks[ch].changes, i);
            }
            
          }
          
        }
      }
    }
	
    //console.log(file.chunks.length); // number of hunks
    //console.log(file.chunks[0].changes.length) // hunk added/deleted/context lines
    //each item in changes is a string
    //console.log(file.deletions); // number of deletions in the patch
    //console.log(file.additions); // number of additions in the patch
};

//From a given file, retrieve the corresponding plugin (as we sort files per plugins)
exports.getPlugin = function(file) {
	var source = file.to;
	if (source == "/dev/null") {
		source = file.from;
	}
	var plugin =  source.split("src/")[0].split("/")[2];
	if (exports.plugins[plugin] == undefined) {
		exports.plugins[plugin] = { "id" : plugin, "classes" : {}};
	}
	return exports.plugins[plugin];
};

//From a given file, retrieve a file data. containing methods, id
exports.getFile = function(file) {
	var source = file.to;
	//console.log(source);
	if (source == "/dev/null") {
		source = file.from;
		//console.log("EQUAL:" + source);
	}
  var location = source.split("src/");
  location = location.length > 1 ? location : source.split("generated/");
  var fileId =  location.length>1 ? location[1].replace(/\//g, ".").replace(".java", "") : location;
  
  if (exports.getPlugin(file).classes[fileId] == undefined) {
    exports.getPlugin(file).classes[fileId] = { "methods" : {}, "id" : fileId};
  }
  
	return exports.getPlugin(file).classes[fileId];
};

//From a given file and a signature, retrieve an object describing the method
exports.getMethod = function(file, signature) {
	var result = signature;

	var trimParameter = function(f) {
		return f.substring(0, f.lastIndexOf(" ")).trim().replace("~", ",").replace("_p", "");
	};
	
	var parameters = result.replace(/.*\(/, "("); //retrieve parameters, ie, after the (
	parameters = parameters.replace(/\).*/, ")"); //retrieve parameters, ie, before the (
	parameters = parameters.replace("(", "");
	parameters = parameters.replace(")", "");
	parameters = parameters.replace(/(<[^>]*),([^>]*>)/g, "$1~$2"); //replace , on parameters such as 'HashMap<String, Appender>' to avoid wrong parameter splits
	
	parameters = parameters.split(",").map(f => trimParameter(f)); //String componentName, HashMap<String, Appender> map doesnat work
	parameters = "("+parameters.join(", ")+")";
	
	//retrieve method name and qualifiers
	result = result.replace(/\(.*/, ""); //remove parameters
	var visibility = (result.indexOf("public") >= 0 ? "public" : ((result.indexOf("protected")>=0) ? "protected" : ((result.indexOf("private")>=0) ? "private" : "package")));
	
	var methodName = result.replace(/.* (\w+)/, "$1"); //remove identifiers like public static void
	
	var methodId = methodName+parameters;
	
	if (exports.getFile(file).methods[methodId] == undefined) {
		exports.getFile(file).methods[methodId] = { "name" : methodName, "parameters" : parameters, "isConstructor" : false, "visibility":visibility };
	}
	return exports.getFile(file).methods[methodId];
};

//Export the plugins to mediawiki format
exports.toMediawiki = function(plugins) {
 var ADDED = '<span class="labels"><span class="label label-success">Added</span></span>';
 var REMOVED = '<span class="labels"><span class="label label-error">Removed</span></span>';
 var DEPRECATED = '<span class="labels"><span class="label label-warning">Deprecated</span></span>';
 
 var visibility = { "public": "../Images/methpub_obj.png", "protected": "../Images/methpro_obj.png", "private": "../Images/methpri_obj.png", "package": "../Images/methdef_obj.png"};
 
 var result = "";
 
 for (p in exports.plugins) {
    var logPlugin = false;
    
    for (c in exports.plugins[p].classes) {
		//if (exports.plugins[p].classes[c].id.indexOf("DAnnotationHelper")>0) {
		//	console.log("ok");
		//}
	  
      if (exports.plugins[p].classes[c].added && !exports.plugins[p].classes[c].removed) {
         if (!logPlugin) { result += ("\n==== Changes in <code>"+p+"</code>====\n"); logPlugin = true; }
         var icon = "../Images/jcu_obj.png";
		 result += ("* "+ADDED+"[[File:"+icon+"|16px]] <code>"+c+"</code>\n");
      }
      
      if (exports.plugins[p].classes[c].removed && !exports.plugins[p].classes[c].added) {
         if (!logPlugin) { result += ("\n==== Changes in <code>"+p+"</code>====\n"); logPlugin = true; }
         var icon = "../Images/jcu_obj.png";
		 result += ("* "+REMOVED+"[[File:"+icon+"|16px]] <code>"+c+"</code>\n");
         
      } else {
        
        for (m in exports.plugins[p].classes[c].methods) {
          var method = exports.plugins[p].classes[c].methods[m];
          
          var icon = visibility[method.visibility];
          var OVERRIDE = "";
          if (method.override == true) {
            OVERRIDE = "[[File:../Images/over_co.png|8px]]";
          }
          if (method.added && !method.removed) {
              if (!logPlugin) { result += ("\n==== Changes in <code>"+p+"</code>====\n"); logPlugin = true; }
              result += ("* "+ADDED+"[[File:"+icon+"|16px]]"+OVERRIDE+" <code>"+c+"."+method.name+method.parameters+"</code>\n");
          }
          if (method.removed && !method.added) {
              if (!logPlugin) { result += ("\n==== Changes in <code>"+p+"</code>====\n"); logPlugin = true; }
              result += ("* "+REMOVED+"[[File:"+icon+"|16px]]"+OVERRIDE+" <code>"+c+"."+method.name+method.parameters+"</code>\n");
          }
          if (method.deprecated == true) {
              if (!logPlugin) { result += ("\n==== Changes in <code>"+p+"</code>====\n"); logPlugin = true; }
              result += ("* "+DEPRECATED+"[[File:"+icon+"|16px]]"+OVERRIDE+" <code>"+c+"."+method.name+method.parameters+"</code>\n");
          }
        }
      }
    }
  }
  return result;
};

var args = process.argv.slice(2);
if (args.length == 0) {
	console.log("node function.js diff-file");
	
} else {
	
	//git diff v1.2.0 head > 1.3.0.txt
   // exports.loadUrl("http://localhost:8090/1.3.0.txt", function(res) {
	exports.loadFile(args[0], function(gitDiffChunks) {
      exports.plugins = {};
      var files = [];

      for (var i = 0; i < gitDiffChunks.length; i++) {
        files = files.concat(parse(gitDiffChunks[i]));
      }
	  //files.map(x => console.log(x.to));
      //exports.proceedFile(files[111]);
      files.forEach(exports.proceedFile);
      
	  fs.writeFile("output.json", JSON.stringify(exports.plugins, null, 2), function(err) {
			if(err) {
				return console.log(err);
			}

			console.log("The file was saved!");
		}); 

		let result = exports.toMediawiki(exports.plugins);
      
	  fs.writeFile("output.mediawiki", result, function(err) {
			if(err) {
				return console.log(err);
			}

			console.log("The file was saved!");
		}); 
		
    });
    
}
