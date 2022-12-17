package kz.pompei.logisizix.data_util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import kz.pompei.logisizix.data_util.accessor.Accessor;
import kz.pompei.logisizix.data_util.accessor.Getter;
import kz.pompei.logisizix.data_util.accessor.Ignore;
import kz.pompei.logisizix.data_util.accessor.Setter;
import lombok.SneakyThrows;

public class ClassAccessors {

  private static final ConcurrentHashMap<Class<?>, Map<String, Accessor>> accessors = new ConcurrentHashMap<>();

  public static Map<String, Accessor> accessorsFor(Class<?> aClass) {
    {
      final Map<String, Accessor> accessorMap = accessors.get(aClass);
      if (accessorMap != null) {
        return accessorMap;
      }
    }

    synchronized (accessors) {
      {
        final Map<String, Accessor> accessorMap = accessors.get(aClass);
        if (accessorMap != null) {
          return accessorMap;
        }
      }

      final Map<String, Accessor> accessorMap = createAccessorMap(aClass);
      accessors.put(aClass, accessorMap);
      return accessorMap;
    }

  }

  private static String firstToLowCase(String str) {
    return str.length() == 0 ? "" : str.substring(0, 1).toLowerCase() + str.substring(1);
  }

  private static Map<String, Accessor> createAccessorMap(Class<?> aClass) {

    Map<String, Getter> getters = new HashMap<>();
    Map<String, Setter> setters = new HashMap<>();

    for (final Field field : aClass.getFields()) {

      if (field.getAnnotation(Ignore.class) != null) {
        continue;
      }

      final String fieldName = field.getName();

      getters.put(fieldName, new Getter() {
        @Override
        @SneakyThrows
        public Object get(Object object) {
          return field.get(object);
        }

        @Override
        public Type getterType() {
          return field.getGenericType();
        }
      });

      setters.put(fieldName, new Setter() {
        @Override
        @SneakyThrows
        public void set(Object object, Object value) {
          field.set(object, value);
        }

        @Override
        public Type setterType() {
          return field.getGenericType();
        }
      });

    }

    for (final Method method : aClass.getMethods()) {

      if (method.getAnnotation(Ignore.class) != null) {
        continue;
      }

      final String methodName = method.getName();

      if (methodName.length() >= 3 && methodName.startsWith("is")
        && (method.getReturnType() == Boolean.class || method.getReturnType() == boolean.class)
        && method.getParameterTypes().length == 0) {

        String fieldName = firstToLowCase(methodName.substring(2));

        getters.put(fieldName, new Getter() {
          @Override
          @SneakyThrows
          public Object get(Object object) {
            return method.invoke(object);
          }

          @Override
          public Class<?> getterType() {
            return method.getReturnType();
          }
        });
        continue;
      }

      if (methodName.length() > 3 && methodName.startsWith("get") && method.getParameterTypes().length == 0) {
        String fieldName = firstToLowCase(methodName.substring(3));

        getters.put(fieldName, new Getter() {
          @Override
          @SneakyThrows
          public Object get(Object object) {
            return method.invoke(object);
          }

          @Override
          public Class<?> getterType() {
            return method.getReturnType();
          }
        });

        continue;
      }

      if (methodName.length() > 3 && methodName.startsWith("set") && method.getParameterTypes().length == 1) {
        String fieldName = firstToLowCase(methodName.substring(3));

        setters.put(fieldName, new Setter() {
          @Override
          @SneakyThrows
          public void set(Object object, Object value) {
            method.invoke(object, value);
          }

          @Override
          public Class<?> setterType() {
            return method.getParameterTypes()[0];
          }
        });

        continue;
      }

    }

    Map<String, Accessor> result = new HashMap<>();

    for (final Map.Entry<String, Getter> e : getters.entrySet()) {
      final String fieldName = e.getKey();
      final Getter getter    = e.getValue();
      final Setter setter    = setters.get(fieldName);
      if (setter == null) {
        continue;
      }

      if (getter.getterType() != setter.setterType()) {
        throw new RuntimeException("o61xd8p1sl :: Different types of setter and getter, MUST be same: `" + fieldName + "`"
                                     + " in " + aClass.getSimpleName() + " : " + aClass.getName());
      }

      result.put(fieldName, new Accessor() {
        @Override
        public Object get(Object object) {
          return getter.get(object);
        }

        @Override
        public Type getterType() {
          return getter.getterType();
        }

        @Override
        public void set(Object object, Object value) {
          setter.set(object, value);
        }

        @Override
        public Type setterType() {
          return setter.setterType();
        }
      });
    }

    return Map.copyOf(result);
  }

}
