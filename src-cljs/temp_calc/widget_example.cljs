(ns temp-calc.widget-example
  (:require
    cljsjs.react
    [quiescent :include-macros true]
    [sablono.core :as sablono :include-macros true]
    [temp-calc.convert :refer [convert-temps]]
    [temp-calc.util :refer [js-log log round]]))

;;------------------------------------------------------------------------------
;; DOM Helpers
;;------------------------------------------------------------------------------

(defn by-id [id]
  (.getElementById js/document id))

;;------------------------------------------------------------------------------
;; Page State
;;------------------------------------------------------------------------------

(def initial-state (convert-temps :C 0))

;;------------------------------------------------------------------------------
;; React Events
;;------------------------------------------------------------------------------

(defn- on-change-input [temp-kwd js-evt the-atom]
  (let [new-value1 (js/parseFloat (aget js-evt "currentTarget" "value"))
        new-value2 (if (js/isNaN new-value1) 0 new-value1)]
    (reset! the-atom (convert-temps temp-kwd new-value2))))

;;------------------------------------------------------------------------------
;; Quiescent Components
;;------------------------------------------------------------------------------

(quiescent/defcomponent TextInput [[temp-kwd v] the-atom]
  (sablono/html
    [:label (str (name temp-kwd) ": ")
      [:input {:on-change #(on-change-input temp-kwd % the-atom)
               :type "text"
               :value v}]]))

(quiescent/defcomponent RangeInput [[temp-kwd v] the-atom]
  (sablono/html
    [:label (str (name temp-kwd) ": ")
      [:input {:min -1000
               :max 1000
               :on-change #(on-change-input temp-kwd % the-atom)
               :type "range"
               :value v}]]))

(quiescent/defcomponent TempCalc [temps the-atom]
  (sablono/html
    [:div
      [:fieldset
        [:legend "Text Inputs:"]
        (TextInput [:C (:C temps)] the-atom)
        (TextInput [:F (:F temps)] the-atom)
        (TextInput [:K (:K temps)] the-atom)]
      [:fieldset
        [:legend "Range Inputs:"]
        (RangeInput [:C (:C temps)] the-atom)
        (RangeInput [:F (:F temps)] the-atom)
        (RangeInput [:K (:K temps)] the-atom)]]))

;;------------------------------------------------------------------------------
;; Atom Change / Render Loop
;;------------------------------------------------------------------------------

(defn- on-change-temps
  "Trigger a React render every time the atom changes."
  [the-atom new-state container-id]
  (.requestAnimationFrame js/window (fn []
    (quiescent/render (TempCalc new-state the-atom) (by-id container-id)))))

;;------------------------------------------------------------------------------
;; Public Methods
;;------------------------------------------------------------------------------

(defn- js-set-temp2 [tmp-kwd value the-atom]
  (reset! the-atom (convert-temps tmp-kwd value)))

(defn- js-set-temp [arg1 arg2 the-atom]
  (if (string? arg1)
    (js-set-temp2 (keyword arg1) (int arg2) the-atom)
    (let [m (js->clj arg1)
          tmp-kwd (-> m keys first keyword)
          value (-> m vals first int)]
      (js-set-temp2 tmp-kwd value the-atom))))

;;------------------------------------------------------------------------------
;; Constructor
;;------------------------------------------------------------------------------

(defn- js-temp-calc [container-id]
  (let [the-atom (atom initial-state)]
    (add-watch the-atom :change #(on-change-temps %2 %4 container-id))
    (swap! the-atom identity)
    (js-obj
      "setTemp" #(js-set-temp %1 %2 the-atom)
      "temps" #(clj->js @the-atom))))

;; export the Constructor function
(goog/exportSymbol "TempCalc" js-temp-calc)
