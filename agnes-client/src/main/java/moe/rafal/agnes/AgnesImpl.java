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
import moe.rafal.agnes.docker.DockerContainer;
import moe.rafal.agnes.docker.DockerContainerFactory;
import moe.rafal.agnes.docker.DockerImage;
import moe.rafal.agnes.proto.container.c2s.C2SContainerCreatePacket;
import moe.rafal.agnes.proto.container.c2s.C2SContainerInspectPacket;
import moe.rafal.agnes.proto.container.c2s.C2SContainerStartPacket;
import moe.rafal.agnes.proto.container.c2s.C2SContainerStopPacket;
import moe.rafal.agnes.proto.container.s2c.S2CContainerCreatePacket;
import moe.rafal.agnes.proto.container.s2c.S2CContainerInspectPacket;
import moe.rafal.cory.Cory;

class AgnesImpl implements Agnes {

  private final Cory cory;

  AgnesImpl(Cory cory) {
    this.cory = cory;
  }

  @Override
  public CompletableFuture<String> createContainer(DockerImage image,
      long availableMemory,
      long availableMemorySwap,
      String hostname,
      String[] exposedPorts,
      String[] publishPorts,
      String[] environmentalVariables) {
    return cory.request(PROTO_BROADCAST_CHANNEL_NAME,
            new C2SContainerCreatePacket(
                image.getImageName(),
                image.getImageTag(),
                availableMemory,
                availableMemorySwap,
                hostname,
                exposedPorts,
                publishPorts,
                environmentalVariables))
        .thenApply(S2CContainerCreatePacket.class::cast)
        .thenApply(S2CContainerCreatePacket::getContainerId);
  }

  @Override
  public CompletableFuture<DockerContainer> inspectContainer(String containerId) {
    return cory.request(PROTO_BROADCAST_CHANNEL_NAME, new C2SContainerInspectPacket(containerId))
        .thenApply(S2CContainerInspectPacket.class::cast)
        .thenApply(DockerContainerFactory::produceDockerContainer);
  }

  @Override
  public CompletableFuture<Void> startContainer(String containerId) {
    return cory.request(PROTO_BROADCAST_CHANNEL_NAME, new C2SContainerStartPacket(containerId))
        .thenAccept(packet -> {
        });
  }

  @Override
  public CompletableFuture<Void> stopContainer(String containerId) {
    return cory.request(PROTO_BROADCAST_CHANNEL_NAME, new C2SContainerStopPacket(containerId))
        .thenAccept(packet -> {
        });
  }
}
