# shiro-redis-share
shiro共享session和cache<br>
## 1.重写cache
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
## 2.重写sessionDao
    <bean id = "shiroSessionRepository" class = "com.yingxinhuitong.shiro.session.JedisShiroSessionRepository">
      <property name = "redisManager" ref = "redisManager"/>
    </bean>
    <bean id = "sessionDAO" class = "com.yingxinhuitong.shiro.session.CustomShiroSessionDao">
      <property name = "sessionIdGenerator" ref = "sessionIdGenerator"/>
      <property name = "shiroSessionRepository" ref = "shiroSessionRepository"/>
    </bean>
