package org.polarsys.capella.groovy.api;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.polarsys.capella.common.ui.massactions.activator.MACapellaActivator;
import org.polarsys.kitalpha.massactions.shared.view.MAView;

public class MassApi {

  public static void createVisualizationTable(String name, Collection<EObject> selection, String ... ids) {

    try {
      IViewPart viewPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(
          MACapellaActivator.MV_VIEW_ID, MACapellaActivator.SEND_TO_MV_VIEW_COMMAND_PARAMETER_SECONDARY_ID,
          IWorkbenchPage.VIEW_VISIBLE);

      MAView maView = (MAView) viewPart;
      maView.setViewName(name);
      maView.dataChanged(selection);
      maView.getTable().applyColumnFilter(new ColumnFilter(ids));

    } catch (PartInitException e) {
      e.printStackTrace();
    }
  }
  
}
