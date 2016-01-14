/*******************************************************************************
 * Copyright (c) 2015, 2016 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Soyatec - initial API and implementation
 *******************************************************************************/

// We can send messages to the information view with the log function:
info "Hello capella from Groovy!"

// Command line arguments can be accessed like this:
info args[0]


/*
 * To work on a given model, the model function is used. Here I use the example
 * model that comes with capella.
 */
model('/In-Flight Entertainment System/In-Flight Entertainment System.aird') {

  /*
   * All logical functions. 
   */
  LogicalFunction.each {
    info it
  }

  /* 
   * All physical functions 
   */
  PhysicalFunction.each {
    info it
  }

  /**
   * Or just name/id pairs:
   */
  Actor.each {
    info "$it.name $it.id"
  }

  /*
   * Or enumerate them:
   */
  Capability.eachWithIndex { capability, index ->
    info "$index $capability.name"
  }

  /*
   * Or check if a predicate holds for all LF 
   */
  if (LogicalFunction.every { it.name != "Root Logical Function" }){
    warn "All functions are root functions, ouch!"
  }

  /*
   * Or check if a predicate holds for any LF
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
   * We can also write to the model. Let's capitalize all Function names that
   * have something to do with a Root. Note that after the script completes, you
   * can actually 'undo' changes made by the script. Nice!
   */
  AbstractFunction.grep ({it.name =~ /Root/ }).each {
    it.name = it.name.toUpperCase()
  }

  /*
   * We can also work with the diagrams in the model
   */

  /*
   * Log the diagram names
   */
  diagrams().each {
    info it.name
  }

  /*
   * We can also export diagrams as images. Here we export only the architecture
   * diagrams.
   */
  diagrams().grep ({it.description.titleExpression =~ "Architecture"})
            .each {
                it.export("In-Flight Entertainment System/${it.name}.jpg")
            }

}