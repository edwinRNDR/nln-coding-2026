import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.rgb
import org.openrndr.math.Vector2

// Part 5 — Lists
//
// New ingredients: listOf, mutableListOf, for (... in list)
//
// A list keeps many things together, in order, under one name:
//
//   val colors = listOf(ColorRGBa.PINK, ColorRGBa.WHITE, ColorRGBa.YELLOW)
//
//   colors[0]       the FIRST thing in the list (counting starts at 0!)
//   colors.size     how many things are in the list (here: 3)
//
// A for loop can walk through a list, one thing at a time:
//   for (color in colors) { ... }
//
// A mutableListOf can change: you can add things to it while the program runs.
// Here, every mouse click adds the mouse position to the list `points`.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val colors = listOf(
                ColorRGBa.PINK,
                ColorRGBa.WHITE,
                ColorRGBa.YELLOW,
                rgb(1.0, 0.5, 0.0),
                rgb(0.4, 0.8, 1.0)
            )

            val points = mutableListOf<Vector2>()   // an empty list of positions

            // when the mouse button is pressed, add its position to the list
            mouse.buttonDown.listen {
                points.add(mouse.position)
            }

            extend {
                drawer.clear(ColorRGBa.BLACK)
                drawer.stroke = null

                // a row of colour swatches: one rectangle for each colour in the list
                for (i in 0 until colors.size) {
                    drawer.fill = colors[i]
                    drawer.rectangle(20.0 + i * 70.0, 20.0, 60.0, 60.0)
                }

                // a circle on every place you clicked
                drawer.fill = colors[0]
                for (point in points) {
                    drawer.circle(point, 30.0)
                }
            }
        }
    }
}

// EXERCISE 5
// a) Add two more colours to the list. Do the swatches follow?
// b) Draw the circles with the LAST colour of the list: colors[colors.size - 1]
// c) Draw lines between the points you clicked:
//    for (i in 1 until points.size) { drawer.lineSegment(points[i - 1], points[i]) }
// d) Give every clicked circle its own colour: colors[i % colors.size]
//    (% gives what is left over after dividing, so the colours repeat: 0 1 2 3 4 0 1 2 ...)
