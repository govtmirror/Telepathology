/**
 * 
  Package: MAG - VistA Imaging
  WARNING: Per VHA Directive 2004-038, this routine should not be modified.
  Date Created: Sep 24, 2010
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
package gov.va.med.imaging.exchange.business;

import gov.va.med.imaging.exchange.enums.ArtifactResultErrorCode;
import gov.va.med.imaging.exchange.enums.ArtifactResultErrorSeverity;

/**
 * Interface which defines a possible artifact result error.  This interface is based on (but not exactly) XCA.
 * This type must be implemented to create an artifact result error.
 * <br><br>
 * ArtifactResultError should <b>NOT</b> be used for general exceptions such as a misconfigured server
 * 
 * @author vhaiswwerfej
 *
 */
public interface ArtifactResultError
{	
	/**
	 * Return a description of the error. This field is optional
	 * @return
	 */
	public String getCodeContext();
	
	/**
	 * Return the severity of the error. This field is not optional and must be specified
	 * @return
	 */
	public ArtifactResultErrorSeverity getSeverity();
	
	/**
	 * Return the location the error came from. In many cases this will be the repository Id of the source of 
	 *  the error. In VA terms this will be the site number. This value should be understandable to the home 
	 *  community the error came from.  The location does not have to be specified (if it is a general error)
	 * @return
	 */
	public String getLocation();

	/**
	 * Error code describing the type of the error.  This field is not optional and must be specified
	 * @return
	 */
	public ArtifactResultErrorCode getErrorCode();

}