import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.turtle.turtle

// Part 9 — Make it move
//
// New ingredients: seconds and mouse.position
//
// Remember: the code in extend { } runs about 60 times per second. Every time, we paint the
// window black and the turtle makes a brand new drawing. If a number changes a little each
// time, the drawing changes a little each time: it moves!
//
//   seconds           how many seconds the program has been running: 0.0, 0.016, 0.033, ...
//   mouse.position.x  how far the mouse is from the left side of the window
//   mouse.position.y  how far the mouse is from the top of the window
//
// Here the turtle first turns seconds * 45.0 degrees, so the square turns slowly.
// The size of the square comes from the mouse: move the mouse left and right!

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
                    rotate(seconds * 45.0)
                    for (i in 0 until 4) {
                        forward(mouse.position.x)
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

// EXERCISE 9
// a) Make the square turn faster. Make it turn the other way.
// b) Use mouse.position.y to change the thickness of the line (drawer.strokeWeight).
// c) Take the spiral from part 5 and use rotate(90.0 + seconds) inside the loop.
