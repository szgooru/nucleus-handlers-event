package org.gooru.nucleus.handlers.events.bootstrap.startup;

import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.app.components.KafkaRegistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Initializers implements Iterable<Initializer> {


  private List<Initializer> initializers = null;
  private Iterator<Initializer> internalIterator;
  
  @Override
  public Iterator<Initializer> iterator() {
    Iterator<Initializer> iterator = new Iterator<Initializer>() {

      @Override
      public boolean hasNext() {
        return internalIterator.hasNext();
      }

      @Override
      public Initializer next() {
        return internalIterator.next();
      }
      
    };
    return iterator;
  }
  
  public Initializers() {
    initializers = new ArrayList<Initializer>();
    initializers.add(DataSourceRegistry.getInstance());    
    initializers.add(KafkaRegistry.getInstance());    
    internalIterator = initializers.iterator();
  }


}
