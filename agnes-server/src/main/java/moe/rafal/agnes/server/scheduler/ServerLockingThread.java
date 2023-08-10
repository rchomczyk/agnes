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

package moe.rafal.agnes.server.scheduler;

public class ServerLockingThread extends Thread {

  private static final long LOCKING_INTERVAL = 50L;

  @Override
  public void run() {
    while (isAlive()) {
      final boolean whetherServerShouldUnlock = isInterrupted();
      if (whetherServerShouldUnlock) {
        break;
      }

      try {
        sleep(LOCKING_INTERVAL);
      } catch (InterruptedException exception) {
        throw new ServerLockException(
            "Could not extend server lock for another cycle, because of unexpected exception.",
            exception);
      }
    }
  }
}
