package org.polarsys.capella.groovy;

import org.polarsys.capella.common.helpers.query.IQuery;
import org.polarsys.capella.common.ui.toolkit.browser.category.ICategory;
import org.polarsys.capella.common.utils.ReflectUtil;

import com.google.common.base.CaseFormat;

public class CategoryEntry {
  ICategory category;
  
  public CategoryEntry(ICategory cat) {
    this.category = cat;
  }
  
  public String getName() {
    return category.getName();
  }
  
  public IQuery getQuery() {
    try {
      return (IQuery) ReflectUtil.getInvisibleFieldValue(category, "categoryQuery");
    } catch(Exception e) {
      return null;
    }
  }
  
  public String getIdentifier() {
    return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, getName().trim().replaceAll(" ", "_"));
  }

}
