package cn.edu.neu.shop.pin.aspect;

import cn.edu.neu.shop.pin.annotation.MutexLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@Aspect
public class MutexLockAspect {

    private static Logger log = LoggerFactory.getLogger(MutexLockAspect.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Around("@annotation(cn.edu.neu.shop.pin.annotation.MutexLock)")
    public Object distributeLock(ProceedingJoinPoint pjp) {
        Object resultObject = null;

        //确认此注解是用在方法上
        Signature signature = pjp.getSignature();
        if (!(signature instanceof MethodSignature)) {
            log.error("Lockable注解需要设置到方法上");
            return resultObject;
        }

        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();

        //获取注解信息
        MutexLock lockable = targetMethod.getAnnotation(MutexLock.class);
        String key = lockable.key();
        String value = lockable.value();
        long expire = lockable.expire();

        // 分布式锁，如果没有此key，设置此值并返回true；如果有此key，则返回false
        boolean result = redisTemplate.boundValueOps(key).setIfAbsent(value).booleanValue();
        if (!result) {
            // 其他程序已经获取分布式锁，不再执行此方法
            return resultObject;
        }

        //设置过期时间，默认一分钟
        redisTemplate.boundValueOps(key).expire(expire, TimeUnit.SECONDS);

        try {
            resultObject = pjp.proceed(); //调用对应方法执行
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return resultObject;
    }
}