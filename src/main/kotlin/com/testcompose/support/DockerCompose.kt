package com.testcompose.support

class DockerCompose(
        val configPath: String,
        val compositionId: String
) {

    fun pull() {
        DockerComposeCommand("-f", configPath, "-p", compositionId, "pull").waitForComplete()
    }

    fun up() {
        DockerComposeCommand("-f", configPath, "-p", compositionId, "up", "-d").waitForComplete()
    }

    fun down() {
        DockerComposeCommand("-f", configPath, "-p", compositionId, "down").waitForComplete()
    }

    fun ps() =
        DockerComposeCommand("-f", configPath, "-p", compositionId, "ps").waitForComplete()


    // TODO better name
    fun waitFor(container: String, message: String) {
        DockerComposeCommand("-f", configPath, "-p", compositionId, "logs", "-f").waitForMessage(message)
    }

    fun findPort(container: String, port: Int): Int =
            DockerComposeCommand("-f", configPath, "-p", compositionId, "port", container, port.toString())
                    .waitForComplete()[0]
                    .substring("0.0.0.0:".length)
                    .toInt()

}