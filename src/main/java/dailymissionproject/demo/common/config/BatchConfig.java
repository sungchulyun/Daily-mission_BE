package dailymissionproject.demo.common.config;

import dailymissionproject.demo.domain.mission.Service.MissionService;
import dailymissionproject.demo.domain.mission.dto.response.MissionAllListResponseDto;
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
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BatchConfig {

    private final MissionService missionService;
    private List<MissionAllListResponseDto> missionLists = new ArrayList<>();

    @Bean
    public Job endJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("endJob", jobRepository)
                .start(readJobStep(jobRepository, transactionManager))
                .next(endJobStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step endJobStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("endJobStep", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================Step2 Executing==================");
                    log.info("==================Step2 ended========================");
                    missionService.end(missionLists);
                    return RepeatStatus.FINISHED;
                }, transactionManager).
                build();
    }

    @Bean Step readJobStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("endJobStep", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    missionLists = missionService.findAllList();
                    log.info("====================Step1 Executing==================");
                    log.info("==================Step1 ended========================");
                    return RepeatStatus.FINISHED;
                }, transactionManager).
                build();
    }
}
