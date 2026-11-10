import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolated
import org.openrndr.draw.loadFont
import org.openrndr.math.map
import java.io.File
import kotlin.math.sqrt

// Part 4 — Your copy: editing the data
//
// New ingredients: File(...).exists(), toDoubleOrNull(), row["column"], null and ?:
//
// Data isn't sacred: you can copy it and change it. This program reads YOUR copy,
// data/csv/my-planets.csv. As long as that file doesn't exist, it reads planets.csv.
//
// How to make your copy, in IntelliJ:
//   1. right-click data/csv/planets.csv -> Copy, right-click data/csv -> Paste
//   2. name the copy my-planets.csv
//   3. open it and change it: add a planet, change numbers, add a column "color"
//
// When you edit by hand, things can go wrong. A program should not crash on a typo:
//   "abc".toDouble()          crashes the program
//   "abc".toDoubleOrNull()    gives null: "nothing", no number. Then we skip that row.
//
//   row.getValue("color")     crashes when there is no column "color"
//   row["color"]              gives null when there is no column "color"
//
//   a ?: b                    "a, or when a is null: b". A default value.
//
// The planets go around the Sun: one year (orbit_days) is one full turn. Earth needs 3
// seconds for a full turn; the other planets turn at the right speed compared to Earth.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val myFile = File("data/csv/my-planets.csv")
            val file = if (myFile.exists()) myFile else File("data/csv/planets.csv")
            val rows = csvReader().readAllWithHeader(file)

            val font = loadFont("data/fonts/default.otf", 16.0)

            extend {
                drawer.clear(ColorRGBa.BLACK)
                drawer.fontMap = font
                drawer.fill = ColorRGBa.PINK
                drawer.text("reading: ${file.path}", 20.0, 30.0)

                // the Sun
                drawer.stroke = null
                drawer.fill = ColorRGBa.YELLOW
                drawer.circle(360.0, 360.0, 12.0)

                var problems = 0
                for (row in rows) {
                    val name = row["name"] ?: "?"
                    val distance = row["distance_million_km"]?.toDoubleOrNull()
                    val diameter = row["diameter_km"]?.toDoubleOrNull()
                    val orbitDays = row["orbit_days"]?.toDoubleOrNull()

                    // skip a row with a missing or broken number, and count it
                    if (distance == null || diameter == null || orbitDays == null) {
                        problems = problems + 1
                        continue
                    }

                    // a "color" column is optional: if it's not there, use pink
                    val hex = row["color"]
                    val color = if (hex != null && hex.isNotBlank()) ColorRGBa.fromHex(hex) else ColorRGBa.PINK

                    val orbitRadius = map(0.0, sqrt(4515.0), 30.0, 330.0, sqrt(distance))
                    val radius = map(0.0, 142984.0, 2.0, 18.0, diameter)
                    val angle = seconds * 120.0 * 365.2 / orbitDays    // degrees

                    // the orbit
                    drawer.fill = null
                    drawer.stroke = ColorRGBa.WHITE.opacify(0.2)
                    drawer.circle(360.0, 360.0, orbitRadius)

                    // the planet, on its orbit
                    drawer.isolated {
                        drawer.translate(360.0, 360.0)
                        drawer.rotate(angle)
                        drawer.stroke = null
                        drawer.fill = color
                        drawer.circle(orbitRadius, 0.0, radius)
                        drawer.fill = ColorRGBa.WHITE
                        drawer.text(name, orbitRadius + radius + 4.0, 5.0)
                    }
                }

                if (problems > 0) {
                    drawer.fill = ColorRGBa.RED
                    drawer.text("$problems row(s) skipped: a number is missing or wrong", 20.0, 700.0)
                }
            }
        }
    }
}

// EXERCISE 4
// a) Make my-planets.csv (see above) and add Pluto, the dwarf planet:
//    Pluto,dwarf,2376,5906.4,90560,153.3,-225
//    Does the program still fit everything in the window? Why (not)? Fix it.
// b) Add a column "color" at the end of the first line (,color) and give every planet a
//    colour, like #3366ff. You may leave some colours empty: Venus,...,464,
// c) Make a typo on purpose: write 12x04 as the diameter of Venus. What happens?
// d) Invent your own planet. What does it look like? How long is its year?
