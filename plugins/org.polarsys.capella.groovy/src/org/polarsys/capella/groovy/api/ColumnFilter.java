package org.polarsys.capella.groovy.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.polarsys.kitalpha.massactions.core.column.IMAColumn;
import org.polarsys.kitalpha.massactions.core.extensionpoint.columnfilter.IMAColumnFilter;

public class ColumnFilter implements IMAColumnFilter {

  public Collection<String> columns = new LinkedHashSet<String>();
  public ColumnFilter(String ... ids ) {
    columns.addAll(Arrays.asList(ids));
  }
  public ColumnFilter( ) {
    columns.addAll(Arrays.asList());
  }
  @Override
  public boolean shouldHide(IMAColumn column) {
    return !columns.contains(column.getId());
  }

  @Override
  public void setBaseColumns(List<IMAColumn> baseColumns) {
    
  }

  @Override
  public void setDisplayName(String displayName) {
    
  }

  @Override
  public String getDisplayName() {
    return "Groovy";
  }

}
