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
package org.polarsys.capella.groovy;

import org.apache.log4j.Logger
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.emf.edit.domain.EditingDomain
import org.eclipse.emf.edit.provider.IItemLabelProvider
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain
import org.eclipse.sirius.business.api.session.Session
import org.eclipse.sirius.business.api.session.SessionManager
import org.polarsys.capella.common.ef.ExecutionManagerRegistry
import org.polarsys.capella.common.tools.report.EmbeddedMessage
import org.polarsys.capella.common.tools.report.config.registry.ReportManagerRegistry
import org.polarsys.capella.common.tools.report.util.IReportManagerDefaultComponents
import org.polarsys.capella.core.data.la.LogicalFunction
import org.polarsys.capella.core.model.handler.provider.CapellaAdapterFactoryProvider

/**
 * This is the baseclass in which all scripts are run. Methods defined in this class are simply callable from the
 * script.
 */
public abstract class CapellaScriptBase extends Script {

  private final static CapellaQueryMethods __queryMethods = new CapellaQueryMethods()
  private final static DiagramMethods __diagramMethods = new DiagramMethods()
  private final static Logger __logger = ReportManagerRegistry.instance.subscribe IReportManagerDefaultComponents.DEFAULT

  void log(object, level) {
    if (object != null){
      String label
      Object provider = CapellaAdapterFactoryProvider.instance.adapterFactory.adapt object, IItemLabelProvider.class
      if (provider instanceof IItemLabelProvider) {
        label = provider.getText object
      } else {
         label = object.toString()
      }
      __logger."$level" new EmbeddedMessage(label, IReportManagerDefaultComponents.DEFAULT, object)
    }
  }

  void info(object) {
    log(object, "info")
  }

  void warn(object) {
    log(object, "warn");
  }

  void error(object) {
    log(object, "error");
  }

  def model(path, closure){
    model(path, "write", closure)
  }

  def model(path, txmode, closure) {
    URI uri = URI.createPlatformResourceURI path, true
    IProgressMonitor monitor = new NullProgressMonitor()
    Session session = SessionManager.INSTANCE.getSession uri, monitor
    session.open(monitor)
    TransactionalEditingDomain domain = session.getTransactionalEditingDomain();
    __queryMethods.domain = domain

    if (txmode == "read") {
      domain.runExclusive(new Runnable(){
        void run() {
          def transactionDelegate = new TransactionDelegate(session)
          closure.delegate = new TransactionDelegate(session)
          closure()
        }
      })

    } else if (txmode == "write"){
      domain.getCommandStack().execute(new RecordingCommand(domain, "Groovy Script"){
        protected void doExecute(){
          closure.delegate = new TransactionDelegate(session)
          closure()
        }
      })
    } else {
      throw new Exception("Unknown txmode. Use either 'read' or 'write'")
    }

  }
}
