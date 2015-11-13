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
package org.polarsys.capella.model2ecore.preferences;

/**
 * @author Stephane Fournier
 */
public interface IPreferenceConstants {
  /**
   * Preference to store data package ecore model folder.
   */
  public static final String PREFERENCE_DATA_PACKAGE_ECORE_MODEL_FOLDER = "dataPackageEcoreModelFolder"; //$NON-NLS-1$
  /**
   * Preference to store interface package ecore model folder.
   */
  public static final String PREFERENCE_INTERFACE_PACKAGE_ECORE_MODEL_FOLDER = "interfacePackageEcoreModelFolder"; //$NON-NLS-1$
  /**
   * Preference to include interface package ecore model export.
   */
  public static final String PREFERENCE_INTERFACE_PACKAGE_ECORE_MODEL_EXPORT = "interfacePackageEcoreModelExport"; //$NON-NLS-1$
}
