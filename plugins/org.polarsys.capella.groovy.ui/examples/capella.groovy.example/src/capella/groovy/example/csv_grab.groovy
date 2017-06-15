/**
 * This example shows how you can use Groovys @Grab annotation to use existing java libraries
 * on demand. The libraries will be downloaded when the script runs, so you only have to deploy
 * the script itself, the rest is automatic.
 * For more grab information, see http://docs.groovy-lang.org/latest/html/documentation/grape.html
 *
 * Here we use the apache commons csv library to export physical functions and their descriptions
 */
package capella.groovy.example

@BaseScript(org.polarsys.capella.groovy.CapellaScriptBase)
@Grab(group='org.apache.commons', module='commons-csv', version='1.1')

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.NullProgressMonitor
import org.polarsys.capella.core.data.pa.PhysicalFunction

import groovy.transform.BaseScript

String project = 'In-Flight Entertainment System/'
String aird = 'In-Flight Entertainment System.aird'

model(project + aird) {

    CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.EXCEL)
    printer.printRecords PhysicalFunction.collect { [ it.name, it.description ] }
    printer.flush()

    // TODO, make it easier to write files to the workspace
    ByteArrayInputStream bis = new ByteArrayInputStream(printer.getOut().toString().getBytes());
    IFile out = ResourcesPlugin.getWorkspace().getRoot().getFile(project + 'csv.txt')
    if (!out.exists()){
        out.create(bis, 0, new NullProgressMonitor());
    } else {
        out.setContents(bis, 0, new NullProgressMonitor())
    }
}