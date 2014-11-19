/**
 * 
 */
package com.harmeetsingh13.reverseengstrategy;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;

/**
 * @author Harmeet Singh(Taara)
 *
 */
public class MyReverseEngStrategy extends DelegatingReverseEngineeringStrategy{

	public MyReverseEngStrategy(ReverseEngineeringStrategy delegate) {
		super(delegate);
	}

	@Override
	public String tableToClassName(TableIdentifier tableIdentifier) {
		String fullClassName = super.tableToClassName(tableIdentifier);
		String packageName = fullClassName.substring(0, fullClassName.lastIndexOf(".")+1);
		String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
		if(className.startsWith("OauthClientDetails")){
			return packageName + "User";
		}else{
			return packageName + className;
		}
	}
}
