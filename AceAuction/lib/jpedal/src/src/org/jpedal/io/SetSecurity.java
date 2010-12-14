/*
 * Created on 28-Apr-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.jpedal.io;

import org.jpedal.utils.LogWriter;

import java.security.Security;

/**
 * @author markee
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SetSecurity {

	
	public static void init(){

        //allow user to over-ride
        String altSP=System.getProperty("org.jpedal.securityprovider");

        if(altSP==null)
            altSP="org.bouncycastle.jce.provider.BouncyCastleProvider";

        try {

            Class c = Class.forName(altSP);
            java.security.Provider provider = (java.security.Provider) c.newInstance();

            Security.addProvider(provider);
        } catch (Exception e) {

            LogWriter.writeLog("Unable to run custom security provider " + altSP);
            LogWriter.writeLog("Exception " + e);


            throw new RuntimeException("Unable to use security library "+e);


        }
    }
}
