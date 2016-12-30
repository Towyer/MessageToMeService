package cn.edu.bjtu.weibo.service.serviceImpl;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.bjtu.weibo.dao.LoginDAO;
import cn.edu.bjtu.weibo.model.LoginUser;
import cn.edu.bjtu.weibo.service.LoginService;
import cn.edu.bjtu.weibo.service.serviceImpl.loginServiceImplHelper.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.sf.json.JSONObject;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	LoginDAO loginDao;

	private String profiles;

	@Override
	public String getLoginUserToken(LoginUser user) {

		// TODO Auto-generated method stub
		// ����֤�˺��Ƿ����
		if (loginDao.select(user.getUsername())) {
			// ������ڣ���֤�˺������Ƿ���ȷ
			String userID = loginDao.getUserId(user.getUsername(), user.getPassword());
			if (userID == null) {
				return "�������";
			} else// ��֤ͨ�� ����token������
			{
				try {

					String token = createJWT(Constant.JWT_ID, generalSubject(userID), Constant.JWT_TTL);

					return token;
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return "�û���������";
	}

	public static String generalSubject(String userID) {
		JSONObject jo = new JSONObject();
		jo.put("userId", userID);
		return jo.toString();
	}

	public String createJWT(String id, String subject, long ttlMillis) throws Exception {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		SecretKey key = generalKey();
		JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).signWith(signatureAlgorithm,
				key);
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}
		return builder.compact();
	}

	public SecretKey generalKey() {
		String stringKey = profiles + Constant.JWT_SECRET;
		byte[] encodedKey = Base64.decodeBase64(stringKey);
		SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
		return key;
	}

	// ��������jwt
	public Claims parseJWT(String jwt) throws Exception {
		SecretKey key = generalKey();
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
		return claims;
	}

}
