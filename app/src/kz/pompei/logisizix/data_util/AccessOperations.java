package kz.pompei.logisizix.data_util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kz.pompei.logisizix.data_util.accessor.Accessor;
import kz.pompei.logisizix.data_util.accessor.ObjectRef;
import lombok.SneakyThrows;

import static java.util.Objects.requireNonNull;

public class AccessOperations {
  public static void setPathValue(Object target, String path, String value) {
    requireNonNull(target, "C13t6o0f82 :: target");

    final List<String> pathList = Arrays.stream(path.split("\\.")).toList();

    ObjectRef current = ObjectRef.ofClass(target);

    for (int i = 0; current != null && i < pathList.size() - 1; i++) {
      current = getOrCreate(current, pathList.get(i));
    }

    if (current != null) {
      setStrValueTo(current.ref(), pathList.get(pathList.size() - 1), value);
    }
  }

  private static void setStrValueTo(Object current, String fieldName, String strValue) {
    final Map<String, Accessor> accessorMap = ClassAccessors.accessorsFor(current.getClass());

    final Accessor accessor = accessorMap.get(fieldName);

    if (accessor == null) {
      return;
    }

    final Object value = convertStrValueToType(strValue, accessor.setterType());

    accessor.set(current, value);
  }

  private static Object convertStrValueToType(String strValue, Type setterType) {
    if (setterType == Double.class) {
      return strValue == null ? null : Double.valueOf(strValue);
    }
    if (setterType == double.class) {
      return strValue == null ? 0d : Double.parseDouble(strValue);
    }
    throw new RuntimeException("ff870Vvq2u :: Cannot convert to type `" + setterType + "` of str value = `" + strValue + "`");
  }

  private static ObjectRef getOrCreate(ObjectRef current, String pathElement) {

    final Object currentRef = current.ref();

    final Map<String, Accessor> accessorMap = ClassAccessors.accessorsFor(currentRef.getClass());

    final Accessor accessor = accessorMap.get(pathElement);
    if (accessor == null) {
      return null;
    }

    final Type type = accessor.getterType();
    {
      final Object result = accessor.get(currentRef);
      if (result != null) {
        return new ObjectRef(type, result);
      }
    }

    {
      final Object newInstance = createInstanceOf(type);
      accessor.set(currentRef, newInstance);
      return new ObjectRef(type, newInstance);
    }
  }

  @SneakyThrows
  private static Object createInstanceOf(Type type) {
    //noinspection rawtypes
    if (type instanceof Class c) {
      //noinspection unchecked
      return c.getConstructor().newInstance();
    }

    if (type instanceof ParameterizedType pt) {
      if (pt.getRawType() == Map.class) {
        //noinspection rawtypes
        return new HashMap();
      }
    }

    throw new RuntimeException("b670azSY0G :: Cannot create instance of " + type);
  }
}
