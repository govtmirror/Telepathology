/**
 * 
  Package: MAG - VistA Imaging
  WARNING: Per VHA Directive 2004-038, this routine should not be modified.
  Date Created: Feb 1, 2012
  Site Name:  Washington OI Field Office, Silver Spring, MD
  Developer:  VHAISWWERFEJ
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
package gov.va.med.imaging.exchange.business.taglib.welcomemessage;

/**
 * @author VHAISWWERFEJ
 *
 */
public class WelcomeMessageErrorMessageTag
extends WelcomeMessageEntryElement
{
	private static final long serialVersionUID = 1262526278897045333L;

	/* (non-Javadoc)
	 * @see gov.va.med.imaging.exchange.business.taglib.welcomemessage.WelcomeMessageEntryElement#getElementValue()
	 */
	@Override
	protected String getElementValue()
	{
		WelcomeMessageTag welcomeMessageTag = getWelcomeMessageTag();
		if(welcomeMessageTag != null)
		{
			if(welcomeMessageTag.getErrorMessage() != null && welcomeMessageTag.getErrorMessage().length() > 0)
			{
				String errorMessage = "There was an error communicating with VistA, please refresh the page to try again. If this error persists please contact your system administrator.<hr/>Error Details:<br />" + welcomeMessageTag.getErrorMessage();				
				return errorMessage;
			}
		}
		return "";
	}

}