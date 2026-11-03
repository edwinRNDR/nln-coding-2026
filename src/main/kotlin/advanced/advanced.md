# Advanced drawing with OPENRNDR and orx

*Part 3 of the course: after "Drawing with OPENRNDR"*

So far you have made pictures. In this course you make the **tools** that make pictures: a
painter, a webcam brush, layers with masks and effects, letters that come alive. At the end you
build a tool that someone else can use.

Most of the new ingredients come from **orx**, the big box of extras for OPENRNDR. Each part
adds a few of them. As before, each part has:

- a short explanation (this document)
- a program you can run: `src/main/kotlin/advanced/Advanced01.kt` up to `Advanced10.kt`
- an **exercise** (also at the bottom of each program)
- a slide: `slides/slide01.png` up to `slide10.png` (plus a text version, `.txt`)

Every window is 720 × 720 pixels, and you already know everything from the drawing course:
`val`/`var`, loops, `if`, lists, `contour { }`, text, images and `drawer.isolated`.

![Advanced OPENRNDR](slides/slide00.png)

---

## Part 1 — A painter that never forgets

**Program:** `Advanced01.kt` · **New:** `extend(NoClear())`, `keyboard.keyDown`

![Part 1](slides/slide01.png)

Until now every frame started with `drawer.clear(...)`, a fresh canvas 60 times per second.
That's what makes animation work. A painter wants the opposite: what you paint should **stay**.

`extend(NoClear())` (from **orx-no-clear**) keeps everything that was drawn in earlier frames,
so a circle that follows the mouse leaves a trail. You give it a `backdrop`, which is what to
draw once at the very start:

```kotlin
extend(NoClear()) {
    backdrop = { drawer.clear(ColorRGBa.BLACK) }
}
```

**The keyboard.** `keyboard.keyDown.listen { }` runs every time a key is pressed, and `it.name`
is the name of the key. To clear the canvas, we remember that we want to (a `var`), and in
`extend` we clear once:

```kotlin
var clearCanvas = false
keyboard.keyDown.listen {
    if (it.name == "c") {
        clearCanvas = true
    }
}
```

The brush is see-through (`opacify(0.1)`), so the more often you paint over a spot, the
stronger the colour gets.

### Exercise 1

1. Change the size and the opacity of the brush. What does `opacify(1.0)` look like?
2. Make the brush grow and shrink with time: `20.0 + seconds % 2.0 * 20.0`.
3. Paint with a white outline and no fill. Then with a square brush.
4. Add a key `w` that clears the canvas to white, and paint in black.

---

## Part 2 — Listening to the mouse

**Program:** `Advanced02.kt` · **New:** `mouse.dragged`, `mouse.scrolled`, events and drawing

![Part 2](slides/slide02.png)

A real painter only paints while you hold the mouse button down and move, which is called
**dragging**.

| listener                      | runs when…                       | useful                           |
|-------------------------------|----------------------------------|----------------------------------|
| `mouse.buttonDown.listen { }` | a button is pressed              | `it.position`                    |
| `mouse.dragged.listen { }`    | the mouse moves with a button down | `it.position`, `it.dragDisplacement` |
| `mouse.buttonUp.listen { }`   | a button is let go               | `it.position`                    |
| `mouse.scrolled.listen { }`   | you scroll                       | `it.rotation.y`                  |

`it.dragDisplacement` is how far the mouse moved since last time. So it came **from**
`it.position - it.dragDisplacement` and went **to** `it.position`. Drawing a line between those
points means fast movements don't leave gaps.

**Listeners collect, `extend` draws.** A listener can run at any moment, so it's not the place
to draw. It only adds what happened to a list. In `extend` we draw everything in the list and
then empty it with `newLines.clear()`. You'll see this pattern in every part from now on.

Keys `1` to `4` choose a colour (black works as an eraser), and scrolling changes the brush
size. `when` picks one of several options, a neat alternative to many `if`s.

### Exercise 2

1. Add two more colours on keys `5` and `6`.
2. Paint a big dot when you click (`mouse.buttonDown`, collect positions in a list).
3. Make the line thicker when you move faster: `it.dragDisplacement.length`.
4. Show the brush as a small circle outline at the mouse. Why does it leave a trail? (Part 6
   fixes that.)

---

## Part 3 — Strokes become curves

**Program:** `Advanced03.kt` · **New:** `hobbyCurve`, `rectified()`, `position(t)`, `normal(t)`

![Part 3](slides/slide03.png)

Instead of painting pixels right away, we can **remember** every stroke as a curve. Then we can
draw it in any style, change the style later, and even animate it.

1. While you drag, collect points, but only when the mouse moved at least 10 pixels.
2. When you let go, `hobbyCurve(points)` (from **orx-shapes**) makes a smooth curve through them.
3. Every frame, draw all strokes again as rows of dots.

**Evenly spaced: `rectified()`.** A contour is made of pieces of different lengths, so
`contour.position(0.5)` is not always halfway along the line. A *rectified* contour fixes that:

```kotlin
val r = stroke.rectified()
r.position(0.5)    // exactly halfway along the curve
r.normal(0.5)      // the direction pointing sideways, away from the curve
```

`t` goes from `0.0` (the start) to `1.0` (the end). To put a dot every 12 pixels:

```kotlin
val count = (stroke.length / 12.0).toInt()
for (i in 0 until count) {
    val t = i / count.toDouble()
    drawer.circle(r.position(t), 4.0)
}
```

There's no `NoClear` here. The canvas is cleared every frame and all strokes are drawn again.

### Exercise 3

1. Draw short lines instead of dots, from `r.position(t)` to `r.position(t) + r.normal(t) * 20.0`.
2. Make the dots smaller towards the end of each stroke (use `t`).
3. Make the dots flow: `val t = (i / count.toDouble() + seconds * 0.1) % 1.0`.
4. Key `c` removes all strokes, and `z` removes the last one (`strokes.removeLast()`).

---

## Part 4 — The webcam

**Program:** `Advanced04.kt` · **New:** `loadVideoDevice`, `drawer.imageFit`

![Part 4](slides/slide04.png)

A webcam is a video that never ends. OPENRNDR plays it with a video player:

```kotlin
val webcam = loadVideoDevice()      // open the webcam
webcam.play()

extend {
    webcam.draw(drawer, blind = true)   // fetch the newest picture, don't draw it yet
    val camera = webcam.colorBuffer     // the picture: an image, like in drawing part 9
    if (camera != null) {
        drawer.imageFit(camera, Rectangle(0.0, 0.0, 720.0, 480.0))
    }
}
```

In the very first frames there may not be a picture yet. Then `colorBuffer` is `null`
(nothing), which is why we check `if (camera != null)`.

**`drawer.imageFit`** (from **orx-image-fit**) draws an image into a rectangle, **without**
squashing it. You choose how with `fitMethod`:

| `FitMethod` | what it does                                         |
|-------------|------------------------------------------------------|
| `Cover`     | fill the whole rectangle and cut off what doesn't fit (the default) |
| `Contain`   | show the whole image, with empty space if needed     |
| `Fill`      | stretch it anyway                                    |

The program shows all three next to each other.

> **No picture?** On a Mac the computer asks for permission to use the camera the first time.
> Allow it (*System Settings → Privacy & Security → Camera*, for IntelliJ IDEA) and run the
> program again. No webcam at all? Use `loadImage(...)` instead and the rest works the same.

### Exercise 4

1. Make the big picture fill the whole window.
2. Make a 4 × 4 grid of small webcam pictures.
3. Mirror the webcam: inside `drawer.isolated { }`, use `drawer.translate(720.0, 0.0)` and
   `drawer.scale(-1.0, 1.0)`.
4. `horizontalPosition` and `verticalPosition` (-1.0 to 1.0) choose which part stays when the
   picture is cut off. Try `verticalPosition = -1.0`.

---

## Part 5 — Painting with the webcam

**Program:** `Advanced05.kt` · **New:** `imageFit` along a curve, `Rectangle.fromCenter`

![Part 5](slides/slide05.png)

Now we put parts 3 and 4 together. Draw strokes with the mouse, and along every stroke we place
small **webcam pictures** instead of dots. Everything is drawn again each frame, so the pictures
stay **live**: your painting moves with whatever is in front of the camera.

```kotlin
val size = 80.0 - t * 60.0                                 // from 80 down to 20 pixels
val box = Rectangle.fromCenter(r.position(t), size, size)  // middle at the point on the curve
drawer.imageFit(camera, box)
```

`Rectangle.fromCenter(point, width, height)` makes a rectangle with its middle at `point`.
`imageFit` makes sure the camera picture isn't squashed into the little squares.

### Exercise 5

1. Put a pink outline around every picture.
2. Try narrow, tall pictures: `Rectangle.fromCenter(r.position(t), 20.0, 80.0)`.
3. Make a frozen version with `NoClear` (part 1). Collect the drag positions in a list, and in
   `extend` draw one webcam picture at each new position. Each picture keeps the moment you
   painted it.
4. Let the pictures breathe: `size * (1.0 + 0.3 * sin(seconds * 3.0))`.

---

## Part 6 — Layers

**Program:** `Advanced06.kt` · **New:** `compose { }`, `layer { }`, `draw { }`, `clearColor`,
`enabled`

![Part 6](slides/slide06.png)

Think of sheets of see-through plastic lying on top of each other. You draw on each sheet
separately, can take one away, or treat one in a special way. Those sheets are **layers**, and
**orx-compositor** manages them:

```kotlin
val composite = compose {
    draw { ... }          // the bottom: drawn first
    layer {               // a sheet on top of it
        draw { ... }
    }
    layer { ... }         // and another one on top of that
}

extend {
    composite.draw(drawer)   // draw all layers, from the bottom up
}
```

Normally every layer is cleared every frame. But `clearColor = null` means "never clear this
layer", like `NoClear` for just one sheet. That's how this painter works:

| layer | what's on it                | cleared?                |
|-------|-----------------------------|-------------------------|
| 1     | dark paper with a grid      | yes                     |
| 2     | the paint                   | **no**, so the paint stays |
| 3     | the brush outline           | yes, so no trail        |

That solves exercise 2.4! `layer { }` gives the layer back, so you can keep it under a name and
switch it off with `enabled = false`. Press `1`, `2` or `3`.

### Exercise 6

1. Switch layers 2 and 3 off and on. What happens to the paint while layer 2 is off?
2. Add a layer with your name in big letters. Can you paint over it? Move it in `compose { }`
   so you can.
3. Add a second paint layer in another colour, and a key to choose which one you paint on.
4. Key `c` clears the paint. (A `var`, plus `drawer.clear(ColorRGBa.TRANSPARENT)` in the paint
   layer's `draw { }`.)

---

## Part 7 — Masks

**Program:** `Advanced07.kt` · **New:** `mask { }`, `invertMask`

![Part 7](slides/slide07.png)

A mask is a **stencil**. You draw a shape in the mask, and only the part of the layer **inside**
that shape stays visible:

```kotlin
layer {
    draw { drawer.imageFit(image, drawer.bounds) }    // what is on the layer
    mask { drawer.circle(mouse.position, 120.0) }     // only here it is visible
}
```

The mask doesn't need colours: everything you draw in it counts as "visible".
`invertMask = true` turns the stencil around, so the layer is visible everywhere **except**
inside the shape.

The program:

- **bottom:** the photo, made very dark with a see-through black rectangle
- **layer 2:** the same photo, bright, masked by a circle at the mouse, which works as a
  flashlight
- **layer 3:** moving pink stripes, masked by the word "MASK"

### Exercise 7

1. Make the flashlight bigger. Then make it a rectangle.
2. Add `invertMask = true` inside the flashlight layer. What happens?
3. Mask the photo with your name.
4. Give the flashlight a trail: in the mask, draw circles at the last 20 mouse positions (a
   list).

---

## Part 8 — Blend modes and post processing

**Program:** `Advanced08.kt` · **New:** `blend(...)`, `post(...)`, orx-fx

![Part 8](slides/slide08.png)

**Blend modes** decide how a layer mixes with what's underneath. Normally a layer just covers
what's below. With a blend mode it mixes:

| blend mode   | what it does                        |
|--------------|-------------------------------------|
| `Add()`      | adds colours, like light: only brighter |
| `Multiply()` | like layers of ink: only darker     |
| `Screen()`   | brighter, but softer than Add       |
| `Overlay()`  | more contrast                       |
| `Darken()` / `Lighten()` | keeps the darkest / lightest colour |

```kotlin
layer {
    blend(Add())
    draw { ... }
}
```

In the program, a red, a green and a blue circle are added like three coloured lamps. Where all
three overlap you get **white**. The blue lamp follows the mouse.

**Post processing** is an effect on a whole layer after it's drawn ("post" means after):

```kotlin
post(ApproximateGaussianBlur()) {
    window = 25
    sigma = 0.1 + mouse.position.y / 720.0 * 10.0
}
```

The settings in `{ }` run every frame, so they can use the mouse or `seconds`. Here the text
gets blurrier as the mouse goes down. `post(...)` directly in `compose { }` works on
**everything**: the program adds a `ChromaticAberration`, which shifts the colours apart.

All blend modes and effects come from **orx-fx**: `org.openrndr.extra.fx.blend`, `.blur`,
`.color`, `.distort`, `.edges` and more. Have a look at what's there!

### Exercise 8

1. Replace `Add()` with `Multiply()`, `Screen()`, `Darken()` or `Lighten()`. Try `Multiply()`
   on a white background.
2. Make GLOW really glow: the text twice, blurred underneath and sharp on top (two layers).
3. Try other effects on everything: `Posterize()` (`levels = 4`), `HorizontalWave()`
   (`amplitude = 0.05`), `EdgesWork()` (`radius = 3`).
4. Add the webcam (part 4) as a layer with `blend(Multiply())` under the lamps.

---

## Part 9 — Typographic art

**Program:** `Advanced09.kt` · **New:** `loadFace`, glyphs, letter outlines

![Part 9](slides/slide09.png)

With `loadFont` (drawing part 8) you can only **write** text. With `loadFace` you get the
letters as **shapes**: outlines you can take apart and use like any contour.

```kotlin
val face = loadFace("data/fonts/default.otf").withSize(220.0)   // letters 220 pixels big
val glyph = face.glyphForCharacter('A')                         // the letter A, a "glyph"
```

| you write                 | you get                                          |
|---------------------------|--------------------------------------------------|
| `glyph.shape()`           | the letter as a shape                            |
| `glyph.shape().contours`  | its outlines. An **O** has two: the outside and the hole |
| `glyph.advanceWidth()`    | how far to move right for the next letter        |

The shape stands on its **baseline** at `y = 0` and goes **up** (negative y). So we use
`drawer.isolated` and `drawer.translate(x, 440.0)` to put each letter on a line at y = 440,
and move `x` along by `advanceWidth()` after each letter. Adding up all the widths first lets
us centre the word.

Every outline is a contour, so we can **rectify** it (part 3) and put things along it. Here, dots
flow around every letter, with little hairs sticking out along the normal. The hair length
comes from the mouse. `% 1.0` keeps `t` between `0.0` and `1.0`, so the dots go round and round.

### Exercise 9

1. Write your own name. Use `withSize(...)` to make it fit.
2. Make every second dot pink: `if (i % 2 == 0) { ... } else { ... }`.
3. Draw only part of every outline: `r.sub(0.0, mouse.position.x / 720.0)`.
4. Use the letters as a mask (part 7), with `drawer.shape(glyph.shape())` in `mask { }`, over a
   webcam layer.

---

## Part 10 — Making your own tool

**Program:** `Advanced10.kt` · **New:** orx-gui: `WindowedGUI`, `@DoubleParameter`, `@ColorParameter`,
`@OptionParameter`, `@BooleanParameter`, `@ActionParameter`

![Part 10](slides/slide10.png)

A **tool** is a program that other people can use without changing the code. **orx-gui** turns
your settings into a panel with sliders, colour pickers and buttons. `WindowedGUI` puts that
panel in a **window of its own**, next to your program, so it never covers your drawing:

```kotlin
val brush = object {
    @DoubleParameter("size", 2.0, 100.0)  var size = 20.0              // a slider
    @ColorParameter("color")              var color = ColorRGBa.PINK   // a colour picker
    @OptionParameter("shape")             var shape = BrushShape.Circle  // a drop-down
    @BooleanParameter("mirror")           var mirror = false           // a checkbox
    @ActionParameter("clear")             fun clear() { ... }          // a button
}
val gui = WindowedGUI()
gui.add(brush, "Brush")
extend(gui)
```

In your code you simply use `brush.size`, `brush.color` and so on, and they follow the panel.
`order = ...` sets the order of the controls.

The choices for a drop-down list come from an **enum class**, a short list of names:
`enum class BrushShape { Circle, Square, Line }`. `when (brush.shape) { ... }` then does
something different for each one.

This is the layered painter from part 6, now with a control window. Because the controls live
in their own window, dragging a slider never paints on the canvas, and a picture saved with
**space** (in the `screenshots` folder) never shows the controls.

### Exercise 10: final project, make your own tool

1. Add a "jitter" slider that moves every stamp a random distance:
   `point + Vector2(Random.nextDouble(-j, j), Random.nextDouble(-j, j))`.
2. Add a webcam brush (part 5) as another option in `BrushShape`.
3. **Final:** build your own tool for making images. Some ideas:
   - a **poster maker**: a word (`@TextParameter`), letter outlines with hairs (part 9),
     sliders for size and hair length, blend modes or blur (part 8)
   - a **webcam photo booth**: masks (part 7) and effects (part 8) behind checkboxes
   - a **pattern painter**: strokes become curves (part 3) with stamps along them

   Then give it to someone who has never seen your code, let them make three pictures, and
   save them with space. What did they do that you didn't expect?

---

## All the new ingredients

```kotlin
// painting
extend(NoClear()) { backdrop = { drawer.clear(ColorRGBa.BLACK) } }
keyboard.keyDown.listen { if (it.name == "c") ... }
mouse.dragged.listen { it.position; it.dragDisplacement }   // collect...
extend { ... }                                              // ...and draw

// curves
val curve = hobbyCurve(points)
val r = curve.rectified()
r.position(t); r.normal(t)                                  // t from 0.0 to 1.0

// webcam and images
val webcam = loadVideoDevice(); webcam.play()
webcam.draw(drawer, blind = true); val camera = webcam.colorBuffer
drawer.imageFit(camera, Rectangle.fromCenter(p, w, h), fitMethod = FitMethod.Cover)

// layers
val composite = compose {
    draw { }
    layer {
        clearColor = null                   // never cleared
        blend(Add())                        // how it mixes
        draw { }
        mask { }                            // a stencil
        post(ApproximateGaussianBlur()) { } // an effect
    }
}
composite.draw(drawer)

// letters as shapes
val face = loadFace("data/fonts/default.otf").withSize(220.0)
face.glyphForCharacter('A').shape().contours

// tools
val settings = object { @DoubleParameter("size", 0.0, 100.0) var size = 20.0 }
val gui = WindowedGUI(); gui.add(settings, "Settings"); extend(gui)
```

## Want more?

- The OPENRNDR guide: https://guide.openrndr.org, especially the ORX chapters on compositor,
  fx, gui and shapes.
- Every orx module has demos in the openrndr/orx repository on GitHub.
