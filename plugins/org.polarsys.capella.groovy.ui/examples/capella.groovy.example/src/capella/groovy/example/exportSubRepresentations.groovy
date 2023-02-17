@ContextualScript(name="Export Sub Representations", applyOn=CapellaElement.class)
package capella.groovy.example

import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.WorkspaceJob

import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.ICoreRunnable
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status
import org.eclipse.sirius.common.tools.api.resource.ImageFileFormat
import org.eclipse.sirius.diagram.DDiagram
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor
import org.eclipse.ui.progress.UIJob
import org.polarsys.capella.common.helpers.EcoreUtil2
import org.polarsys.capella.core.data.capellacore.CapellaElement
import org.polarsys.capella.core.model.handler.helpers.RepresentationHelper

import org.polarsys.capella.groovy.Api
import org.polarsys.capella.groovy.lang.ContextualScript


Api.runInUI("Export") { IProgressMonitor monitor -> 
	Collection representations = RepresentationHelper.getAllRepresentationDescriptorsTargetedBy(Api.getSelection());
	monitor.beginTask("Export", representations.size());

	representations.each {
		DRepresentationDescriptor desc = it;
		if (desc.getRepresentation() instanceof DDiagram) {
			IProject project = EcoreUtil2.getFile(desc.eResource()).getProject();
			Api.exportRepresentation(desc, project.getFile(desc.getName() + "." + ImageFileFormat.SVG.name.toLowerCase()));
		}
		monitor.worked(1);
	}
	return Status.OK_STATUS;
}
