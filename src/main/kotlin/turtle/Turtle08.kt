import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.turtle.Turtle
import org.openrndr.extra.turtle.turtle

// Part 8 — Teach the turtle a new word
//
// New ingredient: making your own command (a function)
//
// The turtle knows forward and rotate. We can teach it a new word, `square`:
//
//   fun Turtle.square(size: Double) {
//       ...
//   }
//
// "Turtle." means: this is a new command for the turtle.
// "size: Double" means: when you use it, you give it a number, and inside it is called size.
// After that you can write square(150.0) inside turtle { }, just like forward(150.0).
//
// Here the turtle draws a square, turns a little, draws a square, turns a little, ...
// 36 squares with a turn of 10 degrees make a flower (36 x 10 = 360, a full turn).

fun Turtle.square(size: Double) {
    for (i in 0 until 4) {
        forward(size)
        rotate(90.0)
    }
}

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            extend {
                drawer.clear(ColorRGBa.BLACK)

                val drawing = turtle(drawer.bounds.center) {
                    for (i in 0 until 36) {
                        square(150.0)
                        rotate(10.0)
                    }
                }

                drawer.stroke = ColorRGBa.PINK
                drawer.strokeWeight = 2.0
                drawer.fill = null
                drawer.contours(drawing)
            }
        }
    }
}

// EXERCISE 8
// a) Make a flower with 12 squares. How much should the turtle turn between the squares?
// b) Teach the turtle the word `triangle` and make a flower of triangles.
// c) Draw your flower 3 times in a row, each in its own place (see part 7).
//
// Tip: two files can not have a command with the same name. If you see the error
// "Conflicting overloads", give your command another name, for example square2.
