package thewrestler.ui;

import thewrestler.WrestlerMod;

public class UiHelper {
  public static String getUiImageResourcePath(String relativePath) {
    return WrestlerMod.getImageResourcePath("ui/" + relativePath);
  }
}
