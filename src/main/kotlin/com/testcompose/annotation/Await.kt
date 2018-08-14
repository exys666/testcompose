package com.testcompose.annotation


/**
 * Annotation used to check container health condition based on logs
 *
 * @property container container identifier
 * @property message expected message
 */
@Target
@Retention(AnnotationRetention.RUNTIME)
annotation class Await(
        val container: String,
        val message: String
)