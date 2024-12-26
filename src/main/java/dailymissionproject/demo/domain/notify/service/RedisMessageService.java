package dailymissionproject.demo.domain.notify.service;


import dailymissionproject.demo.domain.notify.dto.NotifyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class RedisMessageService {

    private final RedisMessageListenerContainer container;
    private final RedisSubscriber subscriber;
    private final RedisTemplate<String, Object> redisTemplate;

    public void subscribe(String channel) {
        container.addMessageListener(subscriber, ChannelTopic.of(getChannelName(channel)));
    }

    public void publish(String channel, NotifyDto notifyDto){
        redisTemplate.convertAndSend(getChannelName(channel), notifyDto);
    }

    public void removeSubscribe(String channel){
        container.removeMessageListener(subscriber, ChannelTopic.of(getChannelName(channel)));
    }

    private String getChannelName(String id){
        return String.valueOf(id);
    }
}
