(ns temp-calc.convert
  (:require
    [temp-calc.util :refer [js-log log]]))

;;------------------------------------------------------------------------------
;; Temperature Conversion
;;------------------------------------------------------------------------------

;; Fahrenheit
(defn F->C [tf]
  (* (/ 5 9) (- tf 32)))

(defn F->K [tf]
  (+ (/ (- tf 32) 1.8) 273.15))

;; Centigrade
(defn C->F [tc]
  (+ (* (/ 9 5) tc) 32))

(defn C->K [tc]
  (+ 273.15 tc))

;; Kelvin
(defn K->C [tk]
  (- tk 273.15))

(defn K->F [tk]
  (+ (* (- tk 273.15) 1.8) 32))

(defn convert-temps
  "Given a keyword 'kwd' of :C, :F, or :K, returns a map of temperature 'v'
   for all temperature formats."
  [kwd v]
  (case kwd
    :C {:C v
        :F (C->F v)
        :K (C->K v)}

    :F {:C (F->C v)
        :F v
        :K (F->K v)}

    :K {:C (K->C v)
        :F (F->C v)
        :K v}))
