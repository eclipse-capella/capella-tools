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
package org.polarsys.capella.groovy.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class CapellaGroovyUIPlugin extends AbstractUIPlugin {

  public static final String PLUGIN_ID = "org.polarsys.capella.groovy.ui";

  private static CapellaGroovyUIPlugin instance;

  public static CapellaGroovyUIPlugin getInstance() {
    return instance;
  }

  /**
   * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
   */
  public void start(BundleContext bundleContext) throws Exception {
    super.start(bundleContext);
    instance = this;
  }

  /**
   * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
   */
  public void stop(BundleContext bundleContext) throws Exception {
    instance = null;
    super.stop(bundleContext);
  }

}
