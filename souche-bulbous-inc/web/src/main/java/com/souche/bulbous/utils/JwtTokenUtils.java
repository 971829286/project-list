package com.souche.bulbous.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Maps;
import com.souche.bulbous.exception.BulbousCoreException;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 加密和校验代码
 *
 * @author XadomGreen
 * @since 2018-09-03
 */
public class JwtTokenUtils {

    public static String SECRET = "FreeMaNong";

    /**
     * 生成Token
     *
     * @return
     * @throws Exception
     */
    public static String createToken() throws Exception {

        // 签发时间
        Date iatDate = new Date();

        // 过期时间 - 1分钟过期
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 1);
        Date expiresDate = nowTime.getTime();

        Map<String, Object> map = Maps.newHashMap();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        String token = JWT.create()
                .withHeader(map)
                .withClaim("name", "Free码农")
                .withClaim("age", "28")
                .withClaim("org", "今日头条")
                .withExpiresAt(expiresDate)
                .withIssuedAt(iatDate)
                .sign(Algorithm.HMAC256(SECRET));

        return token;
    }

    /**
     * 解密token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) throws Exception {

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();

        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
        } catch (Exception e) {
            throw new BulbousCoreException("登陆凭证已过时，请重新登陆");
        }

        return jwt.getClaims();
    }

    @Test
    public void testToken() throws Exception {
        System.err.println("==== 加密后Token ====");
        String token = JwtTokenUtils.createToken();
        System.err.println("Token:" + token);

        System.err.println("==== 解密后数据 ====");
        Map<String, Claim> claims = JwtTokenUtils.verifyToken(token);
        System.err.println("name:" + claims.get("name").asString());
        System.err.println("age:" + claims.get("age").asString());
        System.err.println("org:" + claims.get("org").asString());

        System.err.println("==== 过期的Token ====");
        String tokenEx = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJvcmciOiLku4rml6XlpLTmnaEiLCJuYW1lIjoiRnJlZeeggeWGnCIsImV4cCI6MTUzNTk2MTE4NSwiaWF0IjoxNTM1OTYxMTI1LCJhZ2UiOiIyOCJ9.hK1FZRONed54dCeeXdOBGwOpWw3dRtNxmW7Ck9XaXlE";
        Map<String, Claim> claimsEx = JwtTokenUtils.verifyToken(tokenEx);
        System.err.println("name:" + claimsEx.get("name").asString());
        System.err.println("age:" + claimsEx.get("age").asString());
        System.err.println("org:" + claimsEx.get("org").asString());
    }
}
