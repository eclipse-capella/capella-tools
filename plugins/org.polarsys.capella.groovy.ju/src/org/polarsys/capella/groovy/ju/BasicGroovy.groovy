package org.polarsys.capella.groovy.ju;

import org.junit.Test
import org.polarsys.capella.core.data.ctx.CtxFactory
import org.polarsys.capella.core.data.ctx.SystemFunction

import groovy.test.GroovyTestCase;

public class BasicGroovy extends GroovyTestCase {
  
  @Test
  public void testEobject() {
    SystemFunction sf = CtxFactory.eINSTANCE.createSystemFunction("ok");
    assertTrue(sf.name.equals("ok"));
  }
  
}
