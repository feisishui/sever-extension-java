/*
 * Copyright (c) 2017 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.​
 */

package com.esri.serverextension.core.util;

public class StopWatch {

    private boolean isRunning;
    private boolean hasElapsed;
    private long startTime;
    private long elapsedTime;

    public StopWatch() {
    }

    public static StopWatch createAndStart() {
        StopWatch timer = new StopWatch();
        timer.start();
        return timer;
    }

    public StopWatch start() {
        if (isRunning) {
            throw new IllegalStateException("StopWatch is already running.");
        }
        isRunning = true;
        hasElapsed = false;
        startTime = System.currentTimeMillis();
        return this;
    }

    public StopWatch stop() {
        if (!isRunning) {
            throw new IllegalStateException("StopWatch is not running.");
        }
        elapsedTime = System.currentTimeMillis() - startTime;
        isRunning = false;
        hasElapsed = true;
        return this;
    }

    public long elapsedTimeMillis() {
        if (isRunning) {
            throw new IllegalStateException("StopWatch is already running.");
        }
        if (!hasElapsed) {
            throw new IllegalStateException("StopWatch has never been started.");
        }
        return elapsedTime;
    }

    public double elapsedTimeSeconds() {
        return elapsedTimeMillis() / 1000.0d;
    }
}
