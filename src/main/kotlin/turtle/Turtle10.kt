import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.turtle.turtle

// Part 10 — Remember this spot
//
// New ingredients: push() and pop()
//
//   push()  the turtle remembers where it is standing and which way it is looking
//   pop()   the turtle jumps back to the spot it remembered (without drawing)
//
// That is handy to draw lines that all start from the same point, like the rays of a sun:
// remember the middle, walk out, jump back to the middle, turn a bit, and again.
//
// This program also lets you save your drawing: press the SPACE bar and a picture is saved
// in the folder "screenshots" of the project. That comes from extend(Screenshots()).

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            extend(Screenshots())

            extend {
                drawer.clear(ColorRGBa.BLACK)

                val drawing = turtle(drawer.bounds.center) {
                    for (i in 0 until 24) {
                        push()                    // remember the middle
                        forward(100.0 + i * 8.0)  // walk out: every ray a bit longer
                        pop()                     // jump back to the middle
                        rotate(15.0)              // turn a bit for the next ray
                    }
                }

                drawer.stroke = ColorRGBa.PINK
                drawer.strokeWeight = 6.0
                drawer.fill = null
                drawer.contours(drawing)
            }
        }
    }
}

// EXERCISE 10 — final project
// a) Give every ray a small square at its end (walk out, draw a square, pop back).
// b) Make the sun turn slowly with seconds (see part 9).
// c) FINAL: make your own drawing that uses at least four ingredients from this course:
//    forward, rotate, close, for loops, i, penUp/penDown, Vector2, your own command,
//    seconds or the mouse, push/pop. Press SPACE to save it and share it with the group!
