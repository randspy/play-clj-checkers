(ns checkers.board
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [clojure.core.matrix :as mx]))


(def board-element-dimension-in-px 55)

(def number-of-elements-in-board-dimention 8)

(def board
  (mx/matrix [[:w :b :w :b :w :b :w :b]
              [:b :w :b :w :b :w :b :w]
              [:w :b :w :b :w :b :w :b]
              [:b :w :b :w :b :w :b :w]
              [:w :b :w :b :w :b :w :b]
              [:b :w :b :w :b :w :b :w]
              [:w :b :w :b :w :b :w :b]
              [:b :w :b :w :b :w :b :w]]))

(def new-game
  (mx/matrix [[0 :bp 0 :bp 0 :bp 0 :bp]
              [:bp 0 :bp 0 :bp 0 :bp 0]
              [0 :bp 0 :bp 0 :bp 0 :bp]
              [0 0 0 0 0 0 0 0]
              [0 0 0 0 0 0 0 0]
              [:wp 0 :wp 0 :wp 0 :wp 0]
              [0 :wp 0 :wp 0 :wp 0 :wp]
              [:wp 0 :wp 0 :wp 0 :wp 0]]))

(defn- create-board-element[elem]
  (case elem
     :w "board/white.png"
     :b "board/black.png"
     :g "board/green.png"
     :r "board/red.png"
     :bp "board/black_pawn.png"
     :wp "board/white_pawn.png"))

(defn- generate-board-element [type x y]
  (let [board-element (create-board-element type)]
    (if (not (nil? board-element))
      (assoc (texture board-element)
        :x x
        :y y
        :width board-element-dimension-in-px
        :height board-element-dimension-in-px
        :type type)
      [])))

(defn- calc-position-in-row-in-px [elem-number]
  (* board-element-dimension-in-px
     (mod elem-number number-of-elements-in-board-dimention)))

(defn- calc-position-in-col-in-px [elem-number]
  (* board-element-dimension-in-px
     (int (/ elem-number number-of-elements-in-board-dimention))))

(defn gen-board [board]
  (let [x-starting-position 170
        y-starting-position 430
        flatten-board (flatten board)
        number-of-elements (- (count flatten-board) 1)]
    (loop [elem-index 0
           board-elements []]
      (if (> elem-index number-of-elements)
        board-elements
        (recur (inc elem-index)
               (conj board-elements
                     (generate-board-element
                      (nth flatten-board elem-index)
                      (+ x-starting-position (calc-position-in-row-in-px elem-index))
                      (- y-starting-position (calc-position-in-col-in-px elem-index)))))))))



(defn filter-location [entities position]
  (let [x (:x position)
        y (:y position)]
    (filter #(and (< (:x %) x (+ (:x %) board-element-dimension-in-px))
                  (< (:y %) y (+ (:y %) board-element-dimension-in-px)))
            entities)))

(defn filter-pawns [entities]
  (filter #(= (:type %) :wp) entities))
