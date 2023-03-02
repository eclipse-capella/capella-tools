@ContextualScript(section="group.sendTo", name="Send to Excel", applyOn=FunctionalChain.class)
/**
 * This example shows how you can use Groovys @Grab annotation to use existing java libraries
 * on demand. The libraries will be downloaded when the script runs, so you only have to deploy
 * the script itself, the rest is automatic.
 * For more grab information, see http://docs.groovy-lang.org/latest/html/documentation/grape.html
 *
 * Here we use the apache commons csv library to export physical functions and their descriptions
 */
package capella.groovy.example

@Grab(group='org.apache.poi', module='poi', version='5.2.3')
@Grab(group='org.apache.poi', module='poi-ooxml', version='5.2.3')

import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.ui.PlatformUI
import org.polarsys.capella.core.data.pa.PhysicalFunction
import org.polarsys.capella.common.helpers.EcoreUtil2
import org.polarsys.capella.core.data.fa.AbstractFunction
import org.polarsys.capella.core.data.fa.FunctionalChain
import org.polarsys.capella.core.data.la.LogicalFunction
import org.polarsys.capella.groovy.lang.ContextualScript
import org.polarsys.capella.groovy.Api

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

Collection<FunctionalChain> objects = Api.getSelection();
File file = EcoreUtil2.getProject(objects.first()).getLocation().append("Chains.xlsx").toFile();


// Create a Workbook
Workbook workbook = new XSSFWorkbook();
CreationHelper createHelper = workbook.getCreationHelper();

// Create a Font for styling actor cells
Font headerFont = workbook.createFont();
headerFont.setColor(IndexedColors.LIGHT_BLUE.getIndex());
CellStyle actorStyle = workbook.createCellStyle();
actorStyle.setFont(headerFont);

for(FunctionalChain chain: objects) {
	Sheet sheet = workbook.createSheet(chain.getName());
	int rowNum = 0;
	for (LogicalFunction function: chain.getInvolvedFunctions()) {
		Row row = sheet.createRow(rowNum++);
		Cell cell = row.createCell(0);
		cell.setCellValue(function.getName());
		
		if (function.allocatingLogicalComponents.any { it.actor }) {
			cell.setCellStyle(actorStyle);
		}
	}
	sheet.autoSizeColumn(0);
}

FileOutputStream fileOut = new FileOutputStream(file);
workbook.write(fileOut);
fileOut.close();
workbook.close();
EcoreUtil2.getProject(objects.first()).refreshLocal(IResource.DEPTH_INFINITE, null)
