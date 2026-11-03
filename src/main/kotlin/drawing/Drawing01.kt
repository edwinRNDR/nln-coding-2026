import org.openrndr.application
import org.openrndr.color.ColorRGBa

// Part 1 — Shapes and positions
//
// New ingredients: drawer.circle, drawer.rectangle, drawer.lineSegment
//
// The turtle drew lines by walking. The drawer can also draw shapes directly, if you tell it
// WHERE they go. Remember the window is like squared paper (turtle part 7):
//   x = how far from the LEFT,  y = how far from the TOP
//
//   drawer.circle(x, y, radius)                the middle of the circle, and how big it is
//   drawer.rectangle(x, y, width, height)      the TOP LEFT corner, then how wide and how high
//   drawer.lineSegment(x1, y1, x2, y2)         a line from one point to another
//
// The radius is the distance from the middle of a circle to its edge.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            extend {
                drawer.clear(ColorRGBa.BLACK)

                // all shapes get a pink outline that is 4 pixels thick
                drawer.stroke = ColorRGBa.PINK
                drawer.strokeWeight = 4.0

                // the sun: a yellow circle in the top right
                drawer.fill = ColorRGBa.YELLOW
                drawer.circle(560.0, 160.0, 80.0)

                // the house: a white rectangle
                drawer.fill = ColorRGBa.WHITE
                drawer.rectangle(160.0, 360.0, 240.0, 200.0)

                // the door: a smaller pink rectangle
                drawer.fill = ColorRGBa.PINK
                drawer.rectangle(250.0, 460.0, 60.0, 100.0)

                // the ground: a line from the left side to the right side
                drawer.lineSegment(0.0, 560.0, 720.0, 560.0)
            }
        }
    }
}

// EXERCISE 1
// a) Move the sun to the top left corner.
// b) Give the house two windows.
// c) Draw a snowman: three circles on top of each other, each one smaller than the one below.
