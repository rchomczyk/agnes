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
import java.time.Instant;
import moe.rafal.cory.Packet;
import moe.rafal.cory.serdes.PacketPacker;
import moe.rafal.cory.serdes.PacketUnpacker;

public class S2CContainerInspectPacket extends Packet {

  private String containerId;
  private String imageHash;
  private String address;
  private long assignedMemory;
  private long assignedMemorySwap;
  private int port;
  private Instant startedAt;

  public S2CContainerInspectPacket(
      String containerId,
      String imageHash,
      String address,
      long assignedMemory,
      long assignedMemorySwap,
      int port,
      Instant startedAt) {
    super();
    this.containerId = containerId;
    this.imageHash = imageHash;
    this.address = address;
    this.assignedMemory = assignedMemory;
    this.assignedMemorySwap = assignedMemorySwap;
    this.port = port;
    this.startedAt = startedAt;
  }

  public S2CContainerInspectPacket() {
    super();
  }

  @Override
  public void write(PacketPacker packer) throws IOException {
    packer.packString(containerId);
    packer.packString(imageHash);
    packer.packString(address);
    packer.packLong(assignedMemory);
    packer.packLong(assignedMemorySwap);
    packer.packInt(port);
    packer.packInstant(startedAt);
  }

  @Override
  public void read(PacketUnpacker unpacker) throws IOException {
    containerId = unpacker.unpackString();
    imageHash = unpacker.unpackString();
    address = unpacker.unpackString();
    assignedMemory = unpacker.unpackLong();
    assignedMemorySwap = unpacker.unpackLong();
    port = unpacker.unpackInt();
    startedAt = unpacker.unpackInstant();
  }

  public String getContainerId() {
    return containerId;
  }

  public String getImageHash() {
    return imageHash;
  }

  public String getAddress() {
    return address;
  }

  public long getAssignedMemory() {
    return assignedMemory;
  }

  public long getAssignedMemorySwap() {
    return assignedMemorySwap;
  }

  public int getPort() {
    return port;
  }

  public Instant getStartedAt() {
    return startedAt;
  }
}
