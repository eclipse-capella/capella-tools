/*******************************************************************************
 * Copyright (c) 2006, 2017 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.command.recorder;

import org.osgi.framework.BundleContext;
import org.polarsys.capella.command.recorder.core.preferences.internal.RecorderCorePreferenceInitializer;
import org.polarsys.capella.common.mdsofa.common.activator.AbstractActivator;

public class CapellaCommandRecorderActivator extends AbstractActivator {

  // The shared instance
  private static CapellaCommandRecorderActivator plugin;

  // The plugin ID
  public static final String PLUGIN_ID = "org.polarsys.capella.command.recorder"; //$NON-NLS-1$

  /**
   * {@inheritDoc}
   */
  @Override
  public void start(BundleContext bundleContext) throws Exception {
    super.start(bundleContext);
    plugin = this;
    new RecorderCorePreferenceInitializer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop(BundleContext bundleContext) throws Exception {
    plugin = null;
    super.stop(bundleContext);
  }

  /**
   * Returns the shared instance
   * @return the shared instance
   */
  public static CapellaCommandRecorderActivator getDefault() {
    return plugin;
  }
}
