/*
 * JSON-RPC-Java - a JSON-RPC to Java Bridge with dynamic invocation
 *
 * $Id: StringSerializer.java,v 1.3.2.2 2006/03/06 12:39:21 mclark Exp $
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

public class StringSerializer extends AbstractSerializer
{
    private final static long serialVersionUID = 1;

    private static Class[] _serializableClasses = new Class[]
	{ String.class, char.class, Character.class,
	  byte[].class, char[].class };

    private static Class[] _JSONClasses = new Class[]
	{ String.class, Integer.class };

    public Class[] getSerializableClasses() { return _serializableClasses; }
    public Class[] getJSONClasses() { return _JSONClasses; }

    public ObjectMatch tryUnmarshall(SerializerState state,
				     Class clazz, Object jso)
	throws UnmarshallException
    {
	return ObjectMatch.OKAY;
    }

    public Object unmarshall(SerializerState state, Class clazz, Object jso)
	throws UnmarshallException
    {
        String val = jso instanceof String?(String)jso:jso.toString();
	if(clazz == char.class) {
	    return new Character(val.charAt(0));
	} else if (clazz == byte[].class) {
	    return val.getBytes();
	} else if (clazz == char[].class) {
	    return val.toCharArray();	    
	} else {
	    return val;
	}
    }

    public Object marshall(SerializerState state, Object o)
	throws MarshallException
    {
	if(o instanceof Character) {
	    return o.toString();
	} else if(o instanceof byte[]) {
	    return new String((byte[])o);
	} else if (o instanceof char[]) {
	    return new String((char[])o);
	} else return o;
    }

}
