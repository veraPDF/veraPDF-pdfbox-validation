/**
 * 
 */
package org.verapdf.pdfa;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 26 Oct 2016:22:24:12
 */

public class PdfBoxFoundryProvider implements VeraFoundryProvider {
	private static final VeraFoundryProvider instance = new PdfBoxFoundryProvider(); 
	private PdfBoxFoundryProvider() {
	}
	
	public static void initialise() {
		Foundries.registerDefaultProvider(instance);
	}
	/**
	 * @see org.verapdf.pdfa.VeraFoundryProvider#getInstance()
	 */
	@Override
	public VeraPDFFoundry getInstance() {
		return PdfBoxFoundry.getInstance();
	}

}
