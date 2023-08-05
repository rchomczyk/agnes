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

package moe.rafal.agnes.container.specification;

import moe.rafal.agnes.image.Image;

public class ContainerSpecification {

  private final Image image;
  private final long assignedMemory;
  private final long assignedMemorySwap;
  private final String hostname;
  private final String[] exposedPorts;
  private final String[] publishPorts;
  private final String[] environmentalVariables;
  private final String[] binds;

  protected ContainerSpecification(
      Image image,
      long assignedMemory,
      long assignedMemorySwap,
      String hostname,
      String[] exposedPorts,
      String[] publishPorts,
      String[] environmentalVariables,
      String[] binds) {
    this.image = image;
    this.assignedMemory = assignedMemory;
    this.assignedMemorySwap = assignedMemorySwap;
    this.hostname = hostname;
    this.exposedPorts = exposedPorts;
    this.publishPorts = publishPorts;
    this.environmentalVariables = environmentalVariables;
    this.binds = binds;
  }

  public Image getImage() {
    return image;
  }

  public long getAssignedMemory() {
    return assignedMemory;
  }

  public long getAssignedMemorySwap() {
    return assignedMemorySwap;
  }

  public String getHostname() {
    return hostname;
  }

  public String[] getExposedPorts() {
    return exposedPorts;
  }

  public String[] getPublishPorts() {
    return publishPorts;
  }

  public String[] getEnvironmentalVariables() {
    return environmentalVariables;
  }

  public String[] getBinds() {
    return binds;
  }
}
