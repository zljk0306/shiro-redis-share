  1.重写cache
  <!-- redisManager -->
  <bean id = "redisManager" class = "com.yingxinhuitong.shiro.util.RedisManager">
    <property name = "host" value = "127.0.0.1"/>
    <property name = "port" value = "6379"/>
    <property name = "expire" value = "1800"/>
    <!--<property name = "password" value=""/>-->
    <!--<property name = "timeout" value="0"/>-->
  </bean>
  <!-- shiro缓存 -->
  <bean id = "shrioCacheManager" class = "com.yingxinhuitong.shiro.cache.JedisShiroCacheManager">
    <property name = "redisManager" ref = "redisManager"/>
  </bean>
  <bean id = "shiroJedisManager" class = "com.yingxinhuitong.shiro.cache.CustomShiroCacheManager">
    <property name = "shrioCacheManager" ref = "shrioCacheManager"/>
  </bean>
  
  
  
  2.重写sessionDao
    <!-- 会话DAO -->
  <bean id = "shiroSessionRepository"
        class = "com.yingxinhuitong.shiro.session.JedisShiroSessionRepository">
    <property name = "redisManager" ref = "redisManager"/>
  </bean>
  <bean id = "sessionDAO" class = "com.yingxinhuitong.shiro.session.CustomShiroSessionDao">
    <property name = "sessionIdGenerator" ref = "sessionIdGenerator"/>
    <property name = "shiroSessionRepository" ref = "shiroSessionRepository"/>
  </bean>
  
  
  
  
  3.kickoutfilter修改  增加两处：cache.put(username, deque);
  
  //如果队列里没有此sessionId，且用户没有被踢出；放入队列
    if (!deque.contains(sessionId) && null == session.getAttribute("kickout")) {
      deque.push(sessionId);
      cache.put(username, deque);
    }
    //如果队列里的sessionId数超出最大会话数，开始踢人
    while (deque.size() > maxSession) {
      Serializable kickoutSessionId;
      if (kickoutAfter) { //如果踢出后者
        kickoutSessionId = deque.removeFirst();
      } else { //否则踢出前者
        kickoutSessionId = deque.removeLast();
      }
      cache.put(username, deque);
      