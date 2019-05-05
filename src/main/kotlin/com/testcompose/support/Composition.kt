package com.testcompose.support

import com.testcompose.annotation.Await
import com.testcompose.annotation.Port
import java.util.*

class Composition private constructor(
        val compose: DockerCompose,
        val external: Boolean
) {

    constructor(id: String,
                configPath: String,
                pullArgs: Array<String>,
                upArgs: Array<String>) : this(DockerCompose(configPath, id, pullArgs, upArgs), false)

    companion object {

        fun create(configPath: String, pullArgs: Array<String>, upArgs: Array<String>): Composition {
            val c = Composition(UUID.randomUUID().toString(), configPath, pullArgs, upArgs)
            c.start()
            return c
        }

        fun attach(configPath: String, id: String): Optional<Composition> {
            val compose = DockerCompose(configPath, id, emptyArray(), emptyArray())
            val lines = compose.ps()

            if (lines.size > 2) {
                val index = lines[0].indexOf("State")
                if (lines.stream().skip(2).allMatch({line -> line.substring(index, index + 2) == "Up" })) {
                    return Optional.of(Composition(compose, true))
                }
            }

            return Optional.empty()
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