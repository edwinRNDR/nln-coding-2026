import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.turtle.turtle
import org.openrndr.math.Vector2

// Part 7 — Where does the turtle start?
//
// New ingredient: Vector2(x, y), a place in the window
//
// The window is like a sheet of squared paper, 720 by 720 pixels.
//   x is how far from the LEFT side
//   y is how far from the TOP (so a bigger y is lower in the window!)
// The top left corner is Vector2(0.0, 0.0). The middle is Vector2(360.0, 360.0).
// Until now we used drawer.bounds.center: that is the middle of the window.
//
// In this program a loop makes 5 turtles. Each turtle starts a bit more to the right,
// because x = 60.0 + i * 130.0 is 60, then 190, 320, 450, 580.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            extend {
                drawer.clear(ColorRGBa.BLACK)
                drawer.stroke = ColorRGBa.PINK
                drawer.strokeWeight = 4.0
                drawer.fill = null

                for (i in 0 until 5) {
                    val start = Vector2(60.0 + i * 130.0, 400.0)

                    val drawing = turtle(start) {
                        for (side in 0 until 4) {
                            forward(80.0)
                            rotate(90.0)
                        }
                    }

                    drawer.contours(drawing)
                }
            }
        }
    }
}

// EXERCISE 7
// a) Put one square in each corner of the window.
// b) Make the squares go down the window instead of to the right.
// c) Make each square a bit bigger than the one before. (Hint: use i in forward.)
