/*******************************************************************************
 * Copyright (c) 2015, 2017 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Soyatec - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.groovy

import org.eclipse.sirius.business.api.query.DViewQuery
import org.eclipse.sirius.business.api.session.Session
import org.eclipse.sirius.diagram.DDiagram
import org.eclipse.sirius.table.metamodel.table.DTable

/**
 * This class defines methods and properties that are available in the context of a 
 * model transaction. 
 */
class TransactionDelegate {

  /**
   * The current session
   */
  final Session session
  
  TransactionDelegate(session){
    this.session = session
  }

  /**
   * @return A List that contains all diagrams of the active model
   */
  List<DDiagram> getDiagrams(){
    ((Iterable)session.ownedViews).collectMany { new DViewQuery(it).getLoadedRepresentations() }.grep(DDiagram)
  }
  
  /**
   * @return A List that contains all tables of the active model
   */
  List<DTable> getTables(){
    ((Iterable)session.ownedViews).collectMany { new DViewQuery(it).getLoadedRepresentations() }.grep(DTable)
  }

}
