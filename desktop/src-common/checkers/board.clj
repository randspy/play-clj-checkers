(ns checkers.board
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [clojure.core.matrix :as mx]
            [checkers.game :as game]
            [checkers.move-validation :as mv])
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

(defn generate-board-element [type x y x-board-position y-board-position]
  (let [board-element (create-board-element type)]
    (if (not (nil? board-element))
      (assoc (texture board-element)
        :x x
        :x-board-position x-board-position
        :y y
        :y-board-position y-board-position
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
                      (- y-starting-position (calc-position-in-col-in-px elem-index))
                      (int (/ elem-index number-of-elements-in-board-dimention))
                      (mod elem-index number-of-elements-in-board-dimention))))))))

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

(defn get-pawn-positon-on-board[entities position]
  (let[pawn-at-position (first (filter #(entity-at-position? % position) entities))]
    (if (seq pawn-at-position)
      [(:x-board-position pawn-at-position) (:y-board-position pawn-at-position)]
      [-1 -1])))

(defn- pawn-to-player-mapper [pawn]
  (case pawn
    :white-p :white-player
    :white-q :white-player
    :black-p :black-player
    :black-q :black-player))

(defn- player-to-pawn-mapper [player]
  (case player
    :white-player [:white-p :white-q]
    :black-player [:black-p :black-q]))

(defn selected-pawn-is-valid? [entities position player board]
  (let [pawn-coordinates (get-pawn-positon-on-board entities position)]
    (mv/select-valid? {:board board :from pawn-coordinates :pawn-type (player-to-pawn-mapper player)})))


(defn- change-color [entity new-color]
  (do
      (texture! entity :set-texture (new-texture (create-board-element new-color)))
      (assoc entity :type new-color)))

(defn- chenge-color-of-pawns-beckground [entity position selected-pawn-is-valid]
  (cond
       (and selected-pawn-is-valid
            (#(= (:type %) :b) entity)
            (entity-at-position? entity position)) (change-color entity :g)
       (#(= (:type %) :g) entity) (change-color entity :b)
       :else entity))

(defn- update-active-player-indicator [entity position selected-pawn-is-valid]
  (if (and (#(= (:type %) (game/get-player)) entity)
            selected-pawn-is-valid)
    (do
      (game/change-player)
      (texture! entity :set-texture (new-texture (create-board-element (game/get-player))))
      (assoc entity :type (game/get-player)))
    entity))

(defn get-entitis-containg-type [entities]
  (filter #(contains? % :type) entities))

(defn update-entities [function entities position flag]
  (map #(function % position flag) entities))

(defn select-pawn [entities position]
  (let [selected-pawn-is-valid (selected-pawn-is-valid?
                                    (get-entitis-containg-type entities)
                                    position
                                    (game/get-player)
                                    (game/get-game-state))
        active-player-indicator-updated
          (update-entities
             update-active-player-indicator
             entities position
             selected-pawn-is-valid)]
    (update-entities
       chenge-color-of-pawns-beckground
       active-player-indicator-updated
       position
       selected-pawn-is-valid)))

;;(game/change-player)
