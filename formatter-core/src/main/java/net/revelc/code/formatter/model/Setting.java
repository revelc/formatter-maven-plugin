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
package net.revelc.code.formatter.model;

/**
 * A class representing the setting XML element in the Eclipse formatter config file, including the id and value
 * attributes.
 * 
 * @author Matt Blanchette
 */
public class Setting {

    /** The id. */
    private String id;

    /** The value. */
    private String value;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the id.
     *
     * @param newId
     *            the new id
     */
    public void setId(String newId) {
        this.id = newId;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the value.
     *
     * @param newValue
     *            the new value
     */
    public void setValue(String newValue) {
        this.value = newValue;
    }
}
