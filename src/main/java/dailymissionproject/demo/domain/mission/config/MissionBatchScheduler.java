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

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    /*
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(){
        JobRegistryBeanPostProcessor jobProcessor = new JobRegistryBeanPostProcessor();
        jobProcessor.setJobRegistry(jobRegistry);
        return jobProcessor;
    }

     */

    @Scheduled(cron = "0 0 3 * * *")
    public void runJob(){
        LocalDate time = LocalDate.now();
        try {
            Job job = jobRegistry.getJob("endJob");
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


}
