package com.disaster.disastercode.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.disaster.disastercode.common.ErrorCode;
import com.disaster.disastercode.exception.BusinessException;
import com.disaster.disastercode.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

public class JWTUtils {
    //定义token检测的header
    public static String header = "Authorization";

    //签名密钥
    private static String secret = "1z2x3c4v5b";

    //有效期 1天
    private static long expireTime = 1000 * 60 * 60 * 24;

    //存进request的attribute的key名
    public static final String currentUserKey = "CURRENT_USER_INFO";


    public static String createToken(Integer userId) {
        return JWT.create()
                .withClaim("userId", userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
                .sign(Algorithm.HMAC512(secret));
    }

    public static Integer getTokenInfoId(HttpServletRequest request) {
        User tokenInfo = (User) request.getAttribute(JWTUtils.currentUserKey);
        return tokenInfo.getId();
    }

    public static User validateToken(String token) {
        try {
            //指定解密算法,使用密匙，去掉前缀，取出载荷
            Map<String, Claim> userMap = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token)
                    .getClaims();
            return transferUser(userMap);
        } catch (TokenExpiredException e) {
            throw new BusinessException(ErrorCode.TOKEN_EXPIRE);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.TOKEN_FAIL);
        }
    }
    private static User transferUser(Map<String, Claim> map) {
        User user = new User();
        Integer userId = map.get("userId").asInt();
        user.setId(userId);
        return user;
    }
}
