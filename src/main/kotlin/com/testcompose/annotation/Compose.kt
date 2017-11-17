package com.testcompose.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Compose(
        val config: String = "config.yml",
        val exportPorts: Array<Port> = emptyArray(),
        val waitFor: Array<Await> = emptyArray()
)
