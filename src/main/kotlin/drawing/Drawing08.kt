import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.extra.textwriter.writer
import org.openrndr.shape.Rectangle

// Part 8 — Text
//
// New ingredients: loadFont, drawer.text, writer { }
//
// To write text you need a font: the shapes of the letters. A font is a file. This project
// has one in the folder data/fonts. loadFont also needs the size of the letters, in pixels.
//
//   val font = loadFont("data/fonts/default.otf", 64.0)
//   drawer.fontMap = font                    use this font from now on
//   drawer.text("hello", x, y)               write "hello". (x, y) is the bottom left of the text
//
// Text uses the FILL colour, not the stroke.
//
// Text between quotes is called a String. With ${ } you can put a value inside a String:
//   "x is ${mouse.position.x}"
//
// For longer text, writer { } is handy. You give it a box, and it moves to a new line when a
// line is full:
//   box = Rectangle(x, y, width, height)     where the text may go
//   newLine()                                go to the next line
//   text("...")                              write

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            // load fonts once, at the start: loading takes a moment
            val bigFont = loadFont("data/fonts/default.otf", 64.0)
            val smallFont = loadFont("data/fonts/default.otf", 24.0)

            extend {
                drawer.clear(ColorRGBa.BLACK)

                // a title
                drawer.fontMap = bigFont
                drawer.fill = ColorRGBa.PINK
                drawer.text("HELLO OPENRNDR", 40.0, 120.0)

                // a longer text in a box
                val box = Rectangle(40.0, 200.0, 400.0, 300.0)
                drawer.fill = null
                drawer.stroke = ColorRGBa.PINK
                drawer.rectangle(box)           // show where the box is

                drawer.fontMap = smallFont
                drawer.fill = ColorRGBa.WHITE
                writer {
                    this.box = box
                    newLine()
                    text("This text is written by the writer. When a line is full, it goes on to the next line by itself.")
                    newLine()
                    newLine()
                    text("Neat!")
                }

                // text that changes
                drawer.fill = ColorRGBa.YELLOW
                drawer.text("mouse: ${mouse.position.x.toInt()}, ${mouse.position.y.toInt()}", 40.0, 650.0)
            }
        }
    }
}

// EXERCISE 8
// a) Write your own name, big, in the middle of the window.
// b) Make the box of the writer narrower. What happens to the text?
// c) Show how many seconds the program is running: "${seconds.toInt()} seconds"
// d) Write the same word 10 times underneath each other with a for loop, each line a bit
//    lighter: drawer.fill = ColorRGBa.PINK.opacify(1.0 - i * 0.1)
