(ns greeting.client
  (:require [reagent.core :as reagent :refer [atom]]
            [colinkahn.flux.getters :refer [getter]]
            [colinkahn.flux.dispatcher :as dispatcher])
  (:require-macros  [colinkahn.flux.dispatcher :refer [defhandler]]))

(enable-console-print!)

;; Define state
(def state
  (dispatcher/set-state! (atom {:greeting {:salutation "Hello"
                                           :subject "World"}
                                :waterfall {:last-inputs [{:id (.now js/Date)
                                                           :text "Hello World"}]}})))
;; Define getters
(def get-salutation (getter [:greeting :salutation]))
(def get-subject (getter [:greeting :subject]))
(def get-greeting (getter (fn [sal sub]
                                    (str sal " " sub)) get-salutation get-subject))
(def get-waterfall (getter [:waterfall :last-inputs]))

;; Define action handlers
(defhandler :greeting [action]
  "change-salutation"
  {:salutation (:value action)}
  "change-subject"
  {:subject (:value action)})

(defhandler :waterfall [action]
  ["change-salutation" "change-subject"]
  (do
    (dispatcher/wait-for :greeting)
    (->> {:text (get-greeting @state)
          :id (.now js/Date)}
      (conj (get-waterfall @state))
      (take 10)
      (hash-map :last-inputs))))

(defn input-changed [type e]
  (if (= type :salutation)
    (dispatcher/dispatch
          {:type "change-salutation"
          :value (.. e -target -value)})
    (dispatcher/dispatch
          {:type "change-subject"
           :value (.. e -target -value)})))

(defn greeting []
  (let [new-state @state]
    [:div.greeting
      [:div.greeting__message
        (get-greeting new-state)]
      [:input {:type "text"
              :value (get-salutation new-state)
              :on-change (partial input-changed :salutation)}]
      [:input {:type "text"
              :value (get-subject new-state)
              :on-change (partial input-changed :subject)}]
      (for [t (get-waterfall new-state)]
        [:div {:key (:id t)} (:text t)])]))

(reagent/render [greeting]
                (js/document.getElementById "root"))
