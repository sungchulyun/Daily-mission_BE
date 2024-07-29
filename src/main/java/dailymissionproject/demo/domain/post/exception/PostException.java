package dailymissionproject.demo.domain.post.exception;

import lombok.Getter;

@Getter
public class PostException extends RuntimeException {

    private PostExceptionCode postExceptionCode;

    public PostException(PostExceptionCode postExceptionCode){
        super(postExceptionCode.getMessage());
    }


}
