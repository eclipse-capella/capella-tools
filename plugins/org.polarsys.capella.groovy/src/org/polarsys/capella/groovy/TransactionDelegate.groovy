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
package org.polarsys.capella.groovy

import org.eclipse.sirius.business.api.query.DViewQuery
import org.eclipse.sirius.business.api.session.Session
import org.eclipse.sirius.viewpoint.DView

/**
 * This class defines methods and properties that are available in the context of a 
 * model transaction. 
 */
class TransactionDelegate {

  Session session
  
  TransactionDelegate(session){
    this.session = session
  }

  /**
   * @return A collection that contains all diagrams of the selected model
   */
  def diagrams(){
    session.ownedViews.collectMany { new DViewQuery(it).getLoadedRepresentations() } 
  }

}
