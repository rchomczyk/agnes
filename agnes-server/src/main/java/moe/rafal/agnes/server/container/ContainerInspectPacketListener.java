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

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.EngineResponseContent;
import de.gesellix.docker.remote.api.ContainerInspectResponse;
import de.gesellix.docker.remote.api.ContainerState;
import de.gesellix.docker.remote.api.HostConfig;
import de.gesellix.docker.remote.api.PortBinding;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import moe.rafal.agnes.proto.container.c2s.C2SContainerInspectPacket;
import moe.rafal.agnes.proto.container.s2c.S2CContainerInspectPacket;
import moe.rafal.cory.Cory;
import moe.rafal.cory.message.packet.PacketListenerDelegate;

public class ContainerInspectPacketListener extends
    PacketListenerDelegate<C2SContainerInspectPacket> {

  private final DockerClient dockerClient;
  private final Cory cory;

  public ContainerInspectPacketListener(DockerClient dockerClient, Cory cory) {
    super(C2SContainerInspectPacket.class);
    this.dockerClient = dockerClient;
    this.cory = cory;
  }

  @Override
  public void receive(String channelName, String replyChannelName,
      C2SContainerInspectPacket packet) {
    final EngineResponseContent<ContainerInspectResponse> inspectionResult = dockerClient.inspectContainer(
        packet.getContainerId());

    final ContainerInspectResponse extractedResult = inspectionResult.getContent();
    final PortBinding containerPortBinding = extractContainerPortBinding(extractedResult);
    final S2CContainerInspectPacket replyingPacket = new S2CContainerInspectPacket(
        extractedResult.getId(),
        extractedResult.getImage(),
        containerPortBinding.getHostIp(),
        extractContainerMemory(extractedResult),
        extractContainerMemorySwap(extractedResult),
        Optional.ofNullable(containerPortBinding.getHostPort())
            .map(Integer::parseInt)
            .orElseThrow(() -> new ContainerInspectionException(
                format(
                    "Could not parse port binding from inspection result for container with id %s.",
                    packet.getContainerId()))),
        extractContainerStartTime(extractedResult));

    cory.publish(replyChannelName, replyingPacket);
  }

  private PortBinding extractContainerPortBinding(ContainerInspectResponse inspectResult) {
    return Stream.of(extractParameterFromInspectionResult(inspectResult,
            ContainerInspectResponse::getHostConfig, HostConfig::getPortBindings))
        .map(Map::values)
        .flatMap(Collection::stream)
        .flatMap(List::stream)
        .findFirst()
        .orElseThrow(() -> new ContainerInspectionException(
            format(
                "Could not extract port binding from inspection result for container with id %s.",
                inspectResult.getId())));
  }

  private long extractContainerMemory(ContainerInspectResponse inspectionResult) {
    return extractParameterFromInspectionResult(inspectionResult,
        ContainerInspectResponse::getHostConfig, HostConfig::getMemory);
  }

  private long extractContainerMemorySwap(ContainerInspectResponse inspectResult) {
    return extractParameterFromInspectionResult(inspectResult,
        ContainerInspectResponse::getHostConfig, HostConfig::getMemorySwap);
  }

  private Instant extractContainerStartTime(ContainerInspectResponse inspectResult) {
    return Instant.parse(extractParameterFromInspectionResult(inspectResult,
        ContainerInspectResponse::getState, ContainerState::getStartedAt));
  }

  private <T, R> R extractParameterFromInspectionResult(ContainerInspectResponse inspectionResult,
      Function<ContainerInspectResponse, T> resultSectionNavigator,
      Function<T, R> parameterExtractor) {
    return Optional.ofNullable(resultSectionNavigator.apply(inspectionResult))
        .map(parameterExtractor)
        .orElseThrow(() -> new ContainerInspectionException(
            format(
                "Could not extract parameter from inspection result for container with id %s.",
                inspectionResult.getId())));
  }
}
