import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.turtle.turtle

// Part 2 — Turning
//
// New ingredient: rotate(...)
//
// rotate(90.0) makes the turtle turn on the spot. The number is in degrees:
//   90.0  = a quarter turn to the left
//   180.0 = turn around
//   -90.0 = a quarter turn to the right (a minus sign turns the other way)
//
// The turtle starts looking to the right. Walk, turn, walk, turn... and you get a square.

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
                    forward(200.0)
                    rotate(90.0)
                    forward(200.0)
                    rotate(90.0)
                    forward(200.0)
                    rotate(90.0)
                    forward(200.0)
                }

                drawer.stroke = ColorRGBa.PINK
                drawer.contours(drawing)
            }
        }
    }
}

// EXERCISE 2
// a) Change every rotate(90.0) into rotate(-90.0). Where does the square go now?
// b) Draw a rectangle: a shape that is wider than it is high.
// c) Draw a staircase with 4 steps.
