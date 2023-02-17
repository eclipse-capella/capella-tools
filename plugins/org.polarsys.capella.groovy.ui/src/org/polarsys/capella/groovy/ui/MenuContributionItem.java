package org.polarsys.capella.groovy.ui;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.IWorkbenchContribution;
import org.eclipse.ui.services.IServiceLocator;
import org.polarsys.capella.common.tools.report.config.registry.ReportManagerRegistry;
import org.polarsys.capella.common.tools.report.util.IReportManagerDefaultComponents;
import org.polarsys.capella.core.model.handler.helpers.CapellaAdapterHelper;
import org.polarsys.capella.groovy.CapellaScriptLaunchConfigurationDelegate;

public class MenuContributionItem extends CompoundContributionItem implements IWorkbenchContribution {
  private final static Logger __logger = ReportManagerRegistry.getInstance().subscribe(IReportManagerDefaultComponents.DEFAULT);
  
  String id;
  public MenuContributionItem(String id) {
    this.id = id;
  }

  public static class Accelerator extends MenuContributionItem {

    public Accelerator() {
      super("group.accelerator");
    }
    
  }
  public static class Wizard extends MenuContributionItem {

    public Wizard() {
      super("group.wizard");
    }
    
  }
  public static class Semantic extends MenuContributionItem {

    public Semantic() {
      super("group.semantic");
    }
    
  }
  public static class ShowIn extends MenuContributionItem {

    public ShowIn() {
      super("group.showIn");
    }
    
  }

  public static class SendTo extends MenuContributionItem {

    public SendTo() {
      super("group.sendTo");
    }
    
  }

  public static class Additions extends MenuContributionItem {

    public Additions() {
      super("additions");
    }
    
  }
  
  @Override
  public boolean isDynamic() {
    return true;
  }
  
  @Override
  public boolean isDirty() {
    return true;
  }
  
  /**
   * No contribution.
   */
  private static final IContributionItem[] NO_CONTRIBUTION_ITEM = new IContributionItem[0];

  IServiceLocator locator = null;

  @Override
  public void initialize(IServiceLocator serviceLocator) {
    locator = serviceLocator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IContributionItem[] getContributionItems() {
    ISelectionService service = locator.getService(ISelectionService.class);
    ISelection selection = service.getSelection();

    if ((selection != null) && (selection instanceof IStructuredSelection)) {

      // Preconditions
      final Object firstElement = ((IStructuredSelection) selection).getFirstElement();
      final ArrayList<IContributionItem> c = new ArrayList<>();
      
      if (MenuUpdater.MENUS.get(id) != null) {
        EObject a = CapellaAdapterHelper.resolveBusinessObject(firstElement);
        for (MenuEntry entry : MenuUpdater.MENUS.get(id)) {
          if (entry.applyOn.isInstance(a)) {
            c.add(createNavigationTowards(entry, a));
          }
        }
      }
      
      return c.toArray(new IContributionItem[0]);
/*
      for (EObject currentModelElement : items) {
        result.add(createNavigationTowards(currentModelElement));
      }
      return result.toArray(new IContributionItem[result.size()]);*/
    }

    return NO_CONTRIBUTION_ITEM;
  }

  protected IContributionItem createNavigationTowards(final MenuEntry resource, EObject object) {
    final IFile file = resource.file;
    
    return new ActionContributionItem(new Action(file.getName()) {
      @Override
      public String getText() {
        return resource.name;
      }
      
      @Override
      public ImageDescriptor getImageDescriptor() {
        return resource.icon;
      }
      
      @Override
      public void run() {
        
        CapellaScriptLaunchConfigurationDelegate d = new CapellaScriptLaunchConfigurationDelegate();
        try {
          d.runScript(d.loadClass(file));
        } catch (Exception e) {
          e.printStackTrace();
          __logger.error(e.getMessage(), e);
          throw new RuntimeException(e);
        }
        
        super.run();
      }
    });
  }
  
  
}
