package net.torocraft.torohealth.config.loader;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.awt.Color;
import java.io.IOException;

public class ColorJsonAdpater extends TypeAdapter<Integer> {

  @Override
  public void write(JsonWriter out, Integer value) throws IOException {
    String hex = Integer.toHexString(value & 0xffffff);
    hex = String.format("#%1$6s", hex).replace(' ', '0');
    out.value(hex);
  }

  @Override
  public Integer read(JsonReader in) throws IOException {
    String read = in.nextString();
    try {
      Color c = Color.decode(read);
      return c.getRGB();
    } catch (Exception e) {
      System.out.println("ToroHealth: failed to parse color [" + read + "]");
      e.printStackTrace();
      return null;
    }
  }

}
