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

package moe.rafal.agnes;

import static moe.rafal.agnes.proto.ProtoUtils.PROTO_BROADCAST_CHANNEL_NAME;

import java.util.concurrent.CompletableFuture;
import moe.rafal.agnes.container.ContainerDetails;
import moe.rafal.agnes.container.specification.ContainerSpecification;
import moe.rafal.agnes.proto.container.c2s.C2SContainerCreatePacket;
import moe.rafal.agnes.proto.container.c2s.C2SContainerDeletePacket;
import moe.rafal.agnes.proto.container.c2s.C2SContainerInspectPacket;
import moe.rafal.agnes.proto.container.c2s.C2SContainerStartPacket;
import moe.rafal.agnes.proto.container.c2s.C2SContainerStopPacket;
import moe.rafal.agnes.proto.container.s2c.S2CContainerCreatePacket;
import moe.rafal.agnes.proto.container.s2c.S2CContainerInspectPacket;
import moe.rafal.agnes.proto.container.s2c.S2CContainerStartPacket;
import moe.rafal.cory.Cory;

class AgnesImpl implements Agnes {

  private final Cory cory;

  AgnesImpl(Cory cory) {
    this.cory = cory;
  }

  @Override
  public CompletableFuture<ContainerDetails> inspectContainer(String containerId) {
    return cory.request(PROTO_BROADCAST_CHANNEL_NAME, new C2SContainerInspectPacket(containerId))
        .thenApply(S2CContainerInspectPacket.class::cast)
        .thenApply(this::fromContainerInspectPacket);
  }

  @Override
  public CompletableFuture<String> createContainer(ContainerSpecification containerSpecification) {
    return cory.request(PROTO_BROADCAST_CHANNEL_NAME,
            intoCreateContainerPacket(containerSpecification))
        .thenApply(S2CContainerCreatePacket.class::cast)
        .thenApply(S2CContainerCreatePacket::getContainerId);
  }

  @Override
  public CompletableFuture<Void> deleteContainer(String containerId) {
    return cory.request(PROTO_BROADCAST_CHANNEL_NAME, new C2SContainerDeletePacket(containerId))
        .thenAccept(packet -> {
        });
  }

  @Override
  public CompletableFuture<String> startContainer(String containerId) {
    return cory.request(PROTO_BROADCAST_CHANNEL_NAME, new C2SContainerStartPacket(containerId))
        .thenApply(S2CContainerStartPacket.class::cast)
        .thenApply(S2CContainerStartPacket::getContainerId);
  }

  @Override
  public CompletableFuture<Void> stopContainer(String containerId) {
    return cory.request(PROTO_BROADCAST_CHANNEL_NAME, new C2SContainerStopPacket(containerId))
        .thenAccept(packet -> {
        });
  }

  private C2SContainerCreatePacket intoCreateContainerPacket(
      ContainerSpecification containerSpecification) {
    return new C2SContainerCreatePacket(
        containerSpecification.getImage().getImageName(),
        containerSpecification.getImage().getImageTag(),
        containerSpecification.getAssignedMemory(),
        containerSpecification.getAssignedMemorySwap(),
        containerSpecification.getHostname(),
        containerSpecification.getExposedPorts(),
        containerSpecification.getPublishPorts(),
        containerSpecification.getEnvironmentalVariables(),
        containerSpecification.getBinds());
  }

  private ContainerDetails fromContainerInspectPacket(S2CContainerInspectPacket packet) {
    return new ContainerDetails(
        packet.getContainerId(),
        packet.getImageHash(),
        packet.getAddress(),
        packet.getAssignedMemory(),
        packet.getAssignedMemorySwap(),
        packet.getPort(),
        packet.getStartedAt());
  }
}
