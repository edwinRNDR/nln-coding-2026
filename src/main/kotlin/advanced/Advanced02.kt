import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.noclear.NoClear
import org.openrndr.shape.LineSegment

// Part 2 — Listening to the mouse
//
// New ingredients: mouse.dragged, mouse.scrolled, keys for colours, events vs. drawing
//
// In part 1 the brush painted all the time. A real painter only paints while you hold the
// mouse button down and move: that's called DRAGGING.
//
//   mouse.dragged.listen { }    every time the mouse moves with a button held down
//   mouse.scrolled.listen { }   every time you scroll (it.rotation.y: up or down)
//
// Inside the listener, `it` tells you what happened:
//   it.position          where the mouse is now
//   it.dragDisplacement  how far it moved since the last time
//
// So the mouse came FROM it.position - it.dragDisplacement and went TO it.position. We draw a
// line between those two points, so fast movements don't leave gaps.
//
// Important: listeners only COLLECT what happened, in a list. Drawing happens in extend,
// where we draw what was collected and then empty the list.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            extend(NoClear()) {
                backdrop = { drawer.clear(ColorRGBa.BLACK) }
            }

            val colors = listOf(ColorRGBa.PINK, ColorRGBa.WHITE, ColorRGBa.YELLOW, ColorRGBa.BLACK)
            var color = colors[0]
            var brushSize = 10.0
            val newLines = mutableListOf<LineSegment>()

            mouse.dragged.listen {
                val from = it.position - it.dragDisplacement
                newLines.add(LineSegment(from, it.position))
            }

            mouse.scrolled.listen {
                brushSize = brushSize + it.rotation.y
                if (brushSize < 1.0) {
                    brushSize = 1.0
                }
            }

            keyboard.keyDown.listen {
                when (it.name) {
                    "1" -> color = colors[0]
                    "2" -> color = colors[1]
                    "3" -> color = colors[2]
                    "4" -> color = colors[3]   // black: an eraser!
                }
            }

            extend {
                drawer.stroke = color
                drawer.strokeWeight = brushSize
                drawer.lineSegments(newLines)
                newLines.clear()   // these lines are painted now, forget them
            }
        }
    }
}

// EXERCISE 2
// a) Add two more colours on the keys 5 and 6.
// b) Use mouse.buttonDown.listen { } to paint a big dot when you click (collect the positions
//    in a list, like the lines).
// c) Make the line thicker when you move the mouse faster: it.dragDisplacement.length
// d) Show the brush: draw a small circle outline at the mouse. Why does it leave a trail?
//    (We fix this in part 6 with layers.)
