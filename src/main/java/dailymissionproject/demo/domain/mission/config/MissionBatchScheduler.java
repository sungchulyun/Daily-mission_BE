package dailymissionproject.demo.domain.mission.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MissionBatchScheduler {

    private final JobLauncher endJobLauncher;
    private final JobRegistry endJobRegistry;

    /*
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(){
        JobRegistryBeanPostProcessor jobProcessor = new JobRegistryBeanPostProcessor();
        jobProcessor.setJobRegistry(endJobRegistry);
        return jobProcessor;
    }

     */

    /**
     * 설명 : 스케줄러 설정은 서비스 배포 이후에 수행한다.

    @Scheduled(cron = "0 0 3 * * *")
    public void runJob(){
        LocalDate time = LocalDate.now();
        try {
            Job job = endJobRegistry.getJob("endJob");
            JobParametersBuilder jobParam = new JobParametersBuilder().addLocalDate("time", time);
            jobLauncher.run(job, jobParam.toJobParameters());
        } catch (NoSuchJobException |
                 JobInstanceAlreadyCompleteException |
                 JobExecutionAlreadyRunningException |
                 JobParametersInvalidException |
                 JobRestartException e){
            throw new RuntimeException(e);
        }
    }

        */
}
