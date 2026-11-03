import org.openrndr.application
import org.openrndr.color.ColorRGBa

// Part 4 — Making choices: if and else
//
// New ingredients: if, else, and comparing
//
//   if (something is true) {
//       do this
//   } else {
//       otherwise, do this
//   }
//
// Ways to compare two numbers:
//   a < b    a is smaller than b          a > b    a is bigger than b
//   a <= b   smaller or the same          a >= b   bigger or the same
//   a == b   a is the same as b (two = signs!)
//
// In this program:
//  - circles to the left of the mouse are pink, the others are white
//  - the yellow ball moves to the right. When it leaves the window it starts again on the left.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            var ballX = 0.0

            extend {
                drawer.clear(ColorRGBa.BLACK)
                drawer.stroke = null

                for (row in 0 until 9) {
                    for (column in 0 until 9) {
                        val x = 60.0 + column * 75.0
                        val y = 60.0 + row * 75.0

                        if (x < mouse.position.x) {
                            drawer.fill = ColorRGBa.PINK
                        } else {
                            drawer.fill = ColorRGBa.WHITE
                        }
                        drawer.circle(x, y, 20.0)
                    }
                }

                // the ball
                drawer.fill = ColorRGBa.YELLOW
                drawer.circle(ballX, 360.0, 40.0)
                ballX = ballX + 4.0
                if (ballX > width) {
                    ballX = 0.0
                }
            }
        }
    }
}

// EXERCISE 4
// a) Make the circles ABOVE the mouse pink (use y and mouse.position.y).
// b) Make the circles bigger when they are left of the mouse, instead of another colour.
// c) Make only the middle row a different colour (hint: row == 4).
// d) Make the ball bounce: use a `var speed = 4.0`, add speed to ballX, and when the ball
//    reaches the right side, make speed = -4.0. What do you need for the left side?
