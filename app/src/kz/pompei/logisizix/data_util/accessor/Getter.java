package kz.pompei.logisizix.data_util.accessor;

import java.lang.reflect.Type;

public interface Getter {
  Object get(Object object);

  Type getterType();
}
