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

package moe.rafal.agnes.server;

import static io.lettuce.core.RedisURI.create;
import static java.time.Duration.ofSeconds;
import static moe.rafal.agnes.proto.ProtoUtils.PROTO_BROADCAST_CHANNEL_NAME;
import static moe.rafal.cory.message.RedisMessageBrokerFactory.produceRedisMessageBroker;

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import java.io.IOException;
import moe.rafal.agnes.server.container.ContainerCreatePacketListener;
import moe.rafal.agnes.server.container.ContainerDeletePacketListener;
import moe.rafal.agnes.server.container.ContainerInspectPacketListener;
import moe.rafal.agnes.server.container.ContainerStartPacketListener;
import moe.rafal.agnes.server.container.ContainerStopPacketListener;
import moe.rafal.agnes.server.scheduler.ServerLockingThread;
import moe.rafal.cory.Cory;
import moe.rafal.cory.CoryBuilder;
import moe.rafal.cory.serdes.MessagePackPacketPackerFactory;
import moe.rafal.cory.serdes.MessagePackPacketUnpackerFactory;

class AgnesServerImpl implements AgnesServer {

  private final Cory cory;
  private ServerLockingThread serverLockingThread;

  AgnesServerImpl() {
    this.cory = CoryBuilder.newBuilder()
        .withPacketPackerFactory(MessagePackPacketPackerFactory.INSTANCE)
        .withPacketUnpackerFactory(MessagePackPacketUnpackerFactory.INSTANCE)
        .withMessageBroker(
            produceRedisMessageBroker(
                MessagePackPacketPackerFactory.INSTANCE,
                MessagePackPacketUnpackerFactory.INSTANCE,
                create("redis://127.0.0.1:6379"), ofSeconds(30))
            )
        .build();
  }

  @Override
  public void start() {
    final DockerClient dockerClient = new DockerClientImpl();
    cory.observe(PROTO_BROADCAST_CHANNEL_NAME,
        new ContainerCreatePacketListener(dockerClient, cory));
    cory.observe(PROTO_BROADCAST_CHANNEL_NAME,
        new ContainerDeletePacketListener(dockerClient, cory));
    cory.observe(PROTO_BROADCAST_CHANNEL_NAME,
        new ContainerInspectPacketListener(dockerClient, cory));
    cory.observe(PROTO_BROADCAST_CHANNEL_NAME,
        new ContainerStartPacketListener(dockerClient, cory));
    cory.observe(PROTO_BROADCAST_CHANNEL_NAME,
        new ContainerStopPacketListener(dockerClient, cory));
    startServerLocking();
  }

  @Override
  public void ditch() throws AgnesException {
    serverLockingThread.interrupt();
    try {
      cory.close();
    } catch (IOException exception) {
      throw new AgnesException(
          "Could not gracefully ditch agnes server, because of unexpected exception.",
          exception);
    }
  }

  private void startServerLocking() {
    serverLockingThread = new ServerLockingThread();
    serverLockingThread.start();
  }
}
