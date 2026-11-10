import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.extensions.Screenshots
import org.openrndr.shape.Rectangle
import java.io.File
import kotlin.random.Random

// Part 9 — Your own images
//
// New ingredients: your own folder, mkdirs(), cutting images into tiles, keyboard.character
//
// Now use your own pictures: photos from your phone, scans, drawings. Put them in the folder
// data/my-images. The program makes that folder for you the first time you run it
// (File(...).mkdirs()). Until there are images in it, it uses data/image-archive.
//
// Tips for your own images:
//   - only .jpg and .png. Photos from an iPhone are often .heic: convert them to .jpg first
//     (in Photos: File -> Export).
//   - make them smaller. A phone photo is 4000 pixels wide; around 1000 is plenty and loads
//     much faster. (On a Mac: open in Preview, Tools -> Adjust Size.)
//   - 5 to 20 images is a good number.
//
// The picture: the window is cut into a grid of tiles. Every tile shows the SAME part of a
// RANDOM image, so all images stay "in place" and together make one patchwork picture.
//
//   drawer.image(image, source, target)
//     source: which part of the image (in the pixels of the image)
//     target: where in the window it goes
//
// Keys: + and - change the number of tiles, r gives a new mix, SPACE saves.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            extend(Screenshots())

            val myFolder = File("data/my-images")
            myFolder.mkdirs()

            var files = myFolder.listFiles()
                .filter { it.extension.lowercase() == "jpg" || it.extension.lowercase() == "png" }
            val usingOwnImages = files.isNotEmpty()
            if (!usingOwnImages) {
                files = File("data/image-archive").listFiles()
                    .filter { it.extension == "jpg" || it.extension == "png" }
            }
            val images = files.sortedBy { it.name }.map { loadImage(it) }

            var tiles = 6
            var seed = 1
            // keyboard.character gives the character you typed, like '+' (also with shift)
            keyboard.character.listen {
                if (it.character == 'r') seed = seed + 1
                if (it.character == '+' || it.character == '=') tiles = tiles + 1
                if (it.character == '-' && tiles > 1) tiles = tiles - 1
            }

            val font = loadFont("data/fonts/default.otf", 16.0)

            extend {
                val random = Random(seed)
                drawer.clear(ColorRGBa.BLACK)

                val size = 720.0 / tiles
                for (row in 0 until tiles) {
                    for (column in 0 until tiles) {
                        val image = images.random(random)

                        // the same part of the image, as a fraction: 0.0 = left/top, 1.0 = right/bottom
                        val source = Rectangle(
                            column * image.width / tiles.toDouble(),
                            row * image.height / tiles.toDouble(),
                            image.width / tiles.toDouble(),
                            image.height / tiles.toDouble()
                        )
                        val target = Rectangle(column * size, row * size, size, size)
                        drawer.image(image, source, target)
                    }
                }

                if (!usingOwnImages) {
                    drawer.fontMap = font
                    drawer.stroke = null
                    drawer.fill = ColorRGBa.BLACK.opacify(0.7)
                    drawer.rectangle(0.0, 690.0, 720.0, 30.0)
                    drawer.fill = ColorRGBa.PINK
                    drawer.text("no images in data/my-images yet: showing the archive", 10.0, 710.0)
                }
            }
        }
    }
}

// EXERCISE 9
// a) Put at least 5 of your own images in data/my-images and run the program.
// b) Use only 2 images, photos of two faces taken from the same distance. What happens?
// c) Make the tiles narrow strips instead of squares: 1 row, 40 columns.
// d) Let the tiles change by themselves: give every tile its own Random, with a seed that
//    changes every few seconds, like Random(row * 100 + column + (seconds * 2.0).toInt())
