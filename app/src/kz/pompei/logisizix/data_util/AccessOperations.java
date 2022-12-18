package kz.pompei.logisizix.data_util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;

public class AccessOperations {

  @SneakyThrows
  public static <T> T toTargetClass(Class<T> targetClass, Map<String, String> pathValues) {

    Map<String, Object> mapData = new HashMap<>();

    setValuesToMap(mapData, pathValues);

    ObjectMapper objectMapper = new ObjectMapper();

    final String jsonStr = objectMapper.writeValueAsString(mapData);

    return objectMapper.readValue(jsonStr, targetClass);
  }

  private static void setValuesToMap(Map<String, Object> mapData, Map<String, String> pathValues) {

    for (final Map.Entry<String, String> e : pathValues.entrySet()) {
      final String path     = e.getKey();
      final String strValue = e.getValue();
      setValueToMap(mapData, path, strValue);
    }

  }

  private static void setValueToMap(Map<String, Object> mapData, String path, String strValue) {

    final List<String> pathList = Arrays.stream(path.split("\\.")).toList();

    final Map<String, Object> lastMap = pavePath(mapData, pathList.subList(0, pathList.size() - 1));

    lastMap.put(pathList.get(pathList.size() - 1), strValue);

  }

  private static Map<String, Object> pavePath(Map<String, Object> mapData, List<String> subList) {

    Map<String, Object> current = mapData;

    for (final String pathElement : subList) {

      final Object o = current.get(pathElement);
      if (o instanceof Map) {
        //noinspection unchecked
        current = (Map<String, Object>) o;
        continue;
      }

      final HashMap<String, Object> next = new HashMap<>();
      current.put(pathElement, next);

      current = next;
    }

    return current;
  }

}
