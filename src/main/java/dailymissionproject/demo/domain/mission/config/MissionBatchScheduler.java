package dailymissionproject.demo.domain.mission.config;


/*
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


}

*/