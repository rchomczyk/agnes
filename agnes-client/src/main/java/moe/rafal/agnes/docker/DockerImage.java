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

package moe.rafal.agnes.docker;

import static java.lang.String.format;

public class DockerImage {

  private final String imageName;
  private final String imageTag;

  public DockerImage(String imageName, String tag) {
    this.imageName = imageName;
    this.imageTag = tag;
  }

  public String getImageId() {
    return format("%s:%s", imageName, imageTag);
  }

  public String getImageName() {
    return imageName;
  }

  public String getImageTag() {
    return imageTag;
  }
}
