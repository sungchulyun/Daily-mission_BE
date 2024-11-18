package dailymissionproject.demo.domain.participant.config;


/*
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


 */