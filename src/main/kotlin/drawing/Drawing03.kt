import org.openrndr.application
import org.openrndr.color.ColorRGBa

// Part 3 — Many shapes with loops
//
// New ingredient: a loop inside a loop
//
// You know the for loop from the turtle course:
//   for (i in 0 until 10) { ... }      runs 10 times, i counts 0, 1, 2 ... 9
//
// With i you can place shapes next to each other: x = 60.0 + i * 75.0 gives 60, 135, 210, ...
//
// Put a loop inside another loop and you get a grid. For every row (y), the inner loop
// draws a whole row of circles (x). 9 rows of 9 circles = 81 circles, from 4 lines of code.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            extend {
                drawer.clear(ColorRGBa.BLACK)
                drawer.stroke = null
                drawer.fill = ColorRGBa.PINK

                for (row in 0 until 9) {
                    for (column in 0 until 9) {
                        val x = 60.0 + column * 75.0
                        val y = 60.0 + row * 75.0
                        val radius = 4.0 + column * 3.0   // circles get bigger to the right
                        drawer.circle(x, y, radius)
                    }
                }
            }
        }
    }
}

// EXERCISE 3
// a) Make the circles get bigger going DOWN instead of going right.
// b) Make a grid of 15 by 15 small squares (drawer.rectangle) instead of circles.
// c) Make the radius grow both to the right and down: 4.0 + column * 2.0 + row * 2.0
