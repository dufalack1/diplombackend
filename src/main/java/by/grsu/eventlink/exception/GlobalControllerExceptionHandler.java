package by.grsu.eventlink.exception;

import by.grsu.eventlink.dto.common.ExceptionDto;
import by.grsu.eventlink.exception.category.*;
import by.grsu.eventlink.exception.comment.CommentNotFoundException;
import by.grsu.eventlink.exception.comment.EmptyCommentException;
import by.grsu.eventlink.exception.comment.SpamCommentException;
import by.grsu.eventlink.exception.common.InternalServerError;
import by.grsu.eventlink.exception.common.MissMatchException;
import by.grsu.eventlink.exception.common.NotFoundException;
import by.grsu.eventlink.exception.common.ValidationException;
import by.grsu.eventlink.exception.invitation.ActiveInvitationAlreadyExist;
import by.grsu.eventlink.exception.invitation.InvitationCodeMissMatchException;
import by.grsu.eventlink.exception.invitation.InvitationNotFoundException;
import by.grsu.eventlink.exception.joinrequest.JoinRequestAlreadyPresentException;
import by.grsu.eventlink.exception.joinrequest.JoinRequestNotFoundException;
import by.grsu.eventlink.exception.joinrequest.NodeNotInvitedException;
import by.grsu.eventlink.exception.node.NodeAlreadyJoinedException;
import by.grsu.eventlink.exception.node.NodeExpiredException;
import by.grsu.eventlink.exception.node.NodeNotFoundException;
import by.grsu.eventlink.exception.node.NodeRequiredPeopleOverflowException;
import by.grsu.eventlink.exception.role.RoleNotFoundException;
import by.grsu.eventlink.exception.role.UserAlreadyHaveFollowingRoleException;
import by.grsu.eventlink.exception.role.UserDontHaveFollowingRoleException;
import by.grsu.eventlink.exception.storage.FileUploadException;
import by.grsu.eventlink.exception.storage.WrongPhotoFormatException;
import by.grsu.eventlink.exception.subcategory.SubCategoryAlreadyExist;
import by.grsu.eventlink.exception.subcategory.SubCategoryNotFoundException;
import by.grsu.eventlink.exception.user.AgeLimitException;
import by.grsu.eventlink.exception.user.UserAlreadyExistException;
import by.grsu.eventlink.exception.user.UserNotFoundException;
import by.grsu.eventlink.security.jwt.exception.AuthenticationFailed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.constraints.NotNull;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = {
            InternalServerError.class,
            MissMatchException.class,
            InvitationCodeMissMatchException.class,
            NodeRequiredPeopleOverflowException.class,
            NodeNotInvitedException.class,
            AuthenticationFailed.class,
            EmptyCommentException.class,
            SpamCommentException.class,
            WrongPhotoFormatException.class,
            FileUploadException.class,
            EmptyQueryException.class,
            NotSubscribedException.class,
            AuthenticationFailed.class,
            CategoryValidationFailedException.class,
            ValidationException.class,
            AgeLimitException.class,
            NodeExpiredException.class,
            JoinRequestAlreadyPresentException.class,
            NodeAlreadyJoinedException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionDto badRequestHandler(RuntimeException runtimeException) {
        return getResponse(runtimeException);
    }

    @ExceptionHandler(value = {
            ActiveInvitationAlreadyExist.class,
            UserAlreadyExistException.class,
            AlreadySubscribedException.class,
            CategoryWithSameTitleAlreadyExistException.class,
            UserAlreadyHaveFollowingRoleException.class,
            UserDontHaveFollowingRoleException.class,
            SubCategoryAlreadyExist.class
    })
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ExceptionDto alreadyExist(RuntimeException runtimeException) {
        return getResponse(runtimeException);
    }


    @ExceptionHandler(value = {
            NotFoundException.class,
            NodeNotFoundException.class,
            UserNotFoundException.class,
            JoinRequestNotFoundException.class,
            InvitationNotFoundException.class,
            CategoryNotFoundException.class,
            SubCategoryNotFoundException.class,
            CommentNotFoundException.class,
            RoleNotFoundException.class
    })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ExceptionDto notFoundHandler(RuntimeException runtimeException) {
        return getResponse(runtimeException);
    }

    private ExceptionDto getResponse(@NotNull RuntimeException exception) {
        return ExceptionDto.builder()
                .message(exception.getMessage())
                .build();
    }

}
