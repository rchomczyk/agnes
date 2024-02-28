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

import java.time.Instant;
import java.util.Objects;
import moe.rafal.cory.pojo.PojoPacket;

public class S2CContainerInspectPacket extends PojoPacket {

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
      Instant startedAt
  ) {
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

  public String getContainerId() {
    return containerId;
  }

  public void setContainerId(final String containerId) {
    this.containerId = containerId;
  }

  public String getImageHash() {
    return imageHash;
  }

  public void setImageHash(final String imageHash) {
    this.imageHash = imageHash;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(final String address) {
    this.address = address;
  }

  public long getAssignedMemory() {
    return assignedMemory;
  }

  public void setAssignedMemory(final long assignedMemory) {
    this.assignedMemory = assignedMemory;
  }

  public long getAssignedMemorySwap() {
    return assignedMemorySwap;
  }

  public void setAssignedMemorySwap(final long assignedMemorySwap) {
    this.assignedMemorySwap = assignedMemorySwap;
  }

  public int getPort() {
    return port;
  }

  public void setPort(final int port) {
    this.port = port;
  }

  public Instant getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(final Instant startedAt) {
    this.startedAt = startedAt;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }

    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    if (!super.equals(object)) {
      return false;
    }

    final S2CContainerInspectPacket that = (S2CContainerInspectPacket) object;
    return assignedMemory == that.assignedMemory
        && assignedMemorySwap == that.assignedMemorySwap
        && port == that.port
        && Objects.equals(containerId, that.containerId)
        && Objects.equals(imageHash, that.imageHash)
        && Objects.equals(address, that.address)
        && Objects.equals(startedAt, that.startedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), containerId, imageHash, address, assignedMemory,
        assignedMemorySwap, port, startedAt);
  }
}
