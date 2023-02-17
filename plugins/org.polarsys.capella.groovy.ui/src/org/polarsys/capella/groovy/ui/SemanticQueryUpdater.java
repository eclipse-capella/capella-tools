package org.polarsys.capella.groovy.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.polarsys.capella.common.helpers.query.IQuery;
import org.polarsys.capella.common.ui.toolkit.browser.category.CategoryImpl;
import org.polarsys.capella.common.ui.toolkit.browser.category.CategoryRegistry;
import org.polarsys.capella.common.ui.toolkit.browser.category.ICategory;
import org.polarsys.capella.common.utils.ReflectUtil;
import org.polarsys.capella.groovy.lang.SemanticQuery;
import org.polarsys.capella.groovy.lang.SemanticSection;

public class SemanticQueryUpdater {

  protected HashMap<String, ICategory> getMap(SemanticSection section)
      throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    switch (section) {
    case REFERENCED:
      return (HashMap<String, ICategory>) ReflectUtil.getInvisibleFieldValue(CategoryRegistry.getInstance(),
          "referencedElementRegistry");
    case CURRENT:
      return (HashMap<String, ICategory>) ReflectUtil.getInvisibleFieldValue(CategoryRegistry.getInstance(),
          "currentElementRegistry");
    case REFERENCING:
      return (HashMap<String, ICategory>) ReflectUtil.getInvisibleFieldValue(CategoryRegistry.getInstance(),
          "referencingElementRegistry");
    }
    return new HashMap<>();
  }

  private void removeGroovyQueries(HashMap<String, ICategory> map2) {
    for (String key : new ArrayList<String>(map2.keySet())) {
      if (key.startsWith("groovy.")) {
        System.out.println("Removed : " + key);
        map2.remove(key);
      }
    }
  }

  public void removeGroovyQueries() {
    try {
      removeGroovyQueries(getMap(SemanticSection.CURRENT));
      removeGroovyQueries(getMap(SemanticSection.REFERENCED));
      removeGroovyQueries(getMap(SemanticSection.REFERENCING));
    } catch (Exception e1) {
      e1.printStackTrace();
    }
  }

  private class Query implements IQuery {
    Method m;

    Query(Method m) {
      this.m = m;
    }

    @Override
    public List<Object> compute(Object object) {
      try {
        Object root = m.getDeclaringClass().getDeclaredConstructor().newInstance();
        Object a = m.invoke(root, object);
        if (a instanceof List) {
          return (List<Object>) a;
        }
        return Arrays.asList(a);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (SecurityException e) {
        e.printStackTrace();
      }
      return new ArrayList<>();
    }
  }

  public void createQueries(Class<?> cl) {
    for (final Method m : cl.getMethods()) {
      if (m.isAnnotationPresent(SemanticQuery.class)) {
        try {

          String name = m.getAnnotation(SemanticQuery.class).name();
          String id = "groovy." + m.getName();
          System.out.println("Registered : " + id);
          HashMap<String, ICategory> map = getMap(m.getAnnotation(SemanticQuery.class).section());

          CategoryImpl cat = new CategoryImpl();
          cat.setId(id);
          cat.setIsTopLevel(true);
          cat.setName(name);
          cat.setTypeFullyQualifiedName(m.getParameterTypes()[0].getCanonicalName());
          cat.setQuery(new Query(m));

          map.put("groovy." + id, cat);

        } catch (NoSuchFieldException e1) {
          e1.printStackTrace();
        } catch (IllegalArgumentException e1) {
          e1.printStackTrace();
        } catch (IllegalAccessException e1) {
          e1.printStackTrace();
        }
      }
    }
  }
}
