/*
 *    Copyright 2023-2024 agnes
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

package moe.rafal.agnes.server.container;

import static java.lang.String.format;
import static java.util.List.of;

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.EngineResponseContent;
import de.gesellix.docker.remote.api.ContainerCreateRequest;
import de.gesellix.docker.remote.api.ContainerCreateResponse;
import de.gesellix.docker.remote.api.HostConfig;
import de.gesellix.docker.remote.api.PortBinding;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import moe.rafal.agnes.proto.container.c2s.C2SContainerCreatePacket;
import moe.rafal.agnes.proto.container.s2c.S2CContainerCreatePacket;
import moe.rafal.cory.Cory;
import moe.rafal.cory.message.packet.PacketListenerDelegate;

public class ContainerCreatePacketListener extends
    PacketListenerDelegate<C2SContainerCreatePacket> {

  private static final Object EMPTY_OBJECT = new Object();
  private final DockerClient dockerClient;
  private final Cory cory;

  public ContainerCreatePacketListener(DockerClient dockerClient, Cory cory) {
    super(C2SContainerCreatePacket.class);
    this.dockerClient = dockerClient;
    this.cory = cory;
  }

  @Override
  public void receive(String channelName, String replyChannelName,
      C2SContainerCreatePacket packet) {
    final ContainerCreateRequest containerCreateRequest = getContainerCreateRequest(packet);
    final EngineResponseContent<ContainerCreateResponse> creationResult;
    switch (packet.getCreationType()) {
      case CREATE:
        creationResult = dockerClient.createContainer(containerCreateRequest);
        break;
      case CREATE_AND_RUN:
        creationResult = dockerClient.run(containerCreateRequest);
        break;
      default:
        throw new ContainerCreationException(
            "Could not create container, because of specifying unknown state.");
    }

    final ContainerCreateResponse extractedResult = creationResult.getContent();
    final S2CContainerCreatePacket replyingPacket = new S2CContainerCreatePacket(
        extractedResult.getId());

    cory.publish(replyChannelName, replyingPacket);
  }

  private ContainerCreateRequest getContainerCreateRequest(C2SContainerCreatePacket packet) {
    pullDockerImage(packet.getImageId(), packet.getImageTag());
    final ContainerCreateRequest containerCreateRequest = new ContainerCreateRequest();
    containerCreateRequest.setImage(format("%s:%s", packet.getImageId(), packet.getImageTag()));
    containerCreateRequest.setEnv(of(packet.getEnvironmentVariables()));
    containerCreateRequest.setExposedPorts(getExposedPorts(packet));
    final HostConfig hostConfig = new HostConfig();
    hostConfig.setPublishAllPorts(true);
    hostConfig.setMemory(packet.getAssignedMemory());
    hostConfig.setMemorySwap(packet.getAssignedMemorySwap());
    hostConfig.setPortBindings(getPublishPorts(packet));
    hostConfig.setBinds(List.of(packet.getBinds()));
    containerCreateRequest.setHostConfig(hostConfig);
    return containerCreateRequest;
  }

  private void pullDockerImage(String imageId, String imageTag) {
    dockerClient.pull(null, null, imageId, imageTag);
  }

  private Map<String, Object> getExposedPorts(C2SContainerCreatePacket packet) {
    final Map<String, Object> exposedPorts = new HashMap<>();
    for (final String exposedPort : packet.getExposedPorts()) {
      exposedPorts.put(exposedPort, EMPTY_OBJECT);
    }
    return exposedPorts;
  }

  private Map<String, List<PortBinding>> getPublishPorts(C2SContainerCreatePacket packet) {
    final Map<String, List<PortBinding>> publishPorts = new HashMap<>();
    for (final String publishPort : packet.getPublishPorts()) {
      final String[] aggregatedPorts = publishPort.split(":");
      final String innerPort = aggregatedPorts[0];
      final String localPort = aggregatedPorts[1];
      publishPorts.put(innerPort, of(new PortBinding(packet.getHostname(), localPort)));
    }
    return publishPorts;
  }
}
