@BaseScript(org.polarsys.capella.groovy.CapellaScriptBase)
package capella.groovy.example

import org.polarsys.capella.core.data.ctx.*
import org.polarsys.capella.core.data.fa.*
import org.polarsys.capella.core.data.la.*
import org.polarsys.capella.core.data.pa.*
import groovy.transform.BaseScript

// We can send messages to the information view with the log function:
info "Hello capella from Groovy!"

// Command line arguments are set in the launch configurations script arguments tab and
// can be accessed like this:
info args[0]

/*
 * To work on a given model, the model function is used. All query methods shown below
 * will be executed in the context of this model.
 */
model('/In-Flight Entertainment System/In-Flight Entertainment System.aird') {
	
  /*
   * Lists every logical function in the model
   */
  LogicalFunction.each {
    info it
  }

  /* 
   * Lists every physical function in the model 
   */
  PhysicalFunction.each {
    info it
  }

  /*
   * List name/id pairs for every actor in the model
   */
  Actor.each {
    info "$it.name $it.id"
  }

  /*
   * Or enumerate all Capabilities like this
   */
  Capability.eachWithIndex { capability, index ->
    info "$index $capability.name"
  }

  /*
   * Or check if a predicate holds for all Logical Functions 
   */
  if (LogicalFunction.every { it.name != "Root Logical Function" }){
    warn "All functions are root functions, ouch!"
  }

  /*
   * Or check if a predicate holds for any Logical Function
   */
  if (LogicalFunction.any { it.name == "Root Logical Function"}) {
    info "There's a root function."
  }

  /*
   * Or search functions with regular expressions on their names:
   */
  LogicalFunction.grep ({ it.name =~ /Ground/ }).each {
    info it
  }

  /*
   * Get the first match
   */
  info LogicalFunction.find ({ it.name =~ /Ground/ })


  /* 
   * We can also write to the model. This snippet will capitalize 
   * all root function names. Note that after the script completes, you
   * can actually 'undo' changes made by the script. Nice!
   */
  AbstractFunction.each {
    if (it.name =~ /Root/) {
      it.name = it.name.toUpperCase()
    }
  }

  /*
   * We can also work with the diagrams in the model
   */

  /*
   * Log the diagram names
   */
  getDiagrams().each {
    info it.name
  }

  /*
   * We can also export diagrams as images. Here we export only the PAB diagrams.
   */
  getDiagrams().grep ({it.description.titleExpression =~ "PAB"})
            .each {
                it.export("In-Flight Entertainment System/${it.name}.jpg")
            }

}