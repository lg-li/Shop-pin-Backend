package cn.edu.neu.shop.pin.lock.aspect;

import cn.edu.neu.shop.pin.lock.annotation.LockKeyVariable;
import cn.edu.neu.shop.pin.lock.annotation.MutexLock;
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
import java.lang.reflect.Parameter;
import java.util.concurrent.TimeUnit;

/**
 * @author LLG
 * 互斥锁切面
 * 根据注解注入执行方法，若互斥则停止执行
 */
@SuppressWarnings("ALL")
@Component
@Slf4j
@Aspect
public class MutexLockAspect {

    private static Logger logger = LoggerFactory.getLogger(MutexLockAspect.class);

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public MutexLockAspect(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Around("@annotation(cn.edu.neu.shop.pin.lock.annotation.MutexLock)")
    public Object distributeMutexLock(ProceedingJoinPoint pjp) {
        Object resultObject = null;

        //确认此注解是用在方法上
        Signature signature = pjp.getSignature();
        if (!(signature instanceof MethodSignature)) {
            logger.error("MutexLock 注解需要设置到方法上。");
            return resultObject;
        }

        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();

        //获取注解信息
        MutexLock mutexLock = targetMethod.getAnnotation(MutexLock.class);
        Object[] args = pjp.getArgs();
        Parameter[] parameters = targetMethod.getParameters();

        // 初始key值
        StringBuilder keyBuilder = new StringBuilder(mutexLock.key());
        // 将LockKeyVariable注解对应的变量值使用横杠连接作为锁的key
        for (int index = 0; index < parameters.length; index++) {
            LockKeyVariable lockKeyVariable = parameters[index].getAnnotation(LockKeyVariable.class);
            if (lockKeyVariable != null) {
                // 若此参数存在LockKeyVariable注解，则将其叠加变量注解到key中
                keyBuilder.append("-").append(args[index].toString());
            }
        }

        final String key = keyBuilder.toString();
        String value = mutexLock.value();
        long expire = mutexLock.expire();

        // 分布式锁，如果没有此key，设置此值并返回true；如果有此key，则返回false
        boolean result = redisTemplate.boundValueOps(key).setIfAbsent(value);
        if (!result) {
            logger.info("[回避] 互斥锁" + key +"已被添加，撤销当前方法的执行。");
            // 其他程序已经获取分布式锁，不再执行此方法
            return resultObject;
        }
        logger.info("[加锁] 互斥锁" + key +"已添加。");
        //设置注解中的过期时间，默认一分钟
        redisTemplate.boundValueOps(key).expire(expire, TimeUnit.SECONDS);
        try {
            resultObject = pjp.proceed(); //调用对应方法执行
            logger.info("[解锁] 互斥锁" + key +"已解锁。");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            redisTemplate.delete(key); // 执行完成后解锁
        }
        return resultObject;
    }
}