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

@Service("loginService")
public class LoginServiceImpl implements LoginService {

	@Autowired
	LoginDAO loginDao;

	private String profiles;

	@Override
	public String getLoginUserToken(LoginUser user) {
		
		// 先验证账号是否存在
		if (loginDao.select(user.getUsername())) {
			// 如果存在，验证账号密码是否正确
			String userID = loginDao.getUserId(user.getUsername(), user.getPassword());
			
			if (userID == null||userID.isEmpty()) {
				return null;
			} 
			else{
				// 验证通过 生成token并返回
				try {
					
					String token = createJWT(Constant.JWT_ID, generalSubject(userID), Constant.JWT_TTL);
					
					return token;
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
		return null;
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

	// 用来解析jwt
	public Claims parseJWT(String jwt) throws Exception {
		SecretKey key = generalKey();
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
		return claims;
	}

}
