import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.layer
import org.openrndr.math.Vector2

// Part 6 — Layers: orx-compositor
//
// New ingredients: compose { }, layer { }, draw { }, clearColor, enabled
//
// Think of sheets of transparent plastic lying on top of each other. You can draw on each
// sheet separately, take a sheet away, or treat it in a special way. Those sheets are LAYERS.
//
//   val composite = compose {        all the layers together
//       draw { ... }                 the bottom: what is drawn first
//       layer {                      a sheet on top of it
//           draw { ... }
//       }
//       layer { ... }                and another sheet, on top of that
//   }
//
//   composite.draw(drawer)           in extend: draw all layers, from the bottom up
//
// Normally every layer is cleared every frame, like before. But this painter has a special
// layer: clearColor = null means "never clear this layer", just like NoClear, but for this
// sheet only. So the paint stays, while the brush outline on the layer above moves without
// leaving a trail. That fixes exercise 2d!
//
// layer { } gives the layer back, so we can keep it under a name and switch it off and on
// with `enabled`. Press 1, 2 or 3.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val newPoints = mutableListOf<Vector2>()
            mouse.dragged.listen { newPoints.add(it.position) }

            val composite = compose {
                // the bottom: dark paper with a grid of thin lines
                draw {
                    drawer.clear(ColorRGBa.BLACK)
                    drawer.stroke = ColorRGBa.WHITE.opacify(0.2)
                    for (i in 0 until 18) {
                        drawer.lineSegment(i * 40.0, 0.0, i * 40.0, 720.0)
                        drawer.lineSegment(0.0, i * 40.0, 720.0, i * 40.0)
                    }
                }

                // layer 2: the paint. It is never cleared, so the paint stays.
                val paintLayer = layer {
                    clearColor = null
                    draw {
                        drawer.stroke = null
                        drawer.fill = ColorRGBa.PINK.opacify(0.3)
                        for (point in newPoints) {
                            drawer.circle(point, 25.0)
                        }
                        newPoints.clear()
                    }
                }

                // layer 3: the brush outline. Cleared every frame, so no trail.
                val brushLayer = layer {
                    draw {
                        drawer.fill = null
                        drawer.stroke = ColorRGBa.WHITE
                        drawer.circle(mouse.position, 25.0)
                    }
                }

                keyboard.keyDown.listen {
                    when (it.name) {
                        "1" -> this.enabled = !this.enabled          // the whole composite
                        "2" -> paintLayer.enabled = !paintLayer.enabled
                        "3" -> brushLayer.enabled = !brushLayer.enabled
                    }
                }
            }

            extend {
                composite.draw(drawer)
            }
        }
    }
}

// EXERCISE 6
// a) Switch the layers off and on with 2 and 3. What happens to the paint while layer 2 is off?
// b) Add a layer on top with your name in big letters (drawer.text). Can you paint over it?
//    Move the layer to another place in compose { } so you can.
// c) Add a second paint layer with another colour, and a key to choose which layer you paint on.
// d) Key "c": clear the paint. (Hint: a var, and drawer.clear(ColorRGBa.TRANSPARENT) inside the
//    draw { } of the paint layer.)
