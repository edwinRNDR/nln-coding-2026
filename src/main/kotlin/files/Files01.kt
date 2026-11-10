import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import java.io.File

// Part 1 — Reading a CSV file
//
// New ingredients: CSV files, csvReader().readAllWithHeader(...), row.getValue("column")
//
// A CSV file ("comma separated values") is a table written as plain text. Open
// data/csv/planets.csv in IntelliJ and have a look:
//
//   name,type,diameter_km,distance_million_km,...      <- the first line: the column names
//   Mercury,rocky,4879,57.9,...                        <- every other line: one row
//   Venus,rocky,12104,108.2,...
//
// Commas separate the columns. Any spreadsheet program (Excel, Numbers, Google Sheets) can
// open and save CSV files.
//
//   val rows = csvReader().readAllWithHeader(File("data/csv/planets.csv"))
//
// gives a list (drawing part 5) with one entry per row. Each row knows its values by column
// name:
//   row.getValue("name")       "Mercury"
//   row.getValue("type")       "rocky"
//
// Everything read from a file is TEXT (a String), even "4879". Part 2 turns text into numbers.
//
// println(...) writes to the console at the bottom of IntelliJ, not in the window. Handy to
// see what's inside your data.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val rows = csvReader().readAllWithHeader(File("data/csv/planets.csv"))

            println("planets.csv has ${rows.size} rows")
            for (row in rows) {
                println(row)
            }

            val font = loadFont("data/fonts/default.otf", 28.0)

            extend {
                drawer.clear(ColorRGBa.BLACK)
                drawer.fontMap = font

                // the column names, as a header
                drawer.fill = ColorRGBa.PINK
                drawer.text("name", 60.0, 80.0)
                drawer.text("type", 260.0, 80.0)
                drawer.text("diameter (km)", 440.0, 80.0)

                // one line of text for every row
                drawer.fill = ColorRGBa.WHITE
                for (i in 0 until rows.size) {
                    val row = rows[i]
                    val y = 150.0 + i * 65.0
                    drawer.text(row.getValue("name"), 60.0, y)
                    drawer.text(row.getValue("type"), 260.0, y)
                    drawer.text(row.getValue("diameter_km"), 440.0, y)
                }
            }
        }
    }
}

// EXERCISE 1
// a) Look at the console: what does one row look like when you println it?
// b) Show the column "mean_temp_c" instead of the diameter. Add "°C" behind it:
//    row.getValue("mean_temp_c") + " °C"
// c) Write only the planets of the type "gas giant" (if, drawing part 4). Careful: then the
//    rows leave gaps. Can you fix that with a var y?
// d) Open data/csv/image-archive.csv in the same way, and write the titles of the paintings.
