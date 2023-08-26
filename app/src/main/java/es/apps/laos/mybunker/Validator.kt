package es.apps.laos.mybunker

private const val REQUIRED_MESSAGE = "This field is required"
private const val REGEX_MESSAGE = "value does not match the regex"

sealed interface Validator
open class Required(var message: String = REQUIRED_MESSAGE): Validator
open class Regex(var message: String, var regex: String = REGEX_MESSAGE): Validator