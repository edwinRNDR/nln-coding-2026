import org.openrndr.application
import org.openrndr.color.ColorRGBa

// Part 2 — Giving things a name: val and var
//
// New ingredients: val and var
//
// You can give a number a name, and then use the name instead of the number.
//
//   val size = 100.0     val = a name for something that STAYS the same
//   var x = 0.0          var = a name for something that can CHANGE
//
// Why use names?
//  - the code becomes easier to read: `size` says more than `100.0`
//  - change the number in one place, and everything that uses it changes along
//
// A var can get a new value:  x = x + 2.0  means "x becomes what x was, plus 2".
//
// Watch where the var is made: OUTSIDE extend { }. It is made once, when the program starts.
// The code in extend { } runs 60 times per second and adds 2 to x every time: the circle moves!
//
// OPENRNDR gives you some names for free: `width` and `height` are the size of the window.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            var x = 0.0   // made once, at the start

            extend {
                drawer.clear(ColorRGBa.BLACK)

                val size = 100.0
                val middleY = height / 2.0   // half of the height: the middle, from top to bottom

                drawer.stroke = null
                drawer.fill = ColorRGBa.PINK

                // three circles that all use the same names
                drawer.circle(x, middleY, size)
                drawer.circle(x, middleY - size * 2.0, size / 2.0)
                drawer.circle(x, middleY + size * 2.0, size / 2.0)

                // move a little to the right, for the next time
                x = x + 2.0
            }
        }
    }
}

// EXERCISE 2
// a) Change `size` to 50.0. How many things change?
// b) Make the circles move faster. Then make them move from right to left.
//    (Hint: start x at width.toDouble() and subtract instead of add.)
// c) Add a `var y` so the circles also move down.
// d) What happens if you move `var x = 0.0` inside extend { }? Why?
