package com.github.paulosalonso.notification.adapter.api.dto;

import com.github.paulosalonso.notification.domain.Channel;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;

@ApiModel("NotificationCreate")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NotificationCreateDTO {

    @NotNull
    private Channel channel;

    @NotNull
    @Future
    private OffsetDateTime scheduleAt;

    @Singular
    @NotEmpty
    private List<String> recipients;

    @NotBlank
    private String message;
}
