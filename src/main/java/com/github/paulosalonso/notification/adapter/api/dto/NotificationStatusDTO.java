package com.github.paulosalonso.notification.adapter.api.dto;

import com.github.paulosalonso.notification.domain.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.UUID;

@ApiModel("NotificationStatus")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NotificationStatusDTO {

    private UUID id;

    @ApiModelProperty(example = "SCHEDULED")
    private Status status;
}
