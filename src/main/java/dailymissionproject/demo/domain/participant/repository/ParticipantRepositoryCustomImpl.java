package dailymissionproject.demo.domain.participant.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static dailymissionproject.demo.domain.participant.repository.QParticipant.participant;

@RequiredArgsConstructor
public class ParticipantRepositoryCustomImpl implements ParticipantRepositoryCustom {


    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Participant> findAllAndBannedIsFalse(Pageable pageable) {
        List<Participant> participants = queryFactory
                .select(participant)
                .from(participant)
                .where(participant.banned.isFalse().and(participant.mission.ended.isFalse()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(participants, pageable, participants.size());
    }
}
