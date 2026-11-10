import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolated
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.compositor.Layer
import org.openrndr.extra.compositor.blend
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.mask
import org.openrndr.extra.compositor.post
import org.openrndr.extra.fx.blend.Add
import org.openrndr.extra.fx.blend.Darken
import org.openrndr.extra.fx.blend.Lighten
import org.openrndr.extra.fx.blend.Multiply
import org.openrndr.extra.fx.blend.Normal
import org.openrndr.extra.fx.blend.Screen
import org.openrndr.extra.fx.blur.ApproximateGaussianBlur
import org.openrndr.extra.gui.WindowedGUI
import org.openrndr.extra.imageFit.imageFit
import org.openrndr.extra.parameters.ActionParameter
import org.openrndr.extra.parameters.BooleanParameter
import org.openrndr.extra.parameters.DoubleParameter
import org.openrndr.extra.parameters.IntParameter
import org.openrndr.extra.parameters.OptionParameter
import org.openrndr.shape.Rectangle
import java.io.File
import kotlin.random.Random

// Part 10 — A collage tool
//
// New ingredients: everything together. Images from a folder (part 7 and 9), random
// compositions (part 8), data from a csv (part 1-6), and from the advanced course:
// layers, blend modes, masks and post processing (advanced 6-8) and a WindowedGUI (advanced 10).
//
// The collage has three layers:
//   1. the background: one image, darkened
//   2. the pieces: random cut-outs from the images. This layer has a blend mode, a mask and a
//      blur, all chosen in the GUI.
//   3. the captions: the title and the year of every image that is used, from the csv
//
// Choosing a blend mode in the GUI: a blend mode is fixed when the layer is made. So we make
// the pieces layer once for EVERY blend mode, and switch on only the one that is chosen
// (enabled, advanced part 6).
//
// "new collage" makes a new seed. SPACE saves a picture in the screenshots folder.

enum class CollageBlend { Normal, Multiply, Screen, Add, Darken, Lighten }
enum class CollageMask { None, Circle, Stripes }
enum class CollageImages { Archive, MyImages }

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            fun loadFolder(path: String) = (File(path).listFiles() ?: arrayOf())
                .filter { it.extension.lowercase() == "jpg" || it.extension.lowercase() == "png" }
                .sortedBy { it.name }

            val archiveFiles = loadFolder("data/image-archive")
            val archiveImages = archiveFiles.map { loadImage(it) }
            val myFiles = loadFolder("data/my-images")
            val myImages = myFiles.map { loadImage(it) }

            val info = csvReader().readAllWithHeader(File("data/csv/image-archive.csv"))
            val font = loadFont("data/fonts/default.otf", 16.0)

            val settings = object {
                @IntParameter("seed", 0, 1000, order = 0)
                var seed = 1

                @ActionParameter("new collage", order = 1)
                fun newCollage() {
                    seed = seed + 1
                }

                @OptionParameter("images", order = 2)
                var images = CollageImages.Archive

                @IntParameter("pieces", 1, 60, order = 3)
                var pieces = 15

                @DoubleParameter("smallest", 20.0, 400.0, order = 4)
                var smallest = 80.0

                @DoubleParameter("largest", 20.0, 720.0, order = 5)
                var largest = 300.0

                @DoubleParameter("rotation", 0.0, 180.0, order = 6)
                var rotation = 15.0

                @OptionParameter("blend", order = 7)
                var blend = CollageBlend.Normal

                @OptionParameter("mask", order = 8)
                var mask = CollageMask.None

                @DoubleParameter("blur", 0.0, 20.0, order = 9)
                var blur = 0.0

                @BooleanParameter("captions", order = 10)
                var captions = true
            }

            val gui = WindowedGUI()
            gui.add(settings, "Collage")
            extend(gui)
            extend(Screenshots())

            // which images and files do we use? (my-images only when it's not empty)
            fun currentImages() = if (settings.images == CollageImages.MyImages && myImages.isNotEmpty()) myImages else archiveImages
            fun currentFiles() = if (settings.images == CollageImages.MyImages && myImages.isNotEmpty()) myFiles else archiveFiles

            // the indices of the images used in the current collage, for the captions
            val used = mutableSetOf<Int>()

            val pieceLayers = mutableMapOf<CollageBlend, Layer>()

            val composite = compose {
                // 1. the background
                draw {
                    val random = Random(settings.seed)
                    drawer.clear(ColorRGBa.BLACK)
                    drawer.imageFit(currentImages().random(random), drawer.bounds)
                    drawer.fill = ColorRGBa.BLACK.opacify(0.5)
                    drawer.stroke = null
                    drawer.rectangle(drawer.bounds)
                }

                // 2. the pieces: one layer per blend mode
                for (mode in CollageBlend.entries) {
                    pieceLayers[mode] = layer {
                        when (mode) {
                            CollageBlend.Normal -> blend(Normal())
                            CollageBlend.Multiply -> blend(Multiply())
                            CollageBlend.Screen -> blend(Screen())
                            CollageBlend.Add -> blend(Add())
                            CollageBlend.Darken -> blend(Darken())
                            CollageBlend.Lighten -> blend(Lighten())
                        }
                        draw {
                            // seed + 1, so the pieces don't start with the background image
                            val random = Random(settings.seed + 1)
                            val images = currentImages()
                            used.clear()
                            for (i in 0 until settings.pieces) {
                                val index = random.nextInt(images.size)
                                used.add(index)
                                val w = random.nextDouble(settings.smallest, maxOf(settings.smallest, settings.largest) + 1.0)
                                val h = random.nextDouble(settings.smallest, maxOf(settings.smallest, settings.largest) + 1.0)
                                val x = random.nextDouble(0.0, 720.0)
                                val y = random.nextDouble(0.0, 720.0)
                                val angle = random.nextDouble(-settings.rotation, settings.rotation + 0.001)
                                drawer.isolated {
                                    drawer.translate(x, y)
                                    drawer.rotate(angle)
                                    drawer.imageFit(
                                        images[index], Rectangle(-w / 2.0, -h / 2.0, w, h),
                                        horizontalPosition = random.nextDouble(-1.0, 1.0),
                                        verticalPosition = random.nextDouble(-1.0, 1.0)
                                    )
                                }
                            }
                        }
                        mask {
                            when (settings.mask) {
                                CollageMask.None -> drawer.rectangle(0.0, 0.0, 720.0, 720.0)
                                CollageMask.Circle -> drawer.circle(360.0, 360.0, 300.0)
                                CollageMask.Stripes -> {
                                    for (i in 0 until 12) {
                                        drawer.rectangle(i * 60.0, 0.0, 30.0, 720.0)
                                    }
                                }
                            }
                        }
                        post(ApproximateGaussianBlur()) {
                            window = 25
                            sigma = 0.01 + settings.blur
                        }
                    }
                }

                // 3. the captions, from the csv
                layer {
                    draw {
                        if (settings.captions) {
                            drawer.fontMap = font
                            val files = currentFiles()
                            var y = 30.0
                            for (index in used.sorted()) {
                                // find the row of this file in the csv; own images just show their name
                                var caption = files[index].nameWithoutExtension
                                for (row in info) {
                                    if (row.getValue("file") == files[index].name) {
                                        caption = "${row.getValue("title")} (${row.getValue("year")})"
                                    }
                                }
                                drawer.fill = ColorRGBa.BLACK.opacify(0.6)
                                drawer.stroke = null
                                drawer.rectangle(10.0, y - 16.0, caption.length * 9.5 + 10.0, 22.0)
                                drawer.fill = ColorRGBa.WHITE
                                drawer.text(caption, 15.0, y)
                                y = y + 26.0
                            }
                        }
                    }
                }
            }

            extend {
                for ((mode, pieceLayer) in pieceLayers) {
                    pieceLayer.enabled = mode == settings.blend
                }
                composite.draw(drawer)
            }
        }
    }
}

// EXERCISE 10 — final project: a data collage
// a) Add a slider "border" that draws a white frame around every piece (0 = no frame).
// b) Add a mask option of your own, for example the first letter of your name
//    (loadFace, advanced part 9).
// c) FINAL: make a collage that is about something, with your own images (part 9) AND your
//    own data (part 6). For example: photos of your week, with the size of every photo from
//    your hours of sleep. Or a photo for every row in your csv, with the numbers deciding
//    where, how big or how see-through it is. Save your three best collages with SPACE,
//    write down their seeds, and present them to the group.
