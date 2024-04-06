(ns fly.bird
)
(defprotocol Fly
	(fly [this] [this x])
	(walk [this])
)
(defrecord Bird []
)
(defn plus-one
  ([] 1)
([x] (+ x 1))
([x & y] (+ x (apply + y) 1)))

(defmacro infix2
  [[x f y]] `(~f ~x ~y))

(defmacro infix1 [x f y]
  (list f x y))

(extend-protocol Fly
	String
	(fly [string] (str string " is flying.."))
	Double
	(fly [dbl] (str "Double " dbl " is flying.."))
)
(extend-type java.util.Date
	Fly
	(fly [string] (str string " is flying.."))
)
