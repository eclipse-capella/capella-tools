@BaseScript(org.polarsys.capella.groovy.CapellaScriptBase)
package capella.groovy.example

import org.polarsys.capella.core.data.capellacore.CapellaElement
import org.polarsys.capella.core.data.ctx.*
import org.polarsys.capella.core.data.fa.*
import org.polarsys.capella.core.data.la.*
import org.polarsys.capella.core.data.pa.*

import groovy.transform.BaseScript

/*
 * This example shows how to use setPropertyValue and getPropertyValue which are
 * only available in Capella Groovy. The script first sets a random int, float and string
 * property value on each logical function. It then prints out the Logical Functions that
 * have a float value bigger than 0.5.
 */
model('/In-Flight Entertainment System/In-Flight Entertainment System.aird') {
	
  Random ran = new Random();
  LogicalFunction.each {
    it.setPropertyValue("floatValue", ran.nextFloat())
    it.setPropertyValue("intValue", ran.nextInt())
    it.setPropertyValue("stringValue", String.valueOf(ran.nextLong()))

    float value = it.getPropertyValue("floatValue")
    if (value > 0.5) {
        info "$it.name: $value"
    }
  }

}