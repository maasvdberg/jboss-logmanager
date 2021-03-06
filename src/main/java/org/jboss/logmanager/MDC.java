/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.logmanager;

import java.util.Map;

/**
 * Mapped diagnostic context.  This is a thread-local map used to hold loggable information.
 */
public final class MDC {

    private MDC() {}

    private static final Holder mdc = new Holder();

    /**
     * Get the value for a key, or {@code null} if there is no mapping.
     *
     * @param key the key
     * @return the value
     */
    public static String get(String key) {
        return mdc.get().get(key);
    }

    /**
     * Set the value of a key, returning the old value (if any) or {@code null} if there was none.
     *
     * @param key the key
     * @param value the new value
     * @return the old value or {@code null} if there was none
     */
    public static String put(String key, String value) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        return mdc.get().put(key, value);
    }

    /**
     * Remove a key.
     *
     * @param key the key
     * @return the old value or {@code null} if there was none
     */
    public static String remove(String key) {
        return mdc.get().remove(key);
    }

    /**
     * Get a copy of the MDC map.  This is a relatively expensive operation.
     *
     * @return a copy of the map
     */
    public static Map<String, String> copy() {
        return mdc.get().clone();
    }

    /**
     * Clear the current MDC map.
     */
    public static void clear() {
        mdc.get().clear();
    }

    private static final class Holder extends InheritableThreadLocal<FastCopyHashMap<String, String>> {

        protected FastCopyHashMap<String, String> childValue(final Map<String, String> parentValue) {
            return new FastCopyHashMap<String, String>(parentValue);
        }

        protected FastCopyHashMap<String, String> initialValue() {
            return new FastCopyHashMap<String, String>();
        }
    }
}
