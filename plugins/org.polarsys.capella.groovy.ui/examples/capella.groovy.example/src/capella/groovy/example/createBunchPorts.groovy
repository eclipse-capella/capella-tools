@ContextualScript(section="group.semantic", name="Create New Ports", applyOn=PhysicalComponent.class)
package capella.groovy.example

import static org.polarsys.capella.groovy.Api.log;

import org.eclipse.emf.diffmerge.generic.api.diff.IElementPresence
import org.polarsys.capella.common.data.modellingcore.AbstractNamedElement
import org.polarsys.capella.core.data.cs.CsFactory
import org.polarsys.capella.core.data.cs.PhysicalPort
import org.polarsys.capella.core.data.ctx.SystemFunction
import org.polarsys.capella.core.data.fa.ComponentPort
import org.polarsys.capella.core.data.fa.FaFactory
import org.polarsys.capella.core.data.helpers.fa.services.FunctionExt
import org.polarsys.capella.core.data.la.LogicalFunction
import org.polarsys.capella.core.data.pa.PaFactory
import org.polarsys.capella.core.data.pa.PhysicalComponent
import org.polarsys.capella.core.data.pa.PhysicalComponentNature
import org.polarsys.capella.groovy.Api
import org.polarsys.capella.groovy.lang.ContextualScript
import groovy.transform.BaseScript

Api.runAction("Create New Ports", Api.getSelection().getAt(0)) {

	Api.getSelection().each {
		PhysicalComponent pc = ((PhysicalComponent) it);
		
		if (pc.nature == PhysicalComponentNature.NODE) {
			for (int i = 0; i< 10; i++) {
				PhysicalPort port = CsFactory.eINSTANCE.createPhysicalPort("PP"+i);
				((PhysicalComponent)it).getOwnedFeatures().add(port);
			}
			
		} else {
			for (int i = 0; i< 10; i++) {
				ComponentPort port = FaFactory.eINSTANCE.createComponentPort("CP"+i);
				((PhysicalComponent)it).getOwnedFeatures().add(port);
			}
		}
	};
}
