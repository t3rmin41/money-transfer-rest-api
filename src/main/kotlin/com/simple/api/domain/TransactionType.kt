package com.simple.api.domain

enum class TransactionType {
    DEPOSIT,
    WITHDRAW,
    TRANSFER;
    companion object {
        fun getTypeByString(name: String?): TransactionType? {
            TransactionType.values().forEach { type ->
                if (type.name.equals(ignoreCase = true, other = name)) return type
            }
            return null
        }
    }
}