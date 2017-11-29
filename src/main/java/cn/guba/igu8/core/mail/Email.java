/**
 * 
 */
package cn.guba.igu8.core.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.repo.Base64;

import cn.guba.igu8.core.constants.Constant;

/**
 * @author zongtao liu
 *
 */
public class Email {

	private static Log log = Logs.get();

	private static final String SMPT_HOST_QQ = "smtp.qq.com";
	private static final String SMPT_HOST_163 = "smtp.163.com";

	/***
	 * 账号
	 */
	private String account = "";

	/***
	 * 密码
	 */
	private String password = "";

	/***
	 * 账号名
	 */
	private String accountName = "";

	/***
	 * smtp地址
	 */
	private String smtpHost = "";

	/***
	 * 附件地址
	 */
	private String affix = "";
	/***
	 * 附件名称
	 */
	private String affixName = "";

	private Session session;

	public Email(String account, String password, String accountName, String smtpHost) {
		init(account, password, null, smtpHost);
	}

	public Email(String account, String password) {
		init(account, password, null, null);
	}

	public Email(String account, String password, String accountName) {
		init(account, password, accountName, null);
	}

	private void init(String account, String password, String accountName, String smtpHost) {
		if (Strings.isNotBlank(account)) {
			String[] accountInfoArr = account.split("@");
			int infoSize = accountInfoArr.length;
			if (infoSize == 2) {
				String mailAddr = accountInfoArr[1];
				if (mailAddr.equals("163.com")) {
					this.smtpHost = SMPT_HOST_163;
				} else if (mailAddr.equals("qq.com")) {
					this.smtpHost = SMPT_HOST_QQ;
				} else {
					this.smtpHost = smtpHost;
				}
				this.account = account;
				this.password = password;
				if (accountName != null) {
					this.accountName = accountName;
				}
			}
		}

		// 1. 创建参数配置, 用于连接邮件服务器的参数配置
		Properties props = new Properties(); // 参数配置
		props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
		props.setProperty("mail.smtp.host", this.smtpHost); // 发件人的邮箱的 SMTP
															// 服务器地址
		props.setProperty("mail.smtp.auth", "true"); // 需要请求认证

		// PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
		// 如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
		// 打开下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。
		/***
		 * SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接, 需要改为对应邮箱的 SMTP
		 * 服务器的端口, 具体可查看对应邮箱服务的帮助, QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
		 ***/
		final String smtpPort = "465";
		props.setProperty("mail.smtp.port", smtpPort);
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", smtpPort);

		// 2. 根据配置创建会话对象, 用于和邮件服务器交互
		this.session = Session.getDefaultInstance(props);
		// session.setDebug(true);
	}

	/***
	 * 设置附件
	 * 
	 * @param affix
	 * @param affixName
	 */
	public void setAffix(String affix, String affixName) {
		this.affix = affix;
		this.affixName = affixName;
	}

	/***
	 * 发送邮件
	 * 
	 * @param receiveAccount
	 * @param receiveAccountName
	 * @param mailSubject
	 * @param mailContent
	 * @param bccAccountList
	 * @return
	 */
	public boolean sendMessage(String receiveAccount, String receiveAccountName, String mailSubject, String mailContent,
			Set<String> bccAccountList) throws Exception {
		boolean result = false;
		Transport transport = null;
		// 1.1. 创建一封邮件
		MimeMessage message = null;
		try {
			// 1.1. 创建一封邮件
			message = new MimeMessage(session);
			// 1.2. From: 发件人
			if (Strings.isBlank(accountName)) {
				message.setFrom(new InternetAddress(account));
			} else {
				message.setFrom(new InternetAddress(account, accountName, "UTF-8"));
			}
			// 1.3. To: 收件人（可以增加多个收件人、抄送、密送）
			if (Strings.isBlank(receiveAccountName)) {
				message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveAccount));
			} else {
				message.setRecipient(MimeMessage.RecipientType.TO,
						new InternetAddress(receiveAccount, receiveAccountName, "UTF-8"));
			}
			// To: 收件人（可以增加多个密送）
			if (bccAccountList != null && bccAccountList.size() > 0) {
				for (String bccAccount : bccAccountList) {
					message.addRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(bccAccount));
				}
			}

			// 1.4. Subject: 邮件主题
			message.setSubject(mailSubject, "UTF-8");
			// 1.5. Content: 邮件正文（可以使用html标签）
			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();
			// 设置邮件的文本内容
			if (Strings.isNotBlank(mailContent)) {
				BodyPart contentPart = new MimeBodyPart();
				contentPart.setContent(mailContent, "text/html;charset=UTF-8");
				multipart.addBodyPart(contentPart);
			}

			// 添加附件
			if (Strings.isNotBlank(affix)) {
				BodyPart messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(affix);
				// 添加附件的内容
				messageBodyPart.setDataHandler(new DataHandler(source));
				// 添加附件的标题
				// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
				messageBodyPart.setFileName("=?GBK?B?" + Base64.encodeToString(affixName.getBytes(), true) + "?=");
				multipart.addBodyPart(messageBodyPart);
			}
			// 将multipart对象放到message中
			message.setContent(multipart);

			// 1.6. 设置发件时间
			message.setSentDate(new Date());
			// 1.7. 保存设置
			message.saveChanges();

			// 2. 根据 Session 获取邮件传输对象
			transport = session.getTransport();

			// 3. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
			//
			// PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
			// 仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
			// 类型到对应邮件服务器的帮助网站上查看具体失败原因。
			//
			// PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
			// (1) 邮箱没有开启 SMTP 服务;
			// (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
			// (3) 邮箱服务器要求必须要使用 SSL 安全连接;
			// (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
			// (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
			//
			// PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明。
			transport.connect(account, password);

			// 4. 发送邮件, 发到所有的收件地址, message.getAllRecipients()
			// 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
			transport.sendMessage(message, message.getAllRecipients());

			result = true;
		} catch (SendFailedException e) {
			log.error(e.getMessage());
			Address[] invalidAddresses = e.getInvalidAddresses();
			List<String> invalidAddressList = new ArrayList<String>();
			for (Address invalidAddress : invalidAddresses) {
				invalidAddressList.add(invalidAddress.toString());
			}
			log.error("invalidAddresses is " + Json.toJson(invalidAddressList, JsonFormat.compact()));
			Address[] validUnsentAddresses = e.getValidUnsentAddresses();
			Set<String> unsentSet = new HashSet<String>();
			for (Address validUnsentAddress : validUnsentAddresses) {
				unsentSet.add(validUnsentAddress.toString());
			}
			log.error("unsentSet is " + Json.toJson(unsentSet, JsonFormat.compact()));
			if (validUnsentAddresses != null && validUnsentAddresses.length > 0) {// 当invalidAddresses为空时，说明邮箱系统出了问题，不在进行发送邮件
				result = sendMessage(receiveAccount, receiveAccountName, mailSubject, mailContent, unsentSet);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (transport != null) {
				// 5. 关闭连接
				try {
					transport.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public boolean sendMessage(String receiveAccount, String mailSubject, String mailContent) {
		boolean result = false;
		try {
			result = sendMessage(receiveAccount, null, mailSubject, mailContent, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/***
	 * 发送邮件
	 * 
	 * @param receiveAccount
	 * @param mailSubject
	 * @param mailContent
	 * @param bccAccountList
	 * @return
	 */
	public boolean sendMessage(String receiveAccount, String mailSubject, String mailContent,
			Set<String> bccAccountList) {
		boolean result = false;
		try {
			result = sendMessage(receiveAccount, null, mailSubject, mailContent, bccAccountList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/***
	 * 发送给自己，密送给其他人
	 * 
	 * @param bccAccountList
	 * @param mailSubject
	 * @param mailContent
	 * @return
	 */
	public boolean sendBccMessage(Set<String> bccAccountList, String mailSubject, String mailContent) {
		boolean result = false;
		try {
			result = sendMessage(this.account, null, mailSubject, mailContent, bccAccountList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		Email email = new Email("****@qq.com", "****", Constant.EMAIL_NAME);
		// Email email = new Email("****@163.com", "****", Constant.EMAIL_NAME);
		// email.setAffix("e:/123.txt", "123.txt");
		String content = "<p>有朋友跟我说了几次讲一下波段，好吧，我就把我的珍藏公式发出来，这个公式我一直能卖钱的，哈哈，当然了，而且我是加了密了，没办法这儿又不能用附件，我只好忍痛割爱，直接给大家上源码了，而且这一公式我只在咱们平台发布源码，其他任何地方我都没发过源码，也请大家别给我到处分享了，罢托。</p><p>最郁闷的是开始为了发这个内参方便大家看，本来是发收费内参的发成免费的了，悲剧得不行，现在才发现。</p><p><br/></p><p>这不是条件选股，这是副图公式或者主图公式都可以。看你们各自的习惯，真正做得顺利的人是不需要公式的，而且所谓的轨道，上轨，中轨，下轨哪些随着消息或者接力的存在，随时都可改变，如现在是上轨的压力，突然一个利好可能就突破上轨了，同样如果现在中轨或者下轨有支撑突然一下利空就击破了，我们唯一需要做的就是盘中的盘感，还有超短不谈恋爱做完走人再寻找下一目标，这样才是最大的主动。如今天我身边的朋友昨天高抛了<span><a href='https://www.5igupiao.com/search/query.php?symbol=002497' target='_blank'><font style='color: #507daf'>002497</font></a></span>雅化今天又跌三个多点接回，觉得跌到支撑了，上午一度高兴吸在低位了下午跌成渣，好了不解释了，既然大家有需求，我就发出来。</p><p>下面为公式大家复制后可以用在副图也可用在主图，我一般用副图。但我现在基本不看公式，我就看一下分时力度，该创新高就留不创就走，不求卖在最高也不求买在最低。</p><p>公式如下：</p><p>AA:=MA((2*CLOSE+HIGH+LOW)/4,5);</p><p>通1:=AA*102/100;</p><p>通2:=AA*(200-102)/100;</p><p>CC:=ABS((2*CLOSE+HIGH+LOW)/4-MA(CLOSE,20))/MA(CLOSE,20);</p><p>DD:=DMA(CLOSE,CC);</p><p>上:(1+7/100)*DD,POINTDOT,COLORWHITE;</p><p>下:(1-7/100)*DD,POINTDOT,COLORWHITE;</p><p>中:(上+下)/2,POINTDOT,COLORWHITE;</p><p>M7:EMA(C,7),LINETHICK1,COLORRED;</p><p>M14:EMA(C,14),LINETHICK1,COLORGREEN;</p><p>M25:EMA(C,25),LINETHICK1,COLORBLUE;</p><p>M55:EMA(C,43),LINETHICK1,COLORYELLOW;</p><p>M453:EMA(C,453),LINETHICK1,COLORWHITE;</p><p>中期临界点: DMA(MA(CLOSE+REF(CLOSE,5)/CLOSE*0.098,90),VOL),LINETHICK2,COLORRED;</p><p>年线: DMA(MA(CLOSE+REF(CLOSE,5)/CLOSE*0.098,250),VOL),LINETHICK2,POINTDOT,COLOR30CC00;</p><p>Y1:=STRCAT(&#39;行业：&#39;,HYBLOCK);</p><p>Z1:=STRCAT(Y1,&#39; &#39;);</p><p>Y2:=STRCAT(Z1,&#39;地区：&#39;);</p><p>Z2:=STRCAT(Y2,DYBLOCK);</p><p>Y3:=STRCAT(Z2,&#39; &#39;);</p><p>Z3:=STRCAT(Y3,&#39;概念：&#39;);</p><p>DRAWTEXT_FIX(ISLASTBAR,0.5,0.01,0,STRCAT(Z3,GNBLOCK)),COLORYELLOW;</p><p>STICKLINE(C/REF(C,1)&gt;1.095,C,O,2,0),COLORMAGENTA;</p><p>N:=10;</p><p>T1:=CONST(LLVBARS(LOW,N));</p><p>T1HIGH:=CONST(REF(HIGH,T1));</p><p>CCVV:=CONST(CURRBARSCOUNT);</p><p>T2:=CONST(BARSLAST((CURRBARSCOUNT &gt;(CCVV+T1)) AND (HIGH&gt;T1HIGH)));</p><p>T2HIGH:=CONST(REF(HIGH,T2));</p><p>T3:=CONST(BARSLAST((CURRBARSCOUNT &gt;(CCVV+T2)) AND (HIGH&gt;T2HIGH)));</p><p>T3HIGH:=CONST(REF(HIGH,T3));</p><p>必杀线:DRAWLINE(CURRBARSCOUNT=T3+1, T3HIGH, ISLASTBAR, T3HIGH, 1),LINETHICK2,COLORFF0099;</p><p>DRAWTEXT(ISLASTBAR, T3HIGH, &#39; 必杀线&#39;)LINETHICK,COLORFF0099;</p><p><img src=\"https://cdn.5igupiao.com//upload/livemsg/20170713/14999376950377.jpg\" _src=\"https://cdn.5igupiao.com//upload/livemsg/20170713/14999376950377.jpg\" style=\"\"/></p><p><img src=\"https://cdn.5igupiao.com//upload/livemsg/20170713/14999377005522.jpg\" _src=\"https://cdn.5igupiao.com//upload/livemsg/20170713/14999377005522.jpg\" style=\"\"/></p><p><br/></p>";
		Set<String> accountSet = new HashSet<String>();
		accountSet.add("3478863162@qq.com");

		// email.sendMessage("liuzongtao-tao@163.com", "爱股吧会员信息：张老师", content);
		email.sendBccMessage(accountSet, Constant.EMAIL_NAME + "：执著信仰", content);
	}

}
