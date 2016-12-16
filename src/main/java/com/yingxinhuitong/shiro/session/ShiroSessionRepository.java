package com.yingxinhuitong.shiro.session;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.session.Session;

/**
 * Created by jack on 2016/12/13.
 */
public interface ShiroSessionRepository {

  void saveSession(Session session);

  void deleteSession(Serializable sessionId);

  Session getSession(Serializable sessionId);

  Collection<Session> getAllSessions();
}
