@ContextualScript(section="group.semantic", name="Rename to smaller names", applyOn=NamedElement.class)
package capella.groovy.example

import static org.polarsys.capella.groovy.Api.*;

import org.eclipse.emf.diffmerge.generic.api.diff.IElementPresence
import org.polarsys.capella.common.data.modellingcore.AbstractNamedElement
import org.polarsys.capella.core.data.capellacore.NamedElement
import org.polarsys.capella.groovy.Api
import org.polarsys.capella.groovy.lang.ContextualScript
import groovy.transform.BaseScript

Api.runAction("Rename to smaller names", Api.getSelection().getAt(0)) {
	
 Api.getSelection().each { 
	 NamedElement element = (NamedElement) it;
	 element.setName(element.getName().replaceAll("[a-z ]+", ""));
 };

}
