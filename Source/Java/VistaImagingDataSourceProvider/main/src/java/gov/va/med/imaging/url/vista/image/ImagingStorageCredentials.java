/**
 * 
  Package: MAG - VistA Imaging
  WARNING: Per VHA Directive 2004-038, this routine should not be modified.
  Date Created: Sep 17, 2010
  Site Name:  Washington OI Field Office, Silver Spring, MD
  Developer:  vhaiswwerfej
  Description: 

        ;; +--------------------------------------------------------------------+
        ;; Property of the US Government.
        ;; No permission to copy or redistribute this software is given.
        ;; Use of unreleased versions of this software requires the user
        ;;  to execute a written test agreement with the VistA Imaging
        ;;  Development Office of the Department of Veterans Affairs,
        ;;  telephone (301) 734-0100.
        ;;
        ;; The Food and Drug Administration classifies this software as
        ;; a Class II medical device.  As such, it may not be changed
        ;; in any way.  Modifications to this software may result in an
        ;; adulterated medical device under 21CFR820, the use of which
        ;; is considered to be a violation of US Federal Statutes.
        ;; +--------------------------------------------------------------------+

 */
package gov.va.med.imaging.url.vista.image;

import gov.va.med.imaging.core.interfaces.StorageCredentials;

/**
 * @author vhaiswwerfej
 *
 */
public class ImagingStorageCredentials 
implements StorageCredentials
{
	private final String siteNumber;
	private final String username;
	private final String password;
	
	public ImagingStorageCredentials(String siteNumber, String username, String password)
	{
		this.siteNumber = siteNumber;
		this.password = password;
		this.username = username;
	}

	/* (non-Javadoc)
	 * @see gov.va.med.imaging.core.interfaces.StorageCredentials#getPassword()
	 */
	@Override
	public String getPassword()
	{
		return password;
	}

	/* (non-Javadoc)
	 * @see gov.va.med.imaging.core.interfaces.StorageCredentials#getUsername()
	 */
	@Override
	public String getUsername()
	{
		return username;
	}

	public String getSiteNumber()
	{
		return siteNumber;
	}

}