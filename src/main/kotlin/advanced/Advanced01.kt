import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.noclear.NoClear

// Part 1 — A painter that never forgets: orx-no-clear
//
// New ingredients: extend(NoClear()), the keyboard
//
// Until now we started every frame with drawer.clear(...): a fresh, empty canvas, 60 times per
// second. That's what makes animation work. But a painter wants the opposite: everything you
// paint should STAY on the canvas.
//
// extend(NoClear()) does exactly that. It keeps everything that was drawn in earlier frames.
// Whatever you draw now is painted on top. So a circle that follows the mouse leaves a trail.
//
//   backdrop = { ... }   what to draw once, at the very start (the empty canvas)
//
// To start over, we still need a way to clear. keyboard.keyDown.listen { } runs every time a
// key is pressed. `it.name` is the name of the key. When you press "c", we remember that we
// want to clear, and in extend we clear the canvas once.

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

            var clearCanvas = false
            keyboard.keyDown.listen {
                if (it.name == "c") {
                    clearCanvas = true
                }
            }

            extend {
                if (clearCanvas) {
                    drawer.clear(ColorRGBa.BLACK)
                    clearCanvas = false
                }

                // a see-through pink brush: where you paint more often, it gets stronger
                drawer.stroke = null
                drawer.fill = ColorRGBa.PINK.opacify(0.1)
                drawer.circle(mouse.position, 30.0)
            }
        }
    }
}

// EXERCISE 1
// a) Change the size and the opacity of the brush. What does opacify(1.0) look like?
// b) Make the brush size grow and shrink with time: 20.0 + seconds % 2.0 * 20.0
// c) Paint with a white outline and no fill. Then with a square (drawer.rectangle) instead.
// d) Add a key "w" that clears the canvas to WHITE, and paint in black.
