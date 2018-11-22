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

import org.eclipse.sirius.viewpoint.description.tool.SetValue
import org.polarsys.capella.core.data.capellacore.CapellacoreFactory
import org.polarsys.capella.core.data.capellacore.EnumerationPropertyLiteral
import org.codehaus.groovy.runtime.callsite.ClassMetaClassGetPropertySite
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.emf.transaction.TransactionalEditingDomain
import org.polarsys.capella.core.data.capellacore.AbstractPropertyValue
import org.polarsys.capella.core.data.capellacore.CapellaElement
import org.polarsys.capella.core.data.capellacore.EnumerationPropertyValue
import org.polarsys.capella.core.data.capellacore.FloatPropertyValue
import org.polarsys.capella.core.data.capellacore.IntegerPropertyValue
import org.polarsys.capella.core.data.capellacore.StringPropertyValue
import org.polarsys.capella.core.model.helpers.listeners.CapellaModelDataListenerForClass
import org.polarsys.capella.core.model.helpers.registry.CapellaPackageRegistry

import com.google.common.collect.Iterators

class CapellaQueryMethods {

  TransactionalEditingDomain domain

  CapellaQueryMethods() {
    install()
  }

  def install() {

    /*
     * Adds support for static iterator() on all capella metaclasses, to allow iterating over the instances of the callee in the active editing domain
     */
    for (EPackage p : CapellaPackageRegistry.getAllCapellaPackages()) {
      for (EClassifier cls : p.getEClassifiers()){
        if (cls instanceof EClass){
          staticMethods.each{methodName, methodImpl ->
            ((EClass) cls).getInstanceClass().metaClass.static[methodName] = methodImpl
          }
        }
      }
    }
    
    /*
     * Adds setPropertyValue and getPropertyValue methods 
     */
    CapellaElement.class.metaClass.getPropertyValue = { String name ->
      for (AbstractPropertyValue pv : ((CapellaElement)delegate).getAppliedPropertyValues()){
        if (name.equals(pv.getName())){
          return pv.getValue()
        }
      }
    }

    CapellaElement.class.metaClass.setPropertyValue = { String name, Object value ->
      AbstractPropertyValue pv = ((CapellaElement)delegate).getAppliedPropertyValues().find {
        name.equals(it.name);
      }

      def result = null

      if (pv == null) {
        if (value instanceof EnumerationPropertyLiteral) {
          pv = CapellacoreFactory.eINSTANCE.createEnumerationPropertyValue()
        }
        if (value instanceof String) {
          pv = CapellacoreFactory.eINSTANCE.createStringPropertyValue()
        }
        if (value instanceof Integer) {
          pv = CapellacoreFactory.eINSTANCE.createIntegerPropertyValue()
        }
        if (value instanceof Float || value instanceof Double) {
          value = (float) value;
          pv = CapellacoreFactory.eINSTANCE.createFloatPropertyValue()
        }

        pv.setName(name)
        ((CapellaElement)delegate).getOwnedPropertyValues().add(pv)
        ((CapellaElement)delegate).getAppliedPropertyValues().add(pv)
      } else {
        result = pv.getValue()
      }
      pv.setValue(value)
      result
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
