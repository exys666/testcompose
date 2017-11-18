package com.testcompose.support

import com.testcompose.annotation.Await
import com.testcompose.annotation.Port
import java.util.*

class Composition private constructor(
        val compose: DockerCompose
) {

    constructor(id: String, configPath: String) : this(DockerCompose(configPath, id))

    companion object {

        fun create(configPath: String): Composition {
            val c = Composition(UUID.randomUUID().toString(), configPath)
            c.start()
            return c
        }
    }

    fun exportPorts(ports: Array<Port>) =
        ports.forEach { port -> System.setProperty(port.`as`, compose.findPort(port.container, port.port).toString()) }


    fun await(awaits: Array<Await>) =
            awaits.forEach { await -> compose.waitFor(await.container, await.message) }

    fun stop() {
        compose.down()
    }

    private fun start() {
        compose.pull()
        compose.up()
    }

}