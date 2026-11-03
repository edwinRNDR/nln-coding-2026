import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.rgb
import org.openrndr.extra.turtle.turtle

// Part 3 — Colours, lines and fills
//
// New ingredient: close()
//
// The `drawer` is the part of OPENRNDR that puts things on the screen. Before it draws,
// you tell it how things should look:
//
//   drawer.stroke        the colour of the line around a shape (the outline)
//   drawer.strokeWeight  how thick that line is, in pixels
//   drawer.fill          the colour inside a shape. `null` means: nothing, leave it empty
//
// A fill only works for a *closed* shape. close() tells the turtle to finish the shape by
// connecting the end to the beginning.
//
// You can mix your own colour with rgb(red, green, blue). Each number goes from 0.0 (none)
// to 1.0 (full). rgb(1.0, 0.5, 0.0) is orange.

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
                    close() // the turtle does not walk the last side: close() connects it
                }

                drawer.stroke = ColorRGBa.PINK      // a pink outline
                drawer.strokeWeight = 10.0          // that is 10 pixels thick
                drawer.fill = rgb(1.0, 0.5, 0.0)    // and an orange inside
                drawer.contours(drawing)
            }
        }
    }
}

// EXERCISE 3
// a) Make the outline very thin, then very thick.
// b) Remove the fill with drawer.fill = null. Then remove the outline with drawer.stroke = null.
// c) Mix your own favourite colour with rgb(...) and use it for the fill.
// d) Remove close(). What happens to the shape and to the fill?
