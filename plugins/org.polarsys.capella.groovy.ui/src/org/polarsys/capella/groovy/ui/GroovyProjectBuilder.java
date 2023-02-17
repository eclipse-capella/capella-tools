package org.polarsys.capella.groovy.ui;

import java.util.HashSet;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.polarsys.capella.groovy.CapellaScriptLaunchConfigurationDelegate;

public class GroovyProjectBuilder extends IncrementalProjectBuilder {

  @Override
  protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
    MenuUpdater menuUpdater = new MenuUpdater();
    menuUpdater.removeGroovyMenus();
    
    HashSet<IProject> projects = new HashSet<>();
    for (IFile capellaScriptFile : ProjectHelper.getFiles()) {
      projects.add(capellaScriptFile.getProject());

      try {
        Class<?> cl = new CapellaScriptLaunchConfigurationDelegate().loadClass(capellaScriptFile);
        menuUpdater.registrerMenus(capellaScriptFile, cl);
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
    
    return projects.toArray(new IProject[0]);
  }

}
