import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolated
import org.openrndr.extensions.Screenshots
import org.openrndr.shape.contour

// Part 10 — Moving the paper: translate, rotate, scale, isolated
//
// New ingredients: drawer.translate, drawer.rotate, drawer.scale, drawer.isolated { }
//
// Until now we moved the SHAPES around. You can also move the PAPER under them:
//
//   drawer.translate(x, y)   slide the paper. After this, (0, 0) is at (x, y)
//   drawer.rotate(degrees)   turn the paper around (0, 0)
//   drawer.scale(amount)     make everything bigger (2.0 = twice as big) or smaller (0.5)
//
// So you can draw an eye around (0, 0) once, and then put it anywhere, at any angle and size.
//
// The paper stays moved for everything you draw after it. drawer.isolated { } fixes that:
// inside the { } you can move the paper as much as you like; after the }, the paper jumps back
// to where it was.
//
// This program draws a face. Move the mouse left and right to tilt it, up and down to change
// the size of the eyes. Press SPACE to save a
// picture (from extend(Screenshots())).

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            extend(Screenshots())

            // a smile, drawn around (0, 0)
            val smile = contour {
                moveTo(-90.0, 0.0)
                curveTo(0.0, 90.0, 90.0, 0.0)
            }

            extend {
                drawer.clear(ColorRGBa.BLACK)

                drawer.isolated {
                    // put (0, 0) in the middle of the window, and tilt the face with the mouse
                    drawer.translate(360.0, 380.0)
                    drawer.rotate((mouse.position.x - 360.0) / 10.0)

                    // the head
                    drawer.stroke = ColorRGBa.PINK
                    drawer.strokeWeight = 6.0
                    drawer.fill = ColorRGBa.BLACK
                    drawer.circle(0.0, 0.0, 240.0)

                    // two eyes: the same code, twice, each time on another place
                    val eyeXs = listOf(-90.0, 90.0)
                    for (eyeX in eyeXs) {
                        drawer.isolated {
                            drawer.translate(eyeX, -60.0)
                            drawer.scale(0.5 + mouse.position.y / 720.0)   // bigger eyes when the mouse is lower

                            drawer.fill = ColorRGBa.WHITE
                            drawer.circle(0.0, 0.0, 40.0)
                            drawer.fill = ColorRGBa.PINK
                            drawer.circle(0.0, 10.0, 15.0)
                        }
                    }

                    // the mouth
                    drawer.fill = null
                    drawer.isolated {
                        drawer.translate(0.0, 70.0)
                        drawer.contour(smile)
                    }
                }

                // outside isolated: the paper is back to normal, so this is in the top left
                drawer.fill = ColorRGBa.PINK
                drawer.text("tilt me with the mouse", 20.0, 30.0)
            }
        }
    }
}

// EXERCISE 10 — final project: draw a portrait
// Draw a portrait: of yourself, of someone in the group, or of an imaginary person or animal.
// Use at least five of these ingredients:
//   circle / rectangle / lineSegment     val / var                for loops
//   if / else                            lists                    contour { }
//   sub / scatter                        text / writer            images
//   translate / rotate / scale           isolated
// Some ideas:
//  - hair made of lots of lines or curves in a for loop
//  - freckles with scatter inside the face
//  - eyes that follow the mouse
//  - your name underneath, like a museum label
//  - a background made from a photo
// Press SPACE to save your portrait and share it with the group!
