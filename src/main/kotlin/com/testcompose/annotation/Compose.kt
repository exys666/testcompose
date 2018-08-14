package com.testcompose.annotation

/**
 * Root annotation used to bind test and docker compose
 *
 * @property id docker compose id, used to attach to running containers
 * @property config path to docker compose config file
 * @property exportPorts list of exposed ports
 * @property waitFor list of log conditions
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Compose(
        val id: String = "",
        val config: String = "/docker-compose.yml",
        val exportPorts: Array<Port> = emptyArray(),
        val waitFor: Array<Await> = emptyArray()
)
