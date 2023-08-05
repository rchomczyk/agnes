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

import static java.lang.String.format;

import moe.rafal.agnes.image.Image;

public class ContainerSpecificationBuilder {

  private Image image;
  private long assignedMemory;
  private long assignedMemorySwap;
  private String hostname;
  private String[] exposedPorts;
  private String[] publishPorts;
  private String[] environmentalVariables;
  private String[] binds;

  private ContainerSpecificationBuilder() {

  }

  public static ContainerSpecificationBuilder newBuilder() {
    return new ContainerSpecificationBuilder();
  }

  public ContainerSpecificationBuilder withImage(Image image) {
    this.image = image;
    return this;
  }

  public ContainerSpecificationBuilder withAssignedMemory(long assignedMemory) {
    this.assignedMemory = assignedMemory;
    return this;
  }

  public ContainerSpecificationBuilder withAssignedMemorySwap(long assignedMemorySwap) {
    this.assignedMemorySwap = assignedMemorySwap;
    return this;
  }

  public ContainerSpecificationBuilder withHostname(String hostname) {
    this.hostname = hostname;
    return this;
  }

  public ContainerSpecificationBuilder withExposedPorts(String[] exposedPorts) {
    this.exposedPorts = exposedPorts;
    return this;
  }

  public ContainerSpecificationBuilder withPublishPorts(String[] publishPorts) {
    this.publishPorts = publishPorts;
    return this;
  }

  public ContainerSpecificationBuilder withEnvironmentalVariables(String[] environmentalVariables) {
    this.environmentalVariables = environmentalVariables;
    return this;
  }

  public ContainerSpecificationBuilder withBinds(String[] binds) {
    this.binds = binds;
    return this;
  }

  public ContainerSpecification build() throws ContainerSpecificationBuildException {
    assertBuilderValues();
    return new ContainerSpecification(image, assignedMemory, assignedMemorySwap, hostname,
        exposedPorts, publishPorts, environmentalVariables, binds);
  }

  private void assertBuilderValues() throws ContainerSpecificationBuildException {
    assertBuilderValue(image, "image");
    assertBuilderValue(assignedMemory, "assigned_memory");
    assertBuilderValue(assignedMemorySwap, "assigned_memory_swap");
    assertBuilderValue(hostname, "hostname");
    assertBuilderValue(exposedPorts, "exposed_ports");
    assertBuilderValue(publishPorts, "publish_ports");
    assertBuilderValue(environmentalVariables, "environmental_variables");
    assertBuilderValue(binds, "binds");
  }

  private static void assertBuilderValue(Object value, String valueId)
      throws ContainerSpecificationBuildException {
    if (value == null) {
      throw new ContainerSpecificationBuildException(format(
          "Container specification could not be built, because of missing value for property '%s'.",
          valueId));
    }
  }
}
