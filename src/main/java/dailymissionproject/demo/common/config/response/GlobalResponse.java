package dailymissionproject.demo.common.config.response;

import dailymissionproject.demo.common.meta.MetaInfos;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static dailymissionproject.demo.common.meta.MetaService.createMetaInfo;
import static java.util.Collections.emptyList;

@Getter
@Slf4j
@NoArgsConstructor
public class GlobalResponse {

    private Boolean success;
    private Integer code;
    private Object data;
    private Object errors;
    private Map<String, Object> meta;

    @Builder
    private GlobalResponse(Boolean success, Integer code, Object data, Object errors, Map<String, Object> meta){
        this.success = success;
        this.code = code;
        this.data = data;
        this.errors = errors;
        this.meta = meta;
    }

    public static ResponseEntity<?> ok(Object data){
        return ResponseEntity.ok(success(data));
    }

    public static ResponseEntity<?> ok(Object data, MetaInfos meta){
        return ResponseEntity.ok(success(data, meta));
    }

    /**
     * 성공한 경우 공통 응답 객체 생성
     * @param data - 응답 데이터
     * @return the global response
     */
    public static GlobalResponse success(Object data){
        return GlobalResponse.builder()
                .success(true)
                .code(200)
                .errors(emptyList())
                .meta(createMetaInfo().getMetaInfos())
                .data(data)
                .build();
    }

    /**
     * 성공한 경우 공통 응답 객체 생성
     * 추가적인 메타 정보(페이지값, 서버 요청 가능 횟수 등)을 추가적으로 포함한다.
     * @param data - the data
     * @param meta - the meta
     * @return the global response
     */
    public static GlobalResponse success(Object data, MetaInfos meta){
        return GlobalResponse.builder()
                .success(true)
                .code(200)
                .errors(emptyList())
                .meta(meta.getMetaInfos())
                .data(data)
                .build();
    }

    /**
     * 실패한 경우 공통 응답 객체 생성
     * @param errors 에러 메시지들
     * @return the global response
     */

    public static GlobalResponse fail(Object errors){
        return GlobalResponse.builder()
                .success(false)
                .code(400)
                .data(emptyList())
                .errors(errors)
                .meta(createMetaInfo().getMetaInfos())
                .build();
    }

    /**
     * 실패한 경우 공통 응답 객체 생성
     * @param code - http 상태값
     * @param errors - 에러 메세지
     * @return the global response
     */
    public static GlobalResponse fail(Integer code, Object errors){
        return GlobalResponse.builder()
                .success(false)
                .code(code)
                .data(emptyList())
                .meta(createMetaInfo().getMetaInfos())
                .build();
    }

    /**
     * 실패한 경우 공통 응답 객체 생성
     * @param code - http 상태값
     * @param errors - 에러 메세지
     * @param meta - 추가적인 메타 정보
     * @return the global response
     */

    public static GlobalResponse fail(Integer code, Object errors, MetaInfos meta){
        return GlobalResponse.builder()
                .success(false)
                .code(code)
                .data(emptyList())
                .errors(errors)
                .meta(meta.getMetaInfos())
                .build();
    }

    /**
     * 에러가 발생한 경우 공통 응답 객체 생성
     * @param code - the code
     * @param errors - 에러 메세지
     * @return the global response
     */
    public static GlobalResponse error(Integer code, Object errors){
        return GlobalResponse.builder()
                .success(false)
                .code(code)
                .data(emptyList())
                .errors(errors)
                .meta(createMetaInfo().getMetaInfos())
                .build();
    }
}
