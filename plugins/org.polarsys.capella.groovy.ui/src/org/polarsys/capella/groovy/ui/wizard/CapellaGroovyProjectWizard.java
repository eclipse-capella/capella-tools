/*******************************************************************************
 * Copyright (c) 2017 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *   
 * Contributors:
 *    Felix Dorner - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.groovy.ui.wizard;

import org.eclipse.pde.ui.templates.NewPluginProjectFromTemplateWizard;

public class CapellaGroovyProjectWizard extends NewPluginProjectFromTemplateWizard {

  @Override
  protected String getTemplateID() {
    return "org.polarsys.capella.groovy.ui.capellaGroovyProjectTemplate";
  }
}
