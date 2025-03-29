package dailymissionproject.demo.domain.mission.fixture;

import dailymissionproject.demo.domain.mission.dto.page.PageResponseDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionSaveRequestDto;
import dailymissionproject.demo.domain.mission.dto.request.MissionUpdateRequestDto;
import dailymissionproject.demo.domain.mission.dto.response.*;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.missionRule.dto.MissionRuleResponseDto;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.missionRule.repository.Week;
import dailymissionproject.demo.domain.participant.dto.response.ParticipantUserDto;
import dailymissionproject.demo.domain.participant.repository.Participant;
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
                .imageUrl("THUMBNAIL.jpg")
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

    public static MissionUpdateRequestDto getNullUpdateRequest(){
        return MissionUpdateRequestDto.builder()
                .build();
    }

    public static MissionUpdateRequestDto getHintUpdateRequest(){
        return MissionUpdateRequestDto.builder()
                .hint("Modified_HINT")
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

    /**
     * 미션 Collection 객체를 반환합니다.
     * 신규, 인기, 전체, 종료
     * @return List<T>
     */
    public static List<MissionHotListResponseDto> getHotMissions(){
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

        return List.of(hotMission_1, hotMission_2);
    }

    public static List<MissionNewListResponseDto> getNewMissions(){
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

        return List.of(newMission_1, newMission_2);
    }

    public static List<MissionAllListResponseDto> getAllMissions(){
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

        return List.of(allMission_1, allMission_2);
    }

    public static List<MissionEndedListResponseDto> getEndMissions(){
        MissionEndedListResponseDto endMission_1 = MissionEndedListResponseDto.builder()
                .id(1L)
                .title("종료미션")
                .content("종료된 미션입니다.")
                .imageUrl("END_THUMBNAIL1.jpg")
                .nickname("Sungchul")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().minusDays(1))
                .build();

        MissionEndedListResponseDto endMission_2 = MissionEndedListResponseDto.builder()
                .id(2L)
                .title("미션2")
                .content("화이팅합시다!")
                .imageUrl("THUMBNAIL2.jpg")
                .nickname("sungchul")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().minusDays(1))
                .build();

        return List.of(endMission_1, endMission_2);
    }

    /**
     * Slice 타입의 각 미션 리스트 응답객체를 반환합니다.
     * @return
     */
    public static Slice<MissionHotListResponseDto> getHotMissionPageable(){
        List<MissionHotListResponseDto> hotMissions = getHotMissions();
        boolean hasNext = false;
        Pageable pageable = PageRequest.of(0, 3);

        return new SliceImpl<>(hotMissions, pageable, hasNext);
    }

    public static Slice<MissionNewListResponseDto> getNewMissionPageable(){
        List<MissionNewListResponseDto> newMissions = getNewMissions();
        boolean hasNext = false;
        Pageable pageable = PageRequest.of(0, 3);

        return new SliceImpl<>(newMissions, pageable ,hasNext);
    }

    public static Slice<MissionAllListResponseDto> getAllMissionPageable(){
        List<MissionAllListResponseDto> allMissions = getAllMissions();
        boolean hasNext = false;
        Pageable pageable = PageRequest.of(0, 3);

        return new SliceImpl<>(allMissions, pageable ,hasNext);
    }

    public static Slice<MissionEndedListResponseDto> getEndMissionPageable(){
        List<MissionEndedListResponseDto> endMissions = getEndMissions();
        boolean hasNext = false;
        Pageable pageable = PageRequest.of(0, 3);

        return new SliceImpl<>(endMissions, pageable ,hasNext);
    }

    /**
     * 각 미션별 리스트를 PageResponseDto 타입 객체로 반환합니다.
     * @return
     */
    public static PageResponseDto getHotMissionListResponse(){
        Slice<MissionHotListResponseDto> hotMissionListResponse = getHotMissionPageable();
        return new PageResponseDto(getHotMissionPageable().getContent(), hotMissionListResponse.hasNext());
    }

    public static PageResponseDto getNewMissionListResponse(){
        Slice<MissionNewListResponseDto> newMissionListResponse = getNewMissionPageable();
        return new PageResponseDto(newMissionListResponse.getContent(), newMissionListResponse.hasNext());
    }

    public static PageResponseDto getAllMissionListResponse(){
        Slice<MissionAllListResponseDto> allMissionListResponse = getAllMissionPageable();
        return new PageResponseDto(allMissionListResponse.getContent(), allMissionListResponse.hasNext());
    }

    public static PageResponseDto getEndMissionListResponse(){
        Slice<MissionEndedListResponseDto> endMissionListResponse = getEndMissionPageable();
        return new PageResponseDto(endMissionListResponse.getContent(), endMissionListResponse.hasNext());
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
}
