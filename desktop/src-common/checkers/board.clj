(ns checkers.board
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [clojure.core.matrix :as mx]
            [checkers.game :as game])
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
     :white-player "board/white.png"
     :b "board/black.png"
     :black-player "board/black.png"
     :g "board/green.png"
     :r "board/red.png"
     :black-p "board/black_pawn.png"
     :white-p "board/white_pawn.png"
     0 "board/transparent.png"))

(defn generate-board-element [type x y]
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


(defn- chenge-color-of-pawns-beckground [entity position]
  (if (and (#(= (:type %) :b) entity) (entity-at-position? entity position))
    (do
      (texture! entity :set-texture (new-texture (create-board-element :g)))
      (assoc entity :type :g))
    entity))

(defn- pawn-to-player-mapper [pawn]
  (case pawn
    :white-p :white-player
    :white-q :white-player
    :black-p :black-player
    :black-q :black-player))

(defn selected-pawn-is-valid? [entity position player]
  (and (some #(= % (:type entity)) [:white-p :white-q :black-p :black-q])
           (entity-at-position? entity position)
           (= (pawn-to-player-mapper(:type entity)) player)))

(defn- update-active-player-indicator [entity position selected-pawn-is-valid]
  (if (and (#(= (:type %) (game/get-player)) entity)
            selected-pawn-is-valid)
    (do
      (game/change-player)
      (texture! entity :set-texture (new-texture (create-board-element (game/get-player))))
      (assoc entity :type (game/get-player)))
    entity))

(defn select-pawn [entities position]
  (let [selected-pawn-is-valid
          (some true? (map #(selected-pawn-is-valid? % position (game/get-player)) entities))
        entities-with-updated-active-player-indicator
          (map #(update-active-player-indicator %1 position selected-pawn-is-valid) entities)]
    (map #(chenge-color-of-pawns-beckground %1 position)
         entities-with-updated-active-player-indicator)))
