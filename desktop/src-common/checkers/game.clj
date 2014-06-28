(ns checkers.game
  (:require [clojure.core.matrix :as mx]
            [clatrix.core :as cl]
            [checkers.move-validation :as mv]))


(def current-player (atom :white-player-indicator))

(defn get-player-indicator []
  @current-player)

(defn change-player []
  (if (= @current-player :white-player-indicator)
      (reset! current-player :black-player-indicator)
      (reset! current-player :white-player-indicator)))

(defn move[move]
  (let [{:keys [board from to]} move]
    (if (mv/move-valid? move)
      (mx/mset (mx/mset board (first to) (second to) :white-p) (first from) (second from) 0)
      board)))
