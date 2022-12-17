package kz.pompei.logisizix.data_util.accessor;

import java.lang.reflect.Type;

public interface Setter {
  void set(Object object, Object value);

  Type setterType();
}
