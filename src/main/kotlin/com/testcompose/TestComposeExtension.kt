package com.testcompose

import com.testcompose.support.CompositionRegistry
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.*

class TestComposeExtension : BeforeAllCallback {


    override fun beforeAll(ctx: ExtensionContext) {
        // TODO add exception message
        CompositionRegistry.prepare(ctx.testClass.orElseThrow({RuntimeException("")}))
    }
}