package org.polarsys.capella.groovy.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.resource.ImageDescriptor;

public class MenuEntry {
  IFile file; 
  String name;
  Class<?> applyOn;
  ImageDescriptor icon;
  
  public MenuEntry(IFile file, String name, Class<?> applyOn) {
    super();
    this.file = file;
    this.name = name;
    this.applyOn = applyOn;
    try {
      this.icon = ImageDescriptor.createFromURL(new URL(URI.createURI("platform:/plugin/org.codehaus.groovy.eclipse.codeassist/icons/groovy.png").toString()));
    } catch (MalformedURLException e) {
    }
    
  }

  public MenuEntry(IFile file) {
    this(file, file.getName(), Object.class);
  }
  
}
