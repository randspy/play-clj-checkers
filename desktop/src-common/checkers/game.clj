(ns checkers.game
  (:require [clojure.core.matrix :as mx]
            [clatrix.core :as cl]
            [checkers.move-validation :as mv]))


(defn move-white[move]
  (let [{:keys [board from to]} move]
    (if (mv/is-white-move-valid move)
      (mx/mset (mx/mset board (first to) (second to) :white-p) (first from) (second from) 0)
      board)))
