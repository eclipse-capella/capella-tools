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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.polarsys.capella.model2ecore.CapellaEcoreExporterActivator;


/**
 * @author Stephane Fournier
 */
public class PreferencesInitializer extends AbstractPreferenceInitializer {
  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeDefaultPreferences() {
    IPreferenceStore preferenceStore = CapellaEcoreExporterActivator.getDefault().getPreferenceStore();
    String userHome = System.getProperty("user.home"); //$NON-NLS-1$
    preferenceStore.setDefault(IPreferenceConstants.PREFERENCE_DATA_PACKAGE_ECORE_MODEL_FOLDER, userHome);
    preferenceStore.setDefault(IPreferenceConstants.PREFERENCE_INTERFACE_PACKAGE_ECORE_MODEL_FOLDER, userHome);
    preferenceStore.setDefault(IPreferenceConstants.PREFERENCE_INTERFACE_PACKAGE_ECORE_MODEL_EXPORT, true);
  }
}
