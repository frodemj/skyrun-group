package anagram.server;


import java.util.Map;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;

import anagram.client.SecurityService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SecurityServiceImpl extends RemoteServiceServlet implements
		SecurityService {
	private Log4JLogger log = new Log4JLogger("securityService");
	private SecurityManager securityManager;
	private IniSecurityManagerFactory factory;

//	public SecurityServiceImpl() {
//		factory = new IniSecurityManagerFactory("classpath:shiro.ini");
//		securityManager = factory.getInstance();
//	    SecurityUtils.setSecurityManager(securityManager);
//	}


	@Override
	public String login(String name) {
		throw new UnsupportedOperationException("OpenID support is currently missing");
	}

	@Override
	public void logout() {
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.isAuthenticated() ) {
			currentUser.logout();
		    log.info( "User [" + currentUser.getPrincipal() + "] logged out successfully." );
		}
	}

	@Override
	public String login(String name, String pass) {
//		show(factory);

		System.out.printf("login(%s,%s)%n", name, pass);
		Subject currentUser = SecurityUtils.getSubject();
		if ( !currentUser.isAuthenticated() ) {
		    //collect user principals and credentials in a gui specific manner 
		    //such as username/password html form, X509 certificate, OpenID, etc.
		    //We'll use the username/password example here since it is the most common.
		    UsernamePasswordToken token = new UsernamePasswordToken(name, pass);

		    //this is all you have to do to support 'remember me' (no config - built in!):
		    token.setRememberMe(true);
		    try {
		    	currentUser.login(token);
			    log.info( "User [" + currentUser.getPrincipal() + "] logged in successfully." );
		    	return name;
		    } catch (Exception e) {
		    	log.error("login failed", e);
		    	throw new RuntimeException("Illegal login credentials: " + e.getMessage());
		    }
		} else {
			return (String) currentUser.getPrincipal();
		}
	}


	private void show(IniSecurityManagerFactory factory) {
		for (Map.Entry<String, ?> e : factory.getBeans().entrySet()) {
			System.out.printf("%s : %s%n", e.getKey(), e.getValue().getClass().getName());
			if (e.getValue() instanceof IniRealm) {
				show((IniRealm) e.getValue());
			}
		}
	}


	private void show(IniRealm realm) {
		System.out.printf("guest exists:%s%n", realm.accountExists("guest"));
		System.out.printf("fmj exists:%s%n", realm.accountExists("fmj"));
//		realm.
	}

}
