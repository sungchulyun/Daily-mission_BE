package dailymissionproject.demo.domain.mission.config;


/*
프로덕션 환경에서의 배치를 미룬다.

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MissionBatchConfig {

    private final MissionService missionService;
    private List<MissionAllListResponseDto> missionLists;

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

    @Bean
    public Step readJobStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        missionLists = new ArrayList<>();

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


 */