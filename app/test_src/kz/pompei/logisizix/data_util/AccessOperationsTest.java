package kz.pompei.logisizix.data_util;

import java.util.Map;
import kz.pompei.logisizix.data_util.test_data.RootTest;
import org.assertj.core.data.Offset;
import org.testng.annotations.Test;

import static kz.pompei.logisizix.data_util.AccessOperations.toTargetClass;
import static org.assertj.core.api.Assertions.assertThat;

public class AccessOperationsTest {

  @Test
  public void toTargetClass__double() {

    //
    //
    final RootTest root = toTargetClass(RootTest.class, Map.of("x", "2340.45", "y", "1234.45", "z", "2234.45"));
    //
    //

    assertThat(root.x).isEqualTo(2340.45, Offset.offset(0.0001));
    assertThat(root.y).isEqualTo(1234.45, Offset.offset(0.0001));
    assertThat(root.z).isEqualTo(2234.45, Offset.offset(0.0001));
  }

  @Test
  public void toTargetClass__map() {

    //
    //
    final RootTest root = toTargetClass(RootTest.class, Map.of("elements.q12q1.name", "Параллель"));
    //
    //

    assertThat(root.elements).isNotNull();
    assertThat(root.elements.get("q12q1")).isNotNull();
    assertThat(root.elements.get("q12q1").name).isEqualTo("Параллель");
  }

  @Test
  public void toTargetClass__direct() {

    //
    //
    final RootTest root = toTargetClass(RootTest.class, Map.of("stone.weight", "371.23"));
    //
    //

    assertThat(root.stone).isNotNull();
    assertThat(root.stone.weight).isEqualTo(371.23, Offset.offset(0.0001));
  }

  @Test
  public void toTargetClass__map_direct() {

    //
    //
    final RootTest root = toTargetClass(RootTest.class, Map.of("elements.r4a78.redStone.statusText", "RedStoneIsGoing"));
    //
    //

    assertThat(root.elements).isNotNull();
    assertThat(root.elements.get("r4a78")).isNotNull();
    assertThat(root.elements.get("r4a78").redStone).isNotNull();
    assertThat(root.elements.get("r4a78").redStone.statusText).isEqualTo("RedStoneIsGoing");
  }

  @Test
  public void toTargetClass__tripleElements() {

    //
    //
    final RootTest root = toTargetClass(RootTest.class, Map.of("tripleElements.a11.b11.c11.redStone.weight", "345.67"));
    //
    //

    assertThat(root.tripleElements).isNotNull();
    assertThat(root.tripleElements.get("a11")).isNotNull();
    assertThat(root.tripleElements.get("a11").get("b11")).isNotNull();
    assertThat(root.tripleElements.get("a11").get("b11").get("c11")).isNotNull();
    assertThat(root.tripleElements.get("a11").get("b11").get("c11").redStone).isNotNull();
    assertThat(root.tripleElements.get("a11").get("b11").get("c11").redStone.weight).isEqualTo(345.67, Offset.offset(0.00001));
  }

}
