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

import java.util.ResourceBundle;

import org.codehaus.groovy.eclipse.core.model.GroovyRuntime;
import org.codehaus.groovy.eclipse.dsl.GroovyDSLCoreActivator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginReference;
import org.eclipse.pde.ui.templates.AbstractTemplateSection;
import org.eclipse.pde.ui.templates.ITemplateSection;
import org.eclipse.pde.ui.templates.NewPluginTemplateWizard;
import org.eclipse.pde.ui.templates.PluginReference;
import org.polarsys.capella.groovy.ui.CapellaGroovyDSLSupport;
import org.polarsys.capella.groovy.ui.CapellaGroovyUIPlugin;

public class CapellaGroovyTemplate extends NewPluginTemplateWizard {

  @Override
  public boolean performFinish(IProject project, IPluginModelBase model, IProgressMonitor monitor) {
    boolean result = super.performFinish(project, model, monitor);
    if (result){
      try {
        
        IProjectDescription description = project.getDescription();
        String[] newNatures = new String[] {
            "org.eclipse.jdt.groovy.core.groovyNature",
            "org.eclipse.pde.PluginNature",
            "org.eclipse.jdt.core.javanature"
        };
        description.setNatureIds(newNatures);
        project.setDescription(description, monitor);

        IJavaProject javaProject = JavaCore.create(project);
        GroovyRuntime.addLibraryToClasspath(javaProject, CapellaGroovyDSLSupport.CONTAINER_PATH, false);
        GroovyRuntime.addLibraryToClasspath(javaProject, GroovyDSLCoreActivator.CLASSPATH_CONTAINER_ID, false);

      } catch (CoreException e) {
        CapellaGroovyUIPlugin.getInstance().getLog().log(new Status(e.getStatus().getSeverity(), CapellaGroovyUIPlugin.PLUGIN_ID, e.getStatus().getMessage(), e.getStatus().getException()));
      }
    }
    return result;
  }

  @Override
  public ITemplateSection[] createTemplateSections() {
    return new ITemplateSection[] {
        
        new AbstractTemplateSection() {
          @Override
          public IPluginReference[] getDependencies(String schemaVersion) {
            return new IPluginReference[] { 
                new PluginReference("org.codehaus.groovy"),
                new PluginReference("org.polarsys.capella.groovy"),
                new PluginReference("org.polarsys.capella.core.data.gen"),
                new PluginReference("org.eclipse.sirius.diagram"),
            };
                
          }
          
          @Override
          public String getUsedExtensionPoint() {
            return null;
          }
          
          @Override
          public int getPageCount() {
            return 0;
          }
          
          @Override
          public WizardPage getPage(int pageIndex) {
            return null;
          }
          
          @Override
          public String[] getNewFiles() {
            return new String[0];
          }
          
          @Override
          public String getLabel() {
            return "capella groovy project template label";
          }
          
          @Override
          protected void updateModel(IProgressMonitor monitor) throws CoreException {
//            IPluginImport imp = model.getPluginFactory().createImport();
//            imp.setId("org.polarsys.capella.groovy");
          
          }
          
          @Override
          protected ResourceBundle getPluginResourceBundle() {
            return null;
          }
        }
        
    };
  }

}
