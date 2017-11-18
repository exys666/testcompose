package com.testcompose.support

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream


class DockerComposeCommand private constructor(
        val process: Process
) {

    constructor(vararg args: String) : this(start(args))

    companion object {
        fun start(args: Array<out String>): Process {
            val cmd: Array<String> = Stream.concat(Stream.of("docker-compose"), Arrays.stream(args))
                    .collect(Collectors.toList()).toTypedArray()
            val builder = ProcessBuilder()
            return builder.command(*cmd).start()
            // TODO catch exception
        }
    }

    fun waitForComplete(): List<String>  = waitFor { false }


    fun waitForMessage(message: String): List<String> = waitFor({line -> line.contains(message)})

    private fun waitFor(condition: (String) -> Boolean): List<String> {
        val out = BufferedReader(InputStreamReader(process.inputStream))
        val err = BufferedReader(InputStreamReader(process.errorStream))

        val results = ArrayList<String>()

        try {
            loop@ while (process.isAlive) {

                while (true) {
                    val line: String = out.readLine() ?: break

                    results.add(line)
                    System.out.println("DOCKER >> " + line)
                    if (condition.invoke(line)) {
                        process.destroy()
                        break@loop
                    }
                }

                while (true) {
                    val line: String = err.readLine() ?: break

                    System.out.println("DOCKER >> " + line)
                    if (condition.invoke(line)) {
                        process.destroy()
                        break@loop
                    }
                }

            }
        } catch (ex: IOException) {
            if (!ex.message.equals("Stream closed")) throw ex
        }

        return results
    }


}