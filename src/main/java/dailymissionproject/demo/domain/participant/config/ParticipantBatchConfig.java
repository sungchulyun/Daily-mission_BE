package dailymissionproject.demo.domain.participant.config;

import dailymissionproject.demo.domain.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ParticipantBatchConfig {

    private final ParticipantService participantService;

    @Bean
    public Job BanJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("BanJob", jobRepository)
                .start(BanJobStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step BanJobStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("endJobStep", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================Step1 Executing==================");
                    log.info("====================Step1 Ended========================");
                    participantService.ban();
                    return RepeatStatus.FINISHED;
                }, transactionManager).
                build();
    }
}
