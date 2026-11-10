import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.math.map
import java.io.File

// Part 5 — Data from the internet: curl
//
// New ingredients: downloading a file with curl, a scatter plot, missing values
//
// Lots of data is shared online as CSV. We download a real research data set: measurements of
// 344 penguins on three islands in Antarctica (the "Palmer Penguins").
//
// Open the Terminal in IntelliJ (View -> Tool Windows -> Terminal) and type, on one line:
//
//   curl -L -o data/csv/penguins.csv https://raw.githubusercontent.com/mwaskom/seaborn-data/master/penguins.csv
//
//   curl        a program that downloads things
//   -L          follow the link if the file has moved
//   -o ...      save it here (o for output)
//
// On Windows, type curl.exe instead of curl.
//
// Then run this program again. Real data is messy: some penguins have empty cells, because
// they could not be measured. toDoubleOrNull() (part 4) helps us skip those.
//
// Every penguin becomes a dot: x = the length of its bill (beak), y = the length of its
// flipper (wing). The colour shows the species. You'll see three groups appear!

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val file = File("data/csv/penguins.csv")
            val rows = if (file.exists()) csvReader().readAllWithHeader(file) else listOf()

            val font = loadFont("data/fonts/default.otf", 18.0)
            val species = listOf("Adelie", "Chinstrap", "Gentoo")
            val colors = listOf(ColorRGBa.PINK, ColorRGBa.WHITE, ColorRGBa.YELLOW)

            extend {
                drawer.clear(ColorRGBa.BLACK)
                drawer.fontMap = font

                if (!file.exists()) {
                    drawer.fill = ColorRGBa.PINK
                    drawer.text("data/csv/penguins.csv is not there yet.", 40.0, 300.0)
                    drawer.text("Download it with curl, see the top of Files05.kt", 40.0, 330.0)
                    return@extend    // stop here for this frame
                }

                // the axes
                drawer.stroke = ColorRGBa.WHITE.opacify(0.4)
                drawer.lineSegment(80.0, 640.0, 680.0, 640.0)
                drawer.lineSegment(80.0, 640.0, 80.0, 60.0)

                var skipped = 0
                drawer.stroke = null
                for (row in rows) {
                    val bill = row.getValue("bill_length_mm").toDoubleOrNull()
                    val flipper = row.getValue("flipper_length_mm").toDoubleOrNull()
                    if (bill == null || flipper == null) {
                        skipped = skipped + 1
                        continue
                    }

                    val x = map(30.0, 60.0, 80.0, 680.0, bill)
                    val y = map(170.0, 235.0, 640.0, 60.0, flipper)   // bigger flipper = higher
                    val index = species.indexOf(row.getValue("species"))
                    drawer.fill = colors[index].opacify(0.8)
                    drawer.circle(x, y, 5.0)
                }

                // labels and a legend
                drawer.fill = ColorRGBa.WHITE
                drawer.text("bill length: 30 - 60 mm  -->", 80.0, 670.0)
                drawer.text("flipper length: 170 - 235 mm", 90.0, 50.0)
                for (i in 0 until species.size) {
                    drawer.fill = colors[i]
                    drawer.circle(530.0, 500.0 + i * 30.0, 6.0)
                    drawer.text(species[i], 545.0, 506.0 + i * 30.0)
                }
                drawer.fill = ColorRGBa.WHITE
                drawer.text("${rows.size} penguins ($skipped not measured)", 400.0, 600.0)
            }
        }
    }
}

// EXERCISE 5
// a) Use "body_mass_g" (2700 - 6300) for y instead of the flipper length.
// b) Make the dots of male penguins bigger than the female ones (column "sex").
// c) Draw only the penguins of one island (column "island": Biscoe, Dream, Torgersen).
// d) Find another CSV online, download it with curl, and open it in IntelliJ. What's in it?
//    (Look in the seaborn-data collection: https://github.com/mwaskom/seaborn-data)
