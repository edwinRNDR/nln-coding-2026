import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.shapes.hobbycurve.hobbyCurve
import org.openrndr.extra.shapes.rectify.rectified
import org.openrndr.math.Vector2
import org.openrndr.shape.ShapeContour

// Part 3 — Strokes become curves
//
// New ingredients: hobbyCurve, rectified(), position(t), normal(t), mouse.buttonUp
//
// Instead of painting pixels right away, we can REMEMBER the strokes you draw, as curves.
// Then we can draw them in any style we like, and even animate them.
//
// 1. While you drag, we collect points (only when the mouse moved at least 10 pixels).
// 2. When you let go (mouse.buttonUp), hobbyCurve turns the points into a smooth curve.
// 3. We draw every curve as a row of dots, evenly spaced.
//
// For "evenly spaced" we need rectified(). A normal contour.position(0.5) is not always halfway
// along the line: the curve pieces have different lengths. A RECTIFIED contour fixes this:
//   r.position(0.5)   exactly halfway along the curve
//   r.normal(t)       the direction pointing sideways, away from the curve
//
// There is no NoClear here: we clear every frame, and draw all strokes again. That's what makes
// animation possible.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val strokes = mutableListOf<ShapeContour>()   // finished strokes
            val points = mutableListOf<Vector2>()         // the stroke you are drawing now

            mouse.buttonDown.listen {
                points.clear()
                points.add(it.position)
            }
            mouse.dragged.listen {
                if (it.position.distanceTo(points.last()) > 10.0) {
                    points.add(it.position)
                }
            }
            mouse.buttonUp.listen {
                if (points.size > 2) {
                    strokes.add(hobbyCurve(points))
                }
                points.clear()
            }

            extend {
                drawer.clear(ColorRGBa.BLACK)

                drawer.stroke = null
                drawer.fill = ColorRGBa.PINK
                for (stroke in strokes) {
                    val r = stroke.rectified()
                    val count = (stroke.length / 12.0).toInt()   // a dot every 12 pixels
                    for (i in 0 until count) {
                        val t = i / count.toDouble()             // goes from 0.0 to almost 1.0
                        drawer.circle(r.position(t), 4.0)
                    }
                }

                // the stroke you are drawing right now: a thin line through the points
                drawer.stroke = ColorRGBa.WHITE
                drawer.strokeWeight = 1.0
                if (points.size > 1) {
                    drawer.lineStrip(points)
                }
            }
        }
    }
}

// EXERCISE 3
// a) Draw short lines instead of dots: from r.position(t) to r.position(t) + r.normal(t) * 20.0
// b) Make the dots get smaller towards the end of the stroke (use t).
// c) Make the dots flow along the strokes: val t = (i / count.toDouble() + seconds * 0.1) % 1.0
// d) Press "c" to remove all strokes, and "z" to remove only the last one (strokes.removeLast()).
