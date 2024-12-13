package dailymissionproject.demo.domain.participant.config;


import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.participant.repository.ParticipantRepository;
import dailymissionproject.demo.domain.post.repository.PostRepository;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ParticipantBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final MissionRepository missionRepository;
    private final PostRepository postRepository;
    private final BanWriterListener writerListener;
    private final ParticipantItemWriter banWriter;

    private static final int chunkSize = 10;

    @Bean
    public Job banJob() {
        return new JobBuilder("banJob", jobRepository)
                .start(banStep())
                .build();
    }

    /*

    @Bean
    public Step banStep() {
        return new StepBuilder("banStep", jobRepository)
                .<Participant, Participant> chunk(10, platformTransactionManager)
                .reader(banBeforeReader())
                .processor(banProcessor())
                .writer(banAfterWriter())
                .listener(writerListener)
                .build();
    }

    @Bean
    public RepositoryItemReader<Participant> banBeforeReader(){
        return new RepositoryItemReaderBuilder<Participant>()
                .name("beforeReader")
                .pageSize(chunkSize)
                .methodName("findAllAndBannedIsFalse")
                .repository(participantRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<Participant, Participant> banProcessor() {
        return item -> {
            if(!isSubmitToday(item, LocalDateTime.now())){
                item.ban();
            }

            return item;
        };
    }

   @Bean
   public ItemWriter<Participant> banAfterWriter() {
        return new RepositoryItemWriterBuilder<Participant>()
                .repository(participantRepository)
                .methodName("save")
                .build();
   }

   */

    @Bean
    public Step banStep() {
        return new StepBuilder("banStep", jobRepository)
                .<Mission, List<Participant>> chunk(chunkSize, platformTransactionManager)
                .reader(missionReader())
                .processor(banProcessor())
                .writer(banWriter)
                .listener(writerListener)
                .build();
    }

    @Bean
    public RepositoryItemReader<Mission> missionReader() {
        return new RepositoryItemReaderBuilder<Mission>()
                .name("missionReader")
                .repository(missionRepository)
                .methodName("findAllAndEndedIsFalse")
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<Mission, List<Participant>> banProcessor() {
        LocalDate date = LocalDate.now();
        String today = date.getDayOfWeek().toString();
        log.info("today is {}", today);

        return mission -> {
            if(!mission.checkMandatory(today)){
                return null;
            }

            return mission.getParticipants().stream()
                    .filter(participant -> isSubmitToday(participant, LocalDateTime.now()))
                    .filter(participant -> !participant.isBanned()) // 아직 강퇴되지 않은 참여자만 처리
                    .peek(Participant::ban) // 강퇴 상태로 변경
                    .toList();
        };
    }
    /**
     * 설명 : 현재시간 0시 ~ 3시 : 전날 03시01분 ~ 현재시간까지 제출 여부 확인
     * 현재시간 3시 ~ 24 시 : 금일 03시 ~ 현재시간까지 제출 여부 확인
     * 설명 : 매일 03시에 강퇴 배치를 수행하기 때문에
     * 전날 03시 ~ 다음날 03시를 각각 분기 처리한다.
     */
    public boolean isSubmitToday(Participant participant, LocalDateTime now){
        long postCount = 0;
        LocalDateTime criteria = LocalDate.now().atTime(03,00);
        if(now.isBefore(criteria)){
//            // 전날 새벽 3시 ~ 현재
//            isSubmit = postRepository.countPostSubmit(participant.getMission()
//                    , participant.getUser()
//                    ,criteria.minusDays(1)
//                    , now) > 0;

            postCount = participant.getUser().getPosts().stream()
                    .filter(post -> post.getCreatedDate().isAfter(criteria.minusDays(1)))
                    .filter(post -> post.getCreatedDate().isBefore(now))
                    .count();

        } else {
            // 금일 새벽 3시  ~ 현재
//            isSubmit = postRepository.countPostSubmit(participant.getMission()
//                    ,participant.getUser()
//                    ,criteria
//                    ,now) > 0;

            postCount = participant.getUser().getPosts().stream()
                    .filter(post -> post.getCreatedDate().isAfter(criteria))
                    .filter(post -> post.getCreatedDate().isBefore(now))
                    .count();
        }
        return postCount > 0;
    }

}