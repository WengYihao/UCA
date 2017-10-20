package com.cn.uca.bean.secretkey;

import java.io.Serializable;
import java.security.KeyPair;

/**
 * 加密
 */
public class SecrtKey implements Serializable {
	private static final long serialVersionUID = 3081685758222527707L;
	private KeyPair keyPair;

	public SecrtKey(){

	}

	public SecrtKey(KeyPair keyPair){
		this.keyPair = keyPair;
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}

	public void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
	}
}