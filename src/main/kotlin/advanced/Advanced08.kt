import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.extra.compositor.blend
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.post
import org.openrndr.extra.fx.blend.Add
import org.openrndr.extra.fx.blur.ApproximateGaussianBlur
import org.openrndr.extra.fx.color.ChromaticAberration

// Part 8 — Blend modes and post processing
//
// New ingredients: blend(...), post(...)
//
// BLEND MODES decide how a layer mixes with what is below it. Normally a layer simply covers
// what's under it. With a blend mode you can mix in other ways:
//   Add()        add the colours: like light, it only gets brighter
//   Multiply()   like layers of ink: it only gets darker
//   Screen(), Overlay(), Darken(), Lighten(), Subtract(), ...
//
//   layer {
//       blend(Add())
//       draw { ... }
//   }
//
// Here three circles, red, green and blue, are added together like three coloured lamps.
// Where all three overlap, you get white!
//
// POST PROCESSING is an effect on a whole layer, after it is drawn ("post" = after):
//   post(ApproximateGaussianBlur()) { window = 25; sigma = 10.0 }     make it blurry
//   post(ChromaticAberration()) { aberrationFactor = 8.0 }           shift the colours apart
//
// The settings between { } are run every frame, so they can use the mouse or seconds.
// Here the blur grows when you move the mouse down. All effects live in org.openrndr.extra.fx.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val font = loadFont("data/fonts/default.otf", 120.0)

            val composite = compose {
                draw {
                    drawer.clear(ColorRGBa.BLACK)
                }

                // three lamps, added together
                layer {
                    blend(Add())
                    draw {
                        drawer.stroke = null
                        drawer.fill = ColorRGBa.RED
                        drawer.circle(290.0, 260.0, 150.0)
                    }
                }
                layer {
                    blend(Add())
                    draw {
                        drawer.stroke = null
                        drawer.fill = ColorRGBa.GREEN
                        drawer.circle(430.0, 260.0, 150.0)
                    }
                }
                layer {
                    blend(Add())
                    draw {
                        drawer.stroke = null
                        drawer.fill = ColorRGBa.BLUE
                        drawer.circle(mouse.position, 150.0)   // the blue lamp follows the mouse
                    }
                }

                // text with a blur that depends on the mouse
                layer {
                    draw {
                        drawer.fontMap = font
                        drawer.fill = ColorRGBa.PINK
                        drawer.text("GLOW", 220.0, 620.0)
                    }
                    post(ApproximateGaussianBlur()) {
                        window = 25
                        sigma = 0.1 + mouse.position.y / 720.0 * 10.0
                    }
                }

                // an effect on everything together
                post(ChromaticAberration()) {
                    aberrationFactor = 4.0
                }
            }

            extend {
                composite.draw(drawer)
            }
        }
    }
}

// EXERCISE 8
// a) Replace Add() by Multiply(), Screen(), Darken() or Lighten() (import them from
//    org.openrndr.extra.fx.blend). Try Multiply() on a WHITE background.
// b) Make the GLOW really glow: draw the text twice, once blurred and once sharp on top
//    (two layers).
// c) Try other effects on the whole composite, for example:
//    org.openrndr.extra.fx.color.Posterize { levels = 4 }
//    org.openrndr.extra.fx.distort.HorizontalWave { amplitude = 0.05 }
//    org.openrndr.extra.fx.edges.EdgesWork { radius = 3 }
// d) Add the webcam (part 4) as a layer with blend(Multiply()) under the lamps.
