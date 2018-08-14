package com.testcompose.annotation

/**
 * Annotation used to configure port binding
 *
 * @property container id of container from docker-compose.yml
 * @property port internal container port
 * @property as system property under which port will be exposed
 */
@Target
@Retention(AnnotationRetention.RUNTIME)
annotation class Port (
        val container: String,
        val port: Int,
        val `as`: String
)
