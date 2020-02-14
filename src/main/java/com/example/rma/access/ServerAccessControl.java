package com.example.rma.access;

import java.io.IOException;

import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;

import com.example.rma.access.AccessControl;
import com.example.rma.access.CurrentUser;
import com.example.rma.classes.ConnectionManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class ServerAccessControl
implements AccessControl {
	
    public String signIn(ConnectionManager manager, String username, String password) {
    	
    	//String auth_pw = this.generatePWHash(password);
    	String result = "";
    	
    	int indexAt;
    	indexAt = username.indexOf("@");
    	
    	//connect to server
    	LdapConnection auth = new LdapNetworkConnection("172.16.1.4", 389);
    	
    	System.out.println("-- signIn --");
    	try {
			auth.bind("CN=Administrator, CN=Users, DC=mgenesis, DC=local", "JpxL0F92");
			
			EntryCursor cursor = auth.search( 
					"OU=User, OU=TSC, DC=mgenesis, DC=local", 
					"(objectclass=*)", SearchScope.ONELEVEL 
					);
			//System.out.println("TSC");
			
			for ( Entry entry : cursor )
		    {
				//System.out.println(entry.get("userPrincipalName"));
				if ((entry.get("mailNickname") != null 
						&& entry.get("mailNickname").contains(username.substring(0, indexAt))) 
							|| (entry.get("userPrincipalName") != null 
								&& entry.get("userPrincipalName").toString().contains(username.substring(0, indexAt)))) {
					System.out.println("Found it in TSC!");
					
					//success
					result = "Admin";
					CurrentUser.set(result, "TSC", username);
					break;
				}
		    }
			
			if (result.isEmpty()) {
				cursor = auth.search( 
						"OU=User, OU=Logistics, DC=mgenesis, DC=local", 
						"(objectclass=*)", SearchScope.ONELEVEL 
						);
				//System.out.println("Logistics");
				
				for (Entry entry: cursor) {
					//System.out.println(entry.get("mailNickname"));
					if ((entry.get("mailNickname") != null 
							&& entry.get("mailNickname").contains(username.substring(0, indexAt))) 
							|| (entry.get("userPrincipalName") != null 
								&& entry.get("userPrincipalName").toString().contains(username.substring(0, indexAt)))) {
						System.out.println("Found it in Logistics!");
						
						//success
						result = "Admin";
						CurrentUser.set(result, "LOG", username);
						break;
					}
				}
			}
			
		} catch (LdapException e) {
			//failure
			System.out.println("Failed to authenticate with LDAP server");
			e.printStackTrace();
		}
    	
    	try {
    		//close the connection
			auth.unBind();
			auth.close();
		} catch (LdapException e) {
			System.out.println("Failed to unbind from LDAP server");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Failed to close connection with LDAP server");
			e.printStackTrace();
		}
        
        return result;
    }

    public boolean isUserSignedIn() {
        return !CurrentUser.get().isEmpty();
    }

    public boolean isUserInRole(String role) {
        return this.isRoleValid(role);
    }

    public boolean isRoleValid(String role) {
        return "Admin".equals(role) | "Viewer".equals(role);
    	//return true;
    }

    public String getPrincipalName() {
        return CurrentUser.get();
    }
    
    private String generatePWHash(String pw) {
    	String auth_pw = pw;
    	MessageDigest md;
    	
    	try {
			md = MessageDigest.getInstance("MD5");
			md.update(auth_pw.getBytes());
	    	return DatatypeConverter.printHexBinary(md.digest());
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
			return "";
		}	
    }
}

