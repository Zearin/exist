/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2001-03,  Wolfgang M. Meier (meier@ifs.tu-darmstadt.de)
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 *  $Id$
 */
package org.exist.xquery.value;

import org.exist.xquery.Constants;
import org.exist.xquery.XPathException;

public class BooleanValue extends AtomicValue {

	public final static BooleanValue TRUE = new BooleanValue(true);
	public final static BooleanValue FALSE = new BooleanValue(false);

	private boolean value;

	public BooleanValue(boolean bool) {
		value = bool;
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.AtomicValue#getType()
	 */
	public int getType() {
		return Type.BOOLEAN;
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.Item#getStringValue()
	 */
	public String getStringValue() throws XPathException {
		return value ? "true" : "false";
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.AtomicValue#convertTo(int)
	 */
	public AtomicValue convertTo(int requiredType) throws XPathException {
		switch (requiredType) {
			case Type.BOOLEAN :
			case Type.ATOMIC :
			case Type.ITEM :
				return this;
			case Type.NUMBER :
			case Type.INTEGER :
				return new IntegerValue(value ? 1 : 0);
			case Type.STRING :
				return new StringValue(getStringValue());
			default :
				throw new XPathException(
					"cannot convert boolean '" + value + "' to " + requiredType);
		}
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.AtomicValue#compareTo(int, org.exist.xpath.value.AtomicValue)
	 */
	public boolean compareTo(int operator, AtomicValue other) throws XPathException {
		if (Type.subTypeOf(other.getType(), Type.BOOLEAN)) {
			boolean otherVal = ((BooleanValue) other).getValue();
			switch (operator) {
				case Constants.EQ :
					return value == otherVal;
				case Constants.NEQ :
					return value != otherVal;
				case Constants.LT:
					return (!value) && otherVal;
				case Constants.GT:
					return value && (!otherVal);
				default :
					throw new XPathException("Type error: cannot apply this operator to a boolean value");
			}
		}
		throw new XPathException("Type error: cannot convert operand to boolean");
	}

	public int compareTo(AtomicValue other) throws XPathException {
		boolean otherVal = other.effectiveBooleanValue();
		if (otherVal == value)
			return 0;
		else if (value)
			return 1;
		else
			return -1;
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.AtomicValue#effectiveBooleanValue()
	 */
	public boolean effectiveBooleanValue() throws XPathException {
		return value;
	}

	public boolean getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.AtomicValue#max(org.exist.xpath.value.AtomicValue)
	 */
	public AtomicValue max(AtomicValue other) throws XPathException {
		if (other.getType() == Type.BOOLEAN) {
			boolean otherValue = ((BooleanValue) other).value;
			return value && (!otherValue) ? this : other;
		} else
			throw new XPathException(
				"Invalid argument to aggregate function: expected boolean, got: "
					+ Type.getTypeName(other.getType()));
	}

	public AtomicValue min(AtomicValue other) throws XPathException {
		if (other.getType() == Type.BOOLEAN) {
			boolean otherValue = ((BooleanValue) other).value;
			return (!value) && otherValue ? this : other;
		} else
			throw new XPathException(
				"Invalid argument to aggregate function: expected boolean, got: "
					+ Type.getTypeName(other.getType()));
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.Item#conversionPreference(java.lang.Class)
	 */
	public int conversionPreference(Class javaClass) {
		if(javaClass.isAssignableFrom(BooleanValue.class)) return 0;
		if(javaClass == Boolean.class || javaClass == boolean.class) return 1;
		if(javaClass == Object.class) return 20;
		if(javaClass == String.class || javaClass == CharSequence.class) return 2;
		
		return Integer.MAX_VALUE;
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.Item#toJavaObject(java.lang.Class)
	 */
	public Object toJavaObject(Class target) throws XPathException {
		if(target.isAssignableFrom(BooleanValue.class))
			return this;
		else if(target == Boolean.class || target == boolean.class || target == Object.class)
			return Boolean.valueOf(value);
		else if(target == String.class || target == CharSequence.class) {
			StringValue v = (StringValue)convertTo(Type.STRING);
			return v.value;
		}
		
		throw new XPathException("cannot convert value of type " + Type.getTypeName(getType()) +
			" to Java object of type " + target.getName());
	}
}