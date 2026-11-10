import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.math.map
import java.io.File

// Part 6 — Your own data
//
// New ingredients: column names from the file, ; as separator, decimal commas, arrow keys
//
// Now it's your turn: collect your own data and save it as data/csv/my-data.csv. Some ideas:
//   - your week: day, hours of sleep, cups of tea, minutes on your phone, mood (1-10)
//   - your music: song, artist, year, length in seconds, times played
//   - your street: house number, number of windows, colour of the door, plants in the garden
//
// Make it in a spreadsheet (Excel, Numbers, Google Sheets) and save or download it as CSV,
// or type it in IntelliJ. The first line has the column names.
//
// This program works with (almost) ANY csv file:
//   - it reads the column names from the file: rows[0].keys
//   - it finds out which columns contain only numbers
//   - it draws a bar chart of one number column. The arrow keys choose another column.
//
// Two things that often go wrong with spreadsheets:
//   - Dutch (and other) versions of Excel use ; between the columns instead of ,
//     The program looks at the first line: if there's a ; in it, it uses ; as the separator.
//   - they write 3,5 instead of 3.5. Kotlin only understands 3.5, so we replace , with .
//
// Until my-data.csv exists, the program shows planets.csv.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val myFile = File("data/csv/my-data.csv")
            val file = if (myFile.exists()) myFile else File("data/csv/planets.csv")

            val firstLine = file.readLines().first()
            val separator = if (firstLine.contains(";")) ';' else ','
            val rows = csvReader { delimiter = separator }.readAllWithHeader(file)

            // the column names, in the order of the file
            val columns = rows[0].keys.toList()

            // which columns contain only numbers?
            val numberColumns = mutableListOf<String>()
            for (column in columns) {
                var onlyNumbers = true
                for (row in rows) {
                    if (row.getValue(column).replace(',', '.').toDoubleOrNull() == null) {
                        onlyNumbers = false
                    }
                }
                if (onlyNumbers) {
                    numberColumns.add(column)
                }
            }
            println("columns: $columns")
            println("number columns: $numberColumns")

            // the first column is used for the labels
            val labelColumn = columns[0]
            var chosen = 0

            keyboard.keyDown.listen {
                if (it.name == "arrow-right") chosen = chosen + 1
                if (it.name == "arrow-left") chosen = chosen - 1
                if (chosen >= numberColumns.size) chosen = 0
                if (chosen < 0) chosen = numberColumns.size - 1
            }

            val font = loadFont("data/fonts/default.otf", 16.0)

            extend {
                drawer.clear(ColorRGBa.BLACK)
                drawer.fontMap = font
                drawer.fill = ColorRGBa.WHITE
                drawer.text("reading: ${file.path}", 20.0, 30.0)

                if (numberColumns.isEmpty()) {
                    drawer.text("no column with only numbers found", 20.0, 60.0)
                    return@extend
                }

                val column = numberColumns[chosen]
                drawer.fill = ColorRGBa.PINK
                drawer.text("$column   (use the arrow keys)", 20.0, 60.0)

                // the largest value, so the longest bar fits the window
                var largest = 0.0
                for (row in rows) {
                    val value = row.getValue(column).replace(',', '.').toDouble()
                    if (value > largest) largest = value
                }

                val barHeight = 600.0 / rows.size
                for (i in 0 until rows.size) {
                    val row = rows[i]
                    val value = row.getValue(column).replace(',', '.').toDouble()
                    val y = 90.0 + i * barHeight
                    val length = map(0.0, largest, 0.0, 360.0, value)

                    drawer.fill = ColorRGBa.WHITE
                    drawer.text(row.getValue(labelColumn).take(20), 20.0, y + barHeight * 0.6)
                    drawer.fill = ColorRGBa.PINK
                    drawer.stroke = null
                    drawer.rectangle(220.0, y + 2.0, length, barHeight - 4.0)
                    drawer.fill = ColorRGBa.WHITE
                    drawer.text("$value", 230.0 + length, y + barHeight * 0.6)
                }
            }
        }
    }
}

// EXERCISE 6
// a) Make data/csv/my-data.csv with at least 5 rows and 3 columns with numbers. Run the program.
// b) Download your spreadsheet as CSV from Google Sheets (File -> Download -> CSV), or export
//    it from Excel or Numbers, and put it in data/csv. Does the program read it?
// c) Draw circles instead of bars: one circle per row, the value is the size.
// d) Draw your data in a way that fits the data: hours of sleep as moons, cups of tea as
//    cups... Throw away the bar chart and make a picture!
