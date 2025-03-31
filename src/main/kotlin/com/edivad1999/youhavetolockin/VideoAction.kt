package com.edivad1999.youhavetolockin

import com.intellij.icons.AllIcons
import com.intellij.ide.FileIconProvider
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import java.awt.BorderLayout
import java.awt.Dimension
import java.io.BufferedInputStream
import java.io.FileInputStream
import javax.swing.*

/**
 * Action that shows a video popup when triggered
 */
class VideoAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        VideoDialog(project).show()
    }
}

/**
 * Dialog that displays a video player
 */
class VideoDialog(project: Project) : DialogWrapper(project) {
    init {
        title = "You have To Lock In"
        init()
    }


    override fun createCenterPanel(): JComponent {
        // Create a panel to hold the video player
        val panel = JPanel(BorderLayout())
        panel.preferredSize = Dimension(800, 600)

        // Create the video player using the factory
        val videoPlayer = VideoPlayerFactory.createVideoPanel() {
            SwingUtilities.invokeLater {
                this@VideoDialog.close(OK_EXIT_CODE)
            }
        }
        if (videoPlayer != null) {
            panel.add(videoPlayer, BorderLayout.CENTER)
        }

        return panel
    }

    /**
     * Override to remove the cancel button and make the dialog non-resizable
     */
    override fun createActions() = emptyArray<javax.swing.Action>()

    /**
     * Override to make the dialog non-resizable
     */
    override fun isResizable() = false
}
