import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.turtle.turtle

// Part 6 — Lifting the pen
//
// New ingredients: penUp() and penDown()
//
// The turtle holds a pen. Normally the pen touches the paper, so the turtle draws a line
// wherever it walks.
//   penUp()    lift the pen: the turtle walks without drawing
//   penDown()  put the pen down again: the turtle draws again
//
// In this program the turtle first walks to the left side of the window with its pen up.
// Then it turns around and draws a dashed line: pen down, walk, pen up, walk, ...

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
                    // walk to the left side without drawing
                    penUp()
                    rotate(180.0)
                    forward(300.0)
                    rotate(180.0)

                    // draw a dashed line back to the right
                    for (i in 0 until 15) {
                        penDown()
                        forward(20.0)
                        penUp()
                        forward(20.0)
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

// EXERCISE 6
// a) Make the dashes longer and the gaps shorter.
// b) Draw a dashed square.
// c) Draw two squares next to each other: draw one, lift the pen, walk to the side,
//    put the pen down, draw the second one.
