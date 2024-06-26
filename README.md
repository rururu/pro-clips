# pro-clips

### Protege based CLIPS Integrated Development Environment

[![Watch the video](MultiWindowedIDE.png)](https://www.youtube.com/watch?v=RQq_sqyxsEw)
Click the screenshot to see a video!

IDE based on:

1. Protege 3.5 ontology editor [https://protegewiki.stanford.edu/wiki/Protege_Desktop_Old_Versions](https://protegewiki.stanford.edu/wiki/Protege_Desktop_Old_Versions)

2. CLIPS tool for Building Expert Systems [https://www.clipsrules.net/](https://www.clipsrules.net/)

## Prerequisites

Java version 17 or above.

## Start

```clj
$ cd <installation directory>
$ ./run.sh ;; Linux, Mac OS
$ run.bat  ;; Widows
```

## Short video lessons 

1. [Hello World](https://www.youtube.com/watch?v=khDvm-8RtBU)

2. [Monkees and Bananas, Cannibals and Missionaries](https://www.youtube.com/watch?v=dRMppDBezYQ)

3. [Automotive Expert System](https://www.youtube.com/watch?v=RQq_sqyxsEw)

4. [New Project](https://www.youtube.com/watch?v=MVQQ0cJlbZU)

## Description 

Used Protege 3.5 version is a little bit old. It is *Frame-based*. Recent Protege versions are *OWL-based*. It is superfluous for our purposes.

This IDE has 2 important differences from traditional ones.

1. With Protege, it allows you to first construct the application domain **visually** and manipulate it using Protege's GUI. Then add functionality to the application using Clojure programs. And finally, add rule-based functionality using CLIPS.

2. The programming environment in Protege GUI is multi-window. **Each function, rule, template and so on has a separate window**. This allows quite a lot of objects of different types to be displayed on the screen at the same time and creates additional convenience when constructing and debugging.

## Start original CLIPS IDE

```clj
$ cd <installation directory>
$ ./run_IDE.sh ;; Linux, Mac OS
$ run_IDE.bat  ;; Widows
```

## License

Copyright © 2024 Ruslan Sorokin

Distributed under the Mozilla Public License Version 2.0 as Protege-3.5.
