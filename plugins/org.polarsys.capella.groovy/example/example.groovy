/*******************************************************************************
 * Copyright (c) 2015 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Soyatec - initial API and implementation
 *******************************************************************************/

// We can send messages to the information view with the log function:
log "Hello capella from Groovy!"

// Command line arguments can be accessed like this:
log args[0]


/*
 * To work on a given model, the model function is used. Here I use the example
 * model that comes with capella.
 */
model('/In-Flight Entertainment System/In-Flight Entertainment System.aird') {

  /*
   * All logical functions. 
   */
  LogicalFunction.each {
    log it
  }

  /* 
   * All physical functions 
   */
  PhysicalFunction.each {
    log it
  }

  /**
   * Or just name/id pairs:
   */
  Actor.each {
    log "$it.name $it.id"
  }

  /*
   * Or enumerate them:
   */
  Capability.eachWithIndex { capability, index ->
    log "$index $capability.name"
  }

  /*
   * Or check if a predicate holds for all LF 
   */
  if (LogicalFunction.every { it.name != "Root Logical Function" }){
    log "All functions are root functions, ouch!"
  }

  /*
   * Or check if a predicate holds for any LF
   */
  if (LogicalFunction.any { it.name == "Root Logical Function"}) {
    log "There's a root function."
  }

  /*
   * Or search functions with regular expressions on their names:
   */
  LogicalFunction.grep ({ it.name =~ /Ground/ }).each {
    log it
  }

  /*
   * Get the first match
   */
  log LogicalFunction.find ({ it.name =~ /Ground/ })


  /* 
   * We can also write to the model. Let's capitalize all Function names that
   * have something to do with a Root
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
    log it.name
  }

  /*
   * We can also export diagrams as images
   */
  diagrams().eachWithIndex { diagram, index ->
    diagram.export("In-Flight Entertainment System/diagram${index}.jpg")
  }

}