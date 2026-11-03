import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.turtle.turtle

// Part 5 — Counting along
//
// New ingredient: using i inside the loop
//
// Remember that `i` counts along in the loop: 0, 1, 2, 3, ...
// You can use i in your code. Here the turtle walks i * 4.0 pixels:
//   the first time 0 pixels, then 4, then 8, then 12, ...
// Every step is a little longer than the one before, so the turtle never gets back to where
// it started. It walks a spiral!

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
                    for (i in 0 until 80) {
                        forward(i * 4.0)
                        rotate(90.0)
                    }
                }

                drawer.stroke = ColorRGBa.PINK
                drawer.strokeWeight = 2.0
                drawer.fill = null
                drawer.contours(drawing)
            }
        }
    }
}

// EXERCISE 5
// a) Change rotate(90.0) into rotate(91.0). Then try 120.0, 121.0 and 144.0.
// b) Change i * 4.0 into i * 2.0 and make the loop run 200 times.
// c) Find the spiral you like best. Write down its numbers!
