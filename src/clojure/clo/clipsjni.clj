(ns clo.clipsjni
(:use 
  protege.core)
(:import
  net.sf.clipsrules.jni.Environment
  net.sf.clipsrules.jni.UserFunction
  net.sf.clipsrules.jni.IntegerValue
  net.sf.clipsrules.jni.FloatValue
  net.sf.clipsrules.jni.StringValue
  net.sf.clipsrules.jni.MultifieldValue
  javax.swing.JFrame
  javax.swing.JDialog
  javax.swing.JOptionPane))
(def DEFN-dialog (defn dialog
  ([cls title]
    (when-let [d (foc cls "title" title)]
      (.show *prj* (.shallowCopy d *kb* nil))))
  ([cls title mess]
    (when-let [d (foc cls "title" title)]
      (if (= cls "Log")
        (let [old (sv d "text")]
          (ssv d "text" (if (empty? old) 
                                mess
                                (str old "\n" mess))))
        (ssv   d "text" mess))
      (.show *prj* d)))
 ([cls title mess vsdf]
    (when-let [d (foc cls "title" title)]
      (if (= cls "Input")
        (ssv d "input" vsdf)
        (ssvs d "variants" (map str vsdf)))
      (ssv   d "text" mess)
      (.show *prj* d)))))
(def DEFONSE-ENV (defonce ENV (Environment.)))
(def DUF-user-message (defonce UF1 (do

(.addUserFunction ENV "user-message"
  (proxy [UserFunction] []
    (evaluate [args]
      (let [[title mess] (vec (map #(.getValue %) args))]
        (dialog "Message" title mess)
        (IntegerValue. 0)))))

0)))
(def DUF-user-confirm (defonce UF2 (do

(.addUserFunction ENV "user-confirm"
  (proxy [UserFunction] []
    (evaluate [args]
      (let [[title mess] (vec (map #(.getValue %) args))]
        (dialog "Confirm" title mess)
        (IntegerValue. 0)))))

0)))
(def DUF-user-select (defonce UF3 (do

(.addUserFunction ENV "user-select"
  (proxy [UserFunction] []
    (evaluate [args]
      (let [[title mess & vars] (vec (map #(.getValue %) args))]
        (dialog "Select" title mess vars)
        (IntegerValue. 0)))))

0)))
(def DUF-user-input (defonce UF4 (do

(.addUserFunction ENV "user-input"
  (proxy [UserFunction] []
    (evaluate [args]
      (let [[title mess deflt] (vec (map #(.getValue %) args))]
        (dialog "Input" title mess deflt)
        (IntegerValue. 0)))))

0)))
(def DUF-user-log (defonce UF5 (do

(.addUserFunction ENV "user-log"
  (proxy [UserFunction] []
    (evaluate [args]
      (let [[title mess] (vec (map #(.getValue %) args))]
        (dialog "Log" title mess)
        (IntegerValue. 0)))))

0)))
(def DUF-user-questionnaire (defonce UF6 (do

(.addUserFunction ENV "user-questionnaire"
  (proxy [UserFunction] []
    (evaluate [args]
      (let [[title] (vec (map #(.getValue %) args))]
        (dialog "Questionnaire" title)
        (IntegerValue. 0)))))

0)))
(defn assert-string [s]
  (.getValue (.assertString ENV s)))

(defn get-eval-value [s]
  (.getValue (.eval ENV s)))

(defn get-eval-values [s]
  (map #(.getValue %) (.eval ENV s)))

(defn load-constructs [p]
  (.load ENV p))

(defn clear []
  (.clear ENV))

(defn reset []
  (.reset ENV))

(defn run []
  (.run ENV))

(defn find-facts
  ([type]
  (.findAllFacts ENV type))
([var type condition]
 (.findAllFacts ENV var type condition)))

(defn get-slot-value [s]
  (.getSlotValue s))

(defn select [hm inst]
  (let [mp (into {} hm)
       clw (mp "clsWidget")
       sel (.getSelection (.getSlotWidget clw (slt "variants")))]
  (if (not (empty? sel))
    (let [sel (interpose " " sel)
           sel (apply str sel)
           ans (str "(" (mp "title") " " sel ")")]
      (delin inst)
      (assert-string ans)
      (run)))))

(defn yes [hm inst]
  (let [ans (str "(" (sv inst "title") " yes)")]
  (delin inst)
  (assert-string ans)
  (run)))

(defn no [hm inst]
  (let [ans (str "(" (sv inst "title") " no)")]
  (delin inst)
  (assert-string ans)
  (run)))

(defn input [hm inst]
  (let [mp (into {} hm)
       ans (str "(" (mp "title") " " (mp "input") ")")]
      (delin inst)
      (assert-string ans)
      (run)))

(defn clear-log [hm inst]
  (ssv inst "text" ""))

(defn assert-facts
  ([ff]
  (doseq [f ff]
    (assert-string f)))
([hm inst]
  (doseq [q (svs inst "questions")]
    (assert-string 
      (.replaceFirst (sv q "fact") "\\?"
        (if (is? (sv q "flag"))
           "yes"
           "no"))))
  (delin inst)  
  (run)))

