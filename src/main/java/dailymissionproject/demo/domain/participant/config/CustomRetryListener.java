package dailymissionproject.demo.domain.participant.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

@Slf4j
public class CustomRetryListener implements RetryListener {

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        log.error("Retry attempt {} failed : {}", context.getRetryCount(), throwable.getMessage());
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        if(throwable != null){
            log.error("Retry failed after {} attempts: {}", context.getRetryCount(), throwable.getMessage());
        }
    }

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        log.info("Retry process starting...");
        return true;
    }
}
