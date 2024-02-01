package sms

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.introspect.VisibilityChecker
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.ws.rs.core.Application
import jakarta.ws.rs.ext.ContextResolver
import mu.KotlinLogging
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider
import org.glassfish.jersey.server.ResourceConfig
import sms.controllers.UserController
import sms.data.ChatData
import sms.data.MessageData
import sms.data.UserData
import sms.service.ChatService
import sms.service.MessageService
import sms.service.UserService
import java.net.URI

object Main {
    private val logger = KotlinLogging.logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        logger.info { "Program arguments: ${args.joinToString()}" }
        initializeApplication()
    }

    fun initializeApplication() {
        val applicationBinder = ApplicationBinder()
        startWebserver(
            abstractBinder = applicationBinder
        )
    }
}

class ApplicationBinder : AbstractBinder() {
    override fun configure() {
        val userData = UserData()
        val chatData = ChatData()
        val messageData = MessageData()

        val messageService = MessageService(messageData)
        val chatService = ChatService(userData, messageService, chatData)
        val userService = UserService(userData)

        val objectMapper = jacksonObjectMapper()
        objectMapper.findAndRegisterModules()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY))

        bind(messageService).to(MessageService::class.java)
        bind(chatService).to(ChatService::class.java)
        bind(userService).to(UserService::class.java)
    }
}

fun startWebserver(abstractBinder: AbstractBinder) {
    val resourceConfig = ResourceConfig.forApplicationClass(JerseyApplicationResource::class.java)

    resourceConfig.register(
        ContextResolver {
            ObjectMapper().registerModule(
                KotlinModule.Builder()
                    .withReflectionCacheSize(512)
                    .configure(KotlinFeature.NullToEmptyCollection, false)
                    .configure(KotlinFeature.NullToEmptyMap, false)
                    .configure(KotlinFeature.NullIsSameAsDefault, false)
                    .configure(KotlinFeature.SingletonSupport, false)
                    .configure(KotlinFeature.StrictNullChecks, false)
                    .build()
            ).registerModule(JavaTimeModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    ).register(abstractBinder)
        .register(JacksonFeature())

    val httpServer = NettyHttpContainerProvider.createHttp2Server(
        URI.create("http://0.0.0.0:8080/"),
        resourceConfig,
        null
    )
    Runtime.getRuntime().addShutdownHook(
        Thread {
            httpServer.close()
        }
    )
}

class JerseyApplicationResource : Application() {
    override fun getClasses(): MutableSet<Class<*>> = mutableSetOf(
        UserController::class.java
    )
}
