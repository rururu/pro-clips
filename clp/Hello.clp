(defrule Ask-name

=>
(user-input "My name is" "What is your name?"))

(defrule Hello
(declare (salience 0))
(My name is ?name)
=>
(user-message "Hello" (format nil "Hello %s!" ?name)))

