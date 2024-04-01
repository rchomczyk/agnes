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

package moe.rafal.agnes.proto.container.c2s;

import java.util.Arrays;
import java.util.Objects;
import moe.rafal.agnes.proto.container.ContainerCreationType;
import moe.rafal.cory.pojo.PojoPacket;

public class C2SContainerCreatePacket extends PojoPacket {

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
      final String imageId,
      final String imageTag,
      final long assignedMemory,
      final long assignedMemorySwap,
      final String hostname,
      final ContainerCreationType creationType,
      final String[] exposedPorts,
      final String[] publishPorts,
      final String[] environmentVariables,
      final String[] binds
  ) {
    this.imageId = imageId;
    this.imageTag = imageTag;
    this.assignedMemory = assignedMemory;
    this.assignedMemorySwap = assignedMemorySwap;
    this.hostname = hostname;
    this.creationType = creationType;
    this.exposedPorts = exposedPorts;
    this.publishPorts = publishPorts;
    this.environmentVariables = environmentVariables;
    this.binds = binds;
  }

  public C2SContainerCreatePacket() {
    super();
  }

  public String getImageId() {
    return imageId;
  }

  public void setImageId(final String imageId) {
    this.imageId = imageId;
  }

  public String getImageTag() {
    return imageTag;
  }

  public void setImageTag(final String imageTag) {
    this.imageTag = imageTag;
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

  public String getHostname() {
    return hostname;
  }

  public void setHostname(final String hostname) {
    this.hostname = hostname;
  }

  public ContainerCreationType getCreationType() {
    return creationType;
  }

  public void setCreationType(final ContainerCreationType creationType) {
    this.creationType = creationType;
  }

  public String[] getExposedPorts() {
    return exposedPorts;
  }

  public void setExposedPorts(final String[] exposedPorts) {
    this.exposedPorts = exposedPorts;
  }

  public String[] getPublishPorts() {
    return publishPorts;
  }

  public void setPublishPorts(final String[] publishPorts) {
    this.publishPorts = publishPorts;
  }

  public String[] getEnvironmentVariables() {
    return environmentVariables;
  }

  public void setEnvironmentVariables(final String[] environmentVariables) {
    this.environmentVariables = environmentVariables;
  }

  public String[] getBinds() {
    return binds;
  }

  public void setBinds(final String[] binds) {
    this.binds = binds;
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

    final C2SContainerCreatePacket that = (C2SContainerCreatePacket) object;
    return assignedMemory == that.assignedMemory
        && assignedMemorySwap == that.assignedMemorySwap
        && Objects.equals(imageId, that.imageId)
        && Objects.equals(imageTag, that.imageTag)
        && Objects.equals(hostname, that.hostname)
        && Arrays.equals(exposedPorts, that.exposedPorts)
        && Arrays.equals(publishPorts, that.publishPorts)
        && Arrays.equals(environmentVariables, that.environmentVariables)
        && Arrays.equals(binds, that.binds)
        && creationType == that.creationType;
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(
        super.hashCode(),
        imageId, imageTag, assignedMemory, assignedMemorySwap, hostname, creationType
    );
    result = 31 * result + Arrays.hashCode(exposedPorts);
    result = 31 * result + Arrays.hashCode(publishPorts);
    result = 31 * result + Arrays.hashCode(environmentVariables);
    result = 31 * result + Arrays.hashCode(binds);
    return result;
  }
}
