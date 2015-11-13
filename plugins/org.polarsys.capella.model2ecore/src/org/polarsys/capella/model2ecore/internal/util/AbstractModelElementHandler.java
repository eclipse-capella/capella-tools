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
package org.polarsys.capella.model2ecore.internal.util;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.common.mdsofa.common.misc.Couple;
import org.polarsys.capella.model2ecore.command.AbstractEcoreExporterCommand;


/**
 * @author Stephane Fournier
 */
public abstract class AbstractModelElementHandler {
  /**
   * Handle a model element.
   * @param element_p
   * @param parentPackage_p
   * @param exporter_p
   * @return  a couple with as key the exported classifier and as value if this classifier must be persisted.
   */
  public abstract Couple<EClassifier, Boolean> handleElement(ModelElement element_p, EPackage parentPackage_p, AbstractEcoreExporterCommand exporter_p);
}
