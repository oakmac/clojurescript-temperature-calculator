(ns temp-calc.core
  (:require
    cljsjs.jquery
    [temp-calc.util :refer [js-log log]]))

;; alias jQuery
(def $ js/jQuery)

;;------------------------------------------------------------------------------
;; Temperature Conversion Functions
;;------------------------------------------------------------------------------

;;------------------------------------------------------------------------------
;;
;;------------------------------------------------------------------------------

(def initial-state {
  :C 0
  :F 0
  :K 0
  })

(def state (atom initial-state))

;;------------------------------------------------------------------------------
;; DOM Events
;;------------------------------------------------------------------------------

(defn- add-events!
  "Add event handlers on DOM elements using jQuery."
  []
  ;; TODO: write me
  )

;;------------------------------------------------------------------------------
;; Global App Init
;;------------------------------------------------------------------------------

(defn- init!
  "Initialize the app."
  []
  (add-events!)
  (swap! state identity))

;; add init! on window load event
(.addEventListener js/window "load" init!)