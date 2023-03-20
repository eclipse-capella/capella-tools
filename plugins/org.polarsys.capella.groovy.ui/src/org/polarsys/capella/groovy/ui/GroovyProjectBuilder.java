package org.polarsys.capella.groovy.ui;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.polarsys.capella.groovy.CapellaScriptLaunchConfigurationDelegate;

public class GroovyProjectBuilder extends IncrementalProjectBuilder {

  boolean requireReloadBundle(IResourceDelta delta) {
    if (delta == null) {
      return true;
    }
    ArrayList<IFile> files = new ArrayList<IFile>();
    try {
      delta.accept(new IResourceDeltaVisitor() {
        @Override
        public boolean visit(IResourceDelta delta) throws CoreException {
          if (delta.getResource() instanceof IFile) {
            if ("MF".equals(delta.getResource().getFileExtension().toUpperCase())) {
              files.add((IFile)delta.getResource());
            }
          }
          return true;
        }
      });
    } catch (CoreException e) {
      e.printStackTrace();
    }
    return !files.isEmpty();
  }
  
  @Override
  protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
    MenuUpdater menuUpdater = new MenuUpdater();
    menuUpdater.removeGroovyMenus();
    
    SemanticQueryUpdater queryUpdater = new SemanticQueryUpdater();
    queryUpdater.removeGroovyQueries();
    
    CapellaScriptLaunchConfigurationDelegate loader = new CapellaScriptLaunchConfigurationDelegate();

    if (requireReloadBundle(getDelta(getProject()))) {
      loader.reloadBundle(getProject());
    }
    
    for (IFile capellaScriptFile : ProjectHelper.getFiles(getProject())) {
      try {
        Class<?> cl = loader.loadClass(capellaScriptFile);
        menuUpdater.registrerMenus(capellaScriptFile, cl);
        queryUpdater.createQueries(cl);
        
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
    
    return new IProject[0];
  }

}
