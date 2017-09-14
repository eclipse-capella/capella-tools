/*******************************************************************************
 * Copyright (c) 2017 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.resourceset.viewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.ui.action.ViewerFilterAction;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.helper.SiriusUtil;
import org.eclipse.sirius.business.api.resource.ResourceDescriptor;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.business.internal.resource.SiriusRepresentationResourceFactory;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.description.util.DescriptionResourceImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.polarsys.capella.common.ef.ExecutionManager;
import org.polarsys.capella.common.ef.command.AbstractReadWriteCommand;
import org.polarsys.capella.common.helpers.TransactionHelper;
import org.polarsys.capella.core.model.handler.command.CapellaResourceHelper;
import org.polarsys.capella.core.model.handler.provider.CapellaAdapterFactoryProvider;

/**
 * 
 */
public class ResourceSetViewer extends ViewPart {

  /**
   * The ID of the view as specified by the extension.
   */
  public static final String ID = "org.polarsys.capella.resourceset.viewer.ResourceSetViewer";

  private TreeViewer viewer;
  private DrillDownAdapter drillDownAdapter;
  private Action externalizeRepresentation;
  private Action unloadRepresentation;
  private Action loadRepresentation;

  private ViewerFilterAction ecoreFilter;

  /**
   * This is a callback that will allow us to create the viewer and initialize it.
   */
  public void createPartControl(Composite parent) {
    viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    drillDownAdapter = new DrillDownAdapter(viewer);

    viewer.setContentProvider(new AdapterFactoryContentProvider(CapellaAdapterFactoryProvider.getInstance().getAdapterFactory()));
    viewer.setLabelProvider(new AdapterFactoryLabelProvider(CapellaAdapterFactoryProvider.getInstance().getAdapterFactory()));
    viewer.setInput(getViewSite());
    viewer.addSelectionChangedListener(new ISelectionChangedListener() {
      /**
       * {@inheritDoc}
       */
      @Override
      public void selectionChanged(SelectionChangedEvent event) {
        updateStatusBar(event.getSelection());
      }
    });

    activateListeningToPageSelectionEvents();

    getSite().setSelectionProvider(viewer);

    makeActions();
    hookContextMenu();
    contributeToActionBars();
  }

  /**
   * Displays information on the given element in the status bar.
   * @param selection
   */
  public void updateStatusBar(ISelection selection) {
      IStatusLineManager mgr = getViewSite().getActionBars().getStatusLineManager();
      if (mgr != null) {
          if (selection instanceof ITreeSelection) {
              Object selectedElement = ((ITreeSelection) selection).getFirstElement();
              if (selectedElement instanceof Resource) {
                  int gmfElements = 0;
                  int siriusElements = 0;
                  int semanticElements = 0;
                  int otherElements = 0;
                  TreeIterator<EObject> it = ((Resource) selectedElement).getAllContents();
                  while (it.hasNext()) {
                      EObject obj = it.next();
                      if (obj.eClass().getEPackage().getNsURI().startsWith("http://www.polarsys.org/capella")) {
                          semanticElements++;
                      } else if (obj.eClass().getEPackage().getNsURI().startsWith("http://www.eclipse.org/sirius")) {
                          siriusElements++;
                      } else if (obj.eClass().getEPackage().getNsURI().startsWith("http://www.eclipse.org/gmf")) {
                          gmfElements++;
                      } else {
                          otherElements++;
                      }
                  }

                  if (CapellaResourceHelper.isCapellaResource((Resource) selectedElement)) {
                      mgr.setMessage(ResourceSetViewerPlugin.getDefault().getImage(IImageKeys.IMG_STATUS_INFORMATION), "Capella elements : " + Integer.toString(semanticElements) + " / Other EMF elements : " + Integer.toString(otherElements));
                  } else if (CapellaResourceHelper.isAirdResource(((Resource) selectedElement).getURI())) {
                      mgr.setMessage(ResourceSetViewerPlugin.getDefault().getImage(IImageKeys.IMG_STATUS_INFORMATION), "Sirius elements : " + Integer.toString(siriusElements) + " / GMF elements : " + Integer.toString(gmfElements) + " / Other EMF elements : " + Integer.toString(otherElements));
                  } else {
                      mgr.setMessage(ResourceSetViewerPlugin.getDefault().getImage(IImageKeys.IMG_STATUS_INFORMATION), "EMF elements : " + Integer.toString(otherElements));
                  }
              } else {
                  mgr.setMessage(null);
              }
          }
      }
  }

  private void makeActions() {
    externalizeRepresentation = new Action() {
      public void run() {
        externalizeRepresentation();
      }
    };
    externalizeRepresentation.setText("Externalize representation");
    externalizeRepresentation.setToolTipText("Externalize representation");
    externalizeRepresentation.setImageDescriptor(ResourceSetViewerPlugin.getDefault().getImageDescriptor(IImageKeys.IMG_EXTERNALIZE_REPRESENTATION));

    unloadRepresentation = new Action() {
      public void run() {
        unloadRepresentation();
      }
    };
    unloadRepresentation.setText("Unload representation");
    unloadRepresentation.setToolTipText("Unload representation");
    unloadRepresentation.setImageDescriptor(ResourceSetViewerPlugin.getDefault().getImageDescriptor(IImageKeys.IMG_UNLOAD_RESOURCE));

    loadRepresentation = new Action() {
      public void run() {
        loadRepresentation();
      }
    };
    loadRepresentation.setText("Load representation");
    loadRepresentation.setToolTipText("Load representation");
    loadRepresentation.setImageDescriptor(ResourceSetViewerPlugin.getDefault().getImageDescriptor(IImageKeys.IMG_LOAD_RESOURCE));

    ecoreFilter = new ViewerFilterAction("Filter technical resources", IAction.AS_CHECK_BOX) {
      @Override
      public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (isChecked()) {
          if (element instanceof Resource) {
            String extension = ((Resource) element).getURI().fileExtension();
            if ("odesign".equals(extension) || "ecore".equals(extension)) {
              return false;
            }
          }
          // this is a transient specific resource providing some Sirius referenced styles
          if (element instanceof DescriptionResourceImpl) {
            return false;
          }
        }
        return true;
      }
    };
    ecoreFilter.addViewer(viewer);
    ecoreFilter.setChecked(true);
    ecoreFilter.setToolTipText("Filter technical resources");
    ecoreFilter.setImageDescriptor(ResourceSetViewerPlugin.getDefault().getImageDescriptor(IImageKeys.IMG_FILTER_RESOURCE));
  }

  protected void unloadRepresentation() {
    ISelection selection = viewer.getSelection();
    if (selection instanceof StructuredSelection) {
      List<?> selectedElements = ((StructuredSelection) selection).toList();
      for (final Object selectedElement : selectedElements) {
        if (selectedElement instanceof Resource) {
          TransactionHelper.getExecutionManager((Resource) selectedElement).execute(new AbstractReadWriteCommand() {
            @Override
            public void run() {
              ((Resource) selectedElement).unload();
            }
          });
        }
      }
    }
  }

  protected void loadRepresentation() {
    ISelection selection = viewer.getSelection();
    if (selection instanceof StructuredSelection) {
      List<?> selectedElements = ((StructuredSelection) selection).toList();
      for (final Object selectedElement : selectedElements) {
        if (selectedElement instanceof Resource) {
          TransactionHelper.getExecutionManager((Resource) selectedElement).execute(new AbstractReadWriteCommand() {
            @Override
            public void run() {
              try {
                ((Resource) selectedElement).load(Collections.EMPTY_MAP);
              } catch (IOException ex) {
                ex.printStackTrace();
              }
            }
          });
        }
      }
    }
  }

  protected void externalizeRepresentation() {
    ISelection selection = viewer.getSelection();
    if (selection instanceof StructuredSelection) {
      List<?> selectedElements = ((StructuredSelection) selection).toList();
      for (final Object selectedElement : selectedElements) {
        if (selectedElement instanceof DRepresentation) {
          final Resource[] newResource = { null };
          final Collection<DRepresentationDescriptor> descriptors = new ArrayList<DRepresentationDescriptor>();
          final ExecutionManager executionManager = TransactionHelper.getExecutionManager((EObject) selectedElement);
          final URI airdURI = ((DRepresentation) selectedElement).eResource().getURI();

          executionManager.execute(new AbstractReadWriteCommand() {
            @Override
            public void run() {
              Session session = SessionManager.INSTANCE.getSession(((DSemanticDecorator) selectedElement).getTarget());
              descriptors.addAll(DialectManager.INSTANCE.getRepresentationDescriptors(DialectManager.INSTANCE.getDescription((DRepresentation) selectedElement), session));

              String repName = ((DRepresentation) selectedElement).getName().replace(' ', '_');
              List<String> srmFileSegments = new ArrayList<>(airdURI.segmentsList());
              srmFileSegments.remove(srmFileSegments.size() - 1);
              srmFileSegments.add("diagrams"); //$NON-NLS-1$
              srmFileSegments.add(repName + "." + SiriusUtil.REPRESENTATION_FILE_EXTENSION); //$NON-NLS-1$

              URI resURI = URI.createHierarchicalURI(airdURI.scheme(), airdURI.authority(), airdURI.device(), srmFileSegments.toArray(new String[srmFileSegments.size()]), airdURI.query(), airdURI.fragment());
              newResource[0] = new SiriusRepresentationResourceFactory().createResource(resURI);
              newResource[0].getContents().add((EObject) selectedElement);
              ((ResourceImpl) newResource[0]).attached((EObject) selectedElement);
              executionManager.getEditingDomain().getResourceSet().getResources().add(newResource[0]);

//              try {
//                newResource[0].save(Collections.EMPTY_MAP);
//              } catch (IOException exception) {
//                exception.printStackTrace();
//              }
            }
          });

          executionManager.execute(new AbstractReadWriteCommand() {
            @Override
            public void run() {
              for (DRepresentationDescriptor descriptor : descriptors) {
                URI deresolvedURI = newResource[0].getURI().deresolve(airdURI);
                descriptor.setRepPath(new ResourceDescriptor(deresolvedURI.appendFragment(newResource[0].getURIFragment((DRepresentation) selectedElement))));
              }
            }
          });
        }
      }
    }
  }

  private void hookContextMenu() {
    MenuManager menuMgr = new MenuManager("#PopupMenu");
    menuMgr.setRemoveAllWhenShown(true);
    menuMgr.addMenuListener(new IMenuListener() {
      public void menuAboutToShow(IMenuManager manager) {
        ResourceSetViewer.this.fillContextMenu(manager);
      }
    });
    Menu menu = menuMgr.createContextMenu(viewer.getControl());
    viewer.getControl().setMenu(menu);
    getSite().registerContextMenu(menuMgr, viewer);
  }

  private void contributeToActionBars() {
    IActionBars bars = getViewSite().getActionBars();
    fillLocalPullDown(bars.getMenuManager());
    fillLocalToolBar(bars.getToolBarManager());
  }

  private void fillLocalPullDown(IMenuManager manager) {
    manager.add(externalizeRepresentation);
    manager.add(new Separator());
    manager.add(unloadRepresentation);
    manager.add(loadRepresentation);
    manager.add(new Separator());
    manager.add(ecoreFilter);
  }

  private void fillContextMenu(IMenuManager manager) {
    manager.add(externalizeRepresentation);
    manager.add(new Separator());
    manager.add(unloadRepresentation);
    manager.add(loadRepresentation);
    manager.add(new Separator());
    manager.add(ecoreFilter);
    manager.add(new Separator());
    drillDownAdapter.addNavigationActions(manager);
    // Other plug-ins can contribute there actions here
    manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
  }
  
  private void fillLocalToolBar(IToolBarManager manager) {
    manager.add(externalizeRepresentation);
    manager.add(new Separator());
    manager.add(unloadRepresentation);
    manager.add(loadRepresentation);
    manager.add(new Separator());
    manager.add(ecoreFilter);
    manager.add(new Separator());
    drillDownAdapter.addNavigationActions(manager);
  }

  private ISelectionListener selectionListener;

  /**
   * Activate the listening to page selection events.
   */
  public void activateListeningToPageSelectionEvents() {
    selectionListener = getSelectionListener();
    if (null != selectionListener) {
      getSite().getPage().addSelectionListener(selectionListener);
    }
  }

  protected ISelectionListener getSelectionListener() {
    return new ISelectionListener() {
      /**
       * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
       */
      @SuppressWarnings("synthetic-access")
      @Override
      public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        Object newInput = handleWorkbenchPageSelectionEvent(part, selection);
        if ((null != newInput) && (newInput instanceof ResourceSet)) {
          viewer.setInput(newInput);
        }
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  protected Object handleWorkbenchPageSelectionEvent(IWorkbenchPart part_p, ISelection selection_p) {
    Object handledSelection = handleSelection(part_p, selection_p);
    if (handledSelection instanceof EObject) {
      return TransactionHelper.getEditingDomain((EObject) handledSelection).getResourceSet();
    }
    return handledSelection;
  }

  /**
   * Passing the focus request to the viewer's control.
   */
  public void setFocus() {
    viewer.getControl().setFocus();
  }

  /**
   * Handle selection for specified parameters.
   * @param part
   * @param selection
   * @return <code>null</code> means nothing to select.
   */
  public static Object handleSelection(IWorkbenchPart part, ISelection selection) {
    Object result = null;
    if (selection != null && !selection.isEmpty() && !(part instanceof ResourceSetViewer)) {
      if (selection instanceof IStructuredSelection) {
        IStructuredSelection selection_l = (IStructuredSelection) selection;
        Object firstElement = selection_l.getFirstElement();
        if (firstElement instanceof EObject) {
          result = firstElement;
        }
      }
    }
    return result;
  }
}
