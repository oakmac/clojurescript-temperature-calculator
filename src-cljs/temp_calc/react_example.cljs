(ns temp-calc.react-example
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
(def current-temps (atom initial-state))

;;------------------------------------------------------------------------------
;; React Events
;;------------------------------------------------------------------------------

(defn- on-change-input [temp-kwd js-evt]
  (let [new-value1 (js/parseFloat (aget js-evt "currentTarget" "value"))
        new-value2 (if (js/isNaN new-value1) 0 new-value1)]
    (reset! current-temps (convert-temps temp-kwd new-value2))))

;;------------------------------------------------------------------------------
;; Quiescent Components
;;------------------------------------------------------------------------------

(quiescent/defcomponent TextInput [[temp-kwd v]]
  (sablono/html
    [:label (str (name temp-kwd) ": ")
      [:input {:on-change #(on-change-input temp-kwd %)
               :type "text"
               :value v}]]))

(quiescent/defcomponent RangeInput [[temp-kwd v]]
  (sablono/html
    [:label (str (name temp-kwd) ": ")
      [:input {:min -1000
               :max 1000
               :on-change #(on-change-input temp-kwd %)
               :type "range"
               :value v}]]))

(quiescent/defcomponent TempCalc [temps]
  (sablono/html
    [:div
      [:fieldset
        [:legend "Text Inputs:"]
        (TextInput [:C (:C temps)])
        (TextInput [:F (:F temps)])
        (TextInput [:K (:K temps)])]
      [:fieldset
        [:legend "Range Inputs:"]
        (RangeInput [:C (:C temps)])
        (RangeInput [:F (:F temps)])
        (RangeInput [:K (:K temps)])]]))

;;------------------------------------------------------------------------------
;; Atom Change / Render Loop
;;------------------------------------------------------------------------------

(defn- on-change-temps
  "Trigger a React render every time the atom changes."
  [_the-keyword _the-atom _old-state new-state]
  (.requestAnimationFrame js/window (fn []
    (quiescent/render (TempCalc new-state) (by-id "appContainer")))))

(add-watch current-temps :change on-change-temps)

;;------------------------------------------------------------------------------
;; Page Init
;;------------------------------------------------------------------------------

(defn- init!
  "Initialize the React example page."
  []
  ;; trigger an initial state change
  (swap! current-temps identity))

;; export the init! function
(goog/exportSymbol "startReactExample" init!)
