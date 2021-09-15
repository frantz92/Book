package org.tkit.app.rs.v1.rfc;

import lombok.extern.slf4j.Slf4j;
import org.tkit.quarkus.jpa.exceptions.DAOException;
import org.tkit.quarkus.rs.exceptions.RestException;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps exceptions thrown in application to Response with RFCProblem.
 */
@Slf4j
@Provider
@Priority(ExceptionToRFCProblemMapper.PRIORITY)
public class ExceptionToRFCProblemMapper implements ExceptionMapper<Exception> {

    /**
     * The exception mapper priority
     */
    public static final int PRIORITY = 9000;
    public static final String TECHNICAL_ERROR = "TECHNICAL ERROR";

    /**
     * The request URI info.
     */
    @Context
    UriInfo uriInfo;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response toResponse(Exception exception) {

        log.error("REST exception URL:{},ERROR:{}", uriInfo.getRequestUri(), exception.getMessage());
        log.error("REST exception error!", exception);

        if (exception instanceof DAOException) {
            return createResponse((DAOException) exception);
        }
        if (exception instanceof RestException) {
            return createResponse((RestException) exception);
        }
        if (exception instanceof WebApplicationException) {
            return createResponse((WebApplicationException) exception);
        }
        return createResponse(exception);
    }

    /**
     * Creates the {@link Response} from the {@link DAOException}
     *
     * @param daoException the {@link DAOException}
     * @return the corresponding {@link Response}
     */
    private Response createResponse(DAOException daoException) {
        RFCProblemDTO rfcProblemDTO = RFCProblemDTO.builder()
                .type(RFCProblemType.DAO_EXCEPTION.name())
                .title(TECHNICAL_ERROR)
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .detail(daoException.getMessage())
                .problems(createRfcProblemDetailDTOs(daoException.getCause()))
                .build();

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(rfcProblemDTO)
                .build();
    }

    /**
     * Creates the {@link Response} from the {@link RestException}
     *
     * @param restException the {@link RestException}
     * @return the corresponding {@link Response}
     */
    private Response createResponse(RestException restException) {
        RFCProblemDTO rfcProblemDTO = RFCProblemDTO.builder()
                                                   .type(RFCProblemType.REST_EXCEPTION.name())
                                                   .title(TECHNICAL_ERROR)
                                                   .status(restException.getStatus().getStatusCode())
                                                   .detail(restException.getParameters().get(0).toString())
                                                   .problems(createRfcProblemDetailDTOs(restException.getCause()))
                                                   .build();

        return Response
                .status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(rfcProblemDTO)
                .build();
    }

    private List<RFCProblemDetailDTO> createRfcProblemDetailDTOs(Throwable cause) {
        if (cause == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(cause.getStackTrace())
                .map(this::mapStackTraceElement)
                .collect(Collectors.toList());
    }

    private RFCProblemDetailDTO mapStackTraceElement(StackTraceElement stackTraceElement) {
        RFCProblemDetailDTO problemDetail = new RFCProblemDetailDTO();
        problemDetail.setMessage("An error occured in " + stackTraceElement.getMethodName() + " in line " + stackTraceElement.getLineNumber());
        problemDetail.setMessageId(stackTraceElement.getClassName());

        return problemDetail;
    }

    /**
     * Creates the {@link Response} from the {@link WebApplicationException}
     *
     * @param webApplicationException the {@link WebApplicationException}
     * @return the corresponding {@link Response}
     */
    private Response createResponse(WebApplicationException webApplicationException) {
        RFCProblemDTO rfcProblemDTO = RFCProblemDTO.builder()
                .type(RFCProblemType.WEB_APPLICATION_EXCEPTION.name())
                .title(TECHNICAL_ERROR)
                .status(webApplicationException.getResponse().getStatus())
                .detail(webApplicationException.getMessage())
                .problems(createRfcProblemDetailDTOs(webApplicationException.getCause()))
                .build();

        return Response
                .fromResponse(webApplicationException.getResponse())
                .entity(rfcProblemDTO)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * Creates the {@link Response} from the {@link Exception}
     *
     * @param exception the {@link Exception}
     * @return the corresponding {@link Response}
     */
    private Response createResponse(Exception exception) {
        RFCProblemDTO rfcProblemDTO = RFCProblemDTO.builder()
                .type(RFCProblemType.UNDEFINED_EXCEPTION.name())
                .title(TECHNICAL_ERROR)
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .detail(exception.getMessage())
                .problems(createRfcProblemDetailDTOs(exception.getCause()))
                .build();

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(rfcProblemDTO)
                .build();
    }

    /**
     * The RFCProblem types.
     */
    public enum RFCProblemType {

        /**
         * The error code for the DAO exception {@link DAOException}
         */
        DAO_EXCEPTION,

        /**
         * The error code for the REST exception {@link RestException}
         */
        REST_EXCEPTION,

        /**
         * The error code for the web application exception {@link WebApplicationException}
         */
        WEB_APPLICATION_EXCEPTION,

        /**
         * The error code for undefined exception.
         */
        UNDEFINED_EXCEPTION
    }
}
