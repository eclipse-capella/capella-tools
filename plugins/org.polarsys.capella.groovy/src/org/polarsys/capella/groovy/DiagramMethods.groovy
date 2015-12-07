/*******************************************************************************
 * Copyright (c) 2015 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Soyatec - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.groovy

import org.eclipse.core.runtime.IPath
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.core.runtime.Path
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.emf.transaction.RunnableWithResult
import org.eclipse.jface.operation.IRunnableWithProgress
import org.eclipse.sirius.business.api.session.Session
import org.eclipse.sirius.business.api.session.SessionManager
import org.eclipse.sirius.common.tools.api.resource.ImageFileFormat
import org.eclipse.sirius.diagram.DDiagram
import org.eclipse.sirius.table.metamodel.table.DTable
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager
import org.eclipse.sirius.ui.business.api.dialect.ExportFormat
import org.eclipse.sirius.ui.business.api.dialect.ExportFormat.ExportDocumentFormat
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.progress.IProgressService
import org.eclipse.swt.widgets.Display;
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.sirius.viewpoint.DRepresentation

class DiagramMethods {

  
  
  DiagramMethods() {
    
    install()
  }

  def install() {
    
    DRepresentation.metaClass.export = { path ->

      final DRepresentation theDiagram = delegate
      final URI u = EcoreUtil.getRootContainer(theDiagram, true).eResource().getURI();
      final Session session = SessionManager.INSTANCE.getExistingSession(u)

      RunnableWithResult transactionRunnable = session.getTransactionalEditingDomain().createPrivilegedRunnable(new Runnable(){
        public void run(){
          IPath destination = new Path(path)
          if (!destination.isAbsolute()){
            destination = ResourcesPlugin.getWorkspace().getRoot().getLocation().append(destination)
          }
          DialectUIManager.INSTANCE.export(theDiagram,
              session, destination, new ExportFormat(ExportDocumentFormat.NONE, ImageFileFormat.JPEG), new NullProgressMonitor());          
        }
      })
      PlatformUI.getWorkbench().getDisplay().syncExec(transactionRunnable)
    }
  }
}
