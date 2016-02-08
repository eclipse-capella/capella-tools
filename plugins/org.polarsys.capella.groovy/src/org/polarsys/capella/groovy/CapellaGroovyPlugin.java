/*******************************************************************************
 * Copyright (c) 2015, 2016 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Soyatec - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.groovy;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class CapellaGroovyPlugin extends Plugin {

  public static final String PLUGIN_ID = "org.polarsys.capella.groovy";

  private static CapellaGroovyPlugin instance;

  public static CapellaGroovyPlugin getInstance(){
    return instance;
  }

  public void start(BundleContext bundleContext) throws Exception {
    super.start(bundleContext);
    instance = this;
  }

  public void stop(BundleContext bundleContext) throws Exception {
    instance = null;
    super.stop(bundleContext);
  }
}
