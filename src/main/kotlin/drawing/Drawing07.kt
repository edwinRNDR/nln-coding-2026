import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.noise.scatter
import org.openrndr.shape.Circle
import org.openrndr.shape.contour

// Part 7 — Doing more with shapes: sub and scatter
//
// New ingredients: contour.sub(start, end) and shape.scatter(distance)
//
// sub: a PART of a contour
//   You can ask a contour for a piece of itself. The numbers go from 0.0 (the start) to 1.0 (the end):
//     wave.sub(0.0, 1.0)    the whole contour
//     wave.sub(0.0, 0.5)    the first half
//     wave.sub(0.25, 0.75)  the middle half
//   Here the end comes from the mouse: move the mouse left and right to draw more or less.
//
// scatter: points INSIDE a shape
//   shape.scatter(30.0) gives a list of points spread out inside the shape, each about 30 pixels
//   away from the others. It's a list (part 5), so we walk through it with a for loop.
//   Scattering takes a moment, so we do it once, outside extend.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            // a wavy line made of curves
            val wave = contour {
                moveTo(40.0, 200.0)
                curveTo(130.0, 50.0, 220.0, 200.0)
                curveTo(310.0, 350.0, 400.0, 200.0)
                curveTo(490.0, 50.0, 580.0, 200.0)
                curveTo(630.0, 280.0, 680.0, 200.0)
            }

            // points inside a circle
            val disc = Circle(360.0, 490.0, 190.0)
            val points = disc.scatter(30.0)

            extend {
                drawer.clear(ColorRGBa.BLACK)

                // how much of the wave: 0.0 when the mouse is on the left, 1.0 on the right
                val amount = mouse.position.x / width

                drawer.fill = null
                drawer.stroke = ColorRGBa.PINK
                drawer.strokeWeight = 8.0
                drawer.contour(wave.sub(0.0, amount))

                // a dot on every scattered point
                drawer.stroke = null
                drawer.fill = ColorRGBa.WHITE
                for (point in points) {
                    drawer.circle(point, 8.0)
                }
            }
        }
    }
}

// EXERCISE 7
// a) Draw the wave from the END: wave.sub(1.0 - amount, 1.0)
// b) Use seconds instead of the mouse, so the wave draws itself. Hint: seconds / 5.0
//    keeps growing; use `if` (part 4) to stop it at 1.0.
// c) Scatter with 15.0 instead of 30.0. And with 60.0.
// d) Scatter points inside the house or the leaf from part 6 (a contour can scatter too!).
//    Draw the outline as well.
