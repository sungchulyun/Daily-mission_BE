package dailymissionproject.demo.common.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchSchedulerConfig {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    /**
     * 설명 : 스케줄러 설정은 서비스 배포 이후에 수행한다.
    **/
    //@Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void runEndJob() throws Exception {
        log.info("End Mission Batch start ============================");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(new Date());

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("endJob"), jobParameters);
    }

    //@Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void runBanJob() throws Exception {
        log.info("Ban participants Batch start ============================");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(new Date());

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("banJob"), jobParameters);
    }
}

