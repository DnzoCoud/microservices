package com.microservices.accountService.infrastructure.persistence.sp;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Map;

@Component
public class MovementSpGatewayImpl implements MovementSpGateway {
    private final JdbcTemplate jdbcTemplate;

    public MovementSpGatewayImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SpResult createMovement(Long accountId, String movementType, BigDecimal amount, OffsetDateTime date) {
        Map<String, Object> row = jdbcTemplate.queryForMap(
                "SELECT * FROM account.create_movement(?, ?, ?, ?)",
                accountId,
                movementType,
                amount,
                Timestamp.from((date != null ? date : OffsetDateTime.now()).toInstant())
        );

        Long movementId = ((Number) row.get("movement_id")).longValue();
        BigDecimal newBalance = (BigDecimal) row.get("new_balance");
        return new SpResult(movementId, newBalance);
    }
}
