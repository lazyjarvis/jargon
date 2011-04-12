/**
 * 
 */
package org.irods.jargon.usertagging.domain;

import java.io.Serializable;

import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.query.MetaDataAndDomainData;
import org.irods.jargon.usertagging.UserTaggingConstants;

/**
 * Represents a free tag for iRODS data.   This is an immutable object, and is validated when constructed
 * @author Mike Conway - DICE (www.irods.org)
 *
 */
public final class IRODSTagValue implements Serializable, Comparable<Object> {
	
	private static final long serialVersionUID = 8220689134583599950L;
	private  final String tagData;
	private final  String tagUser;
	
	/**
	 * Construct a tag with the given value
	 * @param tagData <code>String</code> with the tag data value for the tag.
	 * @param user <code>String</code> with the user name that owns the tag.
	 * @throws JargonException
	 */
	public IRODSTagValue(final String tagData, final String tagUser) throws JargonException {
		
		if (tagData == null || tagData.isEmpty()) {
			throw new JargonException("null or empty tagData");
		}
		
		if (tagUser == null || tagUser.isEmpty()) {
			throw new JargonException("null or empty tagUser");
		}
		
		this.tagData = tagData;
		this.tagUser = tagUser;
		
	}
	
	/**
	 * Special constructor that can interpret AVU data as a tag.  Note that the tag is in the AVU attribute, and the user is in the AVU Value
	 * as <code>user[username]
	 * @param metadataAndDomainData <code>MetaDataAndDomainData</code> object from Jargon that contains information from a raw AVU triple.
	 * @throws JargonException
	 */
	public IRODSTagValue(final MetaDataAndDomainData metadataAndDomainData) throws JargonException {
		
		if (metadataAndDomainData == null) {
			throw new JargonException("null metadataAndDomainData");
		}
		
		if (metadataAndDomainData.getAvuValue() == null || metadataAndDomainData.getAvuValue().isEmpty()) {
			throw new JargonException("no user supplied");
		}
		
		this.tagData = metadataAndDomainData.getAvuAttribute();
		this.tagUser = metadataAndDomainData.getAvuValue();
		
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("IRODSTagValue");
		sb.append("\n   tagData:");
		sb.append(tagData);
		sb.append("\n   tagUser:");
		sb.append(tagUser);
		return sb.toString();
	}

	public String getTagData() {
		return tagData;
	}

	public String getTagUser() {
		return tagUser;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IRODSTagValue)) {
			return false;
		}
		
		IRODSTagValue other = (IRODSTagValue) obj;
		return (other.getTagData().equals(getTagData()) && other.getTagUser().equals(getTagUser()));
	
	}

	@Override
	public int hashCode() {
		return getTagData().hashCode() + getTagUser().hashCode();
	}

	@Override
	public int compareTo(Object o) {
		
		if (!(o instanceof IRODSTagValue)) {
			throw new ClassCastException("object is not an instance of IRODSTagValue");
		}
		
		IRODSTagValue otherValue = (IRODSTagValue) o;
		return this.getTagData().compareTo(otherValue.getTagData());
	
	}

}
