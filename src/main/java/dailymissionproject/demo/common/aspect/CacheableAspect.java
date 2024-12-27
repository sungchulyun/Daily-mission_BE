package dailymissionproject.demo.common.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class CacheableAspect {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Random random = new Random();

    private static final double RECOMPUTE_PROBABILITY = 0.1;


    @Around("@annotation(cacheable)")
    public Object applyPer(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        String cacheName = (cacheable.cacheNames().length > 0) ? cacheable.cacheNames()[0] : null;
        Object key = SimpleKeyGenerator.generateKey(joinPoint.getArgs());
        String redisKey = cacheName + "::" + key;

        CacheData cacheData = (CacheData) redisTemplate.opsForValue().get(redisKey);

        if (cacheData != null && shouldRecompute(cacheData.getLastUpdateTime(), cacheable)) {
            log.info("cache update ");
            Object newData = joinPoint.proceed();
            updateCache(redisKey, newData, cacheable);
            return newData;
        }

        return cacheData != null ? cacheData.getData() : joinPoint.proceed();
    }

    private boolean shouldRecompute(long lastUpdateTime, Cacheable cacheable) {
        long currentTime = System.currentTimeMillis() / 1000;
        long elapsedTime = currentTime - lastUpdateTime;

        Long ttl = redisTemplate.getExpire(cacheable.cacheNames()[0], TimeUnit.SECONDS);
        if (ttl == null || ttl <= 0) {
            return true;
        }

        long bufferedTtl = Math.max(ttl - 5, 0);

        double probability = RECOMPUTE_PROBABILITY * (elapsedTime / (double) bufferedTtl);
        return random.nextDouble() < probability;
    }

    private void updateCache(String redisKey, Object data, Cacheable cacheable) {
        long currentTime = System.currentTimeMillis() / 1000;
        CacheData cacheData = new CacheData(data, currentTime);

        redisTemplate.opsForValue().set(redisKey, cacheData, cacheable.key().length(), TimeUnit.SECONDS);
    }

    private static class CacheData {
        private final Object data;
        private final long lastUpdateTime;

        public CacheData(Object data, long lastUpdateTime) {
            this.data = data;
            this.lastUpdateTime = lastUpdateTime;
        }

        public Object getData() {
            return data;
        }

        public long getLastUpdateTime() {
            return lastUpdateTime;
        }
    }
}