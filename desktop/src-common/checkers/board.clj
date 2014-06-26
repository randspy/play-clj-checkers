(ns checkers.board
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [clojure.core.matrix :as mx])
  (:import [com.badlogic.gdx.graphics Texture]))


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
  (mx/matrix [[       0 :black-p        0 :black-p        0 :black-p        0 :black-p]
              [:black-p        0 :black-p        0 :black-p        0 :black-p        0]
              [       0 :black-p        0 :black-p        0 :black-p        0 :black-p]
              [       0        0        0        0        0        0        0        0]
              [       0        0        0        0        0        0        0        0]
              [:white-p        0 :white-p        0 :white-p        0 :white-p        0]
              [       0 :white-p        0 :white-p        0 :white-p        0 :white-p]
              [:white-p        0 :white-p        0 :white-p        0 :white-p        0]]))

(defn- create-board-element[elem]
  (case elem
     :w "board/white.png"
     :b "board/black.png"
     :g "board/green.png"
     :r "board/red.png"
     :black-p "board/black_pawn.png"
     :white-p "board/white_pawn.png"
     0 "board/transparent.png"))

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

(defn new-texture [file]
  (Texture. file))

(defn entity-at-position? [entity position]
  (let [x-pos (:x position)
        y-pos (:y position)
        x-low-ent (:x entity)
        y-low-ent (:y entity)
        x-hight-ent (+ (:width entity) x-low-ent)
        y-hight-ent (+ (:height entity) y-low-ent)]
    (and (< x-low-ent x-pos x-hight-ent) (< y-low-ent y-pos y-hight-ent))))


(defn- update-pawn-position [entity position]
  (if (and (#(= (:type %) :b) entity) (entity-at-position? entity position))
    (do
      (texture! entity :set-texture (new-texture "board/green.png"))
      (assoc entity :type :g))
    entity))


(defn move-pawn [entities position]
  (map #(update-pawn-position %1 position) entities))

(defn filter-pawns [entities]
  (filter #(= (:type %) :white-p) entities))
