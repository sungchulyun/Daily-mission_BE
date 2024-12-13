package dailymissionproject.demo.domain.participant.config;


import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ParticipantItemWriter implements ItemWriter<List<Participant>>, ItemStream, InitializingBean {

    private final ParticipantRepository participantRepository;

    @Override
    public void write(Chunk<? extends List<Participant>> items) throws Exception {
        items.forEach(participantRepository::saveAll);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
