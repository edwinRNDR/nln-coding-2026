import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolated
import org.openrndr.draw.loadImage
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.imageFit.imageFit
import org.openrndr.shape.Rectangle
import java.io.File
import kotlin.random.Random

// Part 8 — Random compositions
//
// New ingredients: Random(seed), nextDouble(), random(), cutting with imageFit
//
// Let the computer make the choices: which image, where, how big, at what angle. That's
// called randomness.
//
//   val random = Random(seed)
//   random.nextDouble(100.0, 300.0)     a random number between 100 and 300
//   random.nextInt(0, 10)               a random whole number: 0, 1, ... 9
//   images.random(random)               a random image from the list
//
// The seed is the starting point of the randomness. The same seed always gives the SAME
// "random" choices, in the same order. That's why we make a new Random(seed) every frame:
// the composition stays still, instead of changing 60 times per second. Press "r" for a new
// seed, and a new composition.
//
// imageFit (advanced part 4) cuts an image to fit a rectangle. horizontalPosition and
// verticalPosition (-1.0 to 1.0) choose WHICH part you see. Random positions give random
// details of the paintings.
//
// The seed is shown at the bottom: if you like a composition, write down its seed. Press
// SPACE to save it.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            extend(Screenshots())

            val images = File("data/image-archive").listFiles()
                .filter { it.extension == "jpg" || it.extension == "png" }
                .sortedBy { it.name }
                .map { loadImage(it) }

            var seed = 1
            keyboard.keyDown.listen {
                if (it.name == "r") {
                    seed = seed + 1
                }
            }

            extend {
                val random = Random(seed)

                drawer.clear(ColorRGBa.BLACK)

                // the background: one image, filling the window
                drawer.imageFit(images.random(random), drawer.bounds)

                // 12 random pieces on top
                for (i in 0 until 12) {
                    val image = images.random(random)
                    val w = random.nextDouble(80.0, 300.0)
                    val h = random.nextDouble(80.0, 300.0)
                    val x = random.nextDouble(0.0, 720.0)
                    val y = random.nextDouble(0.0, 720.0)
                    val angle = random.nextDouble(-15.0, 15.0)

                    drawer.isolated {
                        drawer.translate(x, y)
                        drawer.rotate(angle)
                        val box = Rectangle(-w / 2.0, -h / 2.0, w, h)
                        drawer.imageFit(
                            image, box,
                            horizontalPosition = random.nextDouble(-1.0, 1.0),
                            verticalPosition = random.nextDouble(-1.0, 1.0)
                        )
                        // a thin white border, like a cut out piece of paper
                        drawer.fill = null
                        drawer.stroke = ColorRGBa.WHITE
                        drawer.strokeWeight = 3.0
                        drawer.rectangle(box)
                    }
                }

                drawer.stroke = null
                drawer.fill = ColorRGBa.BLACK.opacify(0.7)
                drawer.rectangle(0.0, 690.0, 720.0, 30.0)
                drawer.fill = ColorRGBa.WHITE
                drawer.text("seed $seed   r: new composition   space: save", 10.0, 710.0)
            }
        }
    }
}

// EXERCISE 8
// a) Change the number of pieces, and their smallest and largest size.
// b) Make all pieces round: use a compositor layer with a mask (advanced part 7), or make
//    narrow strips: a width between 10 and 40, a height of 720.
// c) Make the composition change slowly: seed = (seconds / 2.0).toInt()
// d) Make a composition that is not random at all, but still uses all ten images. Which one
//    do you like better?
