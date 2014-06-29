(ns checkers.game
  (:require [clojure.core.matrix :as mx]
            [clatrix.core :as cl]
            [checkers.move-validation :as mv]))



(def game-state (atom
                 (mx/matrix [[       0 :black-p        0 :black-p        0 :black-p        0 :black-p]
                             [:black-p        0 :black-p        0 :black-p        0 :black-p        0]
                             [       0 :black-p        0 :black-p        0 :black-p        0 :black-p]
                             [       0        0        0        0        0        0        0        0]
                             [       0        0        0        0        0        0        0        0]
                             [:white-p        0 :white-p        0 :white-p        0 :white-p        0]
                             [       0 :white-p        0 :white-p        0 :white-p        0 :white-p]
                             [:white-p        0 :white-p        0 :white-p        0 :white-p        0]])))

(defn get-game-state []
  @game-state)

(def selected-pawn-coordinates (atom nil))

(def current-player (atom :white-player))

(defn get-player []
  @current-player)

(defn change-player []
  (if (= @current-player :white-player)
      (reset! current-player :black-player)
      (reset! current-player :white-player)))

(defn selected-player-valid? [player-type]
  (= player-type (get-player)))

(defn move[move]
  (let [{:keys [board from to]} move]
    (if (mv/move-valid? move)
      (mx/mset (mx/mset board (first to) (second to) :white-p) (first from) (second from) 0)
      board)))
