@ContextualScript(section="group.sendTo", name="See Functional Chain Allocation", applyOn=FunctionalChain.class)
package capella.groovy.example

import static org.polarsys.capella.groovy.Api.log;
import static org.polarsys.capella.groovy.lang.SemanticSection.CURRENT

import org.eclipse.emf.diffmerge.generic.api.diff.IElementPresence
import org.polarsys.capella.core.data.fa.FunctionalChain
import org.polarsys.capella.core.data.helpers.fa.services.FunctionExt
import org.polarsys.capella.core.data.la.LogicalFunction
import org.polarsys.capella.groovy.lang.SemanticQuery
import org.polarsys.capella.groovy.Api
import org.polarsys.capella.groovy.lang.ContextualScript
import groovy.transform.BaseScript


def AllFunctions(FunctionalChain sf) {
	return sf.getInvolvedFunctions()
}

@SemanticQuery(name="Allocating Component")
def AllocationBlock(FunctionalChain sf) {
	return sf.allocationBlocks
}

Api.createVisualizationTable("Functional Chain Allocation", AllFunctions(Api.getSelection().getAt(0)), 
	"name", "Allocating Component");
