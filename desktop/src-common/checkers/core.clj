(ns checkers.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]))

(declare checkers main-screen)

(def board-element-dimension-in-px 55)

(def number-of-elements-in-board-dimention 8)

(defn- generate-board-element [type x y]
  (assoc (texture type)
         :x x :y y :width board-element-dimension-in-px :height board-element-dimension-in-px))

(defn- calc-position-in-row-in-px [elem-number]
  (* board-element-dimension-in-px
     (mod elem-number number-of-elements-in-board-dimention)))

(defn- calc-position-in-col-in-px [elem-number]
  (* board-element-dimension-in-px
     (int (/ elem-number number-of-elements-in-board-dimention))))

(defn- get-board-element[elem-number]
  (if (even? (int (/ elem-number number-of-elements-in-board-dimention)))
    (if (zero? (mod elem-number 2))
      "board/black.png" "board/white.png")
    (if (zero? (mod elem-number 2))
      "board/white.png" "board/black.png")))


(defn- generate-board []
  (let [x-starting-position 170
        y-starting-position 50]
    (loop [number-of-elements 0
           elements []]
      (if (> number-of-elements 63)
        elements
        (recur (inc number-of-elements)
               (conj elements
                     (generate-board-element
                      (get-board-element number-of-elements)
                      (+ x-starting-position (calc-position-in-row-in-px number-of-elements))
                      (+ y-starting-position (calc-position-in-col-in-px number-of-elements)))))))))


(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage))
    (let [background (texture "board/wood.jpg")
          boardfield (generate-board)]
      [background boardfield]))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))

  :on-key-down
  (fn [screen entities]
    (cond
     (= (:key screen) (key-code :r)) (on-gl (set-screen! checkers main-screen))
     :else entities)))

(defgame checkers
  :on-create
  (fn [this]
    (set-screen! this main-screen)))

(-> main-screen :entities deref)
