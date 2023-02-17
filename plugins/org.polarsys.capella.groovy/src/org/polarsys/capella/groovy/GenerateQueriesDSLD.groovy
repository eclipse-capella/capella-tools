@ContextualScript(name="Generate DSLD Capella API", applyOn=SystemEngineering.class)
package org.polarsys.capella.groovy

import java.util.Map.Entry

import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EPackage
import org.polarsys.capella.common.helpers.query.IQuery
import org.polarsys.capella.core.data.capellamodeller.SystemEngineering
import org.polarsys.capella.core.model.helpers.registry.CapellaPackageRegistry
import org.polarsys.capella.groovy.Api
import org.polarsys.capella.groovy.lang.ContextualScript
import org.polarsys.capella.groovy.CategoryEntry

//IFile file = EcoreUtil2.getFile(Api.getSelection().getAt(0).eResource()).getProject().getFile("out.dsld");
IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(".groovy");

IFile file = ResourcesPlugin.getWorkspace().getRoot().getProject(".groovy").getFolder(".groovy").getFile("out.dsld")

String result = "";
for (EPackage p : CapellaPackageRegistry.getAllCapellaPackages()) {
	for (EClassifier cls : p.getEClassifiers()){
		if (cls instanceof EClass){
			Collection<CategoryEntry> categories = Api.getCategories((EClass) cls).entrySet();
			if (categories.size() > 0) {
				result += "contribute(bind(type: currentType(subType(\""+ cls.getInstanceClassName() +"\")))) { \n"
				result += "  provider = \"Capella-Groovy\" \n"
				for (Entry<String, IQuery> cat : categories) {
					result += "  method name: '" + cat.getKey() + "', type: java.util.List<java.lang.Object>, declaringType: type, params: [], doc: 'Add type listener' \n"
				}
				result += "\n}\n\n"
			}
		}
	}
}

if (file.exists()) {
	file.setContents(new ByteArrayInputStream(result.getBytes()), 0, new NullProgressMonitor());
} else {
	file.create(new ByteArrayInputStream(result.getBytes()), true, new NullProgressMonitor());
}
