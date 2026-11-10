import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.math.Vector2
import org.openrndr.math.map
import java.io.File

// Part 3 — Fitting numbers in the window: map
//
// New ingredients: map(...), colours from numbers, finding the largest value
//
// The distances to the Sun go from 57.9 to 4515 million km. We want them between x = 60
// and x = 660 pixels. map does that for us, like reading a ruler:
//
//   map(57.9, 4515.0, 60.0, 660.0, distance)
//       from this range ... to this range ... this value
//
//   map(57.9, 4515.0, 60.0, 660.0, 57.9)     gives 60.0    (the start of the range)
//   map(57.9, 4515.0, 60.0, 660.0, 4515.0)   gives 660.0   (the end of the range)
//
// Instead of typing 57.9 and 4515.0 we find the smallest and largest value with a loop. Then
// the program still works when the data changes.
//
// Colours can come from numbers too: the temperature picks a colour between blue (cold) and
// red (hot) with mix(). mix(…, 0.0) is all blue, mix(…, 1.0) all red.
//
// Move the mouse over a planet to see its numbers.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val rows = csvReader().readAllWithHeader(File("data/csv/planets.csv"))
            val font = loadFont("data/fonts/default.otf", 18.0)

            // find the smallest and largest distance and temperature
            var minDistance = Double.MAX_VALUE
            var maxDistance = -Double.MAX_VALUE
            var minTemp = Double.MAX_VALUE
            var maxTemp = -Double.MAX_VALUE
            for (row in rows) {
                val distance = row.getValue("distance_million_km").toDouble()
                val temp = row.getValue("mean_temp_c").toDouble()
                if (distance < minDistance) minDistance = distance
                if (distance > maxDistance) maxDistance = distance
                if (temp < minTemp) minTemp = temp
                if (temp > maxTemp) maxTemp = temp
            }

            extend {
                drawer.clear(ColorRGBa.BLACK)
                drawer.fontMap = font

                // the line the planets sit on
                drawer.stroke = ColorRGBa.WHITE.opacify(0.3)
                drawer.lineSegment(60.0, 360.0, 660.0, 360.0)

                for (row in rows) {
                    val distance = row.getValue("distance_million_km").toDouble()
                    val diameter = row.getValue("diameter_km").toDouble()
                    val temp = row.getValue("mean_temp_c").toDouble()

                    val x = map(minDistance, maxDistance, 60.0, 660.0, distance)
                    val radius = map(0.0, 142984.0, 3.0, 50.0, diameter)
                    val t = map(minTemp, maxTemp, 0.0, 1.0, temp)
                    val color = ColorRGBa.BLUE.mix(ColorRGBa.RED, t)

                    drawer.stroke = null
                    drawer.fill = color
                    drawer.circle(x, 360.0, radius)

                    // is the mouse close to this planet? then show its numbers
                    if (mouse.position.distanceTo(Vector2(x, 360.0)) < radius + 5.0) {
                        drawer.fill = ColorRGBa.PINK
                        drawer.text(row.getValue("name"), 60.0, 520.0)
                        drawer.fill = ColorRGBa.WHITE
                        drawer.text("distance to the Sun: $distance million km", 60.0, 550.0)
                        drawer.text("diameter: $diameter km", 60.0, 575.0)
                        drawer.text("temperature: $temp °C", 60.0, 600.0)
                        drawer.text("a year lasts ${row.getValue("orbit_days")} days", 60.0, 625.0)
                    }
                }

                drawer.fill = ColorRGBa.PINK
                drawer.text("distance to the Sun  -->", 60.0, 300.0)
                drawer.text("blue = cold, red = hot. Move the mouse over a planet.", 60.0, 690.0)
            }
        }
    }
}

// EXERCISE 3
// a) The four small planets are squashed together on the left. Use the square root of the
//    distance instead: kotlin.math.sqrt(distance). Use it for min, max AND x.
// b) Put the planets on a vertical line: use map for y instead of x.
// c) Use "day_hours" for the radius instead of the diameter. Which planet wins now?
// d) Draw an orbit for every planet: a circle around the Sun (at x = 40), with the
//    distance as the radius. drawer.fill = null.
