import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.imageFit.FitMethod
import org.openrndr.extra.imageFit.imageFit
import org.openrndr.ffmpeg.loadVideoDevice
import org.openrndr.shape.Rectangle

// Part 4 — The webcam
//
// New ingredients: loadVideoDevice(), drawer.imageFit
//
// A webcam is a video that never ends. OPENRNDR plays it with a video player:
//
//   val webcam = loadVideoDevice()      open the webcam
//   webcam.play()                       start it
//
// Every frame, webcam.draw(drawer, blind = true) fetches the newest picture from the camera,
// without drawing it ("blind"). After that, webcam.colorBuffer is the picture, an image just
// like in drawing part 9.
//
// The very first frames there may not be a picture yet: then webcam.colorBuffer is `null`
// (nothing). That's why we check `if (camera != null)` before we use it.
//
// drawer.imageFit draws an image into a rectangle, WITHOUT stretching it. You choose how:
//   FitMethod.Cover     fill the whole rectangle, cut off what doesn't fit
//   FitMethod.Contain   show the whole image, leave empty space if needed
//   FitMethod.Fill      stretch it after all
//
// On a Mac the first time, the computer asks for permission to use the camera: say yes.
// Then run the program again.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val webcam = loadVideoDevice()
            webcam.play()

            extend {
                drawer.clear(ColorRGBa.BLACK)
                webcam.draw(drawer, blind = true)

                val camera = webcam.colorBuffer
                if (camera != null) {
                    // the big picture: fill the top part of the window
                    drawer.imageFit(camera, Rectangle(0.0, 0.0, 720.0, 480.0), fitMethod = FitMethod.Cover)

                    // three small pictures to compare the fit methods
                    val methods = listOf(FitMethod.Cover, FitMethod.Contain, FitMethod.Fill)
                    for (i in 0 until methods.size) {
                        val box = Rectangle(20.0 + i * 235.0, 520.0, 210.0, 140.0)
                        drawer.imageFit(camera, box, fitMethod = methods[i])

                        drawer.fill = null
                        drawer.stroke = ColorRGBa.PINK
                        drawer.rectangle(box)
                        drawer.fill = ColorRGBa.PINK
                        drawer.text(methods[i].name, box.x, box.y + box.height + 20.0)
                    }
                }
            }
        }
    }
}

// EXERCISE 4
// a) Make the big picture fill the whole window.
// b) Make a 4 x 4 grid of small webcam pictures (a loop inside a loop).
// c) Mirror the webcam, so it works like a mirror: put the imageFit in drawer.isolated { },
//    with drawer.translate(720.0, 0.0) and drawer.scale(-1.0, 1.0)
// d) imageFit has horizontalPosition and verticalPosition (-1.0 to 1.0): which part of the
//    image stays when it is cut off. Try verticalPosition = -1.0 in the big picture.
