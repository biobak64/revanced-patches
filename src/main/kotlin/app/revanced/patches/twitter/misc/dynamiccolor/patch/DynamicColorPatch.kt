package app.revanced.patches.twitter.misc.dynamiccolor.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultError
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.twitter.misc.dynamiccolor.annotations.DynamicColorCompatibility
import java.io.FileWriter
import java.nio.file.Files

@Patch
@Name("dynamic-color")
@Description("Replaces the default Twitter Blue with the users Material You palette.")
@DynamicColorCompatibility
@Version("0.0.2")
class DynamicColorPatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {
        val resDirectory = context["res"]
        if (!resDirectory.isDirectory) return PatchResultError("The res folder can not be found.")

        val valuesDirectory = resDirectory.resolve("values")
        if (!valuesDirectory.isDirectory) Files.createDirectories(valuesDirectory.toPath())

        val valuesNightDirectory = resDirectory.resolve("values-night")
        if (!valuesNightDirectory.isDirectory) Files.createDirectories(valuesNightDirectory.toPath())

        listOf(valuesDirectory, valuesNightDirectory).forEach { it ->
            val colorsXml = it.resolve("colors.xml")

            if(!colorsXml.exists()) {
                FileWriter(colorsXml).use {
                    it.write("<?xml version=\"1.0\" encoding=\"utf-8\"?><resources></resources>")
                }
            }
        }

        context.xmlEditor["res/values/colors.xml"].use { editor ->
            val document = editor.file

            mapOf(
				"app_background" to "@android:color/system_accent1_10",
                "ps__twitter_blue" to "@@android:color/system_accent1_400",
                "ps__twitter_blue_pressed" to "@android:color/system_accent1_300",
                "twitter_blue" to "@android:color/system_accent1_400",
                "twitter_blue_fill_pressed" to "@android:color/system_accent1_300",
                "twitter_blue_opacity_30" to "@android:color/system_accent1_100",
                "twitter_blue_opacity_50" to "@android:color/system_accent1_200",
                "twitter_blue_opacity_58" to "@android:color/system_accent1_300",
                "deep_transparent_twitter_blue" to "@android:color/system_accent1_200",
				"unread" to "@android:color/system_accent1_50",
                "ic_launcher_background" to "#1DA1F2"
            ).forEach { (k, v) ->
                val colorElement = document.createElement("color")

                colorElement.setAttribute("name", k)
                colorElement.textContent = v

                document.getElementsByTagName("resources").item(0).appendChild(colorElement)
            }
        }

        context.xmlEditor["res/values-night/colors.xml"].use { editor ->
            val document = editor.file

            mapOf(
				"app_background" to "@android:color/system_accent1_900",
                "twitter_blue" to "@android:color/system_accent1_200",
                "twitter_blue_fill_pressed" to "@android:color/system_accent1_300",
                "twitter_blue_opacity_30" to "@android:color/system_accent1_50",
                "twitter_blue_opacity_50" to "@android:color/system_accent1_100",
                "twitter_blue_opacity_58" to "@android:color/system_accent1_200",
                "deep_transparent_twitter_blue" to "@android:color/system_accent1_200",
				"link_color" to "@android:color/system_accent3_600",
            ).forEach { (k, v) ->
                val colorElement = document.createElement("color")

                colorElement.setAttribute("name", k)
                colorElement.textContent = v

                document.getElementsByTagName("resources").item(0).appendChild(colorElement)
            }
        }

        return PatchResultSuccess()
    }
}