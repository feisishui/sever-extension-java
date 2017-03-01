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

import com.esri.arcgis.system.ILog2;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LogAdaptor extends MarkerIgnoringBase {

    private static final long serialVersionUID = 1L;

    private ILog2 log;
    private boolean showShortLogName;
    /**
     * The short name of this simple log instance
     */
    private transient String shortLogName = null;

    LogAdaptor(ILog2 log, String name, boolean showShortLogName) {
        this.log = log;
        this.name = name;
        this.showShortLogName = showShortLogName;
    }

    /**
     * This is our internal implementation for logging regular
     * (non-parameterized) log messages.
     *
     * @param level   One of the {@link LogConstants} LOG_LEVEL_XXX constants
     *                defining the log level
     * @param message The message itself
     * @param t       The exception whose stack trace should be logged
     */
    private void log(int level, String message, Throwable t) {
        if (!isLevelEnabled(level)) {
            return;
        }

        StringBuilder buf = new StringBuilder(32);
        buf.append(' ');
        if (showShortLogName) {
            if (shortLogName == null)
                shortLogName = computeShortName();
            buf.append(String.valueOf(shortLogName)).append(" - ");
        } else {
            buf.append(String.valueOf(name)).append(" - ");
        }

        buf.append(message);

        write(level, buf, t);
    }

    void write(int level, StringBuilder buf, Throwable t) {

        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer);
        printer.println(buf.toString());
        if (t != null) {
            t.printStackTrace(printer);
        }
        printer.flush();

        if (log != null) {
            try {
                log.addMessage(level, LogConstants.LOG_MESSAGE_CODE_UNKNOWN,
                        writer.toString());
            } catch (IOException e) {
                throw new ArcObjectsInteropException(
                        "Cannot write log message.", e);
            }
        }
    }

    private String computeShortName() {
        return name.substring(name.lastIndexOf(".") + 1);
    }

    /**
     * For formatted messages, first substitute arguments and then log.
     *
     * @param level
     * @param format
     * @param arg1
     * @param arg2
     */
    private void formatAndLog(int level, String format, Object arg1, Object arg2) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    /**
     * For formatted messages, first substitute arguments and then log.
     *
     * @param level
     * @param format
     * @param arguments a list of 3 ore more arguments
     */
    private void formatAndLog(int level, String format, Object... arguments) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    @Override
    public void trace(String msg) {
        log(LogConstants.LOG_LEVEL_DEBUG, msg, null);
    }

    @Override
    public void trace(String format, Object arg) {
        formatAndLog(LogConstants.LOG_LEVEL_DEBUG, format, arg, null);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        formatAndLog(LogConstants.LOG_LEVEL_DEBUG, format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        formatAndLog(LogConstants.LOG_LEVEL_DEBUG, format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        log(LogConstants.LOG_LEVEL_DEBUG, msg, t);
    }

    @Override
    public void debug(String msg) {
        log(LogConstants.LOG_LEVEL_DETAILED, msg, null);
    }

    @Override
    public void debug(String format, Object arg) {
        formatAndLog(LogConstants.LOG_LEVEL_DETAILED, format, arg, null);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        formatAndLog(LogConstants.LOG_LEVEL_DETAILED, format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        formatAndLog(LogConstants.LOG_LEVEL_DETAILED, format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        log(LogConstants.LOG_LEVEL_DETAILED, msg, t);
    }

    @Override
    public void info(String msg) {
        log(LogConstants.LOG_LEVEL_NORMAL, msg, null);
    }

    @Override
    public void info(String format, Object arg) {
        formatAndLog(LogConstants.LOG_LEVEL_NORMAL, format, arg, null);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        formatAndLog(LogConstants.LOG_LEVEL_NORMAL, format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        formatAndLog(LogConstants.LOG_LEVEL_NORMAL, format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        log(LogConstants.LOG_LEVEL_NORMAL, msg, t);
    }

    @Override
    public void warn(String msg) {
        log(LogConstants.LOG_LEVEL_WARNING, msg, null);
    }

    @Override
    public void warn(String format, Object arg) {
        formatAndLog(LogConstants.LOG_LEVEL_WARNING, format, arg, null);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        formatAndLog(LogConstants.LOG_LEVEL_WARNING, format, arg1, arg2);
    }

    @Override
    public void warn(String format, Object... arguments) {
        formatAndLog(LogConstants.LOG_LEVEL_WARNING, format, arguments);
    }

    @Override
    public void warn(String msg, Throwable t) {
        log(LogConstants.LOG_LEVEL_WARNING, msg, t);
    }

    @Override
    public void error(String msg) {
        log(LogConstants.LOG_LEVEL_ERROR, msg, null);
    }

    @Override
    public void error(String format, Object arg) {
        formatAndLog(LogConstants.LOG_LEVEL_ERROR, format, arg, null);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        formatAndLog(LogConstants.LOG_LEVEL_ERROR, format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        formatAndLog(LogConstants.LOG_LEVEL_ERROR, format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        log(LogConstants.LOG_LEVEL_ERROR, msg, t);
    }

    @Override
    public boolean isTraceEnabled() {
        return isLevelEnabled(LogConstants.LOG_LEVEL_DEBUG);
    }

    @Override
    public boolean isDebugEnabled() {
        return isLevelEnabled(LogConstants.LOG_LEVEL_DETAILED);
    }

    @Override
    public boolean isInfoEnabled() {
        return isLevelEnabled(LogConstants.LOG_LEVEL_NORMAL);
    }

    @Override
    public boolean isWarnEnabled() {
        return isLevelEnabled(LogConstants.LOG_LEVEL_WARNING);
    }

    @Override
    public boolean isErrorEnabled() {
        return isLevelEnabled(LogConstants.LOG_LEVEL_ERROR);
    }

    private boolean isLevelEnabled(int level) {
        if (log == null) {
            return false;
        }
        try {
            return log.willLog(level);
        } catch (IOException e) {
            throw new ArcObjectsInteropException(e);
        }
    }
}
