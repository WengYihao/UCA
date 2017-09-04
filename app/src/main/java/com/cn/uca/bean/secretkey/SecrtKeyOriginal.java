package com.cn.uca.bean.secretkey;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class SecrtKeyOriginal extends SecrtKey {
	private static final long serialVersionUID = -3197769738477537281L;
	private RSAPublicKey publicKey;// 公钥
	private RSAPrivateKey privateKey;// 私钥

	public SecrtKeyOriginal(KeyPair keyPair, RSAPublicKey publicKey, RSAPrivateKey privateKey) {
		super(keyPair);
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	public RSAPublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(RSAPublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public RSAPrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(RSAPrivateKey privateKey) {
		this.privateKey = privateKey;
	}
}
