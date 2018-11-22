/*******************************************************************************
 * Copyright (c) 2015, 2017 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Felix Dorner <felix.dorner@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.groovy

import com.google.common.collect.ForwardingIterator
import org.eclipse.emf.common.util.AbstractTreeIterator
import org.eclipse.emf.ecore.impl.BasicEObjectImpl
import org.eclipse.emf.ecore.EObject

/**
 * Additional methods defined on EObject 
 */
class EObjectQueryMethods {

  public EObjectQueryMethods() {
    install();
  }

  def install() {

    // eAllContents does not work for grep/filter/each
    // because the used iterator is also a list and that confuses
    // groovy
    EObject.metaClass.descendants = { ->
      new ForwardingTreeIterator(delegate.eAllContents())
    }

  }

}
