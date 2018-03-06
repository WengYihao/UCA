package com.cn.uca.config;

import android.net.Uri;
import android.os.Environment;

import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.io.File;

public class Constant {
	final public static int PAGE = 1; //当前页数
	final public static int PAGE_COUNT = 10;//当前记录数
	final public static String userIdKey = "UserInfo";       //保存用户信息的key
	final public static String TAG = "UCA";
	final public static int PHOTO_REQUEST_TAKEPHOTO = 1;  // 拍照
	final public static int PHOTO_REQUEST_GALLERY = 2;     // 从相册中选择
	final public static int PHOTO_REQUEST_CUT = 3;        // 结果
	final public static int CODE_GALLERY_REQUEST = 4;
	final public static int CAMERA_PERMISSIONS_REQUEST_CODE = 5;
	final public static int STORAGE_PERMISSIONS_REQUEST_CODE = 6;
	final public static int WRITE_PERMISSIONS_REQUEST_CODE = 1;
	final public static int ROUTE_TYPE_BUS = 1;
	final public static int ROUTE_TYPE_DRIVE = 2;
	final public static int ROUTE_TYPE_WALK = 3;
	final public static String WX_APP_ID = "wx350d62481e115db6";
	// 微信开放平台appid
	final public static String WeChat_APP_ID = "wx350d62481e115db6";
	final public static String WeChat_APP_SECRET = "324211367c9d030e869d8732e3beb2db";
	//RSA加密私钥、公钥
	final public static String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALqFX/pz5096FGg/\r" +
			"bE4YXZ1U5EG/BEvGF1KCL8nK2oEWm+Lo0Y3wyk6iTGViI0vadLaz1RPbxsFdtQJz\n" +
			"+8/Eh6NaiBg+25phOgrlKLq1zKZoCYSnnymNsjBRPYAX9TndGb2oqMH4CD2NJiVH\n" +
			"DmILWDHvChKF7rVrC36gEhWTdO7VAgMBAAECgYAA4SIbtzIvAL9Z4nqrP5gpZr4Q\n" +
			"xUrK5Ks1e2GoTOmM1EN15W0fgzQg/MjpbXYHJIeKWPy3NEESplN1KIDWcPHGxFQx\n" +
			"PvOLJaEgX+CcQpGbO27q+Ha93v6wwQ7q9OG3Jw/aLG9zdYV/lFrR5QyY0qgmRgbK\n" +
			"3ly/u6E2K7rrXwGooQJBAO1Ys2lh2mgYsrGOoXeQOlsfJVNbAH0AgsRcGTqfDaBz\n" +
			"h4VI22fk2oKvS5BXGTFy8mKtjMyarHLxa+H3HQqOl/kCQQDJLhbWen81YfZwF+nf\n" +
			"ElWmD1HB3SbMSyeLfszvAaO46se2U4+YdVKXzaJQZ9tIGyg0fOt/ps3VWggFR5/y\n" +
			"65y9AkEApGwzdgjU/hJGEJ1HnIWs950/BVzgnLgy3wcSbzrqfMIsGxia7oFrIud/\n" +
			"SJIeINSJt/SUutVYLBS1cQf49WxNOQJBAIolfwM6zN2QbpScpzlKMsw8Ws0AGuNQ\n" +
			"fFA6+2joPE//0LIwATwU3GlniJ0kJB0IoPwiB+j5VwC5uSY+/+PkzRUCQGi2j0E9\n" +
			"2OxsqQUg9Pc35Xi1Dsdql5SrJGDqD8YM2qxEQ7XaDICmKkktzfBg4iRcFIBaB1fx\n" +
			"XIf1CDzDM+Hrllk=";
	final public static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC6hV/6c+dPehRoP2xOGF2dVORBvwRLxhdSgi/JytqBFpvi6NGN8MpOokxlYiNL2nS2s9UT28bBXbUCc/vPxIejWogYPtuaYToK5Si6tcymaAmEp58pjbIwUT2AF/U53Rm9qKjB+Ag9jSYlRw5iC1gx7woShe61awt+oBIVk3Tu1QIDAQAB";
	final public static String Refund_Note = "订单变更：\n" +
			"　　所有的订单变更，包括延期抵达、更改出行人数等，请您均事先通知我们，或者进入本APP中我的订单进行订单的修改或取消，以便我们能为您及时地调整，避免造成损失。 \n" +
			"\n" +
			"订单取消： \n" +
			"（1）领咖确认订单前不收取违约金；\n" +
			"（2）出行前三天取消订单，订单费用将全额退返到您的支付账户；\n" +
			"（3）出行前1—2天取消预定，将收取订单20%的违约金；\n" +
			"（4）出行前24小时内取消预订，将收取订单40%的违约金；\n" +
			"（5）出行前其他时间取消订单，不收取违约金。\n" +
			"（6） 在您订单生效后，如因领咖原因，致使您不能如期出行，我们将立即通知您，无条件退返您已支付的所有费用。\n" +
			"（7）如因目的地原因政府禁行或自然灾害等不可控因素导致您不能如期出行的，可及时联系我们取消订单或更改出行日期。";
	final public static String Reservation_Notice = "在订单开始前，请仔细阅读本须知。当您开始使用约咖成功下单时时，已表明您仔细阅读并接受协议的所有条款。\n" +
			"第一条.关于订单\n" +
			"产品内容主要包含：领咖目的地接待及其他服务，具体产品的最终包含内容以确认的订单约定内容为准。\n" +
			"1.您在进行产品选配时，应在提供的备选项目范围内进行。\n" +
			"2.联系方式：请正确、详细的输入您的联系信息并保持通讯畅通，以便领咖能及时地与您联系；如联系电话、联系方式等资料有误，产生一切后果由预订人自付。\n" +
			"3.16岁以下未成年人及65岁以上老年人或患有心脏病等疾病的人群，不可以单独预订，全部行程必须由家人陪同，行程内的后果自负。\n" +
			"\n" +
			"第二条.订单生效与解除\n" +
			"1.只有您付清应付费用，确认的订单才会生效。否则产生的相关变动我们将不承担任何责任。\n" +
			"2.订单生效后，如您在出发前3天通知取消生效订单，我们会帮您立即取消，且不收取任何费用，如果超出这个时间且在出行24小时前，您必须承担处理该订单已经支出的10%费用。如在出行24小时之内取消，我们将收取20%的服务费。\n" +
			"3.在您订单生效后，如因目的地领咖原因，致使您不能如期出行的，我们将立即通知您，无条件退返您已支付的所有费用。\n" +
			"4.如因目的地政府禁行或自然灾害（如地震、海啸、台风、泥石流）等不可控因素导致您不能如期出行的，可及时联系我们取消订单或更改出行日期。\n" +
			" \n" +
			"第三条.更改订单\n" +
			"订单生效后，您若需要更改该订单内一些项目，请务必在旅游活动开始前及时联系领咖。如领咖应允，即可更改，但您必须全额承担因变更带来的损失及可能增加的费用。\n" +
			" 第四条．注意事项\n" +
			"1.为保障您的利益，请不要通过游咖以外的第三方聊天工具与领咖联系，或线下与领咖联系，或与领咖交易，平台仅以聊天记录和真实订单作为产生纠纷的调查依据。\n" +
			"2.游咖平台仅提供信息展示，购买服务由领咖提供，如需发票，请与领咖沟通。";
}
