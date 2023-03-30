@ContextualScript(section="group.sendTo", name="Send to Allocation Table", applyOn=LogicalFunction.class)
package capella.groovy.example

import static org.polarsys.capella.groovy.Api.log;
import static org.polarsys.capella.groovy.lang.SemanticSection.CURRENT

import org.eclipse.emf.diffmerge.generic.api.diff.IElementPresence
import org.polarsys.capella.core.data.ctx.SystemFunction
import org.polarsys.capella.core.data.helpers.fa.services.FunctionExt
import org.polarsys.capella.core.data.la.LogicalFunction
import org.polarsys.capella.groovy.lang.SemanticQuery
import org.polarsys.capella.groovy.Api
import org.polarsys.capella.groovy.api.MassApi
import org.polarsys.capella.groovy.lang.ContextualScript
import groovy.transform.BaseScript

@SemanticQuery(name = "All Leafs", section = CURRENT)
def AllLeafs(LogicalFunction sf) {
	return FunctionExt.getAllLeafAbstractFunctions(sf);
}

@SemanticQuery(name="Allocating Component")
def AllocationBlock(LogicalFunction sf) {
	return sf.allocationBlocks
} 

MassApi.createVisualizationTable("Allocation Table", AllLeafs(Api.getSelection().getAt(0)), 
	"name", "Allocating Component");
