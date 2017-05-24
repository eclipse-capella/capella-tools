/*******************************************************************************
 * Copyright (c) 2015, 2017 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Soyatec - initial API and implementation
 *    Felix Dorner <felix.dorner@gmail.com>
 *******************************************************************************/
package org.polarsys.capella.groovy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public final class CapellaScriptLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

//  private static final ImportCustomizer importCustomizer;
//
//  static {
//    importCustomizer = initializeImportCustomizer();
//  }

  public void launch(final ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
      throws CoreException {

    WorkspaceJob job = new WorkspaceJob("Capella Groovy Script Execution") {
      @Override
      public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
        String location = configuration.getAttribute(CapellaGroovyConstants.LAUNCH_ATTR_SCRIPT_LOCATION.name(),
            (String) null);
        String[] args = configuration.getAttribute(CapellaGroovyConstants.LAUNCH_ATTR_PROGRAM_ARGS.name(), "")
            .split("\\s+");
        IFile capellaScriptFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(location));

        Collection<URL> urls = new ArrayList<URL>();

        
        // Locate jars on the script project's build path and add them to the classloader
        if (capellaScriptFile.getProject().hasNature(JavaCore.NATURE_ID)){
          IJavaProject project = JavaCore.create(capellaScriptFile.getProject());
          for (IClasspathEntry entry : project.getRawClasspath()){
            if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY){
              try {
                urls.add(ResourcesPlugin.getWorkspace().getRoot().getFile(entry.getPath()).getLocationURI().toURL());
              } catch (MalformedURLException e) {
                CapellaGroovyPlugin.getInstance().getLog().log(new Status(IStatus.ERROR, CapellaGroovyPlugin.PLUGIN_ID, e.getMessage(), e));
              }
            }
          }
        }

        try {
          final Reader reader = new BufferedReader(new InputStreamReader(capellaScriptFile.getContents()));
          CompilerConfiguration c = new CompilerConfiguration();

//          c.setScriptBaseClass("org.polarsys.capella.groovy.CapellaScriptBase");
//          c.addCompilationCustomizers(importCustomizer);

          ClassLoader loader = getClass().getClassLoader();

          if (urls.size() > 0){
            loader = new URLClassLoader(urls.toArray(new URL[urls.size()]), loader);        
          }

          final GroovyShell shell = new GroovyShell(loader, new Binding(args), c);
          shell.evaluate(reader);
        } catch (CoreException e) {
          throw new CoreException(new Status(IStatus.ERROR, CapellaGroovyPlugin.PLUGIN_ID, e.getMessage(), e));
        }
        return Status.OK_STATUS;
      }
    };
    job.schedule();
  }

//  private static ImportCustomizer initializeImportCustomizer(){
//
//    ImportCustomizer result = new ImportCustomizer();
//
//    for (EPackage pack : CapellaPackageRegistry.getAllCapellaPackages()){
//      for (EClassifier classifier : pack.getEClassifiers()){
//        if (classifier instanceof EClass) {
//          result.addImports(((EClass) classifier).getInstanceClass().getName());
//        }
//      }
//    }
//
//    return result;
//
//  }

}
