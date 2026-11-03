import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.mask
import org.openrndr.extra.imageFit.imageFit

// Part 7 — Masks: cutting layers into shapes
//
// New ingredients: mask { }, invertMask
//
// A mask is a stencil. You draw a shape in the mask, and only the part of the layer INSIDE that
// shape stays visible. The rest of the layer disappears.
//
//   layer {
//       draw { ... }      what is on the layer
//       mask { ... }      the stencil: only here the layer is visible
//   }
//
// In the mask you don't need colours: everything you draw there counts as "visible".
// invertMask = true turns the stencil around: the layer is visible everywhere EXCEPT the shape.
//
// This program:
//  - the bottom: the photo, very dark
//  - layer 2: the same photo, bright, but masked by a circle at the mouse: a flashlight!
//  - layer 3: moving pink stripes, masked by big letters

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val font = loadFont("data/fonts/default.otf", 200.0)

            val composite = compose {
                // the bottom: the photo, darkened with a see-through black rectangle on top
                draw {
                    drawer.imageFit(image, drawer.bounds)
                    drawer.fill = ColorRGBa.BLACK.opacify(0.9)
                    drawer.stroke = null
                    drawer.rectangle(drawer.bounds)
                }

                // the flashlight
                layer {
                    draw {
                        drawer.imageFit(image, drawer.bounds)
                    }
                    mask {
                        drawer.circle(mouse.position, 120.0)
                    }
                }

                // stripes in the shape of letters
                layer {
                    draw {
                        drawer.stroke = null
                        drawer.fill = ColorRGBa.PINK
                        for (i in 0 until 40) {
                            val x = i * 40.0 - 720.0 + (seconds * 50.0) % 40.0
                            drawer.rectangle(x, 0.0, 15.0, 720.0)
                        }
                    }
                    mask {
                        drawer.fontMap = font
                        drawer.text("MASK", 110.0, 690.0)
                    }
                }
            }

            extend {
                composite.draw(drawer)
            }
        }
    }
}

// EXERCISE 7
// a) Make the flashlight bigger. Then make it a rectangle.
// b) Use invertMask = true on the flashlight layer (write it inside the layer { }). What happens?
// c) Mask the photo with your name.
// d) Make the flashlight leave a trail: a mask can use all you know, so draw circles on a list
//    of the last 20 mouse positions.
