;;;======================================================
;;;   Automotive Expert System
;;;
;;;     This expert system diagnoses some simple
;;;     problems with a car.
;;;
;;;     CLIPS Version 6.0 Example
;;;
;;;     To execute, merely load, reset and run.
;;;======================================================

;;;***********************
;;;* LOG-TIME FUNCTION *
;;;***********************

(deffunction log-time ()
  (bind ?d (local-time))
(format nil "%d/%d/%d %d:%d:%d"
  (nth$ 1 ?d) (nth$ 2 ?d) (nth$ 3 ?d)
  (nth$ 4 ?d) (nth$ 5 ?d) (nth$ 6 ?d)))

;;;**********************
;;;* ENGINE STATE RULES *
;;;**********************

(defrule normal-engine-state-conclusions
(declare (salience 10))
(working-state engine normal)
=>
(assert (repair "No repair needed."))
(assert (spark-state engine normal))
(assert (charge-state battery charged))
(assert (rotation-state engine rotates)))

(defrule unsatisfactory-engine-state-conclusions
(declare (salience 10))
(working-state engine unsatisfactory)
=>
(assert (charge-state battery charged))
(assert (rotation-state engine rotates)))

;;;***************
;;;* QUERY RULES *
;;;***************

(defrule determine-engine-state1
(not (working-state engine ?))
(not (repair ?))
=>
(user-confirm "engine start" "Does the engine start (yes/no)? "))

(defrule determine-engine-state2
(engine start yes)
=>
(user-confirm "engine run normally" "Does the engine run normally (yes/no)? "))

(defrule determine-engine-state3
?a <- (engine run normally yes)
=>
(assert (working-state engine normal))
(retract ?a))

(defrule determine-engine-state4
?a <- (engine run normally no)
=>
(assert (working-state engine unsatisfactory))
(retract ?a))

(defrule determine-engine-state5
?a <- (engine start no)
=>
(assert (working-state engine does-not-start))
(retract ?a))

(defrule determine-rotation-state1
(working-state engine does-not-start)
(not (rotation-state engine ?))
(not (repair ?))
=>
(user-confirm "engine rotate" "Does the engine rotate (yes/no)? "))

(defrule determine-rotation-state2
?a <- (engine rotate yes)
=>
(assert (rotation-state engine rotates))
(assert (spark-state engine irregular-spark))
(retract ?a))

(defrule determine-rotation-state3
?a <- (engine rotate no)
=>
(assert (rotation-state engine does-not-rotate))       
(assert (spark-state engine does-not-spark))
(retract ?a))

(defrule determine-sluggishness1
(working-state engine unsatisfactory)
(not (repair ?))
=>
(user-confirm "engine sluggish" "Is the engine sluggish (yes/no)? "))

(defrule determine-sluggishness2
?a <- (engine sluggish yes)
=>
(assert (repair "Clean the fuel line."))
(retract ?a))

(defrule determine-misfiring1
(working-state engine unsatisfactory)
(not (repair ?))
=>
(user-confirm "engine misfire" "Does the engine misfire (yes/no)? "))

(defrule determine-misfiring2
?a <- (engine misfire yes)
=>
(assert (repair "Point gap adjustment."))       
(assert (spark-state engine irregular-spark))
(retract ?a))

(defrule determine-knocking1
(working-state engine unsatisfactory)
(not (repair ?))
=>
(user-confirm "engine knock" "Does the engine knock (yes/no)? "))

(defrule determine-knocking2
?a <- (engine knock yes)
=>
(assert (repair "Timing adjustment."))
(retract ?a))

(defrule determine-low-output1
(working-state engine unsatisfactory)
(not (symptom engine low-output | not-low-output))
(not (repair ?))
=>
(user-confirm "engine low output" "Is the output of the engine low (yes/no)? "))

(defrule determine-low-output2
(declare (auto-focus TRUE))
?a <- (engine low output yes)
=>
(assert (symptom engine low-output))
(retract ?a))

(defrule determine-low-output3
?a <- (engine low output no)
=>
(assert (symptom engine not-low-output))
(retract ?a))

(defrule determine-gas-level1
(working-state engine does-not-start)
(rotation-state engine rotates)
(not (repair ?))
=>
(user-confirm "engine has gas in tank" "Does the tank have any gas in it (yes/no)? "))

(defrule determine-gas-level2
?a <- (engine has gas in tank)
=>
(assert (repair "Add gas."))
(retract ?a))

(defrule determine-point-surface-state2
?a <- (engine has points burned)
=>
(assert (repair "Replace the points."))
(retract ?a))

(defrule determine-battery-state1
(rotation-state engine does-not-rotate)
(not (charge-state battery ?))
(not (repair ?))
=>
(user-confirm "engine has battery charged" "Is the battery charged (yes/no)? "))

(defrule determine-battery-state2
?a <- (engine has battery charged yes)
=>
(assert (charge-state battery charged))
(retract ?a))

(defrule determine-battery-state3
?a <- (engine has battery charged no)
=>
(assert (repair "Charge the battery."))
(assert (charge-state battery dead))
(retract ?a))

(defrule determine-point-surface-state1
(or (and (working-state engine does-not-start)      
              (spark-state engine irregular-spark))
      (symptom engine low-output))
=>
(user-select "engine has points" "What is the surface state of the points?"
                    normal burned contaminated))

(defrule determine-point-surface-state3
?a <- (engine has points contaminated)
=>
(assert (repair "Clean the points."))
(retract ?a))

(defrule determine-conductivity-test1
(working-state engine does-not-start)      
(spark-state engine does-not-spark)
(charge-state battery charged)
(not (repair ?))
=>
(user-confirm "engine conductivity test positive" "Is the conductivity test for the ignition coil positive (yes/no)? "))

(defrule determine-conductivity-test2
?a <- (engine conductivity test positive yes)
=>
(assert (repair "Repair the distributor lead wire."))
(retract ?a))

(defrule determine-conductivity-test3
?a <- (engine conductivity test positive no)
=>
(assert (repair "Replace the ignition coil."))
(retract ?a))

(defrule to-mechanic
(declare (auto-focus TRUE))
(or (and (engine has gas in tank yes)
             (engine has points normal))
      (and (engine knock no)
             (engine misfire no)
             (engine sluggish no)
             (symptom engine not-low-output)))
=>
(assert (repair "Take your car to a mechanic.")))

;;;****************************
;;;* REPAIR RULES *
;;;****************************

(defrule print-repair
(declare (salience 10))
(repair ?item)
=>
(user-log "Automotive Expert System" 
  (str-cat (log-time) " Suggested Repair: " ?item)))

