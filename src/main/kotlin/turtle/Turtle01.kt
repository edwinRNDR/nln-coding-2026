import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.turtle.turtle

// Part 1 — Your first program
//
// Every OPENRNDR program has the same structure, like boxes inside boxes:
//
//   fun main()          the program starts here
//     application       the app: it opens a window
//       configure       settings for the window (its size)
//       program         your program
//         extend        the code in here runs again and again, about 60 times per second
//
// In this program the turtle walks forward 200 pixels and leaves a line behind.

fun main() {
    application {
        configure {
            width = 720   // the window is 720 pixels wide
            height = 720  // and 720 pixels high
        }
        program {
            extend {
                // Paint the whole window black. We do this first, every time.
                drawer.clear(ColorRGBa.BLACK)

                // Put a turtle in the middle of the window and tell it what to do.
                val drawing = turtle(drawer.bounds.center) {
                    forward(200.0)
                }

                // The turtle has made a drawing. Now show it on the screen:
                drawer.stroke = ColorRGBa.PINK  // the colour of the lines
                drawer.contours(drawing)        // draw the turtle's lines
            }
        }
    }
}

// EXERCISE 1
// a) Make the line longer. How long can you make it before it leaves the window?
// b) Change PINK into another colour, for example YELLOW, WHITE or RED.
// c) Change the background colour BLACK into another colour.
