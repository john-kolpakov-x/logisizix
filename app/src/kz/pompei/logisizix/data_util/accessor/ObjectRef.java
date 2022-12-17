package kz.pompei.logisizix.data_util.accessor;

import java.lang.reflect.Type;
import lombok.NonNull;

public record ObjectRef(@NonNull Type type, @NonNull Object ref) {
  public static ObjectRef ofClass(@NonNull Object object) {
    return new ObjectRef(object.getClass(), object);
  }
}
