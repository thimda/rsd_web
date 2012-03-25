/*
 * JSON-RPC-Java - a JSON-RPC to Java Bridge with dynamic invocation
 *
 * $Id: NumberSerializer.java,v 1.4.2.2 2006/03/06 12:39:21 mclark Exp $
 *
 * Copyright Metaparadigm Pte. Ltd. 2004.
 * Michael Clark <michael@metaparadigm.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.json;

public class NumberSerializer extends AbstractSerializer
{
    private final static long serialVersionUID = 1;

    private static Class[] _serializableClasses = new Class[]
	{ Integer.class, Byte.class, Short.class, Long.class,
	  Float.class, Double.class };

    private static Class[] _JSONClasses = new Class[]
	{ Integer.class, Byte.class, Short.class, Long.class,
	  Float.class, Double.class, String.class };

    public Class[] getSerializableClasses() { return _serializableClasses; }
    public Class[] getJSONClasses() { return _JSONClasses; }

    public ObjectMatch tryUnmarshall(SerializerState state,
				     Class clazz, Object jso)
	throws UnmarshallException
    {
	try {
	    toNumber(clazz, jso);
	} catch (NumberFormatException e) {
	    throw new UnmarshallException("not a number");
	}
	return ObjectMatch.OKAY;
    }

    public Object toNumber(Class clazz, Object jso)
        throws NumberFormatException 
    {
        if (clazz == Integer.class) {
          if (jso instanceof String) 
            return new Integer((String)jso);
          else 
            return new Integer(((Number) jso).intValue());
        } else if (clazz == Long.class) {
          if (jso instanceof String) 
            return new Long((String)jso);
          else 
            return new Long(((Number) jso).longValue());
        } else if (clazz == Short.class) {
          if (jso instanceof String) 
            return new Short((String)jso);
          else 
            return new Short(((Number) jso).shortValue());
        } else if (clazz == Byte.class) {
          if (jso instanceof String) 
            return new Byte((String)jso);
          else 
            return new Byte(((Number) jso).byteValue());
        } else if (clazz == Float.class) {
          if (jso instanceof String) 
            return new Float((String)jso);
          else 
            return new Float(((Number) jso).floatValue());
        } else if (clazz == Double.class) {
          if (jso instanceof String) 
            return new Double((String)jso);
          else 
            return new Double(((Number) jso).doubleValue());
        }
        return null;
    }
  
    public Object unmarshall(SerializerState state, Class clazz, Object jso)
        throws UnmarshallException 
    {
        try {
            if (jso==null||"".equals(jso))
                return null;
            return toNumber(clazz, jso);
        } catch (NumberFormatException nfe) {
            throw new UnmarshallException("cannot convert object "+jso+" to type "+clazz.getName());
        }
    }

    public Object marshall(SerializerState state, Object o)
	throws MarshallException
    {
	return o;
    }

}
