/*
 *    Copyright 2023 agnes
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package moe.rafal.agnes.proto;

import com.pivovarit.function.ThrowingBiConsumer;
import com.pivovarit.function.ThrowingFunction;
import java.io.IOException;
import java.util.function.IntFunction;
import moe.rafal.cory.serdes.PacketPacker;
import moe.rafal.cory.serdes.PacketUnpacker;

public final class ProtoUtils {

  public static final String PROTO_BROADCAST_CHANNEL_NAME = "agnes_proto";

  private ProtoUtils() {

  }

  public static <T> void packArray(PacketPacker packer,
      ThrowingBiConsumer<PacketPacker, T, IOException> packValue,
      ThrowingBiConsumer<PacketPacker, Integer, IOException> packHeader,
      T[] values)
      throws IOException {
    packHeader.accept(packer, values.length);
    for (T value : values) {
      packValue.accept(packer, value);
    }
  }

  public static <T> T[] unpackArray(PacketUnpacker unpacker,
      ThrowingFunction<PacketUnpacker, T, IOException> unpackValue,
      ThrowingFunction<PacketUnpacker, Integer, IOException> unpackHeader,
      IntFunction<T[]> arrayResolver)
      throws IOException {
    final int elementCount = unpackHeader.apply(unpacker);
    final T[] elements = arrayResolver.apply(elementCount);
    for (int currentIndex = 0; currentIndex < elementCount; currentIndex++) {
      elements[currentIndex] = unpackValue.apply(unpacker);
    }
    return elements;
  }
}
