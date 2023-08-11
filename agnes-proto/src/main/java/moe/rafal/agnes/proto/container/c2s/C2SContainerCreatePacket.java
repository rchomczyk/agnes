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

package moe.rafal.agnes.proto.container.c2s;

import static moe.rafal.agnes.proto.ProtoUtils.packArray;
import static moe.rafal.agnes.proto.ProtoUtils.unpackArray;

import java.io.IOException;
import moe.rafal.agnes.proto.container.ContainerCreationType;
import moe.rafal.cory.Packet;
import moe.rafal.cory.serdes.PacketPacker;
import moe.rafal.cory.serdes.PacketUnpacker;

public class C2SContainerCreatePacket extends Packet {

  private String imageId;
  private String imageTag;
  private long assignedMemory;
  private long assignedMemorySwap;
  private String hostname;
  private String[] exposedPorts;
  private String[] publishPorts;
  private String[] environmentVariables;
  private String[] binds;
  private ContainerCreationType creationType;

  public C2SContainerCreatePacket(
      String imageId,
      String imageTag,
      long assignedMemory,
      long assignedMemorySwap,
      String hostname,
      String[] exposedPorts,
      String[] publishPorts,
      String[] environmentVariables,
      String[] binds,
      ContainerCreationType creationType) {
    this.imageId = imageId;
    this.imageTag = imageTag;
    this.assignedMemory = assignedMemory;
    this.assignedMemorySwap = assignedMemorySwap;
    this.hostname = hostname;
    this.exposedPorts = exposedPorts;
    this.publishPorts = publishPorts;
    this.environmentVariables = environmentVariables;
    this.binds = binds;
    this.creationType = creationType;
  }

  public C2SContainerCreatePacket() {
    super();
  }

  @Override
  public void write(PacketPacker packer) throws IOException {
    packer.packString(imageId);
    packer.packString(imageTag);
    packer.packLong(assignedMemory);
    packer.packLong(assignedMemorySwap);
    packer.packString(hostname);
    packArray(packer, PacketPacker::packString, PacketPacker::packArrayHeader, exposedPorts);
    packArray(packer, PacketPacker::packString, PacketPacker::packArrayHeader, publishPorts);
    packArray(packer, PacketPacker::packString, PacketPacker::packArrayHeader,
        environmentVariables);
    packArray(packer, PacketPacker::packString, PacketPacker::packArrayHeader, binds);
    packer.packString(creationType.name());
  }

  @Override
  public void read(PacketUnpacker unpacker) throws IOException {
    imageId = unpacker.unpackString();
    imageTag = unpacker.unpackString();
    assignedMemory = unpacker.unpackLong();
    assignedMemorySwap = unpacker.unpackLong();
    hostname = unpacker.unpackString();
    exposedPorts = unpackArray(unpacker,
        PacketUnpacker::unpackString,
        PacketUnpacker::unpackArrayHeader, String[]::new);
    publishPorts = unpackArray(unpacker,
        PacketUnpacker::unpackString,
        PacketUnpacker::unpackArrayHeader, String[]::new);
    environmentVariables = unpackArray(unpacker,
        PacketUnpacker::unpackString,
        PacketUnpacker::unpackArrayHeader, String[]::new);
    binds = unpackArray(unpacker,
        PacketUnpacker::unpackString,
        PacketUnpacker::unpackArrayHeader, String[]::new);
    creationType = ContainerCreationType.valueOf(unpacker.unpackString());
  }

  public String getImageId() {
    return imageId;
  }

  public String getImageTag() {
    return imageTag;
  }

  public long getAssignedMemory() {
    return assignedMemory;
  }

  public long getAssignedMemorySwap() {
    return assignedMemorySwap;
  }

  public String getHostname() {
    return hostname;
  }

  public String[] getExposedPorts() {
    return exposedPorts;
  }

  public String[] getPublishPorts() {
    return publishPorts;
  }

  public String[] getEnvironmentVariables() {
    return environmentVariables;
  }

  public String[] getBinds() {
    return binds;
  }

  public ContainerCreationType getCreationType() {
    return creationType;
  }
}
