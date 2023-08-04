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

package moe.rafal.agnes.proto.container.s2c;

import java.io.IOException;
import moe.rafal.cory.Packet;
import moe.rafal.cory.serdes.PacketPacker;
import moe.rafal.cory.serdes.PacketUnpacker;

public class S2CContainerStartPacket extends Packet {

  private String containerId;

  public S2CContainerStartPacket(String containerId) {
    super();
    this.containerId = containerId;
  }

  public S2CContainerStartPacket() {
    super();
  }

  @Override
  public void write(PacketPacker packer) throws IOException {
    packer.packString(containerId);
  }

  @Override
  public void read(PacketUnpacker unpacker) throws IOException {
    containerId = unpacker.unpackString();
  }

  public String getContainerId() {
    return containerId;
  }
}
