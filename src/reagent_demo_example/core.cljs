(ns reagent-demo-example.core
  (:require [reagent.core :as r] [clojure.string :as str]))

;(defonce timer (r/atom (js/Date.)))
;
;(defonce time-color (r/atom "#666"))
;
;(defonce time-updater (js/setInterval
;                        #(reset! timer (js/Date.)) 1000))
;
;(defn greeting [message]
;  [:h1 message])
;
;(defn clock []
;  (let [time-str (-> @timer .toTimeString (clojure.string/split " ") first)]
;    [:div.example-clock
;     {:style {:color @time-color}}
;     time-str]))
;
;(defn color-input []
;  [:div.color-input
;   "Time color: "
;   [:input {:type "text"
;            :value @time-color
;            :on-change #(reset! time-color (-> % .-target .-value))}]])
;
;(defn simple-example []
;  [:div
;   [greeting "Hello world, it is now"]
;   [clock]
;   [color-input]])
;
;(r/render-component [simple-example]
;                          (. js/document (getElementById "app")))

(defonce counter (r/atom 0))

(defonce experiments (r/atom (sorted-map)))

(def columns ["Name" "Organism" "Platform" "Method"])

(def test-data [
                ["experiment1" "homo sapiens" "ilumina 666" "microarray"]
                ["experiment2" "mus musculus" "ilumina 667" "microarray"]])

(defn add-experiment [experiment-data]
  (let [[name organism platform method] experiment-data]
    (let [id (swap! counter inc)]
      (.log js/console id)
      (swap! experiments assoc id {:id id :name name :organism organism :platform platform :method method}))))

(defonce init (do
                (doseq [experiment test-data] (add-experiment experiment))))

(defn experiment-table-head []
  [:thead
   [:tr
    (for [column columns] [:td
                           [:b column]
                           ])]])

(defn experiment-row [experiment]
  [:tr
   (for [column columns] [:td (get experiment (keyword (str/lower-case column)))])])

(defn table-body []
  (let [experiments (vals @experiments)]
    [:tbody
     (for [experiment experiments] [experiment-row experiment])]))

(defn experiment-table []
  [:table {:class "table table-hover experiment-table"}
   [experiment-table-head]
   [table-body]])

(r/render-component [experiment-table]
                    (. js/document (getElementById "app")))
