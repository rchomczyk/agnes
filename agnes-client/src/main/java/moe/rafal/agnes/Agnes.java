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

package moe.rafal.agnes;

import java.util.concurrent.CompletableFuture;
import moe.rafal.agnes.container.ContainerDetails;
import moe.rafal.agnes.container.specification.ContainerSpecification;

public interface Agnes {

  CompletableFuture<ContainerDetails> inspectContainer(String containerId);

  CompletableFuture<String> createContainer(ContainerSpecification containerSpecification);

  CompletableFuture<Void> deleteContainer(String containerId);

  CompletableFuture<String> startContainer(String containerId);

  CompletableFuture<Void> stopContainer(String containerId);
}
