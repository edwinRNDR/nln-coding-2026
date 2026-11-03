import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.turtle.turtle

// Part 4 — Doing things again and again
//
// New ingredient: the for loop
//
// In part 2 we wrote forward and rotate four times. A for loop does the repeating for us:
//
//   for (i in 0 until 4) {
//       ...
//   }
//
// The code between { and } runs 4 times. `i` counts along: the first time it is 0, then 1,
// then 2, then 3. "0 until 4" means: start at 0 and stop *before* 4.
// To repeat something 10 times you write: for (i in 0 until 10) { ... }

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            extend {
                drawer.clear(ColorRGBa.BLACK)

                val drawing = turtle(drawer.bounds.center) {
                    for (i in 0 until 4) {
                        forward(200.0)
                        rotate(90.0)
                    }
                }

                drawer.stroke = ColorRGBa.PINK
                drawer.strokeWeight = 4.0
                drawer.fill = null
                drawer.contours(drawing)
            }
        }
    }
}

// EXERCISE 4
// A full turn is 360 degrees. To get back to the start, the turns of the turtle must add up
// to 360. A square has 4 corners: 4 turns of 90 degrees. 4 x 90 = 360.
// a) Draw a triangle: 3 corners, turn 120 degrees each time (3 x 120 = 360).
// b) Draw a hexagon: 6 corners. How far must the turtle turn each time?
// c) Make the loop run 36 times, with forward(20.0) and rotate(10.0). What shape is this?
// d) Draw a star: 5 times forward(300.0) and rotate(144.0).
