/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2012 The eXist Project
 *  http://exist-db.org
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  $Id$
 */
package org.exist.replication.shared;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Container class for clustering messages.
 *
 * @author Dannes Wessels
 */
public class eXistMessage {

    /**
     * Header to describe operation on resource, e.g. CREATE UPDATE
     */
    public final static String EXIST_RESOURCE_OPERATION = "exist.resource.operation";
    
    /**
     * Header to describe resource, DOCUMENT or COLLECTION
     */
    public final static String EXIST_RESOURCE_TYPE = "exist.resource.type";
    
    /**
     * Header to describe path of resource
     */
    public final static String EXIST_SOURCE_PATH = "exist.source.path";
    
    /**
     * Header to describe destination path, for COPY and MOVE operation
     */
    public final static String EXIST_DESTINATION_PATH = "exist.destination.path";
    
    private ResourceOperation resourceOperation;
    private ResourceType resourceType;
    private String path;
    private String destination;
    private byte[] payload;
    
    private Map<String, Object> metaData = new HashMap<String, Object>();

    /**
     * Atomic operations on resources
     */
    public enum ResourceOperation {
        CREATE, UPDATE, DELETE, MOVE, COPY, METADATA
    }

    /**
     * Types of exist-db resources
     */
    public enum ResourceType {
        DOCUMENT, COLLECTION
    }

    public void setResourceOperation(ResourceOperation type) {
        resourceOperation = type;
    }
    
    /**
     * @throws IllegalArgumentException When argument cannot be converted to enum value.
     */
    public void setResourceOperation(String type) {
        resourceOperation = ResourceOperation.valueOf(type.toUpperCase(Locale.ENGLISH));
    }

    public ResourceOperation getResourceOperation() {
        return resourceOperation;
    }

    public void setResourceType(ResourceType type) {
        resourceType = type;
    }
    
    /**
     * @throws IllegalArgumentException When argument cannot be converted to enum value.
     */
    public void setResourceType(String type) {
        resourceType = eXistMessage.ResourceType.valueOf(type.toUpperCase(Locale.ENGLISH));
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourcePath(String path) {
        this.path = path;
    }

    public String getResourcePath() {
        return path;
    }

    public void setDestinationPath(String path) {
        destination = path;
    }

    public String getDestinationPath() {
        return destination;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] data) {
        payload = data;
    }

    public void setMetadata(Map<String, Object> props) {
        metaData = props;
    }

    public Map<String, Object> getMetadata() {
        return metaData;
    }
    
    /**
     * Get one-liner report of message, including the JMS properties.
     */
    public String getReport() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Message summary: ");
        sb.append("ResourceType='").append(resourceType.toString()).append("'  ");
        sb.append("ResourceOperation='").append(resourceOperation).append("'  ");
        
        sb.append("ResourcePath='").append(path).append("'  ");
        
        if(destination!=null){
            sb.append("DestinationPath='").append(resourceType.toString()).append("'  ");
        }
        
        if(payload!=null && payload.length>0){
            sb.append("PayloadSize='").append(payload.length).append("'  ");
        }
        
        // Iterate over properties if present
        Set<String> keys = metaData.keySet();
        if(!keys.isEmpty()){
            sb.append("###  ");

            for(String key : keys){
                Object val=metaData.get(key);
                if(val != null){
                    sb.append(key).append("='").append(val.toString()).append("'  ");
                }
            }
        }
        
        return sb.toString();
    }
}
