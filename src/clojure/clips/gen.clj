(ns clips.gen
(:use protege.core)
(:import
  edu.stanford.smi.protege.model.ValueType))
(defn gen-rule [inst]
  (let [tit (sv inst "title")
       sal (sv inst "salience")
       afs (sv inst "auto-focus")
       lhs (sv inst "lhs")
       rhs (sv inst "rhs")]
  (str "(defrule " tit "\n"
        (cond 
          (and sal afs) 
          (str "(declare (salience " sal ")\n"
                "         (auto-focus TRUE))\n")
          sal
          (str "(declare (salience " sal "))\n")
          afs "(declare (auto-focus TRUE))\n"
          true "")
        lhs
        "\n=>\n"
        rhs
        ")")))

(defn gen-function [inst]
  (let [tit (sv inst "title")
       ars (sv inst "arguments")
       src (sv inst "source")]
  (str "(deffunction " tit " " ars "\n  "
        src ")")))

(defn gen-global [inst]
  (let [tit (sv inst "title")
       src (sv inst "source")]
  (str "(defglobal " tit "\n  " src ")")))

(defn gen-slot [ss fsi pfx sep]
  (let [tit (sv fsi "title")
       val (sv fsi "value")]
  (str ss pfx "(" tit " " val ")" sep)))

(defn gen-slot-type [ss sli]
  (let [tit (sv sli "title")
       typ (sv sli "type")
       aux (sv sli "aux")
       mult (sv sli "multislot")
       head (if (is? mult) "  (multislot  " "  (slot  ")]
  (str ss head tit " (type " typ ") " aux ")\n")))

(defn gen-template [inst]
  (let [type (sv inst "title")
       sls (svs inst "temp-slots")]
  (str (reduce #(gen-slot-type %1 %2) 
                      (str "(deftemplate  " type "\n")
                      sls) ")")))

(defn gen-fact [inst]
  (let [type (sv inst "title")
       impl (sv inst "implied")
       sls (svs inst "fact-slots")
       vrt (sv inst "vertical")
       pfx (if vrt "    " "")
       sep (if vrt "\n" " ")]
  (if impl
    (str "(" type " " impl ")" )
    (str (reduce #(gen-slot %1 %2 pfx sep) 
                        (str "(" type " " sep) 
                        sls) pfx ")\n"))))

(defn gen-facts [inst]
  (loop [ff (svs inst "facts") src (str "(deffacts " (sv inst "title"))]
  (if (seq ff)
    (recur (rest ff)
       (str src "\n  " (gen-fact (first ff)) ))
    (str src ")"))))

(defn gen-module [inst]
  (let [tit (sv inst "title")
       src (sv inst "source")]
  (str "(defmodule " tit "\n  " src ")")))

(defn gen-command [inst]
  (str "(" (sv inst "title") " "  (sv inst "source") ")"))

(defn gen-comment [inst]
  (sv inst "source"))

(defn save-clp-file [hm inst]
  (loop [cc (svs inst "constructs") src ""]
  (if (seq cc)
    (let [[f & r] cc]
      (recur r 
        (str src (condp = (typ f)
                      "Fact"          (gen-fact f)
                      "Facts"        (gen-facts f)
                      "Function"   (gen-function f)
                      "Global"       (gen-global  f)
                      "Module"     (gen-module f)
                      "Rule"          (gen-rule f)
                      "Template"  (gen-template f)
                      "Command" (gen-command f)
                      "Comment"  (gen-comment f)
                      (do (println "Unknown construct " (typ f) "!") ""))
                    "\n\n")))
    (spit (sv inst "path") src))))

