package com.github.paulosalonso.notification.adapter.api.exceptionhandler;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Error {

	@Builder.Default
	private OffsetDateTime timestamp = OffsetDateTime.now();
	private Integer status;
	private String message;
	private List<Field> fields;

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	@Builder
	public static class Field {
		private String name;
		private String message;
	}
}