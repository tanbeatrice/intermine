package org.intermine.web.results;

/*
 * Copyright (C) 2002-2005 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.Map;
import java.util.List;

import org.intermine.objectstore.proxy.LazyCollection;
import org.intermine.metadata.ClassDescriptor;
import org.intermine.web.config.WebConfig;
import org.intermine.objectstore.ObjectStoreException;

/**
 * Class to represent a field of an object for the webapp
 * @author Kim Rutherford
 */

public class DisplayField
{
    ClassDescriptor cld;
    int size = -1;
    InlineResultsTable table = null;
    List collection = null;
    WebConfig webConfig = null;
    Map webProperties = null;

    /**
     * Create a new DisplayField object.
     * @param collection the List the holds the object(s) to display
     * @param cld metadata for the referenced object
     * @param webConfig the WebConfig object for this webapp
     * @param webProperties the web properties from the session
     * @throws Exception if an error occurs
     */
    public DisplayField(List collection, ClassDescriptor cld,
                        WebConfig webConfig, Map webProperties) throws Exception {
        this.collection = collection;
        this.cld = cld;
        this.webConfig = webConfig;
        this.webProperties = webProperties;
    }

    /**
     * Get the inline results table for this collection
     * @return the results table
     */
    public InlineResultsTable getTable() {
        if (table == null) {
            table = new InlineResultsTable(collection, cld, webConfig, webProperties);
        }
        return table;
    }
    
    /**
     * Get the class descriptor for this collection
     * @return the class descriptor
     */
    public ClassDescriptor getCld() {
        return cld;
    }

    /**
     * Get the size of this collection
     * @return the size
     */
    public int getSize() {
        if (size == -1) {
            if (collection instanceof LazyCollection) {
                try {
                    LazyCollection lazyCollection = (LazyCollection) collection;
                    try {
                        // get the first batch to make sure that small collections have an accurate
                        // size
                        // see also ticket #267
                        lazyCollection.get(0);
                        size = lazyCollection.getInfo().getRows();
                    } catch (IndexOutOfBoundsException _) {
                        size = 0;
                    }
                } catch (ObjectStoreException e) {
                    throw new RuntimeException("unable to find the size of a collection", e);
                }
            } else {
                size = collection.size();
            }
        }
        return size;
    }
}
