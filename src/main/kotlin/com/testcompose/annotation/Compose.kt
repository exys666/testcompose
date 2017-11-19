package com.testcompose.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Compose(
        val id: String = "",
        val config: String = "/docker-compose.yml",
        val exportPorts: Array<Port> = emptyArray(),
        val waitFor: Array<Await> = emptyArray()
)
