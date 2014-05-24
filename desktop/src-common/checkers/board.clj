(ns checkers.board
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]))


(def board-element-dimension-in-px 55)

(def number-of-elements-in-board-dimention 8)

(defn- generate-board-element [type x y]
  (assoc (texture type)
         :x x
         :y y
         :width board-element-dimension-in-px
         :height board-element-dimension-in-px))

(defn- calc-position-in-row-in-px [elem-number]
  (* board-element-dimension-in-px
     (mod elem-number number-of-elements-in-board-dimention)))

(defn- calc-position-in-col-in-px [elem-number]
  (* board-element-dimension-in-px
     (int (/ elem-number number-of-elements-in-board-dimention))))

(defn- get-board-element[elem-number]
  (if (even? (int (/ elem-number number-of-elements-in-board-dimention)))
    (if (even? elem-number)
      "board/black.png" "board/white.png")
    (if (even? elem-number)
      "board/white.png" "board/black.png")))


(defn generate-board []
  (let [x-starting-position 170
        y-starting-position 50]
    (loop [elem-index 0
           elements []]
      (if (> elem-index 63)
        elements
        (recur (inc elem-index)
               (conj elements
                     (generate-board-element
                      (get-board-element elem-index)
                      (+ x-starting-position (calc-position-in-row-in-px elem-index))
                      (+ y-starting-position (calc-position-in-col-in-px elem-index)))))))))
