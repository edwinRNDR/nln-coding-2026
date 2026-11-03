# Drawing with a turtle

*Your first programs with OPENRNDR, in 10 small steps*

Imagine a small turtle sitting on a big sheet of paper. It holds a pen. You can tell it to
walk forward and to turn, and wherever it walks it leaves a line. That's all there is to it.
Telling the turtle what to do, step by step, is **programming**.

This course adds **one new ingredient** in every part. Each part has:

- a short explanation (this document)
- a program you can run: `src/main/kotlin/Turtle01.kt` up to `Turtle10.kt`
- an **exercise** for you to make (it is also at the bottom of each program)
- a slide: `slides/slide01.png` up to `slide10.png` (plus a text version, `.txt`)

You don't need to be good at maths. Some parts use a few numbers, and we explain every one.

![Turtle graphics](slides/slide00.png)

---

## Before we start: how to run a program

1. Open the project in **IntelliJ IDEA**.
2. On the left, open the folder `src/main/kotlin` and double-click `Turtle01.kt`.
3. Next to the line `fun main() {` you see a small green ▶ arrow. Click it and choose **Run**.
4. A window opens with the drawing. Close the window to stop the program.

Change something in the code, run it again and see what changed. That's how you learn this:
change, run, look, and change again. You can't break anything. If it stops working, undo
your change with **Ctrl+Z** (**Cmd+Z** on a Mac).

**Red text** means the program has a mistake in it. Read the message: it usually tells you on
which line. Common mistakes:

- a missing `{` or `}`. Every `{` needs a `}`.
- writing `200` where the turtle wants `200.0`. Turtle numbers always get `.0`.
- a typo: `foward` instead of `forward`. Capitals matter too: `Rotate` is not `rotate`.

---

## Where turtles come from

Turtle drawing is more than 50 years old.

- **Around 1950**, the English scientist **Grey Walter** built two small robots that crawled
  around and looked for light. They had round shells, so people called them **tortoises**.
- **In 1967**, **Seymour Papert**, **Cynthia Solomon** and **Wally Feurzeig** made **Logo**, a
  programming language for children. Papert believed children learn best by *making things*.
- **Around 1970** Logo got a real turtle: a robot on wheels with a pen. Children typed
  `FORWARD 100` and `RIGHT 90`, and the robot drew on a large sheet of paper on the floor.
  Soon the turtle also appeared on computer screens as a little triangle.
- **In the 1980s** almost every school computer had Logo. For millions of people, their very
  first program was a turtle drawing a square.
- **Today** you can find turtles in **Scratch** (the pen blocks), in **Python**, and in
  **OPENRNDR**, which is what we use.

Why a turtle? Because you can **play turtle** yourself. Not sure what your program does? Stand
up and walk it: "ten steps forward, turn a quarter to the left…". You *are* the turtle. Papert
thought this was the best thing about it: you use what you already know about moving your own
body.

---

## Part 1 — Your first program

**Program:** `Turtle01.kt` · **New:** the structure of a program, `forward`

![Part 1](slides/slide01.png)

### The structure of an OPENRNDR program

Every program in this course has the same shape. It is built like boxes inside boxes. Each
box starts with `{` and ends with `}`:

```kotlin
fun main() {                    // 1. the program starts here
    application {               // 2. the application: it opens a window
        configure {             // 3. settings for the window
            width = 720
            height = 720
        }
        program {               // 4. your program
            extend {            // 5. this part runs again and again,
                                //    about 60 times every second
            }
        }
    }
}
```

1. **`fun main()`**: the computer always starts here.
2. **`application`**: the OPENRNDR app that opens a window.
3. **`configure`**: settings, here the size of the window: 720 by 720 **pixels**. A pixel
   is one of the tiny dots your screen is made of.
4. **`program`**: your program lives in here.
5. **`extend`**: the code in here runs **over and over**, about 60 times every second. Every
   time it makes a new picture. You won't notice yet, but you will in part 9.

Lines that start with `//` are **comments**: notes for people, which the computer skips.

### The turtle

Inside `extend` there are three steps:

```kotlin
drawer.clear(ColorRGBa.BLACK)               // paint the window black

val drawing = turtle(drawer.bounds.center) {  // a turtle in the middle of the window
    forward(200.0)                          // walk forward 200 pixels
}

drawer.stroke = ColorRGBa.PINK              // lines will be pink
drawer.contours(drawing)                    // show the turtle's lines
```

- **`drawer`** is the part of OPENRNDR that draws on the screen.
- **`drawer.clear(...)`** paints the whole window in one colour.
- **`turtle(drawer.bounds.center) { ... }`** puts a turtle in the middle of the window.
  Everything between `{` and `}` is what the turtle does.
- **`forward(200.0)`** means walk forward 200 pixels. The turtle starts **looking to the
  right**, with its **pen down**, so it draws a line.
- **`val drawing = ...`**: the turtle's lines are kept under the name `drawing`. A name like
  this is called a *variable*.
- **`drawer.stroke`** is the colour of the lines.
- **`drawer.contours(drawing)`** draws the turtle's lines on the screen. (A *contour* is a
  line or a shape.)

### Exercise 1

1. Make the line longer. How long can it get before it leaves the window?
2. Change `PINK` into another colour: `YELLOW`, `WHITE`, `RED`, `GREEN` or `BLUE`.
3. Change the background colour `BLACK` into another colour.

---

## Part 2 — Turning

**Program:** `Turtle02.kt` · **New:** `rotate`

![Part 2](slides/slide02.png)

`rotate(90.0)` makes the turtle turn on the spot. The number is how far it turns, in
**degrees**:

| you write       | the turtle…                    |
|-----------------|--------------------------------|
| `rotate(90.0)`  | turns a quarter to the **left** |
| `rotate(-90.0)` | turns a quarter to the **right** (the minus sign) |
| `rotate(180.0)` | turns around                   |
| `rotate(360.0)` | turns all the way round, and looks the same way again |

Walk, turn, walk, turn, walk, turn, walk… and you have a square:

```kotlin
forward(200.0)
rotate(90.0)
forward(200.0)
rotate(90.0)
forward(200.0)
rotate(90.0)
forward(200.0)
```

The square ends up *above* the middle of the window. Play turtle to see why: you start
looking right, walk, and then turn *left*, which is up.

### Exercise 2

1. Change every `rotate(90.0)` into `rotate(-90.0)`. Where does the square go now?
2. Draw a rectangle: wider than it is high.
3. Draw a staircase with 4 steps.

---

## Part 3 — Colours, lines and fills

**Program:** `Turtle03.kt` · **New:** `stroke`, `strokeWeight`, `fill`, `close`

![Part 3](slides/slide03.png)

Before the `drawer` draws, you tell it how things should look:

| setting               | what it means                              | example                       |
|-----------------------|--------------------------------------------|-------------------------------|
| `drawer.stroke`       | the colour of the **outline** (the line around a shape) | `ColorRGBa.PINK`   |
| `drawer.strokeWeight` | how **thick** the outline is, in pixels    | `10.0`                        |
| `drawer.fill`         | the colour **inside** the shape            | `rgb(1.0, 0.5, 0.0)`          |

You can switch something off with `null`, which means "nothing". `drawer.fill = null` gives
an empty shape, and `drawer.stroke = null` gives a shape without an outline.

**A fill only works on a closed shape**: one where the end connects back to the beginning.
`close()` does that for you. The turtle draws three sides, and `close()` adds the last one.

**Your own colours.** `rgb(red, green, blue)` mixes light. Each number goes from `0.0` (none)
to `1.0` (full):

| mix                    | colour  |
|------------------------|---------|
| `rgb(1.0, 0.0, 0.0)`   | red     |
| `rgb(1.0, 1.0, 0.0)`   | yellow  |
| `rgb(1.0, 0.5, 0.0)`   | orange  |
| `rgb(0.5, 0.5, 0.5)`   | grey    |
| `rgb(1.0, 1.0, 1.0)`   | white   |

### Exercise 3

1. Make the outline very thin, then very thick.
2. Remove the fill with `drawer.fill = null`. Then remove the outline with `drawer.stroke = null`.
3. Mix your favourite colour with `rgb(...)` and use it as the fill.
4. Remove `close()`. What happens?

---

## Part 4 — Doing things again and again

**Program:** `Turtle04.kt` · **New:** the `for` loop

![Part 4](slides/slide04.png)

In part 2 we wrote the same two lines four times. Computers are good at repeating things, so
we let the computer do that:

```kotlin
for (i in 0 until 4) {
    forward(200.0)
    rotate(90.0)
}
```

This is a **loop**. The code between `{` and `}` runs **4 times**.

`i` is a counter. The first time it is `0`, then `1`, then `2`, then `3`. `0 until 4` means
"start at 0 and stop *before* 4", which makes 4 times. To repeat something 10 times you write
`for (i in 0 until 10) { ... }`.

### A full turn is 360 degrees

To get back to its start, the turtle has to turn all the way round once, and that's 360
degrees. A square has 4 corners, and 4 × 90 = 360. The rule for any shape like this:

> **turn = 360 ÷ number of corners**

| shape    | corners | turn each time |
|----------|---------|----------------|
| triangle | 3       | 120            |
| square   | 4       | 90             |
| hexagon  | 6       | 60             |
| "circle" | 36      | 10             |

### Exercise 4

1. Draw a triangle: 3 times, turn `120.0`.
2. Draw a hexagon (6 corners).
3. Let the loop run 36 times with `forward(20.0)` and `rotate(10.0)`. What shape is that?
4. Draw a star: 5 times `forward(300.0)` and `rotate(144.0)`.

---

## Part 5 — Counting along

**Program:** `Turtle05.kt` · **New:** using `i` inside the loop

![Part 5](slides/slide05.png)

You can use the counter `i` in your own code:

```kotlin
for (i in 0 until 80) {
    forward(i * 4.0)
    rotate(90.0)
}
```

`*` means "times". The turtle walks `i * 4.0` pixels, so 0, then 4, then 8, then 12… Every
step is a bit longer than the last one, the turtle never gets back to its start, and it walks
a **spiral**.

This is where it gets fun: change one number a tiny bit and you get a different picture.
Try `rotate(91.0)` instead of `rotate(90.0)`!

### Exercise 5

1. Try `rotate(91.0)`, then `120.0`, `121.0` and `144.0`.
2. Change `i * 4.0` into `i * 2.0` and let the loop run 200 times.
3. Find the spiral you like best and write down its numbers.

---

## Part 6 — Lifting the pen

**Program:** `Turtle06.kt` · **New:** `penUp`, `penDown`

![Part 6](slides/slide06.png)

The turtle holds a pen, and normally the pen touches the paper.

- **`penUp()`**: lift the pen. The turtle walks, but doesn't draw.
- **`penDown()`**: put the pen down again. The turtle draws again.

The program uses this twice:

1. to **walk without drawing**: the turtle goes to the left side of the window first
2. to make a **dashed line**: pen down, walk, pen up, walk, again and again in a loop

### Exercise 6

1. Make the dashes longer and the gaps shorter.
2. Draw a dashed square.
3. Draw two squares next to each other: draw one, lift the pen, walk to the side, put the pen
   down, and draw the second one.

---

## Part 7 — Where does the turtle start?

**Program:** `Turtle07.kt` · **New:** `Vector2(x, y)`, a place in the window

![Part 7](slides/slide07.png)

Think of the window as a sheet of squared paper, 720 by 720 pixels. You point at a spot with
two numbers:

- **x**: how far from the **left** side
- **y**: how far from the **top**. Careful: a bigger y is **lower** in the window!

```
(0, 0) ──────── x ────────▶ (720, 0)
  │
  │          (360, 360)
  y              ●  the middle
  │
  ▼
(0, 720)                    (720, 720)
```

In code you write a place as `Vector2(360.0, 360.0)`. `drawer.bounds.center`, which we used
until now, is just the middle of the window.

The program uses a loop to make **five turtles**, each starting a bit more to the right:

```kotlin
for (i in 0 until 5) {
    val start = Vector2(60.0 + i * 130.0, 400.0)
    val drawing = turtle(start) { ... }
    drawer.contours(drawing)
}
```

`60.0 + i * 130.0` gives 60, 190, 320, 450 and 580.

There's a second loop inside the turtle, which draws the square. We called its counter `side`
instead of `i`, because two loops inside each other each need their own name.

### Exercise 7

1. Put a square in each corner of the window.
2. Make the squares go down instead of to the right.
3. Make each square a bit bigger than the one before. (Hint: use `i` inside `forward`.)

---

## Part 8 — Teach the turtle a new word

**Program:** `Turtle08.kt` · **New:** making your own command

![Part 8](slides/slide08.png)

The turtle knows `forward` and `rotate`. We can teach it more words. Here we teach it `square`:

```kotlin
fun Turtle.square(size: Double) {
    for (i in 0 until 4) {
        forward(size)
        rotate(90.0)
    }
}
```

- **`fun`** means: here comes a new command (a *function*).
- **`Turtle.`** means: it is a command for the turtle.
- **`square`** is the name of the new word.
- **`size: Double`**: when you use the word you give it a number, and inside the command that
  number is called `size`. (`Double` is Kotlin's word for a number with a decimal point.)

Now you can write `square(150.0)` inside `turtle { }`, just like `forward(150.0)`. Words you
make yourself can be used to make bigger things. Here 36 squares, each turned a little,
make a flower.

> **Tip:** two files can't have a command with the same name. If you get the error
> *"Conflicting overloads"*, give your command a different name, for example `square2`.

### Exercise 8

1. Make a flower with 12 squares. How far should the turtle turn between the squares?
2. Teach the turtle the word `triangle` and make a flower of triangles.
3. Draw your flower three times, each in its own place (see part 7).

---

## Part 9 — Make it move

**Program:** `Turtle09.kt` · **New:** `seconds` and `mouse.position`

![Part 9](slides/slide09.png)

Remember from part 1: the code in `extend { }` runs about 60 times per second. Each time we
paint the window black and the turtle makes a brand new drawing. If a number changes a little
each time, the drawing changes a little each time, and it **moves**.

| you write          | what it is                                          |
|--------------------|-----------------------------------------------------|
| `seconds`          | how long the program has been running, in seconds   |
| `mouse.position.x` | how far the mouse is from the **left** of the window |
| `mouse.position.y` | how far the mouse is from the **top** of the window  |

In the program the turtle first turns `seconds * 45.0` degrees: 0 at the start, 45 after one
second, 90 after two seconds… so the square slowly turns. The size of the square is
`mouse.position.x`, so move your mouse left and right!

### Exercise 9

1. Make the square turn faster. Then make it turn the other way.
2. Use `mouse.position.y` for the thickness of the line (`drawer.strokeWeight`).
3. Take the spiral from part 5 and use `rotate(90.0 + seconds)` in the loop.

---

## Part 10 — Remember this spot

**Program:** `Turtle10.kt` · **New:** `push`, `pop`, saving your picture

![Part 10](slides/slide10.png)

- **`push()`**: the turtle remembers where it stands and which way it's looking.
- **`pop()`**: the turtle jumps back to that spot, without drawing.

This is handy when lots of lines start from the same point, like the rays of a sun: remember
the middle, walk out, jump back, turn a bit, and go again.

```kotlin
for (i in 0 until 24) {
    push()                    // remember the middle
    forward(100.0 + i * 8.0)  // walk out, every ray a bit longer
    pop()                     // jump back to the middle
    rotate(15.0)              // turn a bit (24 x 15 = 360)
}
```

**Saving your drawing.** The line `extend(Screenshots())` adds a camera to your program. Press
the **space bar** and a picture of the window is saved in the `screenshots` folder of the
project.

### Exercise 10: final project

1. Give every ray a small square at its end: walk out, draw a square, `pop()` back.
2. Make the sun turn slowly with `seconds` (see part 9).
3. **Final project:** make your own drawing that uses **at least four** ingredients from this
   course. Save it with the space bar and share it with the group!

---

## All the ingredients

```kotlin
turtle(drawer.bounds.center) {   // a turtle in the middle of the window
    forward(100.0)               // walk 100 pixels and draw
    rotate(90.0)                 // turn left 90 degrees (-90.0 turns right)
    close()                      // connect the end to the start
    penUp()                      // stop drawing
    penDown()                    // start drawing again
    push()                       // remember this spot
    pop()                        // jump back to the remembered spot
}

for (i in 0 until 10) { }        // do something 10 times, i counts 0, 1, 2 … 9

Vector2(100.0, 200.0)            // a place: 100 from the left, 200 from the top
seconds                          // time since the start
mouse.position.x                 // where the mouse is

drawer.clear(ColorRGBa.BLACK)    // background colour
drawer.stroke = ColorRGBa.PINK   // outline colour (null = no outline)
drawer.strokeWeight = 4.0        // outline thickness
drawer.fill = rgb(1.0, 0.5, 0.0) // inside colour (null = empty)
drawer.contours(drawing)         // show the drawing
```

## Want more?

- Seymour Papert, *Mindstorms: Children, Computers, and Powerful Ideas* (1980), the book
  that made the turtle famous
- The OPENRNDR guide: https://guide.openrndr.org
