package com.edivad1999.youhavetolockin

import ai.grazie.utils.mpp.time.Time
import com.google.common.io.Files
import kotlinx.coroutines.*
import java.awt.BorderLayout
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.media.Manager
import javax.swing.JPanel
import kotlin.coroutines.CoroutineContext

object VideoPlayerFactory : CoroutineScope {
    private val url = findVideoResource("/youhavetolockin.mov")


    fun createVideoPanel(
        onEnd: () -> Unit = {},
    ): JPanel? = runCatching {

        val player = Manager.createRealizedPlayer(url)
        player.gainControl.mute = false
        player.start()
        JPanel(BorderLayout()).apply {
            add(player.visualComponent, BorderLayout.CENTER)
            player.start()
            launch(coroutineContext) {
                println("pippo ${player.duration.seconds.toLong()}")
                delay(player.duration.seconds.toLong() * 1000)
                player.stop()
                onEnd()

            }

        }
    }.onFailure {
        it.printStackTrace()
    }.getOrNull()


    private fun findVideoResource(resourcePath: String): URL {
        val inputStream = this::class.java.getResourceAsStream(resourcePath)
            ?: throw Error("Could not find resource: $resourcePath")

        val name = resourcePath.substringBeforeLast(".").removePrefix("/") to resourcePath.substringAfterLast(".")
        val temp = createTempFile(name.first, ".${name.second}")

        inputStream.use { input ->
            FileOutputStream(temp).use { output ->
                input.copyTo(output)
            }
        }
        return temp.toURI().toURL()

    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Default
}