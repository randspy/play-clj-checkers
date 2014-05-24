(ns checkers.game
  (:require [clojure.core.matrix :as mx]))

(def new-game
  (mx/matrix [[ 0 :b  0 :b  0 :b  0 :b]
              [:b  0 :b  0 :b  0 :b  0]
              [ 0 :b  0 :b  0 :b  0 :b]
              [ 0  0  0  0  0  0  0  0]
              [ 0  0  0  0  0  0  0  0]
              [ 0 :w  0 :w  0 :w  0 :w]
              [:w  0 :w  0 :w  0 :w  0]
              [ 0 :w  0 :w  0 :w  0 :w]]))
