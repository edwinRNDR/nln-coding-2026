import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.imageFit.imageFit
import org.openrndr.extra.shapes.hobbycurve.hobbyCurve
import org.openrndr.extra.shapes.rectify.rectified
import org.openrndr.ffmpeg.loadVideoDevice
import org.openrndr.math.Vector2
import org.openrndr.shape.Rectangle
import org.openrndr.shape.ShapeContour

// Part 5 — Painting with the webcam
//
// New ingredients: putting it together: webcam + strokes + imageFit along a curve
//
// We take the strokes from part 3, but instead of dots we place small webcam pictures along
// every curve. Because we draw everything again every frame, the pictures stay LIVE: your
// painting moves with the camera.
//
//   Rectangle.fromCenter(point, width, height)   a rectangle with its middle at `point`
//
// Every picture is a bit smaller than the one before, so strokes get thinner at the end.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val webcam = loadVideoDevice()
            webcam.play()

            val strokes = mutableListOf<ShapeContour>()
            val points = mutableListOf<Vector2>()

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
            keyboard.keyDown.listen {
                if (it.name == "c") {
                    strokes.clear()
                }
            }

            extend {
                drawer.clear(ColorRGBa.BLACK)
                webcam.draw(drawer, blind = true)
                val camera = webcam.colorBuffer

                if (camera != null) {
                    for (stroke in strokes) {
                        val r = stroke.rectified()
                        val count = (stroke.length / 25.0).toInt()   // a picture every 25 pixels
                        for (i in 0 until count) {
                            val t = i / count.toDouble()
                            val size = 80.0 - t * 60.0              // from 80 down to 20 pixels
                            val box = Rectangle.fromCenter(r.position(t), size, size)
                            drawer.imageFit(camera, box)
                        }
                    }
                }

                drawer.stroke = ColorRGBa.PINK
                if (points.size > 1) {
                    drawer.lineStrip(points)
                }
            }
        }
    }
}

// EXERCISE 5
// a) Put a pink outline around every picture (drawer.rectangle(box) with fill = null).
// b) Try narrow, tall pictures: Rectangle.fromCenter(r.position(t), 20.0, 80.0).
//    (In part 7 you learn masks; then you can make round ones.)
// c) A "frozen" version with NoClear (part 1): collect the drag positions in a list, and in
//    extend draw one webcam picture at each new position, then empty the list. Now every
//    picture keeps the moment you painted it.
// d) Let the pictures breathe: size * (1.0 + 0.3 * sin(seconds * 3.0))
//    (add `import kotlin.math.sin` at the top)
