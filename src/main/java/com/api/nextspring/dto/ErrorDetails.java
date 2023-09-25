package com.api.nextspring.dto;

import java.util.Date;

public record ErrorDetails(Date timestamp, String message, String details) {
}
