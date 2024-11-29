package dailymissionproject.demo.domain.mission.batch;

import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MissionBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final MissionRepository missionRepository;

    private static final int chunkSize = 10;

    @Bean
    public Job endJob() {
        return new JobBuilder("endJob", jobRepository)
                .start(endStep())
                .build();
    }

    @Bean
    public Step endStep() {
        return new StepBuilder("endStep", jobRepository)
                .<Mission, Mission> chunk(10, platformTransactionManager)
                .reader(beforeReader())
                .processor(endProcessor())
                .writer(afterWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<Mission> beforeReader() {
        return new RepositoryItemReaderBuilder<Mission>()
                .name("beforeReader")
                .pageSize(chunkSize)
                .methodName("findAllAndDeletedIsFalse")
                .repository(missionRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<Mission, Mission> endProcessor() {
       return item -> {
           if(item.isEndAble(LocalDate.now())) {
               item.end();
           }
           return item;
       };
    }

    @Bean
    public ItemWriter<Mission> afterWriter() {
        return new RepositoryItemWriterBuilder<Mission>()
                .repository(missionRepository)
                .methodName("save")
                .build();
    }
}