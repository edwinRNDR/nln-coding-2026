# Drawing with OPENRNDR

*Part 2 of the course: after the turtle*

In the turtle course you drew by **walking**: forward, turn, forward. In this course you tell
the `drawer` **where** things go: a circle *here*, a rectangle *there*, a photo in the corner,
a line of text underneath. Along the way you learn the building blocks of every program:
names, loops, choices and lists.

As before, every part adds a small number of new ingredients. Each part has:

- a short explanation (this document)
- a program you can run: `src/main/kotlin/drawing/Drawing01.kt` up to `Drawing10.kt`
- an **exercise** for you to make (also at the bottom of each program)
- a slide: `slides/slide01.png` up to `slide10.png` (plus a text version, `.txt`)

The course ends with a **portrait** that you draw yourself.

Running a program works just like in the turtle course: open the file and click the green ▶
next to `fun main()`. Every window is 720 × 720 pixels, and every program has the same
structure you already know: `application` → `configure` → `program` → `extend`.

![Drawing with OPENRNDR](slides/slide00.png)

---

## Part 1 — Shapes and positions

**Program:** `Drawing01.kt` · **New:** `drawer.circle`, `drawer.rectangle`, `drawer.lineSegment`

![Part 1](slides/slide01.png)

The drawer can draw shapes directly, if you say **where** they go. Remember the window from
turtle part 7: it is squared paper, and every spot has two numbers.

- **x**: how far from the **left**
- **y**: how far from the **top** (a bigger y is lower in the window)

| you write                                  | it draws                                     |
|--------------------------------------------|----------------------------------------------|
| `drawer.circle(x, y, radius)`              | a circle. `(x, y)` is the **middle**. The radius is the distance from the middle to the edge. |
| `drawer.rectangle(x, y, width, height)`    | a rectangle. `(x, y)` is the **top left corner**. |
| `drawer.lineSegment(x1, y1, x2, y2)`       | a straight line from one point to another    |

The colours work as in the turtle course: `drawer.fill` is the colour inside, `drawer.stroke`
is the outline and `drawer.strokeWeight` is its thickness. **Set the colour first, then
draw.** A shape uses the colours that are set at the moment you draw it. That's why the program
changes `drawer.fill` between the sun, the house and the door.

### Exercise 1

1. Move the sun to the top left corner.
2. Give the house two windows.
3. Draw a snowman: three circles on top of each other, each one smaller than the one below.

---

## Part 2 — Giving things a name

**Program:** `Drawing02.kt` · **New:** `val` and `var`

![Part 2](slides/slide02.png)

You can give a value a **name**, and then use the name instead of the value. You've already
seen this: `val drawing = turtle(...)`.

```kotlin
val size = 100.0     // val: a name for something that STAYS the same
var x = 0.0          // var: a name for something that can CHANGE
```

Think of a box with a label on it. `val` is a box you can't open again. `var` is a box where
you can swap what's inside.

Why use names?

- your code is easier to read: `size` says more than `100.0`
- change the value in **one** place and everything that uses it follows

A `var` gets a new value with `=`:

```kotlin
x = x + 2.0     // x becomes what x was, plus 2
```

**Where you make the var matters.** In the program, `var x = 0.0` is made *outside*
`extend { }`, so it is made once, when the program starts. The code in `extend` runs 60 times
per second and adds 2 to `x` each time, so the circles move to the right.

OPENRNDR gives you a few names for free: `width` and `height` (the size of the window),
`seconds`, and `mouse.position`.

### Exercise 2

1. Change `size` to `50.0`. How many things change?
2. Make the circles move faster. Then make them move from right to left. (Start `x` at
   `width.toDouble()` and subtract instead of add.)
3. Add a `var y` so the circles also move down.
4. Move `var x = 0.0` *inside* `extend { }`. What happens, and why?

---

## Part 3 — Many shapes with loops

**Program:** `Drawing03.kt` · **New:** a loop inside a loop

![Part 3](slides/slide03.png)

You know the `for` loop from the turtle course. With the counter you can place shapes next to
each other: `60.0 + column * 75.0` gives 60, 135, 210, 285…

Put a loop **inside** another loop and you get a **grid**:

```kotlin
for (row in 0 until 9) {
    for (column in 0 until 9) {
        val x = 60.0 + column * 75.0
        val y = 60.0 + row * 75.0
        val radius = 4.0 + column * 3.0
        drawer.circle(x, y, radius)
    }
}
```

For each row, the inner loop draws a whole row of circles. 9 rows × 9 columns gives 81
circles from a handful of lines. The two counters need different names, so we call them `row`
and `column`, which also says what they mean.

Because the radius uses `column`, the circles get bigger towards the right.

### Exercise 3

1. Make the circles get bigger going **down** instead of to the right.
2. Make a grid of 15 × 15 small squares (with `drawer.rectangle`).
3. Make them grow to the right **and** down: `4.0 + column * 2.0 + row * 2.0`.

---

## Part 4 — Making choices

**Program:** `Drawing04.kt` · **New:** `if`, `else`, comparing

![Part 4](slides/slide04.png)

Sometimes you only want to do something **when** something is true:

```kotlin
if (x < mouse.position.x) {
    drawer.fill = ColorRGBa.PINK      // when it is true
} else {
    drawer.fill = ColorRGBa.WHITE     // otherwise
}
```

Ways to compare two numbers:

| you write | it means                     |
|-----------|------------------------------|
| `a < b`   | a is smaller than b          |
| `a > b`   | a is bigger than b           |
| `a <= b`  | a is smaller than b, or the same |
| `a >= b`  | a is bigger than b, or the same  |
| `a == b`  | a is the same as b (**two** `=` signs; one `=` gives a new value) |

The program uses `if` twice:

- circles to the **left** of the mouse are pink, and the rest are white
- the yellow ball moves to the right. When it leaves the window (`ballX > width`) it starts
  again on the left. You can leave out the `else` when there is nothing to do otherwise.

### Exercise 4

1. Make the circles **above** the mouse pink (use `y` and `mouse.position.y`).
2. Make the circles left of the mouse **bigger** instead of another colour.
3. Give only the middle row another colour (hint: `row == 4`).
4. Make the ball bounce: use a `var speed = 4.0` and do `ballX = ballX + speed`. When the ball
   reaches the right side, set `speed = -4.0`. What do you need for the left side?

---

## Part 5 — Lists

**Program:** `Drawing05.kt` · **New:** `listOf`, `mutableListOf`, `for (... in list)`

![Part 5](slides/slide05.png)

A **list** keeps many things together, in order, under one name:

```kotlin
val colors = listOf(ColorRGBa.PINK, ColorRGBa.WHITE, ColorRGBa.YELLOW)
```

| you write     | it means                                         |
|---------------|--------------------------------------------------|
| `colors[0]`   | the **first** thing in the list (counting starts at 0!) |
| `colors[1]`   | the second thing                                 |
| `colors.size` | how many things are in the list (here: 3)        |

There are two ways to go through a list with a loop:

```kotlin
for (color in colors) { ... }           // one thing at a time

for (i in 0 until colors.size) {        // with a counter, use colors[i]
    drawer.fill = colors[i]
    ...
}
```

A `listOf` list stays the same. A **`mutableListOf`** list can change: you can `add` things to
it while the program runs. In the program, every mouse click adds the mouse position to the
list `points`:

```kotlin
val points = mutableListOf<Vector2>()     // an empty list of positions

mouse.buttonDown.listen {                 // when the mouse button is pressed...
    points.add(mouse.position)            // ...add the position to the list
}
```

`<Vector2>` tells Kotlin what kind of things go in the empty list: positions.
`mouse.buttonDown.listen { }` means "every time the mouse button goes down, do this".

### Exercise 5

1. Add two more colours to the list. Do the swatches follow?
2. Draw the circles with the **last** colour: `colors[colors.size - 1]`.
3. Draw lines between the points you clicked:
   `for (i in 1 until points.size) { drawer.lineSegment(points[i - 1], points[i]) }`
4. Give every clicked circle its own colour with `colors[i % colors.size]`. (`%` gives what is
   left over after dividing, so the colours repeat: 0, 1, 2, 3, 4, 0, 1, 2…)

---

## Part 6 — Your own shapes

**Program:** `Drawing06.kt` · **New:** `contour { }`, `moveTo`, `lineTo`, `curveTo`, `close`

![Part 6](slides/slide06.png)

Circles and rectangles are not enough to draw everything. With `contour { }` you can make any
shape, point by point, like moving a pen over paper:

| you write               | it means                                              |
|-------------------------|-------------------------------------------------------|
| `moveTo(x, y)`          | put the pen on the paper **here** (doesn't draw)      |
| `lineTo(x, y)`          | draw a **straight** line to here                      |
| `curveTo(cx, cy, x, y)` | draw a **curved** line to `(x, y)`. The curve is pulled towards the *control point* `(cx, cy)`, like a magnet. |
| `close()`               | draw a line back to the start, so the shape can be **filled** |

```kotlin
val house = contour {
    moveTo(100.0, 600.0)   // bottom left
    lineTo(100.0, 400.0)   // up
    lineTo(200.0, 300.0)   // the top of the roof
    lineTo(300.0, 600.0)   // ...
    close()
}
drawer.contour(house)
```

We make the shapes once, outside `extend`, and draw them every frame with `drawer.contour(...)`.
The program also draws the control points of the leaf in yellow, so you can see where the
curves are pulled.

Do you remember the turtle? It made contours too. That's why we drew its drawing with
`drawer.contours(...)`.

### Exercise 6

1. Give the house a chimney.
2. Move the control points of the leaf (change the numbers in **both** places). What happens?
3. Draw the first letter of your name.
4. Draw a fish: a body made of two curves and a tail made of straight lines.

---

## Part 7 — Doing more with shapes

**Program:** `Drawing07.kt` · **New:** `contour.sub(...)`, `shape.scatter(...)`

![Part 7](slides/slide07.png)

**sub: a part of a contour.** You can ask a contour for a piece of itself. The numbers go from
`0.0` (the start) to `1.0` (the end):

| you write              | you get          |
|------------------------|------------------|
| `wave.sub(0.0, 1.0)`   | the whole contour |
| `wave.sub(0.0, 0.5)`   | the first half   |
| `wave.sub(0.25, 0.75)` | the middle half  |

In the program the end comes from the mouse. `mouse.position.x / width` is `0.0` on the left
and `1.0` on the right, so moving the mouse draws more or less of the wave.

**scatter: points inside a shape.** `shape.scatter(30.0)` gives a **list** of points spread
out inside a shape, each about 30 pixels away from the others. It works on circles, rectangles
and closed contours. Because it's a list, you go through it with a `for` loop (part 5) and draw
something on every point. Scattering takes a moment, so do it once, outside `extend`.

```kotlin
val disc = Circle(360.0, 490.0, 190.0)   // a circle as a shape, not drawn yet
val points = disc.scatter(30.0)

for (point in points) {
    drawer.circle(point, 8.0)
}
```

### Exercise 7

1. Draw the wave from the **end**: `wave.sub(1.0 - amount, 1.0)`.
2. Use `seconds` instead of the mouse, so the wave draws itself. `seconds / 5.0` keeps
   growing, so use an `if` (part 4) to stop it at `1.0`.
3. Scatter with `15.0` instead of `30.0`. And with `60.0`.
4. Scatter points inside the house or the leaf from part 6, and draw its outline too.

---

## Part 8 — Text

**Program:** `Drawing08.kt` · **New:** `loadFont`, `drawer.text`, `writer { }`

![Part 8](slides/slide08.png)

To write text you need a **font**: the shapes of the letters. A font is a file. This project
has one in the folder `data/fonts`. When you load it, you also choose the size of the letters,
in pixels:

```kotlin
val font = loadFont("data/fonts/default.otf", 64.0)   // once, outside extend

drawer.fontMap = font                  // use this font from now on
drawer.fill = ColorRGBa.PINK           // text uses the FILL colour
drawer.text("HELLO OPENRNDR", 40.0, 120.0)
```

`(x, y)` is the **bottom left** of the text. If you don't load a font, OPENRNDR uses a small
built-in one.

**Strings.** Text between quotes is called a *String*. With `${ }` you can put a value inside
it: `"mouse: ${mouse.position.x}"`. `.toInt()` drops the numbers after the decimal point, which
looks tidier.

**Longer text: `writer { }`.** Give the writer a box and it moves to the next line by itself
when a line is full:

```kotlin
writer {
    this.box = Rectangle(40.0, 200.0, 400.0, 300.0)   // where the text may go
    newLine()                                         // go to the next line
    text("A long text that does not fit on one line ...")
}
```

### Exercise 8

1. Write your own name, big, in the middle of the window.
2. Make the writer's box narrower. What happens to the text?
3. Show how many seconds the program has been running: `"${seconds.toInt()} seconds"`.
4. Write the same word 10 times underneath each other with a `for` loop, each one fainter:
   `drawer.fill = ColorRGBa.PINK.opacify(1.0 - i * 0.1)`.

---

## Part 9 — Images

**Program:** `Drawing09.kt` · **New:** `loadImage`, `drawer.image`

![Part 9](slides/slide09.png)

Photos and pictures are files too. This project has one in `data/images`. Put your own
pictures (`.jpg` or `.png`) in that folder to use them.

```kotlin
val image = loadImage("data/images/cheeta.jpg")   // once, outside extend
```

| you write                                   | it draws                                 |
|---------------------------------------------|------------------------------------------|
| `drawer.image(image)`                       | the image with its top left corner at (0, 0) |
| `drawer.image(image, x, y)`                 | top left corner at `(x, y)`, at its own size |
| `drawer.image(image, x, y, width, height)`  | at `(x, y)`, made this wide and this high |

`image.width` and `image.height` tell you how big the image is, in pixels. The cheetah is
640 × 480.

**Watch out:** if the width and height you choose don't match the shape of the image, the
picture gets **stretched** or **squashed**. The bottom copy in the program follows the mouse,
so you can see it happen.

### Exercise 9

1. Put your own photo in `data/images` and draw it.
2. Make a grid of 4 × 4 small copies (a loop inside a loop, part 3).
3. Make a museum label: the image with a frame around it, and a title underneath with
   `drawer.text` (part 8).
4. Draw a copy that does **not** get squashed when you change its width:
   `val h = w * image.height / image.width`.

---

## Part 10 — Moving the paper

**Program:** `Drawing10.kt` · **New:** `translate`, `rotate`, `scale`, `drawer.isolated { }`

![Part 10](slides/slide10.png)

Until now we moved the **shapes**. You can also move the **paper** underneath them:

| you write                 | what happens to the paper                         |
|---------------------------|---------------------------------------------------|
| `drawer.translate(x, y)`  | **slide** it. Afterwards, `(0, 0)` is at `(x, y)` |
| `drawer.rotate(degrees)`  | **turn** it around `(0, 0)`                       |
| `drawer.scale(amount)`    | make everything **bigger** (`2.0` is twice as big) or **smaller** (`0.5` is half) |

This is handy: draw an eye around `(0, 0)` once, then put it anywhere, at any angle and any
size.

There is a catch. The paper **stays** moved for everything you draw after it.
`drawer.isolated { }` fixes that. Inside the `{ }` you can move the paper as much as you like,
and after the `}` it jumps back to where it was.

```kotlin
for (eyeX in listOf(-90.0, 90.0)) {
    drawer.isolated {
        drawer.translate(eyeX, -60.0)   // go to where this eye goes
        drawer.circle(0.0, 0.0, 40.0)   // draw around (0, 0)
    }                                   // back to normal for the next eye
}
```

The program draws a face this way. The whole face is turned with the mouse's left–right
position, and the eyes get bigger as the mouse goes down. The text at the top left is drawn
*after* the `isolated` block, so it isn't turned.

`extend(Screenshots())` is back from the turtle course: press the **space bar** to save a
picture in the `screenshots` folder.

### Exercise 10: final project, draw a portrait

Draw a **portrait**: of yourself, of someone in the group, or of an imaginary person or animal.
Use **at least five** of these ingredients:

| | | |
|---|---|---|
| circle / rectangle / lineSegment | `val` / `var` | `for` loops |
| `if` / `else` | lists | `contour { }` |
| `sub` / `scatter` | text / `writer` | images |
| `translate` / `rotate` / `scale` | `isolated` | the turtle! |

Some ideas:

- hair made of many lines or curves in a `for` loop
- freckles with `scatter` inside the face
- eyes that follow the mouse
- your name underneath, like a museum label
- a photo as the background, or as a piece of the face
- a turtle that draws the eyebrows

Start from `Drawing10.kt`, or from an empty program. Press **space** to save your portrait and
share it with the group!

---

## All the ingredients

```kotlin
// shapes
drawer.circle(x, y, radius)
drawer.rectangle(x, y, width, height)
drawer.lineSegment(x1, y1, x2, y2)
drawer.contour(myContour)

// names
val size = 100.0                    // stays the same
var x = 0.0                         // can change: x = x + 2.0

// loops and choices
for (i in 0 until 10) { }
if (x < width) { } else { }         // <  >  <=  >=  ==

// lists
val colors = listOf(a, b, c)        // colors[0], colors.size
val points = mutableListOf<Vector2>()   // points.add(...)
for (point in points) { }

// your own shapes
val shape = contour { moveTo(x, y); lineTo(x, y); curveTo(cx, cy, x, y); close() }
shape.sub(0.0, 0.5)                 // a part of it
Circle(x, y, r).scatter(30.0)       // points inside it

// text and images
val font = loadFont("data/fonts/default.otf", 48.0)
drawer.fontMap = font
drawer.text("hello", x, y)
writer { box = Rectangle(x, y, w, h); newLine(); text("...") }
val image = loadImage("data/images/cheeta.jpg")
drawer.image(image, x, y, width, height)

// moving the paper
drawer.isolated {
    drawer.translate(x, y)
    drawer.rotate(degrees)
    drawer.scale(amount)
}
```

## Want more?

- The OPENRNDR guide: https://guide.openrndr.org. Its *Drawing* chapter goes further than
  this course.
