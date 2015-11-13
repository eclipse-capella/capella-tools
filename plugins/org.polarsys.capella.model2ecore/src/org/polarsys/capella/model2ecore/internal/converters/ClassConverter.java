/*******************************************************************************
 * Copyright (c) 2009 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Stephane Fournier - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.model2ecore.internal.converters;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.polarsys.capella.core.data.information.Class;
import org.polarsys.capella.core.data.information.util.InformationSwitch;
import org.polarsys.capella.model2ecore.internal.util.ModelHelper;

/**
 * @author Stephane Fournier
 */
public class ClassConverter extends InformationSwitch<EClassifier> {
  /**
   * Create an Ecore class for specified Capella Class. {@inheritDoc}
   */
  @Override
  public EClassifier caseClass(Class capellaClass) {
    EClass ecoreClass = EcoreFactory.eINSTANCE.createEClass();
    ecoreClass.setName(ModelHelper.forceCharactersToEcoreNamingConventions(capellaClass.getName()));
    ecoreClass.setAbstract(capellaClass.isAbstract());
    return ecoreClass;
  }

  /**
   * Get the model package this switch is running against.
   * @return a not <code>null</code> package.
   */
  public EPackage getModelPackage() {
    return modelPackage;
  }
}
