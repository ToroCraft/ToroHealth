package net.torocraft.torohealth.config.loader;

import com.google.common.base.CharMatcher;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class ColorJsonAdpater extends TypeAdapter<Integer> {

  private static final CharMatcher HEX = CharMatcher.anyOf("0123456789abcdefABCDEF");

  @Override
  public void write(JsonWriter out, Integer value) throws IOException {
    String hex = String.format("#%06x", value & 0xFFFFFF);
    out.value(hex);
  }

  @Override
  public Integer read(JsonReader in) throws IOException {
    String read = in.nextString();
    if (read.charAt(0) != '#' || read.length() != 7 || !HEX.matchesAllOf(read.substring(1))) {
      System.out.println("ToroHealth: failed to parse color [" + read + "]");
      return null;
    }
    return Integer.parseInt(read.substring(1), 16);
  }

}
