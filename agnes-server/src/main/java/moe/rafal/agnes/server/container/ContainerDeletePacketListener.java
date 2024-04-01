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

import de.gesellix.docker.client.DockerClient;
import moe.rafal.agnes.proto.container.c2s.C2SContainerDeletePacket;
import moe.rafal.agnes.proto.container.s2c.S2CContainerDeletePacket;
import moe.rafal.cory.Cory;
import moe.rafal.cory.message.packet.PacketListenerDelegate;

public class ContainerDeletePacketListener extends
    PacketListenerDelegate<C2SContainerDeletePacket> {

  private final DockerClient dockerClient;
  private final Cory cory;

  public ContainerDeletePacketListener(DockerClient dockerClient, Cory cory) {
    super(C2SContainerDeletePacket.class);
    this.dockerClient = dockerClient;
    this.cory = cory;
  }

  @Override
  public void receive(String channelName, String replyChannelName,
      C2SContainerDeletePacket packet) {
    dockerClient.rm(packet.getContainerId());
    cory.publish(replyChannelName, new S2CContainerDeletePacket(packet.getContainerId()));
  }
}
