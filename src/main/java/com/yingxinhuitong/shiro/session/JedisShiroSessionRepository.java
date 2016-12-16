package com.yingxinhuitong.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.yingxinhuitong.shiro.util.RedisManager;
import com.yingxinhuitong.shiro.util.SerializeUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jack on 2016/12/13.
 */
public class JedisShiroSessionRepository implements ShiroSessionRepository {
  private static Logger logger = LoggerFactory.getLogger(JedisShiroSessionRepository.class);
  /**
   *
   * redis session key 前缀
   *
   * */
  private final String REDIS_SHIRO_SESSION = "shiro-session:";

  private RedisManager redisManager;

  public RedisManager getRedisManager() {
    return redisManager;
  }

  public void setRedisManager(RedisManager redisManager) {
    this.redisManager = redisManager;
  }

  public JedisShiroSessionRepository() {
  }

  @Override
  public void saveSession(Session session) {
    if (session == null || session.getId() == null) {
      logger.error("session or session ID is null");
    }
    byte[] key = getByteKey(session.getId());
    byte[] value = SerializeUtils.serialize(session);

    Long timeOut = session.getTimeout() / 1000;
    redisManager.set(key, value, timeOut.intValue());
  }

  @Override
  public void deleteSession(Serializable sessionId) {
    if (sessionId == null) {
      logger.error("session id is null");
      return;
    }
    byte[] key = getByteKey(sessionId);
    redisManager.del(key);
  }

  @Override
  public Session getSession(Serializable sessionId) {
    if (null == sessionId) {
      logger.error("session id is null");
      return null;
    }
    Session session = null;
    byte[] key = getByteKey(sessionId);
    byte[] value = redisManager.get(key);
    if (null == value)
      return null;
    session = (Session) SerializeUtils.deserialize(value);
    return session;
  }

  @Override
  public Collection<Session> getAllSessions() {
    Set<Session> sessions = new HashSet<Session>();
    Set<byte[]> byteKeys = redisManager
        .keys(this.REDIS_SHIRO_SESSION + "*");
    if (byteKeys != null && byteKeys.size() > 0) {
      for (byte[] bs : byteKeys) {
        Session s = (Session) SerializeUtils.deserialize(redisManager
            .get(bs));
        sessions.add(s);
      }
    }
    return sessions;
  }

  private byte[] getByteKey(Serializable sessionId) {
    String preKey = this.REDIS_SHIRO_SESSION + sessionId;
    return preKey.getBytes();
  }

}
