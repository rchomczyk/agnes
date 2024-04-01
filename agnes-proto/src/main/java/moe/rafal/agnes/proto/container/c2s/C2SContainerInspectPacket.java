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

import java.util.Objects;
import moe.rafal.cory.pojo.PojoPacket;

public class C2SContainerInspectPacket extends PojoPacket {

  private String containerId;

  public C2SContainerInspectPacket(final String containerId) {
    super();
    this.containerId = containerId;
  }

  public C2SContainerInspectPacket() {
    super();
  }

  public String getContainerId() {
    return containerId;
  }

  public void setContainerId(final String containerId) {
    this.containerId = containerId;
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

    final C2SContainerInspectPacket that = (C2SContainerInspectPacket) object;
    return Objects.equals(containerId, that.containerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), containerId);
  }
}
