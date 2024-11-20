package dailymissionproject.demo.domain.participant.config;


import java.time.LocalDate;
import java.time.LocalDateTime;

/*
@Component
@RequiredArgsConstructor
public class ParticipantBatchScheduler {

    private final JobLauncher banJobLauncher;
    private final JobRegistry banJobRegistry;


    /**
     * 스케줄러 수행은 서비스 배포 이후에 수행한다.

    @Scheduled(cron = "0 0 3 * * *")
    public void runJob(){
        LocalDateTime time = LocalDateTime.now();
        String key = "수행시간";
        try {
            Job job = banJobRegistry.getJob("BanJob");
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


}

*/