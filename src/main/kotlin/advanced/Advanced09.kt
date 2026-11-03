import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.font.loadFace
import org.openrndr.draw.isolated
import org.openrndr.extra.shapes.rectify.rectified

// Part 9 — Typographic art: letters as shapes
//
// New ingredients: loadFace, glyphs, letter outlines + rectified contours
//
// With loadFont (drawing part 8) you can only WRITE text. With loadFace you get the letters
// as SHAPES: outlines you can take apart and use like any contour.
//
//   val face = loadFace("data/fonts/default.otf").withSize(220.0)   a font face, 220 pixels big
//   val glyph = face.glyphForCharacter('A')                         the letter A (a "glyph")
//   glyph.shape()             the letter as a shape. Its baseline is at y = 0, so it sits on
//                             the line and goes UP (negative y)
//   glyph.shape().contours    its outlines. An "O" has two: the outside and the hole
//   glyph.advanceWidth()      how far to move to the right for the next letter
//
// Every outline is a contour, so we can rectify it (part 3) and put things along it. Here every
// letter outline gets dots that flow along it, and little lines that stick out. The length of
// the lines comes from the mouse.

fun main() {
    application {
        configure {
            width = 720
            height = 720
        }
        program {
            val face = loadFace("data/fonts/default.otf").withSize(220.0)
            val word = "TYPE"

            // how wide is the whole word? Add up the widths of the letters.
            var wordWidth = 0.0
            for (character in word) {
                wordWidth = wordWidth + face.glyphForCharacter(character).advanceWidth()
            }

            extend {
                drawer.clear(ColorRGBa.BLACK)
                val hair = mouse.position.x / 720.0 * 40.0

                var x = (720.0 - wordWidth) / 2.0     // start so that the word is in the middle
                for (character in word) {
                    val glyph = face.glyphForCharacter(character)

                    drawer.isolated {
                        drawer.translate(x, 440.0)    // the baseline of the word is at y = 440

                        for (outline in glyph.shape().contours) {
                            val r = outline.rectified()
                            val count = (outline.length / 8.0).toInt()
                            for (i in 0 until count) {
                                // % 1.0 keeps t between 0.0 and 1.0: the dots go round and round
                                val t = (i / count.toDouble() + seconds * 0.02) % 1.0
                                val p = r.position(t)

                                drawer.stroke = ColorRGBa.PINK
                                drawer.lineSegment(p, p + r.normal(t) * hair)

                                drawer.stroke = null
                                drawer.fill = ColorRGBa.WHITE
                                drawer.circle(p, 2.0)
                            }
                        }
                    }
                    x = x + glyph.advanceWidth()
                }
            }
        }
    }
}

// EXERCISE 9
// a) Write your own name. Make the letters smaller with withSize(...) if it does not fit.
// b) Make every second dot pink: if (i % 2 == 0) { ... } else { ... }
// c) Draw only a part of every outline with r.sub(0.0, mouse.position.x / 720.0) (drawing part 7)
// d) Use the letters for a mask (part 7): drawer.shape(glyph.shape()) inside mask { },
//    over a layer with the webcam.
