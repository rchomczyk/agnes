package moe.rafal.agnes;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * The file size human-readable utility class, provide mutual conversions from human-readable size
 * to byte size
 * <p>
 * The similar function in stackoverflow, linked: <a
 * href="https://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java?r=SearchResults">stackoverflow
 * question.</a>
 * <p>
 * Apache also provides similar function
 *
 * @author Ponfee
 */
enum HumanReadables {

  SI(1000, "B", "KB", "MB", "GB", "TB", "PB", "EB"),
  BINARY(1024, "B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB");

  private static final String FORMAT = "#,##0.##";
  private static final Pattern PATTERN = Pattern.compile(".*\\d+.*");

  private final int base;
  private final String[] units;
  private final long[] sizes;

  HumanReadables(int base, String... units) {
    this.base = base;
    this.units = units;
    this.sizes = new long[this.units.length];

    this.sizes[0] = 1;
    for (int i = 1; i < this.sizes.length; i++) {
      this.sizes[i] = this.sizes[i - 1] * this.base;
    }
  }

  /**
   * Returns a string of bytes counts human-readable size
   *
   * @param size the size
   * @return human-readable size
   */
  public strictfp String human(long size) {
    if (size == 0) {
      return "0" + this.units[0];
    }

    String signed = "";
    if (size < 0) {
      signed = "-";
      size = size == Long.MIN_VALUE ? Long.MAX_VALUE : -size;
    }

    int unit = find(size);
    return
        signed
        + formatter().format(size / (double) this.sizes[unit])
        + " "
        + this.units[unit];
  }

  public strictfp long parse(String size) {
    return parse(size, false);
  }

  /**
   * Parse the readable byte count, allowed suffix units: "1", "1B", "1MB", "1MiB", "1M"
   *
   * @param size   the size
   * @param strict the strict, if BINARY then verify whether contains "i"
   * @return a long value bytes count
   */
  public strictfp long parse(String size, boolean strict) {
    if (size == null || size.isEmpty()) {
      return 0L;
    }
    if (!PATTERN.matcher(size).matches()) {
      throw new IllegalArgumentException("Invalid format [" + size + "]");
    }

    String str = size = size.trim();
    long factor = this.sizes[0];
    switch (str.charAt(0)) {
      case '+':
        str = str.substring(1);
        break;
      case '-':
        str = str.substring(1);
        factor = -1L;
        break;
    }

    int end = 0;
    int lastPos = str.length() - 1;

    char c = str.charAt(lastPos - end);
    if (c == 'i') {
      throw new IllegalArgumentException("Invalid format [" + size + "], cannot end with \"i\".");
    }

    if (c == 'B') {
      end++;
      c = str.charAt(lastPos - end);

      boolean flag = isBlank(c);
      while (isBlank(c) && end < lastPos) {
        end++;
        c = str.charAt(lastPos - end);
      }

      if (flag && !Character.isDigit(c)) {
        throw new IllegalArgumentException("Invalid format [" + size + "]: \"" + c + "\".");
      }
    }

    if (!Character.isDigit(c)) {
      if (c == 'i') {
        if (this == SI) {
          throw new IllegalArgumentException(
              "Invalid SI format [" + size + "], cannot contains \"i\".");
        }
        end++;
        c = str.charAt(lastPos - end);
      } else {
        if (this == BINARY && strict) {
          throw new IllegalArgumentException(
              "Invalid BINARY format [" + size + "], miss character \"i\".");
        }
      }

      switch (c) {
        case 'K':
          factor *= this.sizes[1];
          break;
        case 'M':
          factor *= this.sizes[2];
          break;
        case 'G':
          factor *= this.sizes[3];
          break;
        case 'T':
          factor *= this.sizes[4];
          break;
        case 'P':
          factor *= this.sizes[5];
          break;
        case 'E':
          factor *= this.sizes[6];
          break;
        default:
          throw new IllegalArgumentException("Invalid format [" + size + "]: \"" + c + "\".");
      }

      do {
        end++;
        c = str.charAt(lastPos - end);
      } while (isBlank(c) && end < lastPos);
    }

    str = str.substring(0, str.length() - end);
    try {
      return (long) (factor * formatter().parse(str).doubleValue());
    } catch (NumberFormatException | ParseException exception) {
      throw new IllegalArgumentException("Failed to parse [" + size + "]: \"" + str + "\".");
    }
  }

  public int base() {
    return this.base;
  }

  public String[] units() {
    return Arrays.copyOf(this.units, this.units.length);
  }

  public long[] sizes() {
    return Arrays.copyOf(this.sizes, this.sizes.length);
  }

  private int find(long bytes) {
    int n = this.sizes.length;
    for (int i = 1; i < n; i++) {
      if (bytes < this.sizes[i]) {
        return i - 1;
      }
    }
    return n - 1;
  }

  private DecimalFormat formatter() {
    return new DecimalFormat(FORMAT);
  }

  private boolean isBlank(char c) {
    return c == ' ' || c == '\t';
  }
}