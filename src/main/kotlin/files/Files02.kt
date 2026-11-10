import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolated
import org.openrndr.draw.loadFont
import java.io.File

// Part 2 — From text to numbers
//
// New ingredients: toDouble(), drawing with numbers from a file
//
// "142984" is text: you can't use it as a size. toDouble() turns it into a number:
//   val diameter = row.getValue("diameter_km").toDouble()       142984.0
//
// Now we can draw with it. Jupiter is 142984 km wide, too big for our window, so we divide
// by 1000: then Jupiter is 143 pixels wide and Mercury 5. Dividing everything by the same
// number keeps the sizes honest: Jupiter still is 29 times as wide as Mercury.
//
// The planets are placed next to each other. A var x keeps track of where the next planet
// goes: after every planet, x moves on by the width of that planet plus a gap of 25 pixels.
//
// The names are written at an angle (drawing part 10: isolated, translate, rotate), so the
// names of the small planets don't overlap.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val rows = csvReader().readAllWithHeader(File("data/csv/planets.csv"))
            val font = loadFont("data/fonts/default.otf", 20.0)

            extend {
                drawer.clear(ColorRGBa.BLACK)
                drawer.fontMap = font

                var x = 20.0
                for (row in rows) {
                    val diameter = row.getValue("diameter_km").toDouble()
                    val size = diameter / 1000.0           // in pixels
                    val radius = size / 2.0

                    drawer.stroke = null
                    drawer.fill = ColorRGBa.PINK
                    drawer.circle(x + radius, 360.0, radius)

                    // the name and the diameter, at an angle, below the planet
                    drawer.fill = ColorRGBa.WHITE
                    drawer.isolated {
                        drawer.translate(x + radius, 450.0)
                        drawer.rotate(60.0)
                        drawer.text("${row.getValue("name")}  ${diameter.toInt()} km", 0.0, 0.0)
                    }

                    x = x + size + 25.0
                }

                drawer.fill = ColorRGBa.PINK
                drawer.text("the planets, 1 pixel = 1000 km", 30.0, 60.0)
            }
        }
    }
}

// EXERCISE 2
// a) Divide by 800.0 instead of 1000.0. And by 2000.0. What changes, what stays the same?
// b) Put the planets in order from small to big:
//    val sorted = rows.sortedBy { it.getValue("diameter_km").toDouble() }
//    and loop over `sorted` instead of `rows`.
// c) Draw the length of a day (day_hours) as a bar under each planet. Which column do you
//    need to divide by what to make it fit?
// d) Add up all diameters in a var total, and write the total in the window.
