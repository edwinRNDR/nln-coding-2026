import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadImage

// Part 9 — Images
//
// New ingredients: loadImage, drawer.image
//
// You can draw photos and pictures too. An image is a file, just like a font. This project
// has one in the folder data/images. Put your own pictures in that folder to use them.
//
//   val image = loadImage("data/images/cheeta.jpg")    load it once, at the start
//
//   drawer.image(image)                         draw it with its top left corner at (0, 0)
//   drawer.image(image, x, y)                   top left corner at (x, y)
//   drawer.image(image, x, y, width, height)    and make it this wide and this high
//
//   image.width, image.height                   how big the image is, in pixels
//
// Careful: if the width and height you choose don't match the shape of the image, the picture
// gets stretched or squashed.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")   // 640 x 480 pixels

            extend {
                drawer.clear(ColorRGBa.BLACK)

                // the image at half its size, near the top
                val w = image.width / 2.0
                val h = image.height / 2.0
                drawer.image(image, 40.0, 40.0, w, h)

                // a pink frame around it
                drawer.fill = null
                drawer.stroke = ColorRGBa.PINK
                drawer.strokeWeight = 6.0
                drawer.rectangle(40.0, 40.0, w, h)

                // a row of 5 small copies, with a for loop
                for (i in 0 until 5) {
                    drawer.image(image, 40.0 + i * 130.0, 360.0, 120.0, 90.0)
                }

                // a stretched copy that follows the mouse: its width comes from the mouse
                drawer.image(image, 40.0, 480.0, mouse.position.x, 200.0)
            }
        }
    }
}

// EXERCISE 9
// a) Put your own photo in data/images and draw it.
// b) Make a grid of 4 x 4 small copies (a loop inside a loop, see part 3).
// c) Make a museum label: draw the image with a frame, and below it a title with
//    drawer.text (see part 8).
// d) Draw a copy of the image that does NOT get squashed when you change its width:
//    height = width * image.height / image.width
