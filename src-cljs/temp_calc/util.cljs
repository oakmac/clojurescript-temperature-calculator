(ns temp-calc.util)

(defn js-log
  "Logs a JavaScript thing."
  [js-thing]
  (.log js/console js-thing))

(defn log
  "Logs a Clojure thing."
  [clj-thing]
  (js-log (pr-str clj-thing)))

(defn round
  "Rounds to nearest integer."
  [x]
  (.round js/Math x))
