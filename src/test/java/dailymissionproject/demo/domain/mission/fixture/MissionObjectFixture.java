package dailymissionproject.demo.domain.mission.fixture;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionUpdateRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.*;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.QMission;
import dailymissionproject.demo.domain.missionRule.dto.MissionRuleResponseDto;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.missionRule.repository.Week;
import dailymissionproject.demo.domain.participant.dto.response.ParticipantUserDto;
import dailymissionproject.demo.domain.participant.repository.Participant;
import dailymissionproject.demo.domain.participant.repository.QParticipant;
import dailymissionproject.demo.domain.user.repository.Role;
import dailymissionproject.demo.domain.user.repository.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.util.List;

public class MissionObjectFixture {

    /**
     * 유저 엔티티 fixture를 반환합니다.
     * @return User
     */
    public static User getUserFixture(){
        return User.builder()
                .username("google 1923819273")
                .email("google@gmail.com")
                .nickname("sungchul")
                .imageUrl("https://aws-s3.jpg")
                .name("윤성철")
                .role(Role.USER)
                .build();
    }

    /**
     * 미션규칙 엔티티 fixture를 반환합니다.
     * @return MissionRule
     */
    public static MissionRule getMissionRuleFixture(){
        return MissionRule.builder()
                .week(new Week(false, true, true, true, true, true, false))
                .build();
    }

    public static MissionRuleResponseDto getMissionRuleResponseFixture(){
        return MissionRuleResponseDto.of(getMissionRuleFixture());
    }

    /**
     * 미션 엔티티 fixture를 반환합니다.
     * @return Mission
     */
    public static Mission getMissionFixture(){
        return Mission.builder()
                .title("TITLE")
                .content("CONTENT")
                .imageUrl("THUMBNAIL.jpg")
                .hint("HINT")
                .credential("CREDENTIAL")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .user(getUserFixture())
                .missionRule(getMissionRuleFixture())
                .build();
    }

    /**
     * 참여자 DTO fixture를 반환합니다.
     * @return
     */
    public static List<ParticipantUserDto> getParticipantUserFixture(){
        ParticipantUserDto participant = ParticipantUserDto.builder()
                .id(1L)
                .nickname("sungchul")
                .imageUrl("https://aws-s3.jpg")
                .banned(false)
                .build();

        return List.of(participant);
    }

    /**
     * 미션 생성 요청 객체를 반환합니다.
     * @return MissionSaveRequestDto
     */
    public static MissionSaveRequestDto getMissionSaveRequest(){
        return MissionSaveRequestDto.builder()
                .title("TITLE")
                .content("CONTENT")
                .hint("HINT")
                .credential("CREDENTIAL")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .week(new Week(false, true, true, true, true, true, false))
                .build();
    }

    /**
     * 미션 생성 응답 객체를 반환합니다.
     * @return MissionSaveResponseDto
     */
    public static MissionSaveResponseDto getMissionSaveResponse(){
        return MissionSaveResponseDto.builder()
                .credential("CREDENTIAL")
                .build();
    }

    /**
     * 미션 상세 응답 객체를 반환합니다.
     * @return MissionDetailResponseDto
     */
    public static MissionDetailResponseDto getMissionDetailResponse(){
        return MissionDetailResponseDto.builder()
                .title("TITLE")
                .content("CONTENT")
                .imageUrl("THUMBNAIL.jpg")
                .hint("HINT")
                .nickname("sungchul")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .missionRuleResponseDto(MissionRuleResponseDto.of(getMissionRuleFixture()))
                .participantUserDto(getParticipantUserFixture())
                .build();
    }

    /**
     * 미션 수정 요청 객체를 반환합니다.
     * @return MissionUpdateRequestDto
     */
    public static MissionUpdateRequestDto getMissionUpdateRequest(){
        return MissionUpdateRequestDto.builder()
                .hint("HINT")
                .credential("CREDENTIAL")
                .build();
    }

    /**
     * 미션 수정 응답 객체를 반환합니다.
     * @return MissionUpdateResponseDto
     */
    public static MissionUpdateResponseDto getMissionUpdateResponse(){
        return MissionUpdateResponseDto.builder()
                .hint("HINT")
                .credential("CREDENTIAL")
                .build();
    }

    public static Slice<MissionHotListResponseDto> getHotMissionListPageable(){
        MissionHotListResponseDto hotMission_1 = MissionHotListResponseDto.builder()
                .id(1L)
                .title("미션1")
                .content("열심히 합니다.")
                .imageUrl("THUMBNAIL1.jpg")
                .nickname("yoonsu")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusDays(10))
                .build();

        MissionHotListResponseDto hotMission_2 = MissionHotListResponseDto.builder()
                .id(2L)
                .title("미션2")
                .content("화이팅합시다!")
                .imageUrl("THUMBNAIL2.jpg")
                .nickname("sungchul")
                .startDate(LocalDate.now().minusDays(7))
                .endDate(LocalDate.now().plusDays(7))
                .build();

        List<MissionHotListResponseDto> listResponse = List.of(hotMission_1, hotMission_2);

        boolean hasNext = false;
        Pageable pageable = PageRequest.of(0, 3);

        Slice<MissionHotListResponseDto> hotMissionListResponse = new SliceImpl<>(listResponse, pageable ,hasNext);
        return hotMissionListResponse;
    }

    public static Slice<MissionNewListResponseDto> getNewMissionListPageable(){
        MissionNewListResponseDto newMission_1 = MissionNewListResponseDto.builder()
                .id(1L)
                .title("미션1")
                .content("열심히 합니다.")
                .imageUrl("THUMBNAIL1.jpg")
                .nickname("yoonsu")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusDays(10))
                .build();

        MissionNewListResponseDto newMission_2 = MissionNewListResponseDto.builder()
                .id(2L)
                .title("미션2")
                .content("화이팅합시다!")
                .imageUrl("THUMBNAIL2.jpg")
                .nickname("sungchul")
                .startDate(LocalDate.now().minusDays(7))
                .endDate(LocalDate.now().plusDays(7))
                .build();

        List<MissionNewListResponseDto> listResponse = List.of(newMission_1, newMission_2);

        boolean hasNext = false;
        Pageable pageable = PageRequest.of(0, 3);

        Slice<MissionNewListResponseDto> newMissionListResponse = new SliceImpl<>(listResponse, pageable ,hasNext);
        return newMissionListResponse;
    }
    /**
     * 인기 미션 리스트 응답 객체를 반환합니다.
     * @return PageResponseDto
     */
    public static Slice<MissionAllListResponseDto> getAllMissionListPageable(){
        MissionAllListResponseDto allMission_1 = MissionAllListResponseDto.builder()
                .id(1L)
                .title("미션1")
                .content("열심히 합니다.")
                .imageUrl("THUMBNAIL1.jpg")
                .nickname("yoonsu")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusDays(10))
                .build();

        MissionAllListResponseDto allMission_2 = MissionAllListResponseDto.builder()
                .id(2L)
                .title("미션2")
                .content("화이팅합시다!")
                .imageUrl("THUMBNAIL2.jpg")
                .nickname("sungchul")
                .startDate(LocalDate.now().minusDays(7))
                .endDate(LocalDate.now().plusDays(7))
                .build();

        List<MissionAllListResponseDto> listResponse = List.of(allMission_1, allMission_2);

        boolean hasNext = false;
        Pageable pageable = PageRequest.of(0, 3);

        Slice<MissionAllListResponseDto> allMissionListResponse = new SliceImpl<>(listResponse, pageable ,hasNext);
        return allMissionListResponse;
    }


    public static PageResponseDto getHotMissionListResponse(){
        MissionHotListResponseDto hotMission_1 = MissionHotListResponseDto.builder()
                .id(1L)
                .title("미션1")
                .content("열심히 합니다.")
                .imageUrl("THUMBNAIL1.jpg")
                .nickname("yoonsu")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusDays(10))
                .build();

        MissionHotListResponseDto hotMission_2 = MissionHotListResponseDto.builder()
                .id(2L)
                .title("미션2")
                .content("화이팅합시다!")
                .imageUrl("THUMBNAIL2.jpg")
                .nickname("sungchul")
                .startDate(LocalDate.now().minusDays(7))
                .endDate(LocalDate.now().plusDays(7))
                .build();

        List<MissionHotListResponseDto> listResponse = List.of(hotMission_1, hotMission_2);

        boolean hasNext = false;
        Pageable pageable = PageRequest.of(0, 3);

        Slice<MissionHotListResponseDto> hotMissionListResponse = new SliceImpl<>(listResponse, pageable ,hasNext);
        PageResponseDto pageResponse = new PageResponseDto(hotMissionListResponse.getContent(), hotMissionListResponse.hasNext());
        return pageResponse;
    }

    /**
     * 신규 미션 리스트 응답 객체를 반환합니다.
     * @return PageResponseDto
     */
    public static PageResponseDto getNewMissionListResponse(){
        MissionNewListResponseDto newMission_1 = MissionNewListResponseDto.builder()
                .id(1L)
                .title("미션1")
                .content("열심히 합니다.")
                .imageUrl("THUMBNAIL1.jpg")
                .nickname("yoonsu")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusDays(10))
                .build();

        MissionNewListResponseDto newMission_2 = MissionNewListResponseDto.builder()
                .id(2L)
                .title("미션2")
                .content("화이팅합시다!")
                .imageUrl("THUMBNAIL2.jpg")
                .nickname("sungchul")
                .startDate(LocalDate.now().minusDays(7))
                .endDate(LocalDate.now().plusDays(7))
                .build();

        List<MissionNewListResponseDto> listResponse = List.of(newMission_1, newMission_2);

        boolean hasNext = false;
        Pageable pageable = PageRequest.of(0, 3);

        Slice<MissionNewListResponseDto> newMissionListResponse = new SliceImpl<>(listResponse, pageable ,hasNext);
        PageResponseDto pageResponse = new PageResponseDto(newMissionListResponse.getContent(), newMissionListResponse.hasNext());
        return pageResponse;
    }

    /**
     * 전체 미션 리스트 응답 객체를 반환합니다.
     * @return PageResponseDto
     */
    public static PageResponseDto getAllMissionListResponse(){
        MissionAllListResponseDto allMission_1 = MissionAllListResponseDto.builder()
                .id(1L)
                .title("미션1")
                .content("열심히 합니다.")
                .imageUrl("THUMBNAIL1.jpg")
                .nickname("yoonsu")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusDays(10))
                .participating(true)
                .build();

        MissionAllListResponseDto allMission_2 = MissionAllListResponseDto.builder()
                .id(2L)
                .title("미션2")
                .content("화이팅합시다!")
                .imageUrl("THUMBNAIL2.jpg")
                .nickname("sungchul")
                .startDate(LocalDate.now().minusDays(7))
                .endDate(LocalDate.now().plusDays(7))
                .participating(true)
                .build();

        List<MissionAllListResponseDto> listResponse = List.of(allMission_1, allMission_2);

        boolean hasNext = false;
        Pageable pageable = PageRequest.of(0, 3);

        Slice<MissionAllListResponseDto> allMissionListResponse = new SliceImpl<>(listResponse, pageable ,hasNext);
        PageResponseDto pageResponse = new PageResponseDto(allMissionListResponse.getContent(), allMissionListResponse.hasNext());
        return pageResponse;
    }

    public static List<MissionUserListResponseDto> getUserMissionList(){
        MissionUserListResponseDto userMissionResponse = MissionUserListResponseDto.builder()
                .id(1L)
                .title("TITLE")
                .content("CONTENT")
                .imageUrl("THUMBNAIL.jpg")
                .nickname("yoonsu")
                .startDate(LocalDate.now().minusDays(5))
                .endDate(LocalDate.now().plusDays(5))
                .ended(false)
                .build();
        return List.of(userMissionResponse);
    }

    public static Participant getParticipant(){
        return Participant.builder()
                .mission(getMissionFixture())
                .user(getUserFixture())
                .build();
    }

    public static List<Mission> getMissionList() {
        Mission mission_1 = getMissionFixture();

        List<Participant> participantList = List.of(getParticipant());
        mission_1.setParticipants(participantList);

        return List.of(mission_1);
    }

    public static Expression<Boolean> getParticipantExpression(QMission mission, Long userId) {
        QParticipant participant = QParticipant.participant;

        return Expressions.as(
                JPAExpressions
                        .selectOne()
                        .from(participant)
                        .where(participant.mission.eq(mission)
                                .and(participant.user.id.eq(userId)))
                        .isNotNull(),
                "participating"
        );
    }
}
