/**
 * 
 */
package gov.va.med;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import gov.va.med.exceptions.RoutingTokenFormatException;
import gov.va.med.imaging.exceptions.OIDFormatException;

/**
 * A simple value object implementation of RoutingToken
 * that may be used for routing requests that do not contain a
 * route-able identifier.
 * 
 * @author vhaiswbeckec
 *
 */
public class RoutingTokenImpl
implements RoutingToken, Comparable<RoutingToken>, Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	public static boolean isEquivalent(RoutingToken one, RoutingToken two)
	{
		// if both home community IDs can be converted to an OID then use that class
		// to determine home community ID equivalence
		
		try
		{
			OID homeCommunityOidOne = OID.create(one.getHomeCommunityId());
			OID homeCommunityOidTwo = OID.create(two.getHomeCommunityId());
			
			return homeCommunityOidOne.compareTo(homeCommunityOidTwo) == 0 && 
			 one.getRepositoryUniqueId().equalsIgnoreCase(two.getRepositoryUniqueId());
		}
		catch (OIDFormatException x)
		{
			return one.getHomeCommunityId().equalsIgnoreCase(two.getHomeCommunityId()) && 
				one.getRepositoryUniqueId().equalsIgnoreCase(two.getRepositoryUniqueId());
		}
	}
	
	/**
	 * 
	 * @param outer
	 * @param inner
	 * @return
	 */
	public static boolean isIncluding(RoutingToken outer, RoutingToken inner)
	{
		if(isEquivalent(outer, inner))
			return true;
		
		if( RoutingToken.ROUTING_WILDCARD.equals(outer.getHomeCommunityId()) )
		{
			return RoutingToken.ROUTING_WILDCARD.equals(outer.getRepositoryUniqueId()) ||
				outer.getRepositoryUniqueId().equalsIgnoreCase(inner.getRepositoryUniqueId());
		}
		
		try
		{
			OID homeCommunityOidOuter = OID.create(outer.getHomeCommunityId());
			OID homeCommunityOidInner = OID.create(inner.getHomeCommunityId());
			
			return homeCommunityOidOuter.compareTo(homeCommunityOidInner) == 0 &&
				(RoutingToken.ROUTING_WILDCARD.equals(outer.getRepositoryUniqueId()) ||
				 outer.getRepositoryUniqueId().equalsIgnoreCase(inner.getRepositoryUniqueId()));
		}
		catch (OIDFormatException x)
		{
			return outer.getHomeCommunityId().equalsIgnoreCase(inner.getHomeCommunityId()) &&
				(RoutingToken.ROUTING_WILDCARD.equals(outer.getRepositoryUniqueId()) ||
				 outer.getRepositoryUniqueId().equalsIgnoreCase(inner.getRepositoryUniqueId()));
		}
	}
	
	/**
	 * 
	 * @param homeCommunityId
	 * @return
	 * @throws RoutingTokenFormatException
	 */
	public static RoutingToken create(OID homeCommunityId)
	throws RoutingTokenFormatException
	{
		return create(homeCommunityId.toString());
	}
	
	/**
	 * Create a RoutingToken that will route to all repository
	 * within the home community or to the gateway representing
	 * the given home community.
	 * 
	 * @param homeCommunityId
	 * @return
	 */
	public static RoutingToken create(String homeCommunityId)
	throws RoutingTokenFormatException
	{
		return create(homeCommunityId, RoutingToken.ROUTING_WILDCARD);
	}
	
	/**
	 * 
	 * @param routingToken
	 * @return
	 */
	public static RoutingTokenImpl create(RoutingToken routingToken)
	{
		return new RoutingTokenImpl(routingToken.getHomeCommunityId(), routingToken.getRepositoryUniqueId());
	}
	
	/**
	 * 
	 * @param homeCommunityId
	 * @param repositoryUniqueId
	 * @return
	 * @throws RoutingTokenFormatException
	 */
	public static RoutingToken create(OID homeCommunityId, String repositoryUniqueId) 
	throws RoutingTokenFormatException
	{
		return create(homeCommunityId == null ? (String)null : homeCommunityId.toString(), repositoryUniqueId);
	}

	/**
	 * Create an instance of a RoutingTokenImpl using a String generated by a 
	 * RoutingToken.toRoutingTokenString() call.
	 * 
	 * @see #toRoutingTokenString()
	 * @see #RoutingTokenImpl(RoutingTokenMemento)
	 * 
	 * @param serializedRoutingToken
	 * @return
	 * @throws RoutingTokenFormatException
	 */
	public static RoutingToken parse(String serializedRoutingToken) 
	throws RoutingTokenFormatException
	{
		return RoutingTokenImpl.parse(serializedRoutingToken, SERIALIZATION_FORMAT.RAW);
	}
	
	public static RoutingToken parse(String serializedRoutingToken, SERIALIZATION_FORMAT serializationFormat) 
	throws RoutingTokenFormatException
	{
		// deserialize in its entirety
		serializedRoutingToken = serializationFormat.deserialize(serializedRoutingToken);

		int commaIndex = serializedRoutingToken.indexOf(',');
		if(commaIndex <= 0 || commaIndex >= (serializedRoutingToken.length()-1) )
			throw new RoutingTokenFormatException("The value '" + serializedRoutingToken + 
				"' is not a valid form of a routing token, should be '<home community OID>,<repository ID>'");
		String homeCommunityOid = serializedRoutingToken.substring(0, commaIndex);
		String repositoryId = serializedRoutingToken.substring(commaIndex+1);
		
		return RoutingTokenImpl.create(homeCommunityOid, repositoryId);
	}
	
	/**
	 * Create a RoutingToken that will route to a single repository
	 * within the home community or to the gateway representing
	 * the given home community.
	 * 
	 * @param homeCommunityId
	 * @param repositoryUniqueId
	 * @return
	 */
	public static RoutingToken create(String homeCommunityId, String repositoryUniqueId)
	throws RoutingTokenFormatException
	{
		if(homeCommunityId == null)
			throw new RoutingTokenFormatException("Attempt to create a RoutingToken with a null HomeCommunityID.");
		if(repositoryUniqueId == null)
			throw new RoutingTokenFormatException("Attempt to create a RoutingToken with a null RepositoryUniqueID.");
		
		return new RoutingTokenImpl(homeCommunityId, repositoryUniqueId);
	}

	/**
	 * @param that
	 * @return
	 */
	public static String toRoutingTokenString(RoutingToken that) 
	{
		return RoutingTokenImpl.toRoutingTokenString(that, SERIALIZATION_FORMAT.RAW);
	}
	
	/**
	 * @param that
	 * @return
	 */
	public static String toRoutingTokenString(RoutingToken that, SERIALIZATION_FORMAT serializationFormat) 
	{
		String unencoded = that.getHomeCommunityId() + "," + that.getRepositoryUniqueId();
		return serializationFormat.serialize(unencoded);
	}

	/**
	 * Create a routing token for a VA radiology artifact at the given site.
	 * 
	 * @param siteNumber
	 * @return
	 * @throws RoutingTokenFormatException
	 */
	public static RoutingToken createVARadiologySite(String siteNumber)
	throws RoutingTokenFormatException
	{
		if(siteNumber == null)
			throw new RoutingTokenFormatException("Attempt to create a RoutingToken with a null RepositoryUniqueID.");
		
		return create(WellKnownOID.VA_RADIOLOGY_IMAGE.getCanonicalValue().toString(), siteNumber);
	}
	
	public static RoutingToken createDoDRadiologySite(String repositoryId)
	throws RoutingTokenFormatException
	{
		if(repositoryId == null)
			throw new RoutingTokenFormatException("Attempt to create a RoutingToken with a null RepositoryUniqueID.");
		
		return create(WellKnownOID.BHIE_RADIOLOGY.getCanonicalValue().toString(), repositoryId);
	}
	
	/**
	 * 
	 * @param siteNumbers
	 * @return
	 * @throws RoutingTokenFormatException
	 */
	public static Collection<RoutingToken> createVARadiologySites(String[] siteNumbers)
	throws RoutingTokenFormatException
	{
		if(siteNumbers == null)
			throw new RoutingTokenFormatException("Attempt to create a RoutingToken with a null RepositoryUniqueID.");
		
		Collection<RoutingToken> routingTokens = new ArrayList<RoutingToken>();
		for(String siteNumber : siteNumbers)
			routingTokens.add( createVARadiologySite(siteNumber) );
		
		return routingTokens;
	}
	
	/**
	 * Create a routing token for a VA document artifact at the given site.
	 * 
	 * @param siteNumber
	 * @return
	 * @throws RoutingTokenFormatException
	 */
	public static RoutingToken createVADocumentSite(String siteNumber)
	throws RoutingTokenFormatException
	{
		if(siteNumber == null)
			throw new RoutingTokenFormatException("Attempt to create a RoutingToken with a null RepositoryUniqueID.");
		
		return create(WellKnownOID.VA_DOCUMENT.getCanonicalValue().toString(), siteNumber);
	}

	public static RoutingToken createDoDDocumentSite(String repositoryId)
	throws RoutingTokenFormatException
	{
		if(repositoryId == null)
			throw new RoutingTokenFormatException("Attempt to create a RoutingToken with a null RepositoryUniqueID.");
		
		return create(WellKnownOID.HAIMS_DOCUMENT.getCanonicalValue().toString(), repositoryId);
	}

	/**
	 * 
	 * @param siteNumbers
	 * @return
	 * @throws RoutingTokenFormatException
	 */
	public static Collection<RoutingToken> createVADocumentSites(String[] siteNumbers)
	throws RoutingTokenFormatException
	{
		if(siteNumbers == null)
			throw new RoutingTokenFormatException("Attempt to create a RoutingToken with a null RepositoryUniqueID.");
		
		Collection<RoutingToken> routingTokens = new ArrayList<RoutingToken>();
		for(String siteNumber : siteNumbers)
			routingTokens.add( createVADocumentSite(siteNumber) );
		
		return routingTokens;
	}
	
	// ==============================================================================================
	// Instance Members
	// ==============================================================================================
	private final String homeCommunityId;
	private final String repositoryUniqueId;
	
	public RoutingTokenImpl(RoutingTokenMemento memento)
	{
		super();
		this.homeCommunityId = memento.getHomeCommunityId();
		this.repositoryUniqueId = memento.getRepositoryUniqueId();
	}
	
	/**
	 * @param homeCommunityId
	 * @param repositoryUniqueId
	 */
	private RoutingTokenImpl(String homeCommunityId, String repositoryUniqueId)
	{
		super();
		this.homeCommunityId = homeCommunityId;
		this.repositoryUniqueId = repositoryUniqueId;
	}

	/**
	 * @see gov.va.med.RoutingToken#getHomeCommunityId()
	 */
	@Override
	public String getHomeCommunityId()
	{
		return homeCommunityId;
	}

	/**
	 * @see gov.va.med.RoutingToken#getRepositoryUniqueId()
	 */
	@Override
	public String getRepositoryUniqueId()
	{
		return repositoryUniqueId;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RoutingToken that)
	{
		return RoutingTokenImpl.compare(this, that);
	}

	/**
	 * This comparator should work with any RoutingToken
	 * implementations.
	 * 
	 * @param thisOne
	 * @param thatTwo
	 * @return
	 */
	public static int compare(RoutingToken thisOne, RoutingToken thatTwo)
	{
		int homeCommunityCompare = thisOne.getHomeCommunityId().compareTo(thatTwo.getHomeCommunityId());
		if(homeCommunityCompare != 0)
			return homeCommunityCompare;
		
		if(RoutingToken.ROUTING_WILDCARD.equals(thisOne.getRepositoryUniqueId()))
			return 1;
		
		if(RoutingToken.ROUTING_WILDCARD.equals(thatTwo.getRepositoryUniqueId()))
			return -1;
		
		return thisOne.getRepositoryUniqueId().compareTo(thatTwo.getRepositoryUniqueId());
	}

	
	@Override
	public boolean isEquivalent(RoutingToken that)
	{
		return RoutingTokenImpl.isEquivalent(this, that);
	}

	@Override
	public boolean isIncluding(RoutingToken that)
	{
		return RoutingTokenImpl.isIncluding(this, that);
	}

	@Override
	public String toRoutingTokenString() 
	{
		return RoutingTokenImpl.toRoutingTokenString(this);
	}

	// ================================================================================================
	// Eclipse Generated hashCode() and equals()
	// ================================================================================================
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.homeCommunityId == null) ? 0 : this.homeCommunityId.hashCode());
		result = prime * result + ((this.repositoryUniqueId == null) ? 0 : this.repositoryUniqueId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RoutingTokenImpl other = (RoutingTokenImpl) obj;
		if (this.homeCommunityId == null)
		{
			if (other.homeCommunityId != null)
				return false;
		}
		else if (!this.homeCommunityId.equalsIgnoreCase(other.homeCommunityId))
			return false;
		if (this.repositoryUniqueId == null)
		{
			if (other.repositoryUniqueId != null)
				return false;
		}
		else if (!this.repositoryUniqueId.equalsIgnoreCase(other.repositoryUniqueId))
			return false;
		return true;
	}

	@Override
	protected Object clone() 
	throws CloneNotSupportedException
	{
		return new RoutingTokenImpl(this.getHomeCommunityId(), this.getRepositoryUniqueId());
	}

	@Override
	public String toString()
	{
		return RoutingTokenImpl.toRoutingTokenString(this, SERIALIZATION_FORMAT.RAW);
	}
}