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

import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.emf.transaction.TransactionalEditingDomain
import org.polarsys.capella.core.data.la.LogicalFunction
import org.polarsys.capella.core.model.helpers.registry.CapellaPackageRegistry
import com.google.common.collect.Lists
import com.google.common.collect.Iterators

class CapellaQueryMethods {

  TransactionalEditingDomain domain

  CapellaQueryMethods() {
    install()
  }

  def install() {
    for (EPackage p : CapellaPackageRegistry.getAllCapellaPackages()) {
      for (EClassifier cls : p.getEClassifiers()){
        if (cls instanceof EClass){
          staticMethods.each{methodName, methodImpl ->
            ((EClass) cls).getInstanceClass().metaClass.static[methodName] = methodImpl
          }
        }
      }
    }

  }

  def staticMethods = [

       iterator: {
         Iterators.filter(EcoreUtil.getAllContents(domain.getResourceSet(), true), (Class) delegate)
       }

   ]

  static def methodMissing(obj, String method, args) {
    if (method.startsWith("print")) {
        def property = (method - "print").with {
            it[0].toLowerCase() + it[1..-1]
        }
        "${obj.getClass().simpleName} ${obj[property]}"
    }
    else {
        throw new NoSuchMethodException()
    }
  }

}
