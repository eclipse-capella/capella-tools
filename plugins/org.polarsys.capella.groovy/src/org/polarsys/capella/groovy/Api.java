package org.polarsys.capella.groovy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.common.tools.api.interpreter.CompoundInterpreter;
import org.eclipse.sirius.common.tools.api.interpreter.EvaluationException;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterProvider;
import org.eclipse.sirius.common.tools.api.resource.ImageFileFormat;
import org.eclipse.sirius.common.tools.internal.interpreter.DefaultInterpreterProvider;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.ui.business.api.dialect.ExportFormat;
import org.eclipse.sirius.ui.business.api.dialect.ExportFormat.ExportDocumentFormat;
import org.eclipse.sirius.ui.tools.api.actions.export.SizeTooLargeException;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;
import org.polarsys.capella.common.ef.ExecutionManagerRegistry;
import org.polarsys.capella.common.ef.command.AbstractReadWriteCommand;
import org.polarsys.capella.common.helpers.TransactionHelper;
import org.polarsys.capella.common.tools.report.EmbeddedMessage;
import org.polarsys.capella.common.tools.report.config.registry.ReportManagerRegistry;
import org.polarsys.capella.common.tools.report.util.IReportManagerDefaultComponents;
import org.polarsys.capella.core.model.handler.helpers.CapellaAdapterHelper;

import groovy.lang.Closure;

public class Api {

  private final static Logger __logger = ReportManagerRegistry.getInstance()
      .subscribe(IReportManagerDefaultComponents.DEFAULT);

  public static IProject getProject(String name) {
    return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
  }

  public static Collection<EObject> getSelection() {
    IStructuredSelection selection = (IStructuredSelection) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
        .getActivePage().getSelection();
    Collection<EObject> rest = CapellaAdapterHelper.resolveBusinessObjects(selection.toList());
    return rest;
  }

  public static Collection<?> evaluate(EObject o, String aql) {

    IInterpreterProvider provider = CompoundInterpreter.INSTANCE.getProviderForExpression(aql);

    if (provider instanceof DefaultInterpreterProvider) {
      return Collections.emptyList();
    }

    IInterpreter interpreter = provider.createInterpreter();
    Object result = null;
    try {
      result = interpreter.evaluate(o, aql);
    } catch (EvaluationException e) {
      return Collections.emptyList();
    }

    if (result instanceof Collection) {
      Collection<?> originalResult = (Collection<?>) result;
      return originalResult;
    }
    return Collections.singleton(result);
  }

  public static void runAction(String command, EObject toto2, Closure<?> closure) {
    ExecutionManagerRegistry.getInstance().getExecutionManager(TransactionHelper.getEditingDomain(toto2))
        .execute(new AbstractReadWriteCommand() {

          @Override
          public String getName() {
            return command;
          }

          @Override
          public void run() {
            closure.call(toto2);
          }
        });
  }

  public static void log(String toto) {
    __logger.info(toto);
  }

  public static void runInUI(String name, final Closure<IStatus> closure) {
    
    UIJob job = new UIJob(name) {
          public IStatus runInUIThread(IProgressMonitor monitor) {
            return closure.call(monitor);
          }
        };
    job.schedule();
  

  }
  public static void log(String toto, EObject toto2) {
    __logger.info(new EmbeddedMessage(toto, // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        IReportManagerDefaultComponents.MODEL, Arrays.asList(toto2)));
  }

  public static void exportRepresentation(DRepresentationDescriptor descriptor, IFile file) {
    try {
      DialectUIManager.INSTANCE.export((DRepresentation) descriptor.getRepresentation(), Session.of(descriptor).get(),
          file.getLocation(), new ExportFormat(ExportDocumentFormat.NONE, ImageFileFormat.SVG),
          new NullProgressMonitor());
    } catch (SizeTooLargeException e) {
      e.printStackTrace();
    }
  }

}
