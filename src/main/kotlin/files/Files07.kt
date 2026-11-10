import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.extra.imageFit.FitMethod
import org.openrndr.extra.imageFit.imageFit
import org.openrndr.shape.Rectangle
import java.io.File

// Part 7 — A folder full of images
//
// New ingredients: listFiles(), filtering file names, a list of images, data + images
//
// In data/image-archive there are ten famous paintings and photographs (all public domain,
// see data/csv/SOURCES.md). Instead of loading every image by name, we ask the folder which
// files it contains:
//
//   File("data/image-archive").listFiles()     all files in the folder
//
// We keep only the .jpg and .png files (a folder can contain other things too), sort them by
// name, and load them all into a list of images.
//
// data/csv/image-archive.csv tells us the title, the artist and the year of every file. The
// column "file" connects a row to an image: the file name is the same.
//
// Click an image to see it big, with its information. Click again to go back.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val files = File("data/image-archive").listFiles()
                .filter { it.extension == "jpg" || it.extension == "png" }
                .sortedBy { it.name }

            val images = files.map { loadImage(it) }
            println("loaded ${images.size} images")

            val info = csvReader().readAllWithHeader(File("data/csv/image-archive.csv"))

            val font = loadFont("data/fonts/default.otf", 14.0)
            val bigFont = loadFont("data/fonts/default.otf", 24.0)

            val columns = 4
            val cell = 720.0 / columns
            var selected = -1          // -1: nothing selected, show all images

            mouse.buttonDown.listen {
                if (selected == -1) {
                    val column = (it.position.x / cell).toInt()
                    val row = (it.position.y / cell).toInt()
                    val index = row * columns + column
                    if (index < images.size) {
                        selected = index
                    }
                } else {
                    selected = -1
                }
            }

            extend {
                drawer.clear(ColorRGBa.BLACK)

                if (selected == -1) {
                    // a contact sheet: all images in a grid
                    drawer.fontMap = font
                    for (i in 0 until images.size) {
                        val x = (i % columns) * cell
                        val y = (i / columns) * cell
                        drawer.imageFit(images[i], Rectangle(x + 8.0, y + 8.0, cell - 16.0, cell - 36.0), fitMethod = FitMethod.Contain)
                        drawer.fill = ColorRGBa.WHITE
                        drawer.text(files[i].nameWithoutExtension.take(17), x + 8.0, y + cell - 12.0)
                    }
                } else {
                    // one image, big, with its information from the csv
                    drawer.imageFit(images[selected], Rectangle(20.0, 20.0, 680.0, 580.0), fitMethod = FitMethod.Contain)

                    for (row in info) {
                        if (row.getValue("file") == files[selected].name) {
                            drawer.fontMap = bigFont
                            drawer.fill = ColorRGBa.PINK
                            drawer.text(row.getValue("title"), 20.0, 650.0)
                            drawer.fill = ColorRGBa.WHITE
                            drawer.text("${row.getValue("artist")}, ${row.getValue("year")}", 20.0, 690.0)
                        }
                    }
                }
            }
        }
    }
}

// EXERCISE 7
// a) Make the grid 3 columns wide. What else do you need to change? (Nothing? Check!)
// b) Use FitMethod.Cover in the grid. What's better for a contact sheet?
// c) Sort the images by year instead of by name. (Hint: read the year from the csv
//    for every file, and use sortedBy { ... }.)
// d) Make a timeline: put every image on a line from 1500 to 2000, using map (part 3) and
//    the year from the csv.
