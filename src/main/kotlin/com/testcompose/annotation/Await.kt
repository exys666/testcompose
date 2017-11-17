package com.testcompose.annotation


@Target
@Retention(AnnotationRetention.RUNTIME)
annotation class Await(
        val container: String,
        val message: String
)