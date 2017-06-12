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
package org.polarsys.capella.groovy.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.codehaus.groovy.eclipse.dsl.GroovyDSLCoreActivator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class CapellaGroovyDSLSupport extends ClasspathContainerInitializer {

  public static final IPath CONTAINER_PATH = new Path("Capella Groovy DSL"); 

  private static final class DSLDClasspathContainer implements IClasspathContainer {
    
    private IClasspathEntry[] entries;
    private static final IClasspathEntry[] NO_ENTRIES = new IClasspathEntry[] {};
    
    public IPath getPath() {
        return CONTAINER_PATH;
    }

    public int getKind() {
        return K_APPLICATION;
    }

    public String getDescription() {
        return "Capella Groovy DSL";
    }

    public IClasspathEntry[] getClasspathEntries() {
        if (entries == null) {
            entries = calculateEntries();
        }
        return entries;
    }

    /*
     * This calculates the location of the plugin_dsld_support folder in the 
     * capella groovy ui plugin and adds it to the capella dsl support classpath container
     */
    protected IClasspathEntry[] calculateEntries() {
        if (GroovyDSLCoreActivator.getDefault().isDSLDDisabled()) {
            return NO_ENTRIES;
        }
        List<IClasspathEntry> newEntries = new ArrayList<IClasspathEntry>();
        try {
          Enumeration<URL> enu = CapellaGroovyUIPlugin.getInstance().getBundle().findEntries(".", "plugin_dsld_support", false);
          if (enu != null && enu.hasMoreElements()) {
            IPath folder = new Path(FileLocator.toFileURL(enu.nextElement()).getPath());
            if (folder != null) {
                newEntries.add(JavaCore.newLibraryEntry(folder, null, null));
            }
          }
        } catch (Exception e) {
            GroovyDSLCoreActivator.logException(e);
        }
        return newEntries.toArray(NO_ENTRIES);
    }
  }

  @Override
  public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
    JavaCore.setClasspathContainer(CONTAINER_PATH, new IJavaProject[] {project}, new IClasspathContainer[] { new DSLDClasspathContainer() }, null);
  }
}
