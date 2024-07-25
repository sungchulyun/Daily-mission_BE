package dailymissionproject.demo.domain.participant.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ParticipantBatchScheduler {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;


    /**
     * 스케줄러 수행은 서비스 배포 이후에 수행한다.

    @Scheduled(cron = "0 0 3 * * *")
    public void runJob(){
        LocalDateTime time = LocalDateTime.now();
        String key = "수행시간";
        try {
            Job job = jobRegistry.getJob("BanJob");
            JobParametersBuilder jobParam = new JobParametersBuilder().addLocalDateTime(key, LocalDateTime.now());
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
