import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.shape.contour

// Part 6 — Your own shapes: contour { }
//
// New ingredients: contour { }, moveTo, lineTo, curveTo, close
//
// Circles and rectangles are not enough to draw everything. With contour { } you draw any
// shape you like, like moving a pen over paper with points:
//
//   moveTo(x, y)                  put the pen on the paper here (does not draw)
//   lineTo(x, y)                  draw a straight line to here
//   curveTo(cx, cy, x, y)         draw a curved line to (x, y). The curve is pulled
//                                 towards the "control point" (cx, cy), like a magnet
//   close()                       draw a line back to the start, so the shape can be filled
//
// The turtle also made contours! A contour is a line or a shape that the drawer can draw with
// drawer.contour(...).
//
// We make the shapes once (outside extend), and draw them every frame.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            // a house with a pointy roof, only straight lines
            val house = contour {
                moveTo(100.0, 600.0)   // bottom left
                lineTo(100.0, 400.0)   // up
                lineTo(200.0, 300.0)   // the top of the roof
                lineTo(300.0, 400.0)   // down again
                lineTo(300.0, 600.0)   // bottom right
                close()                // back to the start
            }

            // a leaf: two curves
            val leaf = contour {
                moveTo(450.0, 600.0)                      // the bottom point
                curveTo(350.0, 400.0, 550.0, 250.0)       // curve to the top, pulled to the left
                curveTo(650.0, 450.0, 450.0, 600.0)       // curve back down, pulled to the right
                close()
            }

            extend {
                drawer.clear(ColorRGBa.BLACK)
                drawer.stroke = ColorRGBa.PINK
                drawer.strokeWeight = 4.0
                drawer.fill = ColorRGBa.WHITE
                drawer.contour(house)

                drawer.fill = ColorRGBa.PINK.opacify(0.5)  // pink, half see-through
                drawer.contour(leaf)

                // the control points of the leaf, so you can see where the curves are pulled
                drawer.fill = ColorRGBa.YELLOW
                drawer.stroke = null
                drawer.circle(350.0, 400.0, 6.0)
                drawer.circle(650.0, 450.0, 6.0)
            }
        }
    }
}

// EXERCISE 6
// a) Give the house a chimney.
// b) Move the yellow control points of the leaf (change the numbers in BOTH places).
//    What happens to the shape?
// c) Draw the first letter of your name with a contour.
// d) Draw a fish: a body made of two curves and a tail made of lines.
