/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.revelc.code.formatter;

public final class TimeUtil {

    /**
     * Prints the duration in a human readable format as X days Y hours Z minutes etc.
     *
     * @param uptime
     *            the uptime in millis
     * @param precise
     *            whether to be precise and include all details including milli seconds
     * 
     * @return the time used for displaying on screen or in logs
     */
    public static String printDuration(long uptime, boolean precise) {
        if (uptime <= 0) {
            return "0ms";
        }

        StringBuilder sb = new StringBuilder();

        long seconds = uptime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long millis = 0;
        if (uptime > 1000) {
            millis = uptime % 1000;
        } else if (uptime < 1000) {
            millis = uptime;
        }

        if (days > 0) {
            sb.append(days).append("d").append(hours % 24).append("h").append(minutes % 60).append("m")
                    .append(seconds % 60).append("s");
        } else if (hours > 0) {
            sb.append(hours % 24).append("h").append(minutes % 60).append("m").append(seconds % 60).append("s");
        } else if (minutes > 0) {
            sb.append(minutes % 60).append("m").append(seconds % 60).append("s");
        } else if (seconds > 0) {
            sb.append(seconds % 60).append("s");
            // lets include millis when there are only seconds by default
            precise = true;
        } else if (millis > 0) {
            precise = false;
            sb.append(millis).append("ms");
        }

        if (precise & millis > 0) {
            sb.append(millis).append("ms");
        }

        return sb.toString();
    }

}
