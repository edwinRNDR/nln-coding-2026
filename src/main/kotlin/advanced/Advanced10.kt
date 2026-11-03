import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.gui.WindowedGUI
import org.openrndr.extra.parameters.ActionParameter
import org.openrndr.extra.parameters.BooleanParameter
import org.openrndr.extra.parameters.ColorParameter
import org.openrndr.extra.parameters.DoubleParameter
import org.openrndr.extra.parameters.OptionParameter
import org.openrndr.math.Vector2
import org.openrndr.shape.Rectangle

// Part 10 — Making your own tool: orx-gui
//
// New ingredients: WindowedGUI, @DoubleParameter, @ColorParameter, @OptionParameter, @BooleanParameter,
//                  @ActionParameter
//
// A tool is a program that other people can use without changing the code. orx-gui turns
// your settings into sliders, colour pickers and buttons. WindowedGUI puts them in their own
// window, next to your program, so the controls never cover your drawing:
//
//   val brush = object {
//       @DoubleParameter("size", 2.0, 100.0)  var size = 20.0          a slider from 2 to 100
//       @ColorParameter("color")              var color = ColorRGBa.PINK   a colour picker
//       @OptionParameter("shape")             var shape = BrushShape.Circle  a drop-down list
//       @BooleanParameter("mirror")           var mirror = false       a checkbox
//       @ActionParameter("clear")             fun clear() { ... }      a button
//   }
//   val gui = WindowedGUI()
//   gui.add(brush, "Brush")
//   extend(gui)
//
// In your code you simply use brush.size, brush.color, ... and they follow the GUI.
//
// For a drop-down list you first make the choices, with an `enum class`: a short list of names.
//
// This is the painter of part 6, now with a GUI window. Because the controls are in a window
// of their own, dragging a slider never paints on the canvas, and a picture saved with SPACE
// (in the screenshots folder) never shows the controls.

enum class BrushShape { Circle, Square, Line }

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val brush = object {
                @DoubleParameter("size", 2.0, 100.0, order = 0)
                var size = 20.0

                @DoubleParameter("opacity", 0.0, 1.0, order = 1)
                var opacity = 0.5

                @ColorParameter("color", order = 2)
                var color = ColorRGBa.PINK

                @OptionParameter("shape", order = 3)
                var shape = BrushShape.Circle

                @BooleanParameter("mirror", order = 4)
                var mirror = false

                var clearCanvas = false

                @ActionParameter("clear", order = 5)
                fun clear() {
                    clearCanvas = true
                }
            }

            val gui = WindowedGUI()
            gui.add(brush, "Brush")
            extend(gui)
            extend(Screenshots())

            val newPoints = mutableListOf<Vector2>()
            mouse.dragged.listen {
                newPoints.add(it.position)
            }

            val composite = compose {
                draw {
                    drawer.clear(ColorRGBa.BLACK)
                }

                layer {
                    clearColor = null
                    draw {
                        if (brush.clearCanvas) {
                            drawer.clear(ColorRGBa.TRANSPARENT)
                            brush.clearCanvas = false
                        }

                        // with mirror on, every point gets a twin on the other side
                        val points = mutableListOf<Vector2>()
                        for (point in newPoints) {
                            points.add(point)
                            if (brush.mirror) {
                                points.add(Vector2(720.0 - point.x, point.y))
                            }
                        }
                        newPoints.clear()

                        drawer.fill = brush.color.opacify(brush.opacity)
                        drawer.stroke = null
                        for (point in points) {
                            when (brush.shape) {
                                BrushShape.Circle -> drawer.circle(point, brush.size / 2.0)
                                BrushShape.Square -> drawer.rectangle(Rectangle.fromCenter(point, brush.size, brush.size))
                                BrushShape.Line -> drawer.rectangle(Rectangle.fromCenter(point, 2.0, brush.size))
                            }
                        }
                    }
                }
            }

            extend {
                composite.draw(drawer)
            }
        }
    }
}

// EXERCISE 10 — final project: make your own tool
// a) Add a slider "jitter" that moves every brush stamp a random distance:
//    point + Vector2(Random.nextDouble(-j, j), Random.nextDouble(-j, j))  (import kotlin.random.Random)
// b) Add a webcam brush (part 5): an option in BrushShape that paints webcam pictures.
// c) FINAL: build your own tool for making images and give it to someone else to use. Some ideas:
//    - a poster maker: a word (@TextParameter), letter outlines with hairs (part 9), sliders for
//      size and hair length, a blend mode or blur on top (part 8)
//    - a webcam photo booth: masks (part 7) and effects (part 8) with checkboxes
//    - a pattern painter: strokes become curves (part 3) with stamps along them
//    Let someone who has never seen your code make three pictures with it. Save them with SPACE.
