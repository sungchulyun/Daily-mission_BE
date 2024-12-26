package dailymissionproject.demo.domain.notify.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dailymissionproject.demo.domain.notify.dto.NotifyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@RequiredArgsConstructor
@Component
@Slf4j
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final EmitterService emitterService;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            NotifyDto notifyDto = objectMapper.readValue(message.getBody(), NotifyDto.class);

            emitterService.send(notifyDto);
        } catch (IOException e) {
            log.info("IOException is occurred. ", e);
        }
    }
}
