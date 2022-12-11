package pl.damianujma

sealed interface DomainError
sealed interface ConnectionError

data class DatabaseConnectionError(val message: String) : ConnectionError
data class Unexpected(val description: String, val error: Throwable) : ConnectionError

sealed interface ValidationError : DomainError

data class UnexpectedDomainError(val description: String, val error: Throwable) : DomainError

data class EmptyUpdate(val description: String) : ValidationError

