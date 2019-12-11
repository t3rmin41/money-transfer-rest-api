package com.simple.test.api.mapper

import com.simple.api.domain.Account
import com.simple.api.dto.AccountDto
import com.simple.api.mapper.toDomain
import com.simple.api.mapper.toDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.math.BigDecimal

class AccountMapperTest {

    val dto = AccountDto(1, "Alice", BigDecimal.ZERO)
    val domain = Account(1, "Alice", BigDecimal.ZERO)

    @Test
    fun testAccountDtoToDomainMapping() {
        assertAll(
            { assertEquals(domain.id, dto.toDomain().id) },
            { assertEquals(domain.name, dto.toDomain().name) },
            { assertEquals(domain.balance, dto.toDomain().balance) }
        )
    }

    @Test
    fun testAccountDomainToDtoMapping() {
        assertAll(
            { assertEquals(dto.id, domain.toDto().id) },
            { assertEquals(dto.name, domain.toDto().name) },
            { assertEquals(dto.balance, domain.toDto().balance) }
        )
    }

}