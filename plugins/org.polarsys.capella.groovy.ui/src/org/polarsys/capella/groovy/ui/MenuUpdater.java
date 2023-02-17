package org.polarsys.capella.groovy.ui;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

public class MenuUpdater {

  public static HashMap<String, ArrayList<MenuEntry>> MENUS = new HashMap<String, ArrayList<MenuEntry>>();

  public void registrerMenus(IFile capellaScriptFile, Class<?> cl) {

    System.out.println(cl);

    String location = "additions";
    String scriptName = capellaScriptFile.getName();
    String applyOn = "Object.class";
    Class<?> clazz = Object.class;
    try {
      String script = new String(capellaScriptFile.getContents().readAllBytes(), StandardCharsets.UTF_8);

      try {
        Pattern pSection = Pattern.compile("@ContextualScript ?\\([^\\)]*section ?= ?\"([^\"]*)\"");
        Matcher m = pSection.matcher(script);
        m.find();
        location = m.group(1);
      } catch (Exception e) {
        System.err.println(e);
      }
      try {
        Pattern pName = Pattern.compile("@ContextualScript ?\\([^\\)]*name ?= ?\"([^\"]*)\"");
        Matcher m2 = pName.matcher(script);
        m2.find();
        scriptName = m2.group(1);
      } catch (Exception e) {
        System.err.println(e);
      }
      try {
        Pattern pApplyOn = Pattern.compile("@ContextualScript ?\\([^\\)]*applyOn ?= ?([^\\),\n]+)\\.class");
        Matcher m3 = pApplyOn.matcher(script);
        m3.find();
        applyOn = m3.group(1);

        Pattern importApplyOn = Pattern.compile("import ([^\n]+" + applyOn + ")");
        Matcher m4 = importApplyOn.matcher(script);
        m4.find();
        applyOn = m4.group(1);
        clazz = Class.forName(applyOn);
        System.out.println();
      } catch (Exception e) {
        System.err.println(e);
      }

      if (!MENUS.containsKey(location)) {
        MENUS.put(location, new ArrayList<MenuEntry>());
      }
      MENUS.get(location).add(new MenuEntry(capellaScriptFile, scriptName, clazz));

    } catch (IOException e1) {
      e1.printStackTrace();
    } catch (CoreException e1) {
      e1.printStackTrace();
    } catch (IllegalArgumentException e1) {
      e1.printStackTrace();
    }
  }

  public void removeGroovyMenus() {
    MENUS.clear();
  }

}
