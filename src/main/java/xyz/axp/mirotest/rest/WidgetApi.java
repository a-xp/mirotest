package xyz.axp.mirotest.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import xyz.axp.mirotest.domain.entities.Widget;
import xyz.axp.mirotest.domain.service.CreateWidgetDTO;
import xyz.axp.mirotest.domain.service.WidgetPage;
import xyz.axp.mirotest.domain.service.WidgetService;
import xyz.axp.mirotest.exceptions.InvalidSearchParametersException;
import xyz.axp.mirotest.exceptions.InvalidWidgetException;
import xyz.axp.mirotest.exceptions.ServiceException;
import xyz.axp.mirotest.rest.dto.ErrorResponse;
import xyz.axp.mirotest.rest.dto.SearchSpec;
import xyz.axp.mirotest.util.StringUtils;

import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/api/board/v1/widgets")
public class WidgetApi {

    private final Logger logger = LoggerFactory.getLogger(WidgetApi.class);
    private final WidgetService widgetService;

    public WidgetApi(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @GetMapping("{id}")
    public Widget getById(@PathVariable int id) {
        return widgetService.getById(id);
    }

    @PostMapping
    public Widget create(@RequestBody @Valid CreateWidgetDTO widget, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidWidgetException(bindingResult.toString());
        }
        return widgetService.createNew(widget);
    }

    @PostMapping("{id}")
    public Widget updateById(@RequestBody Widget widget, @PathVariable int id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidWidgetException(bindingResult.toString());
        }
        return widgetService.update(id, widget);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        widgetService.deleteById(id);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public WidgetPage list(@Valid SearchSpec searchSpec, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidSearchParametersException(bindingResult.toString());
        }
        if (searchSpec.getSnapshotId() == null) {
            return widgetService.findAll(searchSpec.getOffset(), searchSpec.getLimit(), searchSpec.getBoundaries().orElse(null));
        } else {
            return widgetService.findAllInSnapshot(searchSpec.getSnapshotId(), searchSpec.getOffset(), searchSpec.getLimit(), searchSpec.getBoundaries().orElse(null));
        }
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(StringUtils.formatErrorCode(e.getClass().getSimpleName()), e.getMessage()));
    }

    @ExceptionHandler({ValidationException.class, HttpMessageConversionException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_REQUEST", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        logger.error("Failed to process request", e);
        return ResponseEntity.internalServerError().body(new ErrorResponse("INTERNAL_ERROR", e.getMessage()));
    }

}
