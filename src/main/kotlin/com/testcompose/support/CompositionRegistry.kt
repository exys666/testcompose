package com.testcompose.support

import com.testcompose.annotation.Compose
import org.junit.platform.commons.util.AnnotationUtils
import java.util.*
import java.util.function.Supplier
import kotlin.collections.HashMap

object CompositionRegistry {

    private val compositions = HashMap<String, Composition>()

    init {
        Runtime.getRuntime().addShutdownHook(Thread(Runnable {
            compositions.values.stream()
                    .filter({ !it.external })
                    .forEach(Composition::stop)
        }))
    }

    fun prepare(testClass: Class<*>) {
        findAnnotation(testClass).ifPresent { compose ->
            val composition = compositions.computeIfAbsent(configFullPath(compose)) {
                Composition.attach(it, compose.id)
                        .orElseGet { Composition.create(it, compose.pullArgs, compose.upArgs)}
            }
            composition.exportPorts(compose.exportPorts)
            composition.await(compose.waitFor)
        }
    }


    private tailrec fun findAnnotation(testClass: Class<*>): Optional<Compose> =
            if (testClass == Object::class.java)
                Optional.empty()
            else {
                val c = AnnotationUtils.findAnnotation(testClass, Compose::class.java)
                if (c.isPresent) c else findAnnotation(testClass.superclass)
            }

    private fun configFullPath(compose: Compose): String = configFullPath(compose.config)

    // TODO handle not existing file
    private fun configFullPath(config: String): String = javaClass.getResource(config).path

}
