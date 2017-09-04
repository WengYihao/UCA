package com.cn.uca.bean.secretkey;

import java.security.KeyPair;

public class SecrtKeyBase64 extends SecrtKey {
	private static final long serialVersionUID = -8512731835973521989L;
	private String publicKey;// 公钥
	private String privateKey;// 私钥

	public SecrtKeyBase64(KeyPair keyPair, String publicKey, String privateKey) {
		super(keyPair);
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	@Override
	public String toString() {
		return "SecrtKeyBase64 [publicKey=" + publicKey + ", privateKey=" + privateKey + "]";
	}
}