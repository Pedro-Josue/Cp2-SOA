package com.cp2.SOA.Shared.DTO;

import java.time.LocalDateTime;

public record ErrorResponseDTO(LocalDateTime timestamp,
                               Integer status,
                               String error,
                               String path) {
}
