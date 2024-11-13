package dailymissionproject.demo.domain.image.exception;

import dailymissionproject.demo.common.exception.AbstractCustomException;
import lombok.Getter;

@Getter
public class ImageException extends AbstractCustomException {
    public ImageException(ImageExceptionCode code) {super(code);}
}
