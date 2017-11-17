package com.testcompose.annotation

@Target
@Retention(AnnotationRetention.RUNTIME)
annotation class Port (
        val container: String,
        val port: Int,
        val `as`: String
)
