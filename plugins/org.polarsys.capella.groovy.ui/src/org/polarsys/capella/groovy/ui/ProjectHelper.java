package org.polarsys.capella.groovy.ui;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.core.model.handler.helpers.CapellaAdapterHelper;

public class ProjectHelper {
  public static Collection<IFile> getFiles() {
   final ArrayList<IFile> files = new ArrayList<>();
    
    for (IProject project: ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
      try {
        if (project.isOpen()) {
        project.accept(new IResourceVisitor() {
          
          @Override
          public boolean visit(IResource resource) throws CoreException {
            if ("groovy".equals(resource.getFileExtension()) && resource instanceof IFile)  {
              files.add((IFile) resource);
            }
            return true;
          }
        });
        }
      } catch (CoreException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return files;
  }
}
