(ns temp-calc.core
  (:require
    cljsjs.jquery
    [temp-calc.util :refer [js-log log]]))

;; alias jQuery
(def $ js/jQuery)

;;------------------------------------------------------------------------------
;; Temperature Conversion
;;------------------------------------------------------------------------------

(defn- round
  "Rounds to nearest integer."
  [x]
  (.round js/Math x))

;; Fahrenheit
(defn- F->C [tf]
  (* (/ 5 9) (- tf 32)))

(defn- F->K [tf]
  (* (+ 459.67 tf) (/ 5 9)))

;; Centigrade
(defn- C->F [tc]
  (+ (* (/ 9 5) tc) 32))

(defn- C->K [tc]
  (+ 273.15 tc))

;; Kelvin
(defn- K->C [tk]
  (- tk 273.15))

(defn- K->F [tk]
  (- (* (/ 9 5) tk) 459.67))

(defn- convert-temp
  "Given a temperature value 'v' and keyword 'kwd' of :C, :F, or :K
   Returns a map of the temperature 'v' in all three formats."
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

;;------------------------------------------------------------------------------
;; Page State
;;------------------------------------------------------------------------------

(def initial-state {
  :C 0
  :F (C->F 0)
  :K (C->K 0)})

(def current-temps (atom initial-state))

;;------------------------------------------------------------------------------
;; Update Page State
;;------------------------------------------------------------------------------

(defn- update-range-inputs! [_kwd _atom _old-temps new-temps]
  (.val ($ "#cRangeInput") (int (:C new-temps)))
  (.val ($ "#fRangeInput") (int (:F new-temps)))
  (.val ($ "#kRangeInput") (int (:K new-temps))))

(defn- update-text-inputs! [_kwd _atom _old-temps new-temps]
  (.val ($ "#cTextInput") (round (:C new-temps)))
  (.val ($ "#fTextInput") (round (:F new-temps)))
  (.val ($ "#kTextInput") (round (:K new-temps))))

(defn- log-state-change [_kwd _atom old-state new-state]
  (js-log "current-temps atom updated!")
  (js-log "old state:")
  (log old-state)
  (js-log "new state:")
  (log new-state)
  (js-log "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"))

(add-watch current-temps :ranges update-range-inputs!)
(add-watch current-temps :inputs update-text-inputs!)

;;------------------------------------------------------------------------------
;; DOM Events
;;------------------------------------------------------------------------------

(defn- on-change-input [temp-kwd js-evt]
  (let [new-temp-value (js/parseFloat (aget js-evt "currentTarget" "value"))
        new-temps (convert-temp temp-kwd new-temp-value)]
    (reset! current-temps new-temps)))

(defn- click-start-logging-btn [_js-evt]
  (add-watch current-temps :log log-state-change)
  (.hide ($ "#startLoggingBtn"))
  (.show ($ "#stopLoggingBtn")))

(defn- click-stop-logging-btn [_js-evt]
  (remove-watch current-temps :log)
  (.show ($ "#startLoggingBtn"))
  (.hide ($ "#stopLoggingBtn")))

(defn- add-dom-events!
  "Add event handlers on DOM elements using jQuery."
  []
  ;; text elements
  (.on ($ "#cTextInput") "change" (partial on-change-input :C))
  (.on ($ "#fTextInput") "change" (partial on-change-input :F))
  (.on ($ "#kTextInput") "change" (partial on-change-input :K))

  ;; range elements
  (.on ($ "#cRangeInput") "change" (partial on-change-input :C))
  (.on ($ "#fRangeInput") "change" (partial on-change-input :F))
  (.on ($ "#kRangeInput") "change" (partial on-change-input :K))

  ;; logging buttons
  (.on ($ "#startLoggingBtn") "click" click-start-logging-btn)
  (.on ($ "#stopLoggingBtn") "click" click-stop-logging-btn))

;;------------------------------------------------------------------------------
;; Global App Init
;;------------------------------------------------------------------------------

(defn- init!
  "Initialize the app."
  []
  (add-dom-events!)

  ;; trigger an initial state change
  (swap! current-temps identity))

;; run init! when the body loads
(.addEventListener js/window "load" init!)
