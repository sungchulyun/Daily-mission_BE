package dailymissionproject.demo.domain.post.exception;

import dailymissionproject.demo.common.exception.AbstractCustomException;

public class PostException extends AbstractCustomException {
    public PostException(PostExceptionCode code){
        super(code);
    }


}
