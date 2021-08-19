package net.torocraft.torohealth.config.loader;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class ColorJsonAdapter extends TypeAdapter<Integer> {

  @Override
  public void write(JsonWriter out, Integer value) throws IOException {
    String hex = String.format("#%06x", value & 0xFFFFFF);
    out.value(hex);
  }

  @Override
  public Integer read(JsonReader in) throws IOException {
    return parseColor(in.nextString());
  }

  private static Integer parseColor(String s) {
    if (s.matches("^#[A-Fa-f0-9]{6}$")) {
      return Integer.parseInt(s.substring(1), 16);
    }
    System.out.println("ToroHealth: failed to parse color [" + s + "]");
    return null;
  }

}
