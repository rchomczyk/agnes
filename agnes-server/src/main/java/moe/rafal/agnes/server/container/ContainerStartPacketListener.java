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

package moe.rafal.agnes.server.container;

import de.gesellix.docker.client.DockerClient;
import moe.rafal.agnes.proto.container.c2s.C2SContainerStartPacket;
import moe.rafal.agnes.proto.container.s2c.S2CContainerStartPacket;
import moe.rafal.cory.Cory;
import moe.rafal.cory.message.packet.PacketListenerDelegate;

public class ContainerStartPacketListener extends PacketListenerDelegate<C2SContainerStartPacket> {

  private final DockerClient dockerClient;
  private final Cory cory;

  public ContainerStartPacketListener(DockerClient dockerClient, Cory cory) {
    super(C2SContainerStartPacket.class);
    this.dockerClient = dockerClient;
    this.cory = cory;
  }

  @Override
  public void receive(String channelName, String replyChannelName, C2SContainerStartPacket packet) {
    dockerClient.startContainer(packet.getContainerId());
    cory.publish(replyChannelName, new S2CContainerStartPacket(packet.getContainerId()));
  }
}
