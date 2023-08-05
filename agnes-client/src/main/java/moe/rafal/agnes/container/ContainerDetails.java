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

package moe.rafal.agnes.container;

import java.time.Instant;

public class ContainerDetails {

  private final String imageHash;
  private final String address;
  private final long assignedMemory;
  private final long assignedMemorySwap;
  private final int port;
  private final Instant startedAt;

  public ContainerDetails(
      String imageHash,
      String address,
      long assignedMemory,
      long assignedMemorySwap,
      int port,
      Instant startedAt) {
    this.imageHash = imageHash;
    this.address = address;
    this.assignedMemory = assignedMemory;
    this.assignedMemorySwap = assignedMemorySwap;
    this.port = port;
    this.startedAt = startedAt;
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
