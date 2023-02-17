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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.PDECore;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;

public final class CapellaScriptLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

  public Class<?> loadClass(IFile capellaScriptFile) throws CoreException {
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
      HashMap<IProject, Bundle> bs = new HashMap<>();

      Bundle groovy = Platform.getBundle("org.codehaus.groovy");
      String REFERENCE_URI_PREFIX = "reference:"; //$NON-NLS-1$
      for (IPluginModelBase model : PDECore.getDefault().getModelManager().getWorkspaceModels()) {
        URL url = model.getUnderlyingResource().getProject().getLocationURI().toURL();
        final String candidateLocationReference = REFERENCE_URI_PREFIX
            + URLDecoder.decode(url.toExternalForm(), System.getProperty("file.encoding")); //$NON-NLS-1$

        Bundle b2 = CapellaGroovyPlugin.getInstance().getBundle().getBundleContext()
            .installBundle(candidateLocationReference);
        bs.put(model.getUnderlyingResource().getProject(), b2);
        for (BundleSpecification spec : model.getBundleDescription().getRequiredBundles()) {
          Platform.getBundle(spec.getName());
        }
      }

      Bundle bp = bs.get(capellaScriptFile.getProject());
      bp.start();
      ClassLoader cls = bp.adapt(BundleWiring.class).getClassLoader();

      final GroovyClassLoader transformLoader = new GroovyClassLoader(
          groovy.adapt(BundleWiring.class).getClassLoader());
      GroovyClassLoader loader = new GroovyClassLoader(cls) {

        @Override
        protected CompilationUnit createCompilationUnit(CompilerConfiguration config, CodeSource source) {
          return new CompilationUnit(config, source, this, transformLoader, true, null);
        }
      };

      for (URL u : urls){
        loader.addURL(u);
      }

      Class<?> cl = loader.parseClass(new GroovyCodeSource(capellaScriptFile.getLocation().toFile(), capellaScriptFile.getCharset()));
      return cl;
      
    } catch (CoreException e) {
      throw new CoreException(new Status(IStatus.ERROR, CapellaGroovyPlugin.PLUGIN_ID, e.getMessage(), e));
    } catch (Exception e) {
      throw new CoreException(new Status(IStatus.ERROR, CapellaGroovyPlugin.PLUGIN_ID, e.getMessage(), e));
    }
  }
  

  public IStatus runScript(Class<?> groovyClass, String ... args) throws CoreException {
    try {
      InvokerHelper.runScript(groovyClass, args);

    } catch (Exception e) {
      throw new CoreException(new Status(IStatus.ERROR, CapellaGroovyPlugin.PLUGIN_ID, e.getMessage(), e));
    }
    return Status.OK_STATUS;
  }
  
  
  public void launch(final ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
      throws CoreException {

    WorkspaceJob job = new WorkspaceJob("Capella Groovy Script Execution") {
      @Override
      public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
        String location = configuration.getAttribute(CapellaGroovyConstants.LAUNCH_ATTR_SCRIPT_LOCATION.name(),
            (String) null);
        String[] args = DebugPlugin.parseArguments(configuration.getAttribute(CapellaGroovyConstants.LAUNCH_ATTR_PROGRAM_ARGS.name(), ""));
        IFile capellaScriptFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(location));
        return runScript(loadClass(capellaScriptFile), args);
      }
    };
    job.schedule();
  }

}
