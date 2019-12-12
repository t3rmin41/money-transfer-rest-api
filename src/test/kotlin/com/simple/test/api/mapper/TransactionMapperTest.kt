package com.simple.test.api.mapper

import com.simple.api.domain.Transaction
import com.simple.api.domain.TransactionType
import com.simple.api.dto.TransactionDto
import com.simple.api.mapper.toDomain
import com.simple.api.mapper.toDto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.math.BigDecimal
import java.time.Instant

class TransactionMapperTest {

    val id = 1L
    val type: TransactionType = TransactionType.DEPOSIT
    val from: Long = 1L
    val to: Long = 2L
    val amount: BigDecimal = BigDecimal.TEN
    val timestamp: Instant = Instant.now()

    val dto = TransactionDto(id, type.name, from, to, amount, timestamp)
    val domain = Transaction(id, type, from, to, amount, timestamp)

    @Test
    fun testTransactionDtoToDomainMapper() {
        assertAll(
            { Assertions.assertEquals(domain.id, dto.toDomain().id) },
            { Assertions.assertEquals(domain.type, dto.toDomain().type) },
            { Assertions.assertEquals(domain.from, dto.toDomain().from) },
            { Assertions.assertEquals(domain.to, dto.toDomain().to) },
            { Assertions.assertEquals(domain.amount, dto.toDomain().amount) },
            { Assertions.assertEquals(domain.timestamp, dto.toDomain().timestamp) }
        )
    }

    @Test
    fun testTransactionDomainToDtoMapper() {
        assertAll(
            { Assertions.assertEquals(dto.id, domain.toDto().id) },
            { Assertions.assertEquals(dto.type, domain.toDto().type) },
            { Assertions.assertEquals(dto.from, domain.toDto().from) },
            { Assertions.assertEquals(dto.to, domain.toDto().to) },
            { Assertions.assertEquals(dto.amount, domain.toDto().amount) },
            { Assertions.assertEquals(dto.timestamp, domain.toDto().timestamp) }
        )
    }

}