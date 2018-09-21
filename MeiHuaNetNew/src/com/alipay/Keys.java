/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	public static final String DEFAULT_PARTNER = "2088001308765581";

	public static final String DEFAULT_SELLER = "2088001308765581";
	
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYWTAAvWntm4tshNiIobCANayvJTzOkxrhXMp7 bCF2dghDO1gDhQxXgJlpXdEE76ZBd2HMfx+xspvUO4alLV25VIVYMjDAMoVWG7drOTHNZ+m5AeN1 ijMPp+SxUPWW5vkiB5pb7LXGHxQAXBTkjEVZt7ugeZ1o4LuAmFrzglXDHQIDAQAB";

	public static final String PRIVATE ="MIIBOwIBAAJBAMp3ubXUcFXJ0D3upIubfcS9+NGtTfrvY37ykGIrmfC3Y1nLbjpFyj49bdeRcn7kiPsYVALDTPn4XHiFOMggfD8CAwEAAQJAQ22wGZBQgSGfCqEph50XO6iQ7zOqXsHO/v5Fe4xNzk6M4yzQm9DRhsmaWnoo/Y/I0/oZe+A5qUPWRYtFMmr7MQIhAPV43hgXYnldU1rjIj7LFDfng2VLTGjzRf7aMBYLp4FLAiEA0yayCVmZbhy4PD2MOeAMA260y42SRZ5WOiImX+tFDF0CIQDMX0lp/W47ZXwU266TYTkAIny/RSXk60aHnNkUQbfAaQIgKK+IFq0hF0qJZXImlvBI4FBwndhFmwyLaOAzrL/UqjECIQDs0WMiVfsgn9bXQF5Xchgz/O1OObSDMI4i3mcFDIGSoQ==";
	
//	public static final String PRIVATE ="MIIBOwIBAAJBAMp3ubXUcFXJ0D3upIubfcS9+NGtTfrvY37ykGIrmfC3Y1nLbjpFyj49bdeRcn7kiPsYVALDTPn4XHiFOMggfD8CAwEAAQJAQ22wGZBQgSGfCqEph50XO6iQ7zOqXsHO/v5Fe4xNzk6M4yzQm9DRhsmaWnoo/Y/I0/oZe+A5qUPWRYtFMmr7MQIhAPV43hgXYnldU1rjIj7LFDfng2VLTGjzRf7aMBYLp4FLAiEA0yayCVmZbhy4PD2MOeAMA260y42SRZ5WOiImX+tFDF0CIQDMX0lp/W47ZXwU266TYTkAIny/RSXk60aHnNkUQbfAaQIgKK+IFq0hF0qJZXImlvBI4FBwndhFmwyLaOAzrL/UqjECIQDs0WMiVfsgn9bXQF5Xchgz/O1OObSDMI4i3mcFDIGSoQ==";
//
	
//	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYWTAAvWntm4tshNiIobCANayvJTzOkxrhXMp7 bCF2dghDO1gDhQxXgJlpXdEE76ZBd2HMfx+xspvUO4alLV25VIVYMjDAMoVWG7drOTHNZ+m5AeN1 ijMPp+SxUPWW5vkiB5pb7LXGHxQAXBTkjEVZt7ugeZ1o4LuAmFrzglXDHQIDAQAB";
}
