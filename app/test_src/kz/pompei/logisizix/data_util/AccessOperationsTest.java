package kz.pompei.logisizix.data_util;

import kz.pompei.logisizix.data_util.test_data.RootTest;
import org.assertj.core.data.Offset;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessOperationsTest {

  @Test
  public void setPathValue__double() {

    RootTest root = new RootTest();

    //
    //
    AccessOperations.setPathValue(root, "x", "2340.45");
    AccessOperations.setPathValue(root, "y", "1234.45");
    AccessOperations.setPathValue(root, "z", "2234.45");
    //
    //

    assertThat(root.x).isEqualTo(2340.45, Offset.offset(0.0001));
    assertThat(root.y).isEqualTo(1234.45, Offset.offset(0.0001));
    assertThat(root.z).isEqualTo(2234.45, Offset.offset(0.0001));
  }

  @Test
  public void setPathValue__map() {

    RootTest root = new RootTest();

    //
    //
    AccessOperations.setPathValue(root, "elements.q12q1.name", "Параллель");
    //
    //

    assertThat(root.elements).isNotNull();
    assertThat(root.elements.get("q12q1")).isNotNull();
    assertThat(root.elements.get("q12q1").name).isEqualTo("Параллель");
  }

  @Test
  public void setPathValue__direct() {

    RootTest root = new RootTest();

    //
    //
    AccessOperations.setPathValue(root, "stone.weight", "371.23");
    //
    //

    assertThat(root.stone).isNotNull();
    assertThat(root.stone.weight).isEqualTo(371.23, Offset.offset(0.0001));
  }

  @Test
  public void setPathValue__map_direct() {

    RootTest root = new RootTest();

    //
    //
    AccessOperations.setPathValue(root, "elements.r4a78.redStone.statusText", "RedStoneIsGoing");
    //
    //

    assertThat(root.elements).isNotNull();
    assertThat(root.elements.get("r4a78")).isNotNull();
    assertThat(root.elements.get("r4a78").redStone).isNotNull();
    assertThat(root.elements.get("r4a78").redStone.statusText).isEqualTo("RedStoneIsGoing");
  }

}
